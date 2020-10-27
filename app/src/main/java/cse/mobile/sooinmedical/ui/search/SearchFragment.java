package cse.mobile.sooinmedical.ui.search;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import cse.mobile.sooinmedical.MainActivity;
import cse.mobile.sooinmedical.R;
import cse.mobile.sooinmedical.vo.MedicinesVo;
import cse.mobile.sooinmedical.vo.SuppliesVo;

public class SearchFragment extends Fragment {
    private EditText mETSearchSupplies;

    private ArrayList<SuppliesVo> mALCriteriaSupplies;
    private ArrayList<SuppliesVo> mALSearchSupplies;

    private SuppliesRVAdapter mSuppliesAdapter;

    private EditText mETSearchMedicines;

    private ArrayList<MedicinesVo> mALCriteriaMedicines;
    private ArrayList<MedicinesVo> mALSearchMedicines;

    private MedicinesRVAdapter mMedicinesAdapter;

    private RecyclerView mRVSupplies;
    RecyclerView.LayoutManager mLMSupplies;

    private RecyclerView mRVMedicines;
    RecyclerView.LayoutManager mLMMedicines;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_search, container, false);

        mALCriteriaSupplies = MainActivity.sALSupplies;
        mALSearchSupplies = new ArrayList<>();
        mALSearchSupplies.addAll(mALCriteriaSupplies);

        mALCriteriaMedicines = MainActivity.sALMedicines;
        mALSearchMedicines = new ArrayList<>();
        mALSearchMedicines.addAll(mALCriteriaMedicines);

        /* 리사이클러뷰에 어댑터 연결 */
        mRVSupplies = root.findViewById(R.id.rvSupplies);
        mLMSupplies = new LinearLayoutManager(getActivity());
        mRVSupplies.setLayoutManager(mLMSupplies);
        mSuppliesAdapter = new SuppliesRVAdapter(getActivity(), mALSearchSupplies);
        mRVSupplies.setAdapter(mSuppliesAdapter);

        mRVMedicines = root.findViewById(R.id.rvMedicines);
        mLMMedicines = new LinearLayoutManager(getActivity());
        mRVMedicines.setLayoutManager(mLMMedicines);
        mMedicinesAdapter = new MedicinesRVAdapter(getActivity(), mALSearchMedicines);
        mRVMedicines.setAdapter(mMedicinesAdapter);

        /* setOnItemClickListener */
        SuppliesItemClickSupport.addTo(mRVSupplies).setOnItemClickListener(new SuppliesItemClickSupport.OnItemClickListener() {
            @Override
            public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                infoSupplies(position);
            }
        });

        MedicinesItemClickSupport.addTo(mRVMedicines).setOnItemClickListener(new MedicinesItemClickSupport.OnItemClickListener() {
            @Override
            public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                infoMedicines(position);
            }
        });

        /* 소모품 찾기 EditText*/
        mETSearchSupplies = root.findViewById(R.id.etSearchSupplies);
        mETSearchSupplies.setOnEditorActionListener((v, actionId, event) -> {
            boolean handled = false;
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                searchSupplies(mETSearchSupplies.getText().toString());
            }
            return handled;
        });

        mETSearchMedicines = root.findViewById(R.id.etSearchMedicines);
        mETSearchMedicines.setOnEditorActionListener((v, actionId, event) -> {
            boolean handled = false;
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                searchMedicines(mETSearchMedicines.getText().toString());
            }
            return handled;
        });

        /* 구분선 */
        View vSuppliesDivider = root.findViewById(R.id.vSuppliesDivider);
        View vMedicinesDivider = root.findViewById(R.id.vMedicinesDivider);

        /* 탭 */
        LinearLayout llSuppliesTab = root.findViewById(R.id.llSuppliesTab);
        llSuppliesTab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vSuppliesDivider.setVisibility(View.INVISIBLE);
                mRVSupplies.setVisibility(View.VISIBLE);
                vMedicinesDivider.setVisibility(View.VISIBLE);
                mRVMedicines.setVisibility(View.INVISIBLE);
                mETSearchMedicines.setVisibility(View.INVISIBLE);
                mETSearchSupplies.setVisibility(View.VISIBLE);
            }
        });

        LinearLayout llMedicinesTab = root.findViewById(R.id.llMedicinesTab);
        llMedicinesTab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vMedicinesDivider.setVisibility(View.INVISIBLE);
                mRVMedicines.setVisibility(View.VISIBLE);
                vSuppliesDivider.setVisibility(View.VISIBLE);
                mRVSupplies.setVisibility(View.INVISIBLE);
                mETSearchMedicines.setVisibility(View.VISIBLE);
                mETSearchSupplies.setVisibility(View.INVISIBLE);
            }
        });

        return root;
    }

    /* 소모품 찾기 */
    private void searchSupplies(String containWord) {
        mALSearchSupplies.clear();

        for (int i = 0; i < mALCriteriaSupplies.size(); i++) {
            /* 대소문자 구분 없이 검색 (제품명, 제조사 둘다 검색 가능) */
            if (mALCriteriaSupplies.get(i).getItem().toLowerCase().contains(containWord.toLowerCase())
                    || mALCriteriaSupplies.get(i).getItem().toUpperCase().contains(containWord.toUpperCase())
                    || mALCriteriaSupplies.get(i).getManufacturer().toLowerCase().contains(containWord.toLowerCase())
                    || mALCriteriaSupplies.get(i).getManufacturer().toUpperCase().contains(containWord.toUpperCase()))
                mALSearchSupplies.add(mALCriteriaSupplies.get(i));
        }

        mSuppliesAdapter.notifyDataSetChanged();

        if (mALSearchSupplies.size() != 0) {
            mRVSupplies.scrollToPosition(0);
        } else {
            Toast.makeText(getActivity(), "검색결과가 존재하지 않습니다.", Toast.LENGTH_SHORT).show();
        }
    }

    /* 소모품 정보 */
    private void infoSupplies(int position) {
        new AlertDialog.Builder(getActivity())
                .setTitle("소모품 정보")
                .setMessage(mALSearchSupplies.get(position).toString())
                .setNegativeButton("OK", null)
                .show();
    }

    /* 의약품 찾기 */
    private void searchMedicines(String containWord) {
        mALSearchMedicines.clear();

        int i;
        for (i = 0; i < mALCriteriaMedicines.size(); i++) {
            /* 대소문자 구분 없이 검색 (제품명, 제조사 둘다 검색 가능) */
            if (mALCriteriaMedicines.get(i).getItem().toLowerCase().contains(containWord.toLowerCase())
                    || mALCriteriaMedicines.get(i).getItem().toUpperCase().contains(containWord.toUpperCase())
                    || mALCriteriaMedicines.get(i).getManufacturer().toLowerCase().contains(containWord.toLowerCase())
                    || mALCriteriaMedicines.get(i).getManufacturer().toUpperCase().contains(containWord.toUpperCase()))
                mALSearchMedicines.add(mALCriteriaMedicines.get(i));
        }

        mMedicinesAdapter.notifyDataSetChanged();

        if (mALSearchMedicines.size() != 0) {
            mRVMedicines.scrollToPosition(0);
        } else {
            Toast.makeText(getActivity(), "검색결과가 존재하지 않습니다.", Toast.LENGTH_SHORT).show();
        }
    }

    /* 의약품 정보 */
    private void infoMedicines(int position) {
        new AlertDialog.Builder(getActivity())
                .setTitle("의약품 정보")
                .setMessage(mALSearchMedicines.get(position).toString())
                .setNegativeButton("OK", null)
                .show();
    }
}