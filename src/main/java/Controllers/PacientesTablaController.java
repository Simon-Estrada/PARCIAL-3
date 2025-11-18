package Controllers;

import Objects.Paciente;
import Repositories.PacienteRepository;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;

import java.util.Optional;

public class PacientesTablaController {

    @FXML
    private Button btnAgregar;

    @FXML
    private Button btnEliminar;

    @FXML
    private Button btnModificar;

    @FXML
    private TableColumn<Paciente, String> colCorreo;

    @FXML
    private TableColumn<Paciente, String> colId;

    @FXML
    private TableColumn<Paciente, String> colNombre;

    @FXML
    private TableColumn<Paciente, String> colTel;

    @FXML
    private TableView<Paciente> tablaPacientes;

    @FXML
    private TextField txtCorreo;

    @FXML
    private TextField txtId;

    @FXML
    private TextField txtNombre;

    @FXML
    private TextField txtTel;
    private AnchorPane panelContenido;
    private PacienteRepository pacienteRepository;
    private ObservableList<Paciente> pacientesObservable;

    @FXML
    public void initialize() {
        pacienteRepository = PacienteRepository.getInstance();
        configurarTabla();
        cargarPacientes();
        btnEliminar.setDisable(true);
        btnModificar.setDisable(true);
        tablaPacientes.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> {
                    boolean isSelected = newValue != null;
                    btnEliminar.setDisable(isSelected);
                    btnModificar.setDisable(!isSelected);
                    mostrarPaciente(newValue);
                    txtId.setDisable(isSelected);
                }
        );
    }
    private void configurarTabla() {
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colNombre.setCellValueFactory(new PropertyValueFactory<>("name"));
        colTel.setCellValueFactory(new PropertyValueFactory<>("phone"));
        colCorreo.setCellValueFactory(new PropertyValueFactory<>("email"));
    }
    private void cargarPacientes() {
        pacientesObservable = FXCollections.observableArrayList(
                pacienteRepository.getPacientes());
        tablaPacientes.setItems(pacientesObservable);
    }
    private void limpiarCampos() {
        txtId.clear();
        txtNombre.clear();
        txtTel.clear();
        txtCorreo.clear();
        txtId.setDisable(false);
        tablaPacientes.getSelectionModel().clearSelection();
    }

    @FXML
    void onAgregar() {
        if(!validarCampos()){
            return;
        } try{
            String id = txtId.getText();
            String name = txtNombre.getText();
            String phone = txtTel.getText();
            String email = txtCorreo.getText();
            Paciente paciente= new Paciente(id, name, phone, email);
            pacienteRepository.addPaciente(paciente);
            mostrarAlerta("Éxito", "paciente agregado correctamente.", Alert.AlertType.INFORMATION);
            actualizarTabla();
            limpiarCampos();
        }catch (Exception e) {
            mostrarAlerta("Error", "Error al agregar el paciente: " + e.getMessage(), Alert.AlertType.ERROR);
        }

    }
    private boolean validarCampos() {
        if(txtId.getText().isEmpty() || txtNombre.getText().isEmpty() || txtTel.getText().isEmpty()||
                txtCorreo.getText().isEmpty()) {
            mostrarAlerta("Error", "Todos los campos deben ser diligenciados obligatoriamente.", Alert.AlertType.WARNING);
            return false;
        } return true;
    }

    @FXML
    void onEliminar() {
        Paciente paciente = tablaPacientes.getSelectionModel().getSelectedItem();
        if(paciente == null){
            mostrarAlerta("Error", "Seleccione un paciente para eliminar.", Alert.AlertType.WARNING);
            return;
        }
        Alert confirmacion = new Alert(Alert.AlertType.CONFIRMATION);
        confirmacion.setTitle("Confirmar eliminación");
        confirmacion.setHeaderText("¿Estás seguro?");
        confirmacion.setContentText("¿Deseas eliminar el paciente: " + paciente.getName() + "?");

        Optional<ButtonType> resultado = confirmacion.showAndWait();
        if(resultado.isPresent() && resultado.get() == ButtonType.OK){
            try{
                pacienteRepository.removePaciente(paciente);
                cargarPacientes();
                mostrarAlerta("Exito", "paciente eliminado correctamente.", Alert.AlertType.INFORMATION);
                limpiarCampos();
            } catch (Exception e) {
                mostrarAlerta("Exito", "Error al eliminar el paciente: "+ e.getMessage(), Alert.AlertType.ERROR);
            }
        }

    }
    public void actualizarTabla() {
        cargarPacientes();
    }
    private void mostrarPaciente(Paciente paciente) {
        if(paciente != null){
            txtId.setText(paciente.getId());
            txtNombre.setText(paciente.getName());
            txtTel.setText(paciente.getPhone());
            txtCorreo.setText(paciente.getEmail());
            txtId.setDisable(true);
        } else {
            limpiarCampos();
            txtId.setDisable(false);
        }
    }

    @FXML
    void onModificar() {
        if(!validarCampos()){
            return;
        }
        Paciente paciente = tablaPacientes.getSelectionModel().getSelectedItem();
        if(paciente == null){
            mostrarAlerta("Error", "Seleccione un cliente para modificar.", Alert.AlertType.WARNING);
            return;
        }
        try{
            paciente.setName(txtNombre.getText());
            paciente.setPhone(txtTel.getText());
            paciente.setEmail(txtCorreo.getText());
            actualizarTabla();
            tablaPacientes.refresh();
            mostrarAlerta("Éxito", "Paciente modificado correctamente.", Alert.AlertType.INFORMATION);
            limpiarCampos();
        } catch (Exception e) {
            mostrarAlerta("Error", "Error al modificar el Paciente: " + e.getMessage(), Alert.AlertType.ERROR);
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
