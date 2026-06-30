package com.metrowest.controllers;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.intellij.lang.annotations.Language;
import org.springframework.data.domain.Example;
import org.springframework.data.javapoet.LordOfTheStrings;
import org.springframework.expression.Expression;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.nio.ByteBuffer;

@RestController
@RequestMapping("/utils")
public class UtilController
{
    private static final SpelExpressionParser PARSER = new SpelExpressionParser();

    private void consume_object(Object builder)
    {
        System.out.println("consumed object class = " + builder.getClass().getSimpleName());
    }

    @Language("html")
    @SuppressWarnings("HtmlUnknownTarget")
    private static final String TEST_BODY = """
        <!DOCTYPE html>
        <html lang="en">
        <body>
            <form method="post" action="/echo">
                <input type="text" name="input">
                <input type="submit">
            </form>
        </body>
        </html>
        """;

    @GetMapping("/hey")
    public String hey()
    {
        return TEST_BODY;
    }

    @PostMapping("/echo")
    public void echo(HttpServletRequest request, HttpServletResponse response) throws IOException
    {
        var buffer = ByteBuffer.allocate(1024);
        request.getInputStream().read(buffer);
        response.getOutputStream().write(buffer);
        response.setStatus(200);
    }

    @PostMapping(value = "/echo_probe", produces = MediaType.TEXT_HTML_VALUE)
    public String echo_probe(@RequestBody String probe)
    {
        return Example.of(probe).getProbe();
    }

    @PostMapping(path = "/set_value", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public String set_value(@RequestParam String expression)
    {
        Expression parsedExpression = PARSER.parseExpression("1 + 1");
        parsedExpression.setValue((Object) null, expression);
        System.out.println(parsedExpression.getExpressionString());
        return "OK";
    }

    @PostMapping("/lord_invoke_builder")
    public String lord_invoke_builder(@RequestBody String format, @RequestBody String[] args)
    {
        var builder = LordOfTheStrings.invoke(format, (Object[]) args);
        consume_object(builder);
        return "lord_invoke_builder endpoint executed";
    }
}
