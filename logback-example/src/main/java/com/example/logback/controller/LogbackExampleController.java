package com.example.logback.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
@RequestMapping("/logback/*")
public class LogbackExampleController {

    private static final Logger LOGGER = LoggerFactory.getLogger(LogbackExampleController.class);

    /**
     * http://localhost:8082/logback/test
     *
     * @param request
     * @param response
     * @return
     */
    @ResponseBody
    @RequestMapping("test")
    public String test(HttpServletRequest request, HttpServletResponse response) {
        long startTime = System.currentTimeMillis();

        for (int i = 0; i < 100000; i++) {
            LOGGER.debug("TEST DEBUG LEVEL LOG");
            LOGGER.info("TEST INFO LEVEL LOG");
            LOGGER.warn("TEST WARN LEVEL LOG");
            LOGGER.error("TEST ERROR LEVEL LOG");
        }

        LOGGER.info("执行完成，耗时 {} ms.", System.currentTimeMillis() - startTime);
        return "测试完成";
    }
}
