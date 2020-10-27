package cse.mobile.sooinmedical.ui.bookmark;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import cse.mobile.sooinmedical.MainActivity;
import cse.mobile.sooinmedical.R;
import cse.mobile.sooinmedical.firebase.FirebaseBasketDB;
import cse.mobile.sooinmedical.vo.BasketVo;

public class MedicinesBookmarkRVAdapter extends RecyclerView.Adapter {
    private ArrayList<BasketVo> mALBookmark;
    private LayoutInflater mInflate;
    private Context mContext;
    private int mCount;

    public MedicinesBookmarkRVAdapter(Context context, ArrayList<BasketVo> bookmarkVos) {
        this.mContext = context;
        this.mALBookmark = bookmarkVos;
        this.mContext = context;
        this.mInflate = LayoutInflater.from(context);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflate.inflate(R.layout.custom_bookmark_item, parent, false);
        MyViewHolder viewHolder = new MyViewHolder(view);
        return viewHolder;
    }

    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        MyViewHolder myViewHolder = (MyViewHolder) holder;

        myViewHolder.tvItem.setText(mALBookmark.get(position).getItem());
        myViewHolder.tvManufacturer.setText(mALBookmark.get(position).getManufacturer());
        myViewHolder.llModify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                modifyItemDialog(position);
            }
        });

        myViewHolder.llDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteItemDialog(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mALBookmark.size();
    }

    private static class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView tvItem;
        private TextView tvManufacturer;
        private LinearLayout llModify;
        private LinearLayout llDelete;

        private MyViewHolder(View itemView) {
            super(itemView);

            tvItem = itemView.findViewById(R.id.tvItem);
            tvManufacturer = itemView.findViewById(R.id.tvManufacturer);
            llModify = itemView.findViewById(R.id.llModify);
            llDelete = itemView.findViewById(R.id.llDelete);
        }
    }

    /* 삭제 다이얼로그 */
    private void deleteItemDialog(int position) {
        new AlertDialog.Builder(mContext)
                .setTitle("삭제")
                .setMessage("북마크에서 삭제하시겠습니까?")
                .setPositiveButton("취소", null)
                .setNegativeButton("삭제",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                FirebaseBasketDB.getInstance(mContext).deleteDB("의약품 북마크", mALBookmark.get(position).getItem());
                                mALBookmark.remove(position);

                                notifyDataSetChanged();
                            }
                        })
                .setCancelable(false)
                .show();
    }

    /* 수량선택해서 노트패드에 추가하기 다이얼로그 */
    private void modifyItemDialog(int position) {
        mCount = 1;

        final View innerView = mInflate.inflate(R.layout.dialog_count_amount, null);

        EditText etInputCount = innerView.findViewById(R.id.etInputCount);
        etInputCount.setText(Integer.toString(mCount));

        LinearLayout llIncrease = innerView.findViewById(R.id.llIncrease);
        llIncrease.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                if (etInputCount.getText().toString().isEmpty())
                    Toast.makeText(mContext, "수량을 정해주세요.", Toast.LENGTH_SHORT).show();
                else {
                    mCount = Integer.parseInt(etInputCount.getText().toString());
                    etInputCount.setText(Integer.toString(++mCount));
                }
            }
        });

        LinearLayout llDecrease = innerView.findViewById(R.id.llDecrease);
        llDecrease.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                if (etInputCount.getText().toString().isEmpty())
                    Toast.makeText(mContext, "수량을 정해주세요.", Toast.LENGTH_SHORT).show();
                else {
                    mCount = Integer.parseInt(etInputCount.getText().toString());
                    if (mCount > 1)
                        etInputCount.setText(Integer.toString(--mCount));
                    else
                        Toast.makeText(mContext, "1개 이하는 주문할 수 없습니다.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        new AlertDialog.Builder(mContext)
                .setView(innerView)
                .setTitle("바구니에 담을 수량 선택")
                .setPositiveButton("취소", null)
                .setNegativeButton("추가",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (etInputCount.getText().toString().isEmpty())
                                    Toast.makeText(mContext, "수량을 정해주세요.", Toast.LENGTH_SHORT).show();
                                else if (Integer.parseInt(etInputCount.getText().toString()) < 1)
                                    Toast.makeText(mContext, "1개 이하로 수정할 수 없습니다.", Toast.LENGTH_SHORT).show();
                                else {
                                    mALBookmark.get(position).setCount(Integer.parseInt(etInputCount.getText().toString()));

                                    for (int i = 0; i < MainActivity.sALMedicinesBasket.size(); i++) {
                                        if (MainActivity.sALMedicinesBasket.get(i).getItem().equals(mALBookmark.get(position).getItem()))
                                            MainActivity.sALMedicinesBasket.remove(i);
                                    }

                                    MainActivity.sALMedicinesBasket.add(new BasketVo(mALBookmark.get(position).getItem(),
                                            mALBookmark.get(position).getManufacturer(),
                                            Integer.parseInt(etInputCount.getText().toString())));

                                    FirebaseBasketDB.getInstance(mContext).writeDB("의약품", mALBookmark.get(position));
                                    notifyDataSetChanged();
                                }
                            }
                        })
                .setCancelable(false)
                .show();
    }
}
