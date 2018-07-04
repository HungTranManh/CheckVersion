package checkversion.solar.com.checkversion;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;

import checkversion.solar.com.checkversion.customview.CustomeBackgroundScanApp;
import checkversion.solar.com.checkversion.fragment.LoadDataFragment;
import checkversion.solar.com.checkversion.fragment.StartFragment;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity);
        init();
    }
    private void init(){

        addFragmentStart();

    }
    private void addFragmentStart(){
        StartFragment startFragment=new StartFragment();
        FragmentManager manager=getSupportFragmentManager();
        FragmentTransaction transaction=manager.beginTransaction();
        transaction.replace(R.id.content1,startFragment,StartFragment.class.getSimpleName());
        transaction.commit();
    }
    public void openFragmentLoadAppData(){
        LoadDataFragment loadDataFragment=new LoadDataFragment();
        FragmentManager manager=getSupportFragmentManager();
        FragmentTransaction transaction=manager.beginTransaction();
        transaction.replace(R.id.content1,loadDataFragment,LoadDataFragment.class.getSimpleName());
       transaction.addToBackStack(null);
        transaction.commit();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
