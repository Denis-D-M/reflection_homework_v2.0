package com.epam.mishin.scanner.impl;

import com.epam.mishin.annotation.Entity;
import com.epam.mishin.scanner.ClassAnnotationScanner;

import java.lang.annotation.Annotation;
import java.util.Arrays;

public class ClassAnnotationScannerImpl implements ClassAnnotationScanner {

    @Override
    public boolean isClassEntity(Class<?> aClass) {
        return Arrays.stream(aClass.getAnnotations())
                .map(Annotation::annotationType)
                .anyMatch(aClass1 -> aClass1.equals(Entity.class));
    }
}
