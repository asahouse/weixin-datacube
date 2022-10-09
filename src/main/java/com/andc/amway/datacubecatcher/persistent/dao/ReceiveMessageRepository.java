package com.andc.amway.datacubecatcher.persistent.dao;

import com.andc.amway.datacubecatcher.persistent.entity.ReceiveMessage;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface ReceiveMessageRepository extends PagingAndSortingRepository<ReceiveMessage, Long> {
}
