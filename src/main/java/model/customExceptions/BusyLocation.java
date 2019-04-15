package model.customExceptions;

/**
 * BusyLocation : Lancée lorsque l'emplacement de construction testé
 * est déjà occupé par un autre joueur
 * by Clément Jan 2019
 */
public class BusyLocation extends Exception {
    public BusyLocation() { super(); }
    public BusyLocation(String message) { super(message); }
    public BusyLocation(String message, Throwable cause) { super(message, cause); }
    public BusyLocation(Throwable cause) { super(cause); }
}
