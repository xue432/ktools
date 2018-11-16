package com.kalvin.ktools.comm.kit;

import com.kalvin.ktools.BaseTest;
import org.junit.Test;

import java.io.File;

import static org.junit.Assert.*;

public class ImageKitTest extends BaseTest {

    @Test
    public void toBase64() {
        File file = new File("H:\\Kalvin\\我的图片\\0.png");
//        File file = new File("H:\\Kalvin\\我的图片\\6a04b428gy1fw7qemgx9gg203w04s0zq.gif");
        String s = ImageKit.toBase64(file);
        LOGGER.info("s = {}", s);
    }
}