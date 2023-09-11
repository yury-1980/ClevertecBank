package ru.clevertec.repository.requests;

public class RequestsSQL {

    public static final String GET_CLIENT = "SELECT client.id, client.client_name, a.id , a.account, a.balance," +
            " b.id, bank_name " +
            "FROM client " +
            "left outer join public.account a on client.id = a.client_id " +
            "left outer join public.bank b on b.id = a.bank_id " +
            "WHERE client_name = ?";

    public static final String GET_ACCOUNT = "SELECT id, account, balance\n" +
            "FROM account\n" +
            "WHERE account.account = ?";
    public static final String UPDATE_BALANCE = "UPDATE public.account\n" +
            "SET balance = ? WHERE id = ?";
    public static final String GET_ACCOUNT_ALL = "SELECT  id, balance\n" +
            "FROM account WHERE id > ?;";
}