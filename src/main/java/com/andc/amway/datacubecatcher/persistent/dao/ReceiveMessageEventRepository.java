package com.andc.amway.datacubecatcher.persistent.dao;

import com.andc.amway.datacubecatcher.persistent.entity.ReceiveMessage;
import com.andc.amway.datacubecatcher.persistent.entity.ReceiveMessageEvent;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface ReceiveMessageEventRepository extends PagingAndSortingRepository<ReceiveMessageEvent, Long> {
}
