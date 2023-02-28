package top.fixyou.mail.service;

import top.fixyou.mail.entity.MailConnector;
import java.util.ArrayDeque;
import java.util.Objects;

/**
 * @author 26018
 * @description
 * @date 2023/2/27 15:05
 */

public interface MailSupplier {

    String mail_prefix = "mail.";

    /**
     * 连接器队列
     */
    ArrayDeque<MailConnector> ARRAY_DEQUE = new ArrayDeque<>();

    /**
     * 提供一个邮件连接器
     */
    default MailConnector provide() {
        // 等待5s
        long maxWait = 1000 * 5L;
        long startTimeMillis = System.currentTimeMillis();
        while (startTimeMillis + maxWait > System.currentTimeMillis()) {
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
    default void deprive(MailConnector connector) {
        ARRAY_DEQUE.add(connector);
    }

}
