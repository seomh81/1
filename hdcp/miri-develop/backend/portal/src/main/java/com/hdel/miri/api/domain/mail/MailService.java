package com.hdel.miri.api.domain.mail;

import com.hdel.miri.api.domain.cc.CC;
import com.hdel.miri.api.domain.cc.CCRepository;
import com.hdel.miri.api.domain.mms.mmsVO.request.requestBuilder;
import com.hdel.miri.api.util.response.Message;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.activation.DataSource;
import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeUtility;
import javax.mail.util.ByteArrayDataSource;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Locale;

@Slf4j
@Service
@RequiredArgsConstructor
public class MailService {
    private final Message message;
    private final JavaMailSenderImpl mailSender;

    private final CCRepository ccRepository;

    public void send(MailVO.Request request)  {
        try {
            MimeMessage msg = mailSender.createMimeMessage();
            msg.setHeader("X-SES-CONFIGURATION-SET", "hel-ses-miri-configuration");
            MimeMessageHelper helper = new MimeMessageHelper(msg,true, StandardCharsets.UTF_8.name());
            helper.setSubject(request.getSubject());
            helper.setText(request.getContent());
            helper.setFrom(new InternetAddress(request.getFromAddress(),request.getFromAddress(),StandardCharsets.UTF_8.name()));
            helper.setTo(getAddress(request.getToAddress(),request.getUserLocale()));
            helper.setCc(getAddress(request.getCcAddress(),request.getUserLocale()));
            helper.setBcc(getAddress(request.getBccAddress(),request.getUserLocale()));

            if(0 < request.getAttachSize()){
                for (MultipartFile file : request.getAttachFiles()) {
                    try {
                        DataSource source = new ByteArrayDataSource(file.getBytes(), file.getContentType());
                        helper.addAttachment(MimeUtility.encodeText(file.getOriginalFilename()), source);
                    } catch (Exception ex) {
                        throw new RuntimeException(message.withFailureSendMail(request.getUserLocale()));
                    }
                }
            }
            mailSender.send(helper.getMimeMessage());

            ccRepository.insertMailLog(CC.mailLogVO.builder()
                                        .fromAddress(request.getFromAddress())
                                        .toAddress(request.getToAddress())
                                        .ccAddress(request.getCcAddress())
                                        .bccAddress(request.getBccAddress())
                                        .subject(request.getSubject())
                                        .content(request.getContent())
                                        .userId(request.getCurrentUser()).build());
        } catch ( MessagingException e ) {
            throw new RuntimeException(message.withFailureSendMail(request.getUserLocale()));
        } catch ( UnsupportedEncodingException e ) {
            throw new RuntimeException(message.withFailureEncoding(request.getUserLocale()));
        }
    }

    public void send(MailVO.Request request, boolean isHtml)  {
        try {
            MimeMessage msg = mailSender.createMimeMessage();
            msg.setHeader("X-SES-CONFIGURATION-SET", "hel-ses-miri-configuration");
            MimeMessageHelper helper = new MimeMessageHelper(msg,true, StandardCharsets.UTF_8.name());
            helper.setSubject(request.getSubject());
            helper.setText(request.getContent(), isHtml);
            helper.setFrom(new InternetAddress(request.getFromAddress(),request.getFromAddress(),StandardCharsets.UTF_8.name()));
            helper.setTo(getAddress(request.getToAddress(),request.getUserLocale()));
            helper.setCc(getAddress(request.getCcAddress(),request.getUserLocale()));
            helper.setBcc(getAddress(request.getBccAddress(),request.getUserLocale()));

            if(0 < request.getAttachSize()){
                for (MultipartFile file : request.getAttachFiles()) {
                    try {
                        DataSource source = new ByteArrayDataSource(file.getBytes(), file.getContentType());
                        helper.addAttachment(MimeUtility.encodeText(file.getOriginalFilename()), source);
                    } catch (Exception ex) {
                        throw new RuntimeException(message.withFailureSendMail(request.getUserLocale()));
                    }
                }
            }
            mailSender.send(helper.getMimeMessage());

            ccRepository.insertMailLog(CC.mailLogVO.builder()
                                        .fromAddress(request.getFromAddress())
                                        .toAddress(request.getToAddress())
                                        .ccAddress(request.getCcAddress())
                                        .bccAddress(request.getBccAddress())
                                        .subject(request.getSubject())
                                        .content(request.getContent())
                                        .userId(request.getCurrentUser()).build());
        } catch ( MessagingException e ) {
            throw new RuntimeException(message.withFailureSendMail(request.getUserLocale()));
        } catch ( UnsupportedEncodingException e ) {
            throw new RuntimeException(message.withFailureEncoding(request.getUserLocale()));
        }
    }

    private InternetAddress[] getAddress(List<String> list,String loc) {
        if(list==null) return new InternetAddress[0];
        return list.stream().map(s -> {
            try { return new InternetAddress(s,s,StandardCharsets.UTF_8.name()); }
            catch (UnsupportedEncodingException e) { throw new RuntimeException(message.withFailureEncoding(loc)); }
        }).toArray(InternetAddress[]::new);
    }

}
