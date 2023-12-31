# cordova-plugin-esptouch 2.0
配合esp8266、esp32进行esptouch配网的cordova插件，集成了esptouch V1、V2, 并提供配网时必要的wifi检查功能，可用于ionic、cordova项目。  

本程序不在维护，新版本为[capacitor-esptouch](https://github.com/coloz/capacitor-esptouch)  
  
## 支持平台  
ios 12.x  
android 5+  

## 参考引用  
https://github.com/EspressifApp/EsptouchForAndroid  
https://github.com/EspressifApp/lib-esptouch-v2-android  
https://github.com/EspressifApp/EsptouchForIOS  
https://github.com/EspressifApp/EsptouchForIOS/tree/master/EspTouchDemo/ESPTouchV2  

## 使用方法  
### 安装  
```
cordova plugin add cordova-plugin-esptouch
```

### 使用方法 
#### WiFi相关检查  
```javascript
declare var wifi;

wifi.checkLocation( 
  result => { 
    if(!result) console.log('未开启位置服务')
  }
);

wifi.getConnectedInfo(
  result => { 
    console.log(result)
    /*
    {
      ip:'192.186.1.111'  // 这里不知道为啥多了个/，自己处理下
      is5G: false
      ssid: 'test'
      bssid:'00:00:00:00:00:00'
      state:'Connected'
    }
    */
    if(result.is5G) console.log('当前设备仅支持2.4G热点接入，请更换热点')
  }, 
  error => { 
    console.log(error) 
    if(error.state=='NotConnected') console.log('WiFi未连接');
    else if(error.state=='Connecting') console.log('WiFi连接中');
  }
)

```
#### esptouch v2
```javascript
declare var esptouch2;

esptouch2.start(ssid, password, customData, 
  result => { console.log(result) }, 
  error => { console.log(error) }
}

esptouch2.stop(
  result => { console.log(result) }, // {"bssid":"ffffffffffff","ip":"192.168.1.123"}
  error => { console.log(error) }
);

```
#### esptouch v1
```javascript
declare var esptouch;

esptouch.start(ssid, password, 
  result => { console.log(result) }, 
  error => { console.log(error) }
}

esptouch.stop(
  result => { console.log(result) }, // {"bssid":"ffffffffffff","ip":"192.168.1.123"}
  error => { console.log(error) }
);
```


### 输出  
```json
{"bssid":"ffffffffffff","ip":"192.168.1.123"}
```

## 网络乞讨  
如果你觉得该项目不错，可以打个star支持下  

## 技术支持  
提供cordova/ionic开发、ESP8266/ESP32开发技术支持服务，300元/每小时  
联系邮箱：clz@clz.me  
