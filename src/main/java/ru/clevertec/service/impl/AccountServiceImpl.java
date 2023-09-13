package ru.clevertec.service.impl;

import ru.clevertec.entity.Account;
import ru.clevertec.repository.AccountRepository;
import ru.clevertec.service.AccountService;

import java.math.BigDecimal;
import java.util.ArrayList;

public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;

    public AccountServiceImpl(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Override
    public ArrayList<Account> getAccountAll(String clientName) {
        return accountRepository.getAccountAll(clientName);
    }

    @Override
    public Account replenishingBalance(long accountNum, BigDecimal sum) {

        Account account = accountRepository.getAccount(accountNum);
        BigDecimal balance = new BigDecimal(String.valueOf(account.getBalance()));
        BigDecimal newBalance = balance.add(sum);
        accountRepository.updateAccount(account.getId(), newBalance);

        return accountRepository.getAccount(accountNum);
    }

    @Override
    public Account decreasingBalance(long accountNum, BigDecimal sum) {

        Account account = accountRepository.getAccount(accountNum);
        BigDecimal balance = new BigDecimal(String.valueOf(account.getBalance()));
        BigDecimal newBalance = balance.subtract(sum);
        accountRepository.updateAccount(account.getId(), newBalance);

        return accountRepository.getAccount(accountNum);
    }

    @Override
    public ArrayList<Account> movingToAnotherClient(long accountNum1, long accountNum2, BigDecimal sum) {

        Account account1 = accountRepository.getAccount(accountNum1);
        BigDecimal balance1 = new BigDecimal(String.valueOf(account1.getBalance()));
        BigDecimal newBalance1 = balance1.add(sum);

        Account account2 = accountRepository.getAccount(accountNum2);
        BigDecimal balance2 = new BigDecimal(String.valueOf(account2.getBalance()));
        BigDecimal newBalance2 = balance2.subtract(sum);

        accountRepository.movingToAnotherClient(newBalance1, account1.getId(), newBalance2, account2.getId());

        ArrayList<Account> accounts = new ArrayList<>();
        accounts.add(accountRepository.getAccount(accountNum1));
        accounts.add(accountRepository.getAccount(accountNum2));

        return accounts;
    }
}
