package com.codex.ide.codexide;
import com.codex.ide.codexide.file.format_code;
import com.codex.ide.codexide.file.readingfile;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.event.ActionEvent;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;

public class mainController {

    @FXML
    public TextArea codetextview;
    @FXML
    public ListView listenfiles;

    private Label filename1;




    format_code format_code;

    private void onTextAreaChange(String newValue) {
    }


    int length=0;
    void setLength(int i){if (length>=0){length+=i;}}
    @FXML
    private void initialize(){
        System.out.println("initialize");
        constructor();
//        filename1= new Label("rohit");

        filenames.forEach((String name)->{
            if (name.length()==1){
                System.out.println(name);
            }else {
            listenfiles.getItems().add(name);
            }

        });






        codetextview.textProperty().addListener((observable, oldValue, newValue) -> {
            //TODO: syntax highlighting
            onTextAreaChange(newValue);
        });



        codetextview.addEventHandler(javafx.scene.input.KeyEvent.KEY_PRESSED, event -> {
            if (event.getCode() == javafx.scene.input.KeyCode.ENTER) {
//                String newText = codetextview.getText();
                setLength(1);
                // Apply formatting
//                String formattedText = format_code.Indentjava(newText).toString();

//                codetextview.setText(formattedText+"\n");

                // Move the caret to the end of the text
//                codetextview.positionCaret(formattedText.length());

                // Consume the event to prevent default behavior
//                event.consume();
            }
            if (event.getCode() == javafx.scene.input.KeyCode.BACK_SPACE) {
            setLength(-1);
            }
//            System.out.println("length is : "+length);

        });


    }

    ArrayList<String>filenames;
    public void constructor(){
        filenames=new ArrayList<>();
        filenames.clear();
        format_code = new format_code();
//        format_code.Indentjava("public class hello {");
        String path="/home/rohit/Desktop";
        format_code.LS(path).forEach((String s)->{
            String[]  array = s.split(" ");
                filenames.add(array[array.length-1]);
        });
//        System.out.println(filenames);
    }

}


