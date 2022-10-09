package com.andc.amway.datacubecatcher.persistent.dao;

import com.andc.amway.datacubecatcher.persistent.entity.UpStreamMsgHour;

public interface UpStreamMsgHourRepository extends
        BasicDataRepository<UpStreamMsgHour>,
        BasicHourRepository<UpStreamMsgHour> {
}
