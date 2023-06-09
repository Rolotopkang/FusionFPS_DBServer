package com.topgaming.fusion_server.NetMessagee;


public enum NetCode {

    None(0),
    UserLogin(1001),
    UserRegister(1002),
    VersionControl(1003),
    UserMoneyGet(1051),
    UserMoneyAdd(1052),
    AnnouncementGet(1100),
    ;

    final int code;

    NetCode(int code){this.code = code;}

    public int getCode(){return code;}

    public static NetCode getType(int code)
    {
        NetCode[] values = NetCode.values();
        for (NetCode value : values) {
            if (code == value.code) {
                return value;
            }
        }

        return None;
    }



}
