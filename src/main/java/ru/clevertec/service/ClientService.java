package ru.clevertec.service;

//import ru.clevertec.annotation.Log;
//import ru.clevertec.annotation.LoggingLevel;

import java.math.BigDecimal;
import java.util.ArrayList;

public interface ClientService {

//    @Log(LoggingLevel.INFO)
    ArrayList<String> getClientName(String clientName);

    String replenishingBalance(long account, BigDecimal sum);

    String decreasingBalance(long account, BigDecimal sum);

    String movingToAnotherClient(long account, long account2, BigDecimal sum);
}
