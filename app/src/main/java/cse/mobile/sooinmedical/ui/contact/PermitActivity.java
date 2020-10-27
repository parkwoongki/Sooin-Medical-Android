package cse.mobile.sooinmedical.ui.contact;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import cse.mobile.sooinmedical.R;

public class PermitActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_permit);
        getSupportActionBar().setTitle("의약품 도매상 허가증");
    }
}
