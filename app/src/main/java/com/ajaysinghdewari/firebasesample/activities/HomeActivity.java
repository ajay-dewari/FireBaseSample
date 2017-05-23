package com.ajaysinghdewari.firebasesample.activities;

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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class HomeActivity extends AppCompatActivity {

    private TextView mTextMessage, mTvWelcomeMsg;
    private EditText mEtName, mEtAddress;
    private Button mBtnSave;
    private FirebaseAuth mFireBaseAuth;
    private DatabaseReference mDatabaseReferance;


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

        mTvWelcomeMsg= (TextView) findViewById(R.id.tv_welcome);
        mEtAddress= (EditText) findViewById(R.id.et_address);
        mEtName= (EditText) findViewById(R.id.et_name);
        mBtnSave = (Button) findViewById(R.id.btn_save);

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
    mDatabaseReferance.child(user.getUid()).setValue(user);

    Toast.makeText(this, "Info is saved", Toast.LENGTH_LONG).show();
}

}
