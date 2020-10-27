package cse.mobile.sooinmedical.ui.contact;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.beardedhen.androidbootstrap.BootstrapButton;
import com.google.firebase.auth.FirebaseAuth;

import cse.mobile.sooinmedical.EmailPasswordActivity;
import cse.mobile.sooinmedical.R;

public class LogInfoActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_loginfo);

        /* 로그인 정보 가져오기 */
        mAuth = FirebaseAuth.getInstance();

        TextView tvUserEmail = findViewById(R.id.tvUserEmail);
        tvUserEmail.setText(mAuth.getCurrentUser().getEmail());

        TextView tvUserDisplayName = findViewById(R.id.tvUserDisplayName);
        tvUserDisplayName.setText(mAuth.getCurrentUser().getDisplayName());

        BootstrapButton btLogOut = findViewById(R.id.btLogOut);
        btLogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.signOut();
                Intent intent = new Intent(getApplicationContext(), EmailPasswordActivity.class);
                intent.putExtra("Logout", true);
                startActivity(intent);
                finish();
            }
        });

//        BootstrapButton btWithdrawal = findViewById(R.id.btWithdrawal);
//        btWithdrawal.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                new AlertDialog.Builder(getApplicationContext())
//                        .setTitle("회원탈퇴")
//                        .setMessage("회원탈퇴 하시겠습니까?")
//                        .setPositiveButton("취소", null)
//                        .setNegativeButton("확인",
//                                new DialogInterface.OnClickListener() {
//                                    @Override
//                                    public void onClick(DialogInterface dialog, int which) {
//                                        mAuth.getCurrentUser().delete();
//                                        Toast.makeText(LogInfoActivity.this, "회원탈퇴 되셨습니다.", Toast.LENGTH_SHORT).show();
//                                        Intent intent = new Intent(getApplicationContext(), EmailPasswordActivity.class);
//                                        startActivity(intent);
//                                    }
//                                })
//                        .setCancelable(false)
//                        .show();
//            }
//        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mAuth = null;
    }
}
