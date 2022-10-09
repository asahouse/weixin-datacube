package com.andc.amway.datacubecatcher.persistent.dao;

import com.andc.amway.datacubecatcher.persistent.entity.UserRead;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

/**
 * Created by benjaminkc on 17/7/3.
 */
public interface UserReadRepository extends BasicDataRepository<UserRead> {
    @Query("select u from UserRead u where u.account_id = ?1 and u.ref_date = ?2 and u.user_source = ?3")
    Optional<UserRead> findUserRead(String accountId, String date, long source);
}
