package appinitializer.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface MethodInitializer {
    int repeatTimes() default 1;
    int order() default 0;
    long msToWaitBetweenInvocations() default 100;
    Class<? extends Exception>[] exceptionsToHandle() default {};
}
