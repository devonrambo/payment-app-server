package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Transfer;

import java.util.List;

public interface TransferDAO {

    public Transfer getTransferWithTransferId(int id);

    public List<Transfer> getTransfersWithUserId(int id);

    public List<Transfer> getPendingTransfersWithUserId(int id);

    public void requestTransfer(Transfer transfer);

    public void acceptTransferRequest(Transfer transfer);

    public void rejectTransferRequest(Transfer transfer);

    public void requestPayment(Transfer transfer);


}
