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
        Log.i(TAG, "device: " + result.getInetAddress().getHostAddress());
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
        byte[] apSsid = strToByteArray(args.getString(0));
        byte[] apBssid = strToByteArray(args.getString(1));
        byte[] apPassword = strToByteArray(args.getString(2));
        byte[] deviceCountData = strToByteArray(args.getString(3));
        byte[] broadcastData = strToByteArray(args.getString(4));
        taskResultCount = deviceCountData.length == 0 ? -1 : Integer.parseInt(new String(deviceCountData));
        mEsptouchTask = new EsptouchTask(apSsid, apBssid, apPassword, cordova.getActivity());
        mEsptouchTask.setPackageBroadcast(broadcastData[0] == 1);
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
              if (firstResult.isSuc()) {
                StringBuilder sb = new StringBuilder();
                for (IEsptouchResult resultInList : resultList) {
                  sb.append("device" + count + ",bssid=" + resultInList.getBssid() + ",InetAddress="
                    + resultInList.getInetAddress().getHostAddress() + ".");
                  count++;
                  if (count >= maxDisplayCount) {
                    break;
                  }
                }
                if (count < resultList.size()) {
                  sb.append("\nthere's " + (resultList.size() - count)
                    + " more resultList(s) without showing\n");
                }
                PluginResult result = new PluginResult(PluginResult.Status.OK, "Finished: " + sb);
                result.setKeepCallback(true);
                callbackContext.sendPluginResult(result);
              } else {
                PluginResult result = new PluginResult(PluginResult.Status.ERROR, "No Device Found!");
                result.setKeepCallback(true);
                callbackContext.sendPluginResult(result);
              }
            }
          }
        }
      );
      return true;
    } else if (action.equals("stop")) {
      mEsptouchTask.interrupt();
      callbackContext.success();
//      PluginResult result = new PluginResult(PluginResult.Status.OK, "Cancel Success");
//      result.setKeepCallback(true);
//      callbackContext.sendPluginResult(result);
//      return true;
    } else {
      callbackContext.error("can not find the function " + action);
//      return false;
    }
    return true;
  }

}
