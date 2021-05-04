package com.techelevator.tenmo.dao;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.techelevator.tenmo.model.Transfer;
import org.junit.*;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;

import static org.junit.Assert.*;

public class JdbcTransferDAOTest {

    private static SingleConnectionDataSource dataSource;

    private AccountDAO accountDAO;
    private UserDAO userDAO;
    private TransferDAO transferDAO;

    @BeforeClass
    public static void setup() {
        dataSource = new SingleConnectionDataSource();
        dataSource.setUrl("jdbc:postgresql://localhost:5432/tenmo");
        dataSource.setUsername("postgres");
        dataSource.setPassword("postgres1");
        dataSource.setAutoCommit(false);
    }

    @AfterClass
    public static void cleanup() throws SQLException {

        dataSource.destroy();
    }

    @Before
    public void setupBeforeTest() {
        //do some kind of sql stuff

        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        transferDAO = new JdbcTransferDAO(dataSource);

        //run setup queries

    }

    @After
    public void doAfterEveryTest() throws Exception{
        dataSource.getConnection().rollback();
    }


    @Test
    public void test_get_transfer_with_transfer_id(){

        Transfer transfer = transferDAO.getTransferWithTransferId(3001);

        int actualTransferId = transfer.getTransferId();
        int actualAccountFromId = transfer.getAccountFromId();
        int actualAccountToId = transfer.getAccountToId();
        BigDecimal actualAmount = transfer.getAmount();

        String amountString = String.valueOf(actualAmount);
        String trimmedAmount = amountString.substring(0, 3);
        BigDecimal actualAmountFinal = new BigDecimal(trimmedAmount);

        int actualTransferStatusId = transfer.getTransferStatusId();
        int actualTransferTypeId = transfer.getTransferTypeId();

        int expectedTransferId = 3001;
        int expectedAccountFromId = 2002;
        int expectedAccountToId = 2001;
        BigDecimal expectedAmount = new BigDecimal(400);

        int expectedTransferStatusId = 3;
        int expectedTransferTypeId = 1;

        Assert.assertEquals(actualTransferId, expectedTransferId);
        Assert.assertEquals(actualAccountFromId, expectedAccountFromId);
        Assert.assertEquals(actualAccountToId, expectedAccountToId);
        Assert.assertEquals(actualAmountFinal, expectedAmount);
        Assert.assertEquals(actualTransferStatusId, expectedTransferStatusId);
        Assert.assertEquals(actualTransferTypeId, expectedTransferTypeId);

    }

    @Test
    public void test_get_not_pending_transfers_with_user_id(){

        List<Transfer> transfers = transferDAO.getTransfersWithUserId(1004);

        int actualLength = transfers.size();
        int expectedLength = 2;

        Assert.assertEquals(actualLength, expectedLength);

    }

    @Test
    public void test_get_pending_transfers_with_user_id(){

        List<Transfer> transfers = transferDAO.getPendingTransfersWithUserId(1004);

        int actualLength = transfers.size();
        int expectedLength = 5;

        Assert.assertEquals(actualLength, expectedLength);

    }



}