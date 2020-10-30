package testPackage.models;

import dependencyInjectionLibrary.annotations.Bean;
import dependencyInjectionLibrary.annotations.Qualifier;

@Qualifier("truck")
@Bean
public class Truck implements Vehicle{

    int seatingCapacity = 2;
    int maxSpeed = 90;
    int pullingPower = 100;

    @Override
    public int getSeatingCapacity() {
        return seatingCapacity;
    }

    @Override
    public int getMaxSpeed() {
        return maxSpeed;
    }

    public int getPullingPower() {
        return pullingPower;
    }

    public void setPullingPower(int pullingPower) {
        this.pullingPower = pullingPower;
    }
}
