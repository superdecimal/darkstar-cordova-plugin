
var exec = require("cordova/exec");
var serviceName = "DarkstarUploadServiceClient";
var DarkstarUploadService = function () {};

DarkstarUploadService.prototype.setToken = function (token, successCallback, failureCallback) {
  exec(successCallback, failureCallback, serviceName, 'setToken', [token]);
}

DarkstarUploadService.prototype.ping = function (text, successCallback, failureCallback) {
  exec(successCallback, failureCallback, serviceName, 'ping', [text]);
}

darkstaruploadserviceInst = new DarkstarUploadService();
module.exports = darkstaruploadserviceInst;
