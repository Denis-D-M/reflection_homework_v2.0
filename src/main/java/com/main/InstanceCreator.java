package com.main;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class InstanceCreator {
    public List<Object> createFromDirectory(File pojos){
        return Arrays.stream(pojos.list()).map(pojoName -> pojoName.replaceAll(".java", ""))
                .map(str -> {
                    try {
                        return Class.forName("pojos." + str);
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                    return null;
                })
                .map(aClass -> {
                    try {
                        return aClass.newInstance();
                    } catch (InstantiationException | IllegalAccessException e) {
                        e.printStackTrace();
                    }
                    return null;
                }).collect(Collectors.toList());


    }

}
