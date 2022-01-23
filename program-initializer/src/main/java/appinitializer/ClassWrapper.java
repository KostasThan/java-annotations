package appinitializer;

import appinitializer.annotations.MethodInitializer;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.List;

class ClassWrapper<T> {

    private final T instance;

    private final List<Method> methodList;

    private final List<Collection<Class<? extends Exception>>> exceptionsToHandle;

    private final int methodsCount;

    public ClassWrapper(T instance, List<Method> methodList,
            List<Collection<Class<? extends Exception>>> exceptionsToHandle) {
        this.instance = instance;
        this.methodList = methodList;
        this.exceptionsToHandle = exceptionsToHandle;
        methodsCount = methodList.size();
    }


    public void invokeAllMethods() {
        for (int i = 0; i < methodsCount; i++) {
            invokeSingleMethodWithRetry(methodList.get(i), exceptionsToHandle.get(i));
        }
    }

    private void invokeSingleMethodWithRetry(Method m, Collection<Class<? extends Exception>> exceptionsToHandle) {
        int repeatTimes = m.getAnnotation(MethodInitializer.class).repeatTimes();
        for (int j = 0; j < repeatTimes; j++) {
            if (invokeSingleMethod(m, exceptionsToHandle)) {
                break;
            }
            sleepForSpecifiedTime(m);
        }
    }

    private void sleepForSpecifiedTime(Method m) {
        try {
            Thread.sleep(m.getAnnotation(MethodInitializer.class).msToWaitBetweenInvocations());
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private boolean invokeSingleMethod(Method m, Collection<Class<? extends Exception>> exceptionsToHandle) {
        try {
            Object returnValue = m.invoke(instance);
            return returnValue instanceof Boolean && (Boolean) returnValue;
        } catch (InvocationTargetException e) {
            if (exceptionsToHandle.contains(e.getTargetException().getClass())) {
                System.out.printf("A handled exception was thrown with message %s", e.getTargetException().getMessage());
                return false;
            } else {
                throw new RuntimeException(e.getTargetException());
            }
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
}