package com.example.otpaccess;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskExecutors;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthProvider;

import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import static java.util.concurrent.TimeUnit.SECONDS;

public class MainActivity extends AppCompatActivity {

    FirebaseAuth auth;

    PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;

    EditText mobileBox, otpBox;
    Button sendOtp, verifyOtp, signOutbtn;

    String verificationCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toast.makeText(getApplicationContext(), "otp verification app", Toast.LENGTH_SHORT).show();

        mobileBox = (EditText) findViewById(R.id.mobileno);
        otpBox = (EditText) findViewById(R.id.otpno);
        sendOtp = (Button) findViewById(R.id.sendbtn);
        verifyOtp = (Button) findViewById(R.id.verifybtn);
        signOutbtn = (Button) findViewById(R.id.signoutbtn);
        auth = FirebaseAuth.getInstance();


        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {

            }

            @Override
            public void onVerificationFailed(FirebaseException e) {
                Toast.makeText(getApplicationContext(), "something went wrong try again", Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                super.onCodeSent(s, forceResendingToken);
                verificationCode = s;

                Toast.makeText(getApplicationContext(), "code sent"+s, Toast.LENGTH_SHORT).show();
            }

        };

        sendOtp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendSms();
            }
        });

        verifyOtp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "verifying otp", Toast.LENGTH_SHORT).show();
                String otpNum = otpBox.getText().toString();
                PhoneAuthCredential credential = null;
                credential = PhoneAuthProvider.getCredential(verificationCode, otpNum);
                if(credential != null) {
                    signInWithPhone(credential);
                }
            }
        });

        signOutbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signOut();
            }
        });
    }



    public void sendSms() {
        String phoneNumber = "+91" + mobileBox.getText().toString();
        Toast.makeText(getApplicationContext(), phoneNumber, Toast.LENGTH_SHORT).show();
        PhoneAuthProvider.getInstance().verifyPhoneNumber(phoneNumber, 60, SECONDS, TaskExecutors.MAIN_THREAD, mCallbacks);
    }


    public void signInWithPhone(PhoneAuthCredential credential) {
        auth.signInWithCredential(credential)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()) {
                            Toast.makeText(getApplicationContext(), "sign in success", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    public void signOut() {
        auth.signOut();
        Toast.makeText(getApplicationContext(), "signout success", Toast.LENGTH_SHORT).show();
    }
}
