package ru.nshi.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MimeTypeUtils;
import org.springframework.web.bind.annotation.*;
import ru.nshi.error.MessageValidationException;
import ru.nshi.model.Error;
import ru.nshi.model.Message;
import ru.nshi.service.MessageService;

import javax.annotation.PreDestroy;

@RestController
@RequestMapping(SampleController.MAPPING)
public class SampleController {
    public static final String MAPPING = "/sample";

    @Autowired
    private MessageService service;

    @Autowired
    private ObjectMapper mapper;

    //    @RequestMapping(path = "/hello", method = {RequestMethod.GET, RequestMethod.HEAD})
    @GetMapping(path = "/requestParam", produces = {MimeTypeUtils.APPLICATION_JSON_VALUE})
    public String getRequestParam(@RequestParam(required = false, name = "first") String firstParam) {
        System.out.println("First param: " + firstParam);
        return firstParam;
    }

    @GetMapping("/pathVar/{pathVar1}")
    public String getPathVariable(@PathVariable(name = "pathVar1") String pathVar1) {
        return pathVar1;
    }

    @GetMapping("/rsEntity")
    public ResponseEntity<?> getEntity() {
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
            .header("header-test", "test")
            .build();
    }

    @GetMapping("/header")
    public ResponseEntity<?> getHeader(@RequestHeader(value = "hello", defaultValue = "12") Integer helloHeader) {
        return ResponseEntity.ok()
            .header("header-test", helloHeader.toString())
            .build();
    }

    @GetMapping("/message")
    @PreDestroy
    public Message getMessage() {
        return new Message("message");
    }

    @PostMapping
    public Message createMessage(@RequestBody(required = false) Message message) {
        if (message == null || message.getValue() == null) {
            throw new MessageValidationException("message is null");
        }
        message = service.doHandleMessage(message);
        return message;
    }

    @ExceptionHandler({MessageValidationException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Error handleNotFound(MessageValidationException ex) {
        return new Error(ex.getMessage());
    }
}
