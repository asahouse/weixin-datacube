package com.andc.amway.datacubecatcher.wx.source;

import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by benjaminkc on 17/7/2.
 */
@Service
public class SourceFactory {

    DigiSource digiSource;
    OpenSource openSource;

    public SourceFactory(@Autowired DigiSource digiSource,
                         @Autowired OpenSource openSource){
        this.digiSource = digiSource;
        this.openSource = openSource;
    }

    @SneakyThrows
    public SourceInterface createAccountSource(SourceType sourceType){
        if (sourceType.equals(SourceType.Digi))
            return digiSource;
        if (sourceType.equals(SourceType.Open))
            return openSource;
        throw new IllegalAccessException("SourceType does not exist");
    }
}
