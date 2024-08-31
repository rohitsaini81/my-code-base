package com.codex.ide.codexide.file;

import java.io.*;
import java.util.ArrayList;

public class format_code {
    public format_code() {
        files=new ArrayList<>();
    }
    public StringBuilder Indentjava(String text) {
        String path = "src/main/resources/scripts/line_indentation.py";
        StringBuilder formattedText = new StringBuilder();
        try {
            // Call the Python script with the "text" argument
            ProcessBuilder pb = new ProcessBuilder("python3", path);
            Process process = pb.start();

            // Send the text to the Python script
            PrintWriter writer = new PrintWriter(new OutputStreamWriter(process.getOutputStream()));
            writer.print(text);
            writer.flush();
            writer.close();

            // Read the formatted text from the Python script's output
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                formattedText.append(line).append("\n");
            }
            reader.close();

            // Wait for the process to finish
            process.waitFor();

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        return formattedText;
    }


    private ArrayList<String> files;
    public ArrayList<String> LS(String path) {
//        String path = "/home/rohit/Desktop/Work/my-code-base/only java/codex-ide/src/main/java/com/codex/ide/codexide/file/";  // You may want to expand this to an absolute path
        try {
            // Resolve the user's home directory if using "~"
//            path = Paths.get(path).toAbsolutePath().toString();

            // Set up ProcessBuilder with the 'ls' command and the working directory
            ProcessBuilder pb = new ProcessBuilder("ls", "-l");
            pb.directory(new java.io.File(path)); // Set the working directory

            Process process = pb.start();

            // Read the output of the 'ls' command
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            files.clear();
            while ((line = reader.readLine()) != null) {
//                System.out.println(line);
                files.add(line);

            }
//            System.out.println(files.get(2));
            reader.close();

            // Wait for the process to complete
            process.waitFor();
//            return files;

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        return files;
    }
}

