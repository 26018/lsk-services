package top.fixyou.mail.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import top.fixyou.mail.config.MailConfiguration;
import top.fixyou.mail.entity.MailConnector;
import top.fixyou.mail.entity.MailProperties;
import top.fixyou.mail.service.MailSupplier;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.mail.*;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * @author 26018
 * @description
 * @date 2023/2/27 15:10
 */

@Service
@Slf4j
public class MailSupplierImpl implements MailSupplier {

    @Resource
    MailConfiguration mailConfiguration;

    @PostConstruct
    private void init() {
        List<MailProperties> mailProperties = mailConfiguration.getMailProperties();
        List<MailConnector> connectors = connectToMailServer(mailProperties);
        ARRAY_DEQUE.addAll(connectors);
        log.info("已加载{}条邮件配置", ARRAY_DEQUE.size());
    }

    /**
     * 连接到邮件服务器
     * 返回连接成功的连接器
     */
    private List<MailConnector> connectToMailServer(List<MailProperties> mailProperties) {
        List<MailConnector> connectors = new ArrayList<>();
        mailProperties.forEach(mailProperty -> {
            Properties properties = new Properties();
            // mailProperty对象转properties
            Class<? extends MailProperties> mailPropertyClass = mailProperty.getClass();
            try {
                for (Field field : mailPropertyClass.getDeclaredFields()) {
                    field.setAccessible(true);
                    properties.put(mail_prefix + field.getName(), String.valueOf(field.get(mailProperty)));
                }
                Session session = Session.getDefaultInstance(properties, new Authenticator() {
                    @Override
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(mailProperty.getUsername(), mailProperty.getPassword());
                    }
                });
                Transport transport = session.getTransport();
                transport.connect(mailProperty.getHost(), mailProperty.getUsername(), mailProperty.getPassword());
                connectors.add(new MailConnector(session, transport, properties));
            } catch (Exception e) {
                log.error("邮箱连接异常:{}", e.getMessage());
            }
        });
        return connectors;
    }
}
