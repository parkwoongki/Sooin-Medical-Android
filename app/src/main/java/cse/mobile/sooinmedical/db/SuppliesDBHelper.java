package cse.mobile.sooinmedical.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import java.util.ArrayList;

import cse.mobile.sooinmedical.vo.SuppliesVo;

public class SuppliesDBHelper extends SQLiteOpenHelper {
    // by Singleton
    private static SuppliesDBHelper db = null;

    public SuppliesDBHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    public static SuppliesDBHelper getDB(Context context) {
        if (db == null)
            db = new SuppliesDBHelper(context, "SUPPLIES", null, 1);

        return db;
    }

    // 데이터베이스 생성
    @Override
    public void onCreate(SQLiteDatabase db) {
        StringBuffer sb = new StringBuffer();

        sb.append("create table if not exists SUPPLIES (");
        sb.append("MANUFACTURER text not null, ");
        sb.append("ITEM text not null, ");
        sb.append("ID text not null, ");
        sb.append("STANDARD text not null)");

        db.execSQL(sb.toString());

        Log.e("DB: ", "Success");
    }

    // 데이터베이스 버전 업그레이드
    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("drop table if exists SUPPLIES ");
        onCreate(db);
    }

    // 레코드 추가
    public void insert(SuppliesVo suppliesVo) {
        SQLiteDatabase db = getWritableDatabase();
        StringBuffer sb = new StringBuffer();

        sb.append("insert into SUPPLIES (");
        sb.append("NO, MANUFACTURER, ITEM, STANDARD )");
        sb.append("values (?, ?, ?, ?, ?)");

        db.execSQL(sb.toString(), new Object[]{
                suppliesVo.getNo(),
                suppliesVo.getManufacturer(),
                suppliesVo.getItem(),
                suppliesVo.getStandard()
        });

        Log.e("Insert: ", "Success");
    }

    // 레코드 수정
    public void update(SuppliesVo suppliesVo) {
        SQLiteDatabase db = getWritableDatabase();
        StringBuffer sb = new StringBuffer();

        sb.append("update SUPPLIES set ");
        sb.append("MANUFACTURER = ?, ");
        sb.append("ITEM = ?, ");
        sb.append("STANDARD = ?, ");
        sb.append("where NO = ?");

        db.execSQL(sb.toString(), new Object[]{
                suppliesVo.getManufacturer(),
                suppliesVo.getItem(),
                suppliesVo.getStandard(),
                suppliesVo.getNo()
        });

        Log.e("Update: ", "Success");
    }

    // 레코드 삭제
    public void delete(SuppliesVo suppliesVo) {
        SQLiteDatabase db = getWritableDatabase();
        StringBuffer sb = new StringBuffer();

        sb.append("delete from SUPPLIES ");
        sb.append("where NO = ?");

        db.execSQL(sb.toString(), new Object[]{
                suppliesVo.getNo()
        });

        Log.e("Delete: ", "Success");
    }

    // 리스트 불러오기
    public ArrayList getList() {
        SQLiteDatabase db = getReadableDatabase();
        StringBuffer sb = new StringBuffer();
        ArrayList suppliesList = new ArrayList();

        sb.append("select ID, NO, MANUFACTURER, ITEM, STANDARD ");
        sb.append("from SUPPLIES ");
        sb.append("order by NO");

        Cursor cursor = db.rawQuery(sb.toString(), null);

        while (cursor.moveToNext()) {
            SuppliesVo suppliesVo = new SuppliesVo();

            suppliesVo.setNo(cursor.getString(0));
            suppliesVo.setManufacturer(cursor.getString(1));
            suppliesVo.setItem(cursor.getString(2));
            suppliesVo.setStandard(cursor.getString(3));

            suppliesList.add(suppliesVo);
        }

        Log.e("SelectAll: ", "Success");

        return suppliesList;
    }

    // 소모품 이름으로 조회
    public ArrayList getItemList(String item) {
        SQLiteDatabase db = getReadableDatabase();
        StringBuffer sb = new StringBuffer();
        ArrayList suppliesList = new ArrayList();

        sb.append("select ID, NO, MANUFACTURER, ITEM, STANDARD ");
        sb.append("from SUPPLIES ");
        sb.append("where ITEM like '%?%' ");
        sb.append("order by NO");

        Cursor cursor = db.rawQuery(sb.toString(), new String[]{
                item
        });

        while (cursor.moveToNext()) {
            SuppliesVo suppliesVo = new SuppliesVo();

            suppliesVo.setNo(cursor.getString(0));
            suppliesVo.setManufacturer(cursor.getString(1));
            suppliesVo.setItem(cursor.getString(2));
            suppliesVo.setStandard(cursor.getString(3));

            suppliesList.add(suppliesVo);
        }

        Log.e("SelectAll: ", "Success");

        return suppliesList;
    }

}
