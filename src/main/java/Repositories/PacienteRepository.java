package Repositories;

import Objects.Paciente;

import java.util.ArrayList;

public class PacienteRepository {
    private static PacienteRepository instance;
    private ArrayList<Paciente> pacientes;
    private PacienteRepository() {
        pacientes = new ArrayList<>();
        showExamples();
    }
    public static PacienteRepository getInstance() {
        if(instance == null){
            instance = new PacienteRepository();
        }
        return instance;
    }
    private void showExamples(){
        pacientes.add(new Paciente("1090275087", "Simon Lopez", "3207411485",
                "wooseoksimon@gmail.com"));
    }
    public ArrayList<Paciente> getPacientes(){
        return pacientes;
    }
    public void addPaciente(Paciente paciente){
        pacientes.add(paciente);
    }
    public void removePaciente(Paciente paciente){
        pacientes.remove(paciente);
    }
}
