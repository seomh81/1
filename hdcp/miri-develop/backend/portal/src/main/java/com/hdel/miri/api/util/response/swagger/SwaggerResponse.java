package com.hdel.miri.api.util.response.swagger;

import com.hdel.miri.api.domain.portfolio.Portfolio;
import com.hdel.miri.api.util.response.ResultCode;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import java.time.LocalDateTime;
import java.util.List;

public class SwaggerResponse {

    @Getter
    public static class SwaggerSuccessCapsule<T> {
        @Schema(example = "OK")
        private HttpStatus status;
        @Schema(example = "200")
        private int code;
        @Schema(example = "요청하신 처리가 완료 되었습니다.")
        private String message;
        private SwaggerSuccessResponse<T> response;
        private LocalDateTime timestamp;
    }

    @Getter
    public static class SwaggerSuccessResponse<T> {
        @Schema(example = "success")
        private String result;
        private T data;
        @Schema(example = "null")
        private String because;
    }

    @Getter
    public static class SwaggerSuccessMailCapsule<T> {
        @Schema(example = "OK")
        private HttpStatus status;
        @Schema(example = "200")
        private int code;
        @Schema(example = "요청하신 처리가 완료 되었습니다.")
        private String message;
        private SwaggerSuccessResponse<T> response;
        private LocalDateTime timestamp;
    }

    @Getter
    public static class SwaggerSuccessMailResponse<T> {
        @Schema(example = "success")
        private String result;
        @Schema(example = "메일로 발송 되었습니다.")
        private String data;
        @Schema(example = "null")
        private String because;
    }

    @Getter
    public static class SwaggerBadRequestResponse {
        @Schema(example = "BAD_REQUEST")
        private HttpStatus status;
        @Schema(example = "400")
        private int code;
        @Schema(example = "유효하지 않은 요청입니다.")
        private String message;
        private LocalDateTime timestamp;
    }

    @Getter
    public static class SwaggerInternalErrorResponse {
        @Schema(example = "INTERNAL_SERVER_ERROR")
        private HttpStatus status;
        @Schema(example = "500")
        private int code;
        @Schema(example = "서버 내부에서 오류가 발생하였습니다.")
        private String message;
        @Schema(example = "null")
        private Object response;
        private LocalDateTime timestamp;
    }
}
