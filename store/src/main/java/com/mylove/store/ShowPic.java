package com.mylove.store;

import android.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.ViewFlipper;

import com.mylove.module_base.helper.ImageLoaderHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/7/21.
 */

public class ShowPic extends DialogFragment {

    private int currentSel;
    private List<String> datas;

    public ShowPic() {

    }

    public static ShowPic newInstance(int currentSel, ArrayList<String> datas){
        ShowPic showPic = new ShowPic();
        Bundle bundle = new Bundle();
        bundle.putInt("CURRENTSEL", currentSel);
        bundle.putStringArrayList("DATAS",datas);
        showPic.setArguments(bundle);
        return showPic;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        Bundle bundle = getArguments();
        currentSel = bundle.getInt("CURRENTSEL");
        datas = bundle.getStringArrayList("DATAS");
        View view = inflater.inflate(R.layout.module_store_show_pic,container);

        ViewFlipper viewFlipper = (ViewFlipper)view.findViewById(R.id.show_detail_view_flipper);

        if (datas != null) {
            ImageView imageView = new ImageView(getActivity());
            ImageLoaderHelper.getInstance().load(getActivity(),datas.get(currentSel),imageView);
            viewFlipper.addView(imageView);
            for (int i=0; i<datas.size(); i++){
                if (i == currentSel) continue;
                imageView = new ImageView(getActivity());
                ImageLoaderHelper.getInstance().load(getActivity(),datas.get(i),imageView);
                viewFlipper.addView(imageView);
            }
        }
        viewFlipper.setInAnimation(getActivity(),android.R.anim.slide_in_left);
        viewFlipper.setOutAnimation(getActivity(),android.R.anim.slide_out_right);
        viewFlipper.setFlipInterval(3000);

        viewFlipper.startFlipping();

        return view;
    }

}
