package com.coloz.esptouch;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import java.util.List;

import com.espressif.iot.esptouch.EsptouchTask;
import com.espressif.iot.esptouch.IEsptouchListener;
import com.espressif.iot.esptouch.IEsptouchResult;
import com.espressif.iot.esptouch.IEsptouchTask;
import com.espressif.iot.esptouch.task.__IEsptouchTask;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaPlugin;

import org.apache.cordova.*;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class esptouch extends CordovaPlugin {
  private IEsptouchTask mEsptouchTask;
  private CallbackContext esptouchCallbackContext;
  private final Object mLock = new Object();
  private static final String TAG = "esptouch";

  @Override
  public void initialize(CordovaInterface cordova, CordovaWebView webView) {
    super.initialize(cordova, webView);
  }

  public static byte[] strToByteArray(String str) {
    if (str == null) {
      return null;
    }
    byte[] byteArray = str.getBytes();
    return byteArray;
  }

  private IEsptouchListener myListener = new IEsptouchListener() {
    @Override
    public void onEsptouchResultAdded(final IEsptouchResult result) {
      if (result.isSuc()) {
        cordova.getThreadPool().execute(new Runnable() {
          @Override
          public void run() {
            JSONObject device = new JSONObject();
            try {
              device.put("bssid", result.getBssid());
              device.put("ip", result.getInetAddress().getHostAddress());
            } catch (JSONException e) {
              Log.e(TAG, "unexpected JSON exception", e);
            }
            PluginResult pluginResult = new PluginResult(PluginResult.Status.OK, device);
            pluginResult.setKeepCallback(true);
            esptouchCallbackContext.sendPluginResult(pluginResult);
          }
        });
      }
    }
  };

  @Override
  public boolean execute(String action, final JSONArray args, final CallbackContext callbackContext)
    throws JSONException {
    if (action.equals("start")) {
      int taskResultCount;
      esptouchCallbackContext = callbackContext;
      synchronized (mLock) {
        final byte[] apSsid = strToByteArray(args.getString(0));
        final byte[] apBssid = strToByteArray(args.getString(1));
        final byte[] apPassword = strToByteArray(args.getString(2));
        final byte[] deviceCountData = strToByteArray(args.getString(3));
        final byte[] broadcastData = strToByteArray(args.getString(4));
        taskResultCount = deviceCountData.length == 0 ? -1 : Integer.parseInt(new String(deviceCountData));
        mEsptouchTask = new EsptouchTask(apSsid, apBssid, apPassword, cordova.getActivity());
        mEsptouchTask.setPackageBroadcast(broadcastData[0] == 49);
        mEsptouchTask.setEsptouchListener(myListener);
      }
      cordova.getThreadPool().execute(
        new Runnable() {
          @Override
          public void run() {
            List<IEsptouchResult> resultList = mEsptouchTask.executeForResults(taskResultCount);
            IEsptouchResult firstResult = resultList.get(0);
            if (!firstResult.isCancelled()) {
              int count = 0;
              final int maxDisplayCount = taskResultCount;
              if (!firstResult.isSuc()) {
                callbackContext.error("No Device Found");
              }
            }
          }
        }
      );
      return true;
    } else if (action.equals("stop")) {
      mEsptouchTask.interrupt();
      callbackContext.success();
    } else {
      callbackContext.error("can not find the function " + action);
    }
    return true;
  }

}
