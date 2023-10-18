package br.edu.utfpr.sistemarquivos;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class FileReader {

    public void read(Path path) {
        // TODO implementar a leitura dos arquivos do PATH aqui

        if (!path.toString().substring(path.toString().length() - 3).equals("txt")) {
            throw new UnsupportedOperationException("Extension not supported");
        }
        try {
            Files.readAllLines(path).forEach(System.out::println);
        } catch (UnsupportedOperationException | IOException e) {
            System.out.println("This command should be used with files only");
        }
    }
}
