package com.saenaegi.lfree.SubtitleController;

public class SubtitleData {
    private String subString;
    private String sectionS;
    private String sectionE;

    public SubtitleData(){}
    public SubtitleData(String sectionS, String sectionE, String subString){
        this.sectionS=sectionS;
        this.sectionE=sectionE;
        this.subString =subString;
    }
    public String getString(){
        StringBuilder stringBuilder=new StringBuilder(  );
        stringBuilder.append( sectionS );
        stringBuilder.append( "\t" );
        stringBuilder.append( sectionE );
        stringBuilder.append( "\t" );
        stringBuilder.append(subString);
        stringBuilder.append( "\n" );
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

}
