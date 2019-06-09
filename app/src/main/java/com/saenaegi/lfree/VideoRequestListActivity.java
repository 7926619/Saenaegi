package com.saenaegi.lfree;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.saenaegi.lfree.Data.Video;
import com.saenaegi.lfree.ListviewController.ListviewAdapter;
import com.saenaegi.lfree.ListviewController.ListviewItem;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.regex.Pattern;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

//Video 요청 화면 - DB 연결 완료
//1. 링크에 따른 썸네일 보여주기
//2. Data를 생성순으로 내림 차순으로 정렬<후에>
//3. 사용자 세션을 이용하여 idgoogle과 see정보 알아오기 -> 마지막
//4. FIREBASE 사용자 인증으로 데이터 접근하게 하기 -> 가장 마지막
//5. 링크를 잘못 입력하거나 없는 링크일 경우 사용자에게 에러 메시지 보내기. -> 가장 마지막

public class VideoRequestListActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private FirebaseDatabase firebaseDatabase=FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference =firebaseDatabase.getReference().child("LFREE").child("VIDEO");

    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private ProgressBar progressBar;

    private ArrayList<ListviewItem> data = new ArrayList<>();
    private ArrayList<Video> videos = new ArrayList<>();
    private ArrayList<Video> rvideos=new ArrayList<>();
    private ListView listView;
    private ListviewAdapter adapter;
    private Bitmap thumb;
    private String url;
    private String videoID;
    private int effective;
    private String time;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        this.overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        setContentView(R.layout.activity_video_request_list);

        /* scroll on top */
        final ScrollView scroll_view = (ScrollView) findViewById(R.id.scroll_view);
        scroll_view.post(new Runnable() {
            public void run() {
                scroll_view.scrollTo(0, 0);
            }
        });

        /* Action Bar */
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(null);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        /* navigation */
        drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);

        final ImageButton drawerButton = (ImageButton)findViewById(R.id.drawer_icon);
        drawerButton.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });

        navigationView = (NavigationView) findViewById(R.id.navigationView);
        navigationView.setNavigationItemSelectedListener(this);

        /* link button */
        ImageButton link_button = (ImageButton) findViewById(R.id.link_button) ;
        link_button.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                input_link();
            }
        });

        /* list view */
        listView = (ListView) findViewById(R.id.listview);
        adapter = new ListviewAdapter(this, R.layout.listview_item, data);
        listView.setAdapter(adapter);
        setListViewHeightBasedOnChildren(listView);

        databaseReference.addValueEventListener( new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                data.clear();
                rvideos.clear();
                videos.clear();
                for(DataSnapshot snapshot:dataSnapshot.getChildren()){
                    Video video=snapshot.getValue(Video.class);
                    rvideos.add(video);
                    if (!(video.isLookstate() && video.isListenstate())) {
                        ListviewItem temp = new ListviewItem( StringToBitMap(video.getBitt()), video.getTitle(), null );
                        //videos.add( video );
                        data.add(0,temp);
                    }
                }
                adapter.notifyDataSetChanged();
                setListViewHeightBasedOnChildren(listView);
                progressBar.setVisibility(View.GONE);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        } );

        /* progress bar */
        progressBar = findViewById(R.id.progressBar);
        progressBar.setMax(100);
    }

    @SuppressLint("RestrictedApi")
    void input_link()
    {
        final EditText edittext = new EditText(this);
        edittext.setHint("link address");

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("링크 가져오기");
        builder.setView(edittext, 60, 0, 60, 0);
        builder.setPositiveButton("확인",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        String link = edittext.getText().toString();
                        /* 링크 형식 필터링 */
                        if ((Pattern.matches("^(https://www.youtube.com/watch\\?v=)\\S+$", link)) || (Pattern.matches("^(https://youtu.be/)\\S+$", link))){
                            /* 유튜브 영상 ID 추출 */
                            if(Pattern.matches("^(https://www.youtube.com/watch\\?v=)\\S+$", link))
                                videoID = link.substring(link.indexOf("=")+1);
                            else
                                videoID = link.substring(link.indexOf("be/")+3);
                            if(videoID.indexOf("&")!=-1)
                                videoID = videoID.substring(0,videoID.indexOf("&"));
                            else if(videoID.indexOf("?")!=-1)
                                videoID = videoID.substring(0,videoID.indexOf("?"));

                            new Thread() {
                                public void run() {
                                    String title = Title();
                                    if(effective==0) {
                                        Looper.prepare();
                                        Toast.makeText(getApplicationContext(), "유효한 영상주소가 아닙니다.", Toast.LENGTH_LONG).show();
                                        Looper.loop();
                                    }
                                    else {
                                        Log.e("time",""+ time +" "+effective);
                                        /* 썸네일 주소 설정 */
                                        url = "https://img.youtube.com/vi/"+videoID+"/maxresdefault.jpg";

                                        /* 썸네일 쓰레드 실행 */
                                        Runnable r = new BackgroundTask();
                                        Thread thread = new Thread(r);
                                        thread.start();
                                        try {
                                            thread.join();
                                        } catch (InterruptedException e) {
                                            e.printStackTrace();
                                        }

                                        /* 영상 시간 섹션 쪼개기 */
                                        int hour=0, min=0, section;
                                        if(time.indexOf("H") > -1 && time.indexOf("M") > -1) {
                                            String tmp = time.substring(0,time.indexOf("H"));
                                            hour = Integer.parseInt(tmp);
                                            tmp = time.substring(time.indexOf("H")+1,time.indexOf("M"));
                                            min = Integer.parseInt(tmp);
                                        }
                                        else if(time.indexOf("H") == -1 && time.indexOf("M") > -1){
                                            String tmp = time.substring(0,time.indexOf("M"));
                                            min = Integer.parseInt(tmp);
                                        }
                                        else if(time.indexOf("H") > -1 && time.indexOf("M") == -1) {
                                            String tmp = time.substring(0,time.indexOf("H"));
                                            hour = Integer.parseInt(tmp);
                                        }

                                        int tmp = (hour * 60) + min;
                                        if(tmp < 10)
                                            section = 1;
                                        else if((tmp % 10) < 4)
                                            section = (tmp / 10);
                                        else
                                            section = (tmp / 10) + 1;

                                        setVideoQuery(videoID, title, false, false,  section, 0,BitMapToString(thumb));
                                    }
                                }
                            }.start();
                        }
                        else {
                            Toast.makeText(getApplicationContext(), "올바른 주소형식이 아닙니다.", Toast.LENGTH_LONG).show();
                            edittext.setText(null);
                        }
                    }
                });

        builder.setNegativeButton("취소",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
        builder.show();
    }


    public boolean setVideoQuery(String link, String title, boolean lookstate, boolean listenstate, int sectionCount,int view,String thumbnail) {
        Video video=new Video(link,title,lookstate,listenstate,sectionCount,view,thumbnail);
        databaseReference.push().setValue(video);
        return true;
    }

    public void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            // pre-condition
            return;
        }

        int totalHeight = 0;
        int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(), View.MeasureSpec.AT_MOST);

        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
        listView.requestLayout();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search_menu, menu);

        MenuItem mSearch = menu.findItem(R.id.search_icon);

        SearchView mSearchView = (SearchView) mSearch.getActionView();
        mSearchView.setMaxWidth(Integer.MAX_VALUE);
        mSearchView.setQueryHint("Search");

        int searchImgId = android.support.v7.appcompat.R.id.search_button;
        ImageView v = (ImageView) mSearchView.findViewById(searchImgId);
        v.setImageResource(R.drawable.search);
        v.setPadding(0,0,0,0);

        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                data.clear();
                for(Video video:videos){
                    if(video.getTitle().contains( query )){
                        ListviewItem tmp = new ListviewItem( StringToBitMap(video.getBitt()), video.getTitle(), null );
                        data.add( tmp );
                    }
                }
                adapter.notifyDataSetChanged();
                setListViewHeightBasedOnChildren( listView );
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        Intent intent;
        switch(item.getItemId()) {
            case R.id.it_home:
                intent = new Intent(VideoRequestListActivity.this, LfreeMainActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.it_commentation:
                intent = new Intent(VideoRequestListActivity.this, VideoCommentaryListActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.it_request_video:
                break;
            case R.id.it_my_video:
                intent = new Intent(VideoRequestListActivity.this, MyVideoListActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.it_like_video:
                intent = new Intent(VideoRequestListActivity.this, LikeVideoListActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.it_notice:
                intent = new Intent(VideoRequestListActivity.this, NoticeActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.it_setting:
                intent = new Intent(VideoRequestListActivity.this, SettingsActivity.class);
                startActivity(intent);
                finish();
                break;
        }

        drawerLayout.closeDrawer(GravityCompat.START);
        return false;
    }

    private long timeBack= 0;
    @Override
    public void onBackPressed() {
        if(drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            if(System.currentTimeMillis() - timeBack >= 2000) {
                timeBack = System.currentTimeMillis();
                Toast.makeText(getApplicationContext(),"\'뒤로\' 버튼을 한번 더 누르면 종료합니다.",Toast.LENGTH_SHORT).show();
            } else if (System.currentTimeMillis() - timeBack < 2000) {
                ActivityCompat.finishAffinity(this);
            }
        }
    }

    /* 썸네일 추출 쓰레드 */
    class BackgroundTask implements Runnable {
        @Override
        public void run() {
            try {
                thumb = Picasso.with(VideoRequestListActivity.this).load(url).get();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /* 제목&시간 파싱 쓰레드 */
    private String Title() {
        String result ="";
        String title ="";
        try {
            URL aURL = new URL("https://www.googleapis.com/youtube/v3/videos?id="+videoID+"&key=AIzaSyCxaTSJkqWjCLbh4UzPXb1jOHrVX5-gmxs&part=snippet,contentDetails");
            URLConnection con = aURL.openConnection();
            InputStream is = con.getInputStream();
            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader reader = new BufferedReader(isr);

            while (true) {
                String parsing = reader.readLine();
                if (parsing == null) break;
                result += parsing+"\n";
            }

            JSONObject obj = new JSONObject(result);

            JSONObject item2 = (JSONObject) obj.get("pageInfo");
            effective = ((Integer)item2.get("totalResults")).intValue();
            JSONArray arr = (JSONArray) obj.get("items");
            JSONObject item = (JSONObject) arr.get(0);
            JSONObject snippet = (JSONObject) item.get("snippet");
            JSONObject content = (JSONObject) item.get("contentDetails");
            time = ((String) content.get("duration")).substring(2);
            title = (String) snippet.get("title");

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return title;
    }

    public String BitMapToString(Bitmap bitmap){
        ByteArrayOutputStream baos=new  ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG,100, baos);
        byte [] arr=baos.toByteArray();
        String result=Base64.encodeToString(arr, Base64.DEFAULT);
        return result;
    }

    public Bitmap StringToBitMap(String image){
        try{
            byte [] encodeByte=Base64.decode(image,Base64.DEFAULT);
            Bitmap bitmap=BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
            return bitmap;
        }catch(Exception e){
            return null;
        }
    }
}