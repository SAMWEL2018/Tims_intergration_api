
package com.tims.intergration.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@SuppressWarnings("unused")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Item {


    private List<Description> description;
    private String discounts;
    private String gtin;
    private String hsCode;
    private String name;
    private Long quantity;
    private double unitPrice;
}
