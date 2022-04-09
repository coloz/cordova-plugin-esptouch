package com.coloz.esptouch2;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.espressif.iot.esptouch2.provision.EspProvisioner;
import com.espressif.iot.esptouch2.provision.EspProvisioningListener;
import com.espressif.iot.esptouch2.provision.EspProvisioningRequest;
import com.espressif.iot.esptouch2.provision.EspProvisioningResult;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaPlugin;

import org.apache.cordova.*;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class esptouch2 extends CordovaPlugin {
  private EspProvisioner provisioner;
  private EspProvisioningListener listener;
  private CallbackContext esptouchCallbackContext;
  private final Object mLock = new Object();
  private static final String TAG = "esptouch2";

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

  @Override
  public boolean execute(String action, final JSONArray args, final CallbackContext callbackContext)
    throws JSONException {
    if (action.equals("start")) {
      esptouchCallbackContext = callbackContext;
      synchronized (mLock) {
        final byte[] apSsid = strToByteArray(args.getString(0));
        final byte[] apPassword = strToByteArray(args.getString(1));
        final byte[] customData = strToByteArray(args.getString(2));

        provisioner = new EspProvisioner(cordova.getActivity());
        listener = new EspProvisioningListener() {
          @Override
          public void onStart() {
          }

          @Override
          public void onResponse(EspProvisioningResult result) {
            cordova.getThreadPool().execute(new Runnable() {
              @Override
              public void run() {
                JSONObject device = new JSONObject();
                try {
                  device.put("bssid", result.bssid);
                  device.put("ip", result.address.getHostAddress());
                } catch (JSONException e) {
                  Log.e(TAG, "unexpected JSON exception", e);
                }
                PluginResult pluginResult = new PluginResult(PluginResult.Status.OK, device);
                pluginResult.setKeepCallback(true);
                esptouchCallbackContext.sendPluginResult(pluginResult);
              }
            });
          }

          @Override
          public void onStop() {
          }

          @Override
          public void onError(Exception e) {
          }
        };
        EspProvisioningRequest request = new EspProvisioningRequest.Builder(cordova.getActivity())
                .setSSID(apSsid) // AP's SSID, nullable
                //.setBSSID(apBssid) // AP's BSSID, nonnull
                .setPassword(apPassword) // AP's password, nullable if the AP is open
                .setReservedData(customData) // User's custom data, nullable. If not null, the max length is 127
                // .setAESKey(aesKey) // nullable, if not null, it must be 16 bytes. App developer should negotiate an AES key with Device developer first.
                .build();
        provisioner.startProvisioning(request, listener);
      }

      return true;
    } else if (action.equals("stop")) {
      provisioner.stopProvisioning();
      provisioner.close();
      callbackContext.success();
    } else {
      callbackContext.error("can not find the function " + action);
    }
    return true;
  }

}