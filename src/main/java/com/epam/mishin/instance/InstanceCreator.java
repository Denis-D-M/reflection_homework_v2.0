package com.epam.mishin.instance;

import java.util.List;

public interface InstanceCreator {
    List<Object> createObjects(List<Class<?>> classList);
}
