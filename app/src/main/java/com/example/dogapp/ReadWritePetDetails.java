package com.example.dogapp;

public class ReadWritePetDetails {
    public String NombrePet, EdadPet, DireccionPet, GeneroPet;

    public ReadWritePetDetails(String textNombreMascota, String textEdadMascota, String textDireccion,String generoMascota){
        this.NombrePet = textNombreMascota;
        this.EdadPet = textEdadMascota;
        this.DireccionPet = textDireccion;
        this.GeneroPet = generoMascota;
    }
}
