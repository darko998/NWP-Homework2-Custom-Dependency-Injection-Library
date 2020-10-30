package dependencyInjectionLibrary.customExceptions;

public class ClassHasNoAnnotationForInjecttion extends Exception {

    public ClassHasNoAnnotationForInjecttion(String errorMessage) {
        super(errorMessage);
    }
}
