package cse.mobile.sooinmedical.firebase;

import android.app.ProgressDialog;
import android.content.Context;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

import cse.mobile.sooinmedical.vo.BasketVo;

public class FirebaseBasketDB {
    private static FirebaseBasketDB firebaseBasketDB;
    private static Context mContext;
    private static ProgressDialog mLoadingDialog;

    private FirebaseBasketDB() {
    }

    public static FirebaseBasketDB getInstance(Context context) {
        mContext = context;
        mLoadingDialog = new ProgressDialog(context);
        mLoadingDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        mLoadingDialog.setCancelable(false);
        mLoadingDialog.setCanceledOnTouchOutside(false);

        if (firebaseBasketDB == null) {
            firebaseBasketDB = new FirebaseBasketDB();
        }

        return firebaseBasketDB;
    }

    /* 쓰기 수정 DB */
    public void writeDB(String key, BasketVo vo) {
        mLoadingDialog.setMessage("바구니에 담는 중입니다.\n잠시만 기다려주세요.");
        mLoadingDialog.show();

        FirebaseFirestore.getInstance().collection(FirebaseAuth.getInstance().getCurrentUser().getDisplayName() + "::" + key)
                .document(vo.getItem().replace("/", "*"))
                .set(vo)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        mLoadingDialog.dismiss();
                        Toast.makeText(mContext, "바구니에 추가되었습니다.", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        mLoadingDialog.dismiss();
                        Toast.makeText(mContext, "바구니에 추가하는데 실패하였습니다.", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    /* 삭제 DB */
    public void deleteDB(String key, String item) {
        mLoadingDialog.setMessage("삭제 중입니다.\n잠시만 기다려주세요.");
        mLoadingDialog.show();

        FirebaseFirestore.getInstance().collection(FirebaseAuth.getInstance().getCurrentUser().getDisplayName() + "::" + key)
                .document(item.replace("/", "*"))
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        mLoadingDialog.dismiss();
                        Toast.makeText(mContext, "삭제되었습니다.", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        mLoadingDialog.dismiss();
                        Toast.makeText(mContext, "삭제에 실패하였습니다.", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    /* 바구니 전체 삭제 DB */
    public void deleteAllDB(String key, ArrayList<BasketVo> basketVos) {
        for (int i = 0; i < basketVos.size(); i++) {
            FirebaseFirestore.getInstance().collection(FirebaseAuth.getInstance().getCurrentUser().getDisplayName() + "::" + key)
                    .document(basketVos.get(i).getItem().replace("/", "*"))
                    .delete()
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                        }
                    });
        }
    }

    /* 읽기 DB */
    public void readDB(String key, ArrayList<BasketVo> sALBasket) {
        mLoadingDialog.setMessage("불러오는 중입니다.\n잠시만 기다려주세요.");
        mLoadingDialog.show();

        FirebaseFirestore.getInstance().collection(FirebaseAuth.getInstance().getCurrentUser().getDisplayName() + "::" + key)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
//                                if (document.exists()) {
                                if (document.get("item") != null) {
                                    sALBasket.add(
                                            new BasketVo(
                                                    document.getString("item"),
                                                    document.getString("manufacturer"),
                                                    document.getLong("count"))
                                    );
                                }
                            }
                            mLoadingDialog.dismiss();
                        } else {
                            Toast.makeText(mContext, "에러", Toast.LENGTH_SHORT).show();
                            mLoadingDialog.dismiss();
                        }
                    }
                });
    }

    /* 주문이력 저장 DB */
    public void bookmarkDB(String key, ArrayList<BasketVo> basketVos) {
        for (int i = 0; i < basketVos.size(); i++) {
            FirebaseFirestore.getInstance().collection(FirebaseAuth.getInstance().getCurrentUser().getDisplayName() + "::" + key)
                    .document(basketVos.get(i).getItem().replace("/", "*"))
                    .set(basketVos.get(i))
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            mLoadingDialog.dismiss();
                        }
                    });
        }
    }
}
