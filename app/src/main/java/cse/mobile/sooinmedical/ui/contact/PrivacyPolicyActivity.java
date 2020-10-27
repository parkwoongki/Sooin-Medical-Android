package cse.mobile.sooinmedical.ui.contact;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import cse.mobile.sooinmedical.R;

public class PrivacyPolicyActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_privacy_policy);

        getSupportActionBar().setTitle("개인정보처리방침");
    }
}
