package com.main;

import java.io.File;
import java.util.List;

public interface InstanceCreator {
    List<Object> createFromDirectory(File directory);
}
