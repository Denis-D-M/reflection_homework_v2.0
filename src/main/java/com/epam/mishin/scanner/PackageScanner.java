package com.epam.mishin.scanner;

import java.util.List;

public interface PackageScanner {
    List<Class<?>> scanPackage(String directory);
}
