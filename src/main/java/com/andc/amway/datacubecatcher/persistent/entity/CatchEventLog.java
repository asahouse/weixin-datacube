package com.andc.amway.datacubecatcher.persistent.entity;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

/**
 * Created by benjaminkc on 17/7/4.
 */
@Data
@Entity
public class CatchEventLog implements Serializable{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String threadName;
    @Column
    private String eventName;//直接记录发生事件的方法名 / API地址

    @Column
    private String eventAccount;

    @Column
    private Long eventThenCount;

    @Column
    private Long eventThenLimit;

    @Column(columnDefinition = "TINYINT")
    private EventStatus eventStatus;

    @Column(columnDefinition = "TEXT")
    private String eventProperties;

    @Column(columnDefinition = "TEXT")
    private String eventDetailError;

    @Column(columnDefinition = "DateTime")
    private LocalDateTime createTime;

    public enum EventStatus{
        //0 , 3 监听
        init(0),error(1),abort(2),processing(3),complete(4),remove(5);

        private Integer value;

        EventStatus(Integer value) {
            this.value = value;
        }

        public static EventStatus fromValue(Integer value) {
            if (Optional.ofNullable(value).isPresent()) {
                for (EventStatus review : values()) {
                    if (review.value.equals(value)) {
                        return review;
                    }
                }
            }
            return null;
        }

        public Integer toValue() {
            return value;
        }
    }

    public static String generateThreadName(){
        UUID uuid = UUID.randomUUID();
        StringBuffer sb = new StringBuffer("threadName_");
            sb.append(uuid.toString());
        return sb.toString();
    }
}
