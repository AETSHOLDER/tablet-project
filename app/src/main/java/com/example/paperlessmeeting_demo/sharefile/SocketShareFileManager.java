package com.example.paperlessmeeting_demo.sharefile;

import android.os.Environment;
import android.os.Handler;
import android.os.Message;

import com.example.paperlessmeeting_demo.tool.constant;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
//分享文件
public class SocketShareFileManager {
    private ServerSocket server;
    private Handler handler = null;

    public SocketShareFileManager(Handler handler) {
        this.handler = handler;
        int port = 9999;
        while (port > 9000) {
            try {
                server = new ServerSocket(port);
                break;
            } catch (Exception e) {
                port--;
            }
        }
        SendMessage(1, port,1);
        Thread receiveFileThread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    ReceiveFile("1");
                }
            }
        });
        receiveFileThread.start();
    }

    void SendMessage(int what, Object obj,int actionType) {
        if (handler != null) {
            Message.obtain(handler, what,actionType,actionType ,obj).sendToTarget();
        }
    }
//flag  1:分享  2：  推送
    public  void ReceiveFile(String  flag) {
        try {

            Socket name = server.accept();
            InputStream nameStream = name.getInputStream();
            InputStreamReader streamReader = new InputStreamReader(nameStream);
            BufferedReader br = new BufferedReader(streamReader);
            String fileName = br.readLine();
            br.close();
            streamReader.close();
            nameStream.close();
            name.close();
            SendMessage(2, "正在接收:" + fileName,Integer.parseInt(flag));

            Socket data = server.accept();
            InputStream dataStream = data.getInputStream();
            String path = Environment.getExternalStorageDirectory().getPath() + constant.SHARE_FILE;
            String savePath = path + fileName;
            /*
            * 文件夹不存在创新创建，存在直接写入。
            * */
            File f = new File(path);
            if (!f.exists()) {
                f.mkdir();
            }
            FileOutputStream file = new FileOutputStream(savePath, false);
            byte[] buffer = new byte[1024];
            int size = -1;
            while ((size = dataStream.read(buffer)) != -1) {
                file.write(buffer, 0, size);
            }
            file.close();
            dataStream.close();
            data.close();
            SendMessage(3, savePath ,Integer.parseInt(flag));
        } catch (Exception e) {
            SendMessage(4, "接收错误:\n" + e.getMessage(),Integer.parseInt(flag));
        }
    }

    public void SendFile(ArrayList<String> fileName, ArrayList<String> path, String ipAddress, int port,String actionType) {
        try {
            for (int i = 0; i < fileName.size(); i++) {
                Socket name = new Socket(ipAddress, port);
                OutputStream outputName = name.getOutputStream();
                OutputStreamWriter outputWriter = new OutputStreamWriter(outputName);
                BufferedWriter bwName = new BufferedWriter(outputWriter);
                bwName.write(fileName.get(i));
                bwName.close();
                outputWriter.close();
                outputName.close();
                name.close();
                SendMessage(0, "正在发送" + fileName.get(i),Integer.parseInt(actionType));

                Socket data = new Socket(ipAddress, port);
                OutputStream outputData = data.getOutputStream();
                FileInputStream fileInput = new FileInputStream(path.get(i));
                int size = -1;
                byte[] buffer = new byte[1024];
                while ((size = fileInput.read(buffer, 0, 1024)) != -1) {
                    outputData.write(buffer, 0, size);
                }
                outputData.close();
                fileInput.close();
                data.close();
                SendMessage(5, fileName.get(i) + "  发送完成",Integer.parseInt(actionType));
            }
            SendMessage(5, "所有文件发送完成",Integer.parseInt(actionType));
        } catch (Exception e) {
            SendMessage(6, "发送错误:\n" + e.getMessage(),Integer.parseInt(actionType));
        }
    }
}
