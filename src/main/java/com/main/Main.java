package com.main;

import Annotations.Entity;
import Annotations.Value;
import Exceptions.IllegalStateException;
import Exceptions.NoValueAnnotationException;
import pojos.Human;

import java.io.File;
import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        InstanceCreator creator = new InstanceCreator();
        File pojos = new File(System.getProperty("user.dir") + "\\src\\main\\java\\pojos");
        List<Object> fromDirectory = creator.createFromDirectory(pojos);
        fromDirectory.forEach(o -> {

            boolean isEntity = o.getClass().getAnnotation(Entity.class) != null;

            if (isEntity && Arrays.stream(o.getClass().getDeclaredFields()).anyMatch(field -> field.getDeclaredAnnotation(Value.class) == null)) {
                throw new NoValueAnnotationException("Не на всех полях сущности присутствует аннотация @Value");
            }
            if (!isEntity && Arrays.stream(o.getClass().getDeclaredFields()).anyMatch(field -> field.getDeclaredAnnotation(Value.class) != null)) {
                throw new IllegalStateException("Аннотация @Value не может стоять на полях/методах класса, который не помечен аннотацией @Entity");
            }
            System.out.println("Объект класса " + o.getClass().getSimpleName() + " был успешно создан.");
            Arrays.stream(o.getClass().getDeclaredFields())
                    .forEach(field -> {
                        try {
                            field.setAccessible(true);
                            field.set(o, field.getAnnotation(Value.class).value());
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                        }
                        Arrays.stream(o.getClass().getMethods())
                                .filter(method -> method.getName().toLowerCase().contains(field.getName()) && method.getName().contains("set"))
                                .forEach(method -> {
                                    if (method.getAnnotation(Value.class) != null) {
                                        try {
                                            method.invoke(o, method.getAnnotation(Value.class).value());
                                        } catch (IllegalAccessException | InvocationTargetException e) {
                                            e.printStackTrace();
                                        }
                                    }

                                });
                    });
            System.out.println("Поля объекта класса " + o.getClass().getSimpleName() + " успешно были заполнены из @Value.");

        });
        fromDirectory.forEach(System.out::println);
    }

}
