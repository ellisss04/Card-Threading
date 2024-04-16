package game;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public abstract class Writer {
    public void write(String line, String outputFilePath) throws IOException {
        File file = new File(outputFilePath);
        FileWriter writer = new FileWriter(file, true);

        writer.write(line);
        writer.close();
    }

    public abstract String initialise() throws IOException;
}
