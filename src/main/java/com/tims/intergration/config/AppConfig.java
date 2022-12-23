package com.tims.intergration.config;

import com.tims.intergration.utility.InvoiceCache;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Slf4j
@Data
public class AppConfig {

    @Value("${tims.base_url}")
    private String timsBaseUrl;

    @Value("${tims.invoice_url}")
    private String invoices;
    //private String invoicesPostEndpoint = timsBaseUrl+invoices;

    @Value("${tims.app_key}")
    private String hammer;

    public String getInvoicePostEndpoint(){
        return timsBaseUrl+invoices;
    }

    @Bean
    public InvoiceCache getTransactionCache(){
        return new InvoiceCache();
    }

}
