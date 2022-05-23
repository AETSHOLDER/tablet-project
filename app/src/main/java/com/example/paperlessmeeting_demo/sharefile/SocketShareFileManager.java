package com.example.paperlessmeeting_demo.sharefile;

import static com.example.paperlessmeeting_demo.tool.constant.WUHUPUSH;

import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.blankj.utilcode.util.StringUtils;
import com.example.paperlessmeeting_demo.R;
import com.example.paperlessmeeting_demo.tool.constant;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
//分享文件
public class SocketShareFileManager {
    private ServerSocket server;
    private Handler handler = null;
    private String savePath = null;
    private String signFlag = "signPath=";
    private String voteFlag = "voteType=";
   private  String pushFlag="-push";
    private  String shareFlag="-share";
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
        SendMessage(1, port, 1);
        Thread receiveFileThread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    ReceiveFile();
                }
            }
        });
        receiveFileThread.start();
    }

    void SendMessage(int what, Object obj, int actionType) {
        if (handler != null) {
            Message.obtain(handler, what, actionType, actionType, obj).sendToTarget();
        }
    }

    //flag  1:分享  2：  推送
    public void ReceiveFile() {
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

            Log.e("111","fileName==="+fileName);
            /**
             * 批注文件接收时path是全路径，signFlag区分
             * */
            String path = null;
            if (fileName.contains(signFlag)) {
                Log.e("111","正在接收文件===");
                SendMessage(2, "正在接收:" + fileName, 0);
                path = fileName.replace(signFlag, "");
                savePath = path;
                File file = new File(path);
                try {
                    File directory = file.getParentFile();
                    if (!directory.exists() && !directory.mkdirs()) {
                        Log.e("cuowuwuwu","创建文件夹失败!!!!");
                        return;
                    }
                    file.createNewFile();
                } catch (Exception e) {
                    e.printStackTrace();
                    return;
                }

            } else if (fileName.contains(voteFlag)) {
                path = Environment.getExternalStorageDirectory().getPath() + constant.VOTE_FILE;
                try {
                    savePath = path + fileName.replace(voteFlag, "");
                } catch (Exception e) {
                    e.printStackTrace();
                }
                File f = new File(path);
                if (!f.exists()) {
                    f.mkdir();
                }
            } else {
                SendMessage(2, "正在接收:" + fileName, 0);
                path = Environment.getExternalStorageDirectory().getPath() + constant.SHARE_FILE;
                savePath = path + fileName;
                File f = new File(path);
                if (!f.exists()) {
                    f.mkdir();
                }
            }

            Log.e("aaaawww", "savePath==" + savePath+"fileName="+fileName);

            Socket data = server.accept();
            InputStream dataStream = data.getInputStream();
            /*
             * 文件夹不存在创新创建，存在直接写入。
             * */

            FileOutputStream file = new FileOutputStream(savePath, false);
            byte[] buffer = new byte[1024];
            int size = -1;
            while ((size = dataStream.read(buffer)) != -1) {
                file.write(buffer, 0, size);
            }
            file.close();
            dataStream.close();
            data.close();
            Log.d("fenxiasng", "savePath==" + savePath+"fileName="+fileName);
            if (fileName.contains(signFlag)){
                SendMessage(101, "接收完成:" + fileName, 0);
            }else if (fileName.contains(constant.WUHUPUSH)){
                SendMessage(88, savePath, 0);
            }else if (fileName.contains(constant.WUHUSHARE)){
                SendMessage(33, savePath, 0);
            }

        } catch (Exception e) {
            SendMessage(4, "接收错误:\n" + e.getMessage(), 0);
        }
    }

    public void SendFlag(String flag) {
        if (StringUtils.isEmpty(savePath)) {
            Log.e("SendFlag", "savePath===null");
            return;
        }
        //flag  1:分享  2：  推送 3投票
        if (flag.equals("1")) {
            SendMessage(3, savePath, 1);
        }
        if (flag.equals("2")) {
            Log.d("SendFlag222接收", "22222222");
            SendMessage(8, savePath, 2);
        }
        if (flag.equals("3")) {
            SendMessage(32, savePath, 3);
        }
    }

    /**
     * 批注文件传输
     */
    public void SendFile(String fileName, String path, String ipAddress, int port, String actionType) {
        try {
            Socket name = new Socket(ipAddress, port);
            OutputStream outputName = name.getOutputStream();
            OutputStreamWriter outputWriter = new OutputStreamWriter(outputName);
            BufferedWriter bwName = new BufferedWriter(outputWriter);
            bwName.write(signFlag + path);
            bwName.close();
            outputWriter.close();
            outputName.close();
            name.close();
            SendMessage(0, "正在发送" + fileName, Integer.parseInt(actionType));

            Socket data = new Socket(ipAddress, port);
            OutputStream outputData = data.getOutputStream();
            FileInputStream fileInput = new FileInputStream(path);
            int size = -1;
            byte[] buffer = new byte[1024];
            while ((size = fileInput.read(buffer, 0, 1024)) != -1) {
                outputData.write(buffer, 0, size);
            }
            outputData.close();
            fileInput.close();
            data.close();
            SendMessage(5, "文件发送完成", Integer.parseInt(actionType));

        } catch (Exception e) {
            SendMessage(6, "发送错误:\n" + e.getMessage(), Integer.parseInt(actionType));
        }
    }
  //芜湖分享  推送       //flag  1:分享  2：  推送
    public void SendFile(ArrayList<String> fileName, ArrayList<String> path, String ipAddress, int port, String actionType,String flag) {
        try {
            long total = 0;
            for (int i = 0; i < fileName.size(); i++) {
                Socket name = new Socket(ipAddress, port);
                OutputStream outputName = name.getOutputStream();
                OutputStreamWriter outputWriter = new OutputStreamWriter(outputName);
                BufferedWriter bwName = new BufferedWriter(outputWriter);
                if (actionType.equals("1")){
                    bwName.write(flag+constant.WUHUSHARE+fileName.get(i));
                }else if (actionType.equals("2")){

                    bwName.write(flag+constant.WUHUPUSH+fileName.get(i));
                }


                Log.d("fileName~=",  flag+"="+fileName.get(i));
                bwName.close();
                outputWriter.close();
                outputName.close();
                name.close();
                if (actionType.equals("3")) {

                } else {
                    SendMessage(0, "正在发送" + fileName.get(i), Integer.parseInt(actionType));
                }


                Socket data = new Socket(ipAddress, port);
                OutputStream outputData = data.getOutputStream();
                FileInputStream fileInput = new FileInputStream(path.get(i));
                File fileSize = new File(path.get(i));//计算文件大小
                int size = -1;
                byte[] buffer = new byte[1024];
                while ((size = fileInput.read(buffer, 0, 1024)) != -1) {
                    outputData.write(buffer, 0, size);
                    total += size;

                    Log.d("Upload progress", "" + (int) ((total * 100)/buffer.length)+"fileSize大小="+fileSize.length()+"   传输进度="+ 100 * total/fileSize.length());
                    SendMessage(10, 100 * total/fileSize.length()+"", Integer.parseInt(actionType));
                }
                outputData.close();
                fileInput.close();
                data.close();
            }
            if (actionType.equals("3")) {

            } else {
                SendMessage(55, "所有文件发送完成", Integer.parseInt(actionType));
            }


            try {
                DatagramSocket socket = new DatagramSocket();
                String str = null;
                if (actionType.equals("1")) {
                    str = constant.TEMP_MEETINGSHARE_FILE;
                } else if (actionType.equals("3")) {
                    str = constant.TEMP_VOTE_IMAGE_FILE;
                } else if (actionType.equals("2")) {
                    Log.d("SendFlag3333通知", "savePath===null");
                    str = constant.TEMP_MEETINGPUSH_FILE;
                }

                byte[] sendStr = str.getBytes();
                InetAddress address = InetAddress.getByName(constant.EXTRAORDINARY_MEETING_INETADDRESS);
                DatagramPacket packet = new DatagramPacket(sendStr, sendStr.length, address, constant.EXTRAORDINARY_MEETING_PORT);
                socket.send(packet);
                Log.d("SendFlag444通知", "savePath===null");
                socket.close();
            } catch (SocketException e) {
                e.printStackTrace();
            } catch (UnknownHostException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            if (actionType.equals("3")) {

            } else {
                SendMessage(6, "发送错误:\n" + e.getMessage(), Integer.parseInt(actionType));
            }

        }
    }
    public void SendNewVoteFile(ArrayList<String> fileName, ArrayList<String> path, String ipAddress, int port, String actionType) {
        try {
            for (int i = 0; i < fileName.size(); i++) {
                Socket name = new Socket(ipAddress, port);
                OutputStream outputName = name.getOutputStream();
                OutputStreamWriter outputWriter = new OutputStreamWriter(outputName);
                BufferedWriter bwName = new BufferedWriter(outputWriter);
                bwName.write(voteFlag+fileName.get(i));
                bwName.close();
                outputWriter.close();
                outputName.close();
                name.close();
                if (actionType.equals("3")) {

                } else {
                   // SendMessage(0, "正在发送" + fileName.get(i), Integer.parseInt(actionType));
                }


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
            }
            if (actionType.equals("3")) {

            } else {
               SendMessage(5, "所有文件发送完成", Integer.parseInt(actionType));
            }


            try {
                DatagramSocket socket = new DatagramSocket();
                String   str = constant.TEMP_VOTE_IMAGE_FILE;
               /* if (actionType.equals("1")) {
                    str = constant.TEMP_MEETINGSHARE_FILE;
                } else if (actionType.equals("3")) {

                } else if (actionType.equals("2")) {
                    str = constant.TEMP_MEETINGPUSH_FILE;
                }*/

                byte[] sendStr = str.getBytes();
                InetAddress address = InetAddress.getByName(constant.EXTRAORDINARY_MEETING_INETADDRESS);
                DatagramPacket packet = new DatagramPacket(sendStr, sendStr.length, address, constant.EXTRAORDINARY_MEETING_PORT);
                socket.send(packet);
                socket.close();
            } catch (SocketException e) {
                e.printStackTrace();
            } catch (UnknownHostException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            if (actionType.equals("3")) {

            } else {
              //  SendMessage(6, "发送错误:\n" + e.getMessage(), Integer.parseInt(actionType));
            }

        }
    }
  /*  public void SendVoteFile(ArrayList<String> fileName, ArrayList<String> path, String ipAddress, int port, String actionType) {

            for (int i = 0; i < path.size(); i++) {
                try {
                    Socket name = new Socket(ipAddress, port);
                    OutputStream outputName = name.getOutputStream();
                    OutputStreamWriter outputWriter = new OutputStreamWriter(outputName);
                    BufferedWriter bwName = new BufferedWriter(outputWriter);
                    bwName.write(voteFlag + path);
                    bwName.close();
                    outputWriter.close();
                    outputName.close();
                    name.close();
                    // SendMessage(0, "正在发送" + fileName, Integer.parseInt(actionType));

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

                    //SendMessage(5, "文件发送完成", Integer.parseInt(actionType));

                } catch (Exception e) {
                    // SendMessage(6, "发送错误:\n" + e.getMessage(), Integer.parseInt(actionType));
                }


                try {
                    DatagramSocket socket = new DatagramSocket();
                    String str = null;
                    if (actionType.equals("1")) {
                        str = constant.TEMP_MEETINGSHARE_FILE;
                    } else if (actionType.equals("3")) {
                        str = constant.TEMP_VOTE_IMAGE_FILE;
                    } else if (actionType.equals("2")) {
                        str = constant.TEMP_MEETINGPUSH_FILE;
                    }

                    byte[] sendStr = str.getBytes();
                    InetAddress address = InetAddress.getByName(constant.EXTRAORDINARY_MEETING_INETADDRESS);
                    DatagramPacket packet = new DatagramPacket(sendStr, sendStr.length, address, constant.EXTRAORDINARY_MEETING_PORT);
                    socket.send(packet);
                    socket.close();
                } catch (SocketException e) {
                    e.printStackTrace();
                } catch (UnknownHostException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

    }*/
}