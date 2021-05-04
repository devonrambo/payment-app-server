package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Transfer;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Component
public class JdbcTransferDAO implements TransferDAO {

    private JdbcTemplate template;

    public JdbcTransferDAO(DataSource dataSource) {
        this.template = new JdbcTemplate(dataSource);
    }

    @Override
    public Transfer getTransferWithTransferId(int id) {

        String sql = "SELECT * FROM transfers WHERE transfer_id = ?";

        SqlRowSet results = template.queryForRowSet(sql, id);

        Transfer transferObject = new Transfer();

        if (results.next()) {

            transferObject = createTransferObj(results, transferObject);

        }

        return transferObject;

    }

    @Override
    public List<Transfer> getTransfersWithUserId(int id) {
        String firstSql = "SELECT account_id FROM accounts WHERE user_id = ?";
        SqlRowSet firstResult = template.queryForRowSet(firstSql, id);
        int accountId = 0;
        if(firstResult.next()){
            accountId = firstResult.getInt("account_id");
        }
        String sql = "SELECT * FROM transfers WHERE (account_from = ? OR account_to = ?) AND (transfer_status_id <> 1)";
        SqlRowSet results = template.queryForRowSet(sql, accountId, accountId);
        List<Transfer> transfersList = new ArrayList<Transfer>();
//       Transfer transferObject = new Transfer();
        while (results.next()) {
            Transfer transferObject = new Transfer();
            transferObject = createTransferObj(results, transferObject);
            transfersList.add(transferObject);
        }
        return transfersList;
    }

    @Override
    public List<Transfer> getPendingTransfersWithUserId(int id) {
        String firstSql = "SELECT account_id FROM accounts WHERE user_id = ?";
        SqlRowSet firstResult = template.queryForRowSet(firstSql, id);
        int accountId = 0;
        if(firstResult.next()){
            accountId = firstResult.getInt("account_id");
        }
        String sql = "SELECT * FROM transfers WHERE (account_from = ? OR account_to = ?) AND (transfer_status_id = 1)";
        SqlRowSet results = template.queryForRowSet(sql, accountId, accountId);
        List<Transfer> transfersList = new ArrayList<Transfer>();
//       Transfer transferObject = new Transfer();
        while (results.next()) {
            Transfer transferObject = new Transfer();
            transferObject = createTransferObj(results, transferObject);
            transfersList.add(transferObject);
        }
        return transfersList;
    }

    @Override
    public void requestTransfer(Transfer transfer) {
//        System.out.println("account from :" + transfer.getAccountFromId());
//        System.out.println("account to :" + transfer.getAccountToId());
//        System.out.println("amount " + transfer.getAmount());

        String sql = "INSERT INTO transfers(transfer_type_id, transfer_status_id, account_from, account_to, amount) VALUES(2, 2, ?, ?, ?)";
        template.update(sql, transfer.getAccountFromId(), transfer.getAccountToId(), transfer.getAmount());
    }

    @Override
    public void requestPayment(Transfer transfer) {
        String sql = "INSERT INTO transfers(transfer_type_id, transfer_status_id, account_from, account_to, amount) VALUES(1, 1, ?, ?, ?)";
        template.update(sql, transfer.getAccountFromId(), transfer.getAccountToId(), transfer.getAmount());
    }

    @Override
    public void acceptTransferRequest(Transfer transfer) {

        String sqlAccountFromBalance = "SELECT balance from accounts WHERE account_id = ?";
        SqlRowSet results1 = template.queryForRowSet(sqlAccountFromBalance, transfer.getAccountFromId());

        String sqlAccountToBalance = "SELECT balance from accounts WHERE account_id = ?";
        SqlRowSet results2 = template.queryForRowSet(sqlAccountToBalance, transfer.getAccountToId());

        if(results1.next() && results2.next()) {
            BigDecimal accountFromBalance = new BigDecimal(results1.getDouble("balance"));
            BigDecimal accountToBalance = new BigDecimal(results2.getDouble("balance"));

            String sqlUpdateAccountTo = "UPDATE accounts SET balance = ? + ? WHERE account_id = ?";
            String sqlUpdateAccountFrom = "UPDATE accounts SET balance = ? - ? WHERE account_id = ?";

            template.update(sqlUpdateAccountFrom, accountFromBalance, transfer.getAmount(), transfer.getAccountFromId());
            template.update(sqlUpdateAccountTo, accountToBalance, transfer.getAmount(), transfer.getAccountToId());

            String sqlUpdateTransferStatus = "UPDATE transfers SET transfer_status_id = ? WHERE transfer_id = ?";
            template.update(sqlUpdateTransferStatus, 2, transfer.getTransferId());

//            String sqlUpdateTransferType = "UPDATE transfers SET transfer_type_id = ? WHERE transfer_id = ?";
//            template.update(sqlUpdateTransferStatus, 2, transfer.getTransferId());
        }

    }



    @Override
    public void rejectTransferRequest(Transfer transfer) {

        String sqlUpdateTransferStatus = "UPDATE transfers SET transfer_status_id = ? WHERE transfer_id = ?";
        template.update(sqlUpdateTransferStatus, 3, transfer.getTransferId());

    }


    public Transfer createTransferObj(SqlRowSet results, Transfer transferObject) {

        int transferToId = results.getInt("account_to");
        int transferFromId = results.getInt("account_from");
        BigDecimal amount = results.getBigDecimal("amount");
        int transferTypeId = results.getInt("transfer_type_id");
        int transferStatusId = results.getInt("transfer_status_id");
        int transferId = results.getInt("transfer_id");

        transferObject.setAccountToId(transferToId);
        transferObject.setAccountFromId(transferFromId);
        transferObject.setAmount(amount);
        transferObject.setTransferTypeId(transferTypeId);
        transferObject.setTransferStatusId(transferStatusId);
        transferObject.setTransferId(transferId);

        return transferObject;
    }


}
