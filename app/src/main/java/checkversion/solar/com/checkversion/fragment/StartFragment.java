package checkversion.solar.com.checkversion.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;

import checkversion.solar.com.checkversion.MainActivity;
import checkversion.solar.com.checkversion.R;
import checkversion.solar.com.checkversion.customview.CustomeBackgroundScanApp;

public class StartFragment extends Fragment {
    private CustomeBackgroundScanApp custom;
    private ImageView ivSearch;
    private Animation animation;
    private Button btnScan;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_start, container, false);
        inits(view);
        return view;
    }

    private void inits(View view) {
        custom=view.findViewById(R.id.background_custom);
        ivSearch=view.findViewById(R.id.iv_search);
        animation= AnimationUtils.loadAnimation(getContext(),R.anim.anim_search);
        ivSearch.setAnimation(animation);
        custom.startAnimation();
        btnScan=view.findViewById(R.id.btn_scan);
        btnScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                custom.stopAnimation();
                ((MainActivity)getActivity()).openFragmentLoadAppData();
            }
        });
    }
}
