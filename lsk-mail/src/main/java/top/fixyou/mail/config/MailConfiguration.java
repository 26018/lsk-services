package top.fixyou.mail.config;

import lombok.*;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import top.fixyou.mail.entity.MailProperty;

import java.util.List;

/**
 * @author 26018
 * @description
 * @date 2023/2/27 15:51
 */

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Configuration
@ConfigurationProperties(prefix = "spring.mail")
@EnableConfigurationProperties({MailConfiguration.class})
public class MailConfiguration {
    List<MailProperty> mailProperties;

    Boolean logAble;
}
