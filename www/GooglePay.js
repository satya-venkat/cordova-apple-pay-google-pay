var exec = require('cordova/exec');
var executeCallback = function(callback, message) {
    if (typeof callback === 'function') {
        callback(message);
    }
};


/**
 * Determines if the current device supports Apple Pay and has a supported card installed.
 * @param {object} paymentDataRequest
 * @param {Function} [successCallback] - Optional success callback, recieves message object.
 * @param {Function} [errorCallback] - Optional error callback, recieves message object.
 * @returns {Promise}
 */
exports.canMakePayments = function(paymentDataRequest, successCallback, errorCallback) {
    return new Promise(function(resolve, reject) {
        exec(function(message) {
            executeCallback(successCallback, message);
            resolve(message);
        }, function(message) {
            executeCallback(errorCallback, message);
            reject(message);
        }, 'GooglePay', 'canMakePayments', [paymentDataRequest]);
    });
};

/**
 * Opens the Apple Pay sheet and shows the order information.
 * @param {object} paymentDataRequest
 * @param {Function} [successCallback] - Optional success callback, recieves message object.
 * @param {Function} [errorCallback] - Optional error callback, recieves message object.
 * @returns {Promise}
 */
exports.makePaymentRequest = function(paymentDataRequest, successCallback, errorCallback) {
    return new Promise(function(resolve, reject) {
        exec(function(message) {
            executeCallback(successCallback, message);
            resolve(message);
        }, function(message) {
            executeCallback(errorCallback, message);
            reject(message);
        }, 'GooglePay', 'makePaymentRequest', [paymentDataRequest]);
    });
};
