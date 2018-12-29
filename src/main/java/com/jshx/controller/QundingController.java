package com.jshx.controller;


import com.codahale.metrics.Counter;
import com.codahale.metrics.Histogram;
import com.codahale.metrics.Meter;
import com.codahale.metrics.Timer;
import com.codahale.metrics.annotation.ExceptionMetered;
import com.codahale.metrics.annotation.Timed;
import com.jshx.kafka.MessageProducer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
public class QundingController {
    private static final Logger logger = LoggerFactory.getLogger(QundingController.class);

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
        logger.debug("body data >>" + body);
        requestMeter.mark();
        pendingJobs.inc();
        responseSizes.update(new Random().nextInt(10));
        final Timer.Context context = responses.time();
        messageProducer.sendDelimiterMessage(kafkaTest, body);
        logger.debug("qunding message is sending...");
        context.stop();
        return 1;
    }
}
