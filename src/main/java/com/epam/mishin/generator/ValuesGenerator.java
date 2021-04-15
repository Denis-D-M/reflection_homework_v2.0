package com.epam.mishin.generator;

import java.io.IOException;
import java.util.List;

public interface ValuesGenerator {
    void generateRandomValuesIntString(String path, List<Object> objects, int count) throws IOException;
}
