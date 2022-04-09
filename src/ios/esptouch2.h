#import <Cordova/CDVPlugin.h>
#import <Cordova/CDVPluginResult.h>
#import "esptouch2/ESPPacketUtils.h"
#import "esptouch2/ESPProvisioner.h"
#import "esptouch2/ESPProvisioningParams.h"
#import "esptouch2/ESPProvisioningRequest.h"
#import "esptouch2/ESPProvisioningResult.h"
#import "esptouch2/ESPProvisioningUDP.h"

@interface esptouch : CDVPlugin
@property (nonatomic, strong) NSCondition *_condition;
@property (atomic, strong) ESPTouchTask *_esptouchTask;

- (void)start:(CDVInvokedUrlCommand*)command;

- (void)stop:(CDVInvokedUrlCommand*)command;

@end
