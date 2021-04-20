package com.epam.mishin.scanner.impl;

import com.epam.mishin.annotation.Entity;
import com.epam.mishin.annotation.Value;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class ClassAnnotationValidator {
    private static final Logger LOGGER = Logger.getLogger(ClassAnnotationValidator.class.getName());

    public List<Class<?>> validateClasses(List<Class<?>> classList) {
        return classList.stream().map(ClassAnnotationValidator::checkClass)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());

    }

    private static Optional<Class<?>> checkClass(Class<?> aClass) {
        if (ClassAnnotationValidator.isClassEntity(aClass)) {
            isEntityCorrect(aClass);
            return Optional.of(aClass);
        }
        checkValueAnnotation(aClass);
        return Optional.empty();
    }


    private static boolean isClassEntity(Class<?> aClass) {
        return Arrays.stream(aClass.getAnnotations())
                .map(Annotation::annotationType)
                .anyMatch(aClass1 -> aClass1.equals(Entity.class));
    }


    private static void isEntityCorrect(Class<?> anEntity) {
        Field[] fields = anEntity.getFields();
        boolean isNoValueField = Arrays.stream(fields)
                .anyMatch(field -> Objects.isNull(field.getAnnotation(Value.class)));
        if (isNoValueField) {
            LOGGER.warning("There is field with no @Value annotation in " + anEntity.getSimpleName());
            return;
        }
        LOGGER.info("All fields of " + anEntity.getSimpleName() + " entity have a @Value annotations");
    }

    private static void checkValueAnnotation(Class<?> aClass) {
        Field[] fields = aClass.getFields();
        Method[] methods = aClass.getMethods();
        boolean isValueField = Arrays.stream(fields)
                .anyMatch(field -> Objects.nonNull(field.getAnnotation(Value.class)));
        boolean isValueMethod = Arrays.stream(methods)
                .filter(method -> method.getName().contains("set"))
                .anyMatch(method -> Objects.nonNull(method.getAnnotation(Value.class)));
        if (isValueField || isValueMethod) {
            LOGGER.warning("Some @Value annotation in " + aClass.getSimpleName());
            return;
        }
        LOGGER.info(aClass.getSimpleName() + " have been successfully created");
    }

}
