package com.example.demo.Component.ForGB;

import java.io.UnsupportedEncodingException;

public class ForGB2 {

    public String ForGB2(String str) throws UnsupportedEncodingException {
        StringBuilder sb = new StringBuilder();
//        sb.append("balalabala巴啦啦小魔仙01");
        sb.append("三年六班");
        String utf8 = new String(sb.toString().getBytes("UTF-8"));
        System.out.println(utf8.toString());
        String unicode = new String(utf8.getBytes(), "UTF-8");
        System.out.println(unicode.toString());
        String gbk = new String(unicode.getBytes("GB2312"));
        System.out.println(gbk.toString());
        return gbk.toString();
    }

}
