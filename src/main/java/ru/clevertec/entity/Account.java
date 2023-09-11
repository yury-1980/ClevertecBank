package ru.clevertec.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigInteger;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Account {

    private BigInteger id;
    private BigInteger account;
    private BigInteger clientId;
    private Integer balance;

}