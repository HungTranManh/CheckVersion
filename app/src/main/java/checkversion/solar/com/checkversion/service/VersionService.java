package checkversion.solar.com.checkversion.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.IBinder;
import android.support.annotation.Nullable;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import checkversion.solar.com.checkversion.itemdata.ItemDataApp;

public class VersionService extends Service {
    private AsyncTask<String,Void,Void> asynUpdate;
    private List<ItemDataApp> itemDataApps;
    private AsyncTask<Void,Void,Void> checkVersion;
    private Context context;
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        MyBirder myBirder=new MyBirder(this);
        return myBirder;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        loadDataApp();
        return START_STICKY;
    }

    private void updateVersion(String namePackage){
        asynUpdate=new AsyncTask<String, Void, Void>() {
            String newVersion=null;
            @Override
            protected Void doInBackground(String... strings) {
                try {
                    newVersion = Jsoup.connect("https://play.google.com/store/apps/details?id=" +strings[0] + "&hl=it")
                            .timeout(30000)
                            .userAgent("Mozilla/5.0 (Windows; U; WindowsNT 5.1; en-US; rv1.8.1.6) Gecko/20070725 Firefox/2.0.0.6")
                            .referrer("http://www.google.com")
                            .get()
                            .select(".hAyfc .htlgb")
                            .get(7)
                            .ownText();
                    return null;
                } catch (Exception e) {
                    return null;
                }
            }

            @Override
            protected void onProgressUpdate(Void... values) {
                super.onProgressUpdate(values);
            }

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }
        }.execute(namePackage);
}
    private void loadDataApp() {
        PackageManager manager = getPackageManager();
        itemDataApps=new ArrayList<>();
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
        checkVersion();
    }
    private void checkVersion(){
        checkVersion=new AsyncTask<Void, Void, Void>() {
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

                }
                return null;
            }

            @Override
            protected void onProgressUpdate(Void... values) {
                super.onProgressUpdate(values);
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
    public void  setFlagVersion(int position,boolean is){
        itemDataApps.get(position).setFragVersion(is);
    }

    public  ItemDataApp getItemData(int position){
        return itemDataApps.get(position);
    }
    public int getSizeItem(){
        return  itemDataApps.size();
    }


}
