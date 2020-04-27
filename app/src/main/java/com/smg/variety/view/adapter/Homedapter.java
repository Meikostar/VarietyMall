package com.smg.variety.view.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.smg.variety.R;
import com.smg.variety.bean.BannerDto;
import com.smg.variety.common.utils.GlideUtils;
import com.smg.variety.common.utils.ToastUtil;
import com.smg.variety.utils.TextUtil;
import com.smg.variety.view.activity.ProductListActivity;
import com.smg.variety.view.activity.WebViewActivity;
import com.smg.variety.view.mainfragment.consume.ProductSearchResultActivity;

import java.util.List;

/**
 * Created by mykar on 161/4/13.
 */

public class Homedapter extends BaseAdapter {
    private Context        context;
    private LayoutInflater inflater;
    private List<BannerDto>   list;
    private int            type = 0;//0 表示默认使用list数据
    private String         types;


    private int[] imgs;


    private String[] names;

    public Homedapter(Context context) {
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    public void setData(List<BannerDto> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return list!=null?(list.size()>=10?10:list.size()):0;
    }

    @Override
    public Object getItem(int i) {
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder holder;
        if (view == null) {
            holder = new ViewHolder();
            view = inflater.inflate(R.layout.tools_item, null);
            holder.txt_name = view.findViewById(R.id.txt_name);
            holder.img_icon = view.findViewById(R.id.img_icon);
            holder.llbg = view.findViewById(R.id.ll_bg);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        BannerDto bannerDto = list.get(i);

        if(!TextUtils.isEmpty(bannerDto.title)){
            holder.txt_name.setText(bannerDto.title);
        }
        holder.llbg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(TextUtil.isNotEmpty(bannerDto.url)){
                    Intent intent = new Intent(context, WebViewActivity.class);

                    intent.putExtra(WebViewActivity.WEBTITLE, bannerDto.title);
                    intent.putExtra(WebViewActivity.WEBURL, bannerDto.url);
                    context.startActivity(intent   );
                }else {
                    ToastUtil.showToast("暂未开放");

                }

//                Intent intent = new Intent(context, ProductListActivity.class);
//                Bundle bundle = new Bundle();
//                bundle.putString(ProductSearchResultActivity.ACTION_SEARCH_ID,bannerDto.id);
//                intent.putExtras(bundle);
//                context.startActivity(intent);
            }
        });
        GlideUtils.getInstances().loadNormalImg(context,holder.img_icon,bannerDto.img,R.drawable.moren_sf);
        // PROFILE_ITEM item = list.get(i);
        return view;
    }



    class ViewHolder {
        TextView     txt_name;
        ImageView    img_icon;
        LinearLayout llbg;

    }
}
