package Controllers;

import Objects.Medico;
import Repositories.MedicoRepository;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.Optional;

public class MedicosTablaController {

    @FXML
    private Button btnAgregar;

    @FXML
    private Button btnEliminar;

    @FXML
    private Button btnModificar;

    @FXML
    private TableColumn<Medico, String> colCorreo;

    @FXML
    private TableColumn<Medico, String> colId;

    @FXML
    private TableColumn<Medico, String> colNombre;

    @FXML
    private TableColumn<Medico, Double> colPrecio;

    @FXML
    private TableView<Medico> tablaMedicos;

    @FXML
    private TextField txtCorreo;

    @FXML
    private TextField txtId;

    @FXML
    private TextField txtNombre;

    @FXML
    private TextField txtPrecio;

    private MedicoRepository medicoRepository;
    private ObservableList<Medico> medicosObservable;

    @FXML
    public void initialize() {
        medicoRepository = MedicoRepository.getInstance();
        configurarTabla();
        cargarMedicos();
        btnEliminar.setDisable(true);
        btnModificar.setDisable(true);
        tablaMedicos.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> {
                    boolean isSelected = newValue != null;
                    btnEliminar.setDisable(!isSelected);
                    btnModificar.setDisable(!isSelected);
                    mostrarMedico(newValue);
                    txtId.setDisable(isSelected);
                }
        );
    }

    private void configurarTabla() {
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colCorreo.setCellValueFactory(new PropertyValueFactory<>("correo"));
        colPrecio.setCellValueFactory(new PropertyValueFactory<>("precio"));
    }

    private void cargarMedicos() {
        medicosObservable = FXCollections.observableArrayList(
                medicoRepository.getMedicos());
        tablaMedicos.setItems(medicosObservable);
    }

    private void limpiarCampos() {
        txtId.clear();
        txtNombre.clear();
        txtCorreo.clear();
        txtPrecio.clear();
        txtId.setDisable(false);
        tablaMedicos.getSelectionModel().clearSelection();
    }

    @FXML
    void onAgregar(ActionEvent event) {
        if (!validarCampos()) {
            return;
        }
        try {
            String id = txtId.getText();
            String name = txtNombre.getText();
            String email = txtCorreo.getText();
            double price = Double.parseDouble(txtPrecio.getText());

            Medico medico = new Medico(id, name, price, email);
            medicoRepository.addMedico(medico);
            mostrarAlerta("Éxito", "Médico agregado correctamente.", Alert.AlertType.INFORMATION);
            actualizarTabla();
            limpiarCampos();
        } catch (NumberFormatException e) {
            mostrarAlerta("Error", "El precio debe ser un número válido.", Alert.AlertType.ERROR);
        } catch (Exception e) {
            mostrarAlerta("Error", "Error al agregar el médico: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    private boolean validarCampos() {
        if (txtId.getText().isEmpty() || txtNombre.getText().isEmpty() ||
                txtCorreo.getText().isEmpty() || txtPrecio.getText().isEmpty()) {
            mostrarAlerta("Error", "Todos los campos deben ser diligenciados obligatoriamente.", Alert.AlertType.WARNING);
            return false;
        }
        return true;
    }

    @FXML
    void onEliminar(ActionEvent event) {
        Medico medico = tablaMedicos.getSelectionModel().getSelectedItem();
        if (medico == null) {
            mostrarAlerta("Error", "Seleccione un médico para eliminar.", Alert.AlertType.WARNING);
            return;
        }

        Alert confirmacion = new Alert(Alert.AlertType.CONFIRMATION);
        confirmacion.setTitle("Confirmar eliminación");
        confirmacion.setHeaderText("¿Estás seguro?");
        confirmacion.setContentText("¿Deseas eliminar el médico: " + medico.getName() + "?");

        Optional<ButtonType> resultado = confirmacion.showAndWait();
        if (resultado.isPresent() && resultado.get() == ButtonType.OK) {
            try {
                medicoRepository.removeMedico(medico);
                cargarMedicos();
                mostrarAlerta("Éxito", "Médico eliminado correctamente.", Alert.AlertType.INFORMATION);
                limpiarCampos();
            } catch (Exception e) {
                mostrarAlerta("Error", "Error al eliminar el médico: " + e.getMessage(), Alert.AlertType.ERROR);
            }
        }
    }

    @FXML
    void onModificar(ActionEvent event) {
        if (!validarCampos()) {
            return;
        }
        Medico medico = tablaMedicos.getSelectionModel().getSelectedItem();
        if (medico == null) {
            mostrarAlerta("Error", "Seleccione un médico para modificar.", Alert.AlertType.WARNING);
            return;
        }
        try {
            medico.setName(txtNombre.getText());
            medico.setEmail(txtCorreo.getText());
            medico.setPrice(Double.parseDouble(txtPrecio.getText()));
            actualizarTabla();
            tablaMedicos.refresh();
            mostrarAlerta("Éxito", "Médico modificado correctamente.", Alert.AlertType.INFORMATION);
            limpiarCampos();
        } catch (NumberFormatException e) {
            mostrarAlerta("Error", "El precio debe ser un número válido.", Alert.AlertType.ERROR);
        } catch (Exception e) {
            mostrarAlerta("Error", "Error al modificar el médico: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    public void actualizarTabla() {
        cargarMedicos();
    }

    private void mostrarMedico(Medico medico) {
        if (medico != null) {
            txtId.setText(medico.getId());
            txtNombre.setText(medico.getName());
            txtCorreo.setText(medico.getEmail());
            txtPrecio.setText(String.valueOf(medico.getPrice()));
            txtId.setDisable(true);
        } else {
            limpiarCampos();
            txtId.setDisable(false);
        }
    }

    private void mostrarAlerta(String titulo, String mensaje, Alert.AlertType tipo) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}