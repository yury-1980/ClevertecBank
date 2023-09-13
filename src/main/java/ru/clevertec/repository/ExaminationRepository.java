package ru.clevertec.repository;

import java.math.BigDecimal;
import java.util.concurrent.ConcurrentHashMap;

public interface ExaminationRepository {

    ConcurrentHashMap<Long, BigDecimal> getIdBalanceAllAccount();

    void updateIdBalanceAllAccount(BigDecimal balance, long id);
}
