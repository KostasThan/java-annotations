package appinitializer;

import appinitializer.annotations.ClassInitializer;
import java.util.function.Function;

public enum ClassDiscoveryEnum implements Function<Class<?>, Boolean> {

    ALL_CLASSES(clazz -> true),
    ANNOTATED_CLASSES(clazz -> clazz.isAnnotationPresent(ClassInitializer.class));

    private final Function<Class<?>, Boolean> filter;

    ClassDiscoveryEnum(Function<Class<?>, Boolean> filter) {
        this.filter = filter;
    }

    @Override
    public Boolean apply(Class aClass) {
        return filter.apply(aClass);
    }

    public Function<Class<?>, Boolean> getFilter() {
        return filter;
    }

}