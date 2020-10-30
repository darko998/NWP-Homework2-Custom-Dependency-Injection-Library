package dependencyInjectionLibrary.customExceptions;

public class NoBeanWithSpecificQualifier extends Exception {

    public NoBeanWithSpecificQualifier(String errorMessage) {
        super(errorMessage);
    }
}
