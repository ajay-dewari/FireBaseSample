package com.ajaysinghdewari.firebasesample;

import android.app.ProgressDialog;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class SignUpActivity extends AppCompatActivity {

    private Button mBtnSignUp;
    private EditText mTvEmail, mTvPwd;
    private ProgressDialog mProgressdialog;
    private FirebaseAuth mFirebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        mFirebaseAuth=FirebaseAuth.getInstance();

        mBtnSignUp = (Button) findViewById(R.id.btn_signup);
        mTvEmail = (EditText) findViewById(R.id.et_email);
        mTvPwd = (EditText) findViewById(R.id.et_pwd);

        mProgressdialog=new ProgressDialog(this);

    mBtnSignUp.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            signup();
        }
    });
    }

private void signup(){
    if(mTvEmail.getText().toString().isEmpty()){
        Toast.makeText(SignUpActivity.this,"Please Enter a valid e-mail id", Toast.LENGTH_LONG).show();
    return;
    }
    if(mTvPwd.getText().toString().isEmpty()){
        Toast.makeText(SignUpActivity.this,"Please Enter a valid password", Toast.LENGTH_LONG).show();
    return;
    }
    mProgressdialog.setMessage("Sign up..");
    mProgressdialog.show();
    mFirebaseAuth.createUserWithEmailAndPassword(mTvEmail.getText().toString(), mTvPwd.getText().toString()).addOnCompleteListener(SignUpActivity.this, new OnCompleteListener<AuthResult>() {
        @Override
        public void onComplete(@NonNull Task<AuthResult> task) {
            mProgressdialog.dismiss();
       if(task.isSuccessful()){
           Toast.makeText(SignUpActivity.this,"Signup Sucessufully, Acount Created", Toast.LENGTH_LONG).show();
       }else{
           Toast.makeText(SignUpActivity.this,"Unable to create account", Toast.LENGTH_LONG).show();
       }
        }

    });
}
}
