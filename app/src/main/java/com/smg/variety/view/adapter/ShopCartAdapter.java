package com.smg.variety.view.adapter;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.smg.variety.R;
import com.chad.library.adapter.base.BaseViewHolder;
import com.smg.variety.bean.ShopCartListDto;
import com.smg.variety.bean.ShopCartListItemDto;
import com.smg.variety.common.utils.GlideUtils;
import com.smg.variety.common.utils.ToastUtil;
import com.smg.variety.http.DefaultSingleObserver;
import com.smg.variety.http.error.ApiException;
import com.smg.variety.http.manager.DataManager;
import com.smg.variety.http.response.HttpResult;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 购物车
 * Created by rzb on 2019/6/19
 */
public class ShopCartAdapter extends BaseQuickAdapter<ShopCartListDto, BaseViewHolder> {
    public Set<ShopCartListItemDto> selectList = new HashSet<>();//被选中的资源
    private UpdateBottomListener mUpdateBottomListener;
    private RefreshListsListener  mRefreshListsListener;
    private String mallType = null;

    public ShopCartAdapter(List<ShopCartListDto> data, String mallType) {
        super(R.layout.item_shop_cart_layout, data);
        this.mallType = mallType;
    }

    @Override
    protected void convert(BaseViewHolder helper, ShopCartListDto item) {
        if (item.getShop_name() != null) {
            helper.setText(R.id.tv_dianpu_name, item.getShop_name());
        } else {
            helper.setText(R.id.tv_dianpu_name, "自营店铺");
        }
//        GlideUtils.getInstances().loadRoundImg(mContext,helper.getView(R.id.iv_shop),"http://bbsc.885505.com/seller/"+item.getShop_id()+"/logo",R.drawable.moren_ren);

        helper.setChecked(R.id.cb_shop_cart, item.isSelect());
        helper.addOnClickListener(R.id.cb_shop_cart);
        helper.addOnClickListener(R.id.ll_dp);
        RecyclerView rvlist = helper.getView(R.id.recy_shop_cart_item);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        };
        rvlist.setLayoutManager(linearLayoutManager);
        ShopCartItemAdapter shopCartItemAdapter = new ShopCartItemAdapter(item.getProducts());
        shopCartItemAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                ShopCartListItemDto shopCartListItemDto = shopCartItemAdapter.getItem(position);
                CheckBox checkBox = view.findViewById(R.id.cb_item_shop_cart);
                TextView tvCount = view.findViewById(R.id.count);
                switch (view.getId()) {

                    case R.id.ll_item_shop_cart_content_view:
                        if(mClickItemListener!=null){
                            mClickItemListener.OnClickItemListerner(shopCartListItemDto);
                        }
//                        setCommodityCountIncrease(shopCartItemAdapter, shopCartListItemDto);

                        break;
                    case R.id.increase:
                        setCommodityCountIncrease(shopCartItemAdapter, shopCartListItemDto);
                        updateBottomView(selectList);
                        break;
                    case R.id.decrease:
                        setCommodityCountDecrease(shopCartItemAdapter, shopCartListItemDto);
                        updateBottomView(selectList);
                        break;
                    case R.id.cb_item_shop_cart:
                        if (!checkBox.isChecked()) {
                            shopCartListItemDto.setSelect(false);
                            selectList.remove(shopCartListItemDto);

                        } else {
                            shopCartListItemDto.setSelect(true);
                            selectList.add(shopCartListItemDto);
                        }

                        updateCheckState( item);
                        updateBottomView(selectList);
                        notifyDataSetChanged();
                        shopCartItemAdapter.notifyDataSetChanged();
                        break;
                }
            }
        });
        rvlist.setAdapter(shopCartItemAdapter);
    }

    private void setCommodityCountDecrease(ShopCartItemAdapter shopCartItemAdapter, ShopCartListItemDto item) {
        int count = Integer.valueOf(item.getQty());
        if (count <= 1) {
            count = 1;
        } else {
            --count;
        }
        updateSelectDataCount(shopCartItemAdapter, item, count);
    }

    private void setCommodityCountIncrease(ShopCartItemAdapter shopCartItemAdapter, ShopCartListItemDto item) {
        int count = Integer.valueOf(item.getQty());
        ++count;
        updateSelectDataCount(shopCartItemAdapter, item, count);
    }

    private void updateSelectDataCount(ShopCartItemAdapter shopCartItemAdapter, ShopCartListItemDto item, int count) {
        item.setQty(String.valueOf(count));
        shopCartItemAdapter.notifyDataSetChanged();
        modifyShoppingCart(item.getRowId(), String.valueOf(count));
    }

    private void updateCheckState( ShopCartListDto shopCartListDto) {
        int cout=0;

      for(ShopCartListItemDto dto:shopCartListDto.getProducts()){
          if(dto.isSelect()){
              ++cout;
          }
      }
      if(cout==shopCartListDto.getProducts().size()){
          shopCartListDto.setSelect(true);
      }else {
          shopCartListDto.setSelect(false);
      }
    }

    /**
     * 修改购物车商品个数
     */
    private void modifyShoppingCart(String rowId, String qty) {
        DataManager.getInstance().modifyShoppingCart(new DefaultSingleObserver<HttpResult<ShopCartListItemDto>>() {
            @Override
            public void onSuccess(HttpResult<ShopCartListItemDto> httpResult) {
            }

            @Override
            public void onError(Throwable throwable) {
            }
        }, mallType, rowId, qty);
    }

    /**
     * 删除购物车商品
     */
    private void delShoppingCart(String rowStr) {
//        HashMap<String, String> map = new HashMap<String, String>();
//        map.put("rows", rowStr);
//        DataManager.getInstance().delShoppingCart(new DefaultSingleObserver<HttpResult<Object>>() {
//            @Override
//            public void onSuccess(HttpResult<Object> httpResult) {
//                ToastUtil.showToast("删除商品成功");
//                refreshList(true);
//            }
//
//            @Override
//            public void onError(Throwable throwable) {
//                if (ApiException.getInstance().isSuccess()) {
//                    ToastUtil.showToast("删除商品成功");
//                    refreshList(true);
//                } else {
//                    ApiException.getHttpExceptionMessage(throwable);
//                }
//            }
//        }, mallType, map);
    }

    /**
     * 添加收藏
     */
    private void addFavorites(String product_id) {
        Map<String, Object> map = new HashMap<>();
        map.put("object", "SMG\\Mall\\Models\\MallProduct");
        map.put("id", product_id);
        DataManager.getInstance().addProductFavorites(new DefaultSingleObserver<HttpResult<Object>>() {
            @Override
            public void onSuccess(HttpResult<Object> httpResult) {
                ToastUtil.showToast("收藏成功");
            }

            @Override
            public void onError(Throwable throwable) {
                if (!ApiException.getInstance().isSuccess()) {
//                    ToastUtil.toast(ApiException.getInstance().getErrorMsg());
                    ToastUtil.showToast(ApiException.getHttpExceptionMessage(throwable));
                } else {
                    ToastUtil.showToast("收藏成功");
                }
            }
        }, map);
    }


    private void updateBottomView(Set<ShopCartListItemDto> selectList) {
        if (mUpdateBottomListener != null) {
            mUpdateBottomListener.OnBottomListener(selectList);
        }
    }

    public void setUpdateBottomClickListener(UpdateBottomListener updateBottomListener) {
        this.mUpdateBottomListener = updateBottomListener;
    }

    public interface UpdateBottomListener {
        void OnBottomListener(Set<ShopCartListItemDto> selectList);
    }
    public interface ClickItemListener {
        void OnClickItemListerner(ShopCartListItemDto id);
    }
    private ClickItemListener mClickItemListener;
    public void setOnItemListener(ClickItemListener listener){
        mClickItemListener=listener;
    }
    private void refreshList(boolean isFresh){
        if (mRefreshListsListener != null) {
            mRefreshListsListener.OnRefreshListener(isFresh);
        }
    }

    public void setRefreshListsListener(RefreshListsListener refreshListsListener) {
        this.mRefreshListsListener = refreshListsListener;
    }

    public interface RefreshListsListener {
        void OnRefreshListener(boolean isFresh);
    }


    public void removeSelectedList(List<ShopCartListItemDto> itemList){
        for (int i = 0; i < itemList.size(); i++) {
            selectList.remove(itemList.get(i));
        }
    }

    public void addSelectedList(List<ShopCartListItemDto> itemList){
        for (int i = 0; i < itemList.size(); i++) {
            selectList.add(itemList.get(i));
        }
    }
}