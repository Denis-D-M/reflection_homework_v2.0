package com.epam.mishin.scanner;

import java.lang.annotation.Annotation;

public interface ClassAnnotationScanner {
    boolean isAnnotationOnClass(Class<?> aClass, Class<? extends Annotation> annotationClass);
}
