package com.example.paymentver2.Api;

import android.util.Log;

import com.example.paymentver2.Constant.AppInfo;
import com.example.paymentver2.Helper.Helpers;

import org.json.JSONObject;


import okhttp3.FormBody;
import okhttp3.RequestBody;

public class QueryRefundOrder {

    private static class QueryRefundOrderData {
        long currentTime = System.currentTimeMillis();
        String mRefundId;
        String appId;
        String timestamp;
        String mac;

        public QueryRefundOrderData(String mRefundId) throws Exception {
            this.mRefundId = mRefundId;
            this.appId = String.valueOf(AppInfo.APP_ID);
            this.timestamp = String.valueOf(currentTime);

            String inputMac = String.format("%s|%s|%s",
                    this.appId,
                    this.mRefundId,
                    this.timestamp
            );
            mac = Helpers.getMac(AppInfo.MAC_KEY, inputMac);
            Log.d("QueryRefundMac", mac);
            Log.d("QueryRefundId", mRefundId);
        }


    }

    public void queryRefund(String mRefundId) throws Exception {

        QueryRefundOrderData refundData = new QueryRefundOrderData(mRefundId);

        RequestBody formBody = new FormBody.Builder()
                .add("m_refund_id", refundData.mRefundId)
                .add("app_id", refundData.appId)
                .add("timestamp", refundData.timestamp)
                .add("mac", refundData.mac).build();

        JSONObject data = HttpProvider.sendPost(AppInfo.URL_REFUND_QUERY, formBody);
        Log.d("QueryRefundOrderResponse", data.toString());
        Log.d("QueryRefundOrderResponse", data.getString("return_message"));

    }

}
