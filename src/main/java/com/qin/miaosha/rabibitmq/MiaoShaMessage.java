package com.qin.miaosha.rabibitmq;

import com.qin.miaosha.domain.MiaoShaUser;

public class MiaoShaMessage {

    private MiaoShaUser miaoShaUser;
    private  long goodId;

    public MiaoShaUser getMiaoShaUser() {
        return miaoShaUser;
    }

    public void setMiaoShaUser(MiaoShaUser miaoShaUser) {
        this.miaoShaUser = miaoShaUser;
    }

    public long getGoodId() {
        return goodId;
    }

    public void setGoodId(long goodId) {
        this.goodId = goodId;
    }
}
