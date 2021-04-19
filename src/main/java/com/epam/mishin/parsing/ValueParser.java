package com.epam.mishin.parsing;

import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Collectors;

public class ValueParser {
    public static void parse(Object object, Field field, String s) throws IllegalAccessException {
        Class<?> type = field.getType();
        field.setAccessible(true);
        if (type.equals(int.class)) {
            field.set(object, Integer.parseInt(s));
            return;
        }
        if (type.equals(String.class)) {
            field.set(object, s);
            return;
        }
    }
    public static void parseTxt(Object object, Field field, String path) throws IllegalAccessException, IOException {
        String s = Files.lines(Path.of(path))
                .filter(s1 -> s1.contains(field.getName()))
                .map(s1 -> s1.replace(field.getName() + "=", ""))
                .collect(Collectors.joining());
        Class<?> type = field.getType();
        field.setAccessible(true);
        if (type.equals(int.class)) {
            field.set(object, Integer.parseInt(s));
            return;
        }
        if (type.equals(String.class)) {
            field.set(object, s);
            return;
        }
    }
}
