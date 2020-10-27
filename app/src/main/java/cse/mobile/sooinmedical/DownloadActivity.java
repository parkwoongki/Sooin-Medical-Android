package cse.mobile.sooinmedical;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import cse.mobile.sooinmedical.vo.BasketVo;
import cse.mobile.sooinmedical.vo.MedicinesVo;
import cse.mobile.sooinmedical.vo.SuppliesVo;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;

public class DownloadActivity extends AppCompatActivity {
    private Intent intent;
    private ProgressDialog mLoadingDialog;

    public static ArrayList<SuppliesVo> tmpSupplies;
    public static ArrayList<MedicinesVo> tmpMedicines;

    private StorageReference mStorageRef;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.white_background);

        getSupportActionBar().hide();

        intent = new Intent(getApplicationContext(), MainActivity.class);

        mStorageRef = FirebaseStorage.getInstance().getReference();

        tmpSupplies = new ArrayList<>();
        tmpMedicines = new ArrayList<>();

        ArrayList<BasketVo> tmpSuppliesBasket = new ArrayList<>();
        ArrayList<BasketVo> tmpMedicinesBasket = new ArrayList<>();

        ArrayList<BasketVo> tmpSuppliesBookmark = new ArrayList<>();
        ArrayList<BasketVo> tmpMedicinesBookmark = new ArrayList<>();

        mLoadingDialog = new ProgressDialog(this);
        mLoadingDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        mLoadingDialog.setMessage("항목을 불러오는 중입니다.\n잠시만 기다려주세요.");
        mLoadingDialog.setCancelable(false);
        mLoadingDialog.setCanceledOnTouchOutside(false);
        mLoadingDialog.show();

        /* 소모품 바구니 불러오기 */
        FirebaseFirestore.getInstance().collection(FirebaseAuth.getInstance().getCurrentUser().getDisplayName() + "::소모품")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
//                                if (document.exists()) {
                                if (document.get("item") != null) {
                                    tmpSuppliesBasket.add(
                                            new BasketVo(
                                                    document.getString("item"),
                                                    document.getString("manufacturer"),
                                                    document.getLong("count"))
                                    );
                                }
                            }
                            intent.putExtra("SuppliesBasket", tmpSuppliesBasket);

                            /* 의약품 바구니 불러오기 */
                            FirebaseFirestore.getInstance().collection(FirebaseAuth.getInstance().getCurrentUser().getDisplayName() + "::의약품")
                                    .get()
                                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                            if (task.isSuccessful()) {
                                                for (QueryDocumentSnapshot document : task.getResult()) {
                                                    if (document.get("item") != null) {
                                                        tmpMedicinesBasket.add(
                                                                new BasketVo(
                                                                        document.getString("item"),
                                                                        document.getString("manufacturer"),
                                                                        document.getLong("count"))
                                                        );
                                                    }
                                                }
                                                intent.putExtra("MedicinesBasket", tmpMedicinesBasket);

                                                /* 소모품 북마크 불러오기 */
                                                FirebaseFirestore.getInstance().collection(FirebaseAuth.getInstance().getCurrentUser().getDisplayName() + "::소모품 북마크")
                                                        .get()
                                                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                                if (task.isSuccessful()) {
                                                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                                                        if (document.get("item") != null) {
                                                                            tmpSuppliesBookmark.add(
                                                                                    new BasketVo(
                                                                                            document.getString("item"),
                                                                                            document.getString("manufacturer"),
                                                                                            document.getLong("count"))
                                                                            );
                                                                        }
                                                                    }
                                                                    intent.putExtra("SuppliesBookmark", tmpSuppliesBookmark);

                                                                    /* 의약품 북마크 불러오기 */
                                                                    FirebaseFirestore.getInstance().collection(FirebaseAuth.getInstance().getCurrentUser().getDisplayName() + "::의약품 북마크")
                                                                            .get()
                                                                            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                                                @Override
                                                                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                                                    if (task.isSuccessful()) {
                                                                                        for (QueryDocumentSnapshot document : task.getResult()) {
                                                                                            if (document.get("item") != null) {
                                                                                                tmpMedicinesBookmark.add(
                                                                                                        new BasketVo(
                                                                                                                document.getString("item"),
                                                                                                                document.getString("manufacturer"),
                                                                                                                document.getLong("count"))
                                                                                                );
                                                                                            }
                                                                                        }
                                                                                        intent.putExtra("MedicinesBookmark", tmpMedicinesBookmark);

                                                                                        suppliesExcelDownload();
                                                                                    } else {
                                                                                        Toast.makeText(getApplicationContext(), "의약품 북마크 불러오기에 실패하였습니다.", Toast.LENGTH_SHORT).show();
                                                                                        mLoadingDialog.dismiss();
                                                                                    }
                                                                                }
                                                                            });
                                                                } else {
                                                                    Toast.makeText(getApplicationContext(), "소모품 북마크 불러오기에 실패하였습니다.", Toast.LENGTH_SHORT).show();
                                                                    mLoadingDialog.dismiss();
                                                                }
                                                            }
                                                        });
                                            } else {
                                                Toast.makeText(getApplicationContext(), "의약품 바구니 불러오기에 실패하였습니다.", Toast.LENGTH_SHORT).show();
                                                mLoadingDialog.dismiss();
                                            }
                                        }
                                    });
                        } else {
                            Toast.makeText(getApplicationContext(), "소모품 바구니 불러오기에 실패하였습니다.", Toast.LENGTH_SHORT).show();
                            mLoadingDialog.dismiss();
                        }
                    }
                });
    }

    /* 소모품 엑셀 다운로드 메서드 */
    private void suppliesExcelDownload() {
        try {
            final File localFile = File.createTempFile("supplies", ".xls");
            StorageReference spaceRef = mStorageRef.child("product/supplies.xls");
            spaceRef.getFile(localFile)
                    .addOnSuccessListener(taskSnapshot -> {
                        suppliesExcelReader(localFile);
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    Toast.makeText(getApplicationContext(), "소모품 목록 다운로드에 실패하였습니다.\n네트워크 연결 상태를 확인해주세요.", Toast.LENGTH_SHORT).show();
                    mLoadingDialog.dismiss();
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
            mLoadingDialog.dismiss();
        }
    }

    /* 소모품 엑셀 읽기 메서드 */
    private void suppliesExcelReader(File xlsFile) {
        try {
            Workbook wb = Workbook.getWorkbook(xlsFile);

            if (wb != null) {
                Sheet sheet = wb.getSheet(0);   // 시트 불러오기
                if (sheet != null) {
                    int colTotal = sheet.getColumns();    // 전체 컬럼
                    int rowIndexStart = 1;                  // row 인덱스 시작
                    int rowTotal = sheet.getColumn(colTotal - 1).length;

                    for (int row = rowIndexStart; row < rowTotal; row++) {
                        tmpSupplies.add(new SuppliesVo(sheet.getCell(0, row).getContents(),
                                sheet.getCell(1, row).getContents(),
                                sheet.getCell(2, row).getContents(),
                                sheet.getCell(3, row).getContents())
                        );
                    }

//                    intent.putExtra("Supplies", tmpSupplies);

                    medicinesExcelDownload();
                }
            }
//            mLoadingDialog.dismiss();
        } catch (IOException | BiffException e) {
            e.printStackTrace();
            mLoadingDialog.dismiss();
        }
    }

    /* 의약품 엑셀 다운로드 메서드 */
    private void medicinesExcelDownload() {
        try {
            final File localFile = File.createTempFile("medicines", ".xls");
            StorageReference spaceRef = mStorageRef.child("product/medicines.xls");
            spaceRef.getFile(localFile)
                    .addOnSuccessListener(taskSnapshot -> {
                        medicinesExcelReader(localFile);
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    Toast.makeText(getApplicationContext(), "소모품 및 의약품 목록 다운로드에 실패하였습니다.\n네트워크 연결 상태를 확인해주세요.", Toast.LENGTH_SHORT).show();
                    mLoadingDialog.dismiss();
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
            mLoadingDialog.dismiss();
        }
    }

    /* 의약품 엑셀 읽기 메서드 */
    private void medicinesExcelReader(File xlsFile) {
        try {
            Workbook wb = Workbook.getWorkbook(xlsFile);

            if (wb != null) {
                Sheet sheet = wb.getSheet(0);
                if (sheet != null) {
                    int colTotal = sheet.getColumns();
                    int rowIndexStart = 1;
                    int rowTotal = sheet.getColumn(colTotal - 1).length;

                    for (int row = rowIndexStart; row < rowTotal; row++) {
                        tmpMedicines.add(new MedicinesVo(sheet.getCell(0, row).getContents(),
                                sheet.getCell(1, row).getContents(),
                                sheet.getCell(2, row).getContents(),
                                sheet.getCell(3, row).getContents(),
                                sheet.getCell(4, row).getContents(),
                                sheet.getCell(5, row).getContents(),
                                sheet.getCell(6, row).getContents(),
                                sheet.getCell(7, row).getContents(),
                                sheet.getCell(8, row).getContents(),
                                sheet.getCell(9, row).getContents(),
                                sheet.getCell(10, row).getContents())
                        );
                    }

//                    intent.putExtra("Medicines", tmpMedicines);

                    startActivity(intent);
                    overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                }
            }
        } catch (IOException | BiffException e) {
            e.printStackTrace();
            mLoadingDialog.dismiss();
        }
    }

}
