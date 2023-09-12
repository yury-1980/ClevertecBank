package ru.clevertec.controller.servlets;

import com.google.gson.Gson;
import ru.clevertec.repository.impl.AccountRepositoryImpl;
import ru.clevertec.service.ExaminationService;
import ru.clevertec.service.impl.AccountServiceImpl;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;

@WebServlet("/v1/perevod")
public class TransferServlet extends HttpServlet {

    private static final String ACCOUNT1 = "account1";
    private static final String ACCOUNT2 = "account2";
    private static final String SUM = "sum";

     /**
     *перевод средств
     */
    public void doPut(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        AccountServiceImpl clientService = new AccountServiceImpl(new AccountRepositoryImpl());
        ExaminationService.checksBalance();

        final int CREATED = 201;
        long account1 = Long.parseLong(req.getParameter(ACCOUNT1));
        long account2 = Long.parseLong(req.getParameter(ACCOUNT2));
        BigDecimal sum = new BigDecimal(req.getParameter(SUM));

        String json = new Gson().toJson(clientService.movingToAnotherClient(account1, account2, sum));

        try (PrintWriter writer = resp.getWriter()) {
            writer.println("Query result:" + account1);
            writer.println("Query result:" + account2);
            writer.println();
            writer.write(json);

            resp.setStatus(CREATED);
        }
    }
}
