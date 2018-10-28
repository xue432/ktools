package com.kalvin.ktools;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Objects;

@RunWith(SpringRunner.class)
@SpringBootTest
public class KtoolsApplicationTests {

    @Test
    public void contextLoads() {
//        String realPath = Objects.requireNonNull(Thread.currentThread().getContextClassLoader().getResource("/")).getPath();
//        System.out.println("realPath = " + realPath);
    }

}
