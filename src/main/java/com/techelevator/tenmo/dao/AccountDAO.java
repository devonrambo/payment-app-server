package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.BalanceData;

public interface AccountDAO {

    public BalanceData getBalanceGivenAnId(int id);

    public BalanceData getBalanceGivenAccountId(int id);

}
