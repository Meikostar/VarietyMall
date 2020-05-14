package com.smg.variety.bean;

import java.util.List;

public class MyOrderLogisticsDto {
    /**
     * id : d7cd327441b75c9331c24442de4ccf9e
     * tracking_number : 821875586609
     * carrier_code : yto
     * order_create_time :
     * destination_code : us
     * status : delivered
     * track_update : false
     * original_country :
     * itemTimeLength : 2
     * stayTimeLength : 139
     * service_code : null
     * packageStatus : null
     * substatus : null
     * last_mile_tracking_supported : null
     * origin_info : {"ItemReceived":"2019-05-30 11:42:06","ItemDispatched":null,"DepartfromAirport":null,"ArrivalfromAbroad":null,"CustomsClearance":null,"DestinationArrived":null,"weblink":"http://www.yto.net.cn/","phone":"4006095554","carrier_code":"yto","trackinfo":[{"Date":"2019-06-01 11:34:10","StatusDescription":"客户签收人 :已签收 已签收  感谢使用圆通速递，期待再次为您服务 如有疑问请联系：95554，投诉电话：15879030616","Details":"","checkpoint_status":"delivered"},{"Date":"2019-06-01 08:17:15","StatusDescription":"【江西省南昌市广场恒茂分部公司】 派件中  派件人 :魏亮 电话 95554  如有疑问，请联系：15879030616","Details":"","checkpoint_status":"pickup"},{"Date":"2019-06-01 08:04:33","StatusDescription":"【江西省南昌市广场恒茂分部公司】 已收入","Details":"","checkpoint_status":"transit"},{"Date":"2019-06-01 04:18:06","StatusDescription":"【南昌转运中心】 已发出 下一站 【江西省南昌市广场】","Details":"","checkpoint_status":"transit"},{"Date":"2019-06-01 03:10:16","StatusDescription":"【南昌转运中心公司】 已收入","Details":"","checkpoint_status":"transit"},{"Date":"2019-05-30 21:58:02","StatusDescription":"【成都转运中心】 已发出 下一站 【南昌转运中心】","Details":"","checkpoint_status":"transit"},{"Date":"2019-05-30 21:53:12","StatusDescription":"【成都转运中心公司】 已收入","Details":"","checkpoint_status":"transit"},{"Date":"2019-05-30 20:33:25","StatusDescription":"【四川省成都市武侯新城】 已发出 下一站 【成都转运中心】","Details":"","checkpoint_status":"transit"},{"Date":"2019-05-30 20:24:05","StatusDescription":"【四川省成都市武侯新城公司】 已打包","Details":"","checkpoint_status":"transit"},{"Date":"2019-05-30 11:42:06","StatusDescription":"【四川省成都市武侯新城公司】 已收件  (18982743446)","Details":"","checkpoint_status":"transit","ItemNode":"ItemReceived"}]}
     * destination_info : null
     * lastEvent : 客户签收人 :已签收 已签收  感谢使用圆通速递，期待再次为您服务 如有疑问请联系：95554，投诉电话：15879030616,2019-06-01 11:34:10
     * lastUpdateTime : 2019-06-01 11:34:10
     */

    private String id;
    private String tracking_number;
    private String carrier_code;
    private String order_create_time;
    private String destination_code;
    private String status;
    private boolean track_update;
    private String original_country;
    private int itemTimeLength;
    private int stayTimeLength;
    private Object service_code;
    private Object packageStatus;
    private Object substatus;
    private Object last_mile_tracking_supported;
    private OriginInfoBean origin_info;
    private Object destination_info;
    private String lastEvent;
    private String lastUpdateTime;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTracking_number() {
        return tracking_number;
    }

    public void setTracking_number(String tracking_number) {
        this.tracking_number = tracking_number;
    }

    public String getCarrier_code() {
        return carrier_code;
    }

    public void setCarrier_code(String carrier_code) {
        this.carrier_code = carrier_code;
    }

    public String getOrder_create_time() {
        return order_create_time;
    }

    public void setOrder_create_time(String order_create_time) {
        this.order_create_time = order_create_time;
    }

    public String getDestination_code() {
        return destination_code;
    }

    public void setDestination_code(String destination_code) {
        this.destination_code = destination_code;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public boolean isTrack_update() {
        return track_update;
    }

    public void setTrack_update(boolean track_update) {
        this.track_update = track_update;
    }

    public String getOriginal_country() {
        return original_country;
    }

    public void setOriginal_country(String original_country) {
        this.original_country = original_country;
    }

    public int getItemTimeLength() {
        return itemTimeLength;
    }

    public void setItemTimeLength(int itemTimeLength) {
        this.itemTimeLength = itemTimeLength;
    }

    public int getStayTimeLength() {
        return stayTimeLength;
    }

    public void setStayTimeLength(int stayTimeLength) {
        this.stayTimeLength = stayTimeLength;
    }

    public Object getService_code() {
        return service_code;
    }

    public void setService_code(Object service_code) {
        this.service_code = service_code;
    }

    public Object getPackageStatus() {
        return packageStatus;
    }

    public void setPackageStatus(Object packageStatus) {
        this.packageStatus = packageStatus;
    }

    public Object getSubstatus() {
        return substatus;
    }

    public void setSubstatus(Object substatus) {
        this.substatus = substatus;
    }

    public Object getLast_mile_tracking_supported() {
        return last_mile_tracking_supported;
    }

    public void setLast_mile_tracking_supported(Object last_mile_tracking_supported) {
        this.last_mile_tracking_supported = last_mile_tracking_supported;
    }

    public OriginInfoBean getOrigin_info() {
        return origin_info;
    }

    public void setOrigin_info(OriginInfoBean origin_info) {
        this.origin_info = origin_info;
    }

    public Object getDestination_info() {
        return destination_info;
    }

    public void setDestination_info(Object destination_info) {
        this.destination_info = destination_info;
    }

    public String getLastEvent() {
        return lastEvent;
    }

    public void setLastEvent(String lastEvent) {
        this.lastEvent = lastEvent;
    }

    public String getLastUpdateTime() {
        return lastUpdateTime;
    }

    public void setLastUpdateTime(String lastUpdateTime) {
        this.lastUpdateTime = lastUpdateTime;
    }

    public static class OriginInfoBean {
        /**
         * ItemReceived : 2019-05-30 11:42:06
         * ItemDispatched : null
         * DepartfromAirport : null
         * ArrivalfromAbroad : null
         * CustomsClearance : null
         * DestinationArrived : null
         * weblink : http://www.yto.net.cn/
         * phone : 4006095554
         * carrier_code : yto
         * trackinfo : [{"Date":"2019-06-01 11:34:10","StatusDescription":"客户签收人 :已签收 已签收  感谢使用圆通速递，期待再次为您服务 如有疑问请联系：95554，投诉电话：15879030616","Details":"","checkpoint_status":"delivered"},{"Date":"2019-06-01 08:17:15","StatusDescription":"【江西省南昌市广场恒茂分部公司】 派件中  派件人 :魏亮 电话 95554  如有疑问，请联系：15879030616","Details":"","checkpoint_status":"pickup"},{"Date":"2019-06-01 08:04:33","StatusDescription":"【江西省南昌市广场恒茂分部公司】 已收入","Details":"","checkpoint_status":"transit"},{"Date":"2019-06-01 04:18:06","StatusDescription":"【南昌转运中心】 已发出 下一站 【江西省南昌市广场】","Details":"","checkpoint_status":"transit"},{"Date":"2019-06-01 03:10:16","StatusDescription":"【南昌转运中心公司】 已收入","Details":"","checkpoint_status":"transit"},{"Date":"2019-05-30 21:58:02","StatusDescription":"【成都转运中心】 已发出 下一站 【南昌转运中心】","Details":"","checkpoint_status":"transit"},{"Date":"2019-05-30 21:53:12","StatusDescription":"【成都转运中心公司】 已收入","Details":"","checkpoint_status":"transit"},{"Date":"2019-05-30 20:33:25","StatusDescription":"【四川省成都市武侯新城】 已发出 下一站 【成都转运中心】","Details":"","checkpoint_status":"transit"},{"Date":"2019-05-30 20:24:05","StatusDescription":"【四川省成都市武侯新城公司】 已打包","Details":"","checkpoint_status":"transit"},{"Date":"2019-05-30 11:42:06","StatusDescription":"【四川省成都市武侯新城公司】 已收件  (18982743446)","Details":"","checkpoint_status":"transit","ItemNode":"ItemReceived"}]
         */

        private String              ItemReceived;
        private Object              ItemDispatched;
        private Object              DepartfromAirport;
        private Object              ArrivalfromAbroad;
        private Object              CustomsClearance;
        private Object              DestinationArrived;
        private String              weblink;
        private String              phone;
        private String              carrier_code;
        private List<TrackinfoBean> trackinfo;

        public String getItemReceived() {
            return ItemReceived;
        }

        public void setItemReceived(String ItemReceived) {
            this.ItemReceived = ItemReceived;
        }

        public Object getItemDispatched() {
            return ItemDispatched;
        }

        public void setItemDispatched(Object ItemDispatched) {
            this.ItemDispatched = ItemDispatched;
        }

        public Object getDepartfromAirport() {
            return DepartfromAirport;
        }

        public void setDepartfromAirport(Object DepartfromAirport) {
            this.DepartfromAirport = DepartfromAirport;
        }

        public Object getArrivalfromAbroad() {
            return ArrivalfromAbroad;
        }

        public void setArrivalfromAbroad(Object ArrivalfromAbroad) {
            this.ArrivalfromAbroad = ArrivalfromAbroad;
        }

        public Object getCustomsClearance() {
            return CustomsClearance;
        }

        public void setCustomsClearance(Object CustomsClearance) {
            this.CustomsClearance = CustomsClearance;
        }

        public Object getDestinationArrived() {
            return DestinationArrived;
        }

        public void setDestinationArrived(Object DestinationArrived) {
            this.DestinationArrived = DestinationArrived;
        }

        public String getWeblink() {
            return weblink;
        }

        public void setWeblink(String weblink) {
            this.weblink = weblink;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public String getCarrier_code() {
            return carrier_code;
        }

        public void setCarrier_code(String carrier_code) {
            this.carrier_code = carrier_code;
        }

        public List<TrackinfoBean> getTrackinfo() {
            return trackinfo;
        }

        public void setTrackinfo(List<TrackinfoBean> trackinfo) {
            this.trackinfo = trackinfo;
        }

        public static class TrackinfoBean {
            /**
             * Date : 2019-06-01 11:34:10
             * StatusDescription : 客户签收人 :已签收 已签收  感谢使用圆通速递，期待再次为您服务 如有疑问请联系：95554，投诉电话：15879030616
             * Details :
             * checkpoint_status : delivered
             * ItemNode : ItemReceived
             */

            private String Date;
            private String StatusDescription;
            private String Details;
            private String checkpoint_status;
            private String ItemNode;

            public String getDate() {
                return Date;
            }

            public void setDate(String Date) {
                this.Date = Date;
            }

            public String getStatusDescription() {
                return StatusDescription;
            }

            public void setStatusDescription(String StatusDescription) {
                this.StatusDescription = StatusDescription;
            }

            public String getDetails() {
                return Details;
            }

            public void setDetails(String Details) {
                this.Details = Details;
            }

            public String getCheckpoint_status() {
                return checkpoint_status;
            }

            public void setCheckpoint_status(String checkpoint_status) {
                this.checkpoint_status = checkpoint_status;
            }

            public String getItemNode() {
                return ItemNode;
            }

            public void setItemNode(String ItemNode) {
                this.ItemNode = ItemNode;
            }
        }
    }


    //    private String no;
    //    private String score;
    //    private MyorderItemsDto items;
    //    private ShipmentsInfo shipments;
    //    private MyOrderAddressDetailDtoInfo address;
    //
    //    public String getNo() {
    //        return no;
    //    }
    //
    //    public String getScore() {
    //        return score;
    //    }
    //
    //    public MyorderItemsDto getItems() {
    //        return items;
    //    }
    //
    //    public ShipmentsInfo getShipments() {
    //        return shipments;
    //    }
    //
    //    public MyOrderAddressDetailDtoInfo getAddress() {
    //        return address;
    //    }
}
