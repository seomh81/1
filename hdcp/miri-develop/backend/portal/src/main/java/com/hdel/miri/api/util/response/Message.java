package com.hdel.miri.api.util.response;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hdel.miri.api.util.request.RequestWrapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Locale;

@Slf4j
@Component
@RequiredArgsConstructor
public class Message {
    private final MessageSource messageSource;

    private Locale locale(String loc){
        return new Locale(loc);
    }

    private Locale locale(HttpServletRequest r) throws IOException {
        RequestWrapper request = new RequestWrapper(r);
        if(request.getMethod().equals(HttpMethod.GET.name())){
            String lo = (request.getParameter("userLocale")==null?"ko_kr":request.getParameter("userLocale"));
            return locale(lo);
        }else {
            byte[] binary = request.getInputStream().readAllBytes();
            ObjectMapper objectMapper = new ObjectMapper();
            HashMap<String, Object> req = null;
            if(0 < binary.length){
                TypeReference<HashMap<String,Object>> typeRef = new TypeReference<>() {};
                req = objectMapper.readValue(new String(binary, StandardCharsets.UTF_8), typeRef);
            }else {
                req = new HashMap<>();
                req.put("userLocale","ko_kr");
            }
            return locale(String.valueOf(req.get("userLocale")));
        }
    }

    /**
     * Success Message
     * */
    public String withSuccess(HttpServletRequest request) throws IOException {
        return messageSource.getMessage("m.success",null,locale(request));
    }
    public String withSuccess(String locale){
        return messageSource.getMessage("m.success",null,locale(locale));
    }
    /**
     * BadRequest Message
     * */
    public String withBadRequest(HttpServletRequest request) throws IOException {
        return messageSource.getMessage("m.badrequest",null,locale(request));
    }
    public String withBadRequest(String locale){
        return messageSource.getMessage("m.badrequest",null,locale(locale));
    }

    /**
     * ExpiredToken Message
     * */
    public String withExpiredToken(HttpServletRequest request) throws IOException {
        return messageSource.getMessage("m.expired.token",null,locale(request));
    }
    public String withExpiredToken(String locale){
        return messageSource.getMessage("m.expired.token",null,locale(locale));
    }

    /**
     * RefreshExpiredToken Message
     * */
    public String withRefreshExpiredToken(HttpServletRequest request) throws IOException {
        return messageSource.getMessage("m.expired.refresh.token",null,locale(request));
    }
    public String withRefreshExpiredToken(String locale){
        return messageSource.getMessage("m.expired.refresh.token",null,locale(locale));
    }

    /**
     * AlreadyToken Message
     * */
    public String withAlreadyToken(HttpServletRequest request) throws IOException {
        return messageSource.getMessage("m.already.token",null,locale(request));
    }
    public String withAlreadyToken(String locale){
        return messageSource.getMessage("m.already.token",null,locale(locale));
    }

    /**
     * UnauthenticationToken Message
     * */
    public String withUnauthentication(HttpServletRequest request) throws IOException {
        return messageSource.getMessage("m.unauthentication",null,locale(request));
    }
    public String withUnauthentication(String locale){
        return messageSource.getMessage("m.unauthentication",null,locale(locale));
    }
    /**
     * Unauthorization Message
     * */
    public String withUnauthorization(HttpServletRequest request) throws IOException {
        return messageSource.getMessage("m.unauthorization",null,locale(request));
    }
    public String withUnauthorization(String locale){
        return messageSource.getMessage("m.unauthorization",null,locale(locale));
    }

    /**
     * Verified Email Message
     * */
    public String withVerifiedEmailSuccess(HttpServletRequest request) throws IOException {
        return messageSource.getMessage("m.verified.mail.success",null,locale(request));
    }
    public String withVerifiedEmailFailure(HttpServletRequest request) throws IOException {
        return messageSource.getMessage("m.verified.mail.failure",null,locale(request));
    }
    public String withVerifiedEmailSuccess(String locale) {
        return messageSource.getMessage("m.verified.mail.success",null,locale(locale));
    }
    public String withVerifiedEmailFailure(String locale) {
        return messageSource.getMessage("m.verified.mail.failure",null,locale(locale));
    }

    /**
     * SignOut Message
     * */
    public String withSignOut(HttpServletRequest request) throws IOException {
        return messageSource.getMessage("m.signout",null,locale(request));
    }
    public String withSignOut(String locale){
        return messageSource.getMessage("m.signout",null,locale(locale));
    }

    /**
     * RequiredSignIn Message withRequiredSignIn
     * */
    public String withRequiredSignIn(HttpServletRequest request) throws IOException {
        return messageSource.getMessage("m.required.singin",null,locale(request));
    }
    public String withRequiredSignIn(String locale){
        return messageSource.getMessage("m.required.singin",null,locale(locale));
    }

    public String withRequiredApprove(String locale){
        return messageSource.getMessage("m.required.approve",null,locale(locale));
    }

    public String withRequiredEmailConfirm(String locale){
        return messageSource.getMessage("m.required.email.confirm",null,locale(locale));
    }

    /**
     * IssueAccessToken Message
     * */
    public String withIssueAccessToken(HttpServletRequest request) throws IOException {
        return messageSource.getMessage("m.issuer.accesstoken",null,locale(request));
    }
    public String withIssueAccessToken(String locale){
        return messageSource.getMessage("m.issuer.accesstoken",null,locale(locale));
    }

    /**
     * InternalError Message
     * */
    public String withInternalError(HttpServletRequest request) throws IOException {
        return messageSource.getMessage("m.internal.error",null,locale(request));
    }
    public String withInternalError(String locale){
        return messageSource.getMessage("m.internal.error",null,locale(locale));
    }

    /**
     * BadValidate Message
     * */
    public String withBadValidate(HttpServletRequest request) throws IOException {
        return messageSource.getMessage("m.bad.validate",null,locale(request));
    }
    public String withBadValidate(String locale){
        return messageSource.getMessage("m.bad.validate",null,locale(locale));
    }

    /**
     * SendMail Message
     * */
    public String withSendMail(HttpServletRequest request) throws IOException {
        return messageSource.getMessage("m.sendmail",null,locale(request));
    }
    public String withSendMail(String locale){
        return messageSource.getMessage("m.sendmail",null,locale(locale));
    }

    /**
     * FailureSendMail Message
     * */
    public String withFailureSendMail(HttpServletRequest request) throws IOException {
        return messageSource.getMessage("m.failure.sendmail",null,locale(request));
    }
    public String withFailureSendMail(String locale){
        return messageSource.getMessage("m.failure.sendmail",null,locale(locale));
    }

    /**
     * FailureEncoding Message
     * */
    public String withFailureEncoding(HttpServletRequest request) throws IOException {
        return messageSource.getMessage("m.failure.unsupportencoding",null,locale(request));
    }
    public String withFailureEncoding(String locale){
        return messageSource.getMessage("m.failure.unsupportencoding",null,locale(locale));
    }

    /**
     * FailureAuth Message
     * */
    public String withFailureAuth(HttpServletRequest request) throws IOException {
        return messageSource.getMessage("m.failure.auth",null,locale(request));
    }
    public String withFailureAuth(String locale){
        return messageSource.getMessage("m.failure.auth",null,locale(locale));
    }

    /**
     * becauseNotInsert Message
     * */
    public String becauseNotInsert(HttpServletRequest request) throws IOException {
        return messageSource.getMessage("because.not.insert",null,locale(request));
    }
    public String becauseNotInsert(String locale){
        return messageSource.getMessage("because.not.insert",null,locale(locale));
    }

    /**
     * becauseNotUpdate Message
     * */
    public String becauseNotUpdate(HttpServletRequest request) throws IOException {
        return messageSource.getMessage("because.not.update",null,locale(request));
    }
    public String becauseNotUpdate(String locale){
        return messageSource.getMessage("because.not.update",null,locale(locale));
    }

    /**
     * becauseNotDelete Message
     * */
    public String becauseNotDelete(HttpServletRequest request) throws IOException {
        return messageSource.getMessage("because.not.delete",null,locale(request));
    }
    public String becauseNotDelete(String locale){
        return messageSource.getMessage("because.not.delete",null,locale(locale));
    }

    /**
     * becauseNotDelete Message
     * */
    public String becauseNotAuth(HttpServletRequest request) throws IOException {
        return messageSource.getMessage("because.not.auth",null,locale(request));
    }
    public String becauseNotAuth(String locale){
        return messageSource.getMessage("because.not.auth",null,locale(locale));
    }

    /**
     * becauseFailureSelect Message
     * */
    public String becauseFailureSelect(HttpServletRequest request) throws IOException {
        return messageSource.getMessage("because.fail.select",null,locale(request));
    }
    public String becauseFailureSelect(String locale){
        return messageSource.getMessage("because.fail.select",null,locale(locale));
    }

    /**
     * becauseFailureInsert Message
     * */
    public String becauseFailureInsert(HttpServletRequest request) throws IOException {
        return messageSource.getMessage("because.fail.insert",null,locale(request));
    }
    public String becauseFailureInsert(String locale){
        return messageSource.getMessage("because.fail.insert",null,locale(locale));
    }

    /**
     * becauseFailureDelete Message
     * */
    public String becauseFailureUpdate(HttpServletRequest request) throws IOException {
        return messageSource.getMessage("because.fail.update",null,locale(request));
    }
    public String becauseFailureUpdate(String locale){
        return messageSource.getMessage("because.fail.update",null,locale(locale));
    }

    /**
     * becauseFailureDelete Message
     * */
    public String becauseFailureDelete(HttpServletRequest request) throws IOException {
        return messageSource.getMessage("because.fail.delete",null,locale(request));
    }
    public String becauseFailureDelete(String locale){
        return messageSource.getMessage("because.fail.delete",null,locale(locale));
    }

    /**
     * becauseFailureDelete Message
     * */
    public String becauseFailureTransferFile(HttpServletRequest request) throws IOException {
        return messageSource.getMessage("because.fail.transfer.file",null,locale(request));
    }
    public String becauseFailureTransferFile(String locale){
        return messageSource.getMessage("because.fail.transfer.file",null,locale(locale));
    }

    /**
     * becauseNotExistFile Message
     * */
    public String becauseNotExistFile(HttpServletRequest request) throws IOException {
        return messageSource.getMessage("because.not.exist.file",null,locale(request));
    }
    public String becauseNotExistFile(String locale){
        return messageSource.getMessage("because.not.exist.file",null,locale(locale));
    }

    /**
     * becauseNotExistFile Message
     * */
    public String becauseFailureSendMail(HttpServletRequest request) throws IOException {
        return messageSource.getMessage("because.fail.send.email",null,locale(request));
    }
    public String becauseFailureSendMail(String locale){
        return messageSource.getMessage("because.fail.send.email",null,locale(locale));
    }

    /**
     * becauseAlreadyRegisted Message
     * */
    public String becauseAlreadyRegisted(String locale){
        return messageSource.getMessage("because.fail.account.exist",null,locale(locale));
    }

    public String becauseAvailableAccount(String locale){
        return messageSource.getMessage("because.success.account.exist",null,locale(locale));
    }
}
