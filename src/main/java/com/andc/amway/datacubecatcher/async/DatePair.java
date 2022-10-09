package com.andc.amway.datacubecatcher.async;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * Created by benjaminkc on 17/8/1.
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class DatePair implements Serializable {
    private LocalDateTime start;
    private LocalDateTime end;

    public DatePair(LocalDate start, LocalDate end){
        this.start = LocalDateTime.of(start, LocalTime.MIN);
        this.end = LocalDateTime.of(end, LocalTime.MAX);
    }
}
