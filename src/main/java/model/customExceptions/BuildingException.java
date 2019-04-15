package model.customExceptions;


/**
 * BuildingException : Lancée quand il est impossible de construire sur l'emplacement testé
 * by Clément Jan 2019
 */
public class BuildingException extends Exception {
    public BuildingException() { super(); }
    public BuildingException(String message) { super(message); }
    public BuildingException(String message, Throwable cause) { super(message, cause); }
    public BuildingException(Throwable cause) { super(cause); }
}
