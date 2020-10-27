package cse.mobile.sooinmedical.ui.contact;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.beardedhen.androidbootstrap.BootstrapButton;

import cse.mobile.sooinmedical.R;

public class ContactSooInParkActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_sooin_park);

        getSupportActionBar().setTitle("박수인");

        /* 박수인 휴대폰으로 전화하기 */
        BootstrapButton btCallSooinPark = findViewById(R.id.btCallSooinPark);
        btCallSooinPark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:010-5021-8207"));
                startActivity(intent);
            }
        });

        /* 박수인 휴대폰으로 메세지 보내기 */
        BootstrapButton btSMSToSooinPark = findViewById(R.id.btSMSToSooinPark);
        btSMSToSooinPark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.parse("smsto:01050218207"));
                startActivity(intent);
            }
        });

        /* 박수인으로 이메일 보내기 */
        BootstrapButton btEmailToSooinPark = findViewById(R.id.btEmailToSooinPark);
        btEmailToSooinPark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:tndlstndls2@naver.com"));
                intent.putExtra(Intent.EXTRA_SUBJECT, "[수인약품]문의 메일입니다.");
                intent.putExtra(Intent.EXTRA_TEXT, "\n\n\n\n수인약품 앱에서 보냈습니다.");
                startActivity(intent);
            }
        });
    }
}
