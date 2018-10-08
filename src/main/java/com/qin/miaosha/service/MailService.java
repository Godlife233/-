package com.qin.miaosha.service;

import com.qin.miaosha.common.ServerResponse;
import com.qin.miaosha.domain.MiaoShaUser;

public interface MailService {
    public ServerResponse sendEmail(MiaoShaUser user);
}
