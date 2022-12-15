package com.tims.intergration.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ReceiptRepository {
    @PersistenceContext
    EntityManager entityManager ;

    public void getUnprocessedReceipts(){

        String sql = " SELECT RctNo, TrnDate, UserName, PreVatAmt, VatAmt, Loyalty, CustName, SalesManCode, CRC, CUInvNo, \n" +
                "RIN, CUQR, CUDeviceSrNo, ESDSign FROM RctTrnSummary where tims_status =:status ";
        Query query = entityManager.createNativeQuery(sql);
        query.setParameter("status","NEW");
        List<Object[]>list = query.getResultList();
        System.out.println(list.get(0)[3]);
    }

}
