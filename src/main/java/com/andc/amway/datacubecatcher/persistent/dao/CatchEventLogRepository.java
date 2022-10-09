package com.andc.amway.datacubecatcher.persistent.dao;

import com.andc.amway.datacubecatcher.persistent.entity.CatchEventLog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Created by benjaminkc on 17/7/4.
 */
public interface CatchEventLogRepository extends PagingAndSortingRepository<CatchEventLog, Long> {

    @Modifying
    @Query(nativeQuery = true, value = "show variables like \"%time_zone%\";")
    List<Object[]> time();

    Optional<CatchEventLog> findByThreadName(String threadName);
    List<CatchEventLog> findByCreateTimeAfter(LocalDateTime today);

    @Query("select log from CatchEventLog log where log.eventAccount = ?1 and log.createTime between ?2 and ?3")
    Page<CatchEventLog> findByCreateTimeBetween(String accountId, LocalDateTime start, LocalDateTime end, Pageable pageable);
}
