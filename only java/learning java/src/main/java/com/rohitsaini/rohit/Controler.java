package com.rohitsaini.rohit;
import com.fasterxml.jackson.databind.deser.impl.CreatorCandidate;

import org.springframework.web.bind.annotation.BindParam;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.tags.Param;


@RestController
public class Controler {

    @GetMapping("/")
    public String home() {
        return "Hello World!";
    }
    @GetMapping ("/mains")
    public String mains(){

        String s = "This is the main page";
        return s;}

}

