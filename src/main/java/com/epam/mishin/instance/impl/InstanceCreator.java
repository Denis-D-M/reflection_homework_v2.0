package com.epam.mishin.instance.impl;

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class InstanceCreator {
    private static final Logger LOGGER = Logger.getLogger(InstanceCreator.class.getName());

    public List<Object> createObjects(List<Class<?>> classList) {

        return classList.stream()
                .map(InstanceCreator::createObjectFromClass)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());
    }

    private static Optional<Object> createObjectFromClass(Class<?> aClass) {
        try {
            return Optional.of(aClass.getConstructor().newInstance());
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            LOGGER.warning("Object of " + aClass.getName() + " class has not been created");
        }
        return Optional.empty();
    }

}