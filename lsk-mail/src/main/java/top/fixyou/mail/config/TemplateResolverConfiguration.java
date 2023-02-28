package top.fixyou.mail.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.thymeleaf.templateresolver.StringTemplateResolver;

@Configuration
public class TemplateResolverConfiguration{
	@Bean
	public StringTemplateResolver defaultTemplateResolver() {
		StringTemplateResolver resolver = new StringTemplateResolver();
		return resolver;
	}
}
