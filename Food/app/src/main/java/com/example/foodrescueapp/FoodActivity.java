package com.example.foodrescueapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.internal.Constants;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.wallet.AutoResolveHelper;
import com.google.android.gms.wallet.CardRequirements;
import com.google.android.gms.wallet.IsReadyToPayRequest;
import com.google.android.gms.wallet.PaymentDataRequest;
import com.google.android.gms.wallet.PaymentMethodTokenizationParameters;
import com.google.android.gms.wallet.PaymentsClient;
import com.google.android.gms.wallet.TransactionInfo;
import com.google.android.gms.wallet.Wallet;
import com.google.android.gms.wallet.WalletConstants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.Arrays;
import java.util.Locale;
import java.util.Optional;

public class FoodActivity extends AppCompatActivity implements OnMapReadyCallback {
    private PaymentsClient mPaymentsClient;
    private Item item;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food);

        Intent intent = getIntent();
        item = (Item) intent.getSerializableExtra("Item");

        ImageView image = findViewById(R.id.image);
        TextView title = findViewById(R.id.title);
        TextView description = findViewById(R.id.description);
        TextView datetime = findViewById(R.id.datetime);
        TextView price = findViewById(R.id.price);
        TextView quantity = findViewById(R.id.quantity);
        TextView location = findViewById(R.id.location);

        image.setImageBitmap(BitmapFactory.decodeFile(getBaseContext().getFilesDir().toString() + File.separator + item.uri));
        title.setText(item.title);
        description.setText(String.format("Description: %s", item.description));
        datetime.setText(String.format("Date: %s", item.description));
        price.setText(String.format(Locale.getDefault(), "Price: %.2f", item.price));
        quantity.setText(String.format(Locale.getDefault(), "Quantity: %.2f", item.quantity));
        location.setText(String.format("Location: %s", item.location));

        mPaymentsClient = Wallet.getPaymentsClient(this, new Wallet.WalletOptions.Builder().setEnvironment(WalletConstants.ENVIRONMENT_TEST).build());
    }

    private void isReadyToPay() {
        IsReadyToPayRequest request = IsReadyToPayRequest.newBuilder()
                .addAllowedPaymentMethod(WalletConstants.PAYMENT_METHOD_CARD).addAllowedPaymentMethod(WalletConstants.PAYMENT_METHOD_TOKENIZED_CARD).build();
        Task<Boolean> task = mPaymentsClient.isReadyToPay(request);
        task.addOnCompleteListener(task1 -> {
            try {
                boolean result = task1.getResult(ApiException.class);
                if (result == true) {
                    // Show Google as payment option.

                } else {

                    // Hide Google as payment option.

                }

            } catch (ApiException ignored) {

            }

        });
    }

    private PaymentDataRequest createPaymentDataRequest() {
        PaymentDataRequest.Builder request = PaymentDataRequest.newBuilder()
                .setTransactionInfo(TransactionInfo.newBuilder()
                        .setTotalPriceStatus(WalletConstants.TOTAL_PRICE_STATUS_FINAL)
                        .setTotalPrice("10.00")
                        .setCurrencyCode("USD").build())
                .addAllowedPaymentMethod(WalletConstants.PAYMENT_METHOD_CARD)
                .addAllowedPaymentMethod(WalletConstants.PAYMENT_METHOD_TOKENIZED_CARD)
                .setCardRequirements(CardRequirements.newBuilder()
                        .addAllowedCardNetworks(Arrays.asList(

                                WalletConstants.CARD_NETWORK_AMEX,
                                WalletConstants.CARD_NETWORK_DISCOVER,
                                WalletConstants.CARD_NETWORK_VISA,
                                WalletConstants.CARD_NETWORK_MASTERCARD)
                        ).build());
        PaymentMethodTokenizationParameters params = PaymentMethodTokenizationParameters.newBuilder()
                .setPaymentMethodTokenizationType(
                        WalletConstants.PAYMENT_METHOD_TOKENIZATION_TYPE_PAYMENT_GATEWAY)
                .addParameter("gateway", "allpayments")
                .addParameter("gatewayMerchantId", "exampleGatewayMerchantId")
                .build();
        request.setPaymentMethodTokenizationParameters(params);

        return request.build();
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(-37.8503317, 145.1128872), 10));
    }

    public void onAdd(View view) {
        HomeActivity.items.add(item);
    }

    public void onPay(View view) {
        PaymentDataRequest request = createPaymentDataRequest();
        if (request != null) {
            AutoResolveHelper.resolveTask(mPaymentsClient.loadPaymentData(request), this, 0);
        }
    }
}