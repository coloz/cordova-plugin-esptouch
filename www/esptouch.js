var exec = require('cordova/exec');

module.exports = {
  start: function (apSsid, apPassword, successCallback, failCallback) {
    exec(successCallback, failCallback, "esptouch", "start", [apSsid, apPassword]);
  },
  stop: function (successCallback, failCallback) {
    exec(successCallback, failCallback, "esptouch", "stop", []);
  }
}