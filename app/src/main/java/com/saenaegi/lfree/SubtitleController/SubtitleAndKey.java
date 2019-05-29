package com.saenaegi.lfree.SubtitleController;

import android.support.annotation.NonNull;

import com.saenaegi.lfree.Data.Subtitle;

public class SubtitleAndKey implements Comparable<SubtitleAndKey> {

    private Subtitle subtitle;
    private String key;
    public int recommend;

    public SubtitleAndKey(Subtitle subtitle, String key){

        this.subtitle=subtitle;
        this.key=key;
        recommend=subtitle.getRecommend();
    }

    public Subtitle getSubtitle(){
        return subtitle;
    }
    public String getKey(){
        return key;
    }

    @Override
    public int compareTo(@NonNull SubtitleAndKey o) {
        if(this.recommend<o.recommend){
            return 1;
        }
        else if(this.recommend>o.recommend){
            return -1;
        }
        return 0;

    }
}

