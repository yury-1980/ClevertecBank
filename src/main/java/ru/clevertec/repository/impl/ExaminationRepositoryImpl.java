package ru.clevertec.repository.impl;

import lombok.extern.slf4j.Slf4j;
import ru.clevertec.repository.ConnectionPoolManager;
import ru.clevertec.repository.ExaminationRepository;
import ru.clevertec.repository.requests.RequestsSQL;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
public class ExaminationRepositoryImpl implements ExaminationRepository {

    private static final int NOL = 0;


    public ConcurrentHashMap<Long, BigDecimal> getIdBalanceAllAccount() {

        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ConcurrentHashMap<Long, BigDecimal> concurrentHashMap = new ConcurrentHashMap<>();
        long id;
        BigDecimal balance;

        try {
            connection = ConnectionPoolManager.getConnection();
            preparedStatement = connection.prepareStatement(RequestsSQL.GET_ID_BALANCE_ALL_ACCOUNT);
            preparedStatement.setLong(1, NOL);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {

                id = resultSet.getLong(1);
                balance = new BigDecimal(resultSet.getString(2));
                concurrentHashMap.put(id, balance);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            try {

                if (preparedStatement != null) {
                    preparedStatement.close();
                }
                if (connection != null) {
                    ConnectionPoolManager.releaseConnection(connection);
                }
            } catch (SQLException e) {
                log.info("Not successful!");
            }
        }

        return concurrentHashMap;
    }

    public void updateIdBalanceAllAccount(BigDecimal balance, long id) {

        Connection connection = null;
        PreparedStatement preparedStatement = null;

        try {
            connection = ConnectionPoolManager.getConnection();
            preparedStatement = connection.prepareStatement(RequestsSQL.UPDATE_BALANCE);
            preparedStatement.setBigDecimal(1, balance);
            preparedStatement.setLong(2, id);
            preparedStatement.executeUpdate();
            connection.commit();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            try {

                if (preparedStatement != null) {
                    preparedStatement.close();
                }
                if (connection != null) {
                    ConnectionPoolManager.releaseConnection(connection);
                }
            } catch (SQLException e) {
                log.info("Not successful!");
            }
        }
    }
}