var exec = require('cordova/exec');

exports.coolMethod = function (arg0, success, error) {
	exec(success, error, 'WebJsPlugin', 'coolMethod', [arg0]);
};

exports.getServiceVersion = function (successCallback, errorCallback) {
	exec(successCallback, errorCallback, 'WebJsPlugin', 'getServiceVersion', []);
};

exports.printer_status = function (successCallback, errorCallback, printer_id) {
	exec(successCallback, errorCallback, 'WebJsPlugin', 'printer_status', [printer_id]);
};

exports.printer_setparam = function (successCallback, errorCallback, printer_id, param_id, param1) {
	exec(successCallback, errorCallback, 'WebJsPlugin', 'printer_setparam', [printer_id, param_id, param1]);
};

exports.printer_feedline = function (successCallback, errorCallback, printer_id, pixel) {
	exec(successCallback, errorCallback, 'WebJsPlugin', 'printer_feedline', [printer_id, pixel]);
};

exports.printer_drawhtml = function (successCallback, errorCallback, printer_id, html, dummy_print, saveBmp) {
	exec(successCallback, errorCallback, 'WebJsPlugin', 'printer_drawhtml', [printer_id, html, dummy_print, saveBmp]);
};