package testPackage;

import dependencyInjectionLibrary.annotations.Autowired;
import dependencyInjectionLibrary.annotations.Qualifier;
import testPackage.models.Car;
import testPackage.models.Vehicle;

public class TestClass {

    @Autowired(verbose = true)
    private Car car1;

    @Autowired(verbose = true)
    private Car car2;

    @Qualifier("boat")
    private Vehicle boat1;

    @Qualifier("truck")
    private Vehicle truck1;

    private int testField;

    public TestClass() {
    }

    public void testMethod() {

        System.out.println("----------------------------------------------------------------");

        System.out.println("Car1 acceleration is " + car1.getAcceleration() + "s up to 100km/h");

        System.out.println("Car1 manufacturer is " + car1.getManufacturer());

        System.out.println("Car1 manufacturer city is " + car1.getManufacturerCity());

        System.out.println("----------------------------------------------------------------");

        System.out.println("Car2 acceleration is " + car2.getAcceleration() + "s up to 100km/h");

        System.out.println("----------------------------------------------------------------");

        System.out.println("Boat max speed is " + boat1.getMaxSpeed());

        System.out.println("----------------------------------------------------------------");

        System.out.println("Truck seating capacity is " + truck1.getSeatingCapacity());
    }

}
