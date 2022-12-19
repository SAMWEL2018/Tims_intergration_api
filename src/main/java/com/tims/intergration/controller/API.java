package com.tims.intergration.controller;

import com.tims.intergration.model.TimsInvoice;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class API {
    @RequestMapping(value = "/",produces = MediaType.APPLICATION_JSON_VALUE)
    public String index(){
        return "{\"api_status\" : \"TIMS integration Service is Up and running\" }";
    }

    @RequestMapping(value = "/invoice/structure")
    public ResponseEntity<TimsInvoice> getInvoiceStructure(){
        return ResponseEntity.ok(new TimsInvoice());

    }

}
