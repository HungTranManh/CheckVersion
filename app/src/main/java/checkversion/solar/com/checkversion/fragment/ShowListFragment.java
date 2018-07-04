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

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import checkversion.solar.com.checkversion.adapter.AppAdapter;
import checkversion.solar.com.checkversion.itemdata.ItemDataApp;
import checkversion.solar.com.checkversion.R;

public class ShowListFragment extends Fragment implements AppAdapter.IAppAdapter {
    private List<ItemDataApp> itemDataApps;
    private RecyclerView rcvApp;
    private AppAdapter adapter;
    private Executor executor;
    private AsyncTask<Integer, Void, Void> asyncCheckVersion;
    private static final String TAG = ShowListFragment.class.getName();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_show_list,container,false);
        inits(view);
        return  view;
    }

    private void inits(View view) {
        executor = Executors.newFixedThreadPool(3);
        adapter = new AppAdapter(this);
        itemDataApps = new ArrayList<>();
        rcvApp = view.findViewById(R.id.rcv_app);
        checkVersion(itemDataApps);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rcvApp.setLayoutManager(linearLayoutManager);
        adapter.notifyDataSetChanged();
        destroyAsyn();
    }
    private void checkVersion(List<ItemDataApp> itemDataApps) {
        for (int i=0;i<itemDataApps.size();i++) {
            getVersionApp(i);
        }
        rcvApp.setAdapter(adapter);
    }

    private void getVersionApp(int position) {
//            Document document=Jsoup.parse("https://play.google.com/store/apps/details?id="
//                    + packageName);
//            Elements elements=document.select("div.hAyfc");
//            String CurrentVersion=elements.get(3).selectFirst("span").attr("span");
        asyncCheckVersion = new AsyncTask<Integer, Void, Void>() {
            @Override
            protected Void doInBackground(Integer... position) {
                String newVersion=null;
                try {
                    Document document = Jsoup.connect("https://play.google.com/store/apps/details?id=" + itemDataApps.get(position[0]).getNamePackage() + "&hl=en")
                            .timeout(30000)
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
                if(newVersion!=null&&newVersion.equals(itemDataApps.get(position[0]).getNameVersionApp())){
                    itemDataApps.get(position[0]).setFragVersion(true);
                }else if(newVersion==null){
                    itemDataApps.get(position[0]).setFragVersion(true);
                }
                else {
                    itemDataApps.get(position[0]).setFragVersion(false);
                }
                return null;
            }


        }.execute(position);
    }

    @Override
    public int getItems() {
        return itemDataApps.size();
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
    private void destroyAsyn(){
        if(asyncCheckVersion!=null){
            asyncCheckVersion.cancel(true);

        }
    }

}

