package com.mylove.store;

import android.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;

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

        ImageView pic = (ImageView) view.findViewById(R.id.store_detail_show);
        if (datas != null) {
            ImageLoaderHelper.getInstance().load(getActivity(),datas.get(currentSel),pic);
        }

        return view;
    }

}
