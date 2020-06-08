package com.example.createnewproject2.net;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Handler;
import android.os.Message;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.createnewproject2.AppClient;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Create by 张瀛煜 on 2020-06-08 ：）
 */
public class VolleyTo extends Thread {
    private String Url = "http://118.190.26.201:8080/traffic/";
    private boolean isLoop;
    private int time;
    private JSONObject jsonObject = new JSONObject();
    private VolleyLo volleyLo;
    private ProgressDialog dialog;
    private Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {

            if (dialog != null && dialog.isShowing()) {
                dialog.dismiss();
            }
            if (msg.what == 0) {
                volleyLo.onResponse((JSONObject) msg.obj);
            } else {
                volleyLo.onErrorResponse((VolleyError) msg.obj);
            }
            return false;
        }
    });

    public VolleyTo setDialog(Context context) {
        dialog = new ProgressDialog(context);
        dialog.setTitle("提示");
        dialog.setMessage("Loading....");
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
        return this;
    }

    public VolleyTo setVolleyLo(VolleyLo volleyLo) {
        this.volleyLo = volleyLo;
        return this;
    }

    public VolleyTo setUrl(String url) {
        Url += url;
        return this;
    }

    public VolleyTo setLoop(boolean loop) {
        isLoop = loop;
        return this;
    }

    public VolleyTo setTime(int time) {
        this.time = time;
        return this;
    }

    public VolleyTo setJsonObject(String k, Object v) {
        try {
            jsonObject.put(k, v);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return this;
    }

    @Override
    public void run() {
        super.run();
        do {
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Url, jsonObject
                    , new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject jsonObject) {
                    Message message = new Message();
                    message.what = 0;
                    message.obj = jsonObject;
                    if (dialog != null) {
                        handler.sendMessageDelayed(message, 600);
                    } else {
                        handler.sendMessage(message);
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {
                    Message message = new Message();
                    message.what = 1;
                    message.obj = volleyError;
                    if (dialog != null) {
                        handler.sendMessageDelayed(message, 600);
                    } else {
                        handler.sendMessage(message);
                    }
                }
            });
            AppClient.add(jsonObjectRequest);
            try {
                Thread.sleep(time);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        } while (isLoop);
    }
}
