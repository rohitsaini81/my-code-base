package com.codex.ide.codexide.file;

import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

public class readingfile {
    private ArrayList<String> filedata;
    public readingfile(){
        filedata = new ArrayList<>();



        try {
            File myObj = new File("/home/rohit/Desktop/Work/my-code-base/only java/hello.txt");
            Scanner myReader = new Scanner(myObj);
            while (myReader.hasNextLine()) {
                String data = myReader.nextLine();
                if (data!=null){
                    filedata.add(data);
                }
                System.out.println(data);
            }
            myReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }

    public ArrayList<String> getData() {
        return filedata;
    }


    public String beautifyJavaLine(String line) {
        String formattedLine = "";
        String pythonfilepath = "/home/rohit/Desktop/Work/my-code-base/only java/codex-ide/scripts/beautifier.py";
        try {
            // Call the Python script with the "line" argument
            ProcessBuilder pb = new ProcessBuilder("python3", pythonfilepath, "line");
            Process process = pb.start();

            // Send the line to the Python script
            PrintWriter writer = new PrintWriter(new OutputStreamWriter(process.getOutputStream()));
            writer.print(line);
            writer.flush();
            writer.close();

            // Read the formatted line from the Python script's output
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            formattedLine = reader.readLine();  // Read the single output line
            reader.close();

        } catch (IOException e) {
            e.getMessage();
        }
        System.out.println(formattedLine);
        return formattedLine;
    }


}
