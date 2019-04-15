package model.customExceptions;

/**
 * PlateauLockedException : Lancée quand il est impossible de construire sur le plateau voulu
 * by Clément Jan 2019
 */
public class PlateauLockedException extends Exception {
    public PlateauLockedException() { super(); }
    public PlateauLockedException(String message) { super(message); }
    public PlateauLockedException(String message, Throwable cause) { super(message, cause); }
    public PlateauLockedException(Throwable cause) { super(cause); }

}
