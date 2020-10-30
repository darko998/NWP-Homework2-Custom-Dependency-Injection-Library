package dependencyInjectionLibrary.customExceptions;

public class AnnotationQualifierCouldOnlyAnnotateInterface extends Exception {

    public AnnotationQualifierCouldOnlyAnnotateInterface(String errorMessage) {
        super(errorMessage);
    }
}
