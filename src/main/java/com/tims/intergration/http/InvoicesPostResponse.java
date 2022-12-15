
package com.tims.intergration.http;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@SuppressWarnings("unused")
public class InvoicesPostResponse {

    @JsonProperty("DateTime")
    private String dateTime;
    private String invoiceExtension;
    private String messages;
    private String msn;
    private String mtn;
    private Double totalAmount;
    private Long totalItems;
    private String verificationUrl;
}
