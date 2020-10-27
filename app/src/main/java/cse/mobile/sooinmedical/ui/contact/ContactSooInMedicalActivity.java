package cse.mobile.sooinmedical.ui.contact;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.beardedhen.androidbootstrap.BootstrapButton;

import cse.mobile.sooinmedical.R;

public class ContactSooInMedicalActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_sooin_medical);

        /* 수인약품 사무실 전화하기 */
        BootstrapButton btCallSoodinMedical = findViewById(R.id.btCallSoodinMedical);
        btCallSoodinMedical.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:02)475-4114"));
                startActivity(intent);
            }
        });

        /* 수인약품으로 이메일 보내기 */
        BootstrapButton btEmailToSooinMedical = findViewById(R.id.btEmailToSooinMedical);
        btEmailToSooinMedical.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse("mailto:sooin2011@naver.com");
                Intent intent = new Intent(Intent.ACTION_SENDTO, uri);
                intent.putExtra(Intent.EXTRA_SUBJECT, "[수인약품] 문의 메일입니다.");
                intent.putExtra(Intent.EXTRA_TEXT, "\n\n\n\n수인약품 앱에서 보냈습니다.");
                startActivity(intent);
            }
        });
    }
}
