package com.smg.variety.bean;

public class CarrieryDto {
    /**
     * data : [{"id":69,"name":"YTO Express","code":"yto","phone":"95554","homepage":"http://www.yto.net.cn/","picture":"//s.trackingmore.com/images/icons/express/yto.png","name_cn":"圆通快递","created_at":"2019-10-17 12:16:18"}]
     * meta : {"pagination":{"total":1,"count":1,"per_page":15,"current_page":1,"total_pages":1,"links":[]}}
     */

    //    private MetaBean meta;
    //    private List<DataBean> data;
    //
    //    public MetaBean getMeta() {
    //        return meta;
    //    }
    //
    //    public void setMeta(MetaBean meta) {
    //        this.meta = meta;
    //    }
    //
    //    public List<DataBean> getData() {
    //        return data;
    //    }
    //
    //    public void setData(List<DataBean> data) {
    //        this.data = data;
    //    }

    //    public static class MetaBean {
    /**
     * pagination : {"total":1,"count":1,"per_page":15,"current_page":1,"total_pages":1,"links":[]}
     */

    //        private PaginationBean pagination;
    //
    //        public PaginationBean getPagination() {
    //            return pagination;
    //        }
    //
    //        public void setPagination(PaginationBean pagination) {
    //            this.pagination = pagination;
    //        }

    //        public static class PaginationBean {
    //            /**
    //             * total : 1
    //             * count : 1
    //             * per_page : 15
    //             * current_page : 1
    //             * total_pages : 1
    //             * links : []
    //             */
    //
    //            private int total;
    //            private int count;
    //            private int per_page;
    //            private int current_page;
    //            private int total_pages;
    //            private List<?> links;
    //
    //            public int getTotal() {
    //                return total;
    //            }
    //
    //            public void setTotal(int total) {
    //                this.total = total;
    //            }
    //
    //            public int getCount() {
    //                return count;
    //            }
    //
    //            public void setCount(int count) {
    //                this.count = count;
    //            }
    //
    //            public int getPer_page() {
    //                return per_page;
    //            }
    //
    //            public void setPer_page(int per_page) {
    //                this.per_page = per_page;
    //            }
    //
    //            public int getCurrent_page() {
    //                return current_page;
    //            }
    //
    //            public void setCurrent_page(int current_page) {
    //                this.current_page = current_page;
    //            }
    //
    //            public int getTotal_pages() {
    //                return total_pages;
    //            }
    //
    //            public void setTotal_pages(int total_pages) {
    //                this.total_pages = total_pages;
    //            }
    //
    //            public List<?> getLinks() {
    //                return links;
    //            }
    //
    //            public void setLinks(List<?> links) {
    //                this.links = links;
    //            }
    //        }
    //    }

    //    public static class DataBean {
    /**
     * id : 69
     * name : YTO Express
     * code : yto
     * phone : 95554
     * homepage : http://www.yto.net.cn/
     * picture : //s.trackingmore.com/images/icons/express/yto.png
     * name_cn : 圆通快递
     * created_at : 2019-10-17 12:16:18
     */

    private int id;
    private String name;
    private String code;
    private String phone;
    private String homepage;
    private String picture;
    private String name_cn;
    private String created_at;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getHomepage() {
        return homepage;
    }

    public void setHomepage(String homepage) {
        this.homepage = homepage;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public String getName_cn() {
        return name_cn;
    }

    public void setName_cn(String name_cn) {
        this.name_cn = name_cn;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }
}
