package cse.mobile.sooinmedical;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.beardedhen.androidbootstrap.BootstrapButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

import java.util.regex.Pattern;

public class RegisterActivity extends AppCompatActivity {
    private EditText mETEmail;
    private EditText mETName;
    private EditText mETHospital;
    private EditText mETPassword;
    private EditText mETPasswordCheck;

    private LinearLayout mLLVerifyEmailDoneForm;
    private BootstrapButton mBTVerifyEmail;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mLLVerifyEmailDoneForm = findViewById(R.id.llVerifyEmailDoneForm);

        mETEmail = findViewById(R.id.etInputEmail);
        mETName = findViewById(R.id.etInputName);
        mETHospital = findViewById(R.id.etInputHospital);
        mETPassword = findViewById(R.id.etInputPassword);
        mETPasswordCheck = findViewById(R.id.etInputPasswordCheck);
        mAuth = FirebaseAuth.getInstance();

        mBTVerifyEmail = findViewById(R.id.btVerifyEmail);
        mBTVerifyEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createAccount();
            }
        });

        BootstrapButton btRegister = findViewById(R.id.btRegister);
        btRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.getCurrentUser().reload();
                if (mAuth.getCurrentUser().isEmailVerified()) {
                    Intent sIntent = new Intent(getApplicationContext(), EmailPasswordActivity.class);
                    sIntent.putExtra("Email", mAuth.getCurrentUser().getEmail());
                    sIntent.putExtra("Password", mETPassword.getText().toString());
                    startActivity(sIntent);
                    finish();
                } else
                    Toast.makeText(RegisterActivity.this, "이메일 인증을 완료해주세요.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void createAccount() {
        String e_mail = mETEmail.getText().toString();
        String name = mETName.getText().toString();
        String hospital = mETHospital.getText().toString();
        String password = mETPassword.getText().toString();
        String passwordCheck = mETPasswordCheck.getText().toString();

        String e_mailPattern = "^[a-zA-Z0-9._-]+@[a-zA-z0-9.-]+\\.[a-zA-Z]{2,4}$"; // 이메일 정규식
        if (!Pattern.matches(e_mailPattern, e_mail)) {
            Toast.makeText(RegisterActivity.this, "이메일 형식을 확인해주세요.", Toast.LENGTH_LONG).show();
            return;
        }

        if (name.isEmpty()) {
            Toast.makeText(RegisterActivity.this, "이름을 입력해주세요.", Toast.LENGTH_SHORT).show();
            return;
        }

        if (hospital.isEmpty()) {
            Toast.makeText(RegisterActivity.this, "병원 이름을 입력해주세요.", Toast.LENGTH_SHORT).show();
            return;
        }

        StringBuffer strPW = new StringBuffer(password);
        StringBuffer strCheckPW = new StringBuffer(passwordCheck);

        if (strPW.length() < 8 || strCheckPW.length() < 8) { // 최소 비밀번호 사이즈를 맞추기
            Toast.makeText(getApplicationContext(), "비밀번호는 최소 8자리 이상 입력해주세요.", Toast.LENGTH_SHORT).show();
            return;
        }

        if (password.equals("") || passwordCheck.equals("")) {
            Toast.makeText(RegisterActivity.this, "비밀번호를 입력해주세요.", Toast.LENGTH_LONG).show();
            return;
        }

        if (password.equals(passwordCheck)) {
            /* 키보드 내리기 */
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(mETEmail.getWindowToken(), 0);
            imm.hideSoftInputFromWindow(mETName.getWindowToken(), 0);
            imm.hideSoftInputFromWindow(mETHospital.getWindowToken(), 0);
            imm.hideSoftInputFromWindow(mETPassword.getWindowToken(), 0);
            imm.hideSoftInputFromWindow(mETPasswordCheck.getWindowToken(), 0);

            mAuth.createUserWithEmailAndPassword(e_mail, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                FirebaseUser user = mAuth.getCurrentUser();

                                user.updateProfile(new UserProfileChangeRequest.Builder()
                                        .setDisplayName(hospital + "(" + name + ")")
                                        .build());
                                emailVerification();

                                /* 이메일 비활성화 */
                                mETEmail.setEnabled(false);
                                mETEmail.setClickable(false);
                                mETEmail.setFocusable(false);

                                /* 이름 비활성화 */
                                mETName.setEnabled(false);
                                mETName.setClickable(false);
                                mETName.setFocusable(false);

                                /* 병원 이름 비활성화 */
                                mETHospital.setEnabled(false);
                                mETHospital.setClickable(false);
                                mETHospital.setFocusable(false);

                                /* 비밀번호 비활성화 */
                                mETPassword.setEnabled(false);
                                mETPassword.setClickable(false);
                                mETPassword.setFocusable(false);

                                /* 비밀번호 확인 비활성화 */
                                mETPasswordCheck.setEnabled(false);
                                mETPasswordCheck.setClickable(false);
                                mETPasswordCheck.setFocusable(false);

                                /* 버튼 비활성화 */
                                mBTVerifyEmail.setEnabled(false);
                                mBTVerifyEmail.setClickable(false);
                                mBTVerifyEmail.setFocusable(false);

                                /* Visible 시에 애니메이션 집어넣기 */
                                Animation animation = new AlphaAnimation(0, 1);
                                animation.setDuration(1000);
                                mLLVerifyEmailDoneForm.setVisibility(View.VISIBLE);
                                mLLVerifyEmailDoneForm.setAnimation(animation);
                            } else {
                                try {
                                    task.getResult();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                    Log.d("Fail_register_email", e.getMessage());
                                    Toast.makeText(RegisterActivity.this, "이미 존재하는 이메일입니다.\n이메일을 다시 입력해주세요.", Toast.LENGTH_LONG).show();
                                }
                            }
                        }
                    });
        } else {
            Toast.makeText(getApplicationContext(), "비밀번호가 일치하지 않습니다.\n비밀번호를 다시 확인해주세요.", Toast.LENGTH_SHORT).show();
        }
    }

    public void emailVerification() {
        final FirebaseUser user = mAuth.getCurrentUser();

//        String url = "http://www.sooin-medical.firebaseapp.com/verify?uid=" + user.getUid();
//        ActionCodeSettings actionCodeSettings = ActionCodeSettings.newBuilder()
//                .setUrl(url)
//                .setHandleCodeInApp(true)
//                // The default for this is populated with the current android package name.
//                .setAndroidPackageName("cse.mobile.sooinmedical", true, "23")
//                .build();

        user.sendEmailVerification().addOnCompleteListener(this, new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(RegisterActivity.this, "해당 이메일로 인증 요청이 발송되었습니다.", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(RegisterActivity.this, "인증 요청 메일 발송에 실패했습니다.\n다시 시도해주세요.", Toast.LENGTH_SHORT).show();
                    try {
                        task.getResult();
                    } catch (Exception e) {
                        Log.d("Fail send_email", e.getMessage());
                    } finally {
                        return;
                    }
                }

            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mAuth = null;
    }
}
