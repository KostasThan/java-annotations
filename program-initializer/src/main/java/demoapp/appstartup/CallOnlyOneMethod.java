package demoapp.appstartup;

import appinitializer.annotations.ClassInitializer;
import appinitializer.annotations.MethodInitializer;

@ClassInitializer
public class CallOnlyOneMethod {

    @MethodInitializer
    public void initiateCache(){
        System.out.println("Called the correct method from CallOnlyOneMethod");
    }

    public void unrelatedMethod(){
        System.out.println("Called the wrong method from CallOnlyOneMethod");
    }
}
