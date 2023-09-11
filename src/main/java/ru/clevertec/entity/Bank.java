package ru.clevertec.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigInteger;
import java.util.ArrayList;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Bank {

    private BigInteger id;
    private String bankName;

    private ArrayList<Client> clients = new ArrayList<>();
    private ArrayList<Account> accounts = new ArrayList<>();
}
