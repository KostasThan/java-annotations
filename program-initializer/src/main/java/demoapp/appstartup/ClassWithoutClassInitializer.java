package demoapp.appstartup;

import appinitializer.annotations.MethodInitializer;

public class ClassWithoutClassInitializer {

    @MethodInitializer
    public void anotherMethod(){
        System.out.println("method from a class without class initializer");
    }
}
