package com.bb.video.common.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 全局异常处理
 * Created by LiangyinKwai on 2018/12/18.
 */

@RestControllerAdvice
@Slf4j
public class RestExceptionController {

    @ExceptionHandler(RuntimeException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity badRequest(RuntimeException e) {
        log.error(e.getMessage(), e);
        return ResponseEntity.ok("服务不可用");
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Object handleException(Exception e){
        log.error(e.getMessage(), e);
        //只有在特殊需求下自定义异常才有必要执行写入操作，一般异常在开发过程中打印出跟踪即可
//        log.error(e.getMessage(),e);
        //对于spring定义的某些数据操作异常 响应信息可能引起字段泄漏  故不返回摘要信息
//        if(e instanceof DataAccessException)
//            return "数据处理失败";
        return null;
    }

}
