package com.andc.amway.datacubecatcher.persistent.dao;

import com.andc.amway.datacubecatcher.persistent.entity.UserReadHour;

/**
 * Created by benjaminkc on 17/7/3.
 */
public interface UserReadHourRepository extends
        BasicDataRepository<UserReadHour>,
        BasicHourRepository<UserReadHour> {
}
