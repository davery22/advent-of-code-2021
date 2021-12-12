package advent.of.code.main;

import advent.of.code.day01.Day01;
import advent.of.code.day02.Day02;
import advent.of.code.day03.Day03;
import advent.of.code.day04.Day04;
import advent.of.code.day05.Day05;
import advent.of.code.day06.Day06;
import advent.of.code.day07.Day07;
import advent.of.code.day08.Day08;
import advent.of.code.day09.Day09;
import advent.of.code.day10.Day10;
import advent.of.code.day11.Day11;
import advent.of.code.day12.Day12;
import advent.of.code.day13.Day13;
import advent.of.code.io.Input;
import advent.of.code.io.Output;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.Executors;
import java.util.function.BiConsumer;

public class HttpRunner {
    public void run() throws IOException {
        var host = "localhost";
        var port = 8080;
        var basePath = "http://%s:%d".formatted(host, port);
        var server = HttpServer.create();
        server.bind(new InetSocketAddress(host, port), 0);
    
        // Note: Paths like "/foo/bar/" will capture requests like "/foo/bar/anything/else/".
        // This is useful to support things like query params, but needs to be considered to avoid capturing nonsense.
        server.createContext("/", exchange -> {
            try (exchange) {
                consumeInput(exchange.getRequestBody());
                if (!getRequestSuffix(exchange).isEmpty()) {
                    sendResponse(exchange, 404, simpleHtmlMessage(
                        "404 Not Found",
                        "No context found for request"
                    ));
                } else if (!"GET".equals(exchange.getRequestMethod())) {
                    sendResponse(exchange, 405, simpleHtmlMessage(
                        "405 Method Not Allowed",
                        "Please use GET for this endpoint"
                    ));
                } else {
                    sendResponse(exchange, 200, simpleHtmlMessage(
                        "Welcome to my Advent of Code 2021 server!",
                        "Try POST-ing a well-formed input to any endpoint (eg <a href=\"%s/day/1/part1/\">%s/day/1/part1/</a>)".formatted(basePath, basePath)
                    ));
                }
            }
        });
        server.createContext("/day/1/part1/",  handler(Day01::part1));
        server.createContext("/day/1/part2/",  handler(Day01::part2));
        server.createContext("/day/2/part1/",  handler(Day02::part1));
        server.createContext("/day/2/part2/",  handler(Day02::part2));
        server.createContext("/day/3/part1/",  handler(Day03::part1));
        server.createContext("/day/3/part2/",  handler(Day03::part2));
        server.createContext("/day/4/part1/",  handler(Day04::part1));
        server.createContext("/day/4/part2/",  handler(Day04::part2));
        server.createContext("/day/5/part1/",  handler(Day05::part1));
        server.createContext("/day/5/part2/",  handler(Day05::part2));
        server.createContext("/day/6/part1/",  handler(Day06::part1));
        server.createContext("/day/6/part2/",  handler(Day06::part2));
        server.createContext("/day/7/part1/",  handler(Day07::part1));
        server.createContext("/day/7/part2/",  handler(Day07::part2));
        server.createContext("/day/8/part1/",  handler(Day08::part1));
        server.createContext("/day/8/part2/",  handler(Day08::part2));
        server.createContext("/day/9/part1/",  handler(Day09::part1));
        server.createContext("/day/9/part2/",  handler(Day09::part2));
        server.createContext("/day/10/part1/", handler(Day10::part1));
        server.createContext("/day/10/part2/", handler(Day10::part2));
        server.createContext("/day/11/part1/", handler(Day11::part1));
        server.createContext("/day/11/part2/", handler(Day11::part2));
        server.createContext("/day/12/part1/", handler(Day12::part1));
        server.createContext("/day/12/part2/", handler(Day12::part2));
        server.createContext("/day/13/part1/", handler(Day13::part1));
        server.createContext("/day/13/part2/", handler(Day13::part2));
        
        server.setExecutor(Executors.newVirtualThreadPerTaskExecutor());
        server.start();
        
        System.out.printf("server running at %s/%n", basePath);
    }
    
    private void consumeInput(InputStream input) throws IOException {
        if (input.read() == -1) return;
        try {
            while (input.read() != -1) {}
        } catch (IOException ignored) {
        }
    }
    
    private HttpHandler handler(BiConsumer<Input, Output> solver) {
        return exchange -> {
            try (exchange) {
                if (!getRequestSuffix(exchange).isEmpty()) {
                    sendResponse(exchange, 404, simpleHtmlMessage(
                        "404 Not Found",
                        "No context found for request"
                    ));
                } else if (!"POST".equals(exchange.getRequestMethod())) {
                    sendResponse(exchange, 405, simpleHtmlMessage(
                        "405 Method Not Allowed",
                        "Please use POST with a well-formed input in the request body"
                    ));
                } else try (var in = Input.of(exchange.getRequestBody())) {
                    var response = new ByteArrayOutputStream();
                    solver.accept(in, Output.of(response));
                    sendResponse(exchange, 200, response);
                } catch (Exception e) {
                    sendResponse(exchange, 400, simpleHtmlMessage(
                        "400 Bad Request",
                        "Please make sure your input is well-formed"
                    ));
                }
            }
        };
    }
    
    private String getRequestSuffix(HttpExchange exchange) {
        var prefixSize = exchange.getHttpContext().getPath().length();
        return exchange.getRequestURI().toString().substring(prefixSize);
    }
    
    private String simpleHtmlMessage(String header, String body) {
        return """
            <html>
              <body>
                <h1>%s</h1>
                <p>%s</p>
              </body>
            </html>""".formatted(header, body);
    }
    
    private void sendResponse(HttpExchange exchange, int rCode, String response) throws IOException {
        var bytes = response.getBytes(StandardCharsets.UTF_8);
        exchange.sendResponseHeaders(rCode, bytes.length);
        exchange.getResponseBody().write(bytes);
    }
    
    private void sendResponse(HttpExchange exchange, int rCode, ByteArrayOutputStream out) throws IOException {
        exchange.sendResponseHeaders(rCode, out.size());
        out.writeTo(exchange.getResponseBody());
    }
}

