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

    @Scheduled(fixedDelay = 20000)
    public void invokeInvoiceProcesing(){
        timsInvoiceProcessorService.processInvoice();
    }
}
