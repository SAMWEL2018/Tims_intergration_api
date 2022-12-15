package com.tims.intergration.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Data
public class AppConfig {
    @Value("${tims.base_url}")
    private String timsBaseUrl;

    @Value("${tims.invoice_url}")
    private String invoices;
    //private String invoicesPostEndpoint = timsBaseUrl+invoices;

    public String getInvoicePostEndpoint(){
        return timsBaseUrl+invoices;
    }

}
