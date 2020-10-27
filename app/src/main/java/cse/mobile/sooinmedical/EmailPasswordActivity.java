package cse.mobile.sooinmedical;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.beardedhen.androidbootstrap.BootstrapButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class EmailPasswordActivity extends AppCompatActivity {
    private static final int REQ_CODE_LOGIN = 0;

    private FirebaseAuth mAuth;
    private EditText mETEmail;
    private EditText mETPassword;
    private static boolean mIsChecked;
    private SharedPreferences mShared;

    private long backKeyPressedTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_email_password);
        getSupportActionBar().hide();

        mETEmail = findViewById(R.id.etEmail);
        mETPassword = findViewById(R.id.etPassword);

        mAuth = FirebaseAuth.getInstance();

        /* 자동로그인 구현 */
        mShared = getPreferences(Context.MODE_PRIVATE);

        CheckBox cbSaveLoginInfo = findViewById(R.id.cbSaveLoginInfo);
        cbSaveLoginInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 체크만 해놓고 로그인할 때 계정 캐시에 저장할 것임
                mIsChecked = ((CheckBox) v).isChecked();
            }
        });

        mIsChecked = mShared.getBoolean("isChecked", false); // 마지막 자동 로그인 저장 체크 값 1
        cbSaveLoginInfo.setChecked(mShared.getBoolean("isChecked", mIsChecked)); // 마지막 자동 로그인 저장 체크 값 2
        mETEmail.setText(mShared.getString("email", null));
        mETPassword.setText(mShared.getString("password", null));

        /* 회원가입 직후 해당 이메일과 패스워드 가져와서 세팅하기 */
        if (getIntent().getStringExtra("Email") != null && getIntent().getStringExtra("Password") != null) {
            mETEmail.setText(getIntent().getStringExtra("Email"));
            mETPassword.setText(getIntent().getStringExtra("Password"));
        }

        /* 1. 회원가입 페이지로 이동 */
        TextView tvCreateAccount = findViewById(R.id.tvCreateAcount);
        tvCreateAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), RegisterActivity.class);
                startActivity(intent);
            }
        });

        /* 2. 비밀번호 찾기 페이지로 이동 */
        TextView tvFindPassword = findViewById(R.id.tvFindPassword);
        tvFindPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), FindPasswordActivity.class);
                startActivity(intent);
            }
        });

        /* 3. 로그인 버튼 */
        BootstrapButton btSignIn = findViewById(R.id.btSignIn);
        btSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn(mETEmail.getText().toString(), mETPassword.getText().toString());
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();

        if (!getIntent().getBooleanExtra("Logout", false)) {
            if (mShared.getBoolean("isChecked", false))
                signIn(mETEmail.getText().toString(), mETPassword.getText().toString());
        }
    }

    /* 뒤로가기 2번하면 앱종료 */
    @Override
    public void onBackPressed() {
        // 1번째 백버튼 클릭
        if (System.currentTimeMillis() > backKeyPressedTime + 2000) {
            backKeyPressedTime = System.currentTimeMillis();
            Toast.makeText(this, "'뒤로' 버튼을 한번 더 누르시면 종료됩니다.", Toast.LENGTH_SHORT).show();
        }
        // 2번째 백버튼 클릭 (종료)
        else {
            this.finishAffinity();
            System.exit(0);
            android.os.Process.killProcess(android.os.Process.myPid());
        }
    }

    public void signIn(String email, String password) {
        if (email.equals("") || password.equals("")) {
            Toast.makeText(this, "아이디 비밀번호를 입력해주세요", Toast.LENGTH_SHORT).show();
            return;
        }

        final ProgressDialog dialog = new ProgressDialog(this);
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.setMessage("로그인 중입니다.\n잠시만 기다려주세요.");
        dialog.setCancelable(false);
        dialog.show();

        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) { // 계정이 등록이 되어 있으면
                    FirebaseUser user = mAuth.getCurrentUser();
                    if (user.isEmailVerified()) { // 그리고 그때 그 계정이 실제로 존재하는 계정인지
                        Log.d("login", "signInWithEmail:success" + user.getEmail());
                        Toast.makeText(EmailPasswordActivity.this,
                                mAuth.getCurrentUser().getDisplayName()+"님 환영합니다.\n"
                                +"Sign In With Email : success!\n" + user.getEmail(), Toast.LENGTH_SHORT).show();
                        saveLoginInfo(mETEmail.getText().toString(), mETPassword.getText().toString(), mIsChecked);
                        Intent intent = new Intent(EmailPasswordActivity.this, DownloadActivity.class);
                        startActivityForResult(intent, REQ_CODE_LOGIN);
                        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                        finish();
                    } else {
                        dialog.dismiss();
                        emailVerification();
                    }
                } else {
                    Log.d("login", "signInWithEmail:failure", task.getException());
                    dialog.dismiss();
                    Toast.makeText(EmailPasswordActivity.this, "존재하지 않는 이메일입니다.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void emailVerification() {
        final FirebaseUser user = mAuth.getCurrentUser();
        user.sendEmailVerification().addOnCompleteListener(this, new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(EmailPasswordActivity.this, "해당 이메일로 인증 요청이 재발송되었습니다.", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(EmailPasswordActivity.this, "인증 요청 메일 발송에 실패했습니다.\n잠시 후 다시 시도해주세요.", Toast.LENGTH_SHORT).show();
                    try {
                        task.getResult();
                    } catch (Exception e) {
                        Log.d("Fail send_email", e.getMessage());
                    }
                }

            }
        });
    }

    public void saveLoginInfo(String email, String password, boolean checked) {
        SharedPreferences.Editor editor = mShared.edit();

        editor.putString("email", email);
        editor.putString("password", password);
        editor.putBoolean("isChecked", checked);
        editor.apply();
    }
}
