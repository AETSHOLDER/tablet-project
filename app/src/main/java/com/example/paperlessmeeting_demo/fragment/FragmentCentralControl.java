package com.example.paperlessmeeting_demo.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.paperlessmeeting_demo.R;
import com.example.paperlessmeeting_demo.base.BaseFragment;
import com.example.paperlessmeeting_demo.bean.UserBehaviorBean;
import com.example.paperlessmeeting_demo.tool.TimeUtils;
import com.orhanobut.hawk.Hawk;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by 梅涛 on 2020/10/9.
 */
@SuppressLint("ValidFragment")
public class FragmentCentralControl extends BaseFragment {
    @BindView(R.id.image_chandelier)
    ImageView imageChandelier;
    @BindView(R.id.chandelier_tv)
    TextView chandelierTv;
    @BindView(R.id.sb_chandelier)
    SeekBar sbChandelier;
    @BindView(R.id.image_air_conditioning)
    ImageView imageAirConditioning;
    @BindView(R.id.temperature)
    TextView temperature;
    @BindView(R.id.cool)
    ImageView cool;
    @BindView(R.id.hot)
    ImageView hot;
    @BindView(R.id.swh_status)
    Switch swhStatus;
    @BindView(R.id.image_casting_device)
    ImageView imageCastingDevice;
    @BindView(R.id.casting_device_tv)
    TextView castingDeviceTv;
    @BindView(R.id.switch_casting_device)
    Switch switchCastingDevice;
    @BindView(R.id.image_curtains)
    ImageView imageCurtains;
    @BindView(R.id.curtains_tv)
    TextView curtainsTv;
    @BindView(R.id.sb_curtains)
    SeekBar sbCurtains;
    @BindView(R.id.image_pan_tilt_camera)
    ImageView imagePanTiltCamera;
    @BindView(R.id.pan_tilt_camera_tv)
    TextView panTiltCameraTv;
    @BindView(R.id.switch_pan_tilt_camera)
    Switch switchPanTiltCamera;
    @BindView(R.id.image_speakers)
    ImageView imageSpeakers;
    @BindView(R.id.speakers_tv)
    TextView speakersTv;
    @BindView(R.id.switch_speakers)
    Switch switchSpeakers;
    @BindView(R.id.cc)
    TextView cc;
    private String titles;
    private Context context;
    private int chandelierVule;
    private int sbCurtainsVule;
    private int temperatureInt;

    public FragmentCentralControl() {
    }

    public FragmentCentralControl(String titles, Context context) {
        this.titles = titles;
        this.context = context;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_central_control;
    }

    @Override
    protected boolean isBindEventBus() {
        return false;
    }

    @Override
    protected void initData() {

                /*
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
        temperatureInt = Integer.parseInt(temperature.getText().toString().trim());
        hot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                temperatureInt++;
                if (temperatureInt > 30) {
                    Toast.makeText(getActivity(), "超过空凋最大值了", Toast.LENGTH_LONG).show();
                    return;
                }
                temperature.setText(temperatureInt + "");
            }
        });
        cool.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                temperatureInt--;
                if (temperatureInt < 16) {
                    Toast.makeText(getActivity(), "超过空凋最小值了", Toast.LENGTH_LONG).show();
                    return;
                }
                temperature.setText(temperatureInt + "");
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        unbinder = ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
       /* if (unbinder!=null){
            unbinder.unbind();
        }*/

    }

    @Override
    protected void initView() {
//吊灯
        sbChandelier.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                chandelierTv.setText("亮度" + progress + "%" + "未检测到设备");
                chandelierVule = progress;
                if (0 == chandelierVule) {
                    chandelierTv.setText("亮度" + progress + "%");
                    imageChandelier.setImageResource(R.mipmap.ic_chandelier_un);
                } else {
                    chandelierTv.setText("亮度" + progress + "%" + "未检测到设备");
                    imageChandelier.setImageResource(R.mipmap.ic_chandelier);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                if (0 == chandelierVule) {
                    imageChandelier.setImageResource(R.mipmap.ic_chandelier_un);
                } else {
                    imageChandelier.setImageResource(R.mipmap.ic_chandelier);
                }
            }
        });

        //空凋

        swhStatus.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                //控制开关字体颜色
                if (b) {
                    imageAirConditioning.setImageResource(R.mipmap.ic_air_conditioning);
                    cc.setText("未检测到设备");
                } else {
                    imageAirConditioning.setImageResource(R.mipmap.ic_air_conditioning_un);
                    cc.setText("设备已关闭");
                }
            }
        });

        //投屏设备
        switchCastingDevice.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                //控制开关字体颜色
                if (b) {
                    imageCastingDevice.setImageResource(R.mipmap.ic_casting_device);
                    castingDeviceTv.setText("未检测到设备");
                } else {
                    imageCastingDevice.setImageResource(R.mipmap.ic_casting_device_un);
                    castingDeviceTv.setText("设备已关闭");
                }
            }
        });
//窗帘
        sbCurtains.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                curtainsTv.setText("打开" + progress + "%" + "未检测到设备");
                sbCurtainsVule = progress;
                if (0 == sbCurtainsVule) {
                    curtainsTv.setText("打开" + progress + "%");
                    imageCurtains.setImageResource(R.mipmap.ic_curtains_un);
                } else {
                    curtainsTv.setText("打开" + progress + "%" + "未检测到设备");
                    imageCurtains.setImageResource(R.mipmap.ic_curtains);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                if (0 == sbCurtainsVule) {
                    imageCurtains.setImageResource(R.mipmap.ic_curtains_un);
                } else {
                    imageCurtains.setImageResource(R.mipmap.ic_curtains);
                }
            }
        });

        //云台摄像头
        switchPanTiltCamera.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                //控制开关字体颜色
                if (b) {
                    imagePanTiltCamera.setImageResource(R.mipmap.ic_pan_tilt_camera);
                    panTiltCameraTv.setText("未检测到设备");
                } else {
                    imagePanTiltCamera.setImageResource(R.mipmap.ic_pan_tilt_camera_un);
                    panTiltCameraTv.setText("设备已关闭");
                }
            }
        });
//音响
        switchSpeakers.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                //控制开关字体颜色
                if (b) {
                    imageSpeakers.setImageResource(R.mipmap.ic_speakers);
                    speakersTv.setText("未检测到设备");
                } else {
                    imageSpeakers.setImageResource(R.mipmap.ic_speakers_un);
                    speakersTv.setText("设备已关闭");
                }
            }
        });
    }
}
