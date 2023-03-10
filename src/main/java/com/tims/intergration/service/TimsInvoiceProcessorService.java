package com.tims.intergration.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tims.intergration.IntergrationApplication;
import com.tims.intergration.config.AppConfig;
import com.tims.intergration.http.HttpComponent;
import com.tims.intergration.model.TimsInvoice;
import com.tims.intergration.repository.Db_Gateway;
import com.tims.intergration.utility.Hammer;
import com.tims.intergration.utility.InvoiceCache;
import com.tims.intergration.utility.RSAUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;

@Service
@Slf4j
public class TimsInvoiceProcessorService {
    @Autowired
    private AppConfig config;
    @Autowired
    public Db_Gateway db_gateway;

    @Autowired
    public HttpComponent http;
    @Autowired
    private InvoiceCache cache;


    int count =0;

    public void performCount(){
        for (int i=count; i<=5;i++){

        }
    }


    public void normalInvoiceProcessing() {
        try {
            List<TimsInvoice> invoices = db_gateway.getInvoicesForProcessing();
            if (invoices.size() >0) {
                log.info("Invoice :: " + new ObjectMapper().writeValueAsString(invoices));
                invoices.forEach(this::processInvoice);
            }
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public  void retryInvoiceProcessing(){
        try {
            List<TimsInvoice> invoices = db_gateway.getInvoicesForProcessingRetry();
            if (invoices.size() >0) {
                log.info("Invoice for Retry :: " + new ObjectMapper().writeValueAsString(invoices));
                invoices.forEach(this::processInvoice);
            }

        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

    }

    public void processInvoice(TimsInvoice invoice) {

        try {

            if (!cache.checkIfPresent(invoice.getTraderSystemInvoiceNumber())) { //Process only if it's not available in cache
                new Thread(() -> {
                    try {
                        // Add to cache to prevent Similar transaction from being processed while the current
                        // of the same kind is not yet completed
                        cache.addToCache(invoice);

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
                            log.warn("Timeout when posting : " + invoice.getTraderSystemInvoiceNumber());
                            db_gateway.updateRctSummary(invoice.getTraderSystemInvoiceNumber(), "NEW", msg, LocalDateTime.now().toString(), "", "", "", "", "", "");
                        } else
                            db_gateway.updateRctSummary(invoice.getTraderSystemInvoiceNumber(), "FAILED", node.toString().replaceAll("'", ""), LocalDateTime.now().toString(), "", "", "", "", "", "");

                        //Remove item form cache
                        cache.evictFromCache(invoice);

                    } catch (JsonProcessingException e) {
                        e.printStackTrace();
                    }
                }).start();
            }
                /*else {
                    log.warn("Invoice Still in processing :: "+invoice.getTraderSystemInvoiceNumber());
                }*/


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean hammerService(){
        LocalDateTime dateTime = LocalDateTime.parse(Objects.requireNonNull(RSAUtil.decrypt(config.getHammer(), Hammer.privateKey)), DateTimeFormatter.ISO_OFFSET_DATE_TIME);
        return dateTime.isAfter(LocalDateTime.now());
    }
}
