package dependencyInjectionLibrary.customExceptions;

public class InterfaceAnnotatedWithAutowired extends Exception{

    public InterfaceAnnotatedWithAutowired(String errorMessage) {
        super(errorMessage);
    }
}
