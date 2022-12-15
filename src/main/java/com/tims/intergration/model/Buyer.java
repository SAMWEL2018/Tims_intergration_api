
package com.tims.intergration.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Buyer {

    @JsonProperty("buyerAddress")
    private String buyerAddress;

    private String buyerName;

    private String buyerPhone;

    private String pinOfBuyer;


}
