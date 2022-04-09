# cordova-plugin-esptouch-v2  
**这是esptouch v2版本，和esptouch v1不兼容**  
**当前版本只支持android，ios的空了再写**  
配合esp8266、esp32进行esptouch v2配网的cordova插件，使用Espressif esptouch v2最新的库，可用于ionic、cordova项目  

如果需要使用esptouch v1，请访问[v1分支](https://github.com/coloz/cordova-plugin-esptouch/tree/v1)  


# espTouch v2 lib版本  
android：2.1.0  
ios：2.1.0  
  
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
cordova plugin add cordova-plugin-esptouch-v2
```

### 调用  
```javascript
declare var esptouch2;

esptouch2.start(ssid, password, customData, 
  res => { console.log(res) },
  err => { console.log(err) });
}

esptouch2.stop(res => { console.log(res) }, err => { console.log(err) });

```
### 输出  
```json
{"bssid":"ffffffffffff","ip":"192.168.1.123"}
```

## 网络乞讨  
如果你觉得该项目不错，可以打个star支持下  

## 技术支持  
提供cordova/ionic开发、ESP8266开发技术支持服务，300元/每小时  
联系邮箱：clz@clz.me  
