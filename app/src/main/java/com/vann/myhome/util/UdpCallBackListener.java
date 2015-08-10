package com.vann.myhome.util;

import java.util.List;

/**
 * @Author: wenlong.bian 2015-08-10
 * @E-mail: bxl049@163.com
 */
public interface UdpCallBackListener {
    void onFinish(List<String> response);

    void onError(Exception e);
}
