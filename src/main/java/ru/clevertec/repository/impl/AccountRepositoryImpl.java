package ru.clevertec.repository.impl;

import ru.clevertec.entity.Account;
import ru.clevertec.repository.AccountRepository;
import ru.clevertec.repository.ConnectionPoolManager;
import ru.clevertec.repository.requests.RequestsSQL;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class AccountRepositoryImpl implements AccountRepository {

    @Override
    public ArrayList<Account> getAccountAll(String clientName) {

        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        ArrayList<Account> accountList = new ArrayList<>();
        Account accountClient;

        try {
            connection = ConnectionPoolManager.getConnection();
            preparedStatement = connection.prepareStatement(RequestsSQL.GET_ACCOUNT_CLIENT_ALL);
            preparedStatement.setString(1, clientName);
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {

                int clientId = resultSet.getInt(1);
                int accountId = resultSet.getInt(3);
                int account = resultSet.getInt(4);
                BigDecimal balance = new BigDecimal(resultSet.getInt(5));
                int bankId = resultSet.getInt(6);

                accountClient = new Account(accountId, account, clientId, bankId, balance);
                accountList.add(accountClient);
            }

        } catch (SQLException e) {
            System.out.println("Connection not found.");
        } finally {
            closes(resultSet, connection, preparedStatement);
        }

        return accountList;
    }

    @Override
    public Account getAccount(long accountNum) {

        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        Account accountClient = null;

        try {
            connection = ConnectionPoolManager.getConnection();
            preparedStatement = connection.prepareStatement(RequestsSQL.GET_ACCOUNT);
            preparedStatement.setLong(1, accountNum);
            resultSet = preparedStatement.executeQuery();

            resultSet.next();

            long accountId = resultSet.getLong(1);
            long account = resultSet.getLong(2);
            long clientId = resultSet.getLong(3);
            long bankId = resultSet.getLong(4);
            BigDecimal balance = new BigDecimal(resultSet.getInt(5));

            accountClient = new Account(accountId, account, clientId, bankId, balance);

        } catch (SQLException e) {
            System.out.println("Connection not found.");
        } finally {
            closes(resultSet, connection, preparedStatement);
        }

        return accountClient;
    }

    @Override
    public void updateAccount(long idAccount, BigDecimal balance) {

        Connection connection = null;
        PreparedStatement preparedStatement = null;

        try {
            connection = ConnectionPoolManager.getConnection();
            preparedStatement = connection.prepareStatement(RequestsSQL.UPDATE_BALANCE);
            preparedStatement.setBigDecimal(1, balance);
            preparedStatement.setLong(2, idAccount);
            preparedStatement.executeUpdate();
            connection.commit();

        } catch (SQLException e) {
            System.out.println("Connection not found.");
        } finally {
            try {

                if (preparedStatement != null) {
                    preparedStatement.close();
                }
                if (connection != null) {
                    ConnectionPoolManager.releaseConnection(connection);
                }
            } catch (SQLException e) {
                System.out.println("Not successful!");
            }
        }

    }

    @Override
    public void movingToAnotherClient(BigDecimal newBalance1, long idAccount1, BigDecimal newBalance2,
                                      long idAccount2) {

        Connection connection = null;

        try {
            connection = ConnectionPoolManager.getConnection();

            try (PreparedStatement receiverStatement = connection.prepareStatement(RequestsSQL.UPDATE_BALANCE);
                 PreparedStatement senderStatement = connection.prepareStatement(RequestsSQL.UPDATE_BALANCE)) {

                receiverStatement.setBigDecimal(1, newBalance1);
                receiverStatement.setLong(2, idAccount1);

                senderStatement.setBigDecimal(1, newBalance2);
                senderStatement.setLong(2, idAccount2);

                senderStatement.executeUpdate();
                receiverStatement.executeUpdate();
                connection.commit();
            }
        } catch (SQLException e) {
            System.out.println("Connection not found.");

            try {
                assert connection != null;
                connection.rollback();
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        } finally {

            try {

                if (connection != null) {
                    ConnectionPoolManager.releaseConnection(connection);
                }
            } catch (SQLException e) {
                System.out.println("Not successful!");
            }
        }
    }

    private void closes(ResultSet resultSet, Connection connection, PreparedStatement preparedStatement) {

        try {
            if (resultSet != null) {
                resultSet.close();
            }
            if (preparedStatement != null) {
                preparedStatement.close();
            }
            if (connection != null) {
                ConnectionPoolManager.releaseConnection(connection);
            }
        } catch (SQLException e) {
            System.out.println("Not successful!");
        }
    }
}
