package com.mylove.launcher.controller;

import com.mylove.launcher.bean.HttpEvent;
import com.mylove.module_base.utils.FileUtils;

import org.greenrobot.eventbus.EventBus;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import cn.hotapk.fhttpserver.NanoHTTPD;
import cn.hotapk.fhttpserver.annotation.RequestMapping;
import cn.hotapk.fhttpserver.annotation.RequestParam;
import cn.hotapk.fhttpserver.annotation.ResponseBody;

/**
 * Created by Administrator on 2018/8/14.
 */

public class ActionController {

    @RequestMapping("action")
    public NanoHTTPD.Response action(@RequestParam("fragment") String fragment) {
        System.out.println("fragment =" + fragment);
        HttpEvent httpEvent = new HttpEvent();
        httpEvent.setEvent(HttpEvent.CHANGE_STYLE);
        httpEvent.setObj(fragment);
        EventBus.getDefault().post(httpEvent);
        return NanoHTTPD.newFixedLengthResponse("");
    }


    public static boolean uploadFile(NanoHTTPD.IHTTPSession session, String fileDir, String parm) {
        Map<String, String> files = new HashMap<>();
        try {
            session.parseBody(files);
            Map<String, String> parms = session.getParms();
//            return FileUtils.copyFileTo(files.get(parm), fileDir + "/" + parms.get(parm));
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NanoHTTPD.ResponseException e) {
            e.printStackTrace();
        }
        return false;
    }
}
