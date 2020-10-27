package cse.mobile.sooinmedical.ui.search;

import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

import cse.mobile.sooinmedical.MainActivity;
import cse.mobile.sooinmedical.R;
import cse.mobile.sooinmedical.firebase.FirebaseBasketDB;
import cse.mobile.sooinmedical.vo.BasketVo;
import cse.mobile.sooinmedical.vo.SuppliesVo;

public class SuppliesRVAdapter extends RecyclerView.Adapter {
    private ArrayList<SuppliesVo> mALSupplies;
    private LayoutInflater mInflate;
    private Context mContext;
    private int mCount;

    private FirebaseAuth mAuth;
    private FirebaseFirestore mDB;

    public SuppliesRVAdapter(Context context, ArrayList<SuppliesVo> supplies) {
        this.mContext = context;
        this.mALSupplies = supplies;
        this.mContext = context;
        this.mInflate = LayoutInflater.from(context);

        this.mAuth = FirebaseAuth.getInstance();
        this.mDB = FirebaseFirestore.getInstance();
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflate.inflate(R.layout.custom_list_item, parent, false);
        MyViewHolder viewHolder = new MyViewHolder(view);
        return viewHolder;
    }

    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        MyViewHolder myViewHolder = (MyViewHolder) holder;

        myViewHolder.tvItem.setText(mALSupplies.get(position).getItem());
        myViewHolder.tvManufacturer.setText(mALSupplies.get(position).getManufacturer());
        myViewHolder.llAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addBookmarkDialog(position, mALSupplies.get(position).getItem(), mALSupplies.get(position).getManufacturer());
            }
        });
    }

    @Override
    public int getItemCount() {
        return mALSupplies.size();
    }

    private static class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView tvItem;
        private TextView tvManufacturer;
        private LinearLayout llAdd;

        private MyViewHolder(View itemView) {
            super(itemView);

            tvItem = itemView.findViewById(R.id.tvItem);
            tvManufacturer = itemView.findViewById(R.id.tvManufacturer);
            llAdd = itemView.findViewById(R.id.llAdd);
        }
    }

    private void addBookmarkDialog(int position, String item, String manufacturer) {
        new AlertDialog.Builder(mContext)
                .setTitle("북마크 추가")
                .setMessage(item + "을(를) 북마크에 추가하시겠습니까?")
                .setPositiveButton("취소", null)
                .setNegativeButton("추가",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                MainActivity.sALSuppliesBookmark.add(new BasketVo(item, manufacturer, 1));
                                FirebaseBasketDB.getInstance(mContext).writeDB("소모품 북마크", new BasketVo(item, manufacturer, 1));
                            }
                        })
                .setCancelable(false)
                .show();
    }

//    private void countAmountDialog(int position, String item, String manufacturer) {
//        mCount = 1;
//
//        final View innerView = mInflate.inflate(R.layout.dialog_count_amount, null);
//
//        EditText etInputCount = innerView.findViewById(R.id.etInputCount);
//
//        LinearLayout llIncrease = innerView.findViewById(R.id.llIncrease);
//        llIncrease.setOnClickListener(new View.OnClickListener() {
//            @SuppressLint("SetTextI18n")
//            @Override
//            public void onClick(View v) {
//                if (etInputCount.getText().toString().isEmpty())
//                    Toast.makeText(mContext, "수량을 정해주세요.", Toast.LENGTH_SHORT).show();
//                else {
//                    mCount = Integer.parseInt(etInputCount.getText().toString());
//                    etInputCount.setText(Integer.toString(++mCount));
//                }
//            }
//        });
//
//        LinearLayout llDecrease = innerView.findViewById(R.id.llDecrease);
//        llDecrease.setOnClickListener(new View.OnClickListener() {
//            @SuppressLint("SetTextI18n")
//            @Override
//            public void onClick(View v) {
//                if (etInputCount.getText().toString().isEmpty())
//                    Toast.makeText(mContext, "수량을 정해주세요.", Toast.LENGTH_SHORT).show();
//                else {
//                    mCount = Integer.parseInt(etInputCount.getText().toString());
//                    if (mCount > 1)
//                        etInputCount.setText(Integer.toString(--mCount));
//                    else
//                        Toast.makeText(mContext, "1개 이하는 주문할 수 없습니다.", Toast.LENGTH_SHORT).show();
//                }
//            }
//        });
//
//        new AlertDialog.Builder(mContext)
//                .setView(innerView)
//                .setTitle("수량 선택")
//                .setPositiveButton("취소", null)
//                .setNegativeButton("추가",
//                        new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//                                if (etInputCount.getText().toString().isEmpty())
//                                    Toast.makeText(mContext, "수량을 정해주세요.", Toast.LENGTH_SHORT).show();
//                                else if (Integer.parseInt(etInputCount.getText().toString()) < 1)
//                                    Toast.makeText(mContext, "1개 이하는 주문할 수 없습니다.", Toast.LENGTH_SHORT).show();
//                                else {
//                                    for (int i = 0; i < MainActivity.sALSuppliesBasket.size(); i++) {
//                                        if (MainActivity.sALSuppliesBasket.get(i).getItem().equals(item))
//                                            MainActivity.sALSuppliesBasket.remove(i);
//                                    }
//
//                                    MainActivity.sALSuppliesBasket.add(new BasketVo(item, manufacturer, Integer.parseInt(etInputCount.getText().toString())));
////                                    writeDB(new BasketVo(item, manufacturer, Integer.parseInt(etInputCount.getText().toString())));
//                                    FirebaseBasketDB.getInstance(mContext).writeDB("소모품",
//                                            new BasketVo(item, manufacturer, Integer.parseInt(etInputCount.getText().toString())));
//                                }
//                            }
//                        })
//                .setCancelable(false)
//                .show();
//    }
}
