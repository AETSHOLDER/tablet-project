package com.example.paperlessmeeting_demo.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by 梅涛 on 2020/11/9.
 */

public class MeetingInfoBean implements Serializable {


    /**
     * code : 0
     * msg : 成功！
     * data : {"meeting_room_id":[{"_id":"606612bcb32fd612940ba25d","status":"ENABLE","c_id":"5f67708fbd0c1f2d906dad5e","name":"中协无纸化有发言单元会议室","address":"南京市雨花台区科创园蓝莓8栋101","seating":"100","realisticImg":"meetingRoom/会议室画板 5.jpg","guideMap":"meetingRoom/会议室画板 5.jpg","layoutImg":"","layoutData":"[{\"width\":\"447\",\"height\":\"227\",\"type\":\"desk\",\"borderRadius\":\"20px\",\"border\":\"1px solid #607dd5\",\"left\":\"543\",\"top\":\"126\",\"id\":0,\"lt\":{\"x\":-4,\"y\":-4},\"rb\":{\"x\":-4,\"y\":-4}},{\"type\":\"mic\",\"name\":\"发言单元\",\"width\":\"29\",\"height\":\"22\",\"src\":\"url(data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAB0AAAAWCAYAAAA8VJfMAAAABHNCSVQICAgIfAhkiAAAAYZJREFUSEu1lbFNA0EQRf9mZODYiZFcABINmAqgAyB2MoEDMqACPoFTAx3gCjAVAHJqCSgAya5g0Uez6ITuzj7fMtLKOuk8b2b2z7+Afwoz6wE4BaBfnRTXIQfTzAYhhOi5ejFGnYsQwk5J/ulWUDM7A3AMYABgr2HhzaBmdgDgxmFF1nPh4QOAziDGeBhC+PJnvfIKgBt36sAXT/4J4ArAjKQAjWIjqAOffJQPAIzkshGp8PJaqJnpztShFPhAUvfZKjaBaoyXAN50T206TJXWQr3Ldx/rEclZqxb9z+ug977gU5InOYDKsQ4qsewC2N9GpVVFVkLdAO50lyS1n9miDvrornNOUmPOFnXQ5KWdHIotVlwKlYEDkBlkH22lkMws7eYtScs217QyvosSTLaVACA9SAulVhnMLAkmd0OVlinoj2BGoxG63W5r8GKxwHg8Vp4lyU5Zwl/ocDhEv99vDZ3P55hMJsqzIln6gRc0WV1r4J8EteNVNXQhyfLaxsqFVPnN/Qa994gXdTAQqAAAAABJRU5ErkJggg==)\",\"cursor\":\"pointer\",\"showchild\":false,\"hidden\":true,\"left\":752,\"top\":122,\"id\":1,\"mic\":{\"_id\":\"606641132d347c46240107d1\",\"status\":\"APPLY\",\"c_id\":\"5f67708fbd0c1f2d906dad5e\",\"name\":\"\",\"mac\":\"149b2f42004a\",\"number\":\"1\",\"group\":\"2\",\"ip\":\"192.168.1.74\",\"subnet_mask\":\"\",\"gateway\":\"\",\"mainframe_id\":\"\",\"meeting_room_id\":\"\",\"create_time\":\"Fri Apr 02 2021 05:54:27 GMT+0800 (GMT+08:00)\",\"pap_id\":\"\",\"place\":\"N\",\"rostrum\":\"1\",\"bind\":false},\"lt\":{\"x\":-4,\"y\":-4},\"rb\":{\"x\":-4,\"y\":-4}},{\"type\":\"mic\",\"name\":\"发言单元\",\"width\":\"29\",\"height\":\"22\",\"src\":\"url(data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAB0AAAAWCAYAAAA8VJfMAAAABHNCSVQICAgIfAhkiAAAAYZJREFUSEu1lbFNA0EQRf9mZODYiZFcABINmAqgAyB2MoEDMqACPoFTAx3gCjAVAHJqCSgAya5g0Uez6ITuzj7fMtLKOuk8b2b2z7+Afwoz6wE4BaBfnRTXIQfTzAYhhOi5ejFGnYsQwk5J/ulWUDM7A3AMYABgr2HhzaBmdgDgxmFF1nPh4QOAziDGeBhC+PJnvfIKgBt36sAXT/4J4ArAjKQAjWIjqAOffJQPAIzkshGp8PJaqJnpztShFPhAUvfZKjaBaoyXAN50T206TJXWQr3Ldx/rEclZqxb9z+ug977gU5InOYDKsQ4qsewC2N9GpVVFVkLdAO50lyS1n9miDvrornNOUmPOFnXQ5KWdHIotVlwKlYEDkBlkH22lkMws7eYtScs217QyvosSTLaVACA9SAulVhnMLAkmd0OVlinoj2BGoxG63W5r8GKxwHg8Vp4lyU5Zwl/ocDhEv99vDZ3P55hMJsqzIln6gRc0WV1r4J8EteNVNXQhyfLaxsqFVPnN/Qa994gXdTAQqAAAAABJRU5ErkJggg==)\",\"cursor\":\"pointer\",\"showchild\":false,\"hidden\":true,\"left\":753,\"top\":324,\"id\":2,\"mic\":{\"_id\":\"606641132d347c46240107d3\",\"status\":\"APPLY\",\"c_id\":\"5f67708fbd0c1f2d906dad5e\",\"name\":\"\",\"mac\":\"149b2f1a0031\",\"number\":\"2\",\"group\":\"2\",\"ip\":\"192.168.1.49\",\"subnet_mask\":\"\",\"gateway\":\"\",\"mainframe_id\":\"\",\"meeting_room_id\":\"\",\"create_time\":\"Fri Apr 02 2021 05:54:27 GMT+0800 (GMT+08:00)\",\"pap_id\":\"\",\"place\":\"N\",\"rostrum\":\"0\",\"bind\":false},\"lt\":{\"x\":-4,\"y\":-4},\"rb\":{\"x\":-4,\"y\":-4}},{\"type\":\"mic\",\"name\":\"发言单元\",\"width\":\"29\",\"height\":\"22\",\"src\":\"url(data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAB0AAAAWCAYAAAA8VJfMAAAABHNCSVQICAgIfAhkiAAAAYZJREFUSEu1lbFNA0EQRf9mZODYiZFcABINmAqgAyB2MoEDMqACPoFTAx3gCjAVAHJqCSgAya5g0Uez6ITuzj7fMtLKOuk8b2b2z7+Afwoz6wE4BaBfnRTXIQfTzAYhhOi5ejFGnYsQwk5J/ulWUDM7A3AMYABgr2HhzaBmdgDgxmFF1nPh4QOAziDGeBhC+PJnvfIKgBt36sAXT/4J4ArAjKQAjWIjqAOffJQPAIzkshGp8PJaqJnpztShFPhAUvfZKjaBaoyXAN50T206TJXWQr3Ldx/rEclZqxb9z+ug977gU5InOYDKsQ4qsewC2N9GpVVFVkLdAO50lyS1n9miDvrornNOUmPOFnXQ5KWdHIotVlwKlYEDkBlkH22lkMws7eYtScs217QyvosSTLaVACA9SAulVhnMLAkmd0OVlinoj2BGoxG63W5r8GKxwHg8Vp4lyU5Zwl/ocDhEv99vDZ3P55hMJsqzIln6gRc0WV1r4J8EteNVNXQhyfLaxsqFVPnN/Qa994gXdTAQqAAAAABJRU5ErkJggg==)\",\"cursor\":\"pointer\",\"showchild\":false,\"hidden\":true,\"left\":542,\"top\":234,\"id\":3,\"mic\":{\"_i:\"APPLY\",\"c_id\":\"5f67708fbd0c1f2d906dad5e\",\"name\":\"\",\"mac\":\"149b2f46001f\",\"number\":\"4\",\"group\":\"1\",\"ip\":\"192.168.1.31\",\"subnet_mask\":\"\",\"gateway\":\"\",\"mainframe_id\":\"\",\"meeting_room_id\":\"\",\"create_time\":\"Fri Apr 02 2021 08:11:52 GMT+0800 (GMT+08:00)\",\"pap_id\":\"\",\"place\":\"N\",\"rostrum\":\"0\",\"bind\":false},\"lt\":{\"x\":-4,\"y\":-4},\"rb\":{\"x\":-4,\"y\":-4}},{\"type\":\"mic\",\"name\":\"发言单元\",\"width\":\"29\",\"height\":\"22\",\"src\":\"url(data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAB0AAAAWCAYAAAA8VJfMAAAABHNCSVQICAgIfAhkiAAAAYZJREFUSEu1lbFNA0EQRf9mZODYiZFcABINmAqgAyB2MoEDMqACPoFTAx3gCjAVAHJqCSgAya5g0Uez6ITuzj7fMtLKOuk8b2b2z7+Afwoz6wE4BaBfnRTXIQfTzAYhhOi5ejFGnYsQwk5J/ulWUDM7A3AMYABgr2HhzaBmdgDgxmFF1nPh4QOAziDGeBhC+PJnvfIKgBt36sAXT/4J4ArAjKQAjWIjqAOffJQPAIzkshGp8PJaqJnpztShFPhAUvfZKjaBaoyXAN50T206TJXWQr3Ldx/rEclZqxb9z+ug977gU5InOYDKsQ4qsewC2N9GpVVFVkLdAO50lyS1n9miDvrornNOUmPOFnXQ5KWdHIotVlwKlYEDkBlkH22lkMws7eYtScs217QyvosSTLaVACA9SAulVhnMLAkmd0OVlinoj2BGoxG63W5r8GKxwHg8Vp4lyU5Zwl/ocDhEv99vDZ3P55hMJsqzIln6gRc0WV1r4J8EteNVNXQhyfLaxsqFVPnN/Qa994gXdTAQqAAAAABJRU5ErkJggg==)\",\"cursor\":\"pointer\",\"showchild\":false,\"hidden\":true,\"left\":962,\"top\":235,\"id\":4,\"mic\":{\"_id\":\"6075618130bdc0401826ab06\",\"status\":\"APPLY\",\"c_id\":\"5f67708fbd0c1f2d906dad5e\",\"name\":\"\",\"mac\":\"149b2f2c0039\",\"number\":\"1\",\"group\":\"1\",\"ip\":\"192.168.1.57\",\"subnet_mask\":\"\",\"gateway\":\"\",\"mainframe_id\":\"\",\"meeting_room_id\":\"\",\"create_time\":\"Tue Apr 13 2021 17:16:49 GMT+0800 (GMT+08:00)\",\"pap_id\":\"\",\"place\":\"N\",\"rostrum\":\"0\",\"bind\":false},\"lt\":{\"x\":-4,\"y\":-4},\"rb\":{\"x\":-4,\"y\":-4}},{\"type\":\"chair\",\"name\":\"椅子\",\"width\":\"30\",\"height\":\"24\",\"src\":\"url(data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAB4AAAAYCAYAAADtaU2/AAAABHNCSVQICAgIfAhkiAAAAQFJREFUSEtjLCgokPz//38/IyNjOAMdwP///48wMjKGMebn5x8WFRU1MDY25oHZe+fOHYa7d+8yuLu7U+SUnTt3MigrKzOoqKjAzbl69eqHx48f72QsKCj4D7LAw8MDLrljxw4GkKb+/n6KLC4sLAQ7HpvZg9NiZJeS43VQyJHlY3IsQ9dDlsUjL45HfUxqYht6+Xg0jkfjmFAIDL1UTchHxMiTVUkwMDA0EmM4HjX1ZFk8YcIERkosxte6wdsCoanF+fn5R+Xk5LS0tbUFYL47e/bsl9evX1+cMGGCDSU+xmc2yMeg5u1ERkbGUJgl////v8LCwuLY29v7hhKL8ZkNAMqNnDNh7OH0AAAAAElFTkSuQmCC)\",\"cursor\":\"pointer\",\"hidden\":true,\"left\":462,\"top\":233,\"id\":5,\"lt\":{\"x\":-4,\"y\":-4},\"rb\":{\"x\":-4,\"y\":-4},\"mic\":{\"_id\":\"606641132d347c46240107d1\",\"status\":\"APPLY\",\"c_id\":\"5f67708fbd0c1f2d906dad5e\",\"name\":\"\",\"mac\":\"149b2f42004a\",\"number\":\"1\",\"group\":\"2\",\"ip\":\"192.168.1.74\",\"subnet_mask\":\"\",\"gateway\":\"\",\"mainframe_id\":\"\",\"meeting_room_id\":\"\",\"create_time\":\"Fri Apr 02 2021 05:54:27 GMT+0800 (GMT+08:00)\",\"pap_id\":\"\",\"place\":\"N\",\"rostrum\":\"1\",\"bind\":true}},{\"type\":\"chair\",\"name\":\"椅子\",\"width\":\"30\",\"height\":\"24\",\"src\":\"url(data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAB4AAAAYCAYAAADtaU2/AAAABHNCSVQICAgIfAhkiAAAAQFJREFUSEtjLCgokPz//38/IyNjOAMdwP///48wMjKGMebn5x8WFRU1MDY25oHZe+fOHYa7d+8yuLu7U+SUnTt3MigrKzOoqKjAzbl69eqHx48f72QsKCj4D7LAw8MDLrljxw4GkKb+/n6KLC4sLAQ7HpvZg9NiZJeS43VQyJHlY3IsQ9dDlsUjL45HfUxqYht6+Xg0jkfjmFAIDL1UTchHxMiTVUkwMDA0EmM4HjX1ZFk8YcIERkosxte6wdsCoanF+fn5R+Xk5LS0tbUFYL47e/bsl9evX1+cMGGCDSU+xmc2yMeg5u1ERkbGUJgl////v8LCwuLY29v7hhKL8ZkNAMqNnDNh7OH0AAAAAElFTkSuQmCC)\",\"cursor\":\"pointer\",\"hidden\":true,\"left\":742,\"top\":60,\"id\":6,\"lt\":{\"x\":-4,\"y\":-4},\"rb\":{\"x\":-4,\"y\":-4},\"mic\":{\"_id\":\"6075618130bdc0401826ab06\",\"status\":\"APPLY\",\"c_id\":\"5f67708fbd0c1f2d906dad5e\",\"name\":\"\",\"mac\":\"149b2f2c0039\",\"number\":\"1\",\"group\":\"1\",\"ip\":\"192.168.1.57\",\"subnet_mask\":\"\",\"gateway\":\"\",\"mainframe_id\":\"\",\"meeting_room_id\":\"\",\"create_time\":\"Tue Apr 13 2021 17:16:49 GMT+0800 (GMT+08:00)\",\"pap_id\":\"\",\"place\":\"N\",\"rostrum\":\"0\",\"bind\":true}},{\"type\":\"chair\",\"name\":\"椅子\",\"width\":\"30\",\"height\":\"24\",\"src\":\"url(data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAB4AAAAYCAYAAADtaU2/AAAABHNCSVQICAgIfAhkiAAAAQFJREFUSEtjLCgokPz//38/IyNjOAMdwP///48wMjKGMebn5x8WFRU1MDY25oHZe+fOHYa7d+8yuLu7U+SUnTt3MigrKzOoqKjAzbl69eqHx48f72QsKCj4D7LAw8MDLrljxw4GkKb+/n6KLC4sLAQ7HpvZg9NiZJeS43VQyJHlY3IsQ9dDlsUjL45HfUxqYht6+Xg0jkfjmFAIDL1UTchHxMiTVUkwMDA0EmM4HjX1ZFk8YcIERkosxte6wdsCoanF+fn5R+Xk5LS0tbUFYL47e/bsl9evX1+cMGGCDSU+xmc2yMeg5u1ERkbGUJgl////v8LCwuLY29v7hhKL8ZkNAMqNnDNh7OH0AAAAAElFTkSuQmCC)\",\"cursor\":\"pointer\",\"hidden\":true,\"left\":1043,\"top\":236,\"id\":7,\"lt\":{\"x\":-4,\"y\":-4},\"rb\":{\"x\":-4,\"y\":-4},\"mic\":{\"_id\":\"60666d77d3e76e3c10913a22\",\"status\":\"APPLY\",\"c_id\":\"5f67708fbd0c1f2d906dad5e\",\"name\":\"\",\"mac\":\"149b2f46001f\",\"number\":\"4\",\"group\":\"1\",\"ip\":\"192.168.1.31\",\"subnet_mask\":\"\",\"gateway\":\"\",\"mainframe_id\":\"\",\"meeting_room_id\":\"\",\"create_time\":\"Fri Apr 02 2021 08:11:52 GMT+0800 (GMT+08:00)\",\"pap_id\":\"\",\"place\":\"N\",\"rostrum\":\"0\",\"bind\":true}},{\"type\":\"chair\",\"name\":\"椅子\",\"width\":\"30\",\"height\":\"24\",\"src\":\"url(data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAB4AAAAYCAYAAADtaU2/AAAABHNCSVQICAgIfAhkiAAAAQFJREFUSEtjLCgokPz//38/IyNjOAMdwP///48wMjKGMebn5x8WFRU1MDY25oHZe+fOHYa7d+8yuLu7U+SUnTt3MigrKzOoqKjAzbl69eqHx48f72QsKCj4D7LAw8MDLrljxw4GkKb+/n6KLC4sLAQ7HpvZg9NiZJeS43VQyJHlY3IsQ9dDlsUjL45HfUxqYht6+Xg0jkfjmFAIDL1UTchHxMiTVUkwMDA0EmM4HjX1ZFk8YcIERkosxte6wdsCoanF+fn5R+Xk5LS0tbUFYL47e/bsl9evX1+cMGGCDSU+xmc2yMeg5u1ERkbGUJgl////v8LCwuLY29v7hhKL8ZkNAMqNnDNh7OH0AAAAAElFTkSuQmCC)\",\"cursor\":\"pointer\",\"hidden\":true,\"left\":754,\"top\":384,\"id\":8,\"lt\":{\"x\":-4,\"y\":-4},\"rb\":{\"x\":-4,\"y\":-4},\"mic\":{\"_id\":\"606641132d347c46240107d3\",\"status\":\"APPLY\",\"c_id\":\"5f67708fbd0c1f2d906dad5e\",\"name\":\"\",\"mac\":\"149b2f1a0031\",\"number\":\"2\",\"group\":\"2\",\"ip\":\"192.168.1.49\",\"subnet_mask\":\"\",\"gateway\":\"\",\"mainframe_id\":\"\",\"meeting_room_id\":\"\",\"create_time\":\"Fri Apr 02 2021 05:54:27 GMT+0800 (GMT+08:00)\",\"pap_id\":\"\",\"place\":\"N\",\"rostrum\":\"0\",\"bind\":true}}]","check":"0","is_pap_eq":"0","is_mic":"0","type":"ppl0","note":"中协无纸化有发言单元会议室","designated_group":"10"}],"agenda":[""],"serveItem":[],"user_list":[{"_id":"611085c9acda332c882a36a4","status":"UNCONFIRMED","c_id":"5f67708fbd0c1f2d906dad5e","meeting_record_id":"611085c9acda332c882a36a3","user_id":"5f67767ebd0c1f2d906dad66","name":"江小鱼","avatar":"/upload/e47b5486294f7601.png","phone":"15212718899","role":"0","device":{"type":"","mac":"c400ad789d9","number":"1"},"sign_time":"","chair_id":"","sign_code":"2120","note":"OUT","dept_id":"5f6773f7bd0c1f2d906dad61"},{"_id":"611085c9acda332c882a36a6","status":"UNCONFIRMED","c_id":"5f67708fbd0c1f2d906dad5e","meeting_record_id":"611085c9acda332c882a36a3","user_id":"5f677640bd0c1f2d906dad64","name":"花满楼","avatar":"/upload/482a086f9abcc2a1.png","phone":"12365454111","role":"2","device":{"type":"","mac":"c400ad789d8","number":"2"},"sign_time":"Mon Aug 09 2021 09:59:34 GMT+0800 (GMT+08:00)","chair_id":"","sign_code":"6040","note":"IN","dept_id":"5f6773f7bd0c1f2d906dad61"},{"_id":"611085c9acda332c882a36a8","status":"UNCONFIRMED","c_id":"5f67708fbd0c1f2d906dad5e","meeting_record_id":"611085c9acda332c882a36a3","user_id":"5f67766bbd0c1f2d906dad65","name":"令狐聪","avatar":"/upload/b8be7b9bfdfb9bc0.png","phone":"13637228866","role":"2","device":null,"sign_time":"","chair_id":"","sign_code":"7744","note":"OUT","dept_id":"5f6773f7bd0c1f2d906dad61"},{"_id":"611085c9acda332c882a36a9","status":"UNCONFIRMED","c_id":"5f67708fbd0c1f2d906dad5e","meeting_record_id":"611085c9acda332c882a36a3","user_id":"60812f6320d26f47ac6d3396","name":"谢逊","avatar":"/upload/default.jpg","phone":"18196740126","role":"2","device":null,"sign_time":"","chair_id":"","sign_code":"1647","note":"OUT","dept_id":"5f6773f7bd0c1f2d906dad61"},{"_id":"611085c9acda332c882a36aa","status":"UNCONFIRMED","c_id":"5f67708fbd0c1f2d906dad5e","meeting_record_id":"611085c9acda332c882a36a3","user_id":"60efb36ec39a2e1eb895cf9e","name":"王大佬","avatar":"/upload/472e2fe7ec6b9f0e.png","phone":"15855555555","role":"2","device":null,"sign_time":"","chair_id":"","sign_code":"7422","note":"OUT","dept_id":"5f6773f7bd0c1f2d906dad61"},{"_id":"611085c9acda332c882a36ab","status":"UNCONFIRMED","c_id":"5f67708fbd0c1f2d906dad5e","meeting_record_id":"611085c9acda332c882a36a3","user_id":"6065f471d80db139c887d512","name":"花无缺","avatar":"/upload/230471e8dd843e49.png","phone":"19841810520","role":"2","device":null,"sign_time":"Mon Aug 09 2021 09:51:27 GMT+0800 (GMT+08:00)","chair_id":"","sign_code":"9529","note":"OUT","dept_id":"5f6773f7bd0c1f2d906dad61"},{"_id":"611085c9acda332c882a36ac","status":"UNCONFIRMED","c_id":"5f67708fbd0c1f2d906dad5e","meeting_record_id":"611085c9acda332c882a36a3","user_id":"5f677602bd0c1f2d906dad63","name":"东方不败","avatar":"/upload/5b9db62e5c5a71b9.png","phone":"12365454111","role":"2","device":null,"sign_time":"","chair_id":"","sign_code":"4874","note":"OUT","dept_id":"5f6773f7bd0c1f2d906dad61"}],"_id":"611085c9acda332c882a36a3","status":"UNDERWAY","c_id":"5f67708fbd0c1f2d906dad5e","type":"Z","name":"开发部会议","purpose":"","remind":"15","sche_start_time":"2021-08-09 09:29:00","sche_end_time":"2021-08-09 13:32:00","actual_start_time":"2021-08-09 09:32:58","actual_end_time":"2021-08-09 13:32:00","from":{"_id":"5f67767ebd0c1f2d906dad66","name":"江小鱼","role":"M","avatar":"upload/e47b5486294f7601.png","phone":"15212718899","dept_id":"5f6773f7bd0c1f2d906dad61","note":{"mac":"","number":"","type":""}},"layout_data":"","check":"0","check_status":"ENABLE","failure_cause":"","failure_time":"","create_time":"2021-08-09 09:19:44","receiving_status":"","nameplate_type":"0","background_img":""}
     */

    /**
     * meeting_room_id : [{"_id":"606612bcb32fd612940ba25d","status":"ENABLE","c_id":"5f67708fbd0c1f2d906dad5e","name":"中协无纸化有发言单元会议室","address":"南京市雨花台区科创园蓝莓8栋101","seating":"100","realisticImg":"meetingRoom/会议室画板 5.jpg","guideMap":"meetingRoom/会议室画板 5.jpg","layoutImg":"","layoutData":"[{\"width\":\"447\",\"height\":\"227\",\"type\":\"desk\",\"borderRadius\":\"20px\",\"border\":\"1px solid #607dd5\",\"left\":\"543\",\"top\":\"126\",\"id\":0,\"lt\":{\"x\":-4,\"y\":-4},\"rb\":{\"x\":-4,\"y\":-4}},{\"type\":\"mic\",\"name\":\"发言单元\",\"width\":\"29\",\"height\":\"22\",\"src\":\"url(data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAB0AAAAWCAYAAAA8VJfMAAAABHNCSVQICAgIfAhkiAAAAYZJREFUSEu1lbFNA0EQRf9mZODYiZFcABINmAqgAyB2MoEDMqACPoFTAx3gCjAVAHJqCSgAya5g0Uez6ITuzj7fMtLKOuk8b2b2z7+Afwoz6wE4BaBfnRTXIQfTzAYhhOi5ejFGnYsQwk5J/ulWUDM7A3AMYABgr2HhzaBmdgDgxmFF1nPh4QOAziDGeBhC+PJnvfIKgBt36sAXT/4J4ArAjKQAjWIjqAOffJQPAIzkshGp8PJaqJnpztShFPhAUvfZKjaBaoyXAN50T206TJXWQr3Ldx/rEclZqxb9z+ug977gU5InOYDKsQ4qsewC2N9GpVVFVkLdAO50lyS1n9miDvrornNOUmPOFnXQ5KWdHIotVlwKlYEDkBlkH22lkMws7eYtScs217QyvosSTLaVACA9SAulVhnMLAkmd0OVlinoj2BGoxG63W5r8GKxwHg8Vp4lyU5Zwl/ocDhEv99vDZ3P55hMJsqzIln6gRc0WV1r4J8EteNVNXQhyfLaxsqFVPnN/Qa994gXdTAQqAAAAABJRU5ErkJggg==)\",\"cursor\":\"pointer\",\"showchild\":false,\"hidden\":true,\"left\":752,\"top\":122,\"id\":1,\"mic\":{\"_id\":\"606641132d347c46240107d1\",\"status\":\"APPLY\",\"c_id\":\"5f67708fbd0c1f2d906dad5e\",\"name\":\"\",\"mac\":\"149b2f42004a\",\"number\":\"1\",\"group\":\"2\",\"ip\":\"192.168.1.74\",\"subnet_mask\":\"\",\"gateway\":\"\",\"mainframe_id\":\"\",\"meeting_room_id\":\"\",\"create_time\":\"Fri Apr 02 2021 05:54:27 GMT+0800 (GMT+08:00)\",\"pap_id\":\"\",\"place\":\"N\",\"rostrum\":\"1\",\"bind\":false},\"lt\":{\"x\":-4,\"y\":-4},\"rb\":{\"x\":-4,\"y\":-4}},{\"type\":\"mic\",\"name\":\"发言单元\",\"width\":\"29\",\"height\":\"22\",\"src\":\"url(data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAB0AAAAWCAYAAAA8VJfMAAAABHNCSVQICAgIfAhkiAAAAYZJREFUSEu1lbFNA0EQRf9mZODYiZFcABINmAqgAyB2MoEDMqACPoFTAx3gCjAVAHJqCSgAya5g0Uez6ITuzj7fMtLKOuk8b2b2z7+Afwoz6wE4BaBfnRTXIQfTzAYhhOi5ejFGnYsQwk5J/ulWUDM7A3AMYABgr2HhzaBmdgDgxmFF1nPh4QOAziDGeBhC+PJnvfIKgBt36sAXT/4J4ArAjKQAjWIjqAOffJQPAIzkshGp8PJaqJnpztShFPhAUvfZKjaBaoyXAN50T206TJXWQr3Ldx/rEclZqxb9z+ug977gU5InOYDKsQ4qsewC2N9GpVVFVkLdAO50lyS1n9miDvrornNOUmPOFnXQ5KWdHIotVlwKlYEDkBlkH22lkMws7eYtScs217QyvosSTLaVACA9SAulVhnMLAkmd0OVlinoj2BGoxG63W5r8GKxwHg8Vp4lyU5Zwl/ocDhEv99vDZ3P55hMJsqzIln6gRc0WV1r4J8EteNVNXQhyfLaxsqFVPnN/Qa994gXdTAQqAAAAABJRU5ErkJggg==)\",\"cursor\":\"pointer\",\"showchild\":false,\"hidden\":true,\"left\":753,\"top\":324,\"id\":2,\"mic\":{\"_id\":\"606641132d347c46240107d3\",\"status\":\"APPLY\",\"c_id\":\"5f67708fbd0c1f2d906dad5e\",\"name\":\"\",\"mac\":\"149b2f1a0031\",\"number\":\"2\",\"group\":\"2\",\"ip\":\"192.168.1.49\",\"subnet_mask\":\"\",\"gateway\":\"\",\"mainframe_id\":\"\",\"meeting_room_id\":\"\",\"create_time\":\"Fri Apr 02 2021 05:54:27 GMT+0800 (GMT+08:00)\",\"pap_id\":\"\",\"place\":\"N\",\"rostrum\":\"0\",\"bind\":false},\"lt\":{\"x\":-4,\"y\":-4},\"rb\":{\"x\":-4,\"y\":-4}},{\"type\":\"mic\",\"name\":\"发言单元\",\"width\":\"29\",\"height\":\"22\",\"src\":\"url(data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAB0AAAAWCAYAAAA8VJfMAAAABHNCSVQICAgIfAhkiAAAAYZJREFUSEu1lbFNA0EQRf9mZODYiZFcABINmAqgAyB2MoEDMqACPoFTAx3gCjAVAHJqCSgAya5g0Uez6ITuzj7fMtLKOuk8b2b2z7+Afwoz6wE4BaBfnRTXIQfTzAYhhOi5ejFGnYsQwk5J/ulWUDM7A3AMYABgr2HhzaBmdgDgxmFF1nPh4QOAziDGeBhC+PJnvfIKgBt36sAXT/4J4ArAjKQAjWIjqAOffJQPAIzkshGp8PJaqJnpztShFPhAUvfZKjaBaoyXAN50T206TJXWQr3Ldx/rEclZqxb9z+ug977gU5InOYDKsQ4qsewC2N9GpVVFVkLdAO50lyS1n9miDvrornNOUmPOFnXQ5KWdHIotVlwKlYEDkBlkH22lkMws7eYtScs217QyvosSTLaVACA9SAulVhnMLAkmd0OVlinoj2BGoxG63W5r8GKxwHg8Vp4lyU5Zwl/ocDhEv99vDZ3P55hMJsqzIln6gRc0WV1r4J8EteNVNXQhyfLaxsqFVPnN/Qa994gXdTAQqAAAAABJRU5ErkJggg==)\",\"cursor\":\"pointer\",\"showchild\":false,\"hidden\":true,\"left\":542,\"top\":234,\"id\":3,\"mic\":{\"_i:\"APPLY\",\"c_id\":\"5f67708fbd0c1f2d906dad5e\",\"name\":\"\",\"mac\":\"149b2f46001f\",\"number\":\"4\",\"group\":\"1\",\"ip\":\"192.168.1.31\",\"subnet_mask\":\"\",\"gateway\":\"\",\"mainframe_id\":\"\",\"meeting_room_id\":\"\",\"create_time\":\"Fri Apr 02 2021 08:11:52 GMT+0800 (GMT+08:00)\",\"pap_id\":\"\",\"place\":\"N\",\"rostrum\":\"0\",\"bind\":false},\"lt\":{\"x\":-4,\"y\":-4},\"rb\":{\"x\":-4,\"y\":-4}},{\"type\":\"mic\",\"name\":\"发言单元\",\"width\":\"29\",\"height\":\"22\",\"src\":\"url(data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAB0AAAAWCAYAAAA8VJfMAAAABHNCSVQICAgIfAhkiAAAAYZJREFUSEu1lbFNA0EQRf9mZODYiZFcABINmAqgAyB2MoEDMqACPoFTAx3gCjAVAHJqCSgAya5g0Uez6ITuzj7fMtLKOuk8b2b2z7+Afwoz6wE4BaBfnRTXIQfTzAYhhOi5ejFGnYsQwk5J/ulWUDM7A3AMYABgr2HhzaBmdgDgxmFF1nPh4QOAziDGeBhC+PJnvfIKgBt36sAXT/4J4ArAjKQAjWIjqAOffJQPAIzkshGp8PJaqJnpztShFPhAUvfZKjaBaoyXAN50T206TJXWQr3Ldx/rEclZqxb9z+ug977gU5InOYDKsQ4qsewC2N9GpVVFVkLdAO50lyS1n9miDvrornNOUmPOFnXQ5KWdHIotVlwKlYEDkBlkH22lkMws7eYtScs217QyvosSTLaVACA9SAulVhnMLAkmd0OVlinoj2BGoxG63W5r8GKxwHg8Vp4lyU5Zwl/ocDhEv99vDZ3P55hMJsqzIln6gRc0WV1r4J8EteNVNXQhyfLaxsqFVPnN/Qa994gXdTAQqAAAAABJRU5ErkJggg==)\",\"cursor\":\"pointer\",\"showchild\":false,\"hidden\":true,\"left\":962,\"top\":235,\"id\":4,\"mic\":{\"_id\":\"6075618130bdc0401826ab06\",\"status\":\"APPLY\",\"c_id\":\"5f67708fbd0c1f2d906dad5e\",\"name\":\"\",\"mac\":\"149b2f2c0039\",\"number\":\"1\",\"group\":\"1\",\"ip\":\"192.168.1.57\",\"subnet_mask\":\"\",\"gateway\":\"\",\"mainframe_id\":\"\",\"meeting_room_id\":\"\",\"create_time\":\"Tue Apr 13 2021 17:16:49 GMT+0800 (GMT+08:00)\",\"pap_id\":\"\",\"place\":\"N\",\"rostrum\":\"0\",\"bind\":false},\"lt\":{\"x\":-4,\"y\":-4},\"rb\":{\"x\":-4,\"y\":-4}},{\"type\":\"chair\",\"name\":\"椅子\",\"width\":\"30\",\"height\":\"24\",\"src\":\"url(data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAB4AAAAYCAYAAADtaU2/AAAABHNCSVQICAgIfAhkiAAAAQFJREFUSEtjLCgokPz//38/IyNjOAMdwP///48wMjKGMebn5x8WFRU1MDY25oHZe+fOHYa7d+8yuLu7U+SUnTt3MigrKzOoqKjAzbl69eqHx48f72QsKCj4D7LAw8MDLrljxw4GkKb+/n6KLC4sLAQ7HpvZg9NiZJeS43VQyJHlY3IsQ9dDlsUjL45HfUxqYht6+Xg0jkfjmFAIDL1UTchHxMiTVUkwMDA0EmM4HjX1ZFk8YcIERkosxte6wdsCoanF+fn5R+Xk5LS0tbUFYL47e/bsl9evX1+cMGGCDSU+xmc2yMeg5u1ERkbGUJgl////v8LCwuLY29v7hhKL8ZkNAMqNnDNh7OH0AAAAAElFTkSuQmCC)\",\"cursor\":\"pointer\",\"hidden\":true,\"left\":462,\"top\":233,\"id\":5,\"lt\":{\"x\":-4,\"y\":-4},\"rb\":{\"x\":-4,\"y\":-4},\"mic\":{\"_id\":\"606641132d347c46240107d1\",\"status\":\"APPLY\",\"c_id\":\"5f67708fbd0c1f2d906dad5e\",\"name\":\"\",\"mac\":\"149b2f42004a\",\"number\":\"1\",\"group\":\"2\",\"ip\":\"192.168.1.74\",\"subnet_mask\":\"\",\"gateway\":\"\",\"mainframe_id\":\"\",\"meeting_room_id\":\"\",\"create_time\":\"Fri Apr 02 2021 05:54:27 GMT+0800 (GMT+08:00)\",\"pap_id\":\"\",\"place\":\"N\",\"rostrum\":\"1\",\"bind\":true}},{\"type\":\"chair\",\"name\":\"椅子\",\"width\":\"30\",\"height\":\"24\",\"src\":\"url(data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAB4AAAAYCAYAAADtaU2/AAAABHNCSVQICAgIfAhkiAAAAQFJREFUSEtjLCgokPz//38/IyNjOAMdwP///48wMjKGMebn5x8WFRU1MDY25oHZe+fOHYa7d+8yuLu7U+SUnTt3MigrKzOoqKjAzbl69eqHx48f72QsKCj4D7LAw8MDLrljxw4GkKb+/n6KLC4sLAQ7HpvZg9NiZJeS43VQyJHlY3IsQ9dDlsUjL45HfUxqYht6+Xg0jkfjmFAIDL1UTchHxMiTVUkwMDA0EmM4HjX1ZFk8YcIERkosxte6wdsCoanF+fn5R+Xk5LS0tbUFYL47e/bsl9evX1+cMGGCDSU+xmc2yMeg5u1ERkbGUJgl////v8LCwuLY29v7hhKL8ZkNAMqNnDNh7OH0AAAAAElFTkSuQmCC)\",\"cursor\":\"pointer\",\"hidden\":true,\"left\":742,\"top\":60,\"id\":6,\"lt\":{\"x\":-4,\"y\":-4},\"rb\":{\"x\":-4,\"y\":-4},\"mic\":{\"_id\":\"6075618130bdc0401826ab06\",\"status\":\"APPLY\",\"c_id\":\"5f67708fbd0c1f2d906dad5e\",\"name\":\"\",\"mac\":\"149b2f2c0039\",\"number\":\"1\",\"group\":\"1\",\"ip\":\"192.168.1.57\",\"subnet_mask\":\"\",\"gateway\":\"\",\"mainframe_id\":\"\",\"meeting_room_id\":\"\",\"create_time\":\"Tue Apr 13 2021 17:16:49 GMT+0800 (GMT+08:00)\",\"pap_id\":\"\",\"place\":\"N\",\"rostrum\":\"0\",\"bind\":true}},{\"type\":\"chair\",\"name\":\"椅子\",\"width\":\"30\",\"height\":\"24\",\"src\":\"url(data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAB4AAAAYCAYAAADtaU2/AAAABHNCSVQICAgIfAhkiAAAAQFJREFUSEtjLCgokPz//38/IyNjOAMdwP///48wMjKGMebn5x8WFRU1MDY25oHZe+fOHYa7d+8yuLu7U+SUnTt3MigrKzOoqKjAzbl69eqHx48f72QsKCj4D7LAw8MDLrljxw4GkKb+/n6KLC4sLAQ7HpvZg9NiZJeS43VQyJHlY3IsQ9dDlsUjL45HfUxqYht6+Xg0jkfjmFAIDL1UTchHxMiTVUkwMDA0EmM4HjX1ZFk8YcIERkosxte6wdsCoanF+fn5R+Xk5LS0tbUFYL47e/bsl9evX1+cMGGCDSU+xmc2yMeg5u1ERkbGUJgl////v8LCwuLY29v7hhKL8ZkNAMqNnDNh7OH0AAAAAElFTkSuQmCC)\",\"cursor\":\"pointer\",\"hidden\":true,\"left\":1043,\"top\":236,\"id\":7,\"lt\":{\"x\":-4,\"y\":-4},\"rb\":{\"x\":-4,\"y\":-4},\"mic\":{\"_id\":\"60666d77d3e76e3c10913a22\",\"status\":\"APPLY\",\"c_id\":\"5f67708fbd0c1f2d906dad5e\",\"name\":\"\",\"mac\":\"149b2f46001f\",\"number\":\"4\",\"group\":\"1\",\"ip\":\"192.168.1.31\",\"subnet_mask\":\"\",\"gateway\":\"\",\"mainframe_id\":\"\",\"meeting_room_id\":\"\",\"create_time\":\"Fri Apr 02 2021 08:11:52 GMT+0800 (GMT+08:00)\",\"pap_id\":\"\",\"place\":\"N\",\"rostrum\":\"0\",\"bind\":true}},{\"type\":\"chair\",\"name\":\"椅子\",\"width\":\"30\",\"height\":\"24\",\"src\":\"url(data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAB4AAAAYCAYAAADtaU2/AAAABHNCSVQICAgIfAhkiAAAAQFJREFUSEtjLCgokPz//38/IyNjOAMdwP///48wMjKGMebn5x8WFRU1MDY25oHZe+fOHYa7d+8yuLu7U+SUnTt3MigrKzOoqKjAzbl69eqHx48f72QsKCj4D7LAw8MDLrljxw4GkKb+/n6KLC4sLAQ7HpvZg9NiZJeS43VQyJHlY3IsQ9dDlsUjL45HfUxqYht6+Xg0jkfjmFAIDL1UTchHxMiTVUkwMDA0EmM4HjX1ZFk8YcIERkosxte6wdsCoanF+fn5R+Xk5LS0tbUFYL47e/bsl9evX1+cMGGCDSU+xmc2yMeg5u1ERkbGUJgl////v8LCwuLY29v7hhKL8ZkNAMqNnDNh7OH0AAAAAElFTkSuQmCC)\",\"cursor\":\"pointer\",\"hidden\":true,\"left\":754,\"top\":384,\"id\":8,\"lt\":{\"x\":-4,\"y\":-4},\"rb\":{\"x\":-4,\"y\":-4},\"mic\":{\"_id\":\"606641132d347c46240107d3\",\"status\":\"APPLY\",\"c_id\":\"5f67708fbd0c1f2d906dad5e\",\"name\":\"\",\"mac\":\"149b2f1a0031\",\"number\":\"2\",\"group\":\"2\",\"ip\":\"192.168.1.49\",\"subnet_mask\":\"\",\"gateway\":\"\",\"mainframe_id\":\"\",\"meeting_room_id\":\"\",\"create_time\":\"Fri Apr 02 2021 05:54:27 GMT+0800 (GMT+08:00)\",\"pap_id\":\"\",\"place\":\"N\",\"rostrum\":\"0\",\"bind\":true}}]","check":"0","is_pap_eq":"0","is_mic":"0","type":"ppl0","note":"中协无纸化有发言单元会议室","designated_group":"10"}]
     * agenda : [""]
     * serveItem : []
     * user_list : [{"_id":"611085c9acda332c882a36a4","status":"UNCONFIRMED","c_id":"5f67708fbd0c1f2d906dad5e","meeting_record_id":"611085c9acda332c882a36a3","user_id":"5f67767ebd0c1f2d906dad66","name":"江小鱼","avatar":"/upload/e47b5486294f7601.png","phone":"15212718899","role":"0","device":{"type":"","mac":"c400ad789d9","number":"1"},"sign_time":"","chair_id":"","sign_code":"2120","note":"OUT","dept_id":"5f6773f7bd0c1f2d906dad61"},{"_id":"611085c9acda332c882a36a6","status":"UNCONFIRMED","c_id":"5f67708fbd0c1f2d906dad5e","meeting_record_id":"611085c9acda332c882a36a3","user_id":"5f677640bd0c1f2d906dad64","name":"花满楼","avatar":"/upload/482a086f9abcc2a1.png","phone":"12365454111","role":"2","device":{"type":"","mac":"c400ad789d8","number":"2"},"sign_time":"Mon Aug 09 2021 09:59:34 GMT+0800 (GMT+08:00)","chair_id":"","sign_code":"6040","note":"IN","dept_id":"5f6773f7bd0c1f2d906dad61"},{"_id":"611085c9acda332c882a36a8","status":"UNCONFIRMED","c_id":"5f67708fbd0c1f2d906dad5e","meeting_record_id":"611085c9acda332c882a36a3","user_id":"5f67766bbd0c1f2d906dad65","name":"令狐聪","avatar":"/upload/b8be7b9bfdfb9bc0.png","phone":"13637228866","role":"2","device":null,"sign_time":"","chair_id":"","sign_code":"7744","note":"OUT","dept_id":"5f6773f7bd0c1f2d906dad61"},{"_id":"611085c9acda332c882a36a9","status":"UNCONFIRMED","c_id":"5f67708fbd0c1f2d906dad5e","meeting_record_id":"611085c9acda332c882a36a3","user_id":"60812f6320d26f47ac6d3396","name":"谢逊","avatar":"/upload/default.jpg","phone":"18196740126","role":"2","device":null,"sign_time":"","chair_id":"","sign_code":"1647","note":"OUT","dept_id":"5f6773f7bd0c1f2d906dad61"},{"_id":"611085c9acda332c882a36aa","status":"UNCONFIRMED","c_id":"5f67708fbd0c1f2d906dad5e","meeting_record_id":"611085c9acda332c882a36a3","user_id":"60efb36ec39a2e1eb895cf9e","name":"王大佬","avatar":"/upload/472e2fe7ec6b9f0e.png","phone":"15855555555","role":"2","device":null,"sign_time":"","chair_id":"","sign_code":"7422","note":"OUT","dept_id":"5f6773f7bd0c1f2d906dad61"},{"_id":"611085c9acda332c882a36ab","status":"UNCONFIRMED","c_id":"5f67708fbd0c1f2d906dad5e","meeting_record_id":"611085c9acda332c882a36a3","user_id":"6065f471d80db139c887d512","name":"花无缺","avatar":"/upload/230471e8dd843e49.png","phone":"19841810520","role":"2","device":null,"sign_time":"Mon Aug 09 2021 09:51:27 GMT+0800 (GMT+08:00)","chair_id":"","sign_code":"9529","note":"OUT","dept_id":"5f6773f7bd0c1f2d906dad61"},{"_id":"611085c9acda332c882a36ac","status":"UNCONFIRMED","c_id":"5f67708fbd0c1f2d906dad5e","meeting_record_id":"611085c9acda332c882a36a3","user_id":"5f677602bd0c1f2d906dad63","name":"东方不败","avatar":"/upload/5b9db62e5c5a71b9.png","phone":"12365454111","role":"2","device":null,"sign_time":"","chair_id":"","sign_code":"4874","note":"OUT","dept_id":"5f6773f7bd0c1f2d906dad61"}]
     * _id : 611085c9acda332c882a36a3
     * status : UNDERWAY
     * c_id : 5f67708fbd0c1f2d906dad5e
     * type : Z
     * name : 开发部会议
     * purpose :
     * remind : 15
     * sche_start_time : 2021-08-09 09:29:00
     * sche_end_time : 2021-08-09 13:32:00
     * actual_start_time : 2021-08-09 09:32:58
     * actual_end_time : 2021-08-09 13:32:00
     * from : {"_id":"5f67767ebd0c1f2d906dad66","name":"江小鱼","role":"M","avatar":"upload/e47b5486294f7601.png","phone":"15212718899","dept_id":"5f6773f7bd0c1f2d906dad61","note":{"mac":"","number":"","type":""}}
     * layout_data :
     * check : 0
     * check_status : ENABLE
     * failure_cause :
     * failure_time :
     * create_time : 2021-08-09 09:19:44
     * receiving_status :
     * nameplate_type : 0
     * background_img :
     */

    private String _id;
    private String status;
    private String c_id;
    private String type;
    private String name;
    private String purpose;
    private String remind;
    private String sche_start_time;
    private String sche_end_time;
    private String actual_start_time;
    private String actual_end_time;
    private FromBean from;
    private String layout_data;
    private String check;
    private String check_status;
    private String failure_cause;
    private String failure_time;
    private String create_time;
    private String receiving_status;
    private String nameplate_type;
    private String background_img;
    private List<MeetingRoomIdBean> meeting_room_id;
    private List<String> agenda;
    private List<?> serveItem;
    private List<UserListBean> user_list;

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getC_id() {
        return c_id;
    }

    public void setC_id(String c_id) {
        this.c_id = c_id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPurpose() {
        return purpose;
    }

    public void setPurpose(String purpose) {
        this.purpose = purpose;
    }

    public String getRemind() {
        return remind;
    }

    public void setRemind(String remind) {
        this.remind = remind;
    }

    public String getSche_start_time() {
        return sche_start_time;
    }

    public void setSche_start_time(String sche_start_time) {
        this.sche_start_time = sche_start_time;
    }

    public String getSche_end_time() {
        return sche_end_time;
    }

    public void setSche_end_time(String sche_end_time) {
        this.sche_end_time = sche_end_time;
    }

    public String getActual_start_time() {
        return actual_start_time;
    }

    public void setActual_start_time(String actual_start_time) {
        this.actual_start_time = actual_start_time;
    }

    public String getActual_end_time() {
        return actual_end_time;
    }

    public void setActual_end_time(String actual_end_time) {
        this.actual_end_time = actual_end_time;
    }

    public FromBean getFrom() {
        return from;
    }

    public void setFrom(FromBean from) {
        this.from = from;
    }

    public String getLayout_data() {
        return layout_data;
    }

    public void setLayout_data(String layout_data) {
        this.layout_data = layout_data;
    }

    public String getCheck() {
        return check;
    }

    public void setCheck(String check) {
        this.check = check;
    }

    public String getCheck_status() {
        return check_status;
    }

    public void setCheck_status(String check_status) {
        this.check_status = check_status;
    }

    public String getFailure_cause() {
        return failure_cause;
    }

    public void setFailure_cause(String failure_cause) {
        this.failure_cause = failure_cause;
    }

    public String getFailure_time() {
        return failure_time;
    }

    public void setFailure_time(String failure_time) {
        this.failure_time = failure_time;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    public String getReceiving_status() {
        return receiving_status;
    }

    public void setReceiving_status(String receiving_status) {
        this.receiving_status = receiving_status;
    }

    public String getNameplate_type() {
        return nameplate_type;
    }

    public void setNameplate_type(String nameplate_type) {
        this.nameplate_type = nameplate_type;
    }

    public String getBackground_img() {
        return background_img;
    }

    public void setBackground_img(String background_img) {
        this.background_img = background_img;
    }

    public List<MeetingRoomIdBean> getMeeting_room_id() {
        return meeting_room_id;
    }

    public void setMeeting_room_id(List<MeetingRoomIdBean> meeting_room_id) {
        this.meeting_room_id = meeting_room_id;
    }

    public List<String> getAgenda() {
        return agenda;
    }

    public void setAgenda(List<String> agenda) {
        this.agenda = agenda;
    }

    public List<?> getServeItem() {
        return serveItem;
    }

    public void setServeItem(List<?> serveItem) {
        this.serveItem = serveItem;
    }

    public List<UserListBean> getUser_list() {
        return user_list;
    }

    public void setUser_list(List<UserListBean> user_list) {
        this.user_list = user_list;
    }

    public static class FromBean {
        /**
         * _id : 5f67767ebd0c1f2d906dad66
         * name : 江小鱼
         * role : M
         * avatar : upload/e47b5486294f7601.png
         * phone : 15212718899
         * dept_id : 5f6773f7bd0c1f2d906dad61
         * note : {"mac":"","number":"","type":""}
         */

        private String _id;
        private String name;
        private String role;
        private String avatar;
        private String phone;
        private String dept_id;
        private NoteBean note;

        public String get_id() {
            return _id;
        }

        public void set_id(String _id) {
            this._id = _id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getRole() {
            return role;
        }

        public void setRole(String role) {
            this.role = role;
        }

        public String getAvatar() {
            return avatar;
        }

        public void setAvatar(String avatar) {
            this.avatar = avatar;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public String getDept_id() {
            return dept_id;
        }

        public void setDept_id(String dept_id) {
            this.dept_id = dept_id;
        }

        public NoteBean getNote() {
            return note;
        }

        public void setNote(NoteBean note) {
            this.note = note;
        }

        public static class NoteBean {
            /**
             * mac :
             * number :
             * type :
             */

            private String mac;
            private String number;
            private String type;

            public String getMac() {
                return mac;
            }

            public void setMac(String mac) {
                this.mac = mac;
            }

            public String getNumber() {
                return number;
            }

            public void setNumber(String number) {
                this.number = number;
            }

            public String getType() {
                return type;
            }

            public void setType(String type) {
                this.type = type;
            }
        }
    }

    public static class MeetingRoomIdBean {
        /**
         * _id : 606612bcb32fd612940ba25d
         * status : ENABLE
         * c_id : 5f67708fbd0c1f2d906dad5e
         * name : 中协无纸化有发言单元会议室
         * address : 南京市雨花台区科创园蓝莓8栋101
         * seating : 100
         * realisticImg : meetingRoom/会议室画板 5.jpg
         * guideMap : meetingRoom/会议室画板 5.jpg
         * layoutImg :
         * layoutData : [{"width":"447","height":"227","type":"desk","borderRadius":"20px","border":"1px solid #607dd5","left":"543","top":"126","id":0,"lt":{"x":-4,"y":-4},"rb":{"x":-4,"y":-4}},{"type":"mic","name":"发言单元","width":"29","height":"22","src":"url(data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAB0AAAAWCAYAAAA8VJfMAAAABHNCSVQICAgIfAhkiAAAAYZJREFUSEu1lbFNA0EQRf9mZODYiZFcABINmAqgAyB2MoEDMqACPoFTAx3gCjAVAHJqCSgAya5g0Uez6ITuzj7fMtLKOuk8b2b2z7+Afwoz6wE4BaBfnRTXIQfTzAYhhOi5ejFGnYsQwk5J/ulWUDM7A3AMYABgr2HhzaBmdgDgxmFF1nPh4QOAziDGeBhC+PJnvfIKgBt36sAXT/4J4ArAjKQAjWIjqAOffJQPAIzkshGp8PJaqJnpztShFPhAUvfZKjaBaoyXAN50T206TJXWQr3Ldx/rEclZqxb9z+ug977gU5InOYDKsQ4qsewC2N9GpVVFVkLdAO50lyS1n9miDvrornNOUmPOFnXQ5KWdHIotVlwKlYEDkBlkH22lkMws7eYtScs217QyvosSTLaVACA9SAulVhnMLAkmd0OVlinoj2BGoxG63W5r8GKxwHg8Vp4lyU5Zwl/ocDhEv99vDZ3P55hMJsqzIln6gRc0WV1r4J8EteNVNXQhyfLaxsqFVPnN/Qa994gXdTAQqAAAAABJRU5ErkJggg==)","cursor":"pointer","showchild":false,"hidden":true,"left":752,"top":122,"id":1,"mic":{"_id":"606641132d347c46240107d1","status":"APPLY","c_id":"5f67708fbd0c1f2d906dad5e","name":"","mac":"149b2f42004a","number":"1","group":"2","ip":"192.168.1.74","subnet_mask":"","gateway":"","mainframe_id":"","meeting_room_id":"","create_time":"Fri Apr 02 2021 05:54:27 GMT+0800 (GMT+08:00)","pap_id":"","place":"N","rostrum":"1","bind":false},"lt":{"x":-4,"y":-4},"rb":{"x":-4,"y":-4}},{"type":"mic","name":"发言单元","width":"29","height":"22","src":"url(data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAB0AAAAWCAYAAAA8VJfMAAAABHNCSVQICAgIfAhkiAAAAYZJREFUSEu1lbFNA0EQRf9mZODYiZFcABINmAqgAyB2MoEDMqACPoFTAx3gCjAVAHJqCSgAya5g0Uez6ITuzj7fMtLKOuk8b2b2z7+Afwoz6wE4BaBfnRTXIQfTzAYhhOi5ejFGnYsQwk5J/ulWUDM7A3AMYABgr2HhzaBmdgDgxmFF1nPh4QOAziDGeBhC+PJnvfIKgBt36sAXT/4J4ArAjKQAjWIjqAOffJQPAIzkshGp8PJaqJnpztShFPhAUvfZKjaBaoyXAN50T206TJXWQr3Ldx/rEclZqxb9z+ug977gU5InOYDKsQ4qsewC2N9GpVVFVkLdAO50lyS1n9miDvrornNOUmPOFnXQ5KWdHIotVlwKlYEDkBlkH22lkMws7eYtScs217QyvosSTLaVACA9SAulVhnMLAkmd0OVlinoj2BGoxG63W5r8GKxwHg8Vp4lyU5Zwl/ocDhEv99vDZ3P55hMJsqzIln6gRc0WV1r4J8EteNVNXQhyfLaxsqFVPnN/Qa994gXdTAQqAAAAABJRU5ErkJggg==)","cursor":"pointer","showchild":false,"hidden":true,"left":753,"top":324,"id":2,"mic":{"_id":"606641132d347c46240107d3","status":"APPLY","c_id":"5f67708fbd0c1f2d906dad5e","name":"","mac":"149b2f1a0031","number":"2","group":"2","ip":"192.168.1.49","subnet_mask":"","gateway":"","mainframe_id":"","meeting_room_id":"","create_time":"Fri Apr 02 2021 05:54:27 GMT+0800 (GMT+08:00)","pap_id":"","place":"N","rostrum":"0","bind":false},"lt":{"x":-4,"y":-4},"rb":{"x":-4,"y":-4}},{"type":"mic","name":"发言单元","width":"29","height":"22","src":"url(data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAB0AAAAWCAYAAAA8VJfMAAAABHNCSVQICAgIfAhkiAAAAYZJREFUSEu1lbFNA0EQRf9mZODYiZFcABINmAqgAyB2MoEDMqACPoFTAx3gCjAVAHJqCSgAya5g0Uez6ITuzj7fMtLKOuk8b2b2z7+Afwoz6wE4BaBfnRTXIQfTzAYhhOi5ejFGnYsQwk5J/ulWUDM7A3AMYABgr2HhzaBmdgDgxmFF1nPh4QOAziDGeBhC+PJnvfIKgBt36sAXT/4J4ArAjKQAjWIjqAOffJQPAIzkshGp8PJaqJnpztShFPhAUvfZKjaBaoyXAN50T206TJXWQr3Ldx/rEclZqxb9z+ug977gU5InOYDKsQ4qsewC2N9GpVVFVkLdAO50lyS1n9miDvrornNOUmPOFnXQ5KWdHIotVlwKlYEDkBlkH22lkMws7eYtScs217QyvosSTLaVACA9SAulVhnMLAkmd0OVlinoj2BGoxG63W5r8GKxwHg8Vp4lyU5Zwl/ocDhEv99vDZ3P55hMJsqzIln6gRc0WV1r4J8EteNVNXQhyfLaxsqFVPnN/Qa994gXdTAQqAAAAABJRU5ErkJggg==)","cursor":"pointer","showchild":false,"hidden":true,"left":542,"top":234,"id":3,"mic":{"_i:"APPLY","c_id":"5f67708fbd0c1f2d906dad5e","name":"","mac":"149b2f46001f","number":"4","group":"1","ip":"192.168.1.31","subnet_mask":"","gateway":"","mainframe_id":"","meeting_room_id":"","create_time":"Fri Apr 02 2021 08:11:52 GMT+0800 (GMT+08:00)","pap_id":"","place":"N","rostrum":"0","bind":false},"lt":{"x":-4,"y":-4},"rb":{"x":-4,"y":-4}},{"type":"mic","name":"发言单元","width":"29","height":"22","src":"url(data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAB0AAAAWCAYAAAA8VJfMAAAABHNCSVQICAgIfAhkiAAAAYZJREFUSEu1lbFNA0EQRf9mZODYiZFcABINmAqgAyB2MoEDMqACPoFTAx3gCjAVAHJqCSgAya5g0Uez6ITuzj7fMtLKOuk8b2b2z7+Afwoz6wE4BaBfnRTXIQfTzAYhhOi5ejFGnYsQwk5J/ulWUDM7A3AMYABgr2HhzaBmdgDgxmFF1nPh4QOAziDGeBhC+PJnvfIKgBt36sAXT/4J4ArAjKQAjWIjqAOffJQPAIzkshGp8PJaqJnpztShFPhAUvfZKjaBaoyXAN50T206TJXWQr3Ldx/rEclZqxb9z+ug977gU5InOYDKsQ4qsewC2N9GpVVFVkLdAO50lyS1n9miDvrornNOUmPOFnXQ5KWdHIotVlwKlYEDkBlkH22lkMws7eYtScs217QyvosSTLaVACA9SAulVhnMLAkmd0OVlinoj2BGoxG63W5r8GKxwHg8Vp4lyU5Zwl/ocDhEv99vDZ3P55hMJsqzIln6gRc0WV1r4J8EteNVNXQhyfLaxsqFVPnN/Qa994gXdTAQqAAAAABJRU5ErkJggg==)","cursor":"pointer","showchild":false,"hidden":true,"left":962,"top":235,"id":4,"mic":{"_id":"6075618130bdc0401826ab06","status":"APPLY","c_id":"5f67708fbd0c1f2d906dad5e","name":"","mac":"149b2f2c0039","number":"1","group":"1","ip":"192.168.1.57","subnet_mask":"","gateway":"","mainframe_id":"","meeting_room_id":"","create_time":"Tue Apr 13 2021 17:16:49 GMT+0800 (GMT+08:00)","pap_id":"","place":"N","rostrum":"0","bind":false},"lt":{"x":-4,"y":-4},"rb":{"x":-4,"y":-4}},{"type":"chair","name":"椅子","width":"30","height":"24","src":"url(data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAB4AAAAYCAYAAADtaU2/AAAABHNCSVQICAgIfAhkiAAAAQFJREFUSEtjLCgokPz//38/IyNjOAMdwP///48wMjKGMebn5x8WFRU1MDY25oHZe+fOHYa7d+8yuLu7U+SUnTt3MigrKzOoqKjAzbl69eqHx48f72QsKCj4D7LAw8MDLrljxw4GkKb+/n6KLC4sLAQ7HpvZg9NiZJeS43VQyJHlY3IsQ9dDlsUjL45HfUxqYht6+Xg0jkfjmFAIDL1UTchHxMiTVUkwMDA0EmM4HjX1ZFk8YcIERkosxte6wdsCoanF+fn5R+Xk5LS0tbUFYL47e/bsl9evX1+cMGGCDSU+xmc2yMeg5u1ERkbGUJgl////v8LCwuLY29v7hhKL8ZkNAMqNnDNh7OH0AAAAAElFTkSuQmCC)","cursor":"pointer","hidden":true,"left":462,"top":233,"id":5,"lt":{"x":-4,"y":-4},"rb":{"x":-4,"y":-4},"mic":{"_id":"606641132d347c46240107d1","status":"APPLY","c_id":"5f67708fbd0c1f2d906dad5e","name":"","mac":"149b2f42004a","number":"1","group":"2","ip":"192.168.1.74","subnet_mask":"","gateway":"","mainframe_id":"","meeting_room_id":"","create_time":"Fri Apr 02 2021 05:54:27 GMT+0800 (GMT+08:00)","pap_id":"","place":"N","rostrum":"1","bind":true}},{"type":"chair","name":"椅子","width":"30","height":"24","src":"url(data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAB4AAAAYCAYAAADtaU2/AAAABHNCSVQICAgIfAhkiAAAAQFJREFUSEtjLCgokPz//38/IyNjOAMdwP///48wMjKGMebn5x8WFRU1MDY25oHZe+fOHYa7d+8yuLu7U+SUnTt3MigrKzOoqKjAzbl69eqHx48f72QsKCj4D7LAw8MDLrljxw4GkKb+/n6KLC4sLAQ7HpvZg9NiZJeS43VQyJHlY3IsQ9dDlsUjL45HfUxqYht6+Xg0jkfjmFAIDL1UTchHxMiTVUkwMDA0EmM4HjX1ZFk8YcIERkosxte6wdsCoanF+fn5R+Xk5LS0tbUFYL47e/bsl9evX1+cMGGCDSU+xmc2yMeg5u1ERkbGUJgl////v8LCwuLY29v7hhKL8ZkNAMqNnDNh7OH0AAAAAElFTkSuQmCC)","cursor":"pointer","hidden":true,"left":742,"top":60,"id":6,"lt":{"x":-4,"y":-4},"rb":{"x":-4,"y":-4},"mic":{"_id":"6075618130bdc0401826ab06","status":"APPLY","c_id":"5f67708fbd0c1f2d906dad5e","name":"","mac":"149b2f2c0039","number":"1","group":"1","ip":"192.168.1.57","subnet_mask":"","gateway":"","mainframe_id":"","meeting_room_id":"","create_time":"Tue Apr 13 2021 17:16:49 GMT+0800 (GMT+08:00)","pap_id":"","place":"N","rostrum":"0","bind":true}},{"type":"chair","name":"椅子","width":"30","height":"24","src":"url(data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAB4AAAAYCAYAAADtaU2/AAAABHNCSVQICAgIfAhkiAAAAQFJREFUSEtjLCgokPz//38/IyNjOAMdwP///48wMjKGMebn5x8WFRU1MDY25oHZe+fOHYa7d+8yuLu7U+SUnTt3MigrKzOoqKjAzbl69eqHx48f72QsKCj4D7LAw8MDLrljxw4GkKb+/n6KLC4sLAQ7HpvZg9NiZJeS43VQyJHlY3IsQ9dDlsUjL45HfUxqYht6+Xg0jkfjmFAIDL1UTchHxMiTVUkwMDA0EmM4HjX1ZFk8YcIERkosxte6wdsCoanF+fn5R+Xk5LS0tbUFYL47e/bsl9evX1+cMGGCDSU+xmc2yMeg5u1ERkbGUJgl////v8LCwuLY29v7hhKL8ZkNAMqNnDNh7OH0AAAAAElFTkSuQmCC)","cursor":"pointer","hidden":true,"left":1043,"top":236,"id":7,"lt":{"x":-4,"y":-4},"rb":{"x":-4,"y":-4},"mic":{"_id":"60666d77d3e76e3c10913a22","status":"APPLY","c_id":"5f67708fbd0c1f2d906dad5e","name":"","mac":"149b2f46001f","number":"4","group":"1","ip":"192.168.1.31","subnet_mask":"","gateway":"","mainframe_id":"","meeting_room_id":"","create_time":"Fri Apr 02 2021 08:11:52 GMT+0800 (GMT+08:00)","pap_id":"","place":"N","rostrum":"0","bind":true}},{"type":"chair","name":"椅子","width":"30","height":"24","src":"url(data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAB4AAAAYCAYAAADtaU2/AAAABHNCSVQICAgIfAhkiAAAAQFJREFUSEtjLCgokPz//38/IyNjOAMdwP///48wMjKGMebn5x8WFRU1MDY25oHZe+fOHYa7d+8yuLu7U+SUnTt3MigrKzOoqKjAzbl69eqHx48f72QsKCj4D7LAw8MDLrljxw4GkKb+/n6KLC4sLAQ7HpvZg9NiZJeS43VQyJHlY3IsQ9dDlsUjL45HfUxqYht6+Xg0jkfjmFAIDL1UTchHxMiTVUkwMDA0EmM4HjX1ZFk8YcIERkosxte6wdsCoanF+fn5R+Xk5LS0tbUFYL47e/bsl9evX1+cMGGCDSU+xmc2yMeg5u1ERkbGUJgl////v8LCwuLY29v7hhKL8ZkNAMqNnDNh7OH0AAAAAElFTkSuQmCC)","cursor":"pointer","hidden":true,"left":754,"top":384,"id":8,"lt":{"x":-4,"y":-4},"rb":{"x":-4,"y":-4},"mic":{"_id":"606641132d347c46240107d3","status":"APPLY","c_id":"5f67708fbd0c1f2d906dad5e","name":"","mac":"149b2f1a0031","number":"2","group":"2","ip":"192.168.1.49","subnet_mask":"","gateway":"","mainframe_id":"","meeting_room_id":"","create_time":"Fri Apr 02 2021 05:54:27 GMT+0800 (GMT+08:00)","pap_id":"","place":"N","rostrum":"0","bind":true}}]
         * check : 0
         * is_pap_eq : 0
         * is_mic : 0
         * type : ppl0
         * note : 中协无纸化有发言单元会议室
         * designated_group : 10
         */

        private String _id;
        private String status;
        private String c_id;
        private String name;
        private String address;
        private String seating;
        private String realisticImg;
        private String guideMap;
        private String layoutImg;
        private String layoutData;
        private String check;
        private String is_pap_eq;
        private String is_mic;
        private String type;
        private String note;
        private String designated_group;

        public String get_id() {
            return _id;
        }

        public void set_id(String _id) {
            this._id = _id;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getC_id() {
            return c_id;
        }

        public void setC_id(String c_id) {
            this.c_id = c_id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getSeating() {
            return seating;
        }

        public void setSeating(String seating) {
            this.seating = seating;
        }

        public String getRealisticImg() {
            return realisticImg;
        }

        public void setRealisticImg(String realisticImg) {
            this.realisticImg = realisticImg;
        }

        public String getGuideMap() {
            return guideMap;
        }

        public void setGuideMap(String guideMap) {
            this.guideMap = guideMap;
        }

        public String getLayoutImg() {
            return layoutImg;
        }

        public void setLayoutImg(String layoutImg) {
            this.layoutImg = layoutImg;
        }

        public String getLayoutData() {
            return layoutData;
        }

        public void setLayoutData(String layoutData) {
            this.layoutData = layoutData;
        }

        public String getCheck() {
            return check;
        }

        public void setCheck(String check) {
            this.check = check;
        }

        public String getIs_pap_eq() {
            return is_pap_eq;
        }

        public void setIs_pap_eq(String is_pap_eq) {
            this.is_pap_eq = is_pap_eq;
        }

        public String getIs_mic() {
            return is_mic;
        }

        public void setIs_mic(String is_mic) {
            this.is_mic = is_mic;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getNote() {
            return note;
        }

        public void setNote(String note) {
            this.note = note;
        }

        public String getDesignated_group() {
            return designated_group;
        }

        public void setDesignated_group(String designated_group) {
            this.designated_group = designated_group;
        }
    }

    public static class UserListBean {
        /**
         * _id : 611085c9acda332c882a36a4
         * status : UNCONFIRMED
         * c_id : 5f67708fbd0c1f2d906dad5e
         * meeting_record_id : 611085c9acda332c882a36a3
         * user_id : 5f67767ebd0c1f2d906dad66
         * name : 江小鱼
         * avatar : /upload/e47b5486294f7601.png
         * phone : 15212718899
         * role : 0
         * device : {"type":"","mac":"c400ad789d9","number":"1"}
         * sign_time :
         * chair_id :
         * sign_code : 2120
         * note : OUT
         * dept_id : 5f6773f7bd0c1f2d906dad61
         */

        private String _id;
        private String status;
        private String c_id;
        private String meeting_record_id;
        private String user_id;
        private String name;
        private String avatar;
        private String phone;
        private String role;
        private DeviceBean device;
        private String sign_time;
        private String chair_id;
        private String sign_code;
        private String note;
        private String dept_id;

        public String get_id() {
            return _id;
        }

        public void set_id(String _id) {
            this._id = _id;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getC_id() {
            return c_id;
        }

        public void setC_id(String c_id) {
            this.c_id = c_id;
        }

        public String getMeeting_record_id() {
            return meeting_record_id;
        }

        public void setMeeting_record_id(String meeting_record_id) {
            this.meeting_record_id = meeting_record_id;
        }

        public String getUser_id() {
            return user_id;
        }

        public void setUser_id(String user_id) {
            this.user_id = user_id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getAvatar() {
            return avatar;
        }

        public void setAvatar(String avatar) {
            this.avatar = avatar;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public String getRole() {
            return role;
        }

        public void setRole(String role) {
            this.role = role;
        }

        public DeviceBean getDevice() {
            return device;
        }

        public void setDevice(DeviceBean device) {
            this.device = device;
        }

        public String getSign_time() {
            return sign_time;
        }

        public void setSign_time(String sign_time) {
            this.sign_time = sign_time;
        }

        public String getChair_id() {
            return chair_id;
        }

        public void setChair_id(String chair_id) {
            this.chair_id = chair_id;
        }

        public String getSign_code() {
            return sign_code;
        }

        public void setSign_code(String sign_code) {
            this.sign_code = sign_code;
        }

        public String getNote() {
            return note;
        }

        public void setNote(String note) {
            this.note = note;
        }

        public String getDept_id() {
            return dept_id;
        }

        public void setDept_id(String dept_id) {
            this.dept_id = dept_id;
        }

        public static class DeviceBean {
            /**
             * type :
             * mac : c400ad789d9
             * number : 1
             */

            private String type;
            private String mac;
            private String number;

            public String getType() {
                return type;
            }

            public void setType(String type) {
                this.type = type;
            }

            public String getMac() {
                return mac;
            }

            public void setMac(String mac) {
                this.mac = mac;
            }

            public String getNumber() {
                return number;
            }

            public void setNumber(String number) {
                this.number = number;
            }
        }
    }
}


