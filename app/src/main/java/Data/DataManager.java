package Data;

public interface DataManager {
    public boolean getUserQuery(User user);
    public boolean setUserQuery(String id,String user,boolean see);
    public boolean getReportQuery(Report report);
    public boolean setReportQuery(String id, String idgoogle, String idreported, String matter, int subtitle);
    public boolean getRequestQuery(Request request);
    public boolean setRequestQuery(String id, String idgoogle, boolean type, String link);
    public boolean getVideoQuery(Video video);
    public boolean setVideoQuery(String id, String link, boolean lookstate,boolean listenstate, int sectionCount, int view);
    public boolean setSubtitle(String id, String directory, String idgoogle, String idvideo, String name, int recommend, String sectionF,int sectionNum, String sectionS, boolean type);
    public boolean getSubtitle(Subtitle subtitle);
}
