package com.andc.amway.datacubecatcher.persistent.dao;

import com.andc.amway.datacubecatcher.persistent.entity.UserSummary;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * Created by benjaminkc on 17/7/20.
 */
public interface UserSummaryRepository extends BasicDataRepository<UserSummary> {

    @Query("select us from UserSummary us " +
            "where us.account_id = ?1 and us.ref_date = ?2 " +
            "ORDER BY us.ref_date desc ")
    List<UserSummary> findByAccountIdAndRefDate(String accountId, String refDate);
}
