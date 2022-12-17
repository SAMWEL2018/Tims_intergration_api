package com.tims.intergration.repository;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Repository
public class ReceiptRepository {


    private JdbcTemplate jdbcTemplate;

    public ReceiptRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Map<String, Object>> getReceiptItemS(int receiptNo){
        String sql = "SELECT RctNo, RctEntryNo, TrnDate, ItemCode, Qty, Unit, SPPreVat, VatAmt, VatCode, CPPreVat, SupCode, \n" +
                "Location, SalesmanCode, CustCode, CRC FROM RctTrn WHERE RctNo =? ";
        List<Map<String, Object>> results = jdbcTemplate.queryForList(sql, receiptNo);

        if (results.size() >0){
            return results;
        }
        return new ArrayList<>();
    }

    public List<Map<String, Object>> getUnprocessedReceipts() {

        String sql = " SELECT RctNo, TrnDate, UserName, PreVatAmt, VatAmt, Loyalty, CustName, SalesManCode, CRC, CUInvNo, " +
                "RIN, CUQR, CUDeviceSrNo, ESDSign FROM RctTrnSummary where tims_status =? ";

        List<Map<String, Object>> results = jdbcTemplate.queryForList(sql, "NEW");

        if (results.size() > 0) {
            return results
                    .stream()
                    .filter(this::isReceiptPaid) // Remove Receipt not paid
                    .toList();
        }
        return new ArrayList<>();
    }

    private boolean isReceiptPaid(Map<String, Object> receipt) {

        String sql = "SELECT SUM(Dr) AS dr, SUM(Cr) AS cr FROM TrnLedger where DocNoA =? ";

        List<Map<String, Object>> results = jdbcTemplate.queryForList(sql, receipt.get("RctNo"));
        //If not found in leger return false
        if (results.size() <= 0) {
            return false;
        }
        //Compare Dr - CR if greater or equal to 0
        return results
                .stream()
                .anyMatch(e -> (Double.parseDouble(e.get("dr").toString()) - Double.parseDouble(e.get("cr").toString())) >= 0);

    }


}
