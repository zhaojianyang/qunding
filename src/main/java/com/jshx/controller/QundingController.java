package com.jshx.controller;


import com.codahale.metrics.Counter;
import com.codahale.metrics.Histogram;
import com.codahale.metrics.Meter;
import com.codahale.metrics.Timer;
import com.codahale.metrics.annotation.ExceptionMetered;
import com.codahale.metrics.annotation.Timed;
import com.jshx.kafka.MessageProducer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Random;

/**
 * @Auther: 肖俊
 * @Date: 2018-12-28 17:09
 * @Description:
 */
@Controller
@Slf4j
public class QundingController {

    @Autowired
    MessageProducer messageProducer;

    @Autowired
    private Meter requestMeter;

    @Autowired
    private Histogram responseSizes;

    @Autowired
    private Counter pendingJobs;

    @Autowired
    private Timer responses;

    @Value("${spring.kafka.qingding.data.monitor.topic}")
    private String kafkaTest;

    @Timed
    @ExceptionMetered
    @ResponseBody
    @RequestMapping(value = "/qunding")
    public int sendQundingData(@RequestParam String body){
        log.info("body data >>" + body);
        System.out.println("get data ===> " + body);
        requestMeter.mark();
        pendingJobs.inc();
        responseSizes.update(new Random().nextInt(10));
        final Timer.Context context = responses.time();
        messageProducer.sendDelimiterMessage(kafkaTest, body);
        log.debug("qunding message is sending...");
        context.stop();
        return 1;
    }
}
