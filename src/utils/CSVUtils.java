package utils;

import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.stream.*;

public class CSVUtils {
    public static List<String[]> readCSV(String path) throws IOException {
        Path p = Paths.get(path);
        if (!Files.exists(p)) return Collections.emptyList();
        try (Stream<String> lines = Files.lines(p)) {
            return lines.filter(line -> !line.trim().isEmpty())
                    .map(line -> line.split(","))
                    .collect(Collectors.toList());
        }
    }

    public static void writeCSV(String path, List<String[]> data) throws IOException {
        Path p = Paths.get(path);
        try (BufferedWriter writer = Files.newBufferedWriter(p)) {
            for (String[] row : data) {
                writer.write(String.join(",", row));
                writer.newLine();
            }
        }
    }

    public static void appendCSV(String path, String[] row) throws IOException {
        Path p = Paths.get(path);
        try (BufferedWriter writer = Files.newBufferedWriter(p, StandardOpenOption.CREATE, StandardOpenOption.APPEND)) {
            writer.write(String.join(",", row));
            writer.newLine();
        }
    }
}
