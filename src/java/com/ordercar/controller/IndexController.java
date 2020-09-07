package com.ordercar.controller;


import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Slf4j
@Controller
@RequestMapping("/v1")
public class IndexController {

    @RequestMapping("/index")
    public String index(Model m){
        m.addAttribute("msg","你莫走");
        return "main";
    }
}
