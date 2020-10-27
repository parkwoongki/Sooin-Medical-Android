package cse.mobile.sooinmedical.ui.contact;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import cse.mobile.sooinmedical.R;

public class LicenseActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_license);

        getSupportActionBar().setTitle("오픈소스 라이선스");
    }
}
