package com.bb.video.controller;

import com.baomidou.mybatisplus.plugins.Page;
import com.bb.video.common.constant.Cons;
import com.bb.video.common.task.ScheduledTask;
import com.bb.video.common.vo.Resp;
import com.bb.video.model.Video;
import com.bb.video.model.VideoCn;
import com.bb.video.service.VideoCnService;
import com.bb.video.vo.req.CollectVideoReq;
import com.bb.video.vo.req.SearchVideoReq;
import graphql.ExecutionResult;
import graphql.GraphQL;
import graphql.PublicApi;
import graphql.schema.GraphQLSchema;
import graphql.schema.StaticDataFetcher;
import graphql.schema.idl.RuntimeWiring;
import graphql.schema.idl.SchemaGenerator;
import graphql.schema.idl.SchemaParser;
import graphql.schema.idl.TypeDefinitionRegistry;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.time.Instant;

import static graphql.schema.idl.RuntimeWiring.newRuntimeWiring;

/**
 * Created by LiangyinKwai on 2019-06-05.
 */

@Api(description = "视频资源控制")
@RestController
@RequestMapping("video")
public class VideoController {

    @Autowired
    private ScheduledTask scheduledTask;

    @Autowired
    private VideoCnService videoService;

    @ApiOperation("采集视频")
    @PostMapping("collect")
    public Resp collectVideo(@RequestBody CollectVideoReq collectVideoReq) {
        return videoService.collectVideo(collectVideoReq);
    }

    @GetMapping("search")
    public Resp<Page<VideoCn>> searchVideo(@ModelAttribute SearchVideoReq searchVideoReq) {
        return videoService.searchVideo(searchVideoReq);
    }

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
    @GetMapping("collect/test/{platNo}")
    public ResponseEntity collectVideoTest(@PathVariable byte platNo) {
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
