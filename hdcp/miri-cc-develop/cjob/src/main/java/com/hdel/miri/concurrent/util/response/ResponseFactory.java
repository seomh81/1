package com.hdel.miri.concurrent.util.response;

import com.google.gson.GsonBuilder;
import com.hdel.miri.concurrent.util.datetime.LocalDateTimeSerializer;
import lombok.Builder;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;

/**
 * ResponseFactory
 * */
public class ResponseFactory{

    @Builder
    @Getter
    public static final class Response<V, S> {
        private HttpStatus status;
        private int code;
        private String message;
        private ResponseBody response;
        private LocalDateTime timestamp;
    }

    public static String convertJson(Object object){
        return new GsonBuilder().registerTypeAdapter(LocalDateTime.class,new LocalDateTimeSerializer()).setPrettyPrinting().create().toJson(object);
    }

    public static ResponseEntity create(
            HttpStatus status,
            int code,
            String message,
            ResponseBody response
    ){
        return ResponseEntity.ok(Response.builder()
                        .status(status)
                        .code(code)
                        .message(message)
                        .response(response)
                        .timestamp(LocalDateTime.now())
                .build());
    }

    public static ServletOutputStream setStatus(
            HttpServletResponse response,
            HttpStatus status,
            int code
    ) throws IOException {
        response.setStatus(status.value());
        response.setContentType(MediaType.APPLICATION_JSON.toString());
        return response.getOutputStream();
    }
    public static void streamToJson(
            HttpServletResponse servletResponse,
            HttpStatus status,
            int code,
            String message,
            ResponseBody response) throws IOException {
        setStatus(servletResponse,status,code).write(ResponseFactory.convertJson(Response.builder()
                .status(status)
                .code(code)
                .message(message)
                .response(response)
                .timestamp(LocalDateTime.now())
                .build()).getBytes(StandardCharsets.UTF_8));
    }
}
