package com.example.paymentver2.Api;

import com.example.paymentver2.Constant.AppInfo;
import com.example.paymentver2.Helper.Helpers;

import org.json.JSONObject;

import okhttp3.FormBody;
import okhttp3.RequestBody;

public class QueryOrder {

    private static class QueryOrderData {
        String AppId;
        String AppTransId;
        String Mac;

        public QueryOrderData(String appTransId) throws Exception {
            AppId = String.valueOf(AppInfo.APP_ID);
            AppTransId =appTransId ;

            String inputMac = String.format("%s|%s|%s",
                    this.AppId,
                    this.AppTransId,
                    AppInfo.MAC_KEY

            );
            Mac = Helpers.getMac(AppInfo.MAC_KEY, inputMac);
        }
    }

    public JSONObject queryOrder(String appTransId) throws Exception {
        QueryOrderData queryOrderData = new QueryOrderData(appTransId);
        RequestBody formBody = new FormBody.Builder()
                .add("app_id", queryOrderData.AppId)
                .add("app_trans_id", queryOrderData.AppTransId)
                .add("mac", queryOrderData.Mac)
                .build();

        JSONObject data = HttpProvider.sendPost(AppInfo.URL_QUERY_STATUS, formBody);
        return data;
    }
}
