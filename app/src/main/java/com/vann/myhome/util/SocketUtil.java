package com.vann.myhome.util;

import android.text.TextUtils;

import com.vann.myhome.db.HomeDB;
import com.vann.myhome.model.DeviceModel;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author: wenlong.bian 2015-08-10
 * @E-mail: bxl049@163.com
 */
public class SocketUtil {

    public static final String IP = "255.255.255.255";
    public static final int port = 5999;
    public static DatagramSocket _client;
    public static final String SEND_CODE = "$vnm,";
    public static final int SEND_LENGHT = 22;

    /**
     * 获取设备信息
     *
     * @param msg
     * @param listener
     */
    public static void receive(String msg, UdpCallBackListener listener) {
        ArrayList<String> responses = new ArrayList<String>();
        String Strs = "";
        try {
            if (_client == null) {
                _client = new DatagramSocket(null);
                _client.setReuseAddress(true);
                _client.setBroadcast(true);// 设置标志，T OR E OR R广播数据报
                // 在2.2左右版本不行。在4.0版本上可行
                _client.bind(new InetSocketAddress(port));
            }
            DatagramPacket sendDp = new DatagramPacket(msg.getBytes(),
                    msg.getBytes().length, InetAddress.getByName(IP), port);
            _client.setSoTimeout(2000);
            _client.send(sendDp);
            byte[] buf = new byte[1024];
            DatagramPacket receiveDp = new DatagramPacket(buf, buf.length);

            // 创建长度为100的数据接收包
            while (true) {
                Thread.sleep(50);
                _client.receive(receiveDp);// 套接字接收数据包
                String tmpStr = new String(buf, 0, receiveDp.getLength())
                        .replace(" ", "");
                Strs += tmpStr;
                if (tmpStr.contains(SEND_CODE) && tmpStr.contains(",0")) {
                    String strdev = tmpStr + "|" + receiveDp.getAddress();
                    if (responses.indexOf(strdev) == -1)
                        responses.add(strdev);

                }

            }
        } catch (Exception e) {
            if (listener != null) {
                listener.onError(e);
            }
        }
        if (listener != null) {
            listener.onFinish(responses);
        }

    }

    /**
     * 解析返回服务器返回数据
     *
     * @param db
     * @param responses
     * @return
     */
    public static boolean handleDeviceResponse(HomeDB db, List<String> responses) {
        if (!responses.isEmpty()) {
            for (String res : responses) {
                if (!TextUtils.isEmpty(res)) {
                    String[] response = res.split("\\|/");
                    String data = response[0];
                    String devIp = response[1];
                    Utility.handleDevice(db, data, devIp);
                }
            }
            return true;
        }
        return false;
    }

    /**
     * 发送设备控制请求
     *
     * @param dev
     * @param devState
     */
    public static void send(DeviceModel dev, String devState) {
        int devNum = dev.getDevNum();
        String devId = dev.getDevId() + ",";
        String devIp = dev.getDevIp();
        String content = "";
        String temp = "00n,";
        for (int i = 1; i < 4; i++) {
            if (i == devNum) {
                temp = devState + ",";
            } else {
                temp = "00n,";
            }
            content += temp;
        }
        String msg = SEND_CODE + devId + content + "0";
        sendTo(devIp, port, msg.getBytes());

    }

    public static void sendTo(String ip, int port, byte[] sendBuf) {
        try {
            if (_client == null) {
                _client = new DatagramSocket(null);
                _client.setReuseAddress(true);
                _client.setBroadcast(true);// 设置标志，T OR E OR R广播数据报
                // 在2.2左右版本不行。在4.0版本上可行
                _client.bind(new InetSocketAddress(port));
            }
            InetAddress address = InetAddress.getByName(ip);
            DatagramPacket sendpacket = new DatagramPacket(sendBuf,
                    SEND_LENGHT, address, port);
            _client.setSoTimeout(2000);
            _client.send(sendpacket);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
