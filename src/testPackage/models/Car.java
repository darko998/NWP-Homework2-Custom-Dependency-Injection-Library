package testPackage.models;

import dependencyInjectionLibrary.annotations.Autowired;
import dependencyInjectionLibrary.annotations.Component;

@Component
public class Car implements Vehicle {

    int seatingCapacity = 5;
    int maxSpeed = 250;
    double acceleration = 6.4;

    @Autowired(verbose = true)
    public Factory factory;


    public Car() {
    }


    @Override
    public int getSeatingCapacity() {
        return seatingCapacity;
    }

    @Override
    public int getMaxSpeed() {
        return maxSpeed;
    }

    public double getAcceleration() {
        return acceleration;
    }

    public void setAcceleration(int acceleration) {
        this.acceleration = acceleration;
    }

    public String getManufacturer() {
        return this.factory.name;
    }

    public String getManufacturerCity() {
        return this.factory.getCity();
    }
}
