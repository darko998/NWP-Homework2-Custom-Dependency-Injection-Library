package testPackage;

import dependencyInjectionLibrary.DIEngine;

public class Main {

    public static void main(String[] args) {

        TestClass tc = new TestClass();

        // Here we activate DIEngine
        DIEngine.scanAllClasses(tc);

        // Here we test injection. TestClass has autowired and qualifier fields which DIEngine inject
        tc.testMethod();
    }

}
