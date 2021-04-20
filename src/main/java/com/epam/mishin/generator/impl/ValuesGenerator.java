package com.epam.mishin.generator.impl;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class ValuesGenerator {

    public void generateRandomValuesIntString(String path, List<Object> objects, int count) throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter(path));
        int maxIndex = objects.size() - 1;
        Random randomizer = new Random();

        for (int i = 0; i < count; i++) {
            Object object = objects.get(randomizer.nextInt(maxIndex));
            Arrays.stream(object.getClass().getDeclaredFields())
                    .forEach(field -> writeLine(writer, field.getName(), randomizer.nextInt(100)));
        }
        writer.close();
    }

    private static void writeLine(Writer writer, String field, int value) {
        try {
            writer.write(field + "=" + value);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
