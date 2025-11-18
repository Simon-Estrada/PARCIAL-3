package Objects;

import java.time.LocalDate;
import java.time.LocalTime;

public class DetalleCita {
    private String fecha;
    private String paciente;
    private String medico;
    private String hora;
    private double subtotal;

    public DetalleCita(LocalDate fecha, String paciente, String medico, LocalTime hora, Double subtotal) {
        this.fecha = fecha.toString();
        this.paciente = paciente;
        this.medico = medico;
        this.hora = hora.toString();
        this.subtotal = subtotal;
    }

    public String getFecha() {
        return fecha;
    }

    public String getPaciente() {
        return paciente;
    }

    public String getMedico() {
        return medico;
    }

    public String getHora() {
        return hora;
    }

    public double getSubtotal() {
        return subtotal;
    }
}