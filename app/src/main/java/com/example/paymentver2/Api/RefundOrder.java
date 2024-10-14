package com.example.paymentver2.Api;

import android.annotation.SuppressLint;
import android.util.Log;

import com.example.paymentver2.Constant.AppInfo;
import com.example.paymentver2.Helper.Helpers;

import org.json.JSONObject;

import okhttp3.FormBody;
import okhttp3.RequestBody;

public class RefundOrder {
    private String fake_RefundId;

    public String getmRefundId() {
        return fake_RefundId;
    }

    private static class RefundOrderData {
        String mRefundId;
        String appId;
        String zpTransId;
        String amount;
        String timestamp;
        String mac;
        String description;


        // constructor
        public RefundOrderData(String zpTransId, String amount, String description) throws Exception {
            long currentTimeMillis = System.currentTimeMillis();
            this.mRefundId = generateRefundId(); // Hàm tự tạo mã hoàn tiền
            this.appId = String.valueOf(AppInfo.APP_ID);
            this.zpTransId = zpTransId;
            this.amount = amount;
            this.timestamp = String.valueOf(currentTimeMillis);
            this.description = description;

            // Tạo chuỗi để tính toán MAC
            String inputMac = String.format("%s|%s|%s|%s|%s",
                    this.appId,
                    this.zpTransId,
                    this.amount,
                    this.description,
                    this.timestamp);

            // Tính toán MAC
            mac = Helpers.getMac(AppInfo.MAC_KEY, inputMac);
            Log.d("RefundId", mRefundId);

        }

        // Hàm để tự tạo mã hoàn tiền
        private String generateRefundId() {
            // Định dạng: yymmdd_appid_xxxxxxxxxx
            long currentTime = System.currentTimeMillis();
            @SuppressLint("DefaultLocale") String datePart = String.format("%tY%<tm%<td", currentTime).substring(2); // Lấy 2 ký tự đầu năm, tháng, ngày
            String appIdPart = String.valueOf(AppInfo.APP_ID);
            @SuppressLint("DefaultLocale") String randomPart = String.format("%010d", (int) (Math.random() * 1000000000)); // Random 10 ký tự
            return datePart + "_" + appIdPart + "_" + randomPart;
        }
    }

    public void refund(String zpTransId, String amount, String description) throws Exception {
        // Biến giữ đối tượng RefundOrderData
        RefundOrderData refundData = new RefundOrderData(zpTransId, amount, description);

        // Tạo request body cho yêu cầu hoàn tiền
        RequestBody formBody = new FormBody.Builder()
                .add("m_refund_id", refundData.mRefundId)
                .add("app_id", refundData.appId)
                .add("zp_trans_id", refundData.zpTransId)
                .add("amount", refundData.amount)
                .add("timestamp", refundData.timestamp)
                .add("mac", refundData.mac)
                .add("description", refundData.description)
                .build();

        // Gửi request đến API hoàn tiền
        JSONObject data = HttpProvider.sendPost(AppInfo.URL_REFUND, formBody);
        Log.d("RefundOrderResponse", data.toString());
        fake_RefundId = refundData.mRefundId;
    }
}

