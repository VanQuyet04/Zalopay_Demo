package com.example.paymentver2.Api;

import android.util.Log;

import com.example.paymentver2.Constant.AppInfo;
import com.example.paymentver2.Helper.Helpers;

import org.json.JSONObject;

import java.util.Date;

import okhttp3.FormBody;
import okhttp3.RequestBody;

public class CreateOrder {


    private static class CreateOrderData {
        String AppId;
        String AppUser;
        String AppTime;
        String Amount;
        String AppTransId;
        String EmbedData;
        String Items;
        String BankCode;
        String Description;
        String Mac;

        private CreateOrderData(String amount) throws Exception {
            long appTime = new Date().getTime();
            AppId = String.valueOf(AppInfo.APP_ID);
            AppUser = "Android_Demo";
            AppTime = String.valueOf(appTime);
            Amount = amount;
            AppTransId = Helpers.getAppTransId();
            EmbedData = "{}";
            Items = "[]";
            BankCode = "zalopayapp";
            Description = "Merchant pay for order #" + Helpers.getAppTransId();
            String inputHMac = String.format("%s|%s|%s|%s|%s|%s|%s",
                    this.AppId,
                    this.AppTransId,
                    this.AppUser,
                    this.Amount,
                    this.AppTime,
                    this.EmbedData,
                    this.Items);
            Mac = Helpers.getMac(AppInfo.MAC_KEY, inputHMac);

        }

    }

    public JSONObject createOrder(String amount) throws Exception {

        CreateOrderData orderData = new CreateOrderData(amount);
        RequestBody formBody = new FormBody.Builder()
                .add("app_id", orderData.AppId)
                .add("app_user", orderData.AppUser)
                .add("app_time", orderData.AppTime)
                .add("amount", orderData.Amount)
                .add("app_trans_id", orderData.AppTransId)
                .add("embed_data", orderData.EmbedData)
                .add("item", orderData.Items)
                .add("bank_code", orderData.BankCode)
                .add("description", orderData.Description)
                .add("mac", orderData.Mac)
                .build();
        JSONObject data = HttpProvider.sendPost(AppInfo.URL_CREATE_ORDER, formBody);
        Log.d("CreateOrderResponse", data.toString());
        return data;

    }


}

