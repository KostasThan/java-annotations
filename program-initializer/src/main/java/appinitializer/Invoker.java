package appinitializer;

import java.util.List;

public class Invoker {

    private Invoker() {
    }

    public static void invokeAll(List<Class<?>> classList) {
        classList.stream()
                 .map(ClassWrapperCreator::createClassWrapper)
                 .forEach(ClassWrapper::invokeAllMethods);
    }



}
