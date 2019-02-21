package com.kalvin.ktools;

import sun.audio.AudioPlayer;
import sun.audio.AudioStream;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

public class TestMusic {

    public static void main(String[] args) throws Exception {
        ply();
        ply();
    }

    public static void ply() throws Exception {
        File file=new File("C:\\Users\\14813\\Desktop\\童话镇.wav");//java只支持wav格式
        InputStream is = new FileInputStream(file);
        AudioStream audioStream = new AudioStream(is);
        AudioPlayer.player.start(audioStream);
    }
}
