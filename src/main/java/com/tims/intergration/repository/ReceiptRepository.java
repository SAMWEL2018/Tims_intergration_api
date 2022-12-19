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

    public List<Map<String, Object>> getReceiptItems(int receiptNo){
        String sql = "SELECT rt.RctNo, rt.Qty, rt.Unit, rt.VatCode, rt.SPPreVat, rt.CPPreVat, rt.ItemCode, i.ItemCode, i.Name, i.VATCode, i.Packing, i.Unit, i.SP1InclVAT, i.CPInclVatTopUnit " +
                "FROM FOTRN.dbo.RctTrn rt, FOBASE.dbo.Items i  WHERE rt.ItemCode = i.ItemCode AND  rt.RctNo =? " +
                "ORDER by rt.RctNo  ";
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
            List<Map<String, Object>> r = results
                    .stream()
                    .filter(this::isReceiptPaid) // Remove Receipt not paid
                    .toList();
            System.out.println("==============> "+ r.size());
            return r;
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

    public Map<String, Object> getLegerInfo(int rctNo){
        String sql = "SELECT SUM(Dr) AS dr, SUM(Cr) AS cr, TrnType,TrnNo,DocNoA FROM TrnLedger WHERE DocNoA =? AND " +
                " Details in  ('Receipt Total') GROUP BY TrnType,TrnNo,DocNoA  ";
        List<Map<String, Object>> results = jdbcTemplate.queryForList(sql, rctNo);

        if (results.size() > 0){
            return results.get(0);
        }
        return null;
    }


}
