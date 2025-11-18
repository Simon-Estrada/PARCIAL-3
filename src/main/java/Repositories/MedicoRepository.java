package Repositories;

import Objects.Medico;

import java.util.ArrayList;

public class MedicoRepository {
    private static MedicoRepository instance;
    private ArrayList<Medico> medicos;
    private MedicoRepository() {
        medicos = new ArrayList<>();
        showExamples();
    }
    public static MedicoRepository getInstance() {
        if(instance == null){
            instance = new MedicoRepository();
        }
        return instance;
    }
    private void showExamples(){
        medicos.add(new Medico("1090275087", "Simon Lopez", 500000,
                "wooseoksimon@gmail.com"));
    }
    public ArrayList<Medico> getMedicos(){
        return medicos;
    }
    public void addMedico(Medico medico){
        medicos.add(medico);
    }
    public void removeMedico(Medico medico){
        medicos.remove(medico);
    }
}
