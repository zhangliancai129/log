package com.example.log4j.controller;

import org.apache.log4j.Logger;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/log4j/example/*")
public class Log4jExampleController {

    private static final Logger LOGGER = Logger.getLogger(Log4jExampleController.class);

    /**
     * http://localhost:8081/log4j/example/test
     *
     * @return
     */
    @GetMapping("test")
    public String test(HttpServletRequest request, HttpServletResponse response) {
        long startTime = System.currentTimeMillis();
        for (int i = 0; i < 100000; i++) {
            LOGGER.debug("TEST DEBUG LEVEL LOG");
            LOGGER.info("TEST INFO LEVEL LOG");
            LOGGER.warn("TEST WARN LEVEL LOG");
            LOGGER.error("TEST ERROR LEVEL LOG");
        }

        LOGGER.info("执行完成，耗时：" + (System.currentTimeMillis() - startTime));
        return "执行完成";
    }
}
