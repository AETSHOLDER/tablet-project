package com.example.gpy.whiteboard;

import android.app.Application;

import com.alibaba.android.arouter.launcher.ARouter;
import com.example.gpy.whiteboard.utils.SdCardStatus;
import com.example.gpy.whiteboard.utils.StoreUtil;
import com.github.guanpy.wblib.utils.AppContextUtil;
import com.github.guanpy.wblib.utils.OperationUtils;

/**
 * Created by gpy on 2015/8/17.
 */
public class SelfApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        AppContextUtil.init(this);
        SdCardStatus.init(StoreUtil.CACHE_DIR);
        OperationUtils.getInstance().init();
        if (BuildConfig.DEBUG) {
            //开启InstantRun之后，一定要在ARouter.init之前调用openDebug
            ARouter.openDebug();
            ARouter.openLog();
        } else {
            ARouter.getInstance().inject(this);
        }

    }
}
