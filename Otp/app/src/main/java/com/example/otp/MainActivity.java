package com.example.otp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.FirebaseException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {
    EditText mn;
    Button btnotp;
    ProgressBar pbo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mn = findViewById(R.id.mn);
        btnotp = findViewById(R.id.btnotp);

        pbo = findViewById(R.id.pbo);

        btnotp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!mn.getText().toString().trim().isEmpty()){
                    if ((mn.getText().toString().trim().length()==10)){
                        pbo.setVisibility(View.VISIBLE);
                        btnotp.setVisibility(View.INVISIBLE);

                        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                                "+91" + mn.getText().toString(), 60,
                                TimeUnit.SECONDS, MainActivity.this,
                                new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                                    @Override
                                    public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                                        pbo.setVisibility(View.GONE);
                                        btnotp.setVisibility(View.VISIBLE);
                                    }

                                    @Override
                                    public void onVerificationFailed(@NonNull FirebaseException e) {
                                        pbo.setVisibility(View.GONE);
                                        btnotp.setVisibility(View.VISIBLE);
                                        Toast.makeText(MainActivity.this,e.getMessage(),Toast.LENGTH_SHORT).show();
                                    }

                                    @Override
                                    public void onCodeSent(@NonNull String backendOTP, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                                        super.onCodeSent(backendOTP, forceResendingToken);
                                        pbo.setVisibility(View.GONE);
                                        btnotp.setVisibility(View.VISIBLE);

                                        getIntent().putExtra("backendOTP",backendOTP);

                                        Intent intent= new Intent(MainActivity.this,verifyotp.class);
                                        intent.putExtra("mobile",mn.getText().toString());
                                        startActivity(intent);
                                    }
                                }
                        );


                    }else {
                        Toast.makeText(MainActivity.this,"Enter a valid number",Toast.LENGTH_SHORT).show();
                    }
                }else {
                    Toast.makeText(MainActivity.this,"Enter Mobile number",Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}