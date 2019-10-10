# cordova-plugin-esptouch  
配合esp8266、esp32进行esptouch配网的cordova插件，使用Espressif最新的库，可用于ionic、cordova项目  

# espTouch lib版本  
android：0.3.7.0  
ios：0.3.7.0  
  
## 支持平台  
ios 12.x 
android 5+ 

## 参考引用  
https://github.com/EspressifApp/LibEsptouchForIOS  
https://github.com/EspressifApp/LibEsptouchForAndroid  
https://github.com/EspressifApp/EsptouchForAndroid  
https://github.com/t2wu/cordova-plugin-smartconfig  
https://github.com/xumingxin7398/cordovaEsptouch  

## 使用方法  
### 安装  
cordova-android>7.0.0
```
cordova plugin add cordova-plugin-esptouch
```
cordova-android<7.0.0
```
cordova plugin add cordova-plugin-esptouch@1.0.3
```

### 调用  
```javascript
declare var esptouch;

esptouch.start(ssid, "00:00:00:00:00:00", password, "1", "1", 
  res => { console.log(res) },
  err => { console.log(err) });
}

esptouch.stop(res => { console.log(res) }, err => { console.log(err) });

```
### 输出  
```json
{"bssid":"ffffffffffff","ip":"192.168.1.123"}
```

### 已知问题  
当环境中有太多AP信号时，可能配网失败，但这不是本插件的问题  

## 网络乞讨  
如果你觉得该项目不错，可以打个star支持下  

## 技术支持  
提供cordova/ionic开发、ESP8266开发技术支持服务，300元/每小时  
联系邮箱：clz@clz.me  
