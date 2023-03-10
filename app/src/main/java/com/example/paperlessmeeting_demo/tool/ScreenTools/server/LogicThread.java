package com.example.paperlessmeeting_demo.tool.ScreenTools.server;

import android.util.Log;
import com.example.paperlessmeeting_demo.tool.ScreenTools.entity.ReceiveData;
import com.example.paperlessmeeting_demo.tool.ScreenTools.entity.ReceiveHeader;
import com.example.paperlessmeeting_demo.tool.ScreenTools.utils.AnalyticDataUtils;
import java.io.OutputStream;
import java.net.Socket;

/**
 * Created by wt
 * Date on  2018/7/4 09:48:25.
 *
 * @Desc 处理业务逻辑的线程
 */

public class LogicThread extends Thread {
    private static final String TAG = "LogicThread";
    private ReceiveHeader mHeader;
    private Socket mSocket;
    private AnalyticDataUtils mAnalyticDataUtils;
    private OnAcceptLogicMsgListener mListener;

    public LogicThread(Socket socket, ReceiveHeader header) {
        this.mSocket = socket;
        this.mHeader = header;
        mAnalyticDataUtils = new AnalyticDataUtils();
    }

    @Override
    public void run() {
        super.run();
//        try {
//            ReceiveData receiveData = mAnalyticDataUtils.
//                    analyticData(mSocket.getInputStream(), mHeader);
//            mSocket.getOutputStream();
//            if (mListener != null) {
//                EncodeV1 encodeV1 = mListener.onAcceptLogicMsg(receiveData);
//                if (encodeV1 != null) {
//                    OutputStream outputStream = mSocket.getOutputStream();
//                    outputStream.write(encodeV1.buildSendContent());
//                    //立刻发走字节
//                    outputStream.flush();
//                }
//            }
//            mSocket.close();
//        } catch (Exception e) {
//            Log.e(TAG, e.toString() + "");
//        }
    }

    public void setOnAcceptLogicMsgListener(OnAcceptLogicMsgListener listener) {
        this.mListener = listener;
    }

    public interface OnAcceptLogicMsgListener {
        EncodeV1 onAcceptLogicMsg(ReceiveData data);
    }
}
