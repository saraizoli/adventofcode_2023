package main.utils;

import java.io.File;
import java.net.URL;
import java.nio.file.Files;
import java.util.List;

public class InputReader {
    public List<String> readAsStringList(int day){
        return readAsStringList(String.format("day%02d.txt", day));
    }

    public List<String> readAsStringList(String fileName){
        URL url = getClass().getClassLoader().getResource(fileName);
        try {
            File file = new File(url.toURI());
            return Files.readAllLines(file.toPath());
        } catch (Exception e) {
            throw new RuntimeException("File not found or can't be read", e);
        }
    }
}
