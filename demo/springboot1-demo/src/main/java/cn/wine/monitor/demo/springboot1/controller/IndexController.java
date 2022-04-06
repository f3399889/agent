package cn.wine.monitor.demo.springboot1.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Title&Desc TODO
 * @Author cy.liu
 * @CreateTime 2022/4/6 9:45 上午, IndexController
 */
@RestController
@RequestMapping("/agent1")
public class IndexController {

    @GetMapping("/index")
    public String index() {
        return "OK";
    }

}
