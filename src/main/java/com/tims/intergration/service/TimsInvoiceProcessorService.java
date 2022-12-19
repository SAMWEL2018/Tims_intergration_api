package com.tims.intergration.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tims.intergration.http.HttpComponent;
import com.tims.intergration.model.TimsInvoice;
import com.tims.intergration.repository.Db_Gateway;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class TimsInvoiceProcessorService {
    @Autowired
    public Db_Gateway db_gateway;

    @Autowired
    public HttpComponent http;


    public void processInvoice() {

        try {
            List<TimsInvoice> invoices = db_gateway.getInvoicesForProcessing();
            log.info("Invoice :: " + new ObjectMapper().writeValueAsString(invoices));
            invoices.forEach(invoice -> {
                try {
                    new Thread(() -> http.pushInvoice(invoice)).start();
                } catch (Exception e) {
                    log.error("Invoice Failed : "+e.getMessage());
                    //e.printStackTrace();
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
