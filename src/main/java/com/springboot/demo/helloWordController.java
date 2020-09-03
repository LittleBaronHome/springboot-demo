package com.springboot.demo;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class helloWordController {

    @GetMapping("/hello/{name}")
    public String helloWord(@PathVariable String name) {
        return "hello " + name;
    }
}
