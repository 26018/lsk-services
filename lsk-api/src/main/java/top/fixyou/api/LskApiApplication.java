package top.fixyou.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import springfox.documentation.oas.annotations.EnableOpenApi;

import javax.annotation.PostConstruct;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author Lsk
 * Date 2023/02/27
 */


@EnableOpenApi
@SpringBootApplication
@ComponentScan(basePackages = "top.fixyou")
public class LskApiApplication {
    public static void main(String[] args) {
        try {
            SpringApplication.run(LskApiApplication.class, args);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



}
