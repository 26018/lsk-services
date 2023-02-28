package top.fixyou.mail.entity;

import lombok.*;

import javax.mail.Session;
import javax.mail.Transport;
import java.util.Properties;

/**
 * @author 26018
 * @description
 * @date 2023/2/27 15:08
 */

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MailConnector {
    Session session;
    Transport transport;
    Properties properties;
}
