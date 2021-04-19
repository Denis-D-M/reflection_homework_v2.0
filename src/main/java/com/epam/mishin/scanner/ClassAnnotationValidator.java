package com.epam.mishin.scanner;

import java.util.List;

public interface ClassAnnotationValidator {
    List<Class<?>> validateClasses(List<Class<?>> classList);
}
