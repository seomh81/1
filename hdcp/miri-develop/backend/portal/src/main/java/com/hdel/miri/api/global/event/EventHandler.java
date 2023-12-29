package com.hdel.miri.api.global.event;

import com.hdel.miri.api.util.file.FileService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import javax.servlet.ServletContext;

@Slf4j
@Component
@RequiredArgsConstructor
public class EventHandler {
    private final ServletContext context;
    private final FileService fileService;

    @EventListener(ContextRefreshedEvent.class)
    public void onContextStartedEvent (ContextRefreshedEvent event){
        log.info("Servlet Context Path. : {}",context.getContextPath());
        log.info("Upload Path Created. : {}",fileService.existRoot());
        log.info("Temp Path Created. : {}",fileService.existTemp());
        log.info("Contract-Nas Exist: {}",fileService.existContactNas());
    }
}
