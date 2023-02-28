package top.fixyou.api.controller;

import org.springframework.web.bind.annotation.*;
import top.fixyou.base.result.ResponseResult;
import top.fixyou.mail.config.MailConfiguration;
import top.fixyou.mail.entity.MailProperty;
import top.fixyou.mail.entity.MailRequest;
import top.fixyou.mail.service.MailSupplier;
import top.fixyou.mail.service.impl.MailServiceImpl;

import javax.annotation.Resource;
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
        mailService.asyncSend(mailRequest);
        return ResponseResult.ok();
    }

    @GetMapping("/connector-info")
    public ResponseResult<List<MailProperty>> connectorsInfo() {
        return ResponseResult.ok(null);
    }
}
