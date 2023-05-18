package com.topgaming.fusion_server.Status;

public enum DefaultStatus {


    FAILED(-1),
    SUCCESS(0),
    SERVERWRONG(1)
    ;
    final int code;

    DefaultStatus(int i) {
        code =i;
    }

    public int getCode(){return code;}
}
