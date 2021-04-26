package com.example.demo.Controller;

import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.net.*;
import java.util.Map;

@CrossOrigin
@RestController
public class UDPTestController {

    private String sendStr = "NHII-INTERNETEREADER";
    private String netAddress = "";
    private final int PORT = 48899;

    private DatagramSocket datagramSocket;
    private DatagramPacket datagramPacket;

    public String UdpClient(String UDPNetSec){
        try {
            this.netAddress=UDPNetSec;
            datagramSocket = new DatagramSocket();
            byte[] buf = sendStr.getBytes();
            InetAddress address = InetAddress.getByName(netAddress);
            datagramPacket = new DatagramPacket(buf, buf.length, address, PORT);
            datagramSocket.send(datagramPacket);


            byte[] receBuf = new byte[1024];
            DatagramPacket recePacket = new DatagramPacket(receBuf, receBuf.length);
            datagramSocket.receive(recePacket);

            String receStr = new String(recePacket.getData(), 0 , recePacket.getLength());

            InetAddress serverIp = recePacket.getAddress();
            String ip="";
            for(int i=1;i<serverIp.toString().length();i++){
                ip = ip + serverIp.toString().toCharArray()[i];
            }
            return ip;
        } catch (SocketException e) {
            e.printStackTrace();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            // 关闭UdpSocket
            if(datagramSocket != null){
                datagramSocket.close();
            }
        }
        return "noResult";
    }

    @RequestMapping(value="/UDPTest",method= RequestMethod.POST)
    public String UDPTest(@RequestParam Map map) {

        System.out.println("接收了UDPtEST的请求");
        String UDPNetSec=(String) map.keySet().iterator().next();
        System.out.println(UDPNetSec);
        String ip = UdpClient(UDPNetSec);
        System.out.println("探测到的ip是："+ ip);
        if(ip=="noResult"){ return "未探测到ip";}
        else return ip;
    }
}
