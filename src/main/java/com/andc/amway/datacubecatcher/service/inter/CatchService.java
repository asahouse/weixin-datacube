package com.andc.amway.datacubecatcher.service.inter;

import com.andc.amway.datacubecatcher.async.FutureJob;

import java.time.LocalDate;
import java.util.List;

/**
 * Created by benjaminkc on 17/7/4.
 */
public interface CatchService {
    List<FutureJob> catching(List<String> ids, LocalDate start, LocalDate end);
}
