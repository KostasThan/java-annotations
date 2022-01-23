# Simple Reflection Use-Cases

This project contains 2 projects:

1. Program-initializer
2. Proxy Design Pattern with reflection(under-construction)

## Program initializer

This module contains a simple library along with a demo client Main method. The main goal of the module is to create a simple library that runs classes
specifies with a custom annotation. A simple use case is that when application is started some services-connections needs to be established. Providing those
annotations on the said method this can happen automatically.

### How to use

1. Annotate the wanted classes-and methods.
2. Create an Initializer with one of the two available Enums.
3. Run the initializer.initialize() method.

See example in demoapp package

### Limitations

This is a **simple library** and **some of the many limitations** are listed below.

1. To find the classes the initializer iteratively reads the packages starting from the one that Initialize.class is inside*
2. Methods to be run must be static or the class must have a no args constructor
3. Methods cannot take any arguments
4. Methods must be public
5. If the method is an instance method, the instance that this method will run on is a new instance created from scratch and not any that is already created
6. All (checked!) exceptions that can be thrown due to reflection or method invocation are wrapped in a RuntimeException
7. If a method fails, then the initialization is stopped. **No rollback mechanism is available**

### Providing Features

1. The ability to scan all classes for methods annotated with MethodInitializer annotations or only classes that are annotated with the marker interface
   annotation ClassInitializer
2. The ability to retry a method a specified amount of times (until the limit is reached or it returns true**)
3. The ability to wait between retries
4. The ability to set an order for the method execution.(if not specified, they are invoked as seen in the source code***)
5. The ability to catch some exceptions the method might throw****

- *In a maven package like structure the base package is the target package that is created, meaning all the classes can be loaded. Not handling the case that
  inside the target folder may be any jars. **Have not** tested how the lib works if it is packaged.
- **If the method returns a boolean, the retries will stop upon reaching the maximum amount or true is returned. Else the retries are always the same as
  specified

- ***If order is set for at least one method, then all the methods that have an order specified will run according to that. After that all the methods that do
  not declare an order will be called in order seen in the source code.
    - Order must be greater or equal to 1 to have effect.
    - 0 means in source code order.
    - Less than 0 a runtime exception is thrown

- ****If the method can throw an exception, we can specify which exception(s) we want to catch. If any thrown exception is specified in the exceptions list,
  then the code continues as expected(with retries or moving to the next method to invoke). Else the exception is wrapped in a RuntimeException

## Proxy Design Pattern with reflection(under-construction)