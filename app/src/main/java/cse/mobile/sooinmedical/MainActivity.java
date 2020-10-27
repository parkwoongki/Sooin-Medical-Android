package cse.mobile.sooinmedical;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;

import cse.mobile.sooinmedical.vo.BasketVo;
import cse.mobile.sooinmedical.vo.MedicinesVo;
import cse.mobile.sooinmedical.vo.SuppliesVo;

public class MainActivity extends AppCompatActivity {
    private long backKeyPressedTime;

    public static ArrayList<SuppliesVo> sALSupplies;
    public static ArrayList<MedicinesVo> sALMedicines;

    public static ArrayList<BasketVo> sALSuppliesBasket;
    public static ArrayList<BasketVo> sALMedicinesBasket;

    public static ArrayList<BasketVo> sALSuppliesBookmark;
    public static ArrayList<BasketVo> sALMedicinesBookmark;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_search,  R.id.navigation_contact, R.id.navigation_bookmark)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);

        sALSupplies = DownloadActivity.tmpSupplies;
        sALMedicines = DownloadActivity.tmpMedicines;

        sALSuppliesBasket = (ArrayList<BasketVo>) getIntent().getSerializableExtra("SuppliesBasket");
        sALMedicinesBasket = (ArrayList<BasketVo>) getIntent().getSerializableExtra("MedicinesBasket");

        sALSuppliesBookmark = (ArrayList<BasketVo>) getIntent().getSerializableExtra("SuppliesBookmark");
        sALMedicinesBookmark = (ArrayList<BasketVo>) getIntent().getSerializableExtra("MedicinesBookmark");
    }

    /* 뒤로가기 2번하면 앱종료 */
    @Override
    public void onBackPressed() {
        // 1번째 백버튼 클릭
        if (System.currentTimeMillis() > backKeyPressedTime + 2000) {
            backKeyPressedTime = System.currentTimeMillis();
            Toast.makeText(this, "'뒤로' 버튼을 한번 더 누르시면 종료됩니다.", Toast.LENGTH_SHORT).show();
        }
        // 2번째 백버튼 클릭 (종료)
        else {
            this.finishAffinity();
            System.exit(0);
            android.os.Process.killProcess(android.os.Process.myPid());
        }
    }
}
