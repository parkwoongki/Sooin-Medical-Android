package cse.mobile.sooinmedical;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.beardedhen.androidbootstrap.BootstrapButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

import java.util.regex.Pattern;

public class FindPasswordActivity extends AppCompatActivity {
    private EditText mETFindPassword;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_password);

        mETFindPassword = findViewById(R.id.etFindPassword);

        BootstrapButton btFindPassword = findViewById(R.id.btFindPassword);
        btFindPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetPassword();
            }
        });
    }

    public void resetPassword() {
        FirebaseAuth auth = FirebaseAuth.getInstance();

        String e_mail = mETFindPassword.getText().toString();
        String e_mailPattern = "^[a-zA-Z0-9._-]+@[a-zA-z0-9.-]+\\.[a-zA-Z]{2,4}$";

        if (!Pattern.matches(e_mailPattern, e_mail)) {
            Toast.makeText(FindPasswordActivity.this, "이메일 형식을 확인해주세요.", Toast.LENGTH_LONG).show();
            return;
        }

        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(mETFindPassword.getWindowToken(), 0);

        auth.sendPasswordResetEmail(e_mail)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(FindPasswordActivity.this, "비밀번호 재설정 이메일이 전송되었습니다.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}
