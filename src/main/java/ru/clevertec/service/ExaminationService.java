package ru.clevertec.service;

import ru.clevertec.repository.ExaminationRepository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class ExaminationService {

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
            private final double PERCENT = 0.01;

            @Override
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

                    ConcurrentHashMap<Long, BigDecimal> concurrentHashMap =
                            ExaminationRepository.getIdBalanceAllAccount();

                    for (Map.Entry<Long, BigDecimal> entry : concurrentHashMap.entrySet()) {
                        Long id = entry.getKey();
                        BigDecimal value = entry.getValue();
                        /*
                          меняю значения
                         */
                        BigDecimal multiplier = new BigDecimal(PERCENT);
                        BigDecimal balance = entry.setValue(value.multiply(multiplier));

                        ExaminationRepository.updateIdBalanceAllAccount(balance, id);
                    }
                }
            }
        };
        executor.scheduleAtFixedRate(task, 0, 30, TimeUnit.SECONDS);
    }
}