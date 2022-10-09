package com.andc.amway.datacubecatcher.persistent.dao;

import com.andc.amway.datacubecatcher.persistent.entity.AbstractEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;
import java.util.Set;

@NoRepositoryBean
public interface BasicDataRepository<T extends AbstractEntity>
        extends PagingAndSortingRepository<T, Long> {

    @Query(value = "select ab from #{#entityName} ab " +
            "where ab.account_id = ?1 and ab.ref_date between ?2 and ?3 ")
    Page<T> findByAccountIdAndRefDateBetween(
            String accountId, String start, String end, Pageable pageable);

    @Query(value = "select ab from #{#entityName} ab " +
            "where ab.account_id = ?1 and ab.ref_date between ?2 and ?3 ")
    List<T> findByAccountIdAndRefDateBetween(
            String accountId, String start, String end);

    @Query(value = "select ab from #{#entityName} ab " +
            "where ab.account_id = ?1 and ab.ref_date = ?2 ")
    List<T> findByAccountIdAndRefDate(
            String accountId, String date);

    @Query(value = "select ab from #{#entityName} ab " +
            "where ab.account_id = ?1 and ab.ref_date in ?2 ")
    List<T> findByAccountIdAndRefDate(
            String accountId, Set<String> date);

    @Query(value = "select ab from #{#entityName} ab " +
            "where ab.account_id = ?1 and ab.ref_date = ?2 ")
    Long countByAccountIdAndRefDate(String accountId, String refDate);
}
