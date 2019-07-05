package readerPack;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

/**
 * readerPack
 * Short Description: (눈_눈)
 *
 * @author nikitos
 * @version 1.0.0
 */

public class Reader {

    private String pathOrFilename;

    public Reader(String pathOrFilename) {
        this.pathOrFilename = pathOrFilename;
    }

    public List<Path> getFiles() throws IOException {
        return Files.walk(Paths.get(pathOrFilename)).filter(Files::isRegularFile).collect(Collectors.toList());
    }
}
