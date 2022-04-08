package com.coloz.esptouch2;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.core.app.ActivityCompat;

import com.espressif.esptouch.android.EspTouchActivityAbs;
import com.espressif.esptouch.android.EspTouchApp;
import com.espressif.esptouch.android.R;
import com.espressif.esptouch.android.databinding.ActivityEsptouch2Binding;
import com.espressif.iot.esptouch2.provision.EspProvisioner;
import com.espressif.iot.esptouch2.provision.EspProvisioningRequest;
import com.espressif.iot.esptouch2.provision.EspSyncListener;
import com.espressif.iot.esptouch2.provision.IEspProvisioner;
import com.espressif.iot.esptouch2.provision.TouchNetUtil;

import java.lang.ref.WeakReference;
import java.net.InetAddress;


public class esptouch extends CordovaPlugin {
  private EspProvisioner mProvisioner;
  private CallbackContext esptouchCallbackContext;
  private final Object mLock = new Object();
  private static final String TAG = "esptouch_v2";

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

  private EspProvisioningListener listener = new EspProvisioningListener() {
    @Override
    public void onResponse(EspProvisionResult result) {
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
        final byte[] customData = strToByteArray(args.getString(3));
        taskResultCount = deviceCountData.length == 0 ? -1 : Integer.parseInt(new String(deviceCountData));
        provisioner = new EspProvisioner(cordova.getActivity());
        EspProvisioningRequest request = new EspProvisioningRequest.Builder(context)
                .setSSID(apSsid) // AP's SSID, nullable
                .setBSSID(apBssid) // AP's BSSID, nonnull
                .setPassword(apPassword) // AP's password, nullable if the AP is open
                .setReservedData(customData) // User's custom data, nullable. If not null, the max length is 127
                // .setAESKey(aesKey) // nullable, if not null, it must be 16 bytes. App developer should negotiate an AES key with Device developer first.
                .build();
        provisioner.startProvisioning(request, listener);
      }
      // cordova.getThreadPool().execute(
      //   new Runnable() {
      //     @Override
      //     public void run() {
      //       List<IEsptouchResult> resultList = mEsptouchTask.executeForResults(taskResultCount);
      //       IEsptouchResult firstResult = resultList.get(0);
      //       if (!firstResult.isCancelled()) {
      //         int count = 0;
      //         final int maxDisplayCount = taskResultCount;
      //         if (!firstResult.isSuc()) {
      //           callbackContext.error("No Device Found");
      //         }
      //       }
      //     }
      //   }
      // );
      return true;
    } else if (action.equals("stop")) {
      provisioner.stopProvisioning();
      provisioner.close()
      callbackContext.success();
    } else {
      callbackContext.error("can not find the function " + action);
    }
    return true;
  }

}
