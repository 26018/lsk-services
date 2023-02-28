package top.fixyou.mail.config;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import top.fixyou.mail.entity.MailProperties;

import java.util.List;

/**
 * @author 26018
 * @description
 * @date 2023/2/27 15:51
 */

@Getter
@Builder
@AllArgsConstructor
@Configuration
@ConfigurationProperties(prefix = "spring.mail")
@EnableConfigurationProperties({MailConfiguration.class})
public class MailConfiguration {
    List<MailProperties> mailProperties;

}
