package com.dania.one;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.hardware.fingerprint.FingerprintManager;
import android.os.Build;
import android.os.CancellationSignal;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;

@RequiresApi(api = Build.VERSION_CODES.M)

public class FingerprintHandler extends FingerprintManager.AuthenticationCallback{

    private Context context;
    public FingerprintManager.CryptoObject F;

    public FingerprintHandler(Context context){

        this.context = context;

    }

    public void startAuth(FingerprintManager fingerprintManager, FingerprintManager.CryptoObject cryptoObject){
        CancellationSignal cancellationSignal = new CancellationSignal();
        fingerprintManager.authenticate(F, cancellationSignal, 0, this, null);
    }

    @Override
    public void onAuthenticationError(int errorCode, CharSequence errString) {
    }

    @Override
    public void onAuthenticationFailed() {
        this.update("Auth Failed.",false);
    }

    @Override
    public void onAuthenticationHelp(int helpCode, CharSequence helpString) {
        this.update("Error",false);

    }

    @Override
    public void onAuthenticationSucceeded(FingerprintManager.AuthenticationResult result) {
        this.update("Success",true);
        Intent intent = new Intent(context, Login.class);
        context.startActivity(intent);
        ((Activity)context).finish();
        //((Activity)context).overridePendingTransition(R.anim.fade_in,R.anim.fade_out);

    }

    public void update(String s, boolean b){

        TextView txt1 = (TextView) ((Activity)context).findViewById(R.id.txt1);
        txt1.setText(s);

        if (b == false){
            txt1.setTextColor(ContextCompat.getColor(context,R.color.colorMuladhara));
        }
        else {
            txt1.setTextColor(ContextCompat.getColor(context,R.color.colorAnahata));

        }


    }
}

