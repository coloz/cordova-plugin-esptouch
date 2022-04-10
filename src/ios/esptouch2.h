#import <Cordova/CDVPlugin.h>
#import <Cordova/CDVPluginResult.h>
#import "ESPPacketUtils.h"
#import "ESPProvisioner.h"
#import "ESPProvisioningParams.h"
#import "ESPProvisioningRequest.h"
#import "ESPProvisioningResult.h"
#import "ESPProvisioningUDP.h"

@interface esptouch2 : CDVPlugin
@property (atomic, strong) ESPProvisioner *provisioner;

- (void)start:(CDVInvokedUrlCommand*)command;

- (void)stop:(CDVInvokedUrlCommand*)command;

@end
