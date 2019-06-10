package com.saenaegi.lfree.SubtitleController;


import android.os.Parcel;
import android.os.Parcelable;

public class SubtitleData implements Parcelable {

    private String subString;
    private String sectionS;
    private String sectionE;

    public SubtitleData(){}
    public SubtitleData(String sectionS, String sectionE, String subString){
        this.sectionS=sectionS;
        this.sectionE=sectionE;
        this.subString =subString;
    }

    protected SubtitleData(Parcel in) {
        subString = in.readString();
        sectionS = in.readString();
        sectionE = in.readString();
    }

    public static final Creator<SubtitleData> CREATOR = new Creator<SubtitleData>() {
        @Override
        public SubtitleData createFromParcel(Parcel in) {
            return new SubtitleData( in );
        }

        @Override
        public SubtitleData[] newArray(int size) {
            return new SubtitleData[size];
        }
    };

    public String getString(){
        StringBuilder stringBuilder=new StringBuilder(  );
        stringBuilder.append( sectionS );
        stringBuilder.append( "\t" );
        stringBuilder.append( sectionE );
        stringBuilder.append( "\t" );
        stringBuilder.append(subString);
        stringBuilder.append( "\r\n" );
        return stringBuilder.toString();
    }

    public String getSubString() {
        return subString;
    }

    public String getSectionS() {
        return sectionS;
    }

    public String getSectionE() {
        return sectionE;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString( subString );
        dest.writeString( sectionS );
        dest.writeString( sectionE );
    }
}
