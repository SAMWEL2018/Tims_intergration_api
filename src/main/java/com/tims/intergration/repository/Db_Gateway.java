package com.tims.intergration.repository;

import com.tims.intergration.model.Item;
import com.tims.intergration.model.Line;
import com.tims.intergration.model.Payment;
import com.tims.intergration.model.TimsInvoice;
import com.tims.intergration.utility.PaymentType;
import com.tims.intergration.utility.TransactionType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
@Slf4j
public class Db_Gateway {
    @Autowired
    ReceiptRepository repository;

    public List<TimsInvoice> getInvoicesForProcessing() {
        List<TimsInvoice> timsInvoices = new ArrayList<>();
        repository
                .getUnprocessedReceipts()
                .forEach(rct -> {
                    TimsInvoice t = buildInvoice(rct);
                    if (t.getItems().size() > 0) {
                        timsInvoices.add(t);
                    }
                });

        return timsInvoices;
    }

    private TimsInvoice buildInvoice(Map<String, Object> receipt) {
        int rctNo = Integer.parseInt(receipt.get("RctNo").toString());
        Map<String, Object> legerInfo = repository.getLegerInfo(rctNo);

        return TimsInvoice.builder()
                .buyer(null)
                .cashier("wafula")
                .exemptionNumber(legerInfo.get("TrnNo").toString())
                .invoiceType(0L)
                .items(buildListOfItems(rctNo))
                .lines(List.of(Line.builder()
                        .lineType("Text")
                        .alignment("Left")
                        .format("Bold")
                        .value("Thank you")
                        .build()))
                .payment(List.of(Payment.builder()
                        .amount(Math.round(Double.parseDouble(legerInfo.get("cr").toString())*100.0)/100.0)
                        .paymentType(getPaymentMode(legerInfo.get("TrnType").toString()))
                        .build()))
                .relevantNumber(null)
                .traderSystemInvoiceNumber(legerInfo.get("DocNoA").toString())
                .transactionType(TransactionType.SALE.get())
                .build();
    }

    private List<Item> buildListOfItems(int rctNo) {
        List<Item> items = new ArrayList<>();
        repository.getReceiptItems(rctNo)
                .forEach(item -> {
                    Item i = Item.builder()
                            .hsCode(null)
                            .name(item.get("Name").toString())
                            .quantity((long) Double.valueOf(item.get("Qty").toString()).intValue())
                            .unitPrice(Math.round(Double.parseDouble(item.get("SPPreVat").toString())*100.0)/100.0)
                            .build();
                    items.add(i);

                });
        return items;
    }

    private Long getPaymentMode(String paymentMethod) {
        Long mode = null;
        switch (paymentMethod.toLowerCase()) {
            case "cash":
                mode = PaymentType.CASH.get();
                break;
            case "cheque":
                mode = PaymentType.CHEQUE.get();
                break;
            case "voucher":
                mode = PaymentType.VOUCHER.get();
                break;
            case "coupon":
                mode = PaymentType.COUPON.get();
                break;
            default:
                mode = PaymentType.TRANSFER.get();
        }
        return mode;
    }


    public void updateRctSummary(String rctNo, String status, String msg, String date, String inV, String msn, String rntNo, String tA, String tI, String verUrl){
        try {
            repository.updateRct(rctNo, status, msg, date, inV, msn, rntNo, tA, tI, verUrl);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("ERROR Updating the Rct Summary => "+e.getMessage());
        }
    }

}
