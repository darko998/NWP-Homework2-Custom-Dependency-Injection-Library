package testPackage.models;

import dependencyInjectionLibrary.annotations.Bean;
import dependencyInjectionLibrary.annotations.Qualifier;

@Qualifier(value = "boat")
@Bean
public class Boat implements Vehicle {
    int seatingCapacity;
    int maxSpeed = 90;
    int length;

    @Override
    public int getSeatingCapacity() {
        return seatingCapacity;
    }

    @Override
    public int getMaxSpeed() {
        return maxSpeed;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }
}
