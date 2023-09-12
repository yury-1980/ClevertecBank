package ru.clevertec.repository;

import ru.clevertec.entity.Account;

import java.math.BigDecimal;
import java.util.ArrayList;

public interface AccountRepository {

    ArrayList<Account> getAccountAll(String clientName);

    Account getAccount(long accountNum);

    void updateAccount(long idAccount, BigDecimal balance);

    void movingToAnotherClient(BigDecimal newBalance1, long idAccount1, BigDecimal newBalance2,
                                             long idAccount2);
}
