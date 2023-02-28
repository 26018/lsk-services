package top.fixyou.mail.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import top.fixyou.mail.entity.MailConnector;
import top.fixyou.mail.entity.MailRequest;
import top.fixyou.mail.service.MailService;
import top.fixyou.mail.service.MailSupplier;

import javax.annotation.Resource;
import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

/**
 * @author 26018
 * @description
 * @date 2023/2/27 14:50
 */

@Slf4j
@Service
public class MailServiceImpl implements MailService {

    @Resource
    MailSupplierImpl mailSupplier;
    @Resource
    TemplateEngine templateEngine;

    @Value("${spring.mail.log}")
    Boolean logAble;

    /**
     * 同步发送
     *
     * @param mailRequest 请求参数
     */
    @Override
    public void send(MailRequest mailRequest) {
        MailConnector provide = mailSupplier.provide();
        try {
            Session session = provide.getSession();
            Transport transport = provide.getTransport();
            Properties properties = provide.getProperties();
            // 获取发件人
            String fromMail = String.valueOf(properties.get(MailSupplier.mail_prefix + "username"));
            // 创建邮件
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(fromMail));
            // 收件人
            InternetAddress[] address = InternetAddress.parse(mailRequest.getRecipient());
            message.setRecipients(Message.RecipientType.TO, address);
            message.setSubject(mailRequest.getTitle());
            // 加载HTML模板 - 添加参数
            Context context = new Context();
            context.setVariables(mailRequest.getParams());
            // 模板字符串
            String htmlString = templateEngine.process(mailRequest.getTemplate(), context);
            message.setContent(htmlString, "text/html;charset=utf-8");
            // 发送邮件
            transport.sendMessage(message, message.getAllRecipients());
            if (logAble) {
                log.info("邮件发送 ----- 发件人：{}，收件人：{}，模板：{}，参数：{}", fromMail, address, mailRequest.getTemplate(), mailRequest.getParams());
            }
        } catch (Exception exception) {
            log.error("发送邮件时出现错误：{}", exception.getMessage());
        } finally {
            mailSupplier.deprive(provide);
        }
    }

    /**
     * 异步发送
     */
    public void sendSync(MailRequest mailRequest) {
        // TODO 改为线程池
        @SuppressWarnings("AlibabaAvoidManuallyCreateThread")
        Thread thread = new Thread(() -> send(mailRequest));
        thread.start();
    }
}
