package checkversion.solar.com.checkversion.fragment;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
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
import checkversion.solar.com.checkversion.service.MyBirder;
import checkversion.solar.com.checkversion.service.VersionService;

public class LoadDataFragment extends Fragment {
    private CustomRadarView customRadarView;
    private TextView tvNumberUpdate, tvNameApp;
    private ProgressBar progressBar;
    private AsyncTask<Void, Integer, Void> checkVersion;
    private Handler handler = new Handler();
    private ImageView ivIcon;
    private Button btnBack;
    private ServiceConnection conn;
    private VersionService versionService;
    private int progress = 0;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_load_data, container, false);
        inits(view);
        return view;

    }

    private void inits(View view) {
        tvNameApp = view.findViewById(R.id.tv_name_app);
        ivIcon = view.findViewById(R.id.iv_icon);
        btnBack = view.findViewById(R.id.btn_back);
        customRadarView = view.findViewById(R.id.custom_radar);
        tvNumberUpdate = view.findViewById(R.id.tv_number_update);
        progressBar = view.findViewById(R.id.pb_load_app);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MainActivity) getActivity()).openFragmentStart();
            }
        });

        connectService();
    }

    private void connectService() {
        conn = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
                MyBirder birder = (MyBirder) iBinder;
                versionService = birder.getVersionService();
                progressBar.setMax(versionService.getSizeItem());
                progress = 0;
                runProgressBar();
            }

            @Override
            public void onServiceDisconnected(ComponentName componentName) {
                versionService = null;
            }
        };
        Intent intent = new Intent();
        intent.setClass(getContext(), VersionService.class);
        getContext().bindService(intent, conn, Context.BIND_AUTO_CREATE);

    }

    Runnable runnable = new Runnable() {
        @Override
        public void run() {
                progressBar.setProgress(progress);
                tvNumberUpdate.setText("Update wait:" + progress);
                ivIcon.setImageDrawable(drawable(versionService.getItemData(progress).getNamePackage()));
                tvNameApp.setText(versionService.getItemData(progress).getNameApp());
                handler.postDelayed(runnable, 300);
            if(progress >= versionService.getSizeItem() -1){
                ((MainActivity) getActivity()).openFragmentShowList();
                handler.removeCallbacks(runnable);
            }
                progress++;



        }

    };

    public void runProgressBar() {
        handler.removeCallbacks(runnable);
        handler.post(runnable);

    }


    private Drawable drawable(String packageName) {
        try {
            return getActivity().getPackageManager().getApplicationIcon(packageName);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void onDestroy() {
        handler.removeCallbacks(runnable);
        super.onDestroy();

    }
}
