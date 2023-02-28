package top.fixyou.mail.entity;

import lombok.Builder;
import lombok.Data;

import java.util.Map;

/**
 * @author 26018
 * @description
 * @date 2023/2/27 14:54
 */

@Data
@Builder
public class MailRequest {

    /**
     * 邮件标题
     */
    private String title;
    /**
     * 邮件模板字符串
     */
    private String template;
    /**
     * 邮件接收人
     */
    private String recipient;
    /**
     * 模板参数
     */
    private Map<String, Object> params;
}
