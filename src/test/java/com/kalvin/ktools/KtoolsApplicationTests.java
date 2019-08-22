package com.kalvin.ktools;

import com.kalvin.ktools.controller.ImageController;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Objects;

@RunWith(SpringRunner.class)
@SpringBootTest
public class KtoolsApplicationTests {

    @Autowired
    private ImageController controller;

    @Test
    public void contextLoads() {
//        String realPath = Objects.requireNonNull(Thread.currentThread().getContextClassLoader().getResource("/")).getPath();
//        System.out.println("realPath = " + realPath);
//        controller.generateWordCloud("wx2.jpg", "无论你觉得自己多么的不幸，永远有人比你更加不幸。  摘自：励志语录   www.yuluju.com为了不让生活留下遗憾和后悔，我们应该尽可能抓住一切改变生活的机会。靠山山会倒，靠水水会流，靠自己永远不倒。拥有梦想只是一种智力，实现梦想才是一种能力。方向不对，努力白费，方向大于方法，动力大于能力，做人大于做事，凡事都说这都是我们的错。宁愿跑起来被拌倒无数次，也不愿规规矩矩走一辈子。就算跌倒也要豪迈的笑。\n");
    }

}
