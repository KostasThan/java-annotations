package appinitializer;

import appinitializer.annotations.MethodInitializer;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.stream.Collectors;

public class Validator {

    public static void validateClassesAndMethods(List<Class<?>> classList) {
        classList.forEach(Validator::validate);
    }


    private static void validate(Class<?> clazz) {

        validateMethods(clazz);
        validateClass(clazz);

    }

    private static void validateClass(Class<?> clazz) {
        validateDifferentMethodOrders(clazz);
    }

    private static void validateDifferentMethodOrders(Class<?> clazz) {
        Set<Integer> orderesSeen = new HashSet<>();
        for(Method m: clazz.getDeclaredMethods()){
            if(!m.isAnnotationPresent(MethodInitializer.class)){
                continue;
            }
            int order = m.getAnnotation(MethodInitializer.class).order();

            if(isNotDefaultOrder(order) && !orderesSeen.add(order)){
                throw new RuntimeException(String.format("Duplicate order(%d) found for class %s", order, clazz.getName()));
            }
        }

    }

    private static void validateMethods(Class<?> clazz) {
        List<Method> methodsToValidate = Arrays.stream(clazz.getDeclaredMethods())
                                               .filter(m -> m.isAnnotationPresent(MethodInitializer.class))
                                               .collect(Collectors.toList());

        for (var m : methodsToValidate) {
            methodShouldTakeNoArgs(m, clazz.getName());
            methodShouldBeStaticOrClassNeedsToHaveNoArgsConstructor(clazz, m);
            methodOrderShouldBeZeroOrGreater(clazz, m);
            methodWaitBetweenInvocationShouldBeZeroOrGreater(clazz, m);
        }
    }

    private static void methodWaitBetweenInvocationShouldBeZeroOrGreater(Class<?> clazz, Method m) {
        long waitTimeBetweenInvocation = m.getAnnotation(MethodInitializer.class).msToWaitBetweenInvocations();
        if (waitTimeBetweenInvocation < 0) {
            throw new RuntimeException(
                    String.format("Method %s declared in class %s should have msToWaitBetweenInvocations of at least 0 but had %d", clazz.getName(), m.getName(), waitTimeBetweenInvocation));
        }
    }

    private static void methodOrderShouldBeZeroOrGreater(Class<?> clazz, Method m) {
        int order = m.getAnnotation(MethodInitializer.class).order();
        if (order < 0) {
            throw new RuntimeException(
                    String.format("Method %s declared in class %s should have order of at least 0 but had %d", clazz.getName(), m.getName(), order));
        }
    }

    private static void methodShouldBeStaticOrClassNeedsToHaveNoArgsConstructor(Class<?> clazz, Method m) {
        if (!Modifier.isStatic(m.getModifiers())) {

            try {
                clazz.getConstructor();
            } catch (NoSuchMethodException e) {
                throw new RuntimeException(String.format("Class %s does not have no args constructor and needs"
                        + " to call the non static method %s", clazz.getName(), m.getName()));
            }
        }
    }

    private static void methodShouldTakeNoArgs(Method m, String className) {
        if (m.getParameterCount() != 0) {
            throw new RuntimeException(String.format("Method %s declared in %s should take 0 arguments but found %d",
                    m.getName(),
                    className,
                    m.getParameterCount()));
        }
    }

    private static boolean isNotDefaultOrder(int order) {
        return order != 0;
    }
}
