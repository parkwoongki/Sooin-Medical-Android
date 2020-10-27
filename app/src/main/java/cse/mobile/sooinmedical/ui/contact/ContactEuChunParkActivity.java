package cse.mobile.sooinmedical.ui.contact;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.beardedhen.androidbootstrap.BootstrapButton;

import cse.mobile.sooinmedical.R;

public class ContactEuChunParkActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_euchun_park);

        getSupportActionBar().setTitle("박의천");

        /* 박의천 휴대폰으로 전화하기 */
        BootstrapButton btCallEuChunPark = findViewById(R.id.btCallEuChunPark);
        btCallEuChunPark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:010-5448-6800"));
                startActivity(intent);
            }
        });

        /* 박의천 휴대폰으로 메세지 보내기 */
        BootstrapButton btSMSToEuChunPark = findViewById(R.id.btSMSToEuChunPark);
        btSMSToEuChunPark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.parse("smsto:01054486800"));
                startActivity(intent);
            }
        });
    }
}
