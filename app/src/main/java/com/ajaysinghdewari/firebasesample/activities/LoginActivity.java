package com.ajaysinghdewari.firebasesample.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.ajaysinghdewari.firebasesample.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    private Button mBtnSignIn;
    private EditText mEtEmail, mEtPwd;
    private TextView mTvOpenSignup;
    private ProgressDialog mProgressdialog;
    private FirebaseAuth mFirebaseAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mFirebaseAuth=FirebaseAuth.getInstance();

        if(mFirebaseAuth.getCurrentUser()!=null){
            finish();
            startActivity(new Intent(LoginActivity.this, HomeActivity.class));
        }
        mBtnSignIn = (Button) findViewById(R.id.btn_signin);
        mEtEmail = (EditText) findViewById(R.id.et_email);
        mEtPwd = (EditText) findViewById(R.id.et_pwd);
        mTvOpenSignup= (TextView) findViewById(R.id.tv_open_signup);
        mProgressdialog=new ProgressDialog(this);


        mTvOpenSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this, SignUpActivity.class));
            }
        });

        mBtnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                login();
            }
        });
    }

    private void login(){
        if(mEtEmail.getText().toString().isEmpty()){
            Toast.makeText(LoginActivity.this,"Please Enter a valid e-mail id", Toast.LENGTH_LONG).show();
            return ;
        }
        if(mEtPwd.getText().toString().isEmpty()){
            Toast.makeText(LoginActivity.this,"Please Enter a valid password", Toast.LENGTH_LONG).show();
            return ;
        }
        mProgressdialog.setMessage("Sign in..");
        mProgressdialog.show();
        mFirebaseAuth.signInWithEmailAndPassword(mEtEmail.getText().toString(), mEtPwd.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                mProgressdialog.dismiss();
                mEtEmail.setText("");
                mEtPwd.setText("");
                if(task.isSuccessful()){
                    Toast.makeText(LoginActivity.this,"Login Successfull", Toast.LENGTH_LONG).show();
                    startActivity(new Intent(LoginActivity.this, HomeActivity.class));
                }else{
                    Toast.makeText(LoginActivity.this,"Something went wrong", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}
