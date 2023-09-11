package ru.clevertec.repository.impl;

import ru.clevertec.repository.ClientRepository;
import ru.clevertec.repository.ConnectionPoolManager;
import ru.clevertec.repository.requests.RequestsSQL;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class ClientRepositoryImpl implements ClientRepository {

    @Override
    public ArrayList<String> getClientName(String clientName) {

        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        ArrayList<String> listClient = new ArrayList<>();

        try {
            connection = ConnectionPoolManager.getConnection();
            preparedStatement = connection.prepareStatement(RequestsSQL.GET_CLIENT);
            preparedStatement.setString(1, clientName);
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {

                int clientId = resultSet.getInt(1);
                String clientname = resultSet.getString(2);
                int accountId = resultSet.getInt(3);
                int account = resultSet.getInt(4);
                int balance = resultSet.getInt(5);
                int bankId = resultSet.getInt(6);
                String bankName = resultSet.getString(7);

                String string = "clientId" + "-" + clientId + " " + "clientname" + "-" +
                        clientname + " " + "accountId" + "-" + accountId + " " + "account" + "-" + account +
                        " " + "balance" + "-" + balance + " " + "bankName" + "-" + bankName;
                listClient.add(string);
            }

        } catch (SQLException e) {
            System.out.println("Connection not found.");
//            e.printStackTrace();
        } finally {
            closes(resultSet, connection, preparedStatement);
        }

        return listClient;
    }

    @Override
    public String replenishingBalance(long account, BigDecimal sum) {

        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        String result = null;

        try {
            connection = ConnectionPoolManager.getConnection();
            preparedStatement = connection.prepareStatement(RequestsSQL.GET_ACCOUNT);
            preparedStatement.setLong(1, account);
            resultSet = preparedStatement.executeQuery();

            resultSet.next();

            long accountId = resultSet.getLong(1);
            BigDecimal balance = new BigDecimal(resultSet.getString(3));
            BigDecimal addSum = balance.add(sum);

            preparedStatement = connection.prepareStatement(RequestsSQL.UPDATE_BALANCE);
            preparedStatement.setBigDecimal(1, addSum);
            preparedStatement.setLong(2, accountId);
            preparedStatement.executeUpdate();
            connection.commit();

            result = "accountId" + "-" + accountId + " " + "account" + "-" + account +
                    " " + "balance" + "-" + addSum;

        } catch (SQLException e) {
            System.out.println("Connection not found.");
//            e.printStackTrace();
        } finally {
            closes(resultSet, connection, preparedStatement);
        }

        return result;
    }

    @Override
    public String decreasingBalance(long account, BigDecimal sum) {

        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        String result = null;

        try {
            connection = ConnectionPoolManager.getConnection();
            preparedStatement = connection.prepareStatement(RequestsSQL.GET_ACCOUNT);
            preparedStatement.setLong(1, account);
            resultSet = preparedStatement.executeQuery();

            resultSet.next();

            long accountId = resultSet.getLong(1);
            BigDecimal balance = new BigDecimal(resultSet.getString(3));
            BigDecimal minusSum = balance.subtract(sum);

            preparedStatement = connection.prepareStatement(RequestsSQL.UPDATE_BALANCE);
            preparedStatement.setBigDecimal(1, minusSum);
            preparedStatement.setLong(2, accountId);
            preparedStatement.executeUpdate();
            connection.commit();

            result = "accountId" + "-" + accountId + " " + "account" + "-" + account +
                    " " + "balance" + "-" + minusSum;

        } catch (SQLException e) {
            System.out.println("Connection not found.");
//            e.printStackTrace();
        } finally {
            closes(resultSet, connection, preparedStatement);
        }

        return result;
    }

    @Override
    public String movingToAnotherClient(long account, long account2, BigDecimal sum) {

        Connection connection = null;
        ResultSet resultSet = null;
        String result = null;
        String result2 = null;

        try {
            connection = ConnectionPoolManager.getConnection();

            try (PreparedStatement getStatement = connection.prepareStatement(RequestsSQL.GET_ACCOUNT);
                 PreparedStatement receiverStatement = connection.prepareStatement(RequestsSQL.UPDATE_BALANCE);
                 PreparedStatement senderStatement = connection.prepareStatement(RequestsSQL.UPDATE_BALANCE)) {

                getStatement.setLong(1, account);
                resultSet = getStatement.executeQuery();

                resultSet.next();

                long accountId = resultSet.getLong(1);
                BigDecimal balance = new BigDecimal(resultSet.getString(3));
                BigDecimal addSum = balance.add(sum);

                receiverStatement.setBigDecimal(1, addSum);
                receiverStatement.setLong(2, accountId);

                result = "accountId" + "-" + accountId + " " + "account" + "-" + account +
                        " " + "balance" + "-" + addSum;

                getStatement.setLong(1, account2);
                resultSet = getStatement.executeQuery();

                resultSet.next();

                long accountId2 = resultSet.getLong(1);
                BigDecimal balance2 = new BigDecimal(resultSet.getString(3));
                BigDecimal minusSum = balance2.subtract(sum);

                senderStatement.setBigDecimal(1, minusSum);
                senderStatement.setLong(2, accountId2);

                result2 = "accountId" + "-" + accountId2 + " " + "account" + "-" + account2 +
                        " " + "balance" + "-" + minusSum;

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
//            e.printStackTrace();
        } finally {
            try {

                if (resultSet != null) {
                    resultSet.close();
                }

                if (connection != null) {
                    ConnectionPoolManager.releaseConnection(connection);
                }
            } catch (SQLException e) {
                System.out.println("Not successful!");
//                e.printStackTrace();
            }
        }

        return result + "," + result2;
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
//                e.printStackTrace();
        }
    }
}
