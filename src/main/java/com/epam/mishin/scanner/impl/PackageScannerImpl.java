package com.epam.mishin.scanner.impl;

import com.epam.mishin.instance.impl.InstanceCreatorImpl;
import com.epam.mishin.scanner.PackageScanner;

import java.io.File;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class PackageScannerImpl implements PackageScanner {
    private static final Logger LOGGER = Logger.getLogger(InstanceCreatorImpl.class.getName());
    private static final String PACKAGE_NAME = "com.epam.mishin.pojo.";

    @Override
    public List<Class<?>> scanPackage(String directory) {
        File pojos = new File(directory);
        if (!pojos.isDirectory()) {
            LOGGER.log(Level.WARNING, "Path is not a directory");
            return Collections.emptyList();
        }
        return Arrays.stream(Objects.requireNonNull(pojos.list(), "Pojos has no elements"))
                .map(pojoName ->   PACKAGE_NAME + pojoName.replace(".java", ""))
                .map(PackageScannerImpl::createByClassName)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());
    }

    private static Optional<Class<?>> createByClassName(String className) {
        try {
            return Optional.of(Class.forName(className));
        } catch (java.lang.ClassNotFoundException e) {
            LOGGER.log(Level.WARNING, e, () -> "Class was not found by name " + className);
        }
        return Optional.empty();
    }
}
