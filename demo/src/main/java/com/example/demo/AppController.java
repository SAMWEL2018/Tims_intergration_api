package com.example.demo;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Joseph Kibe
 * Created on Wednesday, December 28, 2022
 * Time 6:28 PM
 */

@RestController
public class AppController {

    @RequestMapping(value = "/")
    public String index(){
        return "Test";
    }
 }
