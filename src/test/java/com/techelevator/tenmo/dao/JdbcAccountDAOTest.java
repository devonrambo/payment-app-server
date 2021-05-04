package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.BalanceData;
import org.junit.*;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;

import java.math.BigDecimal;
import java.sql.SQLException;

import static org.junit.Assert.*;

public class JdbcAccountDAOTest {


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
        accountDAO = new JdbcAccountDAO(dataSource);
        //run setup queries

    }

    @After
    public void doAfterEveryTest() throws Exception{
        dataSource.getConnection().rollback();
    }


    @Test
    public void test_get_balance_given_user_id(){

        BalanceData balanceData = accountDAO.getBalanceGivenAnId(1004);

        BigDecimal actualBalance = balanceData.getBalance();
        BigDecimal actualBalanceFinal = new BigDecimal(String.valueOf(actualBalance));
        String actualBalanceString = String.valueOf(actualBalanceFinal).substring(0, 3);

        BigDecimal actualBalanceComplete = new BigDecimal(actualBalanceString);

        BigDecimal expectedBalance = new BigDecimal("555");


        Assert.assertEquals(actualBalanceComplete, expectedBalance);

    }

    @Test
    public void test_get_balance_give_account_id(){

        BalanceData balanceData = accountDAO.getBalanceGivenAccountId(2004);

        BigDecimal actualBalance = balanceData.getBalance();
        BigDecimal actualBalanceFinal = new BigDecimal(String.valueOf(actualBalance));
        String actualBalanceString = String.valueOf(actualBalanceFinal).substring(0, 3);

        BigDecimal actualBalanceComplete = new BigDecimal(actualBalanceString);

        BigDecimal expectedBalance = new BigDecimal("555");


        Assert.assertEquals(actualBalanceComplete, expectedBalance);

    }
}