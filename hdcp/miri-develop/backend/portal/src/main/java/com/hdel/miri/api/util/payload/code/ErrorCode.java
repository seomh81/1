package com.hdel.miri.api.util.payload.code;

import lombok.Getter;

@Getter
public enum ErrorCode {
    BAD_REQUEST(400, 400, "e.badrequest"),
    UNAUTHORIZED(400, 401, "e.unauthorized"),
    NOT_FOUND(400, 404, "e.notfound"),
    INVALID_PARAMETER(400, 460, "e.invalidparameter"),
    LACK_OF_PARAMETER(400, 461, "e.lackofparameter"),
    EXPIRED_TOKEN(400, 462, "e.expiredtoken"),
    INVALID_TOKEN(400, 463, "e.invalidtoken"),
    BAD_CREDENTIAL(400, 464, "e.badcredential"),
    NOT_FOUND_TOKEN(400, 464, "e.notfoundtoken"),
    ALREADY_REGISTED_USER(200, 465, "e.alreadyregisteduser"),
    NORMAL(200, 200, "m.success"),
    FILE_UPLOAD_FAIL(400, 466, "e.fileuploadfail"),
    FAILT_TO_SEND_MAIL(400, 499, "e.failtosendemail"),
    USER_NOT_FOUND(400, 467, "e.usernotfound"),
    INVALID_ACCOUNT_TYPE(400, 468, "e.invalidaccounttype"),
    ALREADY_REGISTED_KEY(200, 465, "e.alreadyregistedkey");

    private final int status;
    private final int code;
    private final String message;

    ErrorCode(final int status, final int code, final String message) {
        this.status = status;
        this.code = code;
        this.message = message;
    }
}
