package com.maynormoe.takeout.exception;

import com.maynormoe.takeout.common.Results;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLIntegrityConstraintViolationException;

/**
 * 全局异常处理
 *
 * @author Maynormoe
 */

@RestControllerAdvice
@Slf4j
@ResponseBody
public class GlobalExceptionHandler {

    /**
     * 异常处理
     *
     * @param exception 异常
     * @return Results<String>
     */
    @ExceptionHandler(SQLIntegrityConstraintViolationException.class)
    public Results<String> exceptionHandler(SQLIntegrityConstraintViolationException exception) {
        log.error(exception.getMessage());
        if (exception.getMessage().contains("Duplicate entry")) {
            String[] split = exception.getMessage().split("");
            String msg = split[2] + "已存在";
            return Results.error(msg);
        }
        return Results.error("未知错误");
    }

    @ExceptionHandler(Exception.class)
    public Results<String> exceptionHandler(Exception exception) {
        log.error(exception.getMessage());
        return Results.error(exception.getMessage());
    }
}
