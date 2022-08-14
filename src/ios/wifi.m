#import "wifi.h"

@implementation wifi

- (void) getConnectedInfo:(CDVInvokedUrlCommand *)command{
    NSDictionary *wifiDic = @{
        @"ssid":ESPTools.getCurrentWiFiSsid,
        @"bssid":ESPTools.getCurrentBSSID,
        @"state":@"Connected"
    };

    CDVPluginResult *pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK messageAsDictionary:wifiDic];
    [self.commandDelegate sendPluginResult:pluginResult callbackId:command.callbackId];
}

@end
