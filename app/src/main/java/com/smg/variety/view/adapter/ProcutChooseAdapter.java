package com.smg.variety.view.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.lwkandroid.imagepicker.data.ImageBean;
import com.smg.variety.R;
import com.smg.variety.common.Constants;
import com.smg.variety.common.utils.GlideUtils;

import java.util.List;


public class ProcutChooseAdapter extends BaseQuickAdapter<ImageBean, BaseViewHolder> {
    private int maxImgCount=100;


    public ProcutChooseAdapter(List<ImageBean> list) {
        super(R.layout.item_procut_choose, list);


    }

    @Override
    protected void convert(BaseViewHolder helper, ImageBean item) {
        if (!Constants.IMAGEITEM_DEFAULT_ADD.equals( item.getImagePath())) {
            helper.setGone(R.id.iv_pic, true)
                    .addOnClickListener(R.id.iv_pic);
            GlideUtils.getInstances().loadNormalImg(mContext, helper.getView(R.id.iv_pic), item.getImagePath());
        } else {

            helper.setImageResource(R.id.iv_pic, R.drawable.live_add);
        }
    }

    public void refresh() {


        if (getItemCount() > maxImgCount && Constants.IMAGEITEM_DEFAULT_ADD.equals(getData().get(getItemCount() - 1).getImagePath())) {
            //del last
            getData().remove(getItemCount() - 1);
        }else if (getItemCount() < maxImgCount) {
            if (getItemCount() == 0 || (!Constants.IMAGEITEM_DEFAULT_ADD.equals(getData().get(getItemCount() - 1).getImagePath()) && getItemCount() < maxImgCount)) {
                ImageBean imageItem = new ImageBean();
                imageItem.setImagePath(Constants.IMAGEITEM_DEFAULT_ADD);
                getData().add(imageItem);
            }
        }
        notifyDataSetChanged();
    }
}


