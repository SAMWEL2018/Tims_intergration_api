package com.tims.intergration.utility;

public enum PaymentType {

    CASH(0),CARD(1),CHEQUE(2),TRANSFER(3),VOUCHER(4),COUPON(5);

     final long value;
    PaymentType(long i) {
        value= i;
    }
    public long get(){
        return value;
    }
}
