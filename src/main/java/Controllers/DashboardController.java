package Controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;

import java.io.IOException;

public class DashboardController {

    @FXML
    private Button btnCitas;

    @FXML
    private Button btnMedicos;

    @FXML
    private Button btnPacientes;

    @FXML
    private BorderPane mainBorderPane;

    @FXML
    private AnchorPane panelContenido;

    @FXML
    void onCitas() throws IOException {
        AnchorPane panelCitas = FXMLLoader.load(
                getClass().getResource("/com/example/parcial3/Citas.fxml"));
        panelContenido.getChildren().clear();
        panelContenido.getChildren().add(panelCitas);

    }

    @FXML
    void onMedicos() throws IOException {
        AnchorPane panelMedicos = FXMLLoader.load(
                getClass().getResource("/com/example/parcial3/MedicosTabla.fxml"));
        panelContenido.getChildren().clear();
        panelContenido.getChildren().add(panelMedicos);

    }

    @FXML
    void onPacientes() throws IOException {
        AnchorPane panelPacientes = FXMLLoader.load(
                getClass().getResource("/com/example/parcial3/PacientesTabla.fxml"));
        panelContenido.getChildren().clear();
        panelContenido.getChildren().add(panelPacientes);

    }

}
