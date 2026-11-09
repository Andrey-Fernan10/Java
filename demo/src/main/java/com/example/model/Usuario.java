package com.example.model;

public class Usuario {

    private String nome;
    private String documento;

    public Usuario(String nome, String documento) {
        this.nome = nome;
        this.documento = documento;
    }

    public String getNome() {
         return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

       public void setDocumento(String documento) {
        this.documento = documento;
    }


    public String getDocumento() { return documento;}

    public void exibirDados() {
        System.out.println(nome + " - " + documento);
    }

}