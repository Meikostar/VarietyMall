package com.smg.variety.bean;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * Created by rzb on 2019/6/18.
 */
public class ShopCartListItemDto implements Serializable {
     private String             rowId;
     private String             id;
     private String             name;
     private String             qty;
     private String             price;
     private String             sku_id;
     private String             shop_id;
     private String             cover;
     private String             score;
     public  Map<String,String> options;
     public ShopCartListItemDto       ext;
     public String       liver_user_ids;
     private boolean            isSelect; //是否选中

     public String getRowId() {
          return rowId;
     }

     public void setRowId(String rowId) {
          this.rowId = rowId;
     }

     public String getId() {
          return id;
     }

     public void setId(String id) {
          this.id = id;
     }

     public String getName() {
          return name;
     }

     public void setName(String name) {
          this.name = name;
     }

     public String getQty() {
          return qty;
     }

     public void setQty(String qty) {
          this.qty = qty;
     }

     public String getPrice() {
          return price;
     }

     public void setPrice(String price) {
          this.price = price;
     }

     public String getSku_id() {
          return sku_id;
     }

     public void setSku_id(String sku_id) {
          this.sku_id = sku_id;
     }

     public String getShop_id() {
          return shop_id;
     }

     public void setShop_id(String shop_id) {
          this.shop_id = shop_id;
     }

     public String getCover() {
          return cover;
     }

     public void setCover(String cover) {
          this.cover = cover;
     }

     public String getScore() {
          return score;
     }

     public void setScore(String score) {
          this.score = score;
     }



     public boolean isSelect() {
          return isSelect;
     }

     public void setSelect(boolean select) {
          isSelect = select;
     }
}
