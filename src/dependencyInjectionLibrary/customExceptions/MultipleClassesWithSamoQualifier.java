package dependencyInjectionLibrary.customExceptions;

public class MultipleClassesWithSamoQualifier extends Exception {

    public MultipleClassesWithSamoQualifier(String errorMessage) {
        super(errorMessage);
    }
}
