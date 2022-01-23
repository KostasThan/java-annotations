package demoapp.appstartup;

import appinitializer.annotations.ClassInitializer;
import appinitializer.annotations.MethodInitializer;

@ClassInitializer
public class TwoMethodsToBeCalledInOrder {

    @MethodInitializer(order = 1)
    public String refreshData(){
        System.out.println("This method needs to be called on startup but after the db initialization");
        return "A string to be ignored";
    }

    @MethodInitializer(order = 0)
    public void initializeDb(){
        System.out.println("DB was initialized");
    }

}
