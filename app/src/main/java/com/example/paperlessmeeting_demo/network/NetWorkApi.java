package com.example.paperlessmeeting_demo.network;


import com.example.paperlessmeeting_demo.bean.AgreementBean;
import com.example.paperlessmeeting_demo.bean.AttendeBean;
import com.example.paperlessmeeting_demo.bean.BasicResponse;
import com.example.paperlessmeeting_demo.bean.CameraInfoBean;
import com.example.paperlessmeeting_demo.bean.CreateFileBeanResponse;
import com.example.paperlessmeeting_demo.bean.DeviceListBean;
import com.example.paperlessmeeting_demo.bean.DeviceTypeBean;
import com.example.paperlessmeeting_demo.bean.LoginBean;
import com.example.paperlessmeeting_demo.bean.MeetingUserInfoBean;
import com.example.paperlessmeeting_demo.bean.MergeChunkBean;
import com.example.paperlessmeeting_demo.bean.NewFileBean;
import com.example.paperlessmeeting_demo.bean.PaperlessBean;
import com.example.paperlessmeeting_demo.bean.StreamConfigurationBean;
import com.example.paperlessmeeting_demo.bean.UUIdBean;
import com.example.paperlessmeeting_demo.bean.VoteListBean;
import com.example.paperlessmeeting_demo.bean.MeetingInfoBean;
import com.example.paperlessmeeting_demo.bean.UploadBean;
import com.example.paperlessmeeting_demo.bean.WuHuEditBean;
import com.example.paperlessmeeting_demo.bean.WuHuEditBeanRequset;
import com.example.paperlessmeeting_demo.bean.WuHuMeetingListResponse;
import com.example.paperlessmeeting_demo.bean.WuHuNetFileBean;

import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.HTTP;
import retrofit2.http.Multipart;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import okhttp3.MultipartBody;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.PartMap;
import retrofit2.http.Path;
import retrofit2.http.Part;
import retrofit2.http.Query;
import retrofit2.http.Streaming;

/**
 * Created by window on 2018/3/7.
 */

public interface NetWorkApi {

    /*
     *  通过userId登录，临时使用
     * */
    @POST("loginByUserId")
    @FormUrlEncoded
    Observable<BasicResponse<LoginBean>> loginByUserId(@FieldMap Map<String, Object> map);


    @POST("loginUser")
    @FormUrlEncoded
    Observable<BasicResponse<LoginBean>> login(@FieldMap Map<String, Object> map);

    //芜湖版注册
    @POST("ppl/register")
    @FormUrlEncoded
    Observable<BasicResponse<String>> pplRegister(@FieldMap Map<String, Object> map);

    //芜湖版查询注册信息
    @GET("ppl/findPPLByMac")
    Observable<BasicResponse<String>> findPaperLessInfo(@Query("mac") String mac); // 查询发言单元注册信息

    //芜湖版结束会议
    @PUT("meeting/finishMeeting")
    Observable<BasicResponse> finishWUHUMeeting(@Body Map<String, Object> map);

    //获取文件列表
    @GET("findMeetingFileList")
    Observable<BasicResponse<NewFileBean>> getFileList(@Query("meeting_record_id") String meeting_record_id, @Query("type") String type);

    //
    @GET("meetingFile/getMeetingFile")
    Observable<BasicResponse<List<WuHuNetFileBean.DataBean>>> getWuHuFileList(@Query("meeting_id") String meeting_record_id);


    //获取文件列表
    @GET("fqMeetingFile")
    Observable<BasicResponse<List<NewFileBean.MeetingFileListBean>>> getSearchFileList(@Query("key") String key, @Query("c_id") String c_id, @Query("meeting_record_id") String meeting_record_id);

    //获取参会人员列表
    @GET("findMeetingUserList")
    Observable<BasicResponse<List<AttendeBean>>> getMeetingUserList(@Query("meeting_record_id") String meeting_record_id);

    //模糊查询参会人员列表
    @GET("fqMeetingUser")
    Observable<BasicResponse<List<AttendeBean>>> getSearchMeetingUser(@Query("key") String key);

    //  投票列表
    @GET("findMeetingVoteList")
    Observable<BasicResponse<ArrayList<VoteListBean.VoteBean>>> findMeetingVoteList(@Query("meeting_record_id") String meeting_record_id);

    //  创建投票
    @POST("createMeetingVote")
    Observable<BasicResponse<VoteListBean>> createMeetingVote(@Body Map<String, Object> map);

    //  投票
    @POST("toVote")
    Observable<BasicResponse> toVote(@Body Map<String, Object> map);

    //  删除投票
    @POST("removeMeetingVote")
    Observable<BasicResponse> removeMeetingVote(@Body Map<String, Object> map);

    //  结束投票
    @POST("finishVote")
    Observable<BasicResponse> finishVote(@Body Map<String, Object> map);

    //  更新投票
    @PUT("updateMeetingVote")
    Observable<BasicResponse> updateMeetingVote(@Body Map<String, Object> map);

    //文件上传接口
    @Multipart
    @POST("upload")
    Observable<BasicResponse<UploadBean>> upLoadFile(@Query("dirName") String dirName, @Query("uid") String uid, @Query("file") String f, @Part MultipartBody.Part file);

    //大文件合并
    @POST("mergeChunk")
    Observable<BasicResponse<MergeChunkBean>> mergeChunk(@Body Map<String, Object> map);
    /*
     *  大文件分片上传
     * */
    //文件上传接口
    @Multipart
    @POST("receiveChunk")
    Observable<BasicResponse> receiveChunk(@Part MultipartBody.Part file);


    /*   *//*
     *  大文件分片上传
     * *//*
    //文件上传接口
    @Multipart
    @POST("receiveChunk")
    Observable<BasicResponse> receiveChunk( @Query("hash") String hash, @Query("fileName") String fileName);*/
    //创建文件
    @POST("createMeetingFile")
    Observable<BasicResponse<CreateFileBeanResponse>> createMeetingFile(@Body Object fields);
    //芜湖获取会议列表
    @GET("meetingList")
    Observable<BasicResponse<List<WuHuMeetingListResponse>>> meetingList(@Query("name") String name,@Query("startTime") String startTime);

    //芜湖修改会议
    @PUT("meeting")
    Observable<BasicResponse> meeting(@Body WuHuEditBeanRequset wuHuEditBeanRequset);
    //下载文件、、
    @Streaming
    @POST("downloadFile")
    Observable<BasicResponse> downloadFile(@Query("uri") String uri, @Query("dest") String dest);

    //查询会议信息
    @GET("findMeetingRecordInfo")
    Observable<BasicResponse<MeetingInfoBean>> getMeetingInfo(@Query("id") String id);

    //查询会议列表信息
    @GET("findMeetingRecordList")
    Observable<BasicResponse<List<MeetingInfoBean>>> getMeetingListInfo();

    //参会人员签到
    @PUT("signIn")
    @FormUrlEncoded
    Observable<BasicResponse> sign(@FieldMap Map<String, Object> map);

    //创建文件
    @POST("delayMeeting")
    Observable<BasicResponse> delayMeeting(@Body Map<String, Object> map);

    //创建文件
    @POST("finishMeetingRecord")
    Observable<BasicResponse> finishMeeting(@Body Map<String, Object> map);

    //参会人员信息
    @GET("findMeetingUserInfo")
    Observable<BasicResponse<MeetingUserInfoBean>> findMeetingUserInfo(@Query("user_id") String user_id, @Query("meeting_record_id") String meeting_record_id);

    //会议文件公开、私有交替改变
    @POST("updateUnclassified")
    Observable<BasicResponse> updateUnclassified(@Body Map<String, Object> map);

    //删除文件

    /*  @FormUrlEncoded
      @DELETE("removeMeetingFile")*/
    //  @FormUrlEncoded
    @HTTP(method = "DELETE", path = "removeMeetingFile", hasBody = true)
    Observable<BasicResponse> removeMeetingFile(@Body Map<String, Object> map);
    //  Observable<BasicResponse> removeMeetingFile(@Field("ids[]") List<String> idList, @Query("c_id") String id);
    //   Observable<BasicResponse> removeMeetingFile(@Field("ids[]") List<String> idList, @Path("c_id") String id);

    //查询中控设备类型列表
    @GET("findCceTypeList")
    Observable<BasicResponse<List<DeviceTypeBean.DataBean>>> getFindCceTypeList(@Query("c_id") String id);

    //根据设备类型ID查询中控设备列表
    @GET("findCceList")
    Observable<BasicResponse<List<DeviceListBean.DataBean>>> getFindCceList(@Query("c_id") String id, @Query("type_id") String type_id);

    //获取设备协议列表
    @GET("findCceCommandList")
    Observable<BasicResponse<List<AgreementBean.DataBean>>> getFindAgreementList(@Query("c_id") String id, @Query("cce_id") String type_id);

    //获取当前会议UUid
    @GET("proxy/findCcePlatformByMeetingRoom")
    Observable<BasicResponse<UUIdBean>> findUUid(@Query("meeting_room_id") String meeting_room_id, @Query("c_id") String c_id);

    //单个会议室信息
    @GET("findMeetingRoomInfo")
    Observable<BasicResponse> findMeetingRoom(@Query("id") String id);

    //获取摄像头信息
    @GET("cameraInfo")
    Observable<BasicResponse<CameraInfoBean>> getCameraInfo();

    //获取推流方式
    @GET("streamConfiguration")
    Observable<BasicResponse<StreamConfigurationBean>> getStreamConfiguration();

    //获取推流状态
    @GET("streamStatus")
    Observable<BasicResponse> getStreamStatus();

    //转动摄像头
    @GET("positionCall/{pos}")
    Observable<BasicResponse> setPositonCall(@Path("pos") String groupId);

}
