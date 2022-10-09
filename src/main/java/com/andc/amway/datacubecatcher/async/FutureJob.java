package com.andc.amway.datacubecatcher.async;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;
import java.util.concurrent.CompletableFuture;

/**
 * Created by benjaminkc on 17/8/1.
 *
 * DTO For ComplateFuture , Because future CAN NOT record NAME etc info.
 */
@Data
@AllArgsConstructor
public class FutureJob implements Serializable {
    private String name;
    private CompletableFuture<FutureResult> future;
}
