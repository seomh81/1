package com.hdel.miri.concurrent.domain.mail;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.hdel.miri.concurrent.domain.dgk.vo.ReqVO;
import com.hdel.miri.concurrent.domain.dgk.CcRepository;

import com.hdel.miri.concurrent.domain.message.CcMessageVO.MailVO;
import com.hdel.miri.concurrent.util.response.Message;

import javax.activation.DataSource;
import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeUtility;
import javax.mail.util.ByteArrayDataSource;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class MailService {
    private final JavaMailSenderImpl mailSender;
    private final CcRepository ccRepository;

    public void send(MailVO request)  {
        try {
            MimeMessage msg = mailSender.createMimeMessage();
            msg.setHeader("X-SES-CONFIGURATION-SET", "hel-ses-miri-configuration");
            MimeMessageHelper helper = new MimeMessageHelper(msg,true, StandardCharsets.UTF_8.name());
            helper.setSubject(request.getSubject());
            helper.setText(request.getContent());
            helper.setFrom(new InternetAddress(request.getFromAddress(),request.getFromAddress(),StandardCharsets.UTF_8.name()));
            helper.setTo(getAddress(request.getToAddress(),"ko_kr"));
            helper.setCc(getAddress(request.getCcAddress(),"ko_kr"));
            helper.setBcc(getAddress(request.getBccAddress(),"ko_kr"));

            if(0 < request.getAttachSize()){
                for (MultipartFile file : request.getAttachFiles()) {
                    try {
                        DataSource source = new ByteArrayDataSource(file.getBytes(), file.getContentType());
                        helper.addAttachment(MimeUtility.encodeText(file.getOriginalFilename()), source);
                    } catch (Exception ex) {
                        throw new RuntimeException(ex.getLocalizedMessage());
                    }
                }
            }
            mailSender.send(helper.getMimeMessage());

            ccRepository.insertMailLog(ReqVO.mailLogVO.builder()
                                        .fromAddress(request.getFromAddress())
                                        .toAddress(request.getToAddress())
                                        .ccAddress(request.getCcAddress())
                                        .bccAddress(request.getBccAddress())
                                        .subject(request.getSubject())
                                        .content(request.getContent())
                                        .userId("anonymousUser").build());        
            
        } catch ( MessagingException e ) {
            throw new RuntimeException(e.getLocalizedMessage());
        } catch ( UnsupportedEncodingException e ) {
            throw new RuntimeException(e.getLocalizedMessage());
        } catch ( Exception e) {
            throw new RuntimeException(e.getLocalizedMessage());
        }
    }

    public void send( MailVO request, boolean isHtml)  {
        try {
            MimeMessage msg = mailSender.createMimeMessage();
            msg.setHeader("X-SES-CONFIGURATION-SET", "hel-ses-miri-configuration");
            MimeMessageHelper helper = new MimeMessageHelper(msg,true, StandardCharsets.UTF_8.name());
            helper.setSubject(request.getSubject());
            helper.setText(request.getContent(), isHtml);
            helper.setFrom(new InternetAddress(request.getFromAddress(),request.getFromAddress(),StandardCharsets.UTF_8.name()));
            helper.setTo(getAddress(request.getToAddress(),"ko_kr"));
            helper.setCc(getAddress(request.getCcAddress(),"ko_kr"));
            helper.setBcc(getAddress(request.getBccAddress(),"ko_kr"));

            if(0 < request.getAttachSize()){
                for (MultipartFile file : request.getAttachFiles()) {
                    try {
                        DataSource source = new ByteArrayDataSource(file.getBytes(), file.getContentType());
                        helper.addAttachment(MimeUtility.encodeText(file.getOriginalFilename()), source);
                    } catch (Exception ex) {
                        throw new RuntimeException(ex.getLocalizedMessage());
                    }
                }
            }
            mailSender.send(helper.getMimeMessage());

            ccRepository.insertMailLog(ReqVO.mailLogVO.builder()
                                        .fromAddress(request.getFromAddress())
                                        .toAddress(request.getToAddress())
                                        .ccAddress(request.getCcAddress())
                                        .bccAddress(request.getBccAddress())
                                        .subject(request.getSubject())
                                        .content(request.getContent())
                                        .userId("anonymousUser").build());
        } catch ( MessagingException e ) {
            throw new RuntimeException(e.getLocalizedMessage());
        } catch ( UnsupportedEncodingException e ) {
            throw new RuntimeException(e.getLocalizedMessage());
        } catch ( Exception e) {
            throw new RuntimeException(e.getLocalizedMessage());
        }
    }

    private InternetAddress[] getAddress(List<String> list,String loc) {
        if(list==null) return new InternetAddress[0];
        return list.stream().map(s -> {
            try { return new InternetAddress(s,s,StandardCharsets.UTF_8.name()); }
            catch (UnsupportedEncodingException e) { throw new RuntimeException(e.getLocalizedMessage()); }
        }).toArray(InternetAddress[]::new);
    }

}