package br.edu.utfpr.sistemarquivos;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributeView;

import static br.edu.utfpr.sistemarquivos.Application.ROOT;

public enum Command {

    LIST() {
        @Override
        boolean accept(String command) {
            final var commands = command.split(" ");
            return commands.length > 0 && commands[0].startsWith("LIST") || commands[0].startsWith("list");
        }

        @Override
        Path execute(Path path) throws IOException {

            // TODO implementar conforme enunciado

            System.out.println("Contents of " + path);
            for (String folder : path.toFile().list()) {
                System.out.println(folder);
            }

            return path;
        }
    },
    SHOW() {
        private String[] parameters = new String[]{};

        @Override
        void setParameters(String[] parameters) {
            this.parameters = parameters;
        }

        @Override
        boolean accept(String command) {
            final var commands = command.split(" ");
            return commands.length > 0 && commands[0].startsWith("SHOW") || commands[0].startsWith("show");
        }

        @Override
        Path execute(Path path) {

            // TODO implementar conforme enunciado
            FileReader fileReader = new FileReader();
            if(parameters.length == 1){
                throw new UnsupportedOperationException("Show need parameters");
            }
            fileReader.read(Path.of(path + File.separator + parameters[1]));
            return path;
        }
    },
    BACK() {
        @Override
        boolean accept(String command) {
            final var commands = command.split(" ");
            return commands.length > 0 && commands[0].startsWith("BACK") || commands[0].startsWith("back");
        }

        @Override
        Path execute(Path path) {

            // TODO implementar conforme enunciado
            if (path.toString().equals(ROOT)) {
                throw new UnsupportedOperationException("Can't get out of " + path);
            }
            path = Path.of(path.toString().substring(0, path.toString().lastIndexOf(File.separator)));
            return path;
        }
    },
    OPEN() {
        private String[] parameters = new String[]{};

        @Override
        void setParameters(String[] parameters) {
            this.parameters = parameters;
        }

        @Override
        boolean accept(String command) {
            final var commands = command.split(" ");
            return commands.length > 0 && commands[0].startsWith("OPEN") || commands[0].startsWith("open");
        }

        @Override
        Path execute(Path path) {

            // TODO implementar conforme enunciado

            if (parameters.length == 1) {
                throw new UnsupportedOperationException("Open need a directory");
            }
            boolean exists = false;
            for (String folder : path.toFile().list()) {
                if (folder.equals(parameters[1])) {
                    path = Path.of(path + File.separator + parameters[1]);
                    exists = true;
                }
            }

            File file = new File(String.valueOf(path));
            if (!exists || file.isFile()) {
                throw new UnsupportedOperationException("Directory not found");
            }

            return path;
        }
    },
    DETAIL() {
        private String[] parameters = new String[]{};

        @Override
        void setParameters(String[] parameters) {
            this.parameters = parameters;
        }

        @Override
        boolean accept(String command) {
            final var commands = command.split(" ");
            return commands.length > 0 && commands[0].startsWith("DETAIL") || commands[0].startsWith("detail");
        }

        @Override
        Path execute(Path path) {

            // TODO implementar conforme enunciado
            if (parameters.length == 1) {
                throw new UnsupportedOperationException("Detail need a file or directory");
            }
            boolean exists = false;
            for (String folder : path.toFile().list()) {
                if (folder.equals(parameters[1])) {
                    exists = true;
                }
            }
            if (!exists) {
                throw new UnsupportedOperationException("Not found");
            }
            try {
                File file = new File(String.valueOf(path + File.separator + parameters[1]));
                BasicFileAttributeView basicFile = Files.getFileAttributeView(file.toPath(), BasicFileAttributeView.class);

                if (file.exists()) {
                    System.out.println("Is directory [" + file.isDirectory() + "]");
                    System.out.println("Size [" + file.length() + "]");
                    System.out.println("creationTime [" + basicFile.readAttributes().creationTime() + "]");
                    System.out.println("Last modified [" + basicFile.readAttributes().lastModifiedTime() + "]");
                }
            } catch (UnsupportedOperationException | IOException e) {
                System.out.println(e.getMessage());
            }
            return path;
        }
    },
    EXIT() {
        @Override
        boolean accept(String command) {
            final var commands = command.split(" ");
            return commands.length > 0 && commands[0].startsWith("EXIT") || commands[0].startsWith("exit");
        }

        @Override
        Path execute(Path path) {
            System.out.print("Saindo...");
            return path;
        }

        @Override
        boolean shouldStop() {
            return true;
        }
    };

    abstract Path execute(Path path) throws IOException;

    abstract boolean accept(String command);

    void setParameters(String[] parameters) {
    }

    boolean shouldStop() {
        return false;
    }

    public static Command parseCommand(String commandToParse) {

        if (commandToParse.isBlank()) {
            throw new UnsupportedOperationException("Type something...");
        }

        final var possibleCommands = values();

        for (Command possibleCommand : possibleCommands) {
            if (possibleCommand.accept(commandToParse)) {
                possibleCommand.setParameters(commandToParse.split(" "));
                return possibleCommand;
            }
        }

        throw new UnsupportedOperationException("Can't parse command [%s]".formatted(commandToParse));
    }
}
