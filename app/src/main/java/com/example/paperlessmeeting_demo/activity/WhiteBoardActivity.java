
package com.example.paperlessmeeting_demo.activity;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.JavascriptInterface;
import android.webkit.JsResult;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebStorage;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.Toast;
import com.example.paperlessmeeting_demo.MeetingAPP;
import com.example.paperlessmeeting_demo.R;
import com.example.paperlessmeeting_demo.base.WBBasicActivity;
import com.example.paperlessmeeting_demo.bean.AttendeBean;
import com.example.paperlessmeeting_demo.bean.UserBehaviorBean;
import com.example.paperlessmeeting_demo.tool.FLUtil;
import com.example.paperlessmeeting_demo.tool.TempMeetingTools.im.JWebSocketClientService;
import com.example.paperlessmeeting_demo.tool.TimeUtils;
import com.example.paperlessmeeting_demo.tool.UrlConstant;
import com.example.paperlessmeeting_demo.tool.UserUtil;
import com.example.paperlessmeeting_demo.tool.constant;
import com.orhanobut.hawk.Hawk;
import com.tencent.smtt.sdk.CookieSyncManager;
import java.util.ArrayList;
import java.util.List;
import butterknife.BindView;
import butterknife.ButterKnife;

public class WhiteBoardActivity extends WBBasicActivity {
    private String normalShareOrTeam = "";   //  普通用户进入白板是协作还是同屏
    private String user_id;
    private String token;
    private String m_id;
    private String m_role;
    private String c_id;
    private List<String> attendeIdList = new ArrayList<>();
    @BindView(R.id.webview)
    WebView webView;

    @BindView(R.id.fullscreen_back)
    ImageView back;

    @Override
    protected void initView() {
    }

    @Override
    protected void initData() {
        /**
         * 统计用户行为日志
         * */
        if (Hawk.contains("UserBehaviorBean")) {
            UserBehaviorBean userBehaviorBean = Hawk.get("UserBehaviorBean");
            UserBehaviorBean.DataBean dataBean = new UserBehaviorBean.DataBean();
            dataBean.setTittile(this.getClass().getName());
            dataBean.setTime(TimeUtils.getTime(System.currentTimeMillis()));
            List<UserBehaviorBean.DataBean> dataBeanList = userBehaviorBean.getData();
            dataBeanList.add(dataBean);
            Hawk.put("UserBehaviorBean", userBehaviorBean);
        }

        back.setVisibility(View.INVISIBLE);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //  这段代码为了解决退出再进收不到推送，临时方案
                if(JWebSocketClientService.client != null ){
                    JWebSocketClientService.client.reconnect();
                }
                DeleteCookie();
                finish();
            }
        });

        UrlConstant.webUrl = "http://"+FLUtil.getWhiteIPAdress()+":8888";
        normalShareOrTeam = "";   //  普通用户进入白板是协作还是同屏
        user_id = "";
        m_id = "";
        m_role = "";

        attendeIdList = new ArrayList<>();
        if (Hawk.contains(constant.team_share)) {
            if(!UserUtil.ISCHAIRMAN){  //  主席不传type
                normalShareOrTeam = Hawk.get(constant.team_share);
            }

        }

        if (Hawk.contains(constant.user_id)) {
            user_id = Hawk.get(constant.user_id);
        }
        if (Hawk.contains(constant._id)) {
            m_id = Hawk.get(constant._id);
        }
        if (Hawk.contains(constant.meeting_role)) {
            m_role = Hawk.get(constant.meeting_role);
        }

        String temp = "";
        if (Hawk.contains(constant.attendeBeanList)) {
            List<AttendeBean> attendeBeanList = Hawk.get(constant.attendeBeanList);
            for(int i = 0;i<attendeBeanList.size();i++ ){
                AttendeBean bean = attendeBeanList.get(i);
                attendeIdList.add(bean.getUser_id());
                if(i<attendeBeanList.size()-1){
                    temp += bean.getUser_id() + ",";
                }else {
                    temp += bean.getUser_id();
                }

            }
        }
        if (Hawk.contains(constant.c_id)) {
            c_id = Hawk.get(constant.c_id);
        }

//        String strJson = new Gson().toJson(attendeIdList);

        UrlConstant.webUrl = UrlConstant.webUrl + "?user_id=" + user_id + "&m_id=" + m_id +"&c_id="+ c_id +"&m_role="+ m_role + "&user_list=" +temp+"&ip="+ FLUtil.getWhiteIPAdress()+"&m_type="+normalShareOrTeam;
        Log.e("webUrl====",UrlConstant.webUrl);

        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadUrl(UrlConstant.webUrl);

        // 拦截js弹窗
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptCanOpenWindowsAutomatically(false);

        webView.setWebViewClient(new WebViewClient() {

            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                super.onReceivedError(view, request, error);
                back.setVisibility(View.VISIBLE);
            }

            //            @Override
//            public void onPageFinished(WebView view, String url) {
//
//                if (attendeIdList != null) {
////                    String jsonStr = attendeBeanList.toString();
//
//                    Gson gson = new Gson();
//                    String jsonStr = gson.toJson(attendeIdList);
//                    String JattendeBeanList = String.format("javascript:setSession('userList','%s')",jsonStr);
//                    webView.evaluateJavascript(JattendeBeanList, new ValueCallback<String>() {
//                        @Override
//                        public void onReceiveValue(String value) {
//                            Log.d("evaluateJavascript",value);
//                        }
//                    });
//                }
//
//
//                String JSuser_id = String.format("javascript:setSession('user_id','%s')",user_id);
//                webView.evaluateJavascript(JSuser_id, new ValueCallback<String>() {
//                    @Override
//                    public void onReceiveValue(String value) {
//                        Log.d("evaluateJavascript",value);
//                    }
//                });
//
//                String JS_id = String.format("javascript:setSession('m_id','%s')",m_id);
//                webView.evaluateJavascript(JS_id, new ValueCallback<String>() {
//                    @Override
//                    public void onReceiveValue(String value) {
//                        Log.d("evaluateJavascript",value);
//                    }
//                });
//
//                String JS_role = String.format("javascript:setSession('m_role','%s')",m_role);
//                webView.evaluateJavascript(JS_role, new ValueCallback<String>() {
//                    @Override
//                    public void onReceiveValue(String value) {
//                        Log.d("evaluateJavascript",value);
//                    }
//                });
//
//                token = "Bearer " + token;
//                String jstoken = String.format("javascript:setSession('token','%s')",token);
//
//                Log.d("Bearer token",jstoken);
//                webView.evaluateJavascript(jstoken, new ValueCallback<String>() {
//                    @Override
//                    public void onReceiveValue(String value) {
//                        Log.d("evaluateJavascript",value);
//                    }
//                });
//
//                if(!normalShareOrTeam.isEmpty()){
//                    Log.d("normalShareOrTeam","m_type==="+normalShareOrTeam);
//                    String jsnormalShareOrTeamn = String.format("javascript:setSession('m_type','%s')",normalShareOrTeam);
//                    webView.evaluateJavascript(jsnormalShareOrTeamn, new ValueCallback<String>() {
//                        @Override
//                        public void onReceiveValue(String value) {
//                            Log.d("evaluateJavascript",value);
//                        }
//                    });
//                }
//            }
        });

        webView.setWebChromeClient(new WebChromeClient() {
            // 通过alert()和confirm()拦截
            @Override
            public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
                Log.e("收到消息",""+message);
                // 退出
                if(message.equals("exit")){
                    //  这段代码为了解决退出再进收不到推送，临时方案
                    if(JWebSocketClientService.client != null ){
                        JWebSocketClientService.client.reconnect();
                    }

                    DeleteCookie();
                    finish();
                    result.confirm();
                    return true;
                }else if(message.equals("哈哈哈")) {

                    Log.e("收到消息",""+message);

                    result.confirm();
                    return true;
                }else{
                    return super.onJsAlert(view, url, message, result);
                }
//                else if (message.equals("share")){ // 同屏
//                    webView.evaluateJavascript("javascript:setSession('m_type','share')", new ValueCallback<String>() {
//                        @Override
//                        public void onReceiveValue(String value) {
//                            Log.d("evaluateJavascript",value);
//                        }
//                    });
//                    result.confirm();
//                    return true;
//                }else if (message.equals("team")){ //  协作
//                    webView.evaluateJavascript("javascript:setSession('m_type','team')", new ValueCallback<String>() {
//                        @Override
//                        public void onReceiveValue(String value) {
//                            Log.d("evaluateJavascript",value);
//                        }
//                    });
//                    result.confirm();
//                    return true;
//                }
            }
        });
    }

    private void DeleteCookie() {
        CookieSyncManager.createInstance( MeetingAPP.getContext());
        CookieManager cookieManager = CookieManager.getInstance();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            cookieManager.removeSessionCookies(null);
            cookieManager.removeAllCookies(new ValueCallback<Boolean>() {
                @Override
                public void onReceiveValue(Boolean aBoolean) {

                }
            });
            cookieManager.flush();
        } else {
            cookieManager.removeSessionCookies(null);
            cookieManager.removeAllCookies(new ValueCallback<Boolean>() {
                @Override
                public void onReceiveValue(Boolean aBoolean) {

                }
            });
            CookieSyncManager.getInstance().sync();
        }

        WebStorage.getInstance().deleteAllData(); //清空WebView的localStorage
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        if (Hawk.contains(constant.team_share)) {
//            Hawk.delete(constant.team_share);
//        }
    }

    @JavascriptInterface
    public void showToast() {
        Toast.makeText(this, "hello", Toast.LENGTH_LONG).show();
    }


    @Override
    protected int getLayoutId() {
        return R.layout.activity_whiteboard;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        //  这段代码为了解决退出再进收不到推送，临时方案
        JWebSocketClientService.client.reconnect();

        DeleteCookie();
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

}


