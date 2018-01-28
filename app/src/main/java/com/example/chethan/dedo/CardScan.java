package com.example.chethan.dedo;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import io.card.payment.CardIOActivity;
import io.card.payment.CardType;
import io.card.payment.CreditCard;
import io.card.payment.i18n.StringKey;
import io.card.payment.i18n.SupportedLocale;
import io.card.payment.i18n.locales.LocalizedStringsList;

public class CardScan extends Activity {

    private static final int  REQUEST_SCAN = 101;
    private static final int REQUEST_AUTOTEST = 200;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cardscan);
        Button btnScan = findViewById(R.id.btnScan);
        btnScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CardScan.this, CardIOActivity.class)
                        .putExtra(CardIOActivity.EXTRA_REQUIRE_EXPIRY, true)
                        .putExtra(CardIOActivity.EXTRA_SCAN_EXPIRY, true)
                        .putExtra(CardIOActivity.EXTRA_REQUIRE_CVV, true)
                        .putExtra(CardIOActivity.EXTRA_REQUIRE_CARDHOLDER_NAME, true)
                        .putExtra(CardIOActivity.EXTRA_LANGUAGE_OR_LOCALE, "en")
                        .putExtra(CardIOActivity.EXTRA_GUIDE_COLOR, Color.GREEN)
                        .putExtra(CardIOActivity.EXTRA_RETURN_CARD_IMAGE, true);
                startActivityForResult(intent, REQUEST_SCAN);
            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if ((requestCode == REQUEST_SCAN || requestCode == REQUEST_AUTOTEST) && data != null && data.hasExtra(CardIOActivity.EXTRA_SCAN_RESULT))
        {
            ((TextView) findViewById(R.id.tvCardDetail)).setVisibility(View.VISIBLE);
            String resultDisplayStr;
            if (data != null && data.hasExtra(CardIOActivity.EXTRA_SCAN_RESULT))
            {
                CreditCard scanResult = data.getParcelableExtra(CardIOActivity.EXTRA_SCAN_RESULT);
// Never log a raw card number. Avoid displaying it, but if necessary use getFormattedCardNumber()
                resultDisplayStr = "Card Number: " + scanResult.getRedactedCardNumber() + "\n";
// Do something with the raw number, e.g.:
// myService.setCardNumber( scanResult.cardNumber );
                if (scanResult.isExpiryValid())
                {
                    resultDisplayStr += "Expiration Date: " + scanResult.expiryMonth + "/" + scanResult.expiryYear + "\n";
                }
                if (scanResult.cvv != null)
                {
// Never log or display a CVV
                    resultDisplayStr += "CVV has " + scanResult.cvv.length() + " digits.\n";
                }
                if (scanResult.postalCode != null)
                {
                    resultDisplayStr += "Postal Code: " + scanResult.postalCode + "\n";
                }
            }
            else
            {
                resultDisplayStr = "Scan was canceled.";
            }
            ((TextView) findViewById(R.id.tvCardDetail)).setText(resultDisplayStr);
        }
    }
}
