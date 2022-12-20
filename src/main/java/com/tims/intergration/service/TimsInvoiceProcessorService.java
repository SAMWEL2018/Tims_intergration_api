package com.tims.intergration.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tims.intergration.http.HttpComponent;
import com.tims.intergration.model.TimsInvoice;
import com.tims.intergration.repository.Db_Gateway;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
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

                new Thread(() -> {
                    try {
                        JsonNode node = http.pushInvoice(invoice);
                        String msg = node.has("messages") ? node.get("messages").asText() : "Failed";
                        if (msg.toLowerCase().contains("success")) {
                            String date = node.has("DateTime") ? node.get("DateTime").asText() : "Not Found";
                            String invoiceNumber = node.has("mtn") ? node.get("mtn").asText() : "Not Found";
                            String msn = node.has("msn") ? node.get("msn").asText() : "Not Found";
                            String relevantNumber = node.has("relevantNumber") ? node.get("relevantNumber").asText() : "Not Found";
                            String totalAmount = node.has("totalAmount") ? node.get("totalAmount").asText() : "Not Found";
                            String totalItems = node.has("totalItems") ? node.get("totalItems").asText() : "Not Found";
                            String verificationUrl = node.has("verificationUrl") ? node.get("verificationUrl").asText() : "Not Found";
                            db_gateway.updateRctSummary(invoice.getTraderSystemInvoiceNumber(), "SUC", msg, date, invoiceNumber, msn, relevantNumber, totalAmount, totalItems, verificationUrl);
                        } else if (msg.contains("timed out")) {
                            //Do nothing to allow reprocessing
                        } else
                            db_gateway.updateRctSummary(invoice.getTraderSystemInvoiceNumber(), "FAILED", node.asText().replaceAll("\'", ""), LocalDateTime.now().toString(), "", "", "", "", "", "");
                    } catch (JsonProcessingException e) {
                        e.printStackTrace();
                    }
                }).start();

            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
