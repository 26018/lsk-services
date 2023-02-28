package top.fixyou.api.controller;

import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author 26018
 * @description
 * @date 2023/2/27 12:35
 */

@Api("测试模块")
@RestController
@RequestMapping("/main")
public class TestController {

    @GetMapping("/info")
    public String info() {
        return "Lsk-service 是项目中一些常用服务的集合" + "<br>" +
                "您可以通过远程调用来使用这些服务。";
    }

}
