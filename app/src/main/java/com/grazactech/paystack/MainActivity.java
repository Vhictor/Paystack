package com.grazactech.paystack;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import co.paystack.android.Paystack;
import co.paystack.android.PaystackSdk;
import co.paystack.android.Transaction;
import co.paystack.android.model.Card;
import co.paystack.android.model.Charge;

public class MainActivity extends AppCompatActivity {
    Button checkOut;
    Charge charge;
    ProgressDialog dialog;
    String publicKey = "pk_test_3a1212590ccea72b3a0413a83dcbd212a039b16b";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        PaystackSdk.initialize(getApplicationContext());
        PaystackSdk.setPublicKey(publicKey);

        checkOut = findViewById(R.id.checkout);



        checkOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                performCharge();
            }
        });
    }

    String cardNumber = "5060666666666666666";
    int expiryMonth = 12;
    int expiryYear = 21;
    String cvv = "123";
    Card card = new Card(cardNumber,expiryMonth,expiryYear,cvv);
    Boolean checkCard = card.isValid();

    private void performCharge(){
        if (checkCard == true){
            dialog = new ProgressDialog(this);
            dialog.setCancelable(false);
            dialog.setMessage("Please wait...");
            dialog.show();
            charge = new Charge();
            charge.setCard(card);

            charge.setAmount(100);
            charge.setEmail("grazactech@gmail.com");
            PaystackSdk.chargeCard(MainActivity.this, charge, new Paystack.TransactionCallback() {
                @Override
                public void onSuccess(Transaction transaction) {
                    String paymentReference = transaction.getReference();
                    Toast.makeText(MainActivity.this, "Transaction successful " +paymentReference , Toast.LENGTH_SHORT).show();
                    dialog.hide();
                }

                @Override
                public void beforeValidate(Transaction transaction) {

                }

                @Override
                public void onError(Throwable error, Transaction transaction) {
                    String paymentReference = transaction.getReference();
                    Toast.makeText(MainActivity.this, "Transaction unsuccessful"+ paymentReference.toString(), Toast.LENGTH_SHORT).show();
                    dialog.hide();
                }
            });
        }
        else {
            Toast.makeText(this, "Incorrect data", Toast.LENGTH_SHORT).show();
        }
    }

}
