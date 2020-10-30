package dependencyInjectionLibrary.customExceptions;

public class ClassHasNoAnnotationForInject extends Exception {

    public ClassHasNoAnnotationForInject(String errorMessage) {
        super(errorMessage);
    }
}
