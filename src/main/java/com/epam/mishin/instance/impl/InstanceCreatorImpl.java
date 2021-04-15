package com.epam.mishin.instance.impl;

import com.epam.mishin.instance.InstanceCreator;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class InstanceCreatorImpl implements InstanceCreator {
    private static final Logger LOGGER = Logger.getLogger(InstanceCreatorImpl.class.getName());

    public List<Object> createFromDirectory(String directory) {
        if (directory.isEmpty() || directory.isBlank()) {
            LOGGER.log(Level.WARNING, "Directory is empty");
            return Collections.emptyList();
        }

        File pojos = new File(directory);
        if (!pojos.isDirectory()) {
            LOGGER.log(Level.WARNING, "Path is not a directory");
            return Collections.emptyList();
        }
        String packageName = directory.replaceAll("src/main/java/", "").replace('/','.') + ".";

        return Arrays.stream(pojos.list()).map(pojoName ->   packageName + pojoName.replace(".java", ""))
                .map(InstanceCreatorImpl::createClassByName)
                .filter(Optional::isPresent)
                .map(o -> createObjectFromClass(o.get()))
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

    private static Optional<Object> createObjectFromClass(Class<?> aClass) {
        try {
            return Optional.of(aClass.getConstructor().newInstance());
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            LOGGER.log(Level.WARNING, "Object of " + aClass.getName() + " class has not been created");
        }
        return Optional.empty();
    }

}