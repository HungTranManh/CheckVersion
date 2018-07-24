package checkversion.solar.com.checkversion.adapter;

import android.app.Dialog;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import checkversion.solar.com.checkversion.R;
import checkversion.solar.com.checkversion.itemdata.ItemDataApp;

public class DataAdapter extends BaseAdapter {
    private ImageView ivApp;
    private Button btnUpdate;
    private Dialog dialog;
    private TextView tvNameApp,tvNameVersion;
    private IDataAdapter iDataAdapter;
    private AsyncTask<String,Void,Void> asynUpdate;

    public DataAdapter(IDataAdapter iDataAdapter) {
        this.iDataAdapter = iDataAdapter;
    }

    @Override
    public int getCount() {
        return iDataAdapter.getItems();
    }

    @Override
    public Object getItem(int i) {
        return iDataAdapter.getItemData(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {
        if(view==null){
            LayoutInflater inflater=LayoutInflater.from(viewGroup.getContext());
            view=inflater.inflate(R.layout.item_app,viewGroup,false);
        }
        ivApp=view.findViewById(R.id.iv_app);
        tvNameApp=view.findViewById(R.id.tv_name_app);
        tvNameVersion=view.findViewById(R.id.tv_name_version_app);
        btnUpdate=view.findViewById(R.id.btn_update);
        final ItemDataApp item=iDataAdapter.getItemData(i);
        ivApp.setImageDrawable(iDataAdapter.getIconApp(i));
        tvNameApp.setText(item.getNameApp());
        tvNameVersion.setText("ver:"+item.getNameVersionApp());
        if(!item.isFragVersion()){
            btnUpdate.setVisibility(View.VISIBLE);
        }
        else {
            btnUpdate.setVisibility(View.GONE);
        }

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                iDataAdapter.showDialogDownload(item.getNamePackage());
                btnUpdate.setVisibility(View.INVISIBLE);
            }
        });
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                iDataAdapter.clickItem(i);
            }
        });
        return view;
    }
    public  interface IDataAdapter{
        int getItems();
        void clickItem(int position);
        void showDialogDownload(String namePackage);
        ItemDataApp getItemData(int position);
        Drawable getIconApp(int position);
    }

}
