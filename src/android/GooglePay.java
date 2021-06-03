package com.plugin.googlepay;

import android.app.Activity;
import android.content.Intent;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.wallet.*;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.PluginResult;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

//import java.util.concurrent.Executor;

/**
 * Google Pay implementation for Cordova
 */
public class GooglePay extends CordovaPlugin {
    private static final int LOAD_PAYMENT_DATA_REQUEST_CODE = 991;
    private PaymentsClient paymentsClient;

    private CallbackContext callbackContext;

    @Override
    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
        JSONObject paymentData = args.getJSONObject(0);
        String environment = paymentData.getString("environment").toLowerCase();
        int env = environment.equals("test") ? WalletConstants.ENVIRONMENT_TEST : WalletConstants.ENVIRONMENT_PRODUCTION;

        Wallet.WalletOptions walletOptions = new Wallet.WalletOptions.Builder()
                .setEnvironment(env)
                .build();
        Activity activity = cordova.getActivity();

        paymentsClient = Wallet.getPaymentsClient(activity, walletOptions);

        if (action.equals("canMakePayments")) {
            this.canMakePayments(args, callbackContext);
            return true;
        }
        if (action.equals("makePaymentRequest")) {
            this.makePaymentRequest(args, callbackContext);
            return true;
        }
        return false;
    }

    /**
     * Handle a resolved activity from the Google Pay payment sheet.
     *
     * @param requestCode Request code originally supplied to AutoResolveHelper in requestPayment().
     * @param resultCode  Result code returned by the Google Pay API.
     * @param data        Intent from the Google Pay API containing payment or error data.
     * @see <a href="https://developer.android.com/training/basics/intents/result">Getting a result
     * from an Activity</a>
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // value passed in AutoResolveHelper
        if (requestCode != LOAD_PAYMENT_DATA_REQUEST_CODE) {
            return;
        }

        switch (resultCode) {

            case Activity.RESULT_OK:
                PaymentData paymentData = PaymentData.getFromIntent(data);
                String paymentInfo = paymentData.toJson();
                callbackContext.success(paymentInfo);
                break;
            case Activity.RESULT_CANCELED:
                callbackContext.error("Payment cancelled");
                break;

            case AutoResolveHelper.RESULT_ERROR:
                Status status = AutoResolveHelper.getStatusFromIntent(data);
                callbackContext.error(status.getStatusMessage());
                break;
        }
    }

    private void canMakePayments(JSONArray args, CallbackContext callbackContext) {

        try {
            JSONObject paymentData = args.getJSONObject(0);
            paymentData.remove("environment");

            // The call to isReadyToPay is asynchronous and returns a Task. We need to provide an
            // OnCompleteListener to be triggered when the result of the call is known.
            IsReadyToPayRequest request = IsReadyToPayRequest.fromJson(paymentData.toString());

            Task<Boolean> task = paymentsClient.isReadyToPay(request);

            task.addOnCompleteListener(cordova.getActivity(),
                    new OnCompleteListener<Boolean>() {
                        @Override
                        public void onComplete(Task<Boolean> task) {
                            callbackContext.sendPluginResult(new PluginResult(PluginResult.Status.OK, task.isSuccessful()));
                        }
                    });
        } catch (JSONException e) {
            callbackContext.error(e.getMessage());
        }

    }

    private void makePaymentRequest(JSONArray args, CallbackContext callbackContext) {
        Activity activity = cordova.getActivity();
        cordova.setActivityResultCallback(this);
        this.callbackContext = callbackContext;

        try {
            JSONObject paymentData = args.getJSONObject(0);
            paymentData.remove("environment");

            PaymentDataRequest request = PaymentDataRequest.fromJson(paymentData.toString());

            // Since loadPaymentData may show the UI asking the user to select a payment method, we use
            // AutoResolveHelper to wait for the user interacting with it. Once completed,
            // onActivityResult will be called with the result.
            if (request != null) {
                AutoResolveHelper.resolveTask(paymentsClient.loadPaymentData(request), activity, LOAD_PAYMENT_DATA_REQUEST_CODE);
            }

        } catch (JSONException e) {
            callbackContext.error(e.getMessage());
        }
    }
}
