package demoapp.appstartup;

import appinitializer.annotations.ClassInitializer;
import appinitializer.annotations.MethodInitializer;

@ClassInitializer
public class SimpleCase {

    @MethodInitializer
    public static void loadConfigs(){
        System.out.println("Simple case. Class and method with initializer");
    }
}
