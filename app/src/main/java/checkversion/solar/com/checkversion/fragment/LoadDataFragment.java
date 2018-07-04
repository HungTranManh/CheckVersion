package checkversion.solar.com.checkversion.fragment;

import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import checkversion.solar.com.checkversion.customview.CustomRadarView;
import checkversion.solar.com.checkversion.R;
import checkversion.solar.com.checkversion.itemdata.ItemDataApp;

public class LoadDataFragment extends Fragment{
    private CustomRadarView customRadarView;
    private TextView tvNumberUpdate;
    private ProgressBar progressBar;
    private List<ItemDataApp> itemDataApps;
    private Button btnBack;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

       View view=inflater.inflate(R.layout.fragment_load_data,container,false);
        inits(view);
        return view;

    }
    private void inits(View view){
        itemDataApps=new ArrayList<>();
        customRadarView=view.findViewById(R.id.custom_radar);
        tvNumberUpdate=view.findViewById(R.id.tv_number_update);
        progressBar=view.findViewById(R.id.pb_load_app);
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
    }
}
