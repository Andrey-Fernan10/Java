package com.example.model;

public class Funcionario extends Usuario {
    
    private String matricula;

    public Funcionario() {
        super("", "");
    }

    public Funcionario(String nome, String documento, String matricula) {
        super(nome, documento); 
        this.matricula = matricula; 
    }
    public String getMatricula() {
        return matricula;
    }

    public void setMatricula(String matricula) {
        this.matricula = matricula;
    }
}
