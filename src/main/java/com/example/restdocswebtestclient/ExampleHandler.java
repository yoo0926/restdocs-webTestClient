package com.example.restdocswebtestclient;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Component
@RequiredArgsConstructor
public class ExampleHandler {
    private final ObjectMapper objectMapper;

    public Mono<ServerResponse> listAll(ServerRequest serverRequest){
        ArrayNode list = objectMapper.convertValue(getUser(), ArrayNode.class);
        ObjectNode result = JsonNodeFactory.instance.objectNode()
                .set("result", list);
        return ServerResponse.ok().bodyValue(result);
    }

    private List<User> getUser() {
        return IntStream.range(1, 10).boxed()
                .map(i -> User.builder()
                        .id("id_"+i)
                        .name("name_"+i)
                        .password("password_"+i)
                        .build())
                .collect(Collectors.toList());
    }
}
