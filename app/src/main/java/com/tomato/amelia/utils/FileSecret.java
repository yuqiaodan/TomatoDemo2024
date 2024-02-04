package com.tomato.amelia.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;

/**
 * Created by qiaodan on 2022/11/22
 * Description:文件加解密测试
 */
public class FileSecret {
    //输出路径
    private static final String ROOT_PATH = System.getProperty("user.dir") + "/fileSecret/";

    //文件名称

    //加密文件来源
    private static final String ENCRYPT_INPUT_FILE_NAME = "app-release.apk";
    //加密文件输出
    private static final String ENCRYPT_OUTPUT_FILE_NAME = "bgm.mp3";

    //解密文件来源
    private static final String DECRYPT_INPUT_FILE_NAME = "bgm.mp3";
    //解密文件输出
    private static final String DECRYPT_OUTPUT_FILE_NAME = "app-release-解密1.apk";


    public static void main(String[] args) {
        System.out.println("开始加密: " + System.currentTimeMillis());
        encryptFile(ENCRYPT_INPUT_FILE_NAME, ENCRYPT_OUTPUT_FILE_NAME, ROOT_PATH);
        System.out.println("加密完成: " + System.currentTimeMillis());

        System.out.println("开始解密");
        decryptFile(DECRYPT_INPUT_FILE_NAME,DECRYPT_OUTPUT_FILE_NAME, ROOT_PATH);
        System.out.println("解密完成");
    }

    /***
     * 加密文件
     * 文件转为字节流 交换其中两位字节位置 所以加解密Key 可以是字节Index
     * 例如这里固定为[1，100]
     * ***/
    public static void encryptFile(String inputPath, String outputPath, String rootPath) {
        try {
            byte key = 8;
            //找到要加密的文件，盘符自己指定，输入输出不需要在同一个盘符
            File inFile = new File(rootPath, inputPath);
            //将要加密的文件输出到指定的盘符
            File outFile = new File(rootPath, outputPath);
            if (outFile.exists()) {
                System.out.println("文件已存在，删除");
                outFile.delete();
            } else {
                System.out.println("文件不存在 开始加密");
            }
            InputStream input = new FileInputStream(inFile);
            FileOutputStream output = new FileOutputStream(outFile);
            int len = 0;
            byte[] array = new byte[1024];
            while ((len = input.read(array)) > 0) {

                if (len > 1000) {
                    byte a1 = array[1];
                    byte a2 = array[100];
                    array[1] = a2;
                    array[100] = a1;
                }
                output.write(array, 0, len);
                //写到输出文件流中
            }
            output.flush();
            //关闭资源
            input.close();
            output.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /***
     * 解密文件
     *
     * ***/
    public static void decryptFile(String inputPath, String outputPath, String rootPath) {
        try {
            byte key = 8;
            //找到要加密的文件，盘符自己指定，输入输出不需要在同一个盘符
            File inFile = new File(rootPath, inputPath);
            //将要加密的文件输出到指定的盘符
            File outFile = new File(rootPath, outputPath);
            if (outFile.exists()) {
                System.out.println("文件已存在，删除");
                outFile.delete();
            } else {
                System.out.println("文件不存在 开始加密");
            }
            InputStream input = new FileInputStream(inFile);
            FileOutputStream output = new FileOutputStream(outFile);
            int len = 0;
            byte[] array = new byte[1024];
            while ((len = input.read(array)) > 0) {
                if (len > 1000) {
                    byte a1 = array[1];
                    byte a2 = array[100];
                    array[1] = a2;
                    array[100] = a1;
                }
                output.write(array, 0, len);
                //写到输出文件流中
            }
            output.flush();
            //关闭资源
            input.close();
            output.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



}
