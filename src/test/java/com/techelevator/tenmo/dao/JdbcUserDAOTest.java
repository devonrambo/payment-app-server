package com.techelevator.tenmo.dao;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.techelevator.tenmo.model.User;
import org.junit.*;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;

import java.sql.SQLException;
import java.util.List;

public class JdbcUserDAOTest {

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
        userDAO = new JdbcUserDAO(jdbcTemplate);


        //run setup queries

    }

    @After
    public void doAfterEveryTest() throws Exception{
        dataSource.getConnection().rollback();
    }

    @Test
    public void testFindIdByUsername() {
        int userId = userDAO.findIdByUsername("bob");
        int id = 1004;


        Assert.assertEquals(id, userId);

    }

    @Test
    public void testFindAllUsers(){

        List<User> allUsers = userDAO.findAll();
        int length = allUsers.size();
        int expectedLength = 4;

        Assert.assertEquals(length, expectedLength);
    }


    @Test
    public void findByUserName(){

        User user = userDAO.findByUsername("bob");
        int actualId = Integer.parseInt(String.valueOf(user.getId()));
        int expectedId = 1004;
        String actualPassword = user.getPassword();
        String expectedPassword = "$2a$10$g.LcOUZZv/7wk0VGMZmHW.zsrJvfq9DiuRo2IClpkpwZ.9Ra6V2yW";

        Assert.assertEquals(actualId, expectedId);
        Assert.assertEquals(actualPassword, expectedPassword);

    }

    @Test
    public void test_create(){

       boolean actualBoolean = userDAO.create("Timmy", "password");
       boolean expectedBoolean = true;

       Assert.assertEquals(actualBoolean, expectedBoolean);

    }

    @Test
    public void test_find_account_with_user_id(){

        int actualId = userDAO.findAccountIdWithUserId(1004);
        int expectedId = 2004;

        Assert.assertEquals(actualId, expectedId);

    }

    @Test
    public void test_find_user_id_with_account_id(){

        int actualId = userDAO.findUserIdWithAccountId(2004);
        int expectedId = 1004;

        Assert.assertEquals(expectedId, actualId);

    }

    @Test
    public void test_find_username_by_user_id(){

        String actualName = userDAO.findUsernameByUserId(1004);
        String expectedName = "bob";

        Assert.assertEquals(actualName, expectedName);


    }


}