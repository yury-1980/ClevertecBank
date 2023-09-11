package ru.clevertec.controller.servlets;

import com.google.gson.Gson;
import ru.clevertec.repository.impl.ClientRepositoryImpl;
import ru.clevertec.service.impl.ClientServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;

@WebServlet("/v1/perevod")
public class TransferServlet extends HttpServlet {

    private static final String ACCOUNT1 ="account1";
    private static final String ACCOUNT2 ="account2";
    private static final String SUM ="sum";

    // перевод средств
    public void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        ClientServiceImpl clientService = new ClientServiceImpl(new ClientRepositoryImpl());

        final int CREATED = 201;
        long account = Long.parseLong(req.getParameter(ACCOUNT1));
        long account2 = Long.parseLong(req.getParameter(ACCOUNT2));
        BigDecimal sum = new BigDecimal(req.getParameter(SUM));

        String[] json = new Gson().toJson(clientService.movingToAnotherClient(account, account2, sum)).split(",");

        try (PrintWriter writer = resp.getWriter()) {
            writer.println("Query result:" + account);
            writer.println();

            for (String s : json) {
                writer.println();
                writer.write(s);
            }

            resp.setStatus(CREATED);
        }
    }
}
