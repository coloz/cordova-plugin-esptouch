var exec = require('cordova/exec');

module.exports = {
  checkLocation: function (successCallback, failCallback) {
    exec(successCallback, failCallback, "wifi", "checkLocation");
  },
  getConnectedInfo: function (successCallback, failCallback) {
    exec(successCallback, failCallback, "wifi", "getConnectedInfo");
  },
  scan: function (options, successCallback, failCallback) {
    exec(successCallback, failCallback, "wifi", "scan", [options]);
  },
  connect: function (ssid, password, successCallback, failCallback) {
    exec(successCallback, failCallback, "wifi", "connect", [ssid, password]);
  },
  disconnect: function (ssid, successCallback, failCallback) {
    exec(successCallback, failCallback, "wifi", "disconnect", [ssid]);
  }
}