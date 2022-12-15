package com.tims.intergration.repository;

import com.tims.intergration.model.*;
import com.tims.intergration.utility.PaymentType;
import com.tims.intergration.utility.TransactionType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class Db_Gateway {
    @Autowired
    ReceiptRepository repository;

    public TimsInvoice getInvoicesForProcessing(){
        repository.getUnprocessedReceipts();
        return TimsInvoice.builder()
                .buyer(null)
                .cashier("wafula")
                .exemptionNumber("test")
                .invoiceType(0L)
                .items(List.of(Item.builder()
                                .hsCode("0011.11.00")
                                .name("omo")
                                .quantity(1L)
                                .unitPrice(10.0)
                        .build()))
                .lines(List.of(Line.builder()
                                .lineType("Text")
                                .alignment("Left")
                                .format("Bold")
                                .value("Thank you")
                        .build()))
                .payment(List.of(Payment.builder()
                                .amount(100.00)
                                .paymentType(PaymentType.CASH.get())
                        .build()))
                .relevantNumber("XYZ")
                .traderSystemInvoiceNumber("10001")
                .transactionType(TransactionType.SALE.get())
                .build();
    }
}
