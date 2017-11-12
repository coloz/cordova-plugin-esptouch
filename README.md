# cordova-plugin-esptouch

##使用方法
```
declare var esptouch;

esptouch.start(ssid, "00:00:00:00", password, "NO", 1,
  res => { this.configComplete(res) },
  err => { this.configError(err) });
}

esptouch.stop(res => { console.log(res) }, err => { console.log(err) });

```