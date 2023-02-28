package top.fixyou.mail.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import top.fixyou.mail.config.MailConfiguration;
import top.fixyou.mail.entity.MailConnector;
import top.fixyou.mail.entity.MailProperty;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import java.lang.reflect.Field;
import java.util.*;

/**
 * @author 26018
 * @description
 * @date 2023/2/27 15:05
 */

@Slf4j
@Service
public class MailSupplier {

    /**
     * 配置前缀
     */
    public static final String MAIL_PREFIX = "mail.";
    public static final Long MAX_WAIT_TIMEOUT = 1000 * 5L;
    /**
     * 连接器队列
     */
    private final ArrayDeque<MailConnector> ARRAY_DEQUE = new ArrayDeque<>();
    /**
     * 邮箱配置对象
     */
    @Resource
    public MailConfiguration mailConfiguration;

    /**
     * 初始化方法，加载yaml或properties文件配置的邮箱
     */
    @PostConstruct
    private void init() {
        // 获取配置
        List<MailProperty> mailProperties = mailConfiguration.getMailProperties();
        // 连接至服务器
        List<MailConnector> connectors = connectToMailServer(mailProperties);
        // 添加到队列
        ARRAY_DEQUE.addAll(connectors);
        log.info("已加载{}条邮件配置", ARRAY_DEQUE.size());
    }

    /**
     * 连接到邮件服务器
     * 返回连接成功的连接器
     */
    private List<MailConnector> connectToMailServer(List<MailProperty> mailProperties) {
        List<MailConnector> connectors = new ArrayList<>();
        mailProperties.forEach(mailProperty -> {
            Properties properties = new Properties();
            // mailProperty对象转properties
            Class<? extends MailProperty> mailPropertyClass = mailProperty.getClass();
            try {
                for (Field field : mailPropertyClass.getDeclaredFields()) {
                    field.setAccessible(true);
                    properties.put(MAIL_PREFIX + field.getName(), String.valueOf(field.get(mailProperty)));
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


    /**
     * 提供一个邮件连接器
     */
    public MailConnector provide() {
        // 等待5s
        long startTimeMillis = System.currentTimeMillis();
        while (startTimeMillis + MAX_WAIT_TIMEOUT > System.currentTimeMillis()) {
            MailConnector peek = ARRAY_DEQUE.peek();
            if (Objects.nonNull(peek)) {
                return ARRAY_DEQUE.pop();
            }
        }
        throw new RuntimeException("暂无可用的邮件连接器");
    }

    /**
     * 添加一个邮件连接器
     */
    public void deprive(MailConnector connector) {
        ARRAY_DEQUE.add(connector);
    }

}
