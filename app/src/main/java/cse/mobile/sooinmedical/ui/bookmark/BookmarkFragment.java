package cse.mobile.sooinmedical.ui.bookmark;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import cse.mobile.sooinmedical.MainActivity;
import cse.mobile.sooinmedical.R;
import cse.mobile.sooinmedical.vo.BasketVo;

public class BookmarkFragment extends Fragment {
    private RecyclerView mRVSuppliesBookmark;
    private RecyclerView mRVMedicinesBookmark;

    private ArrayList<BasketVo> mALSuppliesBookmark;
    private ArrayList<BasketVo> mALMedicinesBookmark;

    private ArrayList<BasketVo> mALSearchSupplies;
    private ArrayList<BasketVo> mALSearchMedicines;

    SuppliesBookmarkRVAdapter mSuppliesBookmarkAdapter;
    MedicinesBookmarkRVAdapter mMedicinesBookmarkAdapter;

    private EditText mETSearchSupplies;
    private EditText mETSearchMedicines;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_bookmark, container, false);

        /* 소모품 바구니 레퍼런스 */
        mALSuppliesBookmark = MainActivity.sALSuppliesBookmark;
        mALSearchSupplies = new ArrayList<>();
        mALSearchSupplies.addAll(mALSuppliesBookmark);

        /* 의약품 바구니 레퍼런스 */
        mALMedicinesBookmark = MainActivity.sALMedicinesBookmark;
        mALSearchMedicines = new ArrayList<>();
        mALSearchMedicines.addAll(mALMedicinesBookmark);

        /* 리사이클러뷰에 어댑터 연결 */
        mRVSuppliesBookmark = root.findViewById(R.id.rvSuppliesBookmark);
        RecyclerView.LayoutManager mLMSuppliesBookmark = new LinearLayoutManager(getActivity());
        mRVSuppliesBookmark.setLayoutManager(mLMSuppliesBookmark);
        mSuppliesBookmarkAdapter = new SuppliesBookmarkRVAdapter(getActivity(), mALSuppliesBookmark);
        mRVSuppliesBookmark.setAdapter(mSuppliesBookmarkAdapter);

        mRVMedicinesBookmark = root.findViewById(R.id.rvMedicinesBookmark);
        RecyclerView.LayoutManager mLMMedicinesBookmark = new LinearLayoutManager(getActivity());
        mRVMedicinesBookmark.setLayoutManager(mLMMedicinesBookmark);
        mMedicinesBookmarkAdapter = new MedicinesBookmarkRVAdapter(getActivity(), mALMedicinesBookmark);
        mRVMedicinesBookmark.setAdapter(mMedicinesBookmarkAdapter);

        View vSuppliesBookmarkDivider = root.findViewById(R.id.vSuppliesBookmarkDivider);
        View vMedicinesBookmarkDivider = root.findViewById(R.id.vMedicinesBookmarkDivider);

//        mETSearchSupplies = root.findViewById(R.id.etSearchSupplies);
//        mETSearchSupplies.setOnEditorActionListener((v, actionId, event) -> {
//            boolean handled = false;
//            if (actionId == EditorInfo.IME_ACTION_DONE) {
//                searchSupplies(mETSearchSupplies.getText().toString());
//            }
//            return handled;
//        });
//
//        mETSearchMedicines = root.findViewById(R.id.etSearchMedicines);
//        mETSearchMedicines.setOnEditorActionListener((v, actionId, event) -> {
//            boolean handled = false;
//            if (actionId == EditorInfo.IME_ACTION_DONE) {
//                searchMedicines(mETSearchMedicines.getText().toString());
//            }
//            return handled;
//        });

        LinearLayout llSuppliesBookmarkTab = root.findViewById(R.id.llSuppliesBookmarkTab);
        llSuppliesBookmarkTab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vSuppliesBookmarkDivider.setVisibility(View.INVISIBLE);
                mRVSuppliesBookmark.setVisibility(View.VISIBLE);
                vMedicinesBookmarkDivider.setVisibility(View.VISIBLE);
                mRVMedicinesBookmark.setVisibility(View.INVISIBLE);
//                mETSearchMedicines.setVisibility(View.INVISIBLE);
//                mETSearchSupplies.setVisibility(View.VISIBLE);
            }
        });

        LinearLayout llMedicinesBookmarkTab = root.findViewById(R.id.llMedicinesBookmarkTab);
        llMedicinesBookmarkTab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vMedicinesBookmarkDivider.setVisibility(View.INVISIBLE);
                mRVMedicinesBookmark.setVisibility(View.VISIBLE);
                vSuppliesBookmarkDivider.setVisibility(View.VISIBLE);
                mRVSuppliesBookmark.setVisibility(View.INVISIBLE);
//                mETSearchMedicines.setVisibility(View.VISIBLE);
//                mETSearchSupplies.setVisibility(View.INVISIBLE);
            }
        });

        return root;
    }

    /* 소모품 찾기 */
    private void searchSupplies(String containWord) {
        mALSearchSupplies.clear();

        for (int i = 0; i < mALSuppliesBookmark.size(); i++) {
            /* 대소문자 구분 없이 검색 (제품명, 제조사 둘다 검색 가능) */
            if (mALSuppliesBookmark.get(i).getItem().toLowerCase().contains(containWord.toLowerCase())
                    || mALSuppliesBookmark.get(i).getItem().toUpperCase().contains(containWord.toUpperCase())
                    || mALSuppliesBookmark.get(i).getManufacturer().toLowerCase().contains(containWord.toLowerCase())
                    || mALSuppliesBookmark.get(i).getManufacturer().toUpperCase().contains(containWord.toUpperCase()))
                mALSearchSupplies.add(mALSuppliesBookmark.get(i));
        }

        mSuppliesBookmarkAdapter.notifyDataSetChanged();

        if (mALSearchSupplies.size() != 0) {
            mRVSuppliesBookmark.scrollToPosition(0);
        } else {
            Toast.makeText(getActivity(), "검색결과가 존재하지 않습니다.", Toast.LENGTH_SHORT).show();
        }
    }

    /* 의약품 찾기 */
    private void searchMedicines(String containWord) {
        mALSearchMedicines.clear();

        for (int i = 0; i < mALMedicinesBookmark.size(); i++) {
            /* 대소문자 구분 없이 검색 (제품명, 제조사 둘다 검색 가능) */
            if (mALMedicinesBookmark.get(i).getItem().toLowerCase().contains(containWord.toLowerCase())
                    || mALMedicinesBookmark.get(i).getItem().toUpperCase().contains(containWord.toUpperCase())
                    || mALMedicinesBookmark.get(i).getManufacturer().toLowerCase().contains(containWord.toLowerCase())
                    || mALMedicinesBookmark.get(i).getManufacturer().toUpperCase().contains(containWord.toUpperCase()))
                mALSearchMedicines.add(mALMedicinesBookmark.get(i));
        }

        mMedicinesBookmarkAdapter.notifyDataSetChanged();

        if (mALSearchMedicines.size() != 0) {
            mRVMedicinesBookmark.scrollToPosition(0);
        } else {
            Toast.makeText(getActivity(), "검색결과가 존재하지 않습니다.", Toast.LENGTH_SHORT).show();
        }
    }
}