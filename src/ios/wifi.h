#import <Cordova/CDVPlugin.h>
#import <Cordova/CDVPluginResult.h>
#import "ESPTools.h"

@interface wifi : CDVPlugin

- (void)getConnectedInfo:(CDVInvokedUrlCommand*)command;

@end
