package com.bb.video.controller;

import com.bb.video.common.task.ScheduledTask;
import com.bb.video.common.vo.Resp;
import com.bb.video.service.VideoCnService;
import graphql.ExecutionResult;
import graphql.GraphQL;
import graphql.PublicApi;
import graphql.schema.GraphQLSchema;
import graphql.schema.StaticDataFetcher;
import graphql.schema.idl.RuntimeWiring;
import graphql.schema.idl.SchemaGenerator;
import graphql.schema.idl.SchemaParser;
import graphql.schema.idl.TypeDefinitionRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static graphql.schema.idl.RuntimeWiring.newRuntimeWiring;

/**
 * Created by LiangyinKwai on 2019-06-05.
 */

@RestController
@RequestMapping("api/v1/video")
public class VideoController {

    @Autowired
    private ScheduledTask scheduledTask;

    @Autowired
    private VideoCnService videoService;

    @PublicApi
    @GetMapping("index")
    public Resp index() {
        return videoService.index();
    }

    @PublicApi
    @GetMapping("page")
    public Resp findVideo() {
        return null;
    }

    @PublicApi
    @GetMapping("collect/{platNo}")
    public ResponseEntity collectVideo(@PathVariable byte platNo) {
        if(platNo == 1)
        scheduledTask.collectTask("");
        return ResponseEntity.ok("采集成功");
    }

    public static void main(String[] args) {

        String schema = "type Query{hello: String}";

        SchemaParser schemaParser = new SchemaParser();
        TypeDefinitionRegistry typeDefinitionRegistry = schemaParser.parse(schema);

        RuntimeWiring runtimeWiring = newRuntimeWiring()
                .type("Query", builder -> builder.dataFetcher("hello", new StaticDataFetcher("world")))
                .build();

        SchemaGenerator schemaGenerator = new SchemaGenerator();
        GraphQLSchema graphQLSchema = schemaGenerator.makeExecutableSchema(typeDefinitionRegistry, runtimeWiring);

        GraphQL build = GraphQL.newGraphQL(graphQLSchema).build();
        ExecutionResult executionResult = build.execute("{hello}");

        System.out.println(executionResult.getData().toString());
        // Prints: {hello=world}
    }

}
