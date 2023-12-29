package com.hdel.miri.api.domain.logging;

import com.hdel.miri.api.global.jwt.TokenProvider;
import com.hdel.miri.api.util.request.RequestWrapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mobile.device.Device;
import org.springframework.mobile.device.DeviceUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.sql.Timestamp;
import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class LoggingService {
    private final TokenProvider tokenProvider;
    private final LoggingRepository loggingRepository;

    private String getPlatformType(Device device){
        if(device.isMobile()){
            return "MOBILE";
        }else if(device.isTablet()){
            return "TABLET";
        }else{
            return "DESKTOP";
        }
    }

    private String getRequestValues(String method,HttpServletRequest request) throws IOException {
        if(RequestMethod.GET.name().equals(method)){
            return request.getQueryString();
        }else return new String(request.getInputStream().readAllBytes(),StandardCharsets.UTF_8);
    }

    private String getCurrentUser(){
        try {
            return SecurityContextHolder.getContext().getAuthentication().getName();
        } catch ( NullPointerException e ) {
            return "anonymousUser";
        }
    }

    public void create(HttpServletRequest request){
        LocalDateTime now = LocalDateTime.now();
        Device device = DeviceUtils.getCurrentDevice(request);
        String userId = getCurrentUser();
        Logging.Log logging = new Logging.Log();
                    logging.setUserId(userId);
                    logging.setControllerName(request.getRequestURI());
                    logging.setOutValue("-");
                    logging.setErrorYn("n");
                    logging.setErrorContents("-");
                    logging.setPlatform(device.getDevicePlatform().name());
                    logging.setMobile(getPlatformType(device));
                    logging.setUserAgent(request.getHeader("User-Agent"));
                    logging.setStartDt(Timestamp.valueOf(now));
                    logging.setEndDt(Timestamp.valueOf(now));
                    logging.setCreationUser(userId);
                    logging.setLastupdateUser(userId);
        loggingRepository.insert(logging);
        request.setAttribute("X-MIRI-REQUEST-ID",logging.getLogId());
    }

    public void complete(HttpServletRequest r) throws IOException {
        RequestWrapper request = new RequestWrapper(r);
        LocalDateTime now = LocalDateTime.now();
        Device device = DeviceUtils.getCurrentDevice(request);
        String userId = getCurrentUser();


        String inValue = getRequestValues(request.getMethod(),request) == null ? "   " : getRequestValues(request.getMethod(),request);
        int lastIdx = 1;

        if(getRequestValues(request.getMethod(),request) !=null) {
            lastIdx = getRequestValues(request.getMethod(),request).length() > 1999 ? 1999 : getRequestValues(request.getMethod(),request).length();
        }

        Logging.Log logging = new Logging.Log();
                    logging.setLogId((BigDecimal)request.getAttribute("X-MIRI-REQUEST-ID"));
                    logging.setUserId(userId);
                    logging.setControllerName(request.getRequestURI());
                    logging.setInValue( inValue.substring(0, lastIdx));
                    logging.setOutValue("-");
                    logging.setPlatform(device.getDevicePlatform().name());
                    logging.setMobile(getPlatformType(device));
                    logging.setUserAgent(request.getHeader("User-Agent"));
                    logging.setStartDt(Timestamp.valueOf(now));
                    logging.setEndDt(Timestamp.valueOf(now));
                    logging.setCreationUser(userId);
                    logging.setLastupdateUser(userId);
        loggingRepository.updateComplete(logging);
    }
    public void error(HttpServletRequest r,String message) throws IOException {
        RequestWrapper request = new RequestWrapper(r);
        LocalDateTime now = LocalDateTime.now();
        String userId = getCurrentUser();

        Logging.Log logging = new Logging.Log();
                    logging.setLogId((BigDecimal)request.getAttribute("X-MIRI-REQUEST-ID"));
                    logging.setUserId(userId);
                    logging.setControllerName(request.getRequestURI());
                    logging.setErrorYn("y");
                    logging.setErrorContents(message);
                    logging.setEndDt(Timestamp.valueOf(now));
                    logging.setLastupdateUser(userId);
        loggingRepository.updateError(logging);
    }
}
