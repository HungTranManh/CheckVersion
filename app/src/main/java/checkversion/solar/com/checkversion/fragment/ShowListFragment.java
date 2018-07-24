package checkversion.solar.com.checkversion.fragment;

import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
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

public class ShowListFragment extends Fragment implements DataAdapter.IDataAdapter {
    private List<ItemDataApp> itemDataApps;
    //    private RecyclerView rcvApp;
//    private AppAdapter adapter;
    private ListView listView;
    private DataAdapter adapter;
    private Button btnBack;
    private Executor executor;

    private static final String TAG = ShowListFragment.class.getName();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_show_list, container, false);
        inits(view);
        return view;
    }

    public void setItemDataApps(List<ItemDataApp> itemDataApps) {
        this.itemDataApps = itemDataApps;
    }

    private void inits(View view) {
        executor = Executors.newFixedThreadPool(3);
//        adapter = new AppAdapter(this);
        adapter = new DataAdapter(this);
        listView = view.findViewById(R.id.lv_app);
//        rcvApp = view.findViewById(R.id.rcv_app);
        btnBack = view.findViewById(R.id.btn_back);
//        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
//        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
//        rcvApp.setLayoutManager(linearLayoutManager);
        listView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MainActivity) getActivity()).backFragment();
            }
        });
    }



    @Override
    public int getItems() {
        return itemDataApps.size();
    }

    @Override
    public void clickItem(int position) {
        ((MainActivity)getActivity()).openFragmentInformation(itemDataApps.get(position));
    }
    @Override
    public void showDialogDownload(String namePackage) {
        ((MainActivity)getActivity()).showUpdate(namePackage);
    }


    @Override
    public ItemDataApp getItemData(int position) {
        return itemDataApps.get(position);
    }

    @Override
    public Drawable getIconApp(int position) {
        try {
            return getActivity().getPackageManager().getApplicationIcon(itemDataApps.get(position).getNamePackage());
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }


}

