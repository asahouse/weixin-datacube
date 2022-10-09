package com.andc.amway.datacubecatcher.persistent.dao;

import com.andc.amway.datacubecatcher.persistent.entity.ArticleTotalDetail;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

/**
 * Created by benjaminkc on 17/7/3.
 */
public interface ArticleTotalDetailRepository extends BasicDataRepository<ArticleTotalDetail> {

    @Query("select ad from ArticleTotalDetail ad where ad.msgid = ?1 and ad.stat_date in ?2")
    List<ArticleTotalDetail> findByMsgidAndStatDates(String msgid, List<String> statDates);

    @Query(nativeQuery = true, value =
            "SELECT \n" +
            "    SUM(int_page_read_user) as int_page_read_user, SUM(int_page_read_count) as int_page_read_count,\n" +
            "    SUM(ori_page_read_user) as ori_page_read_user, SUM(ori_page_read_count) as ori_page_read_count, \n" +
            "    SUM(int_page_from_feed_read_user) as int_page_from_feed_read_user, SUM(int_page_from_feed_read_count) as int_page_from_feed_read_count,\n" +
            "    SUM(int_page_from_hist_msg_read_user) as int_page_from_hist_msg_read_user, SUM(int_page_from_hist_msg_read_count) as int_page_from_hist_msg_read_count,\n" +
            "    SUM(int_page_from_session_read_user) as int_page_from_session_read_user, SUM(int_page_from_session_read_count) as int_page_from_session_read_count,\n" +
            "    SUM(int_page_from_friends_read_user) as int_page_from_friends_read_user, SUM(int_page_from_friends_read_count) as int_page_from_friends_read_count,\n" +
            "    SUM(int_page_from_other_read_user) as int_page_from_other_read_user, SUM(int_page_from_other_read_count) as int_page_from_other_read_count,\n" +
            "    SUM(feed_share_from_session_user) as feed_share_from_session_user, SUM(feed_share_from_session_cnt) as feed_share_from_session_cnt,\n" +
            "    SUM(feed_share_from_feed_user) as feed_share_from_feed_user, SUM(feed_share_from_feed_cnt) as feed_share_from_feed_cnt,\n" +
            "    SUM(feed_share_from_other_user) as feed_share_from_other_user, SUM(feed_share_from_other_cnt) as feed_share_from_other_cnt,\n" +
            "    SUM(add_to_fav_user) as add_to_fav_user, SUM(add_to_fav_count) as add_to_fav_count,\n" +
            "    stat_date\n" +
            "FROM\n" +
            "    MPDatacube.article_total_detail\n" +
            "where account_id = ?1 and stat_date between ?2 and ?3\n" +
            "GROUP BY stat_date \n" +
            "ORDER BY stat_date DESC ;")
    List<Object[]> statistics(String accountId, String start, String end);

    @Override
    @Query(nativeQuery = true,
            value = "SELECT \n" +
                    "    original.* \n" +
                    "FROM \n" +
                    "    article_total_detail AS original \n" +
                    "JOIN\n" +
                    "   (SELECT \n" +
                    "       temp.msgid, temp.stat_date \n" +
                    "    FROM ( \n" +
                    "           SELECT DISTINCT \n" +
                    "               msgid, MAX(au.stat_date) AS stat_date \n" +
                    "           FROM \n" +
                    "               article_total_detail AS au \n" +
                    "           WHERE \n" +
                    "               au.account_id = ?1 AND au.ref_date BETWEEN ?2 AND ?3 \n" +
                    "           GROUP BY msgid" +
                    "         ) \n" +
                    "       AS temp) \n" +
                    "AS converttable \n" +
                    "ON converttable.msgid = original.msgid AND converttable.stat_date = original.stat_date \n" +
                    " /* #pageable */",
            countQuery =
                    "SELECT \n" +
                    "    count(originalCount.id) \n" +
                    "FROM\n" +
                    "    article_total_detail AS originalCount \n" +
                    "JOIN\n" +
                    "   (SELECT \n" +
                    "       temp.msgid, temp.stat_date \n" +
                    "    FROM ( \n" +
                    "           SELECT DISTINCT \n" +
                    "               msgid, MAX(au.stat_date) AS stat_date \n" +
                    "           FROM \n" +
                    "               article_total_detail AS au \n" +
                    "           WHERE \n" +
                    "               au.account_id = ?1 AND au.ref_date BETWEEN ?2 AND ?3 \n" +
                    "           GROUP BY msgid" +
                    "         ) \n" +
                    "       AS temp) \n" +
                    "AS converttable \n" +
                    "ON converttable.msgid = originalCount.msgid AND converttable.stat_date = originalCount.stat_date;")
    Page<ArticleTotalDetail> findByAccountIdAndRefDateBetween(
            String accountId, String start, String end, Pageable pageable);

    @Override
    @Query(nativeQuery = true,
            value = "SELECT \n" +
                    "    original.*\n" +
                    "FROM \n" +
                    "    article_total_detail AS original \n" +
                    "JOIN \n" +
                    "   (SELECT \n" +
                    "       temp.msgid, temp.stat_date \n" +
                    "    FROM ( \n" +
                    "           SELECT DISTINCT \n" +
                    "               msgid, MAX(au.stat_date) AS stat_date \n" +
                    "           FROM \n" +
                    "               article_total_detail AS au \n" +
                    "           WHERE \n" +
                    "               au.account_id = ?1 AND au.ref_date BETWEEN ?2 AND ?3 \n" +
                    "           GROUP BY msgid" +
                    "         ) \n" +
                    "       AS temp) \n" +
                    "AS converttable \n" +
                    "ON converttable.msgid = original.msgid AND converttable.stat_date = original.stat_date " +
                    "ORDER BY original.ref_date ASC ",
            countQuery =
                    "SELECT \n" +
                    "    count(originalCount.id) \n" +
                    "FROM\n" +
                    "    article_total_detail AS originalCount \n" +
                    "JOIN \n" +
                    "   (SELECT \n" +
                    "       temp.msgid, temp.stat_date \n" +
                    "    FROM ( \n" +
                    "           SELECT DISTINCT \n" +
                    "               msgid, MAX(au.stat_date) AS stat_date \n" +
                    "           FROM \n" +
                    "               article_total_detail AS au \n" +
                    "           WHERE \n" +
                    "               au.account_id = ?1 AND au.ref_date BETWEEN ?2 AND ?3 \n" +
                    "           GROUP BY msgid " +
                    "         ) \n" +
                    "       AS temp) \n" +
                    "AS converttable \n" +
                    "ON converttable.msgid = originalCount.msgid AND converttable.stat_date = originalCount.stat_date;")
    List<ArticleTotalDetail> findByAccountIdAndRefDateBetween(
            String accountId, String start, String end);

    @Query(nativeQuery = true, value =
            "SELECT \n" +
            "   distinct tableOne.account_id, tableOne.ref_date \n" +
            "FROM (" +
            "   SELECT \n" +
            "       original.*\n" +
            "   FROM\n" +
            "       article_total_detail AS original\n" +
            "   JOIN\n" +
            "      (SELECT \n" +
            "          msgid, stat_date \n" +
            "       FROM (\n" +
            "              SELECT DISTINCT \n" +
            "                  msgid, MAX(au.stat_date) AS stat_date \n" +
            "              FROM \n" +
            "                  article_total_detail AS au \n" +
            "              GROUP BY msgid " +
            "            ) \n" +
            "          AS temp) \n" +
            "      AS converttable \n" +
            "      ON converttable.msgid = original.msgid AND converttable.stat_date = original.stat_date" +
            ") as tableOne \n" +
            "where to_days(tableOne.stat_date) - to_days(tableOne.ref_date) < ?1")
    List<Object[]> findObjectsAtRefDatasByDiffDay(Integer diffday);
}
