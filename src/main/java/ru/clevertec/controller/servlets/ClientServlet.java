package ru.clevertec.controller.servlets;

import com.google.gson.Gson;
import ru.clevertec.repository.impl.AccountRepositoryImpl;
import ru.clevertec.repository.impl.ExaminationRepositoryImpl;
import ru.clevertec.service.impl.AccountServiceImpl;
import ru.clevertec.service.impl.ExaminationServiceImpl;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;

@WebServlet("/v1")
public class ClientServlet extends HttpServlet {

    AccountServiceImpl accountService;
    ExaminationServiceImpl examinationServiceImpl;

    @Override
    public void init() {

            accountService = new AccountServiceImpl(new AccountRepositoryImpl());
            getServletContext().setAttribute("accountService", accountService);
            new ExaminationServiceImpl(new ExaminationRepositoryImpl()).checksBalance();
            getServletContext().setAttribute("examinationService", examinationServiceImpl);

    }

    private static final String NAME = "name";
    private static final String ACCOUNT = "account";
    private static final String SUM = "sum";
    private static final String ZNAK = "znak";
    private static final String PLUS = "plus";
    private static final String MINUS = "minus";
    private static final String QUERY_RESULT_ACCOUNT = "Query result account: ";
    private static final String QUERY_RESULT_NAME = "Query result name: ";

    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {

        String name = req.getParameter(NAME);
        String json = new Gson().toJson(accountService.getAccountAll(name));

        try (PrintWriter writer = resp.getWriter()) {
            writer.println(QUERY_RESULT_NAME + name);
            writer.println();
            writer.write(json);

            resp.setStatus(HttpServletResponse.SC_OK);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override

    public void doPut(HttpServletRequest req, HttpServletResponse resp) {

        String json;
        String znak = req.getParameter(ZNAK);
        long account = Long.parseLong(req.getParameter(ACCOUNT));
        BigDecimal sum = new BigDecimal(req.getParameter(SUM));

        json = new Gson().toJson("Invalid operation !");

        if (znak.equals(PLUS)) {
            json = new Gson().toJson(accountService.replenishingBalance(account, sum));
        }

        if (znak.equals(MINUS)) {
            json = new Gson().toJson(accountService.decreasingBalance(account, sum));
        }

        try (PrintWriter writer = resp.getWriter()) {
            writer.println(QUERY_RESULT_ACCOUNT + account);
            writer.println();
            writer.write(json);

            resp.setStatus(HttpServletResponse.SC_OK);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
