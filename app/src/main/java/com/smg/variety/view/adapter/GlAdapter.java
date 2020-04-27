package com.smg.variety.view.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.Request;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.SizeReadyCallback;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.request.transition.Transition;
import com.jaeger.ninegridimageview.ItemImageClickListener;
import com.jaeger.ninegridimageview.ItemImageLongClickListener;
import com.jaeger.ninegridimageview.NineGridImageView;
import com.jaeger.ninegridimageview.NineGridImageViewAdapter;
import com.meiqia.meiqiasdk.util.MQUtils;
import com.smg.variety.R;
import com.smg.variety.bean.DetailDto;
import com.smg.variety.bean.TopicListItemDto;
import com.smg.variety.common.Constants;
import com.smg.variety.common.utils.GlideUtils;
import com.smg.variety.common.utils.StringUtil;
import com.smg.variety.common.utils.ToastUtil;
import com.smg.variety.common.utils.WebViewUtil;
import com.smg.variety.utils.TextUtil;
import com.smg.variety.view.activity.UploadSuperActivity;
import com.smg.variety.view.widgets.autoview.NoScrollWebView;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;

public class GlAdapter extends RecyclerView.Adapter<GlAdapter.PostViewHolder> {

    private LayoutInflater         mInflater;
    private List<DetailDto> mPostList;
    TopListItemClick childClick;
    private Context context;

    public GlAdapter(Context context, List<DetailDto> mPostList, TopListItemClick childClick) {
        super();
        this.context = context;
        this.childClick = childClick;
        this.mPostList = mPostList;
        mInflater = LayoutInflater.from(context);
    }

    public void setNewData(List<DetailDto> mPostList) {
        this.mPostList = mPostList;
        notifyDataSetChanged();
    }

    public void addData(List<DetailDto> mPostList) {
        if (this.mPostList == null) {
            this.mPostList = new ArrayList<>();
        }
        if (mPostList != null) {
            this.mPostList.addAll(mPostList);
            notifyDataSetChanged();
        }
    }

    public List<DetailDto> getmPostList() {
        return mPostList;
    }

    @Override
    public void onBindViewHolder(PostViewHolder holder, int position) {
        holder.bind(mPostList.get(position), position);
    }

    @Override
    public int getItemCount() {
        return mPostList != null ? mPostList.size() : 0;
    }

    @Override
    public PostViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new PostViewHolder(mInflater.inflate(R.layout.item_post_gl, parent, false));
    }

    public class PostViewHolder extends RecyclerView.ViewHolder {
        private LinearLayout              layout_topic_item;

        private ImageView                 img_user_header;
        private ImageView                 iv_content;
        private TextView                  tv_topic_follow;
        private TextView                  tv_title;
        private NoScrollWebView                  tv_topic_content;
        private TextView                  tv_click;
        private TextView                  tv_nickname;


        public PostViewHolder(View itemView) {
            super(itemView);

            img_user_header = (ImageView) itemView.findViewById(R.id.img_user_header);
            iv_content = (ImageView) itemView.findViewById(R.id.iv_content);
            tv_topic_follow = itemView.findViewById(R.id.tv_topic_follow);
            tv_title = (TextView) itemView.findViewById(R.id.tv_title);
            tv_topic_content = (NoScrollWebView) itemView.findViewById(R.id.webView_huodon);
            tv_click = (TextView) itemView.findViewById(R.id.tv_click);

            layout_topic_item = (LinearLayout) itemView.findViewById(R.id.layout_topic_item);



        }

        public void bind(DetailDto post, int position) {
            tv_title.setText(post.title);
            if(TextUtil.isNotEmpty(post.content)){
                WebViewUtil.setWebView(tv_topic_content, Objects.requireNonNull(context));
                WebViewUtil.loadHtml(tv_topic_content, post.content);
            }

            List<String> data=new ArrayList<>();
            if(TextUtil.isNotEmpty(post.img)){
                data.add(post.img);
            }
            GlideUtils.getInstances().loadNormalImg(context,iv_content,post.img);


            tv_topic_follow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String content = post.content;//content含有HTML标签
                    content = content.replace("<em>", "");
                    content = content.replace("</em>", "");
                    content = content.replace("<br>", "\n");
                    content = content.replace("</br>", "");
                    content = content.replace("&nbsp;", " ");
                    content = content.replace("<div>", "\n");
                    content = content.replace("</div>", "");
                    content = content.replace("<p>", "");
                    content = content.replace("</p>", "");
                    MQUtils.clipText(context,content);
                    ToastUtil.showToast("复制成功");
                }
            });
//
//            tv_nickname.setText(post.getUser().getData().getName());


//            GlideUtils.getInstances().loadRoundImg(context, img_user_header, Constants.WEB_IMG_URL_UPLOADS + post.getUser().getData().getAvatar());
            iv_content.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (childClick != null) {
                        childClick.onItemClick(post, position);
                    }
                }
            });

        }
    }

    public interface TopListItemClick {

        /*整个item项*/
        public void onItemClick(DetailDto post, int position);
    }

    public void gotoActivity(Class<?> clz, Bundle bundle) {
        Intent intent = new Intent(context, clz);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        context.startActivity(intent);
    }
}

