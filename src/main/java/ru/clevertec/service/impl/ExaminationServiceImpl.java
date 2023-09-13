package ru.clevertec.service.impl;

import ru.clevertec.repository.ExaminationRepository;
import ru.clevertec.service.ExaminationService;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class ExaminationServiceImpl implements ExaminationService {
    private final ExaminationRepository examinationRepository;

    public ExaminationServiceImpl(ExaminationRepository examinationRepository) {
        this.examinationRepository = examinationRepository;
    }

    @Override
    public void checksBalance() {

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

                LocalDateTime currentDateTime = LocalDateTime.now();

                if (monthAdd == currentDateTime.getMonthValue()
                        && dayOfAdd.getDayOfMonth() == currentDateTime.getDayOfMonth()
                        && dayOfAdd.getHour() == currentDateTime.getHour()
                        && dayOfAdd.getMinute() == currentDateTime.getMinute()) {

                    monthAdd = LOCAL_DATE.plusMonths(MONTH_TO_ADD).getMonthValue();

                    ConcurrentHashMap<Long, BigDecimal> concurrentHashMap =
                            examinationRepository.getIdBalanceAllAccount();

                    concurrentHashMap.entrySet().forEach(entry -> {
                        Long id = entry.getKey();
                        BigDecimal value = entry.getValue();
                        BigDecimal multiplier = new BigDecimal(PERCENT);
                        BigDecimal balance = entry.setValue(value.multiply(multiplier));
                        examinationRepository.updateIdBalanceAllAccount(balance, id);
                    });
                }
            }
        };
        executor.scheduleAtFixedRate(task, 0, 30, TimeUnit.SECONDS);
    }
}