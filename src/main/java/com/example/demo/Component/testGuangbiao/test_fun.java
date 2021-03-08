package com.example.demo.Component.testGuangbiao;

import java.net.Socket;

import static java.lang.Thread.sleep;

public class test_fun {
    public String test() throws Exception {

        String host = "10.10.100.254";
        int port = 8899;

        Socket socket = new Socket(host,port);

        System.out.println();
        System.out.println("=======连接测试========");

        connect c = new connect();
        c.connect(socket);

        sleep(500);

        System.out.println();
        System.out.println("=======模板测试========");
        String filePath = "G:\\Form\\105T.txt";
//        String filePath = "G:\\Form\\90T.txt";
        template t = new template();
        boolean res = t.template(socket,filePath);
        if(res){
            System.out.println("模板发送成功");
        } else {
            System.out.println("模板发送失败");
        }

        //测试是否有返回值，确实没有
//        receive r = new receive();
//        String res_tem = r.receive(socket);
//        System.out.println(res_tem);


        sleep(2000);
        System.out.println();
        System.out.println("=======读卡测试========");
        read re = new read();
        String resl = "";
        resl = re.read(socket);


        sleep(3000);
        System.out.println();
        System.out.println("=======断开测试========");

        exit e = new exit();
        e.exit(socket);
        return resl;
    }
}
