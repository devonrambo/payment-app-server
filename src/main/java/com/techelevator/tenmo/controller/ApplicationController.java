package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.dao.AccountDAO;
import com.techelevator.tenmo.dao.JdbcAccountDAO;
import com.techelevator.tenmo.dao.TransferDAO;
import com.techelevator.tenmo.dao.UserDAO;
import com.techelevator.tenmo.model.BalanceData;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@RestController
public class ApplicationController {

    @Autowired
    AccountDAO accountDAO;

    @Autowired
    UserDAO userDAO;

    @Autowired
    TransferDAO transferDAO;

    @RequestMapping(path = "/get-balance", method = RequestMethod.GET)
    public BalanceData processBalanceRequest(Principal principal){

        int userId = userDAO.findIdByUsername(principal.getName());

        BalanceData balance = accountDAO.getBalanceGivenAnId(userId);

        System.out.println("Requesting Balance");
        System.out.println(principal.getName());
        System.out.println(balance.getBalance());

        return balance;
    }

    @RequestMapping(path = "/user/username/{userId}", method = RequestMethod.GET)
    public String getUsernameWithUserId(@PathVariable int userId){

        String returnString = userDAO.findUsernameByUserId(userId);

        return returnString;

    }


    @RequestMapping(path = "/transfer/{transferId}", method = RequestMethod.GET)
    public Transfer getTransferInfoWithTransferId(@PathVariable int transferId){

        Transfer transfer = transferDAO.getTransferWithTransferId(transferId);

        return transfer;
    }

    @RequestMapping(path = "/transfer/user/{userId}/completed", method = RequestMethod.GET)
    public List<Transfer> getCompletedTransfersWithId(@PathVariable int userId){

          List<Transfer> transfersList = transferDAO.getTransfersWithUserId(userId);

        return transfersList;
    }

    @RequestMapping(path = "/users", method = RequestMethod.GET)
    public List<User> getAllUsers() {
        List<User> returnList = new ArrayList<User>();
        returnList = userDAO.findAll();

        return returnList;
    }

    @RequestMapping(path = "/transfer/makeRequestToSend", method = RequestMethod.POST)
    public void requestTransfer(@RequestBody Transfer transfer) throws Exception {
        // check to see if the necessary funds are here

            BalanceData balanceObject = accountDAO.getBalanceGivenAccountId(transfer.getAccountFromId());

            BigDecimal balance = balanceObject.getBalance();

            BigDecimal requestedSendAmount = new BigDecimal(String.valueOf(transfer.getAmount()));

//        System.out.println(transfer.getAccountFromId());
//        System.out.println(balanceObject.toString());
//        System.out.println(balance);
//        System.out.println(requestedSendAmount);

            if(balance.compareTo(requestedSendAmount) >= 0){

            transferDAO.requestTransfer(transfer);
            }

            else{

        // throw an exception if there are not enough funds
                throw new Exception();

            }
    }

    @RequestMapping(path = "transfer/askForPayment", method = RequestMethod.POST)
    public void askForPayment(@RequestBody Transfer transfer) {
        transferDAO.requestPayment(transfer);
    }

    @RequestMapping(path = "user/account/{userId}", method = RequestMethod.GET)
    public int getAccountIdWithUserId(@PathVariable int userId) {
        int returnInt = userDAO.findAccountIdWithUserId(userId);
        return returnInt;
    }

    @RequestMapping(path = "user/userid/{accountId}", method = RequestMethod.GET)
    public int getUserIdWithAccountId(@PathVariable int accountId){
        int returnInt = userDAO.findUserIdWithAccountId(accountId);
        return returnInt;
    }

    @RequestMapping(path = "transfer/{userId}/pending", method = RequestMethod.GET)
    public List<Transfer> getAllPendingTransferWithUserId(@PathVariable int userId) {
        List<Transfer> allPendingTransfers = transferDAO.getPendingTransfersWithUserId(userId);

        return allPendingTransfers;
    }


    @RequestMapping(path = "transfer/acceptTransfer", method = RequestMethod.PUT)
    public void acceptTransfer(@RequestBody Transfer transfer) throws Exception {

        // check to see if the necessary funds are here

        BalanceData balanceObject = accountDAO.getBalanceGivenAccountId(transfer.getAccountFromId());

        BigDecimal balance = balanceObject.getBalance();

        BigDecimal requestedSendAmount = new BigDecimal(String.valueOf(transfer.getAmount()));

        if(balance.compareTo(requestedSendAmount) >= 0){

            transferDAO.acceptTransferRequest(transfer);
        }

        else{

            // throw an exception if there are not enough funds
            throw new Exception();
        }

    }

    @RequestMapping(path = "transfer/rejectTransfer", method = RequestMethod.PUT)
    public void rejectTransfer(@RequestBody Transfer transfer){

        transferDAO.rejectTransferRequest(transfer);

    }

}
