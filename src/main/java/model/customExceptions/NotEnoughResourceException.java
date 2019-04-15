package model.customExceptions;


/**
 * NotEnoughResourceException : Lancée lorsque le joueur n'a pas assez de ressources
 * pour effectuer l'action
 * by Clément Jan 2019
 */
public class NotEnoughResourceException extends Exception {

    private String nameJoueur;

    public NotEnoughResourceException() { super(); }
    public NotEnoughResourceException(String message) { super(message); }
    public NotEnoughResourceException(String message, String nameJoueur) {
        super(message);
        this.nameJoueur = nameJoueur;
    }
    public NotEnoughResourceException(String message, Throwable cause) { super(message, cause); }
    public NotEnoughResourceException(Throwable cause) { super(cause); }

    public String getNameJoueur() {
        return nameJoueur;
    }
}
