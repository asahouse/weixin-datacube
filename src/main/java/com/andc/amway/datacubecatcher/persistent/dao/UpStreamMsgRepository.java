package com.andc.amway.datacubecatcher.persistent.dao;

import com.andc.amway.datacubecatcher.persistent.entity.UpStreamMsg;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Set;

public interface UpStreamMsgRepository extends BasicDataRepository<UpStreamMsg> {

    @Query(nativeQuery = true,
            value = "SELECT \n" +
                    "    usm.account_id,\n" +
                    "    usm.ref_date,\n" +
                    "    SUM(usm.msg_user),\n" +
                    "    SUM(usm.msg_count)\n" +
                    "FROM\n" +
                    "    up_stream_msg AS usm\n" +
                    "WHERE\n" +
                    "    usm.account_id = ?1\n" +
                    "        AND usm.ref_date BETWEEN ?2 AND ?3\n" +
                    "GROUP BY usm.ref_date \n" +
                    "ORDER BY usm.ref_date DESC ;")
    List<Object[]> findRecordsByAccountIdAndRefDateBetween(
            String accountId, String start, String end);

    @Query(nativeQuery = true,
        value = "SELECT \n" +
                "    usm.account_id,\n" +
                "    usm.ref_date,\n" +
                "    SUM(usm.msg_user),\n" +
                "    SUM(usm.msg_count)\n" +
                "FROM\n" +
                "    up_stream_msg AS usm\n" +
                "WHERE\n" +
                "    usm.account_id = ?1\n" +
                "        AND usm.ref_date BETWEEN ?2 AND ?3\n" +
                "GROUP BY usm.ref_date\n" +
                " /* #pageable */",
        countQuery =
                "SELECT \n" +
                "    COUNT(*)\n" +
                "FROM\n" +
                "    (SELECT \n" +
                "        usm.account_id,\n" +
                "            usm.ref_date,\n" +
                "            SUM(usm.msg_count),\n" +
                "            SUM(usm.msg_user)\n" +
                "    FROM\n" +
                "        up_stream_msg AS usm\n" +
                "    WHERE\n" +
                "        usm.account_id = ?1\n" +
                "            AND usm.ref_date BETWEEN ?2 AND ?3\n" +
                "    GROUP BY usm.ref_date) AS usma;")
    Page<Object[]> findRecordsByAccountIdAndRefDateBetween(
            String accountId, String start, String end, Pageable pageable);

    @Query(value = "select usm from UpStreamMsg usm " +
            "where usm.account_id = ?1 and usm.ref_date in ?2")
    List<UpStreamMsg> findByAccountIdAndRefDateIn(String accountId, Set<String> dates);
}
