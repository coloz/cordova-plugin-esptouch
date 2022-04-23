package com.coloz.wifi;

import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.net.wifi.SupplicantState;
import android.util.Log;

import com.espressif.iot.esptouch2.provision.TouchNetUtil;

import java.net.InetAddress;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaPlugin;

import org.apache.cordova.*;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class wifi extends CordovaPlugin {

    private CallbackContext wifiCallbackContext;
    private static final String TAG = "wifi";
    private WifiManager mWifiManager;

    protected void getConnectedInfo() {
        JSONObject result = new JSONObject();
        WifiInfo wifiInfo = mWifiManager.getConnectionInfo();
        try {
            if (!TouchNetUtil.isWifiConnected(mWifiManager)) {
                result.put("state", "NotConnected");
                wifiCallbackContext.error(result);
                return;
            }
            if (!wifiInfo.getSupplicantState().equals(SupplicantState.COMPLETED)) {
                result.put("state", "Connecting");
                wifiCallbackContext.error(result);
                return;
            }
            String ssid = TouchNetUtil.getSsidString(wifiInfo);
            InetAddress ip;
            int ipValue = wifiInfo.getIpAddress();
            if (ipValue != 0) {
                ip = TouchNetUtil.getAddress(wifiInfo.getIpAddress());
            } else {
                ip = TouchNetUtil.getIPv4Address();
                if (ip == null) {
                    ip = TouchNetUtil.getIPv6Address();
                }
            }
            result.put("ip", ip);
            result.put("is5G", TouchNetUtil.is5G(wifiInfo.getFrequency()));
            result.put("ssid", ssid);
            result.put("bssid", wifiInfo.getBSSID());
            result.put("state", "Connected");
            wifiCallbackContext.success(result);
        } catch (JSONException e) {
            Log.e(TAG, "unexpected JSON exception", e);
        }
    }

    @Override
    public void initialize(CordovaInterface cordova, CordovaWebView webView) {
        super.initialize(cordova, webView);
        mWifiManager = (WifiManager) cordova.getActivity().getApplicationContext()
                .getSystemService(Context.WIFI_SERVICE);
    }

    @Override
    public boolean execute(String action, final JSONArray args, final CallbackContext callbackContext)
            throws JSONException {
        wifiCallbackContext = callbackContext;

        if (action.equals("getConnectedInfo")) {
            getConnectedInfo();
        } else if (action.equals("scan")) {

            callbackContext.success();
        } else if (action.equals("connect")) {

            callbackContext.success();
        } else if (action.equals("disconnect")) {

            callbackContext.success();
        } else {
            callbackContext.error("can not find the function " + action);
        }
        return true;
    }
}