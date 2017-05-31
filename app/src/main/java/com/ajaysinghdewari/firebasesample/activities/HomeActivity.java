package com.ajaysinghdewari.firebasesample.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.ajaysinghdewari.firebasesample.R;
import com.ajaysinghdewari.firebasesample.models.UserInformation;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class HomeActivity extends AppCompatActivity {

    private TextView mTextMessage, mTvWelcomeMsg;
    private EditText mEtName, mEtAddress;
    private Button mBtnSave;
    private FirebaseAuth mFireBaseAuth;
    private DatabaseReference mDatabaseReferance;
    private final static int PICK_IMAGE=121;
    private StorageReference mStorageRef;
    private Uri file;
    private Button mBtnUpload;


    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    mTextMessage.setText(R.string.title_home);
                    return true;
                case R.id.navigation_dashboard:
                    mTextMessage.setText(R.string.title_dashboard);
                    return true;
                case R.id.navigation_notifications:
                    mTextMessage.setText(R.string.title_notifications);
                    return true;
            }
            return false;
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        mTextMessage = (TextView) findViewById(R.id.btn_logout);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        mFireBaseAuth=FirebaseAuth.getInstance();
        mDatabaseReferance= FirebaseDatabase.getInstance().getReference();
        mStorageRef = FirebaseStorage.getInstance().getReference();

        mTvWelcomeMsg= (TextView) findViewById(R.id.tv_welcome);
        mEtAddress= (EditText) findViewById(R.id.et_address);
        mEtName= (EditText) findViewById(R.id.et_name);
        mBtnSave = (Button) findViewById(R.id.btn_save);
/*==============file upload code===========*/

        mBtnUpload= (Button) findViewById(R.id.btn_upload);
        mBtnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent, PICK_IMAGE);
            }
        });
/*==============file upload code===========*/

        if(mFireBaseAuth.getCurrentUser()!=null){
            FirebaseUser user=mFireBaseAuth.getCurrentUser();
            mTvWelcomeMsg.setText("Welcome "+user.getEmail());
        }

        mTextMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mFireBaseAuth.signOut();
                finish();
            }
        });

        mBtnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveUserInfo();
            }
        });
    }

private void saveUserInfo(){
    String name=mEtName.getText().toString();
    String address=mEtAddress.getText().toString();

    if(name.isEmpty() || name==null){
        Toast.makeText(this, "Please enter a valid name", Toast.LENGTH_LONG).show();
        return;
    }

    if(address.isEmpty() || address==null){
        Toast.makeText(this, "address can't be empty", Toast.LENGTH_LONG).show();
        return;
    }
    UserInformation userInfo=new UserInformation(name, address);

    //now we will create a child of the user to database

    FirebaseUser user=mFireBaseAuth.getCurrentUser();
    mDatabaseReferance.child(user.getUid()).setValue(userInfo);

    Toast.makeText(this, "Info is saved", Toast.LENGTH_LONG).show();
}

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==PICK_IMAGE && resultCode==RESULT_OK && data.getData()!=null){
            file=data.getData();
            uploadFile();
        }
    }

    private void uploadFile(){

        if(file!=null){
            final ProgressDialog progressDialog=new ProgressDialog(this);
            progressDialog.setTitle("Uploading");
            progressDialog.show();
            StorageReference imgStorageRef=mStorageRef.child("images/photo.jpg");

            imgStorageRef.putFile(file)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            // Get a URL to the uploaded content
    //                        Uri downloadUrl = taskSnapshot.getDownloadUrl();
                            progressDialog.dismiss();
                            Toast.makeText(HomeActivity.this, "SUCESS", Toast.LENGTH_LONG).show();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(Exception exception) {
                            // Handle unsuccessful uploads
                            // ...
                            progressDialog.dismiss();
                            Toast.makeText(HomeActivity.this, "ERROR", Toast.LENGTH_LONG).show();
                        }
                    }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                    double progress=(100.0*taskSnapshot.getBytesTransferred())/taskSnapshot.getTotalByteCount();
                    progressDialog.setMessage((int)progress+"% uploaed");
                }
            });
        }else{
            Toast.makeText(HomeActivity.this, "Please select an image first", Toast.LENGTH_LONG).show();
        }

    }
}
