package ru.clevertec.repository;

import java.math.BigDecimal;
import java.util.ArrayList;

public interface ClientRepository {

    public ArrayList<String> getClientName(String clientName);

    String replenishingBalance(long account, BigDecimal sum);

    String decreasingBalance(long account, BigDecimal sum);

    String movingToAnotherClient(long account, long account2, BigDecimal sum);
}
