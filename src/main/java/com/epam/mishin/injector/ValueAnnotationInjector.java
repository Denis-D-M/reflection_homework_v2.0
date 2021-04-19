package com.epam.mishin.injector;

import com.epam.mishin.annotation.Value;
import com.epam.mishin.parsing.ValueParser;

import java.io.IOException;
import java.util.Arrays;
import java.util.Objects;

public class ValueAnnotationInjector {
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
                            return;
                        }
                        ValueParser.parse(object, aClass.getDeclaredField(s), method.getAnnotation(Value.class).value());
                    } catch (IllegalAccessException | NoSuchFieldException | IOException e) {
                        e.printStackTrace();
                    }
                });


    }
    private static void injectFields(Object object){
        Arrays.stream(object.getClass().getDeclaredFields())
                .forEach(field -> {
                    try {
                        ValueParser.parse(object, field, field.getAnnotation(Value.class).value());
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                });
    }
}
