package com.saenaegi.lfree;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
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

public class aRecentVideoActivity extends AppCompatActivity {
    //private aListviewAdapter adapter;
    private ArrayList<aListviewItem> data = new ArrayList<>();
    private ArrayList<Video> videos=new ArrayList<>();

    public ArrayList<TextView> rowList = new ArrayList<>();
    public ArrayList<TextView> rowbeforeList = new ArrayList<>();
    public ArrayList<TextView> rowafterList = new ArrayList<>();
    public ListView listView;
    public aListviewAdapter adapter;
    public int focusposition = 0;
    public ConstraintLayout constraintLayout;
    public static Context context;

    private int count = 0;

    private static final String TAG = "aRecentVideo";

    private FirebaseDatabase firebaseDatabase=FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference=firebaseDatabase.getReference().child( "LFREE" ).child( "VIDEO" );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        setContentView(R.layout.activity_a_recent_video);

        listView = (ListView) findViewById(R.id.listview);
        adapter = new aListviewAdapter(this, R.layout.a_list_item, data);

        listView.setItemsCanFocus(true);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener( new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.e(TAG, "CLICKED! " + parent + " " + view + " "  + position + " " + id);
                Intent intent = new Intent(aRecentVideoActivity.this, aWatchVideoActivity.class);
                intent.putExtra("link",videos.get(position).getLink());
                intent.putExtra("count",videos.get(position).getSectionCount());
                intent.putExtra( "nowSection" ,1 );
                startActivity(intent);
            }
        } );

        /*
        listView.setOnItemClickListener( new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(aRecentVideoActivity.this, aWatchVideoActivity.class);
                intent.putExtra("link",videos.get(position).getLink());
                intent.putExtra("count",videos.get(position).getSectionCount());
                intent.putExtra( "nowSection" ,1 );
                startActivity(intent);
            }
        } );
        */

        getData();
        context = this;
    }

    public void getData(){
        databaseReference.addListenerForSingleValueEvent( new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                data.clear();
                videos.clear();
                for(DataSnapshot snapshot:dataSnapshot.getChildren()){
                    Video video=snapshot.getValue(Video.class);
                    videos.add( video );
                    aListviewItem aListviewItem=new aListviewItem( video.getTitle());
                    data.add( aListviewItem );
                    count++;
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
                    if(childRowCount == 1)
                        break;
                    else if(childRowCount >= 2 && i - firstpos == 0) {
                        row = (TextView) linearLayout1.findViewWithTag(i - firstpos);
                        /*
                        Log.e(TAG, "row content : " + row.getContentDescription());
                        //constraintLayout.setContentDescription(row.getContentDescription());
                        //Log.e(TAG, "constraint ID : " + constraintLayout.getId());
                        //Log.e(TAG, "constraint content : " + constraintLayout.getContentDescription());
                        ((ConstraintLayout) listView.getAdapter().getView(i - firstpos, null, listView)).setContentDescription(row.getContentDescription());
                        Log.e(TAG, "constraint ID : " + ((ConstraintLayout) listView.getAdapter().getView(i - firstpos, null, listView)).getId());
                        Log.e(TAG, "constraint content : " + ((ConstraintLayout) listView.getAdapter().getView(i - firstpos, null, listView)).getContentDescription());
                        */
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

                        rowList.add(row);
                        rowbeforeList.add(rowbefore);
                        rowafterList.add(rowafter);
                    }
                    else if(childRowCount >= 2 && (i + 1 - firstpos) < childRowCount && i - firstpos >= 1) {
                        row = (TextView) linearLayout1.findViewWithTag(i - firstpos);
                        /*
                        Log.e(TAG, "row content : " + row.getContentDescription());
                        //constraintLayout.setContentDescription(row.getContentDescription());
                        //Log.e(TAG, "constraint ID : " + constraintLayout.getId());
                        //Log.e(TAG, "constraint content : " + constraintLayout.getContentDescription());
                        ((ConstraintLayout) listView.getAdapter().getView(i - firstpos, null, listView)).setContentDescription(row.getContentDescription());
                        Log.e(TAG, "constraint ID : " + ((ConstraintLayout) listView.getAdapter().getView(i - firstpos, null, listView)).getId());
                        Log.e(TAG, "constraint content : " + ((ConstraintLayout) listView.getAdapter().getView(i - firstpos, null, listView)).getContentDescription());
                        */
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

                        rowList.add(row);
                        rowbeforeList.add(rowbefore);
                        rowafterList.add(rowafter);
                    }
                    else if(childRowCount-1 == i - firstpos) {
                        row = (TextView) linearLayout1.findViewWithTag(i - firstpos);
                        /*
                        Log.e(TAG, "row content : " + row.getContentDescription());
                        //constraintLayout.setContentDescription(row.getContentDescription());
                        //Log.e(TAG, "constraint ID : " + constraintLayout.getId());
                        //Log.e(TAG, "constraint content : " + constraintLayout.getContentDescription());
                        ((ConstraintLayout) listView.getAdapter().getView(i - firstpos, null, listView)).setContentDescription(row.getContentDescription());
                        Log.e(TAG, "constraint ID : " + ((ConstraintLayout) listView.getAdapter().getView(i - firstpos, null, listView)).getId());
                        Log.e(TAG, "constraint content : " + ((ConstraintLayout) listView.getAdapter().getView(i - firstpos, null, listView)).getContentDescription());
                        */
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

                        rowList.add(row);
                        rowbeforeList.add(rowbefore);
                        rowafterList.add(rowafter);
                    }
                }

                adapter.notifyDataSetChanged();


                for(int i = 0 ; i < childRowCount ; i++)
                    Log.e(TAG, "listviewitem print " + listView.getAdapter().getView(i, null, listView));

            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        } );


    }
}
