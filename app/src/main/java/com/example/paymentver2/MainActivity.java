package com.example.paymentver2;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.paymentver2.Api.CreateOrder;
import com.example.paymentver2.Api.QueryOrder;
import com.example.paymentver2.Api.QueryRefundOrder;
import com.example.paymentver2.Api.RefundOrder;

import com.example.paymentver2.databinding.ActivityMainBinding;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    private String zp_trans_id, app_trans_id, m_refund_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        CreateOrder orderApi = new CreateOrder();
        QueryOrder query = new QueryOrder();
        RefundOrder refund = new RefundOrder();


        // cấp all quyền
        StrictMode.ThreadPolicy policy = new
                StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        // Handle tạo đơn hàng
        binding.btnCreateOrder.setOnClickListener(v -> {


            try {
                JSONObject data = orderApi.createOrder(binding.txtAmount.getText().toString(), orderApi);
                String code = data.getString("return_code");
                Toast.makeText(getApplicationContext(), "return_code: " + code, Toast.LENGTH_LONG).show();

                if (code.equals("1")) {
                    // Khởi tạo QR code từ order_url và hiển thị
                    String orderUrl = data.getString("order_url");
                    BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
                    Bitmap bitmap = barcodeEncoder.encodeBitmap(orderUrl, com.google.zxing.BarcodeFormat.QR_CODE, 400, 400);
                    binding.qrImageView.setImageBitmap(bitmap);
                    binding.qrImageView.setVisibility(View.VISIBLE); // Hiển thị mã QR

                }

            } catch (Exception e) {
                e.printStackTrace();
            }

        });

        binding.btnCheckStatus.setOnClickListener(v -> {

            String status;
            try {
                app_trans_id = orderApi.getApptransId();
                JSONObject data = query.queryOrder(app_trans_id);
                status = data.getString("return_message");
                zp_trans_id = data.getString("zp_trans_id");
                if (status.equalsIgnoreCase("Giao dịch thành công")) {
                    binding.btnCheckStatus.setVisibility(View.GONE);
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }

            binding.txtStatus.setText(status);
            if (status.equalsIgnoreCase("Giao dịch thành công")) {
                binding.txtDescription.setVisibility(View.VISIBLE);
            }


        });

        binding.btnRefund.setOnClickListener(v -> {

            try {
                refund.refund(zp_trans_id, binding.txtAmount.getText().toString(), binding.txtDescription.getText().toString());
                binding.txtDescription.setVisibility(View.GONE);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }

            m_refund_id = refund.getmRefundId();
        });


        binding.btnQueryRefund.setOnClickListener(v -> {

            QueryRefundOrder queryRefund = new QueryRefundOrder();
            try {
                queryRefund.queryRefund(m_refund_id);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }


        });

    }


}
