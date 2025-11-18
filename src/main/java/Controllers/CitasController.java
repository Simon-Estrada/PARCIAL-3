package Controllers;

import Objects.DetalleCita;
import Objects.Medico;
import Objects.Paciente;
import Repositories.MedicoRepository;
import Repositories.PacienteRepository;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class CitasController {

    @FXML
    private Button btnAgendar;

    @FXML
    private ComboBox<Medico> cmbMedico;

    @FXML
    private ComboBox<Paciente> cmbPaciente;

    @FXML
    private TableColumn<DetalleCita, String> colFecha;

    @FXML
    private TableColumn<DetalleCita, String> colMedico;

    @FXML
    private TableColumn<DetalleCita, String> colPaciente;

    @FXML
    private TableColumn<DetalleCita, Double> colSubtotal;

    @FXML
    private DatePicker dateSelector;

    @FXML
    private Label lblHora;

    @FXML
    private Label lblPrecio;

    @FXML
    private TableView<DetalleCita> tablaCitas;

    private PacienteRepository pacienteRepository;
    private MedicoRepository medicoRepository;
    private ObservableList<DetalleCita> citasObservable;
    private List<DetalleCita> listaCitas;

    @FXML
    public void initialize() {
        pacienteRepository = PacienteRepository.getInstance();
        medicoRepository = MedicoRepository.getInstance();
        listaCitas = new ArrayList<>();

        configurarTabla();
        cargarComboBoxes();
        configurarListeners();

        actualizarHoraActual();

        citasObservable = FXCollections.observableArrayList();
        tablaCitas.setItems(citasObservable);
    }

    private void configurarTabla() {
        colFecha.setCellValueFactory(new PropertyValueFactory<>("fecha"));
        colPaciente.setCellValueFactory(new PropertyValueFactory<>("paciente"));
        colMedico.setCellValueFactory(new PropertyValueFactory<>("medico"));
        colSubtotal.setCellValueFactory(new PropertyValueFactory<>("subtotal"));
    }

    private void cargarComboBoxes() {
        List<Paciente> pacientes = pacienteRepository.getPacientes();
        cmbPaciente.setItems(FXCollections.observableArrayList(pacientes));
        cmbPaciente.setConverter(new javafx.util.StringConverter<Paciente>() {
            @Override
            public String toString(Paciente paciente) {
                return paciente != null ? paciente.getName() : "";
            }

            @Override
            public Paciente fromString(String string) {
                return null;
            }
        });

        List<Medico> medicos = medicoRepository.getMedicos();
        cmbMedico.setItems(FXCollections.observableArrayList(medicos));
        cmbMedico.setConverter(new javafx.util.StringConverter<Medico>() {
            @Override
            public String toString(Medico medico) {
                return medico != null ? medico.getName() : "";
            }

            @Override
            public Medico fromString(String string) {
                return null;
            }
        });
    }

    private void configurarListeners() {
        cmbMedico.setOnAction(event -> {
            Medico medico = cmbMedico.getSelectionModel().getSelectedItem();
            if (medico != null) {
                lblPrecio.setText("$" + String.format("%.2f", medico.getPrice()));
            } else {
                lblPrecio.setText("$0.00");
            }
        });
    }

    private void actualizarHoraActual() {
        LocalTime horaActual = LocalTime.now();
        lblHora.setText(horaActual.format(java.time.format.DateTimeFormatter.ofPattern("HH:mm")));
    }

    @FXML
    void onAgendar(ActionEvent event) {
        if (!validarCampos()) {
            return;
        }

        try {
            Paciente paciente = cmbPaciente.getSelectionModel().getSelectedItem();
            Medico medico = cmbMedico.getSelectionModel().getSelectedItem();
            LocalDate fecha = dateSelector.getValue();
            LocalTime hora = LocalTime.parse(lblHora.getText());

            if (existeCitaEnFechaHora(medico, fecha, hora)) {
                mostrarAlerta("Error", "El médico ya tiene una cita agendada en esa fecha y hora.", Alert.AlertType.WARNING);
                return;
            }

            DetalleCita detalleCita = new DetalleCita(
                    fecha,
                    paciente.getName(),
                    medico.getName(),
                    hora,
                    medico.getPrice()
            );

            listaCitas.add(detalleCita);
            citasObservable.add(detalleCita);

            mostrarAlerta("Éxito", "Cita agendada correctamente.", Alert.AlertType.INFORMATION);
            limpiarCampos();

        } catch (Exception e) {
            mostrarAlerta("Error", "Error al agendar la cita: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    private boolean existeCitaEnFechaHora(Medico medico, LocalDate fecha, LocalTime hora) {
        return listaCitas.stream()
                .anyMatch(c -> c.getMedico().equals(medico.getName())
                        && c.getFecha().equals(fecha.toString())
                        && c.getHora().equals(hora.toString()));
    }

    private boolean validarCampos() {
        if (cmbPaciente.getSelectionModel().getSelectedItem() == null) {
            mostrarAlerta("Error", "Debe seleccionar un paciente.", Alert.AlertType.WARNING);
            return false;
        }
        if (cmbMedico.getSelectionModel().getSelectedItem() == null) {
            mostrarAlerta("Error", "Debe seleccionar un médico.", Alert.AlertType.WARNING);
            return false;
        }
        if (dateSelector.getValue() == null) {
            mostrarAlerta("Error", "Debe seleccionar una fecha.", Alert.AlertType.WARNING);
            return false;
        }
        if (dateSelector.getValue().isBefore(LocalDate.now())) {
            mostrarAlerta("Error", "La fecha no puede ser anterior a hoy.", Alert.AlertType.WARNING);
            return false;
        }
        return true;
    }

    private void limpiarCampos() {
        cmbPaciente.getSelectionModel().clearSelection();
        cmbMedico.getSelectionModel().clearSelection();
        dateSelector.setValue(null);
        lblPrecio.setText("$0.00");
        actualizarHoraActual();
    }

    private void mostrarAlerta(String titulo, String mensaje, Alert.AlertType tipo) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}