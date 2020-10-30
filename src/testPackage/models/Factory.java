package testPackage.models;

import dependencyInjectionLibrary.annotations.Autowired;
import dependencyInjectionLibrary.annotations.Component;

@Component
public class Factory {

    public String name;
    public String address;

    @Autowired
    private City city;

    public Factory() {
        name = "BMW";
        address = "Street 1";
    }

    public String getCity() {
        return city.name;
    }
}
