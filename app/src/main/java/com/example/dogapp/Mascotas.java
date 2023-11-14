package com.example.dogapp;

public class Mascotas {

    String NombrePet, EdadPet,DireccionPet,GeneroPet, image, key,QR;

    public Mascotas() {
    }

    public Mascotas(String nombrePet, String edadPet, String direccionPet, String generoPet, String image, String QR) {
        this.NombrePet = nombrePet;
        this.EdadPet = edadPet;
        this.DireccionPet = direccionPet;
        this.GeneroPet = generoPet;
        this.image = image;
        this.QR = QR;
    }

    public String getQR() {
        return QR;
    }

    public void setQR(String QR) {
        this.QR = QR;
    }

    public String getNombrePet() {
        return NombrePet;
    }

    public void setNombrePet(String nombrePet) {
        this.NombrePet = nombrePet;
    }

    public String getEdadPet() {
        return EdadPet;
    }

    public void setEdadPet(String edadPet) {
        this.EdadPet = edadPet;
    }

    public String getDireccionPet() {
        return DireccionPet;
    }

    public void setDireccionPet(String direccionPet) {
        DireccionPet = direccionPet;
    }

    public String getGeneroPet() {
        return GeneroPet;
    }

    public void setGeneroPet(String generoPet) {
        GeneroPet = generoPet;
    }

    public Mascotas(String image) {
        this.image = image;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
