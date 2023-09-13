package ru.clevertec.service;


import ru.clevertec.entity.Account;

import java.math.BigDecimal;
import java.util.ArrayList;

public interface AccountService {

    ArrayList<Account> getAccountAll(String clientName);

    Account replenishingBalance(long accountNum, BigDecimal sum);

    Account decreasingBalance(long account, BigDecimal sum);

    ArrayList<Account> movingToAnotherClient(long account, long account2, BigDecimal sum);
}
