package com.epam.mishin.injector;

import com.epam.mishin.annotation.Value;
import com.epam.mishin.parsing.ValueParser;

import java.io.IOException;
import java.util.Arrays;
import java.util.Objects;
import java.util.logging.Logger;

public class ValueAnnotationInjector {
    private static final Logger LOGGER = Logger.getLogger(ValueAnnotationInjector.class.getName());
    public static void injectValue(Object object){
        injectFields(object);
        injectMethods(object);
    }
    private static void injectMethods(Object object){
        Class<?> aClass = object.getClass();
        Arrays.stream(aClass.getMethods())
                .filter(method -> Objects.nonNull(method.getAnnotation(Value.class)))
                .forEach(method -> {
                    String s = method.getName().replace("set", "").toLowerCase();
                    String path = method.getAnnotation(Value.class).valuesTxtPath();
                    try {
                        if (!path.isEmpty() && !path.isBlank()){
                            ValueParser.parseTxt(object, aClass.getDeclaredField(s), path);
                            LOGGER.info("Values to " + object + "have been injected from file");
                            return;
                        }
                        ValueParser.parse(object, aClass.getDeclaredField(s), method.getAnnotation(Value.class).value());
                        LOGGER.info("Values to " + object + "have been injected from annotation");
                    } catch (IllegalAccessException | NoSuchFieldException | IOException e) {
                        LOGGER.warning("Injecting from @Value on method " + method.getName() + " went wrong");
                    }
                });


    }
    private static void injectFields(Object object){
        Arrays.stream(object.getClass().getDeclaredFields())
                .forEach(field -> {
                    try {
                        ValueParser.parse(object, field, field.getAnnotation(Value.class).value());
                        LOGGER.info("Value from @Value have been injected to " + field.getName() + " field");
                    } catch (IllegalAccessException e) {
                        LOGGER.warning("Injecting from @Value on field " + field.getName() + " went wrong");
                    }
                });
    }
}
