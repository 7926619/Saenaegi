package com.saenaegi.lfree;

//import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.saenaegi.lfree.Data.Video;
import com.saenaegi.lfree.ListviewController.aListviewAdapter;
import com.saenaegi.lfree.ListviewController.aListviewItem;

import java.util.ArrayList;

public class aLikeVideoActivity extends AppCompatActivity {

    public ArrayList<TextView> likeList = new ArrayList<>();
    public ArrayList<TextView> likebeforeList = new ArrayList<>();
    public ArrayList<TextView> likeafterList = new ArrayList<>();
    public ListView listView;
    public aListviewAdapter adapter;
    public ConstraintLayout constraintLayout;

    private ArrayList<aListviewItem> data = new ArrayList<>();

    private FirebaseDatabase firebaseDatabase=FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference=firebaseDatabase.getReference().child( "LFREE" );
    private ArrayList<Video> videos=new ArrayList<>();
    private ArrayList<String> lvideos=new ArrayList<>();
    private ProgressBar progressBar;

    //public static Context context;

    private int count = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        setContentView(R.layout.activity_a_like_video);

        listView = (ListView) findViewById(R.id.listview);
        adapter = new aListviewAdapter(this, R.layout.a_list_item, data);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener( new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(aLikeVideoActivity.this, aWatchVideoActivity.class);
                intent.putExtra("link",videos.get(position).getLink());
                intent.putExtra("count",videos.get(position).getSectionCount());
                intent.putExtra( "nowSection" ,0 );
                startActivity(intent);
            }
        } );

        progressBar = findViewById(R.id.progressBar3);
        progressBar.setMax(100);

        getData();

        FontApplication app = (FontApplication) getApplication();

        app.setLikeList(likeList);
        app.setLikeAfterList(likeafterList);
        app.setLikeBeforeList(likebeforeList);
        app.setLikeListView(listView);

        //context = this;
    }
    public void getData() {
        databaseReference.addListenerForSingleValueEvent( new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                lvideos.clear();
                videos.clear();
                data.clear();
                for(DataSnapshot snapshot:dataSnapshot.child( "LIKEVIDEO" ).getChildren()){
                    if(snapshot.getKey().equals( "userid" )){
                        for(DataSnapshot snapshot1:snapshot.getChildren()){
                            lvideos.add(snapshot1.getKey());
                        }
                    }
                }
                for(DataSnapshot snapshot:dataSnapshot.child( "VIDEO" ).getChildren()){
                    for(String idvideo:lvideos){
                        if(snapshot.getKey().equals( idvideo )){
                            Video video=snapshot.getValue(Video.class);
                            videos.add( video );
                            aListviewItem aListviewItem=new aListviewItem( video.getTitle());
                            data.add( aListviewItem );
                        }
                    }
                }

                adapter.notifyDataSetChanged();

                int childRowCount = listView.getCount();

                constraintLayout = (ConstraintLayout) listView.getAdapter().getView(0, null, listView);
                LinearLayout linearLayout1 = (LinearLayout)constraintLayout.findViewById(R.id.linear1);
                TextView row = (TextView) linearLayout1.findViewWithTag(count);
                int firstpos = (int)listView.getAdapter().getItemId(0);
                int lastpos = listView.getAdapter().getCount()-1;

                count++;

                for (int i = 0; i < childRowCount; i++) {
                    constraintLayout = (ConstraintLayout) listView.getAdapter().getView(i - firstpos, null, listView);
                    linearLayout1 = (LinearLayout)constraintLayout.findViewById(R.id.linear1);
                    if(childRowCount == 1) {
                        row = (TextView) linearLayout1.findViewWithTag(i - firstpos);
                        row.setId(i - firstpos);
                        likeList.add(row);
                        break;
                    }
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

                        likeList.add(row);
                        likebeforeList.add(rowbefore);
                        likeafterList.add(rowafter);
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

                        likeList.add(row);
                        likebeforeList.add(rowbefore);
                        likeafterList.add(rowafter);
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

                        likeList.add(row);
                        likebeforeList.add(rowbefore);
                        likeafterList.add(rowafter);
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
