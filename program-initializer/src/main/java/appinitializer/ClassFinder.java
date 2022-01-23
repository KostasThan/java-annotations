package appinitializer;

import static appinitializer.ClassDiscoveryEnum.ALL_CLASSES;
import static appinitializer.ClassDiscoveryEnum.ANNOTATED_CLASSES;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public class ClassFinder {

    private static int ENDING_POINT_OFFSET = ".class".length();

    private static String STARTING_REGEX = "\\target\\classes\\";

    private static int OFFSET_FROM_STARTING_REGEX = STARTING_REGEX.length();


    public static List<Class<?>> findClasses(ClassDiscoveryEnum classDiscoveryEnum) {
        switch (classDiscoveryEnum) {
            case ALL_CLASSES:
                return findFilteredClasses(ALL_CLASSES.getFilter());
            case ANNOTATED_CLASSES:
                return findFilteredClasses(ANNOTATED_CLASSES.getFilter());
        }

        throw new RuntimeException("This should never happen");
    }


    private static List<Class<?>> findFilteredClasses(Function<Class<?>, Boolean> classFilter) {

        List<String> foundClassNames =
                findAllClassNames(Path.of(ClassFinder.class.getProtectionDomain().getCodeSource().getLocation().toString().substring(6)));

        return foundClassNames.stream()
                              .map(ClassFinder::getClassFromFile)
                              .filter(classFilter::apply)
                              .collect(Collectors.toList());

    }

    private static List<String> findAllClassNames(Path filePath) {

        checkPackageIsPresentAndAccessible(filePath);

        if (Files.isRegularFile(filePath)) {
            if (filePath.getFileName().toString().endsWith(".class")) {
                return List.of(getQualifyingName(filePath));
            } else
                return Collections.emptyList();
        }

        List<String> results = new ArrayList<>();
        List<Path> filesFound = getFilesFromDirectory(filePath);

        for (var f : filesFound) {
            if (Files.isDirectory(f)) {
                results.addAll(findAllClassNames(f));
            } else if (Files.isRegularFile(f) && f.getFileName().toString().endsWith(".class")) {
                results.add(getQualifyingName(f));
            }
        }

        return results;
    }

    private static List<Path> getFilesFromDirectory(Path filePath) {
        try {
            return Files.list(filePath)
                        .collect(Collectors.toList());

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    private static Class<?> getClassFromFile(String className) {

        try {
            return Class.forName(className);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    private static String getQualifyingName(Path f) {
        String s = f.toAbsolutePath().toString();

        int firstAppearance = s.indexOf(STARTING_REGEX);
        int startingPoint = firstAppearance + OFFSET_FROM_STARTING_REGEX;

        int endingPoint = s.length() - ENDING_POINT_OFFSET;

        String substring = s.substring(startingPoint, endingPoint);
        substring = substring.replace("\\", ".");

        return substring;

    }

    private static void checkPackageIsPresentAndAccessible(Path packagePath) {
        try {
            if (!Files.exists(packagePath)) {
                throw new RuntimeException(String.format("Package was not found %s", packagePath.toAbsolutePath().toString()));
            }
        } catch (SecurityException e) {
            throw new RuntimeException(String.format("Package %s could not be accessed", packagePath.toAbsolutePath()));
        }
    }



}
