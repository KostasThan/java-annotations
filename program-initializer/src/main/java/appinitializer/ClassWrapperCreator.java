package appinitializer;

import appinitializer.annotations.MethodInitializer;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.stream.Collectors;

public class ClassWrapperCreator {

    private ClassWrapperCreator() {
    }

    public static <T> ClassWrapper<T> createClassWrapper(Class<T> clazz) {

        Map<Integer, Method> methodMap = new TreeMap<>(Comparator.<Integer>naturalOrder().reversed());

        List<Method> unorderedMethods = new ArrayList<>();

        boolean hasMemberMethod = false;
        for (var m : clazz.getDeclaredMethods()) {

            //skip if it does not have the required annotation
            if (!m.isAnnotationPresent(MethodInitializer.class)) {
                continue;
            }

            //if we find at least one that is not static we need to keep track of that
            if (!Modifier.isStatic(m.getModifiers())) {
                hasMemberMethod = true;
            }

            //add it to the correct list, depending on if it has an assigned order other than the Default
            int order = m.getAnnotation(MethodInitializer.class).order();
            if (order == 0) {
                unorderedMethods.add(m);
            } else {
                methodMap.put(m.getAnnotation(MethodInitializer.class).order(), m);
            }

        }

        List<Method> methodsList = getMethodsListFromCollection(methodMap.values(), unorderedMethods);
        List<Collection<Class<? extends Exception>>> exceptionToHandle = getExceptionToHandle(methodsList);
        T instance = getInstance(clazz, hasMemberMethod);
        return new ClassWrapper<>(instance, methodsList, exceptionToHandle);
    }

    private static List<Collection<Class<? extends Exception>>> getExceptionToHandle(List<Method> methodsList) {
        return methodsList.stream()
                          .map(m -> m.getAnnotation(MethodInitializer.class))
                          .map(MethodInitializer::exceptionsToHandle)
                          .map(Set::of)
                          .collect(Collectors.toList());

    }

    private static List<Method> getMethodsListFromCollection(Collection<Method> firstCollection, Collection<Method> secondCollection) {
        List<Method> finalMethodsList = new ArrayList<>();
        finalMethodsList.addAll(firstCollection);
        finalMethodsList.addAll(secondCollection);
        return finalMethodsList;
    }

    private static <T> T getInstance(Class<T> clazz, boolean hasMemberMethod) {
        try {
            return hasMemberMethod ? clazz.getConstructor().newInstance() : null;
        } catch (Exception e) {
           throw new RuntimeException(e);
        }
    }

}
