//package checkversion.solar.com.checkversion.adapter;
//
//import android.graphics.drawable.Drawable;
//import android.support.annotation.NonNull;
//import android.support.v7.widget.RecyclerView;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.ImageView;
//import android.widget.TextView;
//
//import checkversion.solar.com.checkversion.itemdata.ItemDataApp;
//import checkversion.solar.com.checkversion.R;
//
//public class AppAdapter extends RecyclerView.Adapter<AppAdapter.RecyclerViewHolder>{
//    private IAppAdapter iAppAdapter;
//
//    public AppAdapter(IAppAdapter iAppAdapter) {
//        this.iAppAdapter = iAppAdapter;
//    }
//
//    @NonNull
//    @Override
//    public RecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
//        LayoutInflater inflater=LayoutInflater.from(viewGroup.getContext());
//        View view=inflater.inflate(R.layout.item_app,viewGroup,false);
//        return new RecyclerViewHolder(view);
//    }
//
//    @Override
//    public void onBindViewHolder(@NonNull RecyclerViewHolder holder, int i) {
//        ItemDataApp item=iAppAdapter.getItemData(i);
//        holder.tvNameApp.setText(item.getNameApp());
//        holder.tvNamePackage.setText(item.getNamePackage());
//        holder.tvNameVersion.setText(item.getNameVersionApp());
//        holder.ivApp.setImageDrawable(iAppAdapter.getIconApp(i));
//        if(item.isFragVersion()){
//            holder.tvUpdateVersion.setVisibility(View.GONE);
//        }
//    }
//
//    @Override
//    public int getItemCount() {
//        return iAppAdapter.getItems();
//    }
//
//    public class RecyclerViewHolder extends RecyclerView.ViewHolder{
//        private TextView tvNameApp,tvNameVersion,tvNamePackage,tvUpdateVersion;
//        private ImageView ivApp;
//        public RecyclerViewHolder(@NonNull View view) {
//            super(view);
//            tvUpdateVersion=view.findViewById(R.id.tv_update_version);
//            tvNameApp=view.findViewById(R.id.tv_name_app);
//            tvNamePackage=view.findViewById(R.id.tv_package_name);
//            tvNameVersion=view.findViewById(R.id.tv_name_version_app);
//            ivApp=view.findViewById(R.id.iv_app);
//        }
//    }
//    public  interface IAppAdapter{
//        int getItems();
//        ItemDataApp getItemData(int position);
//        Drawable getIconApp(int position);
//    }
//
//}
//
//
