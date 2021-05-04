package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.BalanceData;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.math.BigDecimal;

@Component
public class JdbcAccountDAO implements AccountDAO{

    private JdbcTemplate template;

    public JdbcAccountDAO(DataSource dataSource){

        this.template = new JdbcTemplate(dataSource);

    }

    @Override
    public BalanceData getBalanceGivenAnId(int id) {

        String sql = "SELECT balance from accounts WHERE user_id = ?";

        SqlRowSet result = template.queryForRowSet(sql, id);

        BalanceData balanceData = new BalanceData();

        if(result.next()){

            String balance = result.getString("balance");

            BigDecimal balanceBD = new BigDecimal(balance);

            balanceData.setBalance(balanceBD);

        }
        return balanceData;
    }

    @Override
    public BalanceData getBalanceGivenAccountId(int id) {

        String sql = "SELECT balance from accounts WHERE account_id = ?";

        SqlRowSet result = template.queryForRowSet(sql, id);

        BalanceData balanceData = new BalanceData();

        if(result.next()){

            String balance = result.getString("balance");

            BigDecimal balanceBD = new BigDecimal(balance);

            balanceData.setBalance(balanceBD);

        }
        return balanceData;

    }


}
