package com.example.paperlessmeeting_demo.network;


import com.example.paperlessmeeting_demo.bean.AgreementBean;
import com.example.paperlessmeeting_demo.bean.AttendeBean;
import com.example.paperlessmeeting_demo.bean.BasicResponse;
import com.example.paperlessmeeting_demo.bean.CameraInfoBean;
import com.example.paperlessmeeting_demo.bean.CreateFileBeanResponse;
import com.example.paperlessmeeting_demo.bean.DeviceListBean;
import com.example.paperlessmeeting_demo.bean.DeviceTypeBean;
import com.example.paperlessmeeting_demo.bean.LoginBean;
import com.example.paperlessmeeting_demo.bean.MeetingIdBean;
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
     *  ??????userId?????????????????????
     * */
    @POST("loginByUserId")
    @FormUrlEncoded
    Observable<BasicResponse<LoginBean>> loginByUserId(@FieldMap Map<String, Object> map);


    @POST("loginUser")
    @FormUrlEncoded
    Observable<BasicResponse<LoginBean>> login(@FieldMap Map<String, Object> map);

    //???????????????
    @POST("ppl/register")
    @FormUrlEncoded
    Observable<BasicResponse<String>> pplRegister(@FieldMap Map<String, Object> map);

    //???????????????????????????
    @GET("ppl/findPPLByMac")
    Observable<BasicResponse<String>> findPaperLessInfo(@Query("mac") String mac); // ??????????????????????????????

    //?????????????????????
    @PUT("meeting/finishMeeting")
    Observable<BasicResponse> finishWUHUMeeting(@Body Map<String, Object> map);

    //??????????????????
    @GET("findMeetingFileList")
    Observable<BasicResponse<NewFileBean>> getFileList(@Query("meeting_record_id") String meeting_record_id, @Query("type") String type);

    //
    @GET("meetingFile/getMeetingFile")
    Observable<BasicResponse<List<WuHuNetFileBean.DataBean>>> getWuHuFileList(@Query("meeting_id") String meeting_record_id);


    //??????????????????
    @GET("fqMeetingFile")
    Observable<BasicResponse<List<NewFileBean.MeetingFileListBean>>> getSearchFileList(@Query("key") String key, @Query("c_id") String c_id, @Query("meeting_record_id") String meeting_record_id);

    //????????????????????????
    @GET("findMeetingUserList")
    Observable<BasicResponse<List<AttendeBean>>> getMeetingUserList(@Query("meeting_record_id") String meeting_record_id);

    //??????????????????????????????
    @GET("fqMeetingUser")
    Observable<BasicResponse<List<AttendeBean>>> getSearchMeetingUser(@Query("key") String key);

    //  ????????????
    @GET("findMeetingVoteList")
    Observable<BasicResponse<ArrayList<VoteListBean.VoteBean>>> findMeetingVoteList(@Query("meeting_record_id") String meeting_record_id);

    //  ????????????
    @POST("createMeetingVote")
    Observable<BasicResponse<VoteListBean>> createMeetingVote(@Body Map<String, Object> map);

    //  ??????
    @POST("toVote")
    Observable<BasicResponse> toVote(@Body Map<String, Object> map);

    //  ????????????
    @POST("removeMeetingVote")
    Observable<BasicResponse> removeMeetingVote(@Body Map<String, Object> map);

    //  ????????????
    @POST("finishVote")
    Observable<BasicResponse> finishVote(@Body Map<String, Object> map);

    //  ????????????
    @PUT("updateMeetingVote")
    Observable<BasicResponse> updateMeetingVote(@Body Map<String, Object> map);

    //??????????????????
    @Multipart
    @POST("upload")
    Observable<BasicResponse<UploadBean>> upLoadFile(@Query("dirName") String dirName, @Query("uid") String uid, @Query("file") String f, @Part MultipartBody.Part file);

    //???????????????
    @POST("mergeChunk")
    Observable<BasicResponse<MergeChunkBean>> mergeChunk(@Body Map<String, Object> map);

    /*
     *  ?????????????????????
     * */
    //??????????????????
    @Multipart
    @POST("receiveChunk")
    Observable<BasicResponse> receiveChunk(@Part MultipartBody.Part file);


    /*   *//*
     *  ?????????????????????
     * *//*
    //??????????????????
    @Multipart
    @POST("receiveChunk")
    Observable<BasicResponse> receiveChunk( @Query("hash") String hash, @Query("fileName") String fileName);*/
    //????????????
    @POST("createMeetingFile")
    Observable<BasicResponse<CreateFileBeanResponse>> createMeetingFile(@Body Object fields);

    //????????????????????????
    @GET("meetingList")
    Observable<BasicResponse<List<WuHuMeetingListResponse>>> meetingList(@Query("name") String name, @Query("startTime") String startTime);

    //??????????????????
    @PUT("meeting")
    Observable<BasicResponse<MeetingIdBean>> meeting(@Body WuHuEditBeanRequset wuHuEditBeanRequset);

    //??????????????????
    @Streaming
    @POST("downloadFile")
    Observable<BasicResponse> downloadFile(@Query("uri") String uri, @Query("dest") String dest);

    //????????????????????????
    @GET("meetingInfo")
    Observable<BasicResponse<WuHuMeetingListResponse>> getWuHuMeetingInfo(@Query("id") String id);

    //??????????????????
    @GET("findMeetingRecordInfo")
    Observable<BasicResponse<MeetingInfoBean>> getMeetingInfo(@Query("id") String id);

    //????????????????????????
    @GET("findMeetingRecordList")
    Observable<BasicResponse<List<MeetingInfoBean>>> getMeetingListInfo();

    //??????????????????
    @PUT("signIn")
    @FormUrlEncoded
    Observable<BasicResponse> sign(@FieldMap Map<String, Object> map);

    //????????????
    @POST("delayMeeting")
    Observable<BasicResponse> delayMeeting(@Body Map<String, Object> map);

    //????????????
    @POST("finishMeetingRecord")
    Observable<BasicResponse> finishMeeting(@Body Map<String, Object> map);

    //??????????????????
    @GET("findMeetingUserInfo")
    Observable<BasicResponse<MeetingUserInfoBean>> findMeetingUserInfo(@Query("user_id") String user_id, @Query("meeting_record_id") String meeting_record_id);

    //???????????????????????????????????????
    @POST("updateUnclassified")
    Observable<BasicResponse> updateUnclassified(@Body Map<String, Object> map);

    //????????????

    /*  @FormUrlEncoded
      @DELETE("removeMeetingFile")*/
    //  @FormUrlEncoded
    @HTTP(method = "DELETE", path = "removeMeetingFile", hasBody = true)
    Observable<BasicResponse> removeMeetingFile(@Body Map<String, Object> map);
    //  Observable<BasicResponse> removeMeetingFile(@Field("ids[]") List<String> idList, @Query("c_id") String id);
    //   Observable<BasicResponse> removeMeetingFile(@Field("ids[]") List<String> idList, @Path("c_id") String id);

    //??????????????????????????????
    @GET("findCceTypeList")
    Observable<BasicResponse<List<DeviceTypeBean.DataBean>>> getFindCceTypeList(@Query("c_id") String id);

    //??????????????????ID????????????????????????
    @GET("findCceList")
    Observable<BasicResponse<List<DeviceListBean.DataBean>>> getFindCceList(@Query("c_id") String id, @Query("type_id") String type_id);

    //????????????????????????
    @GET("findCceCommandList")
    Observable<BasicResponse<List<AgreementBean.DataBean>>> getFindAgreementList(@Query("c_id") String id, @Query("cce_id") String type_id);

    //??????????????????UUid
    @GET("proxy/findCcePlatformByMeetingRoom")
    Observable<BasicResponse<UUIdBean>> findUUid(@Query("meeting_room_id") String meeting_room_id, @Query("c_id") String c_id);

    //?????????????????????
    @GET("findMeetingRoomInfo")
    Observable<BasicResponse> findMeetingRoom(@Query("id") String id);

    //?????????????????????
    @GET("cameraInfo")
    Observable<BasicResponse<CameraInfoBean>> getCameraInfo();

    //??????????????????
    @GET("streamConfiguration")
    Observable<BasicResponse<StreamConfigurationBean>> getStreamConfiguration();

    //??????????????????
    @GET("streamStatus")
    Observable<BasicResponse> getStreamStatus();

    //???????????????
    @GET("positionCall/{pos}")
    Observable<BasicResponse> setPositonCall(@Path("pos") String groupId);

    //????????????APP???????????????
    @POST("device")
    Observable<BasicResponse> verificationEquipment(@Body Map<String, Object> map);

}
