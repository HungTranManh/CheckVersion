package checkversion.solar.com.checkversion.fragment;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import checkversion.solar.com.checkversion.MainActivity;
import checkversion.solar.com.checkversion.R;
import checkversion.solar.com.checkversion.itemdata.ItemDataApp;

public class InformationFragment extends Fragment implements View.OnClickListener
{
    private ImageView ivApp;
    private ItemDataApp item;
    private SimpleDateFormat dateFormat=new SimpleDateFormat("dd/MM/yyyy");
    private TextView tvNameApp, tvNameVersion, tvStorage, tvMemory, tvDate;
    private Button btnUpdate, btnOpen, btnShare,btnBack;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_infomation, container, false);
        init(view);
        setUpView();
        return view;
    }

    public void setItem(ItemDataApp item) {
        this.item = item;

    }

    private void init(View view) {
        ivApp = view.findViewById(R.id.iv_app);
        tvNameApp = view.findViewById(R.id.tv_name_app);
        tvNameVersion = view.findViewById(R.id.tv_name_version_app);
        tvDate = view.findViewById(R.id.tv_date);
        tvMemory = view.findViewById(R.id.tv_memory_cache);
        tvStorage = view.findViewById(R.id.tv_storage);
        btnUpdate = view.findViewById(R.id.btn_update);
        btnOpen = view.findViewById(R.id.btn_open);
        btnShare = view.findViewById(R.id.btn_share);
        btnBack=view.findViewById(R.id.btn_back);
        btnOpen.setOnClickListener(this);
        btnBack.setOnClickListener(this);
        btnShare.setOnClickListener(this);
        btnUpdate.setOnClickListener(this);
    }
    private void setUpView(){
        Drawable drawable=null;
        try {
            drawable=getActivity().getPackageManager().getApplicationIcon(item.getNamePackage());
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        ivApp.setImageDrawable(drawable);
        tvNameApp.setText(item.getNameApp());
        tvNameVersion.setText((item.getNameVersionApp()));
        try {
            long installed = getContext()
                    .getPackageManager()
                    .getPackageInfo(item.getNamePackage(), 0)
                    .firstInstallTime
                    ;
            tvDate.setText(dateFormat.format(new Date(installed)));
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        try {

            int m=getContext().getPackageManager().getPackageInfo(item.getNamePackage(), 0).installLocation;
            tvStorage.setText(getPath(item.getNamePackage())+"");
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }
    public Long getPath(String namePackage){
        String path;
        try {
            PackageManager manager=getActivity().getPackageManager();
            PackageInfo p=manager.getPackageInfo(namePackage,0);
            path = p.applicationInfo.dataDir;
            File file=new File(path);
            if(!file.isDirectory()){
                return file.length();
            }

        }
        catch (PackageManager.NameNotFoundException e) {
        }
        return  null;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_open:
                ((MainActivity) getActivity()).openApp(item.getNamePackage());
                break;
            case R.id.btn_share:
                ((MainActivity)getActivity()).shareApp(item);
                break;
            case R.id.btn_update:
                ((MainActivity)getActivity()).showUpdate(item.getNamePackage());
                break;
            case R.id.btn_back:
                ((MainActivity)getActivity()).backFragment();
            default:
                break;
        }
    }
}
