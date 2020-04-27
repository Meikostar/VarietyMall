package com.smg.variety.view.widgets;

import android.app.Dialog;
import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.donkingliang.labels.LabelsView;
import com.smg.variety.bean.BaseDto;
import com.jakewharton.rxbinding2.view.RxView;
import com.smg.variety.R;
import com.smg.variety.bean.CommodityDetailAttrDto;
import com.smg.variety.bean.CommodityDetailAttrItemDto;
import com.smg.variety.bean.CommodityDetailInfoDto;
import com.smg.variety.bean.GoodsAttrDto;
import com.smg.variety.common.Constants;
import com.smg.variety.common.utils.GlideUtils;
import com.smg.variety.common.utils.ToastUtil;
import com.smg.variety.utils.TextUtil;
import com.smg.variety.view.adapter.GoodsAttrAdapter;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observable;
import io.reactivex.functions.Action;

/**
 * 产品类型、规格
 * Created by rzb on 2018/6/18.
 */
public class ShopProductTypeDialog extends Dialog implements GoodsAttrAdapter.GoodsSpecListener {
    private static final String  TAG = ShopProductTypeDialog.class.getSimpleName();
    private              Context mContext;
    @BindView(R.id.iv_dialog_select_commodity_img)
    ImageView    iv_dialog_select_commodity_img;
    @BindView(R.id.tv_dialog_select_commodity_price)
    TextView     tv_dialog_select_commodity_price;
    @BindView(R.id.tv_dialog_select_commodity_num)
    TextView     tv_dialog_select_commodity_num;
    @BindView(R.id.but_dialog_select_commodity_submit)
    Button       butSubmit;
    @BindView(R.id.iv_dialog_select_commodity_clean)
    ImageView    clean;
    @BindView(R.id.iv_dialog_select_commodity_decrease)
    ImageView    iv_dialog_select_commodity_decrease;
    @BindView(R.id.tv_dialog_select_commodity_count)
    TextView     tv_dialog_select_commodity_count;
    @BindView(R.id.iv_dialog_select_commodity_increase)
    ImageView    iv_dialog_select_commodity_increase;
    @BindView(R.id.tv_commodity_count)
    TextView     tv_commodity_count;
    @BindView(R.id.tv_commodity_total_price)
    TextView     tv_commodity_total_price;
    @BindView(R.id.rv_dialog_select_commodity)
    RecyclerView recyclerView;
    private int                              goodscount     = 1;//数量
    private boolean                          isShoppingCart = false; //是否添加到购物车
    private ShopProductTypeListener          shopProductTypeListener;
    private GoodsAttrAdapter                 goodsAttrAdapter;
    private String                           stockId        = null;
    private String                           productId      = null;
    private List<CommodityDetailAttrItemDto> attrItemList;
    private LinearLayoutManager              mLinearLayoutManager;
    private List<String>                     attrList       = new ArrayList<String>();
    private String                           strPrice       = null;
    private CommodityDetailInfoDto           cDetailInfo;
    private int                              minBuy;

    public ShopProductTypeDialog(Context context, CommodityDetailInfoDto commodityDetailInfoDto, CommodityDetailAttrDto object, String imageUrl, String price, String title, ShopProductTypeListener listener, boolean isShoppingCart) {
        super(context, R.style.dialog_with_alpha);
        //      setCanceledOnTouchOutside(false);//设置点击外部不可以取消;
        this.mContext = context;
        setContentView(R.layout.dialog_shop_product_type);
        ButterKnife.bind(this);
        this.shopProductTypeListener = listener;
        this.isShoppingCart = isShoppingCart;
        this.cDetailInfo = commodityDetailInfoDto;
        initView();
        initData(object, imageUrl, price, title);
        initListener();
    }

    private void initView() {
        WindowManager manager = getWindow().getWindowManager();
        Display display = manager.getDefaultDisplay();
        WindowManager.LayoutParams p = getWindow().getAttributes();
        p.width = (display.getWidth());
        Window window = getWindow();
        window.setAttributes(p);
        window.setGravity(Gravity.BOTTOM);
        window.setWindowAnimations(R.style.bottomAnimStyle);
    }

    private void initListener() {
        bindClickEvent(iv_dialog_select_commodity_decrease, () -> {
            if (minBuy > 0 && goodscount - 1 < minBuy) {
                ToastUtil.showToast("购买数量不能小于" + minBuy);
                return;
            }
            if (goodscount <= 1) {
                goodscount = 1;
            } else {
                --goodscount;
            }
            tv_dialog_select_commodity_count.setText("" + goodscount);
            tv_commodity_count.setText("" + goodscount);
            double price = Double.valueOf(strPrice) * Double.parseDouble("" + goodscount);
            DecimalFormat decimalFormat = new DecimalFormat("0.00");//构造方法的字符格式这里如果小数不足2位,会以0补足.
            String distanceString = decimalFormat.format(price);
            tv_commodity_total_price.setText("¥" + distanceString);
        });
        bindClickEvent(iv_dialog_select_commodity_increase, () -> {
            ++goodscount;
            tv_dialog_select_commodity_count.setText("" + goodscount);
            tv_commodity_count.setText("" + goodscount);
            double price = Double.valueOf(strPrice) * Double.parseDouble("" + goodscount);
            DecimalFormat decimalFormat = new DecimalFormat("0.00");//构造方法的字符格式这里如果小数不足2位,会以0补足.
            String distanceString = decimalFormat.format(price);
            tv_commodity_total_price.setText("¥" + distanceString);
        });
        bindClickEvent(clean, () -> {
            hide();
        });
        bindClickEvent(butSubmit, () -> {
            if (null != shopProductTypeListener) {
                List<GoodsAttrDto> attList = goodsAttrAdapter.getData();
                if (attList != null) {
                    if (attList.size() > 0) {
                        if(TextUtil.isEmpty(stockId)){
                            getStockId();

                        }

                    } else {
                        if (isShoppingCart) {
                            productId = cDetailInfo.getId();
                        } else {
                            productId = cDetailInfo.getId();
                        }

                    }
                } else {
                    productId = cDetailInfo.getId();
                }
                if (isShoppingCart) {
                    shopProductTypeListener.callbackSelect(stockId, productId, tv_commodity_count.getText().toString());

                } else {
                    shopProductTypeListener.callbackSelect(stockId, productId, tv_commodity_count.getText().toString());
                }
            }
            hide();
        });
    }
    private List<Map<String, String>> mapList = new ArrayList<Map<String, String>>();
    private    String prices = null;
    private void initData(CommodityDetailAttrDto object, String imageUrl, String price, String title) {
        if (object == null) {
            return;
        }
        strPrice = price;
        String imgUrl = imageUrl;
        GlideUtils.getInstances().loadNormalImg(mContext, iv_dialog_select_commodity_img, imgUrl);
//        tv_dialog_select_commodity_price.setText("¥" + price);
        tv_dialog_select_commodity_num.setText(title);
//        tv_commodity_total_price.setText("¥" + price);
        if (cDetailInfo != null && cDetailInfo.getExt() != null && !TextUtils.isEmpty(cDetailInfo.getExt().getMin_buy())) {
            minBuy = Integer.valueOf(cDetailInfo.getExt().getMin_buy());
            goodscount = minBuy;
            tv_commodity_count.setText(cDetailInfo.getExt().getMin_buy());
            tv_dialog_select_commodity_count.setText("" + goodscount);
            double prices = Double.valueOf(strPrice) * Double.parseDouble("" + goodscount);
            DecimalFormat decimalFormat = new DecimalFormat("0.00");//构造方法的字符格式这里如果小数不足2位,会以0补足.
            String distanceString = decimalFormat.format(prices);
            tv_commodity_total_price.setText(distanceString);
        } else {
            tv_commodity_count.setText("1");
        }
        if (isShoppingCart) {
            butSubmit.setText("加入购物车");
        } else {
            butSubmit.setText("立即购买");
        }

        attrItemList = object.getData();
        Map<String, String> tmap = new HashMap<>();
        List<GoodsAttrDto> gadList = new ArrayList<>();
        List<GoodsAttrDto> totaDto = new ArrayList<>();

        if (attrItemList != null) {

            if (attrItemList.size() > 0) {
                CommodityDetailAttrItemDto AttrItemDto = attrItemList.get(0);
                String atStr = AttrItemDto.getAttr_str();
                prices = AttrItemDto.getPrice();
                String[] ats = atStr.split("\\|");
                for (int a = 0; a < ats.length; a++) {
                    Map<String, String> atmap = new HashMap<String, String>();
                    mapList.add(atmap);
                }
                for (int i = 0; i < attrItemList.size(); i++) {
                    CommodityDetailAttrItemDto commodityDetailAttrItemDto = attrItemList.get(i);

                    String attrStr = commodityDetailAttrItemDto.getAttr_str();
                    tmap.put(attrStr, commodityDetailAttrItemDto.getPrice());
                    String[] attrArray = attrStr.split("\\|");
                    HashMap<String, String> attrMp = new HashMap<String, String>();
                    for (int j = 0; j < attrArray.length; j++) {
                        String[] itemAttrArray = attrArray[j].split(":");
                        Map<String, String> map = mapList.get(j);
                        map.put(itemAttrArray[1], itemAttrArray[0]);
                    }

                }

                for (Map<String, String> maps : mapList) {
                    List<BaseDto> valueList = new ArrayList<BaseDto>();
                    String key = "";
                    int i=0;
                    for (Map.Entry<String, String> a : maps.entrySet()) {
                        BaseDto baseDto = new BaseDto();
                        if(i==0){

                            baseDto.isChoose=true;
                        }
                        baseDto.name = a.getKey();
                        baseDto.price = prices;
                        key = a.getValue();
                        valueList.add(baseDto);
                        i++;
                    }
                    GoodsAttrDto gaDto = new GoodsAttrDto();
                    gaDto.setKey(key);
                    gaDto.data = valueList;
                    gadList.add(gaDto);

                }
            }
        }

        mLinearLayoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(mLinearLayoutManager);
        recyclerView.setFocusable(false);

        goodsAttrAdapter = new GoodsAttrAdapter(gadList, mContext);
        goodsAttrAdapter.setGoodsSpecListener(new GoodsAttrAdapter.GoodsSpecListener() {
            @Override
            public void callbackGoodsSpec(String attStrs,String key) {
                List<GoodsAttrDto> data = goodsAttrAdapter.getData();
                contents.clear();

                for (GoodsAttrDto attrDto : data) {
                    for (BaseDto btdo : attrDto.data) {
                        if (btdo.isChoose) {
                            contents.put(attrDto.getKey(), attrDto.getKey() + ":" + btdo.name);
                        }
                    }
                }

                if (attrItemList != null) {

                    if (attrItemList.size() > 0) {

                        int hc = 0;
                        if(contents.size()==mapList.size()){
                            for (int i = 0; i < attrItemList.size(); i++) {

                                CommodityDetailAttrItemDto commodityDetailAttrItemDto = attrItemList.get(i);

                                String attrStr = commodityDetailAttrItemDto.getAttr_str();
                                int a = 0;
                                for (Map.Entry<String, String> bmap : contents.entrySet()) {
                                    if (!attrStr.contains(bmap.getValue())) {
                                        a=1;
                                    }
                                }
                                if(a==0){
                                    stockId = commodityDetailAttrItemDto.getId();
                                    strPrice = commodityDetailAttrItemDto.getPrice();
                                    tv_dialog_select_commodity_price.setText("¥" + commodityDetailAttrItemDto.getPrice());

                                    tv_commodity_total_price.setText("¥" + commodityDetailAttrItemDto.getPrice());

                                    hc=1;
                                }
                            }
                        }
                        if(hc==0){
                          ToastUtil.showToast("库存不足");
                        }


                    }
                }

            }
        });
        recyclerView.setAdapter(goodsAttrAdapter);
        getStockId();
    }

    private Map<String, String> contents = new HashMap<>();
    private  int allcout=4;
    private void getStockId() {
        List<GoodsAttrDto> data = goodsAttrAdapter.getData();
        contents.clear();

        for (GoodsAttrDto attrDto : data) {
            for (BaseDto btdo : attrDto.data) {
                if (btdo.isChoose) {
                    contents.put(attrDto.getKey(), attrDto.getKey() + ":" + btdo.name);
                }
            }
        }

        if (attrItemList != null) {

            if (attrItemList.size() > 0) {

                int hc = 0;
                if (contents.size() == mapList.size()) {
                    for (int i = 0; i < attrItemList.size(); i++) {

                        CommodityDetailAttrItemDto commodityDetailAttrItemDto = attrItemList.get(i);

                        String attrStr = commodityDetailAttrItemDto.getAttr_str();
                        int a = 0;
                        for (Map.Entry<String, String> bmap : contents.entrySet()) {
                            if (!attrStr.contains(bmap.getValue())) {
                                a = 1;
                            }
                        }
                        if (a == 0) {
                            stockId = commodityDetailAttrItemDto.getId();
                            strPrice = commodityDetailAttrItemDto.getPrice();
                            tv_dialog_select_commodity_price.setText("¥" + commodityDetailAttrItemDto.getPrice());

                            tv_commodity_total_price.setText("¥" + commodityDetailAttrItemDto.getPrice());

                            hc = 1;
                        }
                    }
                }
                if (hc == 0) {

                    ToastUtil.showToast("库存不足");
                    return;
                }


            }
        }
    }
    /**
     * 基本点击事件跳转默认节流1000毫秒
     *
     * @param view   绑定的view
     * @param action 跳转的Acticvity
     */
    protected void bindClickEvent(View view, final Action action) {
        bindClickEvent(view, action, 1000);
    }

    protected void bindClickEvent(View view, final Action action, long frequency) {
        Observable<Object> observable = RxView.clicks(view);
        if (frequency > 0) {
            observable.throttleFirst(frequency, TimeUnit.MILLISECONDS);
        }
        observable.subscribe(trigger -> action.run());
    }

    @Override
    public void callbackGoodsSpec(String attStrs,String key) {

    }

    public interface ShopProductTypeListener {
        /**
         * 回调
         *
         * @param stockId
         */
        void callbackSelect(String stockId, String productId, String countBuy);
    }
}
