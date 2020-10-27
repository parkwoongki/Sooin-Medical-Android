package cse.mobile.sooinmedical.ui.home;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

import cse.mobile.sooinmedical.MainActivity;
import cse.mobile.sooinmedical.R;
import cse.mobile.sooinmedical.firebase.FirebaseBasketDB;
import cse.mobile.sooinmedical.vo.BasketVo;

public class HomeFragment extends Fragment {
    private ArrayList<BasketVo> mALSuppliesBasket;
    private ArrayList<BasketVo> mALMedicinesBasket;

    private SuppliesBasketRVAdapter mSuppliesBasketAdapter;
    private MedicinesBasketRVAdapter mMedicinesBasketAdapter;

    private RecyclerView mRVSuppliesBasket;
    private RecyclerView mRVMedicinesBasket;

    private RecyclerView.LayoutManager mLMSuppliesBasket;
    private RecyclerView.LayoutManager mLMMedicinesBasket;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        setHasOptionsMenu(true);

        /* 바구니 레퍼런스 */
        mALSuppliesBasket = MainActivity.sALSuppliesBasket;
        mALMedicinesBasket = MainActivity.sALMedicinesBasket;

        /* 리사이클러뷰에 어댑터 연결 */
        mRVSuppliesBasket = root.findViewById(R.id.rvSuppliesBasket);
        mLMSuppliesBasket = new LinearLayoutManager(getActivity());
        mRVSuppliesBasket.setLayoutManager(mLMSuppliesBasket);
        mSuppliesBasketAdapter = new SuppliesBasketRVAdapter(getActivity(), mALSuppliesBasket);
        mRVSuppliesBasket.setAdapter(mSuppliesBasketAdapter);

        mRVMedicinesBasket = root.findViewById(R.id.rvMedicinesBasket);
        mLMMedicinesBasket = new LinearLayoutManager(getActivity());
        mRVMedicinesBasket.setLayoutManager(mLMMedicinesBasket);
        mMedicinesBasketAdapter = new MedicinesBasketRVAdapter(getActivity(), mALMedicinesBasket);
        mRVMedicinesBasket.setAdapter(mMedicinesBasketAdapter);

        View vSuppliesBasketDivider = root.findViewById(R.id.vSuppliesBasketDivider);
        View vMedicinesBasketDivider = root.findViewById(R.id.vMedicinesBasketDivider);

        LinearLayout llSuppliesBasketTab = root.findViewById(R.id.llSuppliesBasketTab);
        llSuppliesBasketTab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vSuppliesBasketDivider.setVisibility(View.INVISIBLE);
                mRVSuppliesBasket.setVisibility(View.VISIBLE);
                vMedicinesBasketDivider.setVisibility(View.VISIBLE);
                mRVMedicinesBasket.setVisibility(View.INVISIBLE);
            }
        });

        LinearLayout llMedicinesBasketTab = root.findViewById(R.id.llMedicinesBasketTab);
        llMedicinesBasketTab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vMedicinesBasketDivider.setVisibility(View.INVISIBLE);
                mRVMedicinesBasket.setVisibility(View.VISIBLE);
                vSuppliesBasketDivider.setVisibility(View.VISIBLE);
                mRVSuppliesBasket.setVisibility(View.INVISIBLE);
            }
        });

        Button btSubmitSMS = root.findViewById(R.id.btSubmitSMS);
        btSubmitSMS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if (mALSuppliesBasket.isEmpty() && mALMedicinesBasket.isEmpty()) {
                        Toast.makeText(getActivity(), "바구니에 물건을 담아주세요!", Toast.LENGTH_SHORT).show();
                    } else {
                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("mmsto:"));
                        intent.setType("vnd.android-dir/mms-sms");
                        intent.addCategory("android.intent.category.DEFAULT");
                        intent.putExtra("address", "01050218207");
                        intent.putExtra("exit_on_sent", true);
                        intent.putExtra("subject", "수인약품");
                        intent.putExtra("sms_body", "[수인약품] - " + orderList() + "\n기타 문의 사항 :\n\n");
                        startActivity(intent);
                        Toast.makeText(getActivity(), "수인약품앱으로 돌아가시려면 뒤로가기를 눌러주세요.", Toast.LENGTH_SHORT).show();

                        FirebaseBasketDB.getInstance(getActivity()).bookmarkDB("소모품 북마크", mALSuppliesBasket);
                        FirebaseBasketDB.getInstance(getActivity()).bookmarkDB("의약품 북마크", mALMedicinesBasket);

                        reloadSuppliesBookmark();
                        reloadMedicinesBookmark();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(getActivity(), "잠시후 다시 이용해주세요.", Toast.LENGTH_SHORT).show();
                }
            }
        });

//        Button btSubmitKakao = root.findViewById(R.id.btSubmitKakao);
//        btSubmitKakao.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                try {
//                    if (mALSuppliesBasket.isEmpty() && mALMedicinesBasket.isEmpty()) {
//                        Toast.makeText(getActivity(), "바구니에 물건을 담아주세요!", Toast.LENGTH_SHORT).show();
//                    } else {
//                        Intent intent = new Intent(Intent.ACTION_SEND);
//                        intent.setType("text/plain");
//                        intent.putExtra(Intent.EXTRA_SUBJECT, "[수인약품]");
//                        intent.putExtra(Intent.EXTRA_TEXT, orderList());
//                        intent.setPackage("com.kakao.talk");
//                        startActivity(intent);
//
//                        FirebaseBasketDB.getInstance(getActivity()).bookmarkDB("소모품 주문이력", mALSuppliesBasket);
//                        FirebaseBasketDB.getInstance(getActivity()).bookmarkDB("의약품 주문이력", mALMedicinesBasket);
//
//                        reloadSuppliesBookmark();
//                        reloadMedicinesBookmark();
//                    }
//                } catch (Exception e) {
//                    Toast.makeText(getActivity(), "카카오톡이 없으실 경우 문자로 전송해주세요.", Toast.LENGTH_SHORT).show();
//                }
//            }
//        });

        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mSuppliesBasketAdapter.notifyDataSetChanged();
        mMedicinesBasketAdapter.notifyDataSetChanged();
    }

    @Override
    public void onResume() {
        super.onResume();
        // destroy all menu and re-call onCreateOptionsMenu
        getActivity().invalidateOptionsMenu();
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_home_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.menu_delete) {
            new AlertDialog.Builder(getActivity())
                    .setTitle("바구니 비우기")
                    .setMessage("바구니를 모두 비우시겠습니까?")
                    .setPositiveButton("취소", null)
                    .setNegativeButton("비우기",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    FirebaseBasketDB.getInstance(getActivity()).deleteAllDB("소모품", mALSuppliesBasket);
                                    mALSuppliesBasket.clear();
                                    FirebaseBasketDB.getInstance(getActivity()).deleteAllDB("의약품", mALMedicinesBasket);
                                    mALMedicinesBasket.clear();

                                    mSuppliesBasketAdapter.notifyDataSetChanged();
                                    mMedicinesBasketAdapter.notifyDataSetChanged();
                                }
                            })
                    .setCancelable(false)
                    .show();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private String orderList() {
        StringBuilder message = new StringBuilder(FirebaseAuth.getInstance().getCurrentUser().getDisplayName()).append("\n\n");

        if (!mALSuppliesBasket.isEmpty()) {
            message.append("\n소모품 :\n\n");

            for (int i = 0; i < mALSuppliesBasket.size(); i++) {
                message.append(mALSuppliesBasket.get(i).getItem()).append("\n");
                message.append(mALSuppliesBasket.get(i).getManufacturer()).append("\n");
                message.append(mALSuppliesBasket.get(i).getCount()).append("EA\n\n");
            }
        }

        if (!mALMedicinesBasket.isEmpty()) {
            message.append("\n의약품 :\n\n");
            for (int i = 0; i < mALMedicinesBasket.size(); i++) {
                message.append(mALMedicinesBasket.get(i).getItem()).append("\n");
                message.append(mALMedicinesBasket.get(i).getManufacturer()).append("\n");
                message.append(mALMedicinesBasket.get(i).getCount()).append("EA\n\n");
            }
        }

        return message.toString();
    }

    /* 소모품 주문이력 불러오기 */
    private void reloadSuppliesBookmark() {
        MainActivity.sALSuppliesBookmark.clear();

        FirebaseFirestore.getInstance().collection(FirebaseAuth.getInstance().getCurrentUser().getDisplayName() + "::소모품 주문이력")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                if (document.get("item") != null) {
                                    MainActivity.sALSuppliesBookmark.add(
                                            new BasketVo(
                                                    document.getString("item"),
                                                    document.getString("manufacturer"),
                                                    document.getLong("count"))
                                    );
                                }
                            }
                        } else {
                            Toast.makeText(getActivity(), "소모품 주문이력 불러오기에 실패하였습니다.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    /* 의약품 주문이력 불러오기 */
    private void reloadMedicinesBookmark() {
        MainActivity.sALMedicinesBookmark.clear();

        FirebaseFirestore.getInstance().collection(FirebaseAuth.getInstance().getCurrentUser().getDisplayName() + "::의약품 주문이력")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                if (document.get("item") != null) {
                                    MainActivity.sALMedicinesBookmark.add(
                                            new BasketVo(
                                                    document.getString("item"),
                                                    document.getString("manufacturer"),
                                                    document.getLong("count"))
                                    );
                                }
                            }
                        } else {
                            Toast.makeText(getActivity(), "의약품 주문이력 불러오기에 실패하였습니다.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}