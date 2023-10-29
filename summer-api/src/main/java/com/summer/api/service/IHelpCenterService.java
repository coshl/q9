package com.summer.api.service;

import com.summer.dao.entity.HelpCenter;

public interface IHelpCenterService {
    HelpCenter selectByPrimaryKey(int id);

    int selectColumn();
}
