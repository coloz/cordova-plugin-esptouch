#import <Cordova/CDVPlugin.h>
#import <Cordova/CDVPluginResult.h>
#import "esptouch/ESPTouchTask.h"
#import "esptouch/ESPTouchResult.h"
#import "esptouch/ESP_NetUtil.h"
#import "esptouch/ESPTouchDelegate.h"



@interface esptouch : CDVPlugin
@property (nonatomic, strong) NSCondition *_condition;
@property (atomic, strong) ESPTouchTask *_esptouchTask;

- (void)start:(CDVInvokedUrlCommand*)command;

- (void)stop:(CDVInvokedUrlCommand*)command;

@end
