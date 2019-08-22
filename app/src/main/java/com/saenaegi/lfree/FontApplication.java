package com.saenaegi.lfree;

import java.lang.reflect.Field;
import java.util.ArrayList;

import android.app.Application;
import android.content.Context;
import android.graphics.Typeface;
import android.widget.ListView;
import android.widget.TextView;

public class FontApplication extends Application {

    private ArrayList<TextView> rowList = new ArrayList<>();
    private ArrayList<TextView> rowbeforeList = new ArrayList<>();
    private ArrayList<TextView> rowafterList = new ArrayList<>();
    private ListView rowListView;

    private ArrayList<TextView> sectionList = new ArrayList<>();
    private ArrayList<TextView> sectionbeforeList = new ArrayList<>();
    private ArrayList<TextView> sectionafterList = new ArrayList<>();
    private ListView sectionListView;

    private ArrayList<TextView> likeList = new ArrayList<>();
    private ArrayList<TextView> likebeforeList = new ArrayList<>();
    private ArrayList<TextView> likeafterList = new ArrayList<>();
    private ListView likeListView;

    private ArrayList<TextView> noticeList = new ArrayList<>();
    private ArrayList<TextView> noticebeforeList = new ArrayList<>();
    private ArrayList<TextView> noticeafterList = new ArrayList<>();
    private ListView noticeListView;

    private ArrayList<TextView> searchList = new ArrayList<>();
    private ArrayList<TextView> searchbeforeList = new ArrayList<>();
    private ArrayList<TextView> searchafterList = new ArrayList<>();
    private ListView searchListView;

    @Override
    public void onCreate() {
        super.onCreate();
        setDefaultFont(this, "DEFAULT", "KoPubDotumMedium.ttf");
        setDefaultFont(this, "SANS_SERIF", "KoPubDotumMedium.ttf");
        setDefaultFont(this, "SERIF", "KoPubDotumMedium.ttf");
    }

    public static void setDefaultFont(Context ctx,
                                      String staticTypefaceFieldName, String fontAssetName) {
        final Typeface regular = Typeface.createFromAsset(ctx.getAssets(),
                fontAssetName);
        replaceFont(staticTypefaceFieldName, regular);
    }

    protected static void replaceFont(String staticTypefaceFieldName,
                                      final Typeface newTypeface) {
        try {
            final Field StaticField = Typeface.class
                    .getDeclaredField(staticTypefaceFieldName);
            StaticField.setAccessible(true);
            StaticField.set(null, newTypeface);
        } catch (NoSuchFieldException e) {
            android.util.Log.d(null, "==================> " + e.toString());
        } catch (IllegalAccessException e) {
            android.util.Log.d(null, "==================> " + e.toString());
        }
    }

    public void setRowList(ArrayList<TextView> rowList) {
        this.rowList = rowList;
    }

    public void setRowAfterList(ArrayList<TextView> rowafterList) {
        this.rowafterList = rowafterList;
    }

    public void setRowBeforeList(ArrayList<TextView> rowbeforeList) {
        this.rowbeforeList = rowbeforeList;
    }

    public void setRowListView(ListView rowListView) {
        this.rowListView = rowListView;
    }

    public ArrayList<TextView> getRowList() {
        return rowList;
    }

    public ArrayList<TextView> getRowAfterList() {
        return rowafterList;
    }

    public ArrayList<TextView> getRowBeforeList() {
        return rowbeforeList;
    }

    public ListView getRowListView() {
        return rowListView;
    }

    public void setSectionList(ArrayList<TextView> sectionList) {
        this.sectionList = sectionList;
    }

    public void setSectionAfterList(ArrayList<TextView> sectionafterList) {
        this.sectionafterList = sectionafterList;
    }

    public void setSectionBeforeList(ArrayList<TextView> sectionbeforeList) {
        this.sectionbeforeList = sectionbeforeList;
    }

    public void setSectionListView(ListView sectionListView) {
        this.sectionListView = sectionListView;
    }

    public ArrayList<TextView> getSectionList() {
        return sectionList;
    }

    public ArrayList<TextView> getSectionAfterList() {
        return sectionafterList;
    }

    public ArrayList<TextView> getSectionBeforeList() {
        return sectionbeforeList;
    }

    public ListView getSectionListView() {
        return sectionListView;
    }

    public void setLikeList(ArrayList<TextView> likeList) {
        this.likeList = likeList;
    }

    public void setLikeAfterList(ArrayList<TextView> likeafterList) {
        this.likeafterList = likeafterList;
    }

    public void setLikeBeforeList(ArrayList<TextView> likebeforeList) {
        this.likebeforeList = likebeforeList;
    }

    public void setLikeListView(ListView likeListView) {
        this.likeListView = likeListView;
    }

    public ArrayList<TextView> getLikeList() {
        return likeList;
    }

    public ArrayList<TextView> getLikeAfterList() {
        return likeafterList;
    }

    public ArrayList<TextView> getLikeBeforeList() {
        return likebeforeList;
    }

    public ListView getLikeListView() {
        return likeListView;
    }

    public void setNoticeList(ArrayList<TextView> noticeList) {
        this.noticeList = noticeList;
    }

    public void setNoticeAfterList(ArrayList<TextView> noticeafterList) {
        this.noticeafterList = noticeafterList;
    }

    public void setNoticeBeforeList(ArrayList<TextView> noticebeforeList) {
        this.noticebeforeList = noticebeforeList;
    }

    public void setNoticeListView(ListView noticeListView) {
        this.noticeListView = noticeListView;
    }

    public ArrayList<TextView> getNoticeList() {
        return noticeList;
    }

    public ArrayList<TextView> getNoticeAfterList() {
        return noticeafterList;
    }

    public ArrayList<TextView> getNoticeBeforeList() {
        return noticebeforeList;
    }

    public ListView getNoticeListView() {
        return noticeListView;
    }

    public void setSearchList(ArrayList<TextView> searchList) {
        this.searchList = searchList;
    }

    public void setSearchAfterList(ArrayList<TextView> searchafterList) {
        this.searchafterList = searchafterList;
    }

    public void setSearchBeforeList(ArrayList<TextView> searchbeforeList) {
        this.searchbeforeList = searchbeforeList;
    }

    public void setSearchListView(ListView searchListView) {
        this.searchListView = searchListView;
    }

    public ArrayList<TextView> getSearchList() {
        return searchList;
    }

    public ArrayList<TextView> getSearchAfterList() {
        return searchafterList;
    }

    public ArrayList<TextView> getSearchBeforeList() {
        return searchbeforeList;
    }

    public ListView getSearchListView() {
        return searchListView;
    }
}
