package com.andc.amway.datacubecatcher.persistent.dao;

import com.andc.amway.datacubecatcher.persistent.entity.AbstractHourEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.PagingAndSortingRepository;

@NoRepositoryBean
public interface BasicHourRepository<T extends AbstractHourEntity>
        extends PagingAndSortingRepository<T, Long> {

    @Query(value = "select count(ab) from #{#entityName} ab " +
            "where ab.account_id = ?1 and ab.ref_date = ?2 and ab.ref_hour = ?3 ")
    Long countByAccountIdAndRefDateAndRefHour(String accountId, String refDate, String refHour);
}
