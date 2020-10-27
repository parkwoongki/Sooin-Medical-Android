package cse.mobile.sooinmedical.ui.contact;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import cse.mobile.sooinmedical.R;

public class DeveloperInfoActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_developer_info);
        getSupportActionBar().setTitle("개발자 정보");
    }
}
