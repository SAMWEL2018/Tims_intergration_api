
package com.tims.intergration.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


@SuppressWarnings("unused")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TimsInvoice {

    private Buyer buyer;
    private String cashier;
    @JsonProperty("ExemptionNumber")
    private String exemptionNumber;
    private Long invoiceType;
    private List<Item> items;
    private List<Line> lines;
    private List<Payment> payment;
    private String relevantNumber;
    @JsonProperty("TraderSystemInvoiceNumber")
    private String traderSystemInvoiceNumber;
    private Long transactionType;


}
