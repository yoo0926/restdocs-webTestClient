package com.example.restdocswebtestclient;

import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.GET;

@Component
public class ExampleRouter {
    @Bean
    public RouterFunction<ServerResponse> routes(ExampleHandler handler){
        return RouterFunctions.route(GET("/user/listAll"), handler::listAll);
    }
}
