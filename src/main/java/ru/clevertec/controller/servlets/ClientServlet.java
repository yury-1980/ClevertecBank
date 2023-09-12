package ru.clevertec.controller.servlets;

import com.google.gson.Gson;
import ru.clevertec.repository.impl.AccountRepositoryImpl;
import ru.clevertec.service.impl.AccountServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;

@WebServlet("/v1")
public class ClientServlet extends HttpServlet {

    private static final String NAME = "name";
    private static final String ACCOUNT = "account";
    private static final String SUM = "sum";
    private static final String ZNAK = "znak";
    private static final String PLUS = "plus";
    private static final String MINUS = "minus";
    private static final String QUERY_RESULT_ACCOUNT = "Query result account: ";
    private static final String QUERY_RESULT_NAME = "Query result name: ";

    // информация о клиенте
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {


        AccountServiceImpl accountService = new AccountServiceImpl(new AccountRepositoryImpl());

        final int CREATED = 201;
        String name = req.getParameter(NAME);

        String json = new Gson().toJson(accountService.getAccountAll(name));

        try (PrintWriter writer = resp.getWriter()) {
            writer.println(QUERY_RESULT_NAME + name);
            writer.println();
            writer.write(json);

            resp.setStatus(CREATED);
        }
    }

    @Override
    // операции пополнения и снятия
    public void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        AccountServiceImpl clientService = new AccountServiceImpl(new AccountRepositoryImpl());

        String json = null;
        final int CREATED = 201;
        String znak = req.getParameter(ZNAK);
        long account = Long.parseLong(req.getParameter(ACCOUNT));
        BigDecimal sum = new BigDecimal(req.getParameter(SUM));

        json = new Gson().toJson("Invalid operation !");

        if (znak.equals(PLUS)) {
            json = new Gson().toJson(clientService.replenishingBalance(account, sum));
        }

        if (znak.equals(MINUS)) {
            json = new Gson().toJson(clientService.decreasingBalance(account, sum));
        }

        try (PrintWriter writer = resp.getWriter()) {
            writer.println(QUERY_RESULT_ACCOUNT + account);
            writer.println();
            writer.write(json);

            resp.setStatus(CREATED);
        }
    }
}
