package demoapp.main;

import appinitializer.ClassDiscoveryEnum;
import appinitializer.Initializer;

public class Main {

    /*
        Here is the entry point of the demo app.
        From here we will use the custom library
        to set up the app's environment.
     */

    public static void main(String[] args) {

        Initializer initializer = new Initializer(ClassDiscoveryEnum.ANNOTATED_CLASSES);
//        Initializer initializer = new Initializer(ClassDiscoveryEnum.ALL_CLASSES);


        initializer.initialize();
    }





}
