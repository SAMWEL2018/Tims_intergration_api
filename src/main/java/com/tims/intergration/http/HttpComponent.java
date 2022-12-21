package com.tims.intergration.http;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tims.intergration.config.AppConfig;
import com.tims.intergration.model.TimsInvoice;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
@Slf4j
public class HttpComponent {

    @Autowired
    private RestTemplate rt;

    @Autowired
    private AppConfig config;


    public JsonNode pushInvoice(TimsInvoice invoice) throws JsonProcessingException {
        try {
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.add("RequestId",invoice.getExemptionNumber());
            httpHeaders.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<Object> httpEntity = new HttpEntity<>(invoice, httpHeaders);
            ResponseEntity<JsonNode> res = rt.exchange(
                    config.getInvoicePostEndpoint(),
                    HttpMethod.POST,
                    httpEntity,
                    JsonNode.class
            );
            log.info("Tims Response response RCT NO: "+ invoice.getTraderSystemInvoiceNumber()+" =>"+res.getBody());

            return res.getBody();

        } catch (Exception e) {
            log.error("Error On Posting Invoice to TIMS Invoice: "+ invoice.getTraderSystemInvoiceNumber()+" => "+ e.getMessage());
            return new ObjectMapper().readTree("{\"messages\":\""+e.getMessage()+"\"}");
        }


    }
}
