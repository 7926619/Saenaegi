package com.saenaegi.lfree.SubtitleController;

public class InputData {
    private String context;
    private String sectionS;
    private String sectionF;

    public InputData(){}
    public  InputData(String sectionS,String sectionF,String context){
        this.sectionS=sectionS;
        this.sectionF=sectionF;
        this.context=context;
    }
    public String getString(){
        StringBuilder stringBuilder=new StringBuilder(  );
        stringBuilder.append( sectionS );
        stringBuilder.append( "\t" );
        stringBuilder.append( sectionF );
        stringBuilder.append( "\t" );
        stringBuilder.append( context );
        stringBuilder.append( "\n" );
        return stringBuilder.toString();
    }

}
