package com.qin.miaosha.service.imp;

import com.qin.miaosha.common.ServerResponse;
import com.qin.miaosha.domain.MiaoShaUser;
import com.qin.miaosha.service.MailService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMailMessage;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.mail.internet.MimeMessage;

@Service("MailService")
public class MailServiceImpl implements MailService {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private JavaMailSender sender ;

    @Value("${mail.fromMail.addr}")
    private String from;

    @Autowired
    private TemplateEngine templateEngine;


    @Override
    public ServerResponse sendEmail(MiaoShaUser user) {
        MimeMessage message = sender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(message,true);
            Context context = new Context();
            context.setVariable("id",user.getId());
            String emailContent = templateEngine.process("emailTemplate", context);
            helper.setFrom(from);
            helper.setTo(user.getEmail());
            helper.setSubject("邮箱验证");
            helper.setText(emailContent,true);
            sender.send(message);
            logger.info("邮件已经发送。");
        } catch (Exception e) {
            logger.error("发送邮件时发生异常！", e);
        }
        return ServerResponse.createBySuccess("邮件已发送请查收");
    }
}
