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

    @Override
    public List<Class<?>> scanPackage(String directory) {
        if (directory.isEmpty() || directory.isBlank()) {
            LOGGER.log(Level.WARNING, "Directory is empty");
            return Collections.emptyList();
        }

        File pojos = new File(directory);
        if (!pojos.isDirectory()) {
            LOGGER.log(Level.WARNING, "Path is not a directory");
            return Collections.emptyList();
        }
        String packageName = "com.epam.mishin.pojo.";

        return Arrays.stream(Objects.requireNonNull(pojos.list(), "Pojos has no elements")).map(pojoName ->   packageName + pojoName.replace(".java", ""))
                .map(PackageScannerImpl::createClassByName)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());
    }

    private static Optional<Class<?>> createClassByName(String className) {
        try {
            return Optional.of(Class.forName(className));
        } catch (java.lang.ClassNotFoundException e) {
            LOGGER.log(Level.WARNING, e, () -> "Class was not found by name " + className);
        }
        return Optional.empty();
    }
}
