package ru.clevertec.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigInteger;
import java.util.ArrayList;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Client {

    private BigInteger id;
    private String clientName;

    private ArrayList<Account> accounts = new ArrayList<>();
}