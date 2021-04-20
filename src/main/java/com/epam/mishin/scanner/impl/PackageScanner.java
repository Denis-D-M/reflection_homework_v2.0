package com.epam.mishin.scanner.impl;

import com.epam.mishin.instance.impl.InstanceCreator;

import java.io.File;
import java.util.*;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class PackageScanner {
    private static final Logger LOGGER = Logger.getLogger(InstanceCreator.class.getName());
    private static final String PACKAGE_NAME = "com.epam.mishin.pojo.";

    public List<Class<?>> scanPackage(String directory) {
        File pojos = new File(directory);
        if (!pojos.isDirectory()) {
            LOGGER.warning("Path is not a directory");
            return Collections.emptyList();
        }
        return Arrays.stream(Objects.requireNonNull(pojos.list(), "Pojos has no elements"))
                .map(pojoName -> PACKAGE_NAME + pojoName.replace(".java", ""))
                .map(PackageScanner::createByClassName)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());
    }

    private static Optional<Class<?>> createByClassName(String className) {
        try {
            return Optional.of(Class.forName(className));
        } catch (java.lang.ClassNotFoundException e) {
            LOGGER.warning("Class was not found by name " + className);
        }
        return Optional.empty();
    }
}
