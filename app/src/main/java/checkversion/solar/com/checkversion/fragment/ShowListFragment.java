package checkversion.solar.com.checkversion.fragment;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import checkversion.solar.com.checkversion.MainActivity;
import checkversion.solar.com.checkversion.adapter.DataAdapter;
import checkversion.solar.com.checkversion.itemdata.ItemDataApp;
import checkversion.solar.com.checkversion.R;
import checkversion.solar.com.checkversion.service.MyBirder;
import checkversion.solar.com.checkversion.service.VersionService;

public class ShowListFragment extends Fragment implements DataAdapter.IDataAdapter {
    private List<ItemDataApp> itemDataApps;
    //    private RecyclerView rcvApp;
//    private AppAdapter adapter;
    private ListView listView;
    private DataAdapter adapter;
    private Button btnBack;
    private ServiceConnection connection;
    private VersionService versionService;
    private static final String TAG = ShowListFragment.class.getName();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_show_list, container, false);
        inits(view);
        return view;
    }


    private void inits(View view) {
        connectionService();
        adapter = new DataAdapter(this);
        listView = view.findViewById(R.id.lv_app);
//        rcvApp = view.findViewById(R.id.rcv_app);
        btnBack = view.findViewById(R.id.btn_back);
//        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
//        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
//        rcvApp.setLayoutManager(linearLayoutManager);
        listView.setAdapter(adapter);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MainActivity) getActivity()).backFragment();
            }
        });
    }

    private  void connectionService(){
        connection=new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
                MyBirder myBirder=(MyBirder)iBinder;
                versionService=myBirder.getVersionService();
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onServiceDisconnected(ComponentName componentName) {

            }
        };
        Intent intent=new Intent();
        intent.setClass(getContext(),VersionService.class);
        getContext().bindService(intent,connection,getContext().BIND_AUTO_CREATE);
    }
    @Override
    public int getItems() {

        if(versionService==null) {
            return  0;
        }
        return versionService.getSizeItem();

    }

    @Override
    public void clickItem(int position) {
        ((MainActivity)getActivity()).openFragmentInformation(versionService.getItemData(position));
    }
    @Override
    public void showDialogDownload(String namePackage){
     versionService.updateVersion(namePackage);
    }


    @Override
    public ItemDataApp getItemData(int position) {
        if(versionService==null){
            return null;
        }
        return versionService.getItemData(position);
    }

    @Override
    public Drawable getIconApp(int position) {
        if (versionService==null){
            return null;
        }
        try {
            return getActivity().getPackageManager().getApplicationIcon(versionService.getItemData(position).getNamePackage());
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }


}

