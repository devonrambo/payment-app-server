package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.User;

import java.util.List;

public interface UserDAO {

    List<User> findAll();

    User findByUsername(String username);

    int findIdByUsername(String username);

    boolean create(String username, String password);

    int findAccountIdWithUserId(int userId);

    int findUserIdWithAccountId(int accountId);

    String findUsernameByUserId(int userId);
}
