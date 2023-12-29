package com.hdel.miri.api.util.request;

import lombok.extern.slf4j.Slf4j;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.*;


@Slf4j
public class RequestWrapper extends HttpServletRequestWrapper {
    private byte[] cachedBody;
    public RequestWrapper(HttpServletRequest request) throws IOException {
        super(request);
        cachedBody = request.getInputStream().readAllBytes();

    }
    @Override
    public ServletInputStream getInputStream() {
        return new CachedServletInputStream(cachedBody);
    }

    @Override
    public BufferedReader getReader()  {
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(cachedBody);
        return new BufferedReader(new InputStreamReader(byteArrayInputStream));
    }


}
