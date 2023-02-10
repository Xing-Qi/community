package com.nowcoder.community;

import java.io.IOException;

/**
 * @author Oliver
 * @create 2023-02-10 11:42
 */
public class WkTests {
    public static void main(String[] args) throws IOException {
    String cmd =
            "d:/JavaTools/wkhtmltopdf/bin/wkhtmltoimage --quality 75 https://baidu.com D:/JavaTools/data/wk-image/3.png";
        Runtime.getRuntime().exec(cmd);
        System.out.println("ok");
    }
}
