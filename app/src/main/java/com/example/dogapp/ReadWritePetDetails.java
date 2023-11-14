package com.example.dogapp;

public class ReadWritePetDetails {
    public String NombrePet, EdadPet, DireccionPet, GeneroPet, image,QR;

    public ReadWritePetDetails(){}


    public ReadWritePetDetails(String textNombreMascota, String textEdadMascota, String textDireccion, String generoMascota, String image){
        this.NombrePet = textNombreMascota;
        this.EdadPet = textEdadMascota;
        this.DireccionPet = textDireccion;
        this.GeneroPet = generoMascota;
        this.image = image;
    }

    public ReadWritePetDetails(String textNombreMascota, String textEdadMascota, String textDireccion, String generoMascota, String image, String qr) {
        this.NombrePet = textNombreMascota;
        this.EdadPet = textEdadMascota;
        this.DireccionPet = textDireccion;
        this.GeneroPet = generoMascota;
        this.image = image;
        this.QR = qr;
    }
}
