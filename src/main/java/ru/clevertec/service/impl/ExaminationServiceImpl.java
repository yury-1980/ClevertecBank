package ru.clevertec.service.impl;

import ru.clevertec.repository.ConnectionPoolManager;
import ru.clevertec.repository.requests.RequestsSQL;
import ru.clevertec.service.ExaminationService;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class ExaminationServiceImpl implements ExaminationService {

    public static void checksBalance() {

        ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);

        Runnable task = new Runnable() {

            private final LocalDate LOCAL_DATE = LocalDate.now();
            private final int CURRENT_YEAR = LOCAL_DATE.getYear();
            private final int LAST_DAY_OF_MONTH = LOCAL_DATE.lengthOfMonth();
            private final int HOUR = 23;
            private final int MINUTE = 59;
            private final int MONTH_TO_ADD = 1;
            private int monthAdd = LOCAL_DATE.getMonthValue();
            private final int NOL = 0;
            private final double PERCENT = 0.01;

            public void run() {

                LocalDateTime dayOfAdd = LocalDateTime.of(CURRENT_YEAR, monthAdd, LAST_DAY_OF_MONTH, HOUR, MINUTE);

                monthAdd = dayOfAdd.getMonthValue();
                int dayAdd = dayOfAdd.getDayOfMonth();
                int hourAdd = dayOfAdd.getHour();
                int minuteAdd = dayOfAdd.getMinute();

                LocalDateTime currentDateTime = LocalDateTime.now();

                int monthCurrent = currentDateTime.getMonthValue();
                int dayCurrent = currentDateTime.getDayOfMonth();
                int hourCurrent = currentDateTime.getHour();
                int minuteCurrent = currentDateTime.getMinute();

                if (monthAdd == monthCurrent && dayAdd == dayCurrent
                        && hourAdd == hourCurrent && minuteAdd == minuteCurrent) {

                    LocalDate localDate1 = LOCAL_DATE.plusMonths(MONTH_TO_ADD);
                    monthAdd = localDate1.getMonthValue();

                    // моя задача по процентам

                    Connection connection = null;
                    PreparedStatement preparedStatement = null;
                    ResultSet resultSet = null;
                    String result = null;
                    ConcurrentHashMap<Long, BigDecimal> concurrentHashMap = new ConcurrentHashMap<>();
                    Long id = null;
                    BigDecimal balance;

                    try {
                        connection = ConnectionPoolManager.getConnection();
                        preparedStatement = connection.prepareStatement(RequestsSQL.GET_ACCOUNT);
                        preparedStatement.setLong(1, NOL);
                        resultSet = preparedStatement.executeQuery();

                        while (resultSet.next()) {

                            id = resultSet.getLong(1);
                            balance = new BigDecimal(resultSet.getString(2));
                            concurrentHashMap.put(id, balance);
                        }

                        for (Map.Entry<Long, BigDecimal> entry : concurrentHashMap.entrySet()) {
                            Long key = entry.getKey();
                            BigDecimal value = entry.getValue();
                            // меняю значения
                            BigDecimal multiplier = new BigDecimal(PERCENT);
                            entry.setValue(value.multiply(multiplier));
                        }

                        for (Map.Entry<Long, BigDecimal> entry : concurrentHashMap.entrySet()) {
                            preparedStatement = connection.prepareStatement(RequestsSQL.UPDATE_BALANCE);
                            id = entry.getKey();
                            balance = entry.getValue();
                            preparedStatement.setBigDecimal(1, balance);
                            preparedStatement.setLong(2, id);
                            preparedStatement.executeUpdate();
                            connection.commit();
                        }
                    } catch (SQLException e) {
                        System.out.println("Connection not found.");
//            e.printStackTrace();
                    } finally {
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
            }
        };

        executor.scheduleAtFixedRate(task, 0, 30, TimeUnit.SECONDS);
    }
}