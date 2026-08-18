package kamilzadroga.BudowaKosztorysu.exception;

public class BudowaKosztorysuNotFoundException extends RuntimeException{
    public BudowaKosztorysuNotFoundException(Long id) {
        super("Program nie widzi tego numeru " + id );
    }
    public BudowaKosztorysuNotFoundException(String message) {
        super(message);
    }
}
