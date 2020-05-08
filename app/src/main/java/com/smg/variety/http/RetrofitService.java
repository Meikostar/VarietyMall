package com.smg.variety.http;

import com.smg.variety.bean.ActionBean;
import com.smg.variety.bean.AddressDto;
import com.smg.variety.bean.AllCityDto;
import com.smg.variety.bean.AnchorInfo;
import com.smg.variety.bean.AreaDto;
import com.smg.variety.bean.AttentionCommunityBean;
import com.smg.variety.bean.BalanceDto;
import com.smg.variety.bean.BankCardDto;
import com.smg.variety.bean.BannerDto;
import com.smg.variety.bean.BannerInfoDto;
import com.smg.variety.bean.BaseDto2;
import com.smg.variety.bean.BaseDto4;
import com.smg.variety.bean.BrandListItemDto;
import com.smg.variety.bean.CaptchaImgDto;
import com.smg.variety.bean.CategorieBean;
import com.smg.variety.bean.CategoryListdto;
import com.smg.variety.bean.CheckOutOrderResult;
import com.smg.variety.bean.CommentDto;
import com.smg.variety.bean.CommentListBean;
import com.smg.variety.bean.CommentTopicBean;
import com.smg.variety.bean.CommodityDetailInfoDto;
import com.smg.variety.bean.ConfigDto;
import com.smg.variety.bean.CountOrderBean;
import com.smg.variety.bean.CountStatisticsBean;
import com.smg.variety.bean.CouponBean;
import com.smg.variety.bean.CouponDto;
import com.smg.variety.bean.DetailDto;
import com.smg.variety.bean.DynamicBean;
import com.smg.variety.bean.ExpressList;
import com.smg.variety.bean.FootInfoDto;
import com.smg.variety.bean.FriendListItemDto;
import com.smg.variety.bean.FriendPageDto;
import com.smg.variety.bean.GAME;
import com.smg.variety.bean.GiftBean;
import com.smg.variety.bean.GroupInfoDto;
import com.smg.variety.bean.GroupListDto;
import com.smg.variety.bean.HeadLineDetailDto;
import com.smg.variety.bean.HeadLineDto;
import com.smg.variety.bean.HotCityDto;
import com.smg.variety.bean.HotSearchInfo;
import com.smg.variety.bean.IncomeDto;
import com.smg.variety.bean.InviteFriendBean;
import com.smg.variety.bean.LearnRecordInfo;
import com.smg.variety.bean.LiveCatesBean;
import com.smg.variety.bean.LiveMessageInfo;
import com.smg.variety.bean.LiveVideoInfo;
import com.smg.variety.bean.LoginDto;
import com.smg.variety.bean.MemberDto;
import com.smg.variety.bean.MyOrderDto;
import com.smg.variety.bean.MyOrderLogisticsDto;
import com.smg.variety.bean.NewListItemDto;
import com.smg.variety.bean.NewPeopleBeam;
import com.smg.variety.bean.NewsDetailDto;
import com.smg.variety.bean.NewsOtherListItemDto;
import com.smg.variety.bean.NewsRecommendListDto;
import com.smg.variety.bean.NoticeDto;
import com.smg.variety.bean.OnlineApplyInfo;
import com.smg.variety.bean.OnlineLiveFinishBean;
import com.smg.variety.bean.OrderCheckoutBean;
import com.smg.variety.bean.OrderPreviewDto;
import com.smg.variety.bean.Param;
import com.smg.variety.bean.PersonalInfoDto;
import com.smg.variety.bean.ProductBean;
import com.smg.variety.bean.ProductDto;
import com.smg.variety.bean.PublishInfo;
import com.smg.variety.bean.RecommendListDto;
import com.smg.variety.bean.RedPacketDto;
import com.smg.variety.bean.RedPacketInfoDto;
import com.smg.variety.bean.RegisterDto;
import com.smg.variety.bean.RenWuBean;
import com.smg.variety.bean.RoomUserBean;
import com.smg.variety.bean.ScoreBean;
import com.smg.variety.bean.ScoreIncomeBean;
import com.smg.variety.bean.ServiceMenuBean;
import com.smg.variety.bean.ShopCartInfoDto;
import com.smg.variety.bean.ShopCartListItemDto;
import com.smg.variety.bean.ShopInfoBean;
import com.smg.variety.bean.ShopInfoDto;
import com.smg.variety.bean.StoreCategoryDto;
import com.smg.variety.bean.TagBean;
import com.smg.variety.bean.TopicDetailDto;
import com.smg.variety.bean.TopicListItemDto;
import com.smg.variety.bean.UploadFilesDto;
import com.smg.variety.bean.UrlInfoDto;
import com.smg.variety.bean.UserInfoDto;
import com.smg.variety.bean.VideoBean;
import com.smg.variety.bean.VideoLiveBean;
import com.smg.variety.bean.WEIXINREQ;
import com.smg.variety.bean.apply;
import com.smg.variety.db.bean.StoreInfo;
import com.smg.variety.http.response.HttpResult;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.Single;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;

/**
 * 请求方法接口
 * Created by rzb on 2019/04/18.
 */
public interface RetrofitService {
    /**
     * 手机登录POST
     */
    @POST("api/base/authorizations")
    Single<HttpResult<LoginDto>> login(@Body RequestBody body);


    @POST("/api/live/chatter/quit")
    Single<Object> quitChatter(@Header("Authorization") String token,@QueryMap Map<String, String> map);
    /**
     * 聊天室成员列表
     */
    @POST("api/live/chatter")
    Single<Object> joinChatter(@Header("Authorization") String token,@QueryMap Map<String, String> map);
    /**
     * 用户注册接口
     */
    @POST("api/base/users")
    Single<HttpResult<RegisterDto>> register(@Body RequestBody body);

    /**
     * 退出登录
     */
    @DELETE("api/base/authorizations/current")
    Single<HttpResult<Object>> logout(@Header("Authorization") String token);

    /**
     * 获取验证码
     */
    @GET("api/captcha")
    Single<HttpResult<CaptchaImgDto>> getImgCode();

    /**
     * 发送短信验证码
     */
    @POST("api/base/sms")
    Single<HttpResult<String>> getSmsCode(@Body RequestBody body);
    /**
     * 发送短信验证码
     */
    @POST("api/base/sms")
    Single<HttpResult<String>> getSmsCodes(@Header("Authorization") String token,@Body RequestBody body);

    /**
     * 重设密码
     *
     * @return
     */
    @PUT("api/base/user/password")
    Single<HttpResult<String>> resetPwd(@Header("Authorization") String user_token, @Body HashMap<String, String> map);

    /**
     * 修改用户信息
     */
    @PUT("api/base/user")
    Single<HttpResult<PersonalInfoDto>> modifUserInfo(@Header("Authorization") String token, @Body HashMap<String, String> body);

    /**
     * 重设支付密码
     */
    @PUT("api/package/wallet/pay_password")
    Single<Object> setPayPassword(@Header("Authorization") String token, @Body Map<String, Object> map);

    /**
     * 获取用户信息
     */
    @GET("api/base/user")
    Single<HttpResult<PersonalInfoDto>> getUserInfo(@Header("Authorization") String token,@Query("include") String include);

    /**
     * 获取用户信息
     */
    @GET("api/package/wallet/log")
    Single<HttpResult<List<IncomeDto>>> getUserlog(@Header("Authorization") String token,@QueryMap Map<String, String> map);


    /**
     * 获取用户信息
     */
    @GET("api/live/reward/water")
    Single<HttpResult<List<IncomeDto>>> getGiftLog(@Header("Authorization") String token,@QueryMap Map<String, String> map);


    @GET("api/package/tips")
    Single<HttpResult<List<IncomeDto>>> getTipsLog(@Header("Authorization") String token,@QueryMap Map<String, String> map);


    /**
     * 获取用户信息/
     *
     */
    @GET("api/package/user/withdraw")
    Single<HttpResult<List<IncomeDto>>> getWithdraw(@Header("Authorization") String token,@QueryMap Map<String, String> map);

    /**
     * 获取验证码
     */
    @GET("api/applications")
    Single<apply> getApplications();

    /**
     * 获取验证码
     */
    @GET("api/data/apps")
    Single<List<apply>> getApps();

    /**http://bbsc.885505.com/api/data/apps
     * 获取会员基本信息/
     */
    @POST("noSilent/member/findMemberBaseInfo")
    Single<HttpResult<UserInfoDto>> getMemberBaseInfo();

    /**
     * 用户地址列表
     */
    @GET("api/package/user/addresses")
    Single<HttpResult<List<AddressDto>>> getAddresses(@Header("Authorization") String token);


    /**
     * 添加用户地址
     */
    @POST("api/package/user/addresses")
    Single<HttpResult<AddressDto>> addAddresses(@Header("Authorization") String token, @Body RequestBody body);

    /**
     * 用户默认地址
     */
    @GET("api/package/user/address")
    Single<HttpResult<AddressDto>> getDefaultAddress(@Header("Authorization") String token);


    /**
     * 更新用户地址
     */
    @Headers("Content-Type: application/x-www-form-urlencoded; charset=utf-8")
    @FormUrlEncoded
    @PUT("api/package/user/addresses/{address_id}")
    Single<HttpResult<AddressDto>> updateAddresses(@Header("Authorization") String token, @Path("address_id") String address_id,
                                                   @Field("name") String name, @Field("mobile") String mobile,
                                                   @Field("area_id") String area_id,
                                                   @Field("detail") String detail,
                                                   @Field("is_default") String is_default);

    /**
     * 删除用户地址
     */
    @DELETE("api/package/user/addresses/{address_id}")
    Single<HttpResult<Object>> delAddresses(@Header("Authorization") String token, @Path("address_id") String address_id);

    /**
     * 地区列表
     */
    @GET("api/package/areas")
    Single<HttpResult<List<AreaDto>>> getArea(@QueryMap Map<String, String> map);

    /**BaseDto1
     * 获取订单列表
     */
    @GET("api/all_order")
    Single<HttpResult<List<MyOrderDto>>> getMyOrderList(@Header("Authorization") String token, @QueryMap Map<String, String> map);

    /**
     * 上传文件
     *
     * @return
     */
    @Multipart
    @POST("api/base/files")
    Single<HttpResult<UploadFilesDto>> uploadFiles(@Header("Authorization") String user_token, @Query("type") String fileType, @Part() MultipartBody.Part file);

    /**
     * 收藏列表
     */
    @GET("api/user/favorites")
    Single<HttpResult<List<TopicListItemDto>>> getCollectionList(@Header("Authorization") String token, @QueryMap Map<String, String> map);

    /**
     * 删除收藏
     */
    @DELETE("api/user/favorites")
    Single<Object> delCollection(@Header("Authorization") String token, @QueryMap HashMap<String, String> map);

    /**
     * 关注列表
     */
    @GET("api/user/followings")
    Single<HttpResult<List<AttentionCommunityBean>>> getAttentionList(@Header("Authorization") String token, @QueryMap Map<String, String> map);

    /**
     * 动态列表
     */
    @GET("api/user/posts")
    Single<HttpResult<List<DynamicBean>>> getDynamicList(@Header("Authorization") String token, @QueryMap Map<String, String> map);

    /**
     * 发布动态
     */
    @POST("api/user/posts")
    Single<HttpResult<Object>> publishDynamic(@Header("Authorization") String token, @QueryMap Map<String, Object> map);

    /**
     * 发布宝贝
     */
    @POST("api/package/mall/{mall_type}/products")
    Single<HttpResult<Object>> publishBaby(@Header("Authorization") String token, @Path("mall_type") String mall_type, @QueryMap Map<String, Object> map);

    /**
     * 动态评论列表
     */
    @GET("api/package/comments")
    Single<HttpResult<List<CommentListBean>>> getDynamicCommentList(@Header("Authorization") String token, @QueryMap Map<String, String> map);

    /**
     * 获取我的评论列表
     */
    @GET("api/package/user/comments")
    Single<HttpResult<List<CommentTopicBean>>> getMyCommentListData(@Header("Authorization") String token, @QueryMap Map<String, String> map);

    /**
     * 动态详情
     */
    @GET("api/user/posts/{id}")
    Single<HttpResult<DynamicBean>> getDynamicDetail(@Header("Authorization") String token, @Path("id") long id);

    /**
     * 获取余额
     */
    @GET("api/package/wallet")
    Single<HttpResult<BalanceDto>> getBalance(@Header("Authorization") String token);

    /**
     * 获取我的活动列表
     */
    @GET("api/package/pages/activity")
    Single<HttpResult<List<ActionBean>>> getActionListData(@Header("Authorization") String token, @QueryMap Map<String, String> map);

    /**
     * 获取我的活动详情
     */
    @GET("api/package/pages/activity/{id}")
    Single<HttpResult<ActionBean>> getActionDetailData(@Header("Authorization") String token, @Path("id") long id, @Query("include") String include);

    /**
     * 在线报名（活动）
     */
    @POST("api/online_apply")
    Single<HttpResult<Object>> onlineApply(@Header("Authorization") String token, @Body RequestBody body);

    /**
     * 社区发布话题标签
     */
    @GET("api/package/tags")
    Single<TagBean> getTagList(@Header("Authorization") String token);
    /**
     * 社区发布话题标签
     */
    @GET("api/base/user/data")
    Single<MemberDto> getMemberInfo(@Header("Authorization") String token);

    /**
     * 新闻频道 每日推荐列表
     */
    @GET("api/package/pages/news/flags?flags=flag1,flag2,flag3")
    Single<NewsRecommendListDto> getNewsRecommendList();

    /**
     * 新闻频道 其他列表
     */
    @GET("api/package/pages/news?sort=-order,-id")
    Single<HttpResult<List<NewsOtherListItemDto>>> getNewsList(@Query("filter[withAllTags]") String withAllTags);

    /**
     * 热门话题  每日推荐
     */
    @GET("api/posts")
    Single<HttpResult<List<TopicListItemDto>>> getTopicRecommendList(@Query("include") String include, @QueryMap Map<String, String> map);

    /**
     * 热门话题  其他列表
     */
    @GET("api/posts")
    Single<HttpResult<List<TopicListItemDto>>> getTopicList(@Query("filter[scopeWithAllTags]") String scopeWithAllTags, @Query("include") String include,  @QueryMap Map<String, String> map);

    /**
     * 获取用户好友信息列表
     */
    @GET("api/friend/my_friend_list")
    Single<HttpResult<ArrayList<FriendListItemDto>>> getFriendList(@Header("Authorization") String token, @Query("include") String include);


    @GET("api/package/mall/default/categories")
    Single<HttpResult<List<BaseDto2>>> findOtherCategory(@QueryMap Map<String, String> map);


    @GET("api/package/mall/default/categories")
    Single<HttpResult<List<StoreCategoryDto>>> findStoreCategory(@Query("include") String include);

    /**
     * 根据用户ID获取用户信息
     */
    @GET("api/search_user_info")
    Single<FriendPageDto> queryUserInfoPage(@Header("Authorization") String token, @Query("id") String id, @Query("include") String include);

    /**
     * 根据手机号模糊,获取用户列表信息
     */
    @GET("api/friend/search")
    Single<HttpResult<List<UserInfoDto>>> queryUserListByPhone(@Header("Authorization") String token, @Query("phone") String phone);

    /**
     * 获取群列表
     */
    @GET("api/group/my_group")
    Single<HttpResult<List<GroupListDto>>> getGroupList(@Header("Authorization") String token, @Query("type") String type, @Query("status") String status);


    /**
     * 获取群列表
     */
    @GET("api/package/mall/default/coupons/group/newbie_coupon/items")
    Single<HttpResult<List<NewPeopleBeam>>> getCouPon(@Header("Authorization") String token, @Query("include") String include);


    /**
     * 获取群列表
     */
    @GET("api/package/mall/all/user/coupons")
    Single<HttpResult<List<NewPeopleBeam>>> getCouPons(@Header("Authorization") String token,@QueryMap Map<String, String> map);


    /**
     * 获取群列表
     */
    @POST("api/package/mall/default/coupons/group/newbie_coupon/items/{id}")
    Single<HttpResult<Object>> putCouPon(@Header("Authorization") String token, @Path("id") String id);

    /**
     * 获取群相关信息
     */
    @GET("api/group/get_user")
    Single<HttpResult<GroupInfoDto>> getGroupInfo(@Header("Authorization") String token, @Query("group_id") String groupId, @Query("include") String include);

    @GET("api/package/wallet/sum")
    Single<BaseDto4> getTotalInofs(@Header("Authorization") String token, @Query("type") String type, @Query("money_type") String money_type);

    /**api/package/wallet/sum
     * 广告(共用)
     */
    @GET("api/package/sliders/list")
    Single<HttpResult<BannerInfoDto>> getBannerList(@Query("position_code") String pCode);



    @GET("api/membership_application/get_vip_welfare_img")
    Single<HttpResult<BannerInfoDto>> getVipWelfare();

    @GET("api/membership_application")
    Single<HttpResult<BannerInfoDto>> getMembership(@Header("Authorization") String token);
    /**
     * 国家
     */
    @GET("api/package/category_new/brand/categories")
    Single<HttpResult<List<AreaDto>>> getCategorisContury();


    /**
     * 国家
     */
    @GET("api/package/category_new/brand/categories")
    Single<HttpResult<List<AllCityDto>>> getConturyData();

    @GET("api/package/mall/default/brands")
    Single<HttpResult<List<AreaDto>>> getBrandsByCate(@Query("filter[category_id]") String id);
    /**
     * 广告(共用)
     */
    @GET("api/package/sliders/list")
    Single<HttpResult<BannerInfoDto>> getBannerProductId(@QueryMap Map<String, String> map);

    /**
     * 联盟商城列表
     */
    @GET("api/union_malls")
    Single<HttpResult<List<BrandListItemDto>>> getMallsBrandList();

    /**
     * 联盟商城列表点击的URL
     */
    @GET("api/union_malls/{id}/url")
    Single<HttpResult<UrlInfoDto>> getMallsBrandUrl(@Header("Authorization") String token, @Path("id") String id);

    /**
     * 联盟商城(新品列表)   品牌店铺(新品驾到)
     */
    @GET("api/package/mall/{mall_type}/products")
    Single<HttpResult<List<NewListItemDto>>> getNewList(@Path("mall_type") String mall_type, @Query("filter[is_new]") String isNew);

    /**
     * 联盟商城(猜你喜欢列表)
     */
    @GET("api/package/mall/{mall_type}/products")
    Single<HttpResult<List<NewListItemDto>>> getLikeList(@Path("mall_type") String mall_type);

    /**
     * 爱心家庭(猜你喜欢)
     */
    @GET("api/package/mall/{mall_type}/products")
    Single<HttpResult<List<NewListItemDto>>> getFamilyLikeList(@Path("mall_type") String mall_type, @QueryMap Map<String, String> map);

    /**
     * 商品详情(共用)
     */
    @GET("api/package/mall/{mall_type}/products/{id}")
    Single<HttpResult<CommodityDetailInfoDto>> getGoodsDetail(@Path("mall_type") String mall_type, @Path("id") String id, @QueryMap HashMap<String, String> map);

    /**
     * 商品详情(共用(登录带Token))
     */
    @GET("api/package/mall/{mall_type}/products/{id}")
    Single<HttpResult<CommodityDetailInfoDto>> getGoodsDetailToken(@Header("Authorization") String token, @Path("mall_type") String mall_type, @Path("id") String id, @QueryMap HashMap<String, String> map);

    /**
     * 购物车列表
     */
    @GET("api/package/mall/{mall_type}/cart")
    Single<HttpResult<ShopCartInfoDto>> findShoppingCartList(@Header("Authorization") String token, @Path("mall_type") String mall_type, @Query("include") String include);
    /**
     * 删除商品
     */
    @DELETE("api/package/mall/{mall_type}/cart/row")
    Single<HttpResult<Object>> delShoppingCart(@Header("Authorization") String token, @Path("mall_type") String mall_type, @QueryMap HashMap<String, String> body);

    /**
     * 添加购物车(联盟)
     */
    @POST("api/package/mall/{mall_type}/cart")
    Single<HttpResult<Object>> addShoppingCart(@Header("Authorization") String token, @Path("mall_type") String mall_type, @QueryMap HashMap<String, Object> map);

    /**
     * 类别分类
     */
    @GET("api/package/mall/{mall_type}/categories")
    Single<HttpResult<List<CategoryListdto>>> getCategoryList(@Path("mall_type") String mall_type, @Query("include") String include);

    /**
     * 发布宝贝
     */
    @POST("api/package/mall/{mall_type}/products")
    Single<HttpResult<Object>> pubProduct(@Header("Authorization") String token, @Path("mall_type") String mall_type, @QueryMap HashMap<String, String> body);


    /**
     * 爱心家庭(最近上新)
     */
    @GET("api/package/mall/{mall_type}/products")
    Single<HttpResult<List<NewListItemDto>>> getFamilyNewList(@Path("mall_type") String mall_type, @QueryMap Map<String, String> map);

    @GET("api/package/mall/default/products")
    Single<HttpResult<List<NewListItemDto>>> findGoodsList( @QueryMap Map<String, String> map);

    @GET("api/package/mall/default/products")
    Single<HttpResult<List<NewListItemDto>>> findGoodsLists(@Header("Authorization") String token, @QueryMap Map<String, String> map);

    @GET("api/live/default/liver_product")
    Single<HttpResult<List<NewListItemDto>>> findGoodsLives(@Header("Authorization") String token, @QueryMap Map<String, String> map);


    /**
     * 品牌推荐
     */
    @GET("api/package/sellers")
    Single<HttpResult<List<RecommendListDto>>> getRecommendList(@Query("filter[site_recommend]") String site_recommend, @Query("filter[type]") String type, @Query("include") String include);

    /**
     * 首页最下面列表
     */
    @GET("api/package/mall/{mall_type}/products")
    Single<HttpResult<List<NewListItemDto>>> findGoodLists(@Path("mall_type") String mall_type, @Query("filter[is_recommend]") String is_recommend, @QueryMap Map<String, String> map);


    /**
     * 首页最下面列表
     */
    @GET("api/package/mall/{mall_type}/products")
    Single<HttpResult<List<NewListItemDto>>> findHomeGoodLists(@Path("mall_type") String mall_type, @QueryMap Map<String, String> map);

    @GET("api/package/mall/default/products/guess_like")
    Single<HttpResult<List<NewListItemDto>>> findGussGoodLists( @QueryMap Map<String, String> map);

    /**
     * 发现好货
     */
    @GET("api/package/sellers")
    Single<HttpResult<List<RecommendListDto>>> getHomeRecommendList(@Query("filter[site_recommend]") String site_recommend, @Query("filter[type]") String type);

    /**
     * 获取地区列表所有值
     */
    @GET("api/package/areas/all")
    Single<HttpResult<List<AllCityDto>>> getAllCityList();

    /**
     * 获取热门城市
     */
    @GET("api/package/areas/hot")
    Single<HttpResult<List<HotCityDto>>> getHotCityList();

    /**
     * 添加好友
     */
    @POST("api/friend/add_friend")
    Single<HttpResult<Object>> addFriend(@Header("Authorization") String token, @Query("friend_user_id") String friendId, @Query("remark_name") String remark_name, @Query("msg") String msg, @Query("source") String source);

    /**
     * 消息通知列表
     */
    @GET("api/base/user/notifications")
    Single<HttpResult<List<NoticeDto>>> getNoticeList(@Header("Authorization") String token, @QueryMap Map<String, String> map);
    /**
     * 消息通知列表
     */
    @GET("api/package/pages/announcement")
    Single<HttpResult<List<NoticeDto>>> announcement( @QueryMap Map<String, String> map);

    /**
     * 验证好友申请
     */
    @POST("api/friend/verify_friend")
    Single<HttpResult<Object>> verifyFriend(@Header("Authorization") String token, @Query("friend_user_id") String friendId, @Query("remark_name") String remark_name);

    /**
     * 添加群成员
     */
    @POST("api/group/add_user")
    Single<HttpResult<Object>> addGroupUser(@Header("Authorization") String token, @Query("group_id") String groupId, @Query("ar_user_id") List<String> userIdArr);


    @POST("api/live/default/liver_product")
    Single<HttpResult<Object>> addLiveProduct(@Header("Authorization") String token, @Query("product_id") String groupId);

    @DELETE("api/live/default/liver_product")
    Single<HttpResult<Object>> delLiveProduct(@Header("Authorization") String token, @Query("product_id") String groupId);

    /**
     * 删除群成员
     */
    @DELETE("api/group/del_user")
    Single<HttpResult<Object>> delGroupUser(@Header("Authorization") String token, @Query("group_id") String groupId, @Query("ar_user_id") List<String> userIdArr);

    /**
     * 发起群聊
     */
    @POST("api/group/add_group")
    Single<HttpResult<Object>> creGroup(@Header("Authorization") String token, @Query("group_name") String groupName, @Query("type") int type, @Query("ar_user_id") List<String> userIdArr);

    /**
     * 新朋友验证列表
     */
    @GET("api/friend/newFriendList")
    Single<HttpResult<List<FriendListItemDto>>> getNewFriendLists(@Header("Authorization") String token, @Query("include") String include);

    /**
     * 删除好友
     */
    @DELETE("api/friend/del_friend")
    Single<HttpResult<Object>> delFriend(@Header("Authorization") String token, @Query("friend_user_id") String friend_user_id, @Query("is_del_party") String is_del_party);

    /**
     * 获取话题详情
     */
    @GET("api/posts/{id}")
    Single<HttpResult<TopicDetailDto>> getTopicDetail(@Header("Authorization") String token, @Path("id") String id, @Query("include") String include);


    @GET("api/package/pages/page/privacy_policy")
    Single<HttpResult<DetailDto>> privacy_policy();

    @GET("api/package/pages/page/tos")
    Single<HttpResult<DetailDto>> tos();

    @GET("api/package/pages/page/shop_service")
    Single<HttpResult<DetailDto>> shop_service();
    @GET("api/package/pages/page/task_rules")
    Single<HttpResult<DetailDto>> task_rules();
    @GET("api/package/pages/page/reg_agreement")
    Single<HttpResult<DetailDto>> reg_agreement();
    /**
     *http://bbsc.885505.com/
     */
    @GET("api/package/pages/{type}")
    Single<HttpResult<List<DetailDto>>> getHelpData(@Path("type") String type, @QueryMap Map<String, String> map);

    /**
     *http://bbsc.885505.com/
     */
    @GET("api/package/pages/{type}")
    Single<HttpResult<List<DetailDto>>> getHelpData(@Path("type") String type);
    /**
     * 获取帮助详情
     */
    @GET("api/package/pages/{type}/{id}")
    Single<HttpResult<DetailDto>> getHelpDetail(@Path("type") String type, @Path("id") String id);

    /**
     * 获取帮助详情
     */
    @GET("api/package/pages/page/privacy_policy")
    Single<HttpResult<DetailDto>> getShopService();

    /**
     * 获取新闻详情
     */
    @GET("api/package/pages/{type}/{id}")
    Single<HttpResult<NewsDetailDto>> getNewsDetail(@Path("type") String type, @Path("id") String id, @Query("include") String include);

    /**
     * 商家信息
     * @param id
     * @return
     */
    @GET("api/sellers/{id}")
    Single<HttpResult<ShopInfoDto>> getShopInfo(@Header("Authorization") String token, @Path("id") String id);




    @GET("api/user/following_shops_detail")
    Single<HttpResult<ShopInfoBean>> following_shops_detai(@Header("Authorization") String token, @Query("user_id") String user_id);


    @GET("api/package/mall/default/brands/{brand_id}")
    Single<HttpResult<ShopInfoDto>> getBrandInfo(@Header("Authorization") String token, @Path("brand_id") String id, @Query("include") String include);


    /**
     * 获取评价列表
     */
    @GET("api/package/comments/query")
    Single<HttpResult<List<CommentDto>>> getCommentsList(@Header("Authorization") String token, @Query("filter[commented_type]") String commented_type, @Query("filter[commented_id]") String commented_id, @Query("include") String include);

    /**
     * 获取评价列表
     */
    @GET("api/package/comments/query")
    Single<HttpResult<List<CommentDto>>> getCommentsList(@QueryMap Map<String, String> map);

    /**
     * 实体店铺详情
     */
    @GET("api/sellers/{id}")
    Single<HttpResult<RecommendListDto>> getShopDetail(@Header("Authorization") String token, @Path("id") String id, @Query("extra_include") String include, @Query("lat") String lat, @Query("lng") String lng);

    /**
     * 品牌店铺详情
     */
    @GET("api/sellers/{id}")
    Single<HttpResult<RecommendListDto>> getBrandShopDetail(@Header("Authorization") String token,@Path("id") String id);


    /**
     * 店铺详情(全部下拉列表数据)
     */
    @GET("api/package/tags")
    Single<TagBean> getTagsList(@Query("user_id") String userId);

    /**
     * 自动定位
     */
    @GET("api/package/areas/location")
    Single<HttpResult<AreaDto>> getLocation(@QueryMap HashMap<String, String> map);

    /**
     * wohahaha
     */
    @GET("api/configs")
    Single<HttpResult<ConfigDto>> getConfigs();

    /**
     * 实体店铺-最下面列表
     */
    @GET("api/sellers")
    Single<HttpResult<List<RecommendListDto>>> getShopList(@Query("filter[type]") String type, @Query("filter[scopeAreaSearch]") String scopeAreaSearch, @Query("filter[scopeDistanceIn]") String scopeDistanceIn, @Query("lat") String lat, @Query("lng") String lng, @Query("sort") String sort);

    /**
     * 店铺详情-最下面的产品列表
     */
    @GET("api/package/mall/{mall_type}/products")
    Single<HttpResult<List<NewListItemDto>>> getStProductList(@Path("mall_type") String mall_type,@QueryMap HashMap<String, String> map);

    /**
     * 爱心工厂分类
     */
    @GET("api/package/mall/{mall_type}/products")
    Single<HttpResult<List<ProductDto>>> getGcTypeProductList(@Path("mall_type") String mall_type, @Query("filter[category_id]") String cateId);

    /**
     * 商品搜索结果
     */
    @GET("api/package/mall/{mall_type}/products")
    Single<HttpResult<List<ProductDto>>> getSearchResultProductList(@Path("mall_type") String mall_type, @Query("filter[title]") String title);

    /**
     * 直播公告
     */
    @GET("api/configs")
    Single<HttpResult<LiveMessageInfo>> getLiveConfigs();

    /**
     * 礼物列表
     */
    @GET("api/live/gift")
    Single<HttpResult<List<GiftBean>>> getLiveGift();

    /**
     * 礼物列表
     */
    @GET("api/live/top")
    Single<HttpResult<List<UserInfoDto>>> getLiveTop(@Header("Authorization") String token, @Query("day_type") String include);
    /**
     * 是否认证
     */
    @GET("api/live/video/liveing")
    Single<PersonalInfoDto> isliveing(@Header("Authorization") String token);

    /**
     * 是否主播
     */
    @GET("api/live/is_live")
    Single<HttpResult<Object>> isLive(@Header("Authorization") String token);

    /**
     * 查询主播信息
     */
    @GET("api/live/apply")
    Single<HttpResult<AnchorInfo>> getAnchorInfo(@Header("Authorization") String token, @Query("user_id") String user_id );

    /**
     * 申请主播
     */
    @POST("api/live/apply")
    Single<HttpResult<AnchorInfo>> applyAnchor(@Header("Authorization") String token);

    /**
     * 实名认证
     */
    @POST("api/package/user/real_name")
    Single<HttpResult<Object>> applyrealName(@Header("Authorization") String token, @QueryMap HashMap<String, String> map);


    /**
     * 打赏礼物
     */
    @POST("api/live/reward")
    Single<HttpResult<AnchorInfo>> liveReward(@Header("Authorization") String token, @Body HashMap<String, String> map);

    /**
     * 打赏积分
     */
    @POST("api/live/live_reward_score")
    Single<HttpResult<AnchorInfo>> liveRewardScore(@Header("Authorization") String token, @Body HashMap<String, String> map);


    @POST("api/package/tips")
    Single<HttpResult<AnchorInfo>> getTips(@Header("Authorization") String token, @Body HashMap<String, String> map);

    @POST("api/user/check_in")
    Single<HttpResult<Object>> putCheckIn(@Header("Authorization") String token);

    @GET("api/user/check_in/data")
    Single<HttpResult<ScoreBean>> getCheckIn(@Header("Authorization") String token);

    /**
     * 打赏流水/
     */
    @GET("api/live/live_reward_water")
    Single<HttpResult<List<ScoreBean>>> liveRewardWater(@Header("Authorization") String token, @QueryMap HashMap<String, String> map);

    /**
     * 直播列表/api/live/videos
     */
    @GET("api/live/video")
    Single<HttpResult<List<VideoLiveBean>>> liveVideos(@QueryMap HashMap<String, String> map);

    /**
     * 直播列表/api/live/videos
     */
    @DELETE("api/live/video")
    Single<HttpResult<Object>> delVideos(@Header("Authorization") String token, @QueryMap HashMap<String, String> map);
    /**
     * 直播列表/api/live/videos
     */
    @PUT("api/live/video")
    Single<HttpResult<Object>> UpdateliveVideo(@Header("Authorization") String token, @Body Map<String, String> map);


    @PUT("api/package/wallet/exchange")
    Single<HttpResult<Object>> Updateexchange(@Header("Authorization") String token, @Body Map<String, String> map);

    /**
     * 开启视频直播
     */
    @POST("api/live/video")
    Single<HttpResult<LiveVideoInfo>> liveVideosList(@Header("Authorization") String token, @Body HashMap<String, Object> map);

    /**
     * 停播
     */
    @POST("api/live/video/close")
    Single<HttpResult<AnchorInfo>> liveVideosClose(@Header("Authorization") String token, @Body Map<String, String> map);

    /**
     * 停播成功，返回数据
     */
    @GET("api/live/video/close")
    Single<HttpResult<OnlineLiveFinishBean>> liveVideosCloseSuccess(@Query("id") String id, @Query("include") String include);
    /**
     * 直播详情
     */
    @GET("api/live/video/infos")
    Single<HttpResult<VideoLiveBean>> liveVideosInfo(@Header("Authorization") String token,@Query("id") String id, @Query("include") String include);
    /**
     * 直播详情
     */
    @GET("api/live/apply")
    Single<HttpResult<VideoLiveBean>> liveApply(@Query("user_id") String id);

//    /**
//     * 直播详情
//     */
//    @GET("/api/sellers{id}")
//    Single<HttpResult<Object>> getShopInfo(@Header("Authorization") String token, @Path("id") String id);
//
     @POST("api/user/ext")
     Single<HttpResult<Object>> afterLogin(@Header("Authorization") String token, @Body HashMap<String, String> map);

    @PUT("api/tasks/{type}/complete")
    Single<HttpResult<Object>> putLookLive(@Header("Authorization") String token, @Path("type") String type);
    @Headers({"Content-Type: application/json","Accept: application/json"})
    @POST("/api/sellers")
    Single<HttpResult<Object>> upSellers(@Header("Authorization") String token, @Body StoreInfo body);

    @POST("/api/user/likes")
    Single<HttpResult<Object>> getLike(@Header("Authorization") String token, @Body Map<String, String> map);

    @PUT("api/tasks/{task_id}/reward")
    Single<HttpResult<Object>> putReward(@Header("Authorization") String token, @Path("task_id") String task_id);

    /**
     * 分类列表
     */
    @GET("api/live/cates")
    Single<HttpResult<List<AllCityDto>>> liveCate(@Query("is_recommended") int is_recommended);
    /**
     * 分类列表
     */
    @GET("api/live/cates")
    Single<HttpResult<List<LiveCatesBean>>> liveCates(@Query("is_recommended") int is_recommended);

    /**
     * 分类列表
     */
    @GET("api/live/cates")
    Single<HttpResult<List<LiveCatesBean>>> liveCates(@Query("is_recommended") int is_recommended, @Query("include") String include);

    /**
     * 课件广告轮播图
     */
    @GET("api/package/sliders/list")
    Single<HttpResult<BannerInfoDto>> getSlidersList(@Query("position_code") String position_code);

    /**
     * 课件分类
     */
    @GET("api/package/mall/course/categories")
    Single<HttpResult<List<CategorieBean>>> categories();


    /**
     * 课件分类
     */
    @GET("api/package/tags")
    Single<Param> getTags();

    @GET("api/indust")
    Single<Param> getInduct();

    /**
     *
     */
    @GET("/api/package/mall/default/categories")
    Single<HttpResult<List<BannerDto>>> banCategorie();

    @GET("api/package/pages/application")
    Single<HttpResult<List<BannerDto>>> getApplication(@Query("sort") String include,@Query("per_page") String per_page,@Query("filter[flag1]") String flag);

    @GET("/api/package/mall/default/categories")
    Single<HttpResult<List<BannerDto>>> AllCategorie(@QueryMap Map<String, String> map);

    /**
     * 课件分类下在视频列表
     */
    @GET("api/package/mall/course/products")
    Single<HttpResult<List<VideoBean>>> getCourseProducts(@QueryMap Map<String, String> map);

    /**
     * 课件分视频详情
     */
    @GET("api/package/mall/course/products/{id}")
    Single<HttpResult<VideoBean>> getCourseProductsDetail(@Header("Authorization") String token, @Path("id") String id, @Query("include") String include);

    /**
     * 课件学习记录
     */
    @GET("api/package/user/footprints")
    Single<HttpResult<List<LearnRecordInfo>>> learnRecords(@Header("Authorization") String token, @QueryMap Map<String, String> map);

    /**
     * 我的足迹
     */
    @GET("api/package/user/footprints")
    Single<HttpResult<List<FootInfoDto>>> userFootprints(@Header("Authorization") String token, @QueryMap Map<String, String> map);

    /**
     * 支付生成订单号
     */
    @POST("api/package/mall/course/order/checkout")
    Single<HttpResult<List<OrderCheckoutBean>>> orderCheckout(@Header("Authorization") String token, @Body HashMap<String, String> map);

    /**
     * 首页热门搜索
     */
    @GET("api/configs")
    Single<HttpResult<HotSearchInfo>> getHotSearch(@Query("include") String include);

    /**
     * 聊天室成员列表
     */
    @GET("api/live/chatter")
    Single<HttpResult<List<RoomUserBean>>> liveChatter(@QueryMap Map<String, String> map);

    /**
     * 主播积分接口
     */
    @GET("api/package/wallet/log")
    Single<HttpResult<List<ScoreIncomeBean>>> walletLog(@Header("Authorization") String token, @QueryMap Map<String, String> map);

    @GET("api/base/user/children")
    Single<HttpResult<List<ScoreIncomeBean>>> getChildren(@Header("Authorization") String token, @QueryMap Map<String, String> map);

    /**
     * 在线报名列表
     */
    @GET("api/online_apply")
    Single<HttpResult<List<OnlineApplyInfo>>> onlineApplyList(@Header("Authorization") String token, @Query("include") String include);

    /**
     * 关注用户、关注店铺
     */
    @POST("api/user/followings")
    Single<HttpResult<Object>> postAttention(@Header("Authorization") String token, @Body Map<String, String> map);

    /**
     * 取消关注
     */
    @DELETE("api/user/followings")
    Single<HttpResult<Object>> deleteAttention(@Header("Authorization") String token, @QueryMap Map<String, String> map);

    /**
     * 修改购物车商品个数
     */
    @PUT("api/package/mall/{mall_type}/cart/{rowId}")
    Single<HttpResult<ShopCartListItemDto>> modifyShoppingCart(@Header("Authorization") String token, @Path("mall_type") String mall_type, @Path("rowId") String rowId, @Query("qty") String qty);

    /**
     * 预览订单
     */
    @POST("api/package/mall/{mall_type}/order/preview")
    Single<HttpResult<OrderPreviewDto>> getOrderPreInfo(@Header("Authorization") String token, @Path("mall_type") String mall_type, @QueryMap HashMap<String, String> map);

    /**
     * 下单
     */
    @POST("api/package/mall/{mall_type}/order/checkout")
    Single<HttpResult<List<CheckOutOrderResult>>> checkOutOrder(@Header("Authorization") String token, @Path("mall_type") String mall_type, @QueryMap HashMap<String, String> map);
    /**
     * 下单2
     */
    @POST("api/package/mall/{mall_type}/order/checkout")
    Single<HttpResult<List<CheckOutOrderResult>>> checkOutOrder2(@Header("Authorization") String token, @Path("mall_type") String mall_type, @QueryMap HashMap<String, String> map);

    /**
     * 爱心头条
     */
    @GET("api/package/pages/headline")
    Single<HttpResult<List<HeadLineDto>>> getHeadLines(@QueryMap Map<String, String> map);
    @GET("/api/feedback/types")
    Single<HttpResult<List<String>>> getTypes();

    @GET("api/package/mall/default/brands")
    Single<HttpResult<List<NewListItemDto>>> getConturyProduct(@QueryMap Map<String, String> map);

    @GET("api/package/mall/default/products")
    Single<HttpResult<List<NewListItemDto>>> getConturyProduct1(@QueryMap Map<String, String> map);
    @GET("api/package/mall/default/brands")
    Single<HttpResult<List<AllCityDto>>> getConturyProducts(@QueryMap Map<String, String> map);

    /**
     * 爱心头条详情
     */
    @GET("api/package/pages/headline/{id}")
    Single<HttpResult<HeadLineDetailDto>> getHeadLineDetail(@Path("id") String id);

    /******************************************** 个人中心 *******************************************/
    /**
     * 用户收藏、关注、发布、评论数量统计信息
     */
    @GET("api/user/count_statistics")
    Single<HttpResult<CountStatisticsBean>> userCountStatistics(@Header("Authorization") String token);

    /**
     * 关注的店铺列表
     */
    @GET("api/user/following_shops")
    Single<HttpResult<List<AttentionCommunityBean>>> getFollowingShops(@Header("Authorization") String token, @QueryMap Map<String, String> map);

    /**
     * 关注的店铺列表
     */
    @GET("api/live/apply/lists")
    Single<HttpResult<List<AttentionCommunityBean>>> getShopsList(@Header("Authorization") String token, @QueryMap Map<String, String> map);


    @GET("api/tasks")
    Single<HttpResult<RenWuBean>> getTasks(@Header("Authorization") String token);


    /**
     * 粉丝列表
     */
    @GET("api/user/followers")
    Single<HttpResult<List<AttentionCommunityBean>>> getUserFollowers(@Header("Authorization") String token, @QueryMap Map<String, String> map);

    /**
     * 银行卡列表
     */
    @GET("api/package/user/card")
    Single<HttpResult<List<BankCardDto>>> getBankCardList(@Header("Authorization") String token, @Query("page") int page);

    /**
     * 添加银行卡
     */
    @POST("api/package/user/card")
    Single<HttpResult<Object>> addBankCard(@Header("Authorization") String token, @Body Map<String, String> map);
    /**
     * 添加银行卡
     */
    @POST("api/feedback")
    Single<HttpResult<Object>> putFeedback(@Header("Authorization") String token, @Body Map<String, String> map);
    /**
     * 默认银行卡
     */
    @GET("/api/package/user/card/default")
    Single<HttpResult<BankCardDto>> getBankCardDefault(@Header("Authorization") String token);

    /**
     * 设为默认银行卡
     */
    @PUT("api/package/user/card/{card}")
    Single<HttpResult<Object>> putBankCardDefault(@Header("Authorization") String token, @Path("card") String card, @Body Map<String, String> map);

    /**
     * 删除银行卡
     */
    @DELETE("api/package/user/card/{card}")
    Single<HttpResult<Object>> deleteBankCard(@Header("Authorization") String token, @Path("card") String card);

    /**
     * 我买到的
     */
    @GET("api/package/mall/default/user/orders")
    Single<HttpResult<List<MyOrderDto>>> getAllUserOrders(@Header("Authorization") String token, @QueryMap Map<String, String> map);


    @GET("api/promote_order")
    Single<HttpResult<List<MyOrderDto>>> getPromoteOrders(@Header("Authorization") String token, @QueryMap Map<String, String> map);

    /**
     * 售后列表
     */
    @GET("api/package/mall/all/user/orders/refund_list")
    Single<HttpResult<List<MyOrderDto>>> getAllUserRefundList(@Header("Authorization") String token, @QueryMap Map<String, String> map);

    /**
     * 售后详情
     */
    @GET("api/package/mall/all/user/orders/refund_list/{id}")
    Single<HttpResult<MyOrderDto>> getAllUserRefundDetail(@Header("Authorization") String token, @Path("id") String id, @QueryMap Map<String, String> map);


    /**
     * 售后详情/api/package/mall/{mall_type}/user/orders/refund_list/{refund_id}/cancel
     */
    @PUT("/api/package/mall/default/user/orders/refund_list/{refund_id}/cancel")
    Single<HttpResult<Object>> getOrdercancel(@Header("Authorization") String token, @Path("refund_id") String id);

    /**
     * 我卖出的
     */
    @GET("api/package/mall/ax/seller/orders")
    Single<HttpResult<List<MyOrderDto>>> getSellerOrders(@Header("Authorization") String token, @QueryMap Map<String, String> map);

    /**
     * 我发布的
     */
    @GET("api/package/mall/ax/seller/products")
    Single<HttpResult<List<PublishInfo>>> getSellerProducts(@Header("Authorization") String token, @QueryMap Map<String, String> map);

    /**
     * 我邀请在好友列表
     */
    @GET("api/package/user/children")
    Single<HttpResult<List<InviteFriendBean>>> getUserChildren(@Header("Authorization") String token, @QueryMap Map<String, String> map);

    /**
     * 我的订单数量
     */
    @GET("api/package/mall/all/user/orders/count")
    Single<HttpResult<CountOrderBean>> getAllUserOrdersCount(@Header("Authorization") String token);

    /**
     * 我的订单数量
     */
    @GET("api/package/mall/all/user/orders/refund_count")
    Single<HttpResult<String>> getAlOrdersRefundCount(@Header("Authorization") String token);

    /**
     * 充值接口
     */
    @POST("api/user/recharges")
    Single<HttpResult<String>> moneyRecharges(@Header("Authorization") String token, @Body Map<String, String> map);

    @POST("api/membership_application/create")
    Single<HttpResult<String>> getCreate(@Header("Authorization") String token, @Body Map<String, String> map);

    /**
     ​/
     * 取消订单
     */
    @PUT("api/package/mall/all/user/orders/{orderId}/cancel")
    Single<HttpResult<Object>> cancelOrder(@Header("Authorization") String token, @Path("orderId") String orderId);

    /**
     * 催发货
     */
    @PUT("/api/package/mall/default/user/orders/{order_id}/hurry")
    Single<HttpResult<Object>> hurryOrder(@Header("Authorization") String token, @Path("order_id") String orderId);

    /**
     * 退款原因
     */
    @GET("api/package/mall/all/refund_reasons")
    Single<HttpResult<List<String>>> refundReasons(@Header("Authorization") String token);

    /**
     * 退款
     */
    @POST("api/package/mall/{type}/user/orders/{orderId}/refund")
    Single<Object> refundOrder(@Header("Authorization") String token, @Path("orderId") String orderId, @Body Map<String, Object> map, @Path("type") String type);

    /**
     * 评价列表
     */
    @GET("api/package/mall/all/user/orders/{orderId}")
    Single<HttpResult<MyOrderDto>> commentList(@Header("Authorization") String token, @Path("orderId") String orderId, @QueryMap Map<String, String> map);

    /**
     * 去评价
     */
    @POST("api/package/comments")
    Single<Object> toComment(@Header("Authorization") String token, @Body Map<String, Object> map);

    /**
     * 评价完成，推荐列表
     */
    @GET("api/package/mall/default/products/random")
    Single<HttpResult<List<ProductBean>>> getProductsRandom(@Header("Authorization") String token, @QueryMap Map<String, String> map);

    /**
     * 物流信息
     */
    @GET("api/package/mall/all/user/orders/{orderId}")
    Single<HttpResult<MyOrderLogisticsDto>> getLogisticsList(@Header("Authorization") String token, @Path("orderId") String orderId, @QueryMap Map<String, String> map);

    /**
     * 确定收货
     */
    @PUT("api/package/mall/all/user/orders/{orderId}/confirm")
    Single<HttpResult<Object>> confirmOrder(@Header("Authorization") String token, @Path("orderId") String orderId);

    /**
     * 快递公司
     */
    @GET("api/package/shipment/express_list")
    Single<HttpResult<List<ExpressList>>> expressList();

    /**
     * 发货
     */
    @POST("api/package/mall/ax/seller/orders/{id}/delivery")
    Single<Object> toShip(@Header("Authorization") String token, @Path("id") String id, @Body Map<String, Object> map);

    /**
     * 获取邀请人数量
     */
    @GET("api/package/user/children_count")
    Single<HttpResult<String>> getUserCount(@Header("Authorization") String token);
    /**
     * 获取邀请人数量
     */
    @GET("api/membership_application/get_super_vip_data")
    Single<HttpResult<String>> getVipData(@Header("Authorization") String token);
    /**BaseDto1
     * 我的卡包
     */
    @GET("api/package/mall/all/user/coupons")
    Single<HttpResult<List<CouponDto>>> getUserCoupons(@Header("Authorization") String token, @QueryMap Map<String, Object> map);

    /**
     * 清空足迹
     */
    @DELETE("api/package/user/footprints")
    Single<Object> clearFootPrint(@Header("Authorization") String token, @QueryMap Map<String, Object> map);

    /**
     * 提现
     */
    @POST("api/package/user/withdraw")
    Single<Object> withdraw(@Header("Authorization") String token, @Body Map<String, Object> map);

    /**
     * 编辑宝贝
     */
    @PUT("api/package/mall/ax/products/{id}")
    Single<Object> editProducts(@Header("Authorization") String token, @Path("id") String id, @Body Map<String, Object> map);

    /**
     * 用户中心更多服务菜单
     */
    @GET("api/service_menu")
    Single<HttpResult<List<ServiceMenuBean>>> serviceMenu();

    /**
     * 收藏
     */
    @POST("api/user/favorites")
    Single<HttpResult<Object>> addProductFavorites(@Header("Authorization") String token, @QueryMap Map<String, Object> map);

    /**
     * 取消收藏
     */
    @DELETE("api/user/favorites")
    Single<HttpResult<Object>> cancleProductFavorites(@Header("Authorization") String token, @QueryMap Map<String, Object> map);

    /**
     * 提交评价
     */
    @POST("api/package/comments")
    Single<HttpResult<Object>> articleComment(@Header("Authorization") String token, @Body RequestBody body);

    /**
     * 点赞
     */
    @POST("api/user/likes")
    Single<HttpResult<Object>> addPraise(@Header("Authorization") String token, @QueryMap Map<String, Object> map);

    /**
     * 取消点赞
     */
    @DELETE("api/user/likes")
    Single<HttpResult<Object>> canclePraise(@Header("Authorization") String token, @QueryMap Map<String, Object> map);

    /**
     * 支付wx
     */
    @POST("api/package/pay/union")
    Single<HttpResult<WEIXINREQ>> submitWxOrder(@Header("Authorization") String token, @QueryMap Map<String, String> map);


    /**
     * 支付zfb
     */
    @POST("api/package/pay/union")
    Single<HttpResult<String>> submitZfbOrder(@Header("Authorization") String token, @QueryMap Map<String, String> map);
    /**
     * 支付zfb
     */
    @POST("api/package/pay/union")
    Single<HttpResult<Object>> submitWalletOrder(@Header("Authorization") String token, @QueryMap Map<String, String> map);

    /**
     * 获取用户详情
     */
    @GET("api/package/mall/{mall_type}/user/orders/{id}")
    Single<HttpResult<MyOrderDto>> getOrderDetail(@Header("Authorization") String token, @Path("id") String id, @Path("mall_type") String mall_type, @Query("include") String include);

    /**
     * 领取红包
     */
    @PUT("api/wallet/cash/coupon")
    Single<HttpResult<RedPacketDto>> getRedPacket(@Header("Authorization") String token, @Query("cash_coupon_user_id") String cash_coupon_user_id);

    /**
     * 发红包
     */
    @POST("api/wallet/cash/coupon")
    Single<HttpResult<RedPacketDto>> sendRedPacket(@Header("Authorization") String token, @Query("pay_password") String pay_password
            , @Query("type") String type, @Query("total") String total
            , @Query("ar_user_id") String ar_user_id, @Query("say") String say
            , @Query("is_transfer") String is_transfer);

    /**
     * 获取红包详情
     */




    @GET("api/wallet/cash/coupon")
    Single<HttpResult<RedPacketInfoDto>> getPacketInfo(@Header("Authorization") String token, @Query("cash_coupon_id") String cash_coupon_id, @Query("include") String include);

    /**
     * 店铺优惠券
     */
    @GET("api/package/mall/st/coupons")
    Single<HttpResult<List<CouponBean>>> getShopCoupons(@Header("Authorization") String token, @QueryMap Map<String, Object> map);

    /**
     * 店铺优惠券
     */
    @POST("api/package/mall/st/user/coupons/{id}")
    Single<HttpResult<Object>> postShopCoupons(@Header("Authorization") String token, @Path("id") String id);

    /**
     * 店铺优惠券可用优惠券
     */
    @POST("api/package/mall/{mall_type}/order/preview_coupons")
    Single<HttpResult<List<CouponDto>>> getPreviewCoupons(@Header("Authorization") String token, @Path("mall_type") String mall_type,@Body Map map);
}


