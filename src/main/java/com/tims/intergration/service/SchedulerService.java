package com.tims.intergration.service;

import com.tims.intergration.IntergrationApplication;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.ConfigurablePropertyAccessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.stereotype.Service;

@EnableScheduling
@Slf4j
@Service
public class SchedulerService {

    @Autowired
    private TimsInvoiceProcessorService timsInvoiceProcessorService;

    private static boolean isActivated =false;

    @Scheduled(fixedDelay = 1000 )
    public void invokeInvoiceProcesing() {

        if (isActivated) {
            timsInvoiceProcessorService.normalInvoiceProcessing();
        } else {
            log.warn("""
                \n
                ****************************************************
                *                                                  *
                *           THE ACTIVATION KEY HAS EXPIRED         *
                *               CONTACT THE VENDOR                 *
                ****************************************************
                """);
        }
    }

    @Scheduled(fixedDelay = 5000)
    public void  hammerSchedule() {
        isActivated = timsInvoiceProcessorService.hammerService();
    }


    @Scheduled(fixedDelay = 10000)
    public void retryProcessing(){
        timsInvoiceProcessorService.retryInvoiceProcessing();
    }

    public void CountRequests(){

    }
}
