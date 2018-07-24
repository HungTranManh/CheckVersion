package checkversion.solar.com.checkversion.fragment;

import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import checkversion.solar.com.checkversion.MainActivity;
import checkversion.solar.com.checkversion.customview.CustomRadarView;
import checkversion.solar.com.checkversion.R;
import checkversion.solar.com.checkversion.itemdata.ItemDataApp;

public class LoadDataFragment extends Fragment{
    private CustomRadarView customRadarView;
    private TextView tvNumberUpdate,tvNameApp;
    private ProgressBar progressBar;
    private AsyncTask<Void,Integer,Void> checkVersion;
    private Handler handler=new Handler();
    private List<ItemDataApp> itemDataApps;
    private ImageView ivIcon;
    private Button btnBack;
    private  int progress=0;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view=inflater.inflate(R.layout.fragment_load_data,container,false);
        inits(view);
        return view;

    }

    private void inits(View view){
        itemDataApps=new ArrayList<>();
        tvNameApp=view.findViewById(R.id.tv_name_app);
        ivIcon=view.findViewById(R.id.iv_icon);
        btnBack=view.findViewById(R.id.btn_back);
        customRadarView=view.findViewById(R.id.custom_radar);
        tvNumberUpdate=view.findViewById(R.id.tv_number_update);
        progressBar=view.findViewById(R.id.pb_load_app);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cancelProgress();
                destroyAsyn();
                ((MainActivity)getActivity()).openFragmentStart();
            }
        });
        loadDataApp();
    }
    private void loadDataApp() {
        PackageManager manager = getActivity().getPackageManager();
        List<ApplicationInfo> lstAppInfo = manager.getInstalledApplications(PackageManager.GET_META_DATA);
        for (ApplicationInfo aInfo : lstAppInfo) {
            if ((aInfo.flags & ApplicationInfo.FLAG_SYSTEM) != 0) {

            } else {

                ItemDataApp appInfo = new ItemDataApp();

                appInfo.setNameApp(aInfo.loadLabel(manager).toString());
                appInfo.setNamePackage(aInfo.packageName);
                try {
                    PackageInfo info = manager.getPackageInfo(aInfo.packageName, 0);
                    appInfo.setNameVersionApp(info.versionName.toString());
                    appInfo.setCodeVersion(info.versionCode);

                    itemDataApps.add(appInfo);
                } catch (PackageManager.NameNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }
        progressBar.setMax(itemDataApps.size());
        checkVersion();

    }

    Runnable runnable=new Runnable() {
        @Override
        public void run() {
            progressBar.setProgress(progress);
            tvNumberUpdate.setText("Update wait:"+progress);
            if(progress>=itemDataApps.size()-1){
                cancelProgress();
                destroyAsyn();
                ((MainActivity)getActivity()).openFragmentShowList(itemDataApps);
                return;
            }
        }
    };
    public void runProgressBar(){
        handler.removeCallbacks(runnable);
        handler.post(runnable);

    }

    private void checkVersion(){
        checkVersion=new AsyncTask<Void, Integer, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                for (int i = 0; i < itemDataApps.size(); i++) {
                    String newVersion = null;
                    try {
                        Document document = Jsoup.connect("https://play.google.com/store/apps/details?id=" + itemDataApps.get(i).getNamePackage() + "&hl=en")
                                .timeout(20000)
                                .userAgent("Mozilla/5.0 (Windows; U; WindowsNT 5.1; en-US; rv1.8.1.6) Gecko/20070725 Firefox/2.0.0.6")
                                .referrer("http://www.google.com")
                                .get();
                        if (document != null) {
                            Elements element = document.getElementsContainingOwnText("Current Version");
                            for (Element ele : element) {
                                if (ele.siblingElements() != null) {
                                    Elements sibElemets = ele.siblingElements();
                                    for (Element sibElemet : sibElemets) {
                                        newVersion = sibElemet.text();
                                    }
                                }
                            }
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    if (newVersion != null && newVersion.equals(itemDataApps.get(i).getNameVersionApp())) {
                        itemDataApps.get(i).setFragVersion(true);
                    } else if (newVersion == null) {
                        itemDataApps.get(i).setFragVersion(true);
                    } else if (!itemDataApps.get(i).getNameVersionApp().equals(newVersion) && newVersion != null) {
                        itemDataApps.get(i).setFragVersion(false);
                    }
                    if (newVersion != null && !(isVersion(newVersion))) {
                        itemDataApps.get(i).setFragVersion(true);
                    }

                    publishProgress(i);
                    runProgressBar();
                    handler.postDelayed(runnable, 200);
                    progress++;
                }
                return null;
            }

            @Override
            protected void onProgressUpdate(Integer... values) {
                super.onProgressUpdate(values);
                tvNameApp.setText(itemDataApps.get(values[0]).getNameApp());
                ivIcon.setImageDrawable(drawable(itemDataApps.get(values[0]).getNamePackage()));
            }
        }.execute();
    }

    private  boolean isVersion(String nameVersion){
        for (int i=0;i<nameVersion.length();i++){
            if(48<=nameVersion.charAt(i)&&nameVersion.charAt(i)<=57){
                return true;
            }
        }
        return false;
    }

    private Drawable drawable(String packageName){
        try {
            return getActivity().getPackageManager().getApplicationIcon(packageName);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return  null;
    }
    public void cancelProgress(){
        handler.removeCallbacks(runnable);
    }
    private void destroyAsyn() {
        if (checkVersion != null) {
            checkVersion.cancel(true);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        cancelProgress();
        destroyAsyn();
    }
}
