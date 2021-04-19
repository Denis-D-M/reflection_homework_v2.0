package com.epam.mishin;

import com.epam.mishin.injector.ValueAnnotationInjector;
import com.epam.mishin.instance.InstanceCreator;
import com.epam.mishin.instance.impl.InstanceCreatorImpl;
import com.epam.mishin.scanner.ClassAnnotationValidator;
import com.epam.mishin.scanner.PackageScanner;
import com.epam.mishin.scanner.impl.ClassAnnotationValidatorImpl;
import com.epam.mishin.scanner.impl.PackageScannerImpl;

import java.util.List;
import java.util.logging.Logger;

public class Main {
    static PackageScanner scanner = new PackageScannerImpl();
    static InstanceCreator creator = new InstanceCreatorImpl();
    static ClassAnnotationValidator classAnnotationValidator = new ClassAnnotationValidatorImpl();
    private static final Logger LOGGER = Logger.getLogger(Main.class.getName());

    public static void main(String[] args)  {
        String directory = "src/main/java/com/epam/mishin/pojo";
        LOGGER.info("Star scanning directory " + directory);
        List<Class<?>> classes = scanner.scanPackage(directory);
        LOGGER.info( "All classes have been read from " + directory);
        LOGGER.info( "Start validating");
        List<Class<?>> validatedClasses = classAnnotationValidator.validateClasses(classes);
        LOGGER.info( "Validation is done");
        List<Object> objects = creator.createObjects(validatedClasses);
        LOGGER.info( "Start injecting values");
        objects.forEach(ValueAnnotationInjector::injectValue);
        LOGGER.info( "List of @Entity classes:");
        System.out.println(objects);
    }

}
