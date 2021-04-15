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

    public List<Object> createObjects(List<Class<?>> classList) {

        return classList.stream()
                .map(InstanceCreatorImpl::createObjectFromClass)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());
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