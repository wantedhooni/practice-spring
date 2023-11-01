package com.revy.spring3swagger.endpoint;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/example")
public class ExampleController {


    @GetMapping
    public String get() {
        return "get";
    }

    @PostMapping
    public String post() {
        return "post";
    }

    @DeleteMapping
    public String delete() {
        return "delete";
    }

    @PutMapping
    public String put() {
        return "put";
    }

}
