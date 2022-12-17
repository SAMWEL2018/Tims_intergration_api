package com.tims.intergration.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tims.intergration.http.HttpComponent;
import com.tims.intergration.model.TimsInvoice;
import com.tims.intergration.repository.Db_Gateway;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class TimsInvoiceProcessorService {
    @Autowired
    public Db_Gateway db_gateway;

    @Autowired
    public HttpComponent http;


    public void processInvoice(){

        try {
            TimsInvoice invoice = db_gateway.getInvoicesForProcessing();
            //log.info("Invoice :: "+new ObjectMapper().writeValueAsString(invoice));

            //http.pushInvoice(invoice);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
