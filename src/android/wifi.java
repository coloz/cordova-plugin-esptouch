package com.coloz.wifi;

import android.Manifest;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;

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

    private WifiManager mWifiManager;

    protected void getConnectedInfo() {
        JSONObject result = new JSONObject();
        WifiInfo wifiInfo = mWifiManager.getConnectionInfo();
        boolean connected = TouchNetUtil.isWifiConnected(mWifiManager);
        if (!connected) {
            result.put("message", getString(R.string.esptouch_message_wifi_connection));
            wifiCallbackContext.error(result);
        }
        String ssid = TouchNetUtil.getSsidString(wifiInfo);
        String ip;
        int ipValue = wifiInfo.getIpAddress();
        if (ipValue != 0) {
            ip = TouchNetUtil.getAddress(wifiInfo.getIpAddress());
        } else {
            ip = TouchNetUtil.getIPv4Address();
            if (ip == null) {
                ip = TouchNetUtil.getIPv6Address();
            }
        }
        result.put("address", ip);
        result.put("is5G", TouchNetUtil.is5G(wifiInfo.getFrequency()));
        result.put("ssid", ssid);
        result.put("bssid", wifiInfo.getBSSID());
        wifitouchCallbackContext.success(result);
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