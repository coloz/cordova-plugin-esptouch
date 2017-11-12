#import "esptouch.h"

@interface EspTouchDelegateImpl : NSObject<ESPTouchDelegate>
@property (nonatomic, strong) CDVInvokedUrlCommand *command;
@property (nonatomic, weak) id <CDVCommandDelegate> commandDelegate;

@end

@implementation EspTouchDelegateImpl

-(void) onEsptouchResultAddedWithResult: (ESPTouchResult *) result
{
    NSString *InetAddress=[ESP_NetUtil descriptionInetAddrByData:result.ipAddrData];
    NSString *text=[NSString stringWithFormat:@"bssid=%@,InetAddress=%@",result.bssid,InetAddress];
    CDVPluginResult* pluginResult = nil;
    pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK messageAsString: text];
    [pluginResult setKeepCallbackAsBool:true];
    //[self.commandDelegate sendPluginResult:pluginResult callbackId:self.command.callbackId];  //add by lianghuiyuan
}
@end


@implementation esptouch

- (void) start:(CDVInvokedUrlCommand *)command{
    [self._condition lock];
    NSString *apSsid = (NSString *)[command.arguments objectAtIndex:0];
    NSString *apBssid = (NSString *)[command.arguments objectAtIndex:1];
    NSString *apPwd = (NSString *)[command.arguments objectAtIndex:2];
    NSString *isSsidHiddenStr=(NSString *)[command.arguments objectAtIndex:3];
    
    BOOL isSsidHidden = true;
    if([isSsidHiddenStr compare:@"NO"]==NSOrderedSame){
        isSsidHidden=false;
    }
    int taskCount = (int)[command.arguments objectAtIndex:4];
    self._esptouchTask =
    [[ESPTouchTask alloc]initWithApSsid:apSsid andApBssid:apBssid andApPwd:apPwd andIsSsidHiden:isSsidHidden];
    EspTouchDelegateImpl *esptouchDelegate=[[EspTouchDelegateImpl alloc]init];
    esptouchDelegate.command=command;
    esptouchDelegate.commandDelegate=self.commandDelegate;
    [self._esptouchTask setEsptouchDelegate:esptouchDelegate];
    [self._condition unlock];
    NSArray * esptouchResultArray = [self._esptouchTask executeForResults:taskCount];
    [self.commandDelegate runInBackground:^{
    dispatch_queue_t  queue = dispatch_get_global_queue(DISPATCH_QUEUE_PRIORITY_DEFAULT, 0);
    dispatch_async(queue, ^{
        // show the result to the user in UI Main Thread
        dispatch_async(dispatch_get_main_queue(), ^{

            
            ESPTouchResult *firstResult = [esptouchResultArray objectAtIndex:0];
            // check whether the task is cancelled and no results received
            if (!firstResult.isCancelled)
            {
                //NSMutableString *mutableStr = [[NSMutableString alloc]init];
                //NSUInteger count = 0;
                // max results to be displayed, if it is more than maxDisplayCount,
                // just show the count of redundant ones
                //const int maxDisplayCount = 5;
                if ([firstResult isSuc])
                {
                    CDVPluginResult* pluginResult = nil;
                    pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK messageAsString: @"finished"];
                    [pluginResult setKeepCallbackAsBool:true];
                    [self.commandDelegate sendPluginResult:pluginResult callbackId:command.callbackId];
                }
                
                else
                {
                    CDVPluginResult* pluginResult = nil;
                    pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK messageAsString: @"Esptouch fail"];
                    [pluginResult setKeepCallbackAsBool:true];
                    [self.commandDelegate sendPluginResult:pluginResult callbackId:command.callbackId];
                }
            }
            
        });
    });
    }];
}


- (void) stop:(CDVInvokedUrlCommand *)command{
    [self._condition lock];
    if (self._esptouchTask != nil)
    {
        [self._esptouchTask interrupt];
    }
    [self._condition unlock];
    CDVPluginResult* pluginResult = nil;
    pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK messageAsString: @"cancel success"];
    [pluginResult setKeepCallbackAsBool:true];
    [self.commandDelegate sendPluginResult:pluginResult callbackId:command.callbackId];
}

@end