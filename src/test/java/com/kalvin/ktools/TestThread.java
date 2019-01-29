package com.kalvin.ktools;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestThread {

    private final static Logger LOGGER = LoggerFactory.getLogger(TestThread.class);

    private int ti = 0;


    public void tryCount()  {
        while (true) {
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            ti++;
            LOGGER.info("线程{}的ti={}", Thread.currentThread().getName(), ti);
            if (ti > 100) {
                break;
            }
        }

    }
}
