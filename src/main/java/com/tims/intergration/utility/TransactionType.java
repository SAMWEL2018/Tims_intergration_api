package com.tims.intergration.utility;

public enum TransactionType {

    SALE(0),DEBIT(2),CREDIT(1);

    final long value;

    TransactionType(long i){
        value = i;
    }

    public long get(){
        return value;
    }


}
