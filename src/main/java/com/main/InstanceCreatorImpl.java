package com.main;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class InstanceCreatorImpl implements InstanceCreator {
    public List<Object> createFromDirectory(File pojos){

        if (pojos == null) {
            return new ArrayList<>();
        }

        return Arrays.stream(pojos.list()).map(pojoName -> pojoName.replace(".java", ""))
                .map(str -> {
                    try {
                        return Class.forName(str);
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                    throw new RuntimeException(new ClassNotFoundException());
                })
                .map(aClass -> {
                    try {
                        return aClass.getConstructor().newInstance();
                    } catch (NoSuchMethodException | InvocationTargetException | InstantiationException | IllegalAccessException e) {
                        e.printStackTrace();
                    }
                    throw new RuntimeException(new InstantiationException());
                }).collect(Collectors.toList());

    }

}
