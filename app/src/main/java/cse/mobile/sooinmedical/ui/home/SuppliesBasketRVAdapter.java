package cse.mobile.sooinmedical.ui.home;

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

import cse.mobile.sooinmedical.R;
import cse.mobile.sooinmedical.firebase.FirebaseBasketDB;
import cse.mobile.sooinmedical.vo.BasketVo;

public class SuppliesBasketRVAdapter extends RecyclerView.Adapter {
    private ArrayList<BasketVo> mALBasket;
    private LayoutInflater mInflate;
    private Context mContext;
    private int mCount;

    public SuppliesBasketRVAdapter(Context context, ArrayList<BasketVo> basketVos) {
        this.mContext = context;
        this.mALBasket = basketVos;
        this.mContext = context;
        this.mInflate = LayoutInflater.from(context);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflate.inflate(R.layout.custom_basket_item, parent, false);
        MyViewHolder viewHolder = new MyViewHolder(view);
        return viewHolder;
    }

    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        MyViewHolder myViewHolder = (MyViewHolder) holder;

        myViewHolder.tvItem.setText(mALBasket.get(position).getItem());
        myViewHolder.tvManufacturer.setText(mALBasket.get(position).getManufacturer());
        myViewHolder.tvCount.setText(mALBasket.get(position).getCount() + "EA");
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
        return mALBasket.size();
    }

    private static class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView tvItem;
        private TextView tvManufacturer;
        private TextView tvCount;
        private LinearLayout llModify;
        private LinearLayout llDelete;

        private MyViewHolder(View itemView) {
            super(itemView);

            tvItem = itemView.findViewById(R.id.tvItem);
            tvManufacturer = itemView.findViewById(R.id.tvManufacturer);
            tvCount = itemView.findViewById(R.id.tvCount);
            llModify = itemView.findViewById(R.id.llModify);
            llDelete = itemView.findViewById(R.id.llDelete);
        }
    }

    /* 삭제 다이얼로그 */
    private void deleteItemDialog(int position) {
        new AlertDialog.Builder(mContext)
                .setTitle("삭제")
                .setMessage("정말로 삭제하시겠습니까?")
                .setPositiveButton("취소", null)
                .setNegativeButton("삭제",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                FirebaseBasketDB.getInstance(mContext).deleteDB("소모품", mALBasket.get(position).getItem());
                                mALBasket.remove(position);

                                notifyDataSetChanged();
                                Toast.makeText(mContext, "삭제되었습니다.", Toast.LENGTH_SHORT).show();
                            }
                        })
                .setCancelable(false)
                .show();
    }

    /* 수정 다이얼로그 */
    private void modifyItemDialog(int position) {
        mCount = (int) mALBasket.get(position).getCount();

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
                .setTitle("수량 변경")
                .setPositiveButton("취소", null)
                .setNegativeButton("변경",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (etInputCount.getText().toString().isEmpty())
                                    Toast.makeText(mContext, "수량을 정해주세요.", Toast.LENGTH_SHORT).show();
                                else if (Integer.parseInt(etInputCount.getText().toString()) < 1)
                                    Toast.makeText(mContext, "1개 이하로 수정할 수 없습니다.", Toast.LENGTH_SHORT).show();
                                else {
                                    mALBasket.get(position).setCount(Integer.parseInt(etInputCount.getText().toString()));
                                    FirebaseBasketDB.getInstance(mContext).writeDB("소모품", mALBasket.get(position));
                                    notifyDataSetChanged();
                                    Toast.makeText(mContext, "수정되었습니다.", Toast.LENGTH_SHORT).show();
                                }
                            }
                        })
                .setCancelable(false)
                .show();
    }
}
