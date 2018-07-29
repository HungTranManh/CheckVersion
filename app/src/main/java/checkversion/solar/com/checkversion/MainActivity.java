package checkversion.solar.com.checkversion;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import java.util.List;

import checkversion.solar.com.checkversion.fragment.InformationFragment;
import checkversion.solar.com.checkversion.fragment.LoadDataFragment;
import checkversion.solar.com.checkversion.fragment.ShowListFragment;
import checkversion.solar.com.checkversion.fragment.StartFragment;
import checkversion.solar.com.checkversion.itemdata.ItemDataApp;
import checkversion.solar.com.checkversion.service.VersionService;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity);
        init();
    }
    private void init() {
        startService();
        openFragmentStart();

    }

    public void showUpdate(final String namePackage) {
        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse
                ("market://details?id=" + namePackage)));
    }

    public void openFragmentStart() {
        StartFragment startFragment = new StartFragment();
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.add(R.id.content1, startFragment, StartFragment.class.getSimpleName());
        transaction.addToBackStack(null);
        transaction.commit();
    }

    public void openFragmentLoadAppData() {
        LoadDataFragment loadDataFragment = new LoadDataFragment();
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.content1, loadDataFragment, LoadDataFragment.class.getSimpleName());
        transaction.addToBackStack(null);
        transaction.commit();
    }


    public void openFragmentShowList() {
        ShowListFragment showListFragment = new ShowListFragment();
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        Fragment fragment=getSupportFragmentManager().findFragmentByTag(LoadDataFragment.class.getSimpleName());
        transaction.remove(fragment);
        transaction.replace(R.id.content1, showListFragment, ShowListFragment.class.getSimpleName());
        transaction.addToBackStack(null);
        transaction.commit();
    }

    public void openFragmentInformation(ItemDataApp item) {
        InformationFragment fragment = new InformationFragment();
        fragment.setItem(item);
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.content1, fragment, InformationFragment.class.getSimpleName());
        transaction.addToBackStack(null);
        transaction.commit();
    }

    public void openApp(String namePackage) {
        Intent openIntent = getPackageManager().getLaunchIntentForPackage(namePackage);
        if (openIntent != null) {
            startActivity(openIntent);//null pointer check in case package name was not found
        }
    }
    public void shareApp(ItemDataApp item){
        try {
            Intent i = new Intent(Intent.ACTION_SEND);
            i.setType("text/plain");
            i.putExtra(Intent.EXTRA_SUBJECT, item.getNameApp());
            String sAux = "\nLet me recommend you this application\n\n";
            sAux = sAux + "https://play.google.com/store/apps/details?id="+item.getNamePackage()+" \n\n";
            i.putExtra(Intent.EXTRA_TEXT, sAux);
            startActivity(Intent.createChooser(i, "choose one"));
        } catch(Exception e) {
        }

    }
private void startService(){
        Intent intent=new Intent();
        intent.setClass(this, VersionService.class);
        startService(intent);
}
    public void backFragment() {
        super.onBackPressed();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
