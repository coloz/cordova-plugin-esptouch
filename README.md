# cordova-plugin-esptouch  
配合esp8266、esp32进行esptouch配网的cordova插件，使用Espressif最新的库，可用于ionic、cordova项目  
  
##支持平台  
ios  
android  
  
##参考引用  
https://github.com/EspressifApp/LibEsptouchForIOS  
https://github.com/EspressifApp/LibEsptouchForAndroid  
https://github.com/xumingxin7398/cordovaEsptouch

##使用方法  
  
```
cordova plugin add cordova-plugin-esptouch
```
  
```
declare var esptouch;

esptouch.start(ssid, "00:00:00:00", password, "NO", 1,
  res => { this.configComplete(res) },
  err => { this.configError(err) });
}

esptouch.stop(res => { console.log(res) }, err => { console.log(err) });

```


forked from https://github.com/t2wu/cordova-plugin-smartconfig  
