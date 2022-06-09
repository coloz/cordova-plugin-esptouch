#import <Cordova/CDVPlugin.h>
#import <Cordova/CDVPluginResult.h>
#import "ESPTouchTask.h"
#import "ESPTouchResult.h"
#import "ESP_NetUtil.h"
#import "ESPDataCode.h"
#import "ESPTouchDelegate.h"



@interface esptouch : CDVPlugin
@property (nonatomic, strong) NSCondition *_condition;
@property (atomic, strong) ESPTouchTask *_esptouchTask;

- (void)start:(CDVInvokedUrlCommand*)command;

- (void)stop:(CDVInvokedUrlCommand*)command;

@end