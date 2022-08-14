#import "esptouch2.h"
#import "ESP_ByteUtil.h"

NSString *callback_ID2;

@implementation esptouch2

- (void)onProvisoningScanResult:(ESPProvisioningResult *)result {
    NSLog(@"onProvisonScanResult: address=%@, bssid=%@", result.address, result.bssid);
    [[NSOperationQueue mainQueue] addOperationWithBlock:^{
        NSDictionary *device =@{@"bssid":result.bssid,@"ip":result.address};
        CDVPluginResult *pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK messageAsDictionary: device];
        [pluginResult setKeepCallbackAsBool:true];
        [self.commandDelegate sendPluginResult:pluginResult callbackId:callback_ID2];
        // [self.resultArray addObject:result];
        // [self.resultView reloadData];
        // if ([self.request.deviceCount intValue] > 0) {
        //     // check result array size == expect size
        //     if (self.resultArray.count >= [self.request.deviceCount intValue]) {
        //         [self stopProvisioning:nil];
        //     }
        // }
    }];
}

- (void) start:(CDVInvokedUrlCommand *)command{
    [self.commandDelegate runInBackground:^{
        dispatch_queue_t queue = dispatch_get_global_queue(DISPATCH_QUEUE_PRIORITY_DEFAULT, 0);
        callback_ID2 = command.callbackId;

        NSString *apSsid = (NSString *)[command.arguments objectAtIndex:0];
        NSString *apPassword = (NSString *)[command.arguments objectAtIndex:1];
        NSString *customData = (NSString *)[command.arguments objectAtIndex:2];
        self.provisioner = [ESPProvisioner share];
        ESPProvisioningRequest *request = [[ESPProvisioningRequest alloc] init];
        request.ssid = [ESP_ByteUtil getBytesByNSString:apSsid];
        request.password = [ESP_ByteUtil getBytesByNSString:apPassword];
        request.reservedData = [ESP_ByteUtil getBytesByNSString:customData];
        // request.deviceCount = deviceCount;
        // request.aesKey = aesKey;
        [self.provisioner startProvisioning:request withDelegate:self];
    }];
}


- (void) stop:(CDVInvokedUrlCommand *)command{
    [self.provisioner stopProvisioning];
    CDVPluginResult* pluginResult = nil;
    pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK messageAsString: @"cancel success"];
    [pluginResult setKeepCallbackAsBool:true];
    [self.commandDelegate sendPluginResult:pluginResult callbackId:command.callbackId];
}

@end