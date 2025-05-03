package com.videogames.videogames.Exception;

public class QuantitaInsufficenteException extends  RuntimeException{

    private final int disponibilitaMagazzino;

    public QuantitaInsufficenteException (int disponibilitaMagazzino){
        super("Quantità insufficente in magazzino");
        this.disponibilitaMagazzino = disponibilitaMagazzino;
    }

    public int getDisponibilitaMagazzino() {
        return disponibilitaMagazzino;
    }
}
