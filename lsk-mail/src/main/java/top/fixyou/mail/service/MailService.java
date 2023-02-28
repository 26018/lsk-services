package top.fixyou.mail.service;

import top.fixyou.mail.entity.MailRequest;

import java.util.List;

/**
 * @author 26018
 * @description
 * @date 2023/2/27 14:49
 */


public interface MailService {

    /**
     * 发送模板邮件
     * @param mailRequest 请求参数
     */
    void send(MailRequest mailRequest);

}
