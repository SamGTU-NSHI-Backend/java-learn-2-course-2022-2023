package ru.nshi.api;

import com.fasterxml.jackson.databind.ObjectMapper;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.concurrent.atomic.AtomicInteger;

public class TestServlet extends HttpServlet {
    public static final String JSON_VALUE = "application/json";
    private AtomicInteger counter;
    private Set<String> threadSets;
    private ObjectMapper mapper;

    @Override
    public void destroy() {
        System.out.println("Destroy method called and threads size: ");
        System.out.println(threadSets.size());
    }

    @Override
    public void init() throws ServletException {
        System.out.println("Init method called");
        counter = new AtomicInteger(0);
        threadSets = new ConcurrentSkipListSet<>();
        mapper = new ObjectMapper();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("req.getServletPath() = " + req.getServletPath());
        System.out.println("req.getPathInfo() = " + req.getPathInfo());
        System.out.println("req.getParameterValues(\"key\") = " +
            Arrays.toString(req.getParameterValues("key")));

        System.out.println("req.getParameterMap() = " + req.getParameterMap());

        String threadName = Thread.currentThread().getName();
        System.out.println("Request id: " + counter.incrementAndGet()
            + " Thread name: " + threadName);
        resp.getWriter().println("<h1>Hello servlet</h1>Simple Text");

        threadSets.add(threadName);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType(JSON_VALUE);
        if (!req.getContentType().contains(JSON_VALUE)) {
            resp.setStatus(400);
            mapper.writeValue(resp.getWriter(), Map.of("error", "Expected " + JSON_VALUE));
            return;
        }

        Message value = mapper.readValue(req.getInputStream(), Message.class);

        if (value == null || value.getValue() == null) {
            resp.setStatus(400);
            mapper.writeValue(resp.getWriter(), Map.of("error", "message is null"));
            return;
        }

        String message = value.getValue();
        mapper.writeValue(resp.getOutputStream(), new Message(message.toUpperCase()));
        resp.setStatus(200);
    }
}
