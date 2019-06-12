package com.saenaegi.lfree;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.saenaegi.lfree.Data.Video;
import com.saenaegi.lfree.ListviewController.aListviewAdapter;
import com.saenaegi.lfree.ListviewController.aListviewItem;

import java.util.ArrayList;
import java.util.Locale;

public class aSearchVideoActivity extends AppCompatActivity {
    //private aListviewAdapter adapter;
    private ArrayList<aListviewItem> data = new ArrayList<>();
    private ArrayList<Video> videos=new ArrayList<>();

    public ArrayList<TextView> searchList = new ArrayList<>();
    public ArrayList<TextView> searchbeforeList = new ArrayList<>();
    public ArrayList<TextView> searchafterList = new ArrayList<>();
    public ListView listView;
    public aListviewAdapter adapter;
    public int focusposition = 0;
    public ConstraintLayout constraintLayout;
    public static Context context;
    private ProgressBar progressBar;
    private String search;

    public int count = 0;
    private TextToSpeech tts;

    private static final String TAG = "aRecentVideo";

    private FirebaseDatabase firebaseDatabase=FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference=firebaseDatabase.getReference().child( "LFREE" ).child( "VIDEO" );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        setContentView(R.layout.activity_a_search_video);

        tts = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status == TextToSpeech.SUCCESS) {
                    int result = tts.setLanguage( Locale.KOREAN);
                }
                else
                    Toast.makeText(getApplication(), "TTS : TTS's Initialization is Failed!", Toast.LENGTH_SHORT).show();
            }
        });

        listView = (ListView) findViewById(R.id.listview);
        adapter = new aListviewAdapter(this, R.layout.a_list_item, data);

        listView.setItemsCanFocus(true);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener( new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(aSearchVideoActivity.this, aWatchVideoActivity.class);
                intent.putExtra("link",videos.get(position).getLink());
                intent.putExtra("count",videos.get(position).getSectionCount());
                intent.putExtra( "nowSection" ,0 );
                startActivity(intent);
            }
        } );

        progressBar = findViewById(R.id.progressBar3);
        progressBar.setMax(100);

        Intent data = getIntent();
        search = data.getExtras().getString( "search" );
        getData();
        context = this;
        count = 0;
    }

    public void getData(){
        databaseReference.addListenerForSingleValueEvent( new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                data.clear();
                videos.clear();
                for(DataSnapshot snapshot:dataSnapshot.getChildren()){
                    Video video=snapshot.getValue(Video.class);
                    if(video.getTitle().contains(search)){
                        videos.add( video );
                        aListviewItem aListviewItem=new aListviewItem( video.getTitle());
                        data.add( aListviewItem );
                        count++;
                    }
                }

                if(count == 0) {
                    String eventText = search + ". 라는 문구를 가진 영상은 현재 목록에서 존재하지 않습니다.";
                    Toast.makeText(getApplication(), eventText, Toast.LENGTH_SHORT).show();
                    tts.speak(eventText, TextToSpeech.QUEUE_FLUSH, null);
                    progressBar.setVisibility(View.GONE);
                    return;
                }

                adapter.notifyDataSetChanged();

                int childRowCount = listView.getCount();

                constraintLayout = (ConstraintLayout) listView.getAdapter().getView(0, null, listView);
                LinearLayout linearLayout1;
                TextView row;

                int firstpos = (int)listView.getAdapter().getItemId(0);
                int lastpos = listView.getAdapter().getCount()-1;;

                for (int i = 0; i < childRowCount; i++) {
                    constraintLayout = (ConstraintLayout) listView.getAdapter().getView(i - firstpos, null, listView);
                    linearLayout1 = (LinearLayout)constraintLayout.findViewById(R.id.linear1);
                    if(childRowCount == 1)
                        break;
                    else if(childRowCount >= 2 && i - firstpos == 0) {
                        row = (TextView) linearLayout1.findViewWithTag(i - firstpos);
                        row.setId(i - firstpos);

                        constraintLayout = (ConstraintLayout) listView.getAdapter().getView(lastpos  - firstpos, null, listView);
                        linearLayout1 = (LinearLayout)constraintLayout.findViewById(R.id.linear1);
                        TextView rowbefore = (TextView)linearLayout1.findViewWithTag(lastpos - firstpos);
                        rowbefore.setId(lastpos - firstpos);

                        constraintLayout = (ConstraintLayout) listView.getAdapter().getView(i + 1 - firstpos, null, listView);
                        linearLayout1 = (LinearLayout)constraintLayout.findViewById(R.id.linear1);
                        TextView rowafter = (TextView)linearLayout1.findViewWithTag(i + 1 - firstpos);
                        rowafter.setId(i + 1 - firstpos);

                        row.setAccessibilityTraversalBefore(rowbefore.getId());
                        row.setAccessibilityTraversalAfter(rowafter.getId());

                        searchList.add(row);
                        searchbeforeList.add(rowbefore);
                        searchafterList.add(rowafter);
                    }
                    else if(childRowCount >= 2 && (i + 1 - firstpos) < childRowCount && i - firstpos >= 1) {
                        row = (TextView) linearLayout1.findViewWithTag(i - firstpos);
                        row.setId(i - firstpos);

                        constraintLayout = (ConstraintLayout) listView.getAdapter().getView(i - 1 - firstpos, null, listView);
                        linearLayout1 = (LinearLayout)constraintLayout.findViewById(R.id.linear1);
                        TextView rowbefore = (TextView)linearLayout1.findViewWithTag(i - 1 - firstpos);
                        rowbefore.setId(i - 1 -firstpos);

                        constraintLayout = (ConstraintLayout) listView.getAdapter().getView(i + 1 - firstpos, null, listView);
                        linearLayout1 = (LinearLayout)constraintLayout.findViewById(R.id.linear1);
                        TextView rowafter = (TextView)linearLayout1.findViewWithTag(i + 1 - firstpos);
                        rowafter.setId(i + 1 - firstpos);

                        row.setAccessibilityTraversalBefore(rowbefore.getId());
                        row.setAccessibilityTraversalAfter(rowafter.getId());

                        searchList.add(row);
                        searchbeforeList.add(rowbefore);
                        searchafterList.add(rowafter);
                    }
                    else if(childRowCount-1 == i - firstpos) {
                        row = (TextView) linearLayout1.findViewWithTag(i - firstpos);
                        row.setId(i - firstpos);

                        constraintLayout = (ConstraintLayout) listView.getAdapter().getView(i - 1 - firstpos, null, listView);
                        linearLayout1 = (LinearLayout)constraintLayout.findViewById(R.id.linear1);
                        TextView rowbefore = (TextView)linearLayout1.findViewWithTag(i - 1 - firstpos);
                        rowbefore.setId(i - 1 - firstpos);

                        constraintLayout = (ConstraintLayout) listView.getAdapter().getView(firstpos, null, listView);
                        linearLayout1 = (LinearLayout)constraintLayout.findViewById(R.id.linear1);
                        TextView rowafter = (TextView)linearLayout1.findViewWithTag(firstpos);
                        rowafter.setId(firstpos);

                        row.setAccessibilityTraversalBefore(rowbefore.getId());
                        row.setAccessibilityTraversalAfter(rowafter.getId());

                        searchList.add(row);
                        searchbeforeList.add(rowbefore);
                        searchafterList.add(rowafter);
                    }
                }

                adapter.notifyDataSetChanged();
                progressBar.setVisibility(View.GONE);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        } );


    }
}
