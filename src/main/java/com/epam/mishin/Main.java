package com.epam.mishin;

import com.epam.mishin.annotation.Entity;
import com.epam.mishin.annotation.Value;
import com.epam.mishin.exception.NoValueAnnotationException;
import com.epam.mishin.instance.InstanceCreator;
import com.epam.mishin.instance.impl.InstanceCreatorImpl;
import com.epam.mishin.pojo.Human;

import java.io.*;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class Main {
    static InstanceCreator creator = new InstanceCreatorImpl();
    private static final Logger LOGGER = Logger.getLogger(Main.class.getName());

    public static void main(String[] args) throws ClassNotFoundException {
        System.out.println(creator.createFromDirectory("src/main/java/com/epam/mishin/pojo"));
    }

    public void main() {
        List<Object> fromDirectory = creator.createFromDirectory("src/main/java/pojos");

        fromDirectory.forEach(o -> {
            checkClassAnnotations(o);
            LOGGER.log(Level.FINE, "Объект класса " + o.getClass().getSimpleName() + " был успешно создан.");

            Arrays.stream(o.getClass().getDeclaredFields())
                    .forEach(field -> {
                        try {
                            field.setAccessible(true);
                            field.set(o, field.getAnnotation(Value.class).value());
                            if (!field.getAnnotation(Value.class).valuesTxtPath().isEmpty()) {
                                field.set(o, readFromFile(field.getAnnotation(Value.class).valuesTxtPath(), field.getName()));
                                return;
                            }
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                        } catch (IllegalArgumentException e) {
                            System.err.println("Недопустимый @Value у поля");
                        }

                    });
            System.out.println("Поля объекта класса " + o.getClass().getSimpleName() + " успешно были заполнены из @Value.");

        });

        fromDirectory.forEach(System.out::println);

        System.out.println("Классы с аннотцией @Entity");
        for (Class<?> aClass : checkClasses(Path.of("src/main/java/pojos"))) {
            System.out.println(aClass);
        }
    }

    public static void checkClassAnnotations(Object object) {
        Field[] fields = object.getClass().getDeclaredFields();

        boolean isEntity = Objects.nonNull(object.getClass().getAnnotation(Entity.class));
        boolean isValue = Arrays.stream(fields)
                .anyMatch(o -> Objects.nonNull(o.getAnnotation(Value.class)));
        boolean isNoValue = Arrays.stream(fields)
                .anyMatch(o -> Objects.isNull(o.getAnnotation(Value.class)));
        if (isEntity && isNoValue) {
            LOGGER.log(Level.WARNING, new NoValueAnnotationException(), () -> "Entity has a field without @Value annotation");
            return;
        }
        if (!isEntity && isValue) {
            LOGGER.log(Level.WARNING, new IllegalStateException(), () -> "@Value annotation on a non-entity's field.");
        }
    }

    public static void fillFields(Object object) {

    }

//    public static boolean checkSetters(Object object) {
//        Arrays.stream(object.getClass().getMethods())
//                .filter(method -> Objects.nonNull(method.getAnnotation(Value.class)))
//                .filter(method -> method.getName().contains("set"))
//                .forEach(method -> {
//                    if (!isValueTxtInMethod(object, method)) {
//                        invokeMethod(object, method, method.getAnnotation(Value.class).value());
//                    }
//                });
//        if ()
//    }


    public static boolean isValueTxtInMethod(Object object, Method method) {
        String valuePath = method.getAnnotation(Value.class).valuesTxtPath();
        if (!(valuePath.isEmpty())) {
            try {
                method.invoke(object, readFromFile(valuePath, method.getName().replace("set", "").toLowerCase()));
                return true;
            } catch (IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    public static void invokeMethod(Object object, Method method, String value) {
        try {
            method.invoke(object, value);
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    public static Optional<String> readFromFile(String path, String fieldName) {
        try (BufferedReader reader = new BufferedReader(new FileReader(path))) {
            String s;
            while ((s = reader.readLine()) != null) {
                if (s.contains(fieldName)) {
                    s = s.replaceAll(fieldName + "=", "");
                    return Optional.of(s);
                }
            }
        } catch (IOException e) {
            LOGGER.log(Level.WARNING, e, () -> "Error with " + path + " file");
        }
        return Optional.empty();
    }

    public static List<Class<?>> checkClasses(Path path) {
        List<Class<?>> collect = new ArrayList<>();
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
