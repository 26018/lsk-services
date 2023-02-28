package top.fixyou.mail.entity;

import lombok.Data;


/**
 * @author 26018
 * @description
 * @date 2023/2/27 15:55
 */


@Data
public class MailProperties {

    String host;
    String username;
    String password;
    String defaultEncoding;
    Transport transport = new Transport();

    @Data
    private static class Transport {
        private String protocol;
    }

}
