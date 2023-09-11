package ru.clevertec.service.impl;

import ru.clevertec.repository.ClientRepository;
import ru.clevertec.service.ClientService;

import java.math.BigDecimal;
import java.util.ArrayList;

public class ClientServiceImpl implements ClientService {

    private final ClientRepository clientRepository;

    public ClientServiceImpl(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    @Override
    public ArrayList<String> getClientName(String clientName) {
        return clientRepository.getClientName(clientName);
    }

    @Override
    public String replenishingBalance(long account, BigDecimal sum) {
        return clientRepository.replenishingBalance(account, sum);
    }

    @Override
    public String decreasingBalance(long account, BigDecimal sum) {
        return clientRepository.decreasingBalance(account, sum);
    }

    @Override
    public String movingToAnotherClient(long account, long account2, BigDecimal sum) {
        return clientRepository.movingToAnotherClient(account, account2, sum);
    }
}
