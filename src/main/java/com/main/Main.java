package com.main;

import Annotations.Entity;
import Annotations.Value;
import Exceptions.NoValueAnnotationException;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Main {
    List<Object> searchedClasses;

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
                            if (!field.getAnnotation(Value.class).valuesTxtPath().isEmpty()) {
                                field.set(o,readFromFile(field.getAnnotation(Value.class).valuesTxtPath(), field.getName()));
                            }
                        } catch (IllegalAccessException | IOException e) {
                            e.printStackTrace();
                        } catch (IllegalArgumentException e) {
                            System.err.println("Недопустимый @Value у поля");
                        }
                        Arrays.stream(o.getClass().getMethods())
                                .filter(method -> method.getName().toLowerCase().contains(field.getName()) && method.getName().contains("set"))
                                .forEach(method -> {
                                    if (method.getAnnotation(Value.class) != null) {
                                        try {
                                            method.invoke(o, method.getAnnotation(Value.class).value());
                                            if (!method.getAnnotation(Value.class).valuesTxtPath().isEmpty()) {
                                                method.invoke(o, readFromFile(method.getAnnotation(Value.class).valuesTxtPath(), field.getName()));
                                            }
                                        } catch (IllegalAccessException | InvocationTargetException | IOException e) {
                                            e.printStackTrace();
                                        } catch (IllegalArgumentException e) {
                                            System.err.println("Недопустимый @Value у метода");
                                        }
                                    }

                                });
                    });
            System.out.println("Поля объекта класса " + o.getClass().getSimpleName() + " успешно были заполнены из @Value.");

        });

        fromDirectory.forEach(System.out::println);
        try {
            System.out.println(valuesGenerator(System.getProperty("user.dir")+ "\\src\\main\\resources\\generated.txt",fromDirectory));
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("Классы с аннотцией @Entity");
            for (Class aClass : checkClasses(Path.of(System.getProperty("user.dir") + "/src/main/java")))  {
                System.out.println(aClass);
            }


    }

    public static String readFromFile(String path, String fieldName) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(path));
        String s;
        while ((s = reader.readLine()) != null) {
            if (s.contains(fieldName)) {
                s = s.replaceAll(fieldName + "=", "");
                return s;
            }
        }
        return null;
    }

    public static boolean valuesGenerator(String path, List<Object> objects) throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter(path));
        objects.forEach(o -> {
            Arrays.stream(o.getClass().getDeclaredFields())
                    .forEach(field -> {
                        try {
                            field.setAccessible(true);
                            writer.write(field.getName() + "=" + field.get(o) + "\n");

                        } catch (IOException | IllegalAccessException e) {
                            System.err.println("Ошибка записи значений в файл.");
                            e.printStackTrace();
                        }
                    });
        });
        writer.close();
        return true;
    }

    public static List<Class> checkClasses(Path path) {
        List<Class> collect = new ArrayList<>();
        try {
            collect = Files.walk(path)
                    .filter(Files::isRegularFile)
                    .map(Path::toFile)
                    .map(File::getAbsolutePath)
                    .map(s -> s.replace(path.toString() + "\\", ""))
                    .map(s -> s.replaceAll("\\\\", "."))
                    .map(s -> s.replace(".java", ""))
                    .map(s -> {
                        try {
                            return Class.forName(s);
                        } catch (ClassNotFoundException e) {
                            e.printStackTrace();
                        }
                        return null;
                    })
                    .filter(aClass -> aClass.getAnnotation(Entity.class) != null)
                    .collect(Collectors.toList());

        } catch (IOException e) {
            e.printStackTrace();
        }
        return collect;

    }


}
