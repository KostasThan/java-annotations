package appinitializer;

import java.util.List;
import java.util.stream.Collectors;

public class Invoker {

    private Invoker(){}

    public static void invokeAll(List<Class<?>> classList) {

        List<ClassWrapper<?>> classWrappers = classList.stream()
                                                       .map(ClassWrapperCreator::createClassWrapper)
                                                       .collect(Collectors.toList());

        classWrappers.forEach(ClassWrapper::invokeAllMethods);
    }



}
