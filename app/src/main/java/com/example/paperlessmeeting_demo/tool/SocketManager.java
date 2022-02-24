package com.example.paperlessmeeting_demo.tool;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;

import android.os.Environment;

/**
 * Socket管理类
 * 1、接收文件
 * 2、发送文件
 *
 * @author jiaocg
 */


public class SocketManager {

    public static ServerSocket server;


    public SocketManager(ServerSocket server) {
        this.server = server;
    }
    //接收文件


    public static  String ReceiveFile() {
        try {
            //接收文件名
            Socket name = server.accept();
            //获取数据的输入流
            InputStream nameStream = name.getInputStream();
            //字符流转换为字节流
            InputStreamReader streamReader = new InputStreamReader(nameStream);
            BufferedReader br = new BufferedReader(streamReader);
            //接收到文件名
            String fileName = br.readLine();
            br.close();
            streamReader.close();
            nameStream.close();
            name.close();
            //接收文件数据
            Socket data = server.accept();
            InputStream dataStream = data.getInputStream();

            String savePath = Environment.getExternalStorageDirectory().getPath() + "/" + fileName;
            FileOutputStream file = new FileOutputStream(savePath, false);
            byte[] buffer = new byte[1024];
            int size = -1;
            while ((size = dataStream.read(buffer)) != -1) {
                file.write(buffer, 0, size);

            }
            file.close();
            dataStream.close();
            data.close();
            return fileName + "---接收完成" + "---保存至：" + savePath;

        } catch (Exception e) {
            System.out.println(e.toString());
            return "接收错误:\n" + e.getMessage();

        }

    }

    //发送文件


    public static  String SendFile(String fileName, String path, String ipAddress, int port) {
        try {
            //发送文件名
            //首先发送文件名
            Socket name = new Socket(ipAddress, port);
            //创建输出流
            OutputStream outputName = name.getOutputStream();
            //字符流转换为字节流
            OutputStreamWriter outputWriter = new OutputStreamWriter(outputName);
            //缓冲区
            BufferedWriter bwName = new BufferedWriter(outputWriter);
            //写到缓冲区
            bwName.write(fileName);
            bwName.close();
            outputWriter.close();
            outputName.close();
            name.close();

            //发送文件数据
            Socket data = new Socket(ipAddress, port);
            //创建输出流
            OutputStream outputData = data.getOutputStream();
            //从文件系统中的某个文件中获得输入字节
            FileInputStream fileInput = new FileInputStream(path);
            int size = -1;
            byte[] buffer = new byte[1024];
            while ((size = fileInput.read(buffer, 0, 1024)) != -1) {
                outputData.write(buffer, 0, size);

            }
            outputData.close();
            fileInput.close();
            data.close();
            return fileName + " 发送完成";

        } catch (Exception e) {
            return "发送错误:\n" + e.getMessage();

        }

    }

}