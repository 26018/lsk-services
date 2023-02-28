package top.fixyou.api.controller;

import org.springframework.web.bind.annotation.*;
import top.fixyou.base.result.ResponseResult;
import top.fixyou.mail.config.MailConfiguration;
import top.fixyou.mail.entity.MailConnector;
import top.fixyou.mail.entity.MailProperties;
import top.fixyou.mail.entity.MailRequest;
import top.fixyou.mail.service.MailSupplier;
import top.fixyou.mail.service.impl.MailServiceImpl;
import top.fixyou.mail.service.impl.MailSupplierImpl;

import javax.annotation.Resource;
import java.util.ArrayDeque;
import java.util.List;


/**
 * @author 26018
 * @description
 * @date 2023/2/27 14:33
 */

@RestController
@RequestMapping("/mail")
public class MailController {

    @Resource
    MailServiceImpl mailService;

    @PostMapping("/send")
    public ResponseResult<String> sendMail(@RequestBody MailRequest mailRequest) {
        mailService.sendSync(mailRequest);
        return ResponseResult.ok();
    }

    @GetMapping("/connector-info")
    public ResponseResult<List<MailProperties>> connectorsInfo() {

        return ResponseResult.ok(null);
    }
}
