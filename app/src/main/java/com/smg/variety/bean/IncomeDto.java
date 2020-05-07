package com.smg.variety.bean;

import java.io.Serializable;

public class IncomeDto implements Serializable {
//                "id": 4,
////                        "user_id": 1,
//     "id": 11,
//             "user_id": 8,
//             "type": "member",
//             "money": 111,
//             "name": "chenw",
//             "number": "6217002020021885912",
//             "status": 0,
//             "created_at": "2020-04-02 16:44:53",
//             "updated_at": "2020-04-02 16:44:53"
//                        "wallet_type_name": "现金"

    public String user_id;
    public String value;
    public String type;
    public String type_id;
    public String gift_name;
    public String qty;
    public String wallet_type;
    public String money;
    public String created_at;
    public String updated_at;
    public String name;
    public String total;
    public String type_name;
    public IncomeDto user;
    public IncomeDto data;
    public String wallet_type_name;

    public int id;
    public int status;

}
