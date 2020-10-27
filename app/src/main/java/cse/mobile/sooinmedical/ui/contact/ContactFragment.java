package cse.mobile.sooinmedical.ui.contact;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import cse.mobile.sooinmedical.R;

public class ContactFragment extends Fragment {
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_contact, container, false);

        /* 로그인 정보 */
        LinearLayout llLogInfo = root.findViewById(R.id.llLogInfo);
        llLogInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), LogInfoActivity.class);
                startActivity(intent);
            }
        });

        /* 의약품 도매상 허가증 */
        LinearLayout llPermit = root.findViewById(R.id.llPermit);
        llPermit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), PermitActivity.class);
                startActivity(intent);
            }
        });

        /* 수인약품 */
        LinearLayout llSooinMedical = root.findViewById(R.id.llSooInMedical);
        llSooinMedical.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ContactSooInMedicalActivity.class);
                startActivity(intent);
            }
        });

        /* 박수인 */
        LinearLayout llSooInPark = root.findViewById(R.id.llSooInPark);
        llSooInPark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ContactSooInParkActivity.class);
                startActivity(intent);
            }
        });

        /* 박의천 */
        LinearLayout llEuChunPark = root.findViewById(R.id.llEuChunPark);
        llEuChunPark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ContactEuChunParkActivity.class);
                startActivity(intent);
            }
        });

        /* 개인정보처리방침 */
        LinearLayout llPrivacyPolicy = root.findViewById(R.id.llPrivacyPolicy);
        llPrivacyPolicy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), PrivacyPolicyActivity.class);
                startActivity(intent);
            }
        });

        /* 에러 문의 */
        LinearLayout llErrorReport = root.findViewById(R.id.llErrorReport);
        llErrorReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:sbe03005@naver.com"));
                intent.putExtra(Intent.EXTRA_SUBJECT, "[수인약품]어플리케이션 오류 신고 메일입니다.");
                intent.putExtra(Intent.EXTRA_TEXT, "\n\n\n\n수인약품 앱에서 보냈습니다.");
                startActivity(intent);
            }
        });

        /* 오픈소스 라이선스 */
        LinearLayout llLicense = root.findViewById(R.id.llLicense);
        llLicense.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), LicenseActivity.class);
                startActivity(intent);
            }
        });

        /* 내 정보 */
        LinearLayout llDeveloperInfo = root.findViewById(R.id.llDeveloperInfo);
        llDeveloperInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), DeveloperInfoActivity.class);
                startActivity(intent);
            }
        });

        return root;
    }
}