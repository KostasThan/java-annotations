package appinitializer;

import java.util.List;

/*
    From here we will perform the various initializations
    using java reflection as well as custom annotations

 */
public class Initializer {

    private final ClassDiscoveryEnum classDiscovery;

    public Initializer() {
        this(ClassDiscoveryEnum.ANNOTATED_CLASSES);
    }

    public Initializer(ClassDiscoveryEnum classDiscovery) {
        this.classDiscovery = classDiscovery;
    }

    public void initialize(){
        List<Class<?>> classes = ClassFinder.findClasses(classDiscovery);

        Validator.validateClassesAndMethods(classes);

        Invoker.invokeAll(classes);

    }
}
