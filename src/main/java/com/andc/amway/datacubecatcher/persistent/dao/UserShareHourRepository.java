package com.andc.amway.datacubecatcher.persistent.dao;

import com.andc.amway.datacubecatcher.persistent.entity.UserShareHour;

/**
 * Created by benjaminkc on 17/7/3.
 */
public interface UserShareHourRepository extends
        BasicDataRepository<UserShareHour>,
        BasicHourRepository<UserShareHour> {
}
