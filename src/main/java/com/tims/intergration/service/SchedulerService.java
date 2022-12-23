package com.tims.intergration.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@EnableScheduling
@Service
public class SchedulerService {

    @Autowired
    private TimsInvoiceProcessorService timsInvoiceProcessorService;

    @Scheduled(fixedDelay = 1000)
    public void invokeInvoiceProcesing(){
        timsInvoiceProcessorService.normalInvoiceProcessing();
    }

    @Scheduled(fixedDelay = 10000)
    public void retryProcessing(){
        timsInvoiceProcessorService.retryInvoiceProcessing();
    }

    public void CountRequests(){

    }
}
