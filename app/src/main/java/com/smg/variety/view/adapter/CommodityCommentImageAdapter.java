package com.smg.variety.view.adapter;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.lwkandroid.imagepicker.data.ImageBean;
import com.lwkandroid.imagepicker.data.ImageContants;
import com.lwkandroid.imagepicker.data.ImageDataModel;
import com.lwkandroid.imagepicker.data.ImagePickerOptions;
import com.lwkandroid.imagepicker.ui.pager.view.ImagePagerActivity;
import com.smg.variety.R;
import com.smg.variety.common.Constants;
import com.smg.variety.common.utils.GlideUtils;
import com.smg.variety.view.mainfragment.ImagePreviewActivity;

import java.util.ArrayList;
import java.util.List;

public class CommodityCommentImageAdapter extends BaseQuickAdapter<String, BaseViewHolder> {
    public CommodityCommentImageAdapter(@Nullable List<String> data) {
        super(R.layout.adapter_comment_image,data);
    }
   private List<String> datas=new ArrayList<>();
    @Override
    protected void convert(BaseViewHolder helper, String item) {
        GlideUtils.getInstances().loadNormalImg(mContext, helper.getView(R.id.iv_commodity_comments_img),
                item, R.mipmap.img_default_1);
        View view = helper.getView(R.id.iv_commodity_comments_img);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //去预览界面
                datas.clear();
                List<String> data = getData();
                int i=0;
                int a=0;
                for(String url:data){
                  if(url.equals(item)){
                      a=i;
                  }
                  i++;
                    datas.add(url);
                }
                Bundle bundle = new Bundle();
                bundle.putStringArrayList(ImageContants.INTENT_KEY_DATA, (ArrayList)datas);
                bundle.putInt(ImageContants.INTENT_KEY_START_POSITION, a);
                Intent intent = new Intent(mContext, ImagePreviewActivity.class);
                intent.putExtras(bundle);
                mContext.startActivity(intent);
            }
        });
    }
}
