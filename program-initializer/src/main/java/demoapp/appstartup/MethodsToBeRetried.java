package demoapp.appstartup;

import appinitializer.annotations.ClassInitializer;
import appinitializer.annotations.MethodInitializer;
import java.time.LocalDateTime;
import java.util.Random;


@ClassInitializer
public class MethodsToBeRetried {

    @MethodInitializer(repeatTimes = 20)
    public boolean registerServices() {
        Random rand = new Random();

        if (rand.nextInt(10) < 2) {
            System.out.println("Services register successfully. Timestamp: " + LocalDateTime.now());
            return true;
        } else {
            System.out.println("Unable to register services, retrying cause of returning false");
            return false;
        }
    }

    @MethodInitializer(repeatTimes = 20,
            exceptionsToHandle = RuntimeException.class,
            msToWaitBetweenInvocations = 1)
    public boolean registerServices2() {
        Random rand = new Random();

        if (rand.nextInt(10) < 2) {
            System.out.println("Other services register successfully. Timestamp: " + LocalDateTime.now());
            return true;
        } else {
            throw new RuntimeException("exception thrown!!\n");
        }
    }
}
