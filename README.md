# Cordova Google Pay integration
This plugin is built as unified method for obtaining payment tokens to forward it to payment processor (eg Adyen, Stripe, Wayforpay etc).

## Installation

```
cordova plugin add https://github.com/satya-venkat/cordova-plugin-google-pay
```

For Android, register and fill all required forms at https://pay.google.com/business/console. Add following to config.xml:

```
<widget ...>
  <preference name="GOOGLE_WALLET_SERVICES_VERSION" value="(google servises verison used)" />
  <preference name="SUPPORT_LIBRARY_VERSION" value="(support lib versions used)" />
  <config-file parent="/manifest/application" target="AndroidManifest.xml">
      <meta-data
              android:name="com.google.android.gms.wallet.api.enabled"
              android:value="true" />
  </config-file>
</widget>
```

## Usage

`canMakePayments()` checks whether device is capable to make payments via Apple Pay or Google Pay.

```
var requestObject = {
  "apiVersion": 2,
  "apiVersionMinor": 0,
  "environment": "TEST",
  "emailRequired": false,
  "merchantInfo": {
    "merchantName": "Example Merchant"
  },
  "allowedPaymentMethods": [
    {
      "type": "CARD",
      "parameters": {
        "allowedAuthMethods": [
          "PAN_ONLY",
          "CRYPTOGRAM_3DS"
        ],
        "allowedCardNetworks": [
          "AMEX",
          "DISCOVER",
          "INTERAC",
          "JCB",
          "MASTERCARD",
          "VISA"
        ],
        "billingAddressRequired": false
      },
      "tokenizationSpecification": {
        "type": "PAYMENT_GATEWAY",
        "parameters": {
          "gatewayMerchantId": "",
          "gateway": ""
        }
      }
    }
  ],
  "transactionInfo": {
    "currencyCode": "",
    "countryCode": "",
    "totalPriceStatus": "FINAL"
  }
}

GooglePay.canMakePayments(
  requestObject,
  callbackSuccess,
  callbackError,
);

```

`makePaymentRequest()` initiates pay session.

```
var requestObject = {
  "apiVersion": 2,
  "apiVersionMinor": 0,
  "environment": "TEST",
  "emailRequired": false,
  "merchantInfo": {
    "merchantName": "Example Merchant"
  },
  "allowedPaymentMethods": [
    {
      "type": "CARD",
      "parameters": {
        "allowedAuthMethods": [
          "PAN_ONLY",
          "CRYPTOGRAM_3DS"
        ],
        "allowedCardNetworks": [
          "AMEX",
          "DISCOVER",
          "INTERAC",
          "JCB",
          "MASTERCARD",
          "VISA"
        ],
        "billingAddressRequired": false
      },
      "tokenizationSpecification": {
        "type": "PAYMENT_GATEWAY",
        "parameters": {
          "gatewayMerchantId": "",
          "gateway": ""
        }
      }
    }
  ],
  "transactionInfo": {
    "totalPrice": "",
    "totalPriceLabel": "",
    "currencyCode": "",
    "countryCode": "",
    "totalPriceStatus": "FINAL"
  }
}

GooglePay.makePaymentRequest(
  requestObject,
  callbackSuccess,
  callbackError,
);
```

Essentially the request object is the same object we provide to the web client of goole payments
