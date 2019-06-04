package com.saenaegi.lfree;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
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

import static android.content.ContentValues.TAG;

public class aRecentVideoActivity extends AppCompatActivity {

    private ListView listView;
    private aListviewAdapter adapter;
    private ArrayList<aListviewItem> data = new ArrayList<>();
    private ArrayList<Video> videos=new ArrayList<>();

    private int count = 0;
    //public LinearLayout linearLayout;

    private FirebaseDatabase firebaseDatabase=FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference=firebaseDatabase.getReference().child( "LFREE" ).child( "VIDEO" );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        setContentView(R.layout.activity_a_recent_video);

        //linearLayout = (LinearLayout)findViewById(R.id.recent_video_ll);

        listView = (ListView) findViewById(R.id.listview);
        adapter = new aListviewAdapter(this, R.layout.a_list_item, data);

        listView.setItemsCanFocus(true);
        listView.setAdapter(adapter);

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

        getData();
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
                    Log.e(TAG, "DataSnapshot Loop count : " + count);
                    count++;
                }

                count = 0;

                adapter.notifyDataSetChanged();

                int childRowCount = listView.getCount();

                Log.e(TAG, "=========================================================================");
                Log.e(TAG, "Count Value : " + count);
                Log.e(TAG, "=========================================================================");

                ConstraintLayout constraintLayout = (ConstraintLayout) listView.getAdapter().getView(0, null, listView);
                LinearLayout linearLayout1 = (LinearLayout)constraintLayout.findViewById(R.id.linear1);
                TextView row = (TextView) linearLayout1.findViewWithTag(count);
                int firstpos = (int)listView.getAdapter().getItemId(0);
                int lastpos = listView.getAdapter().getCount()-1;

                count++;

                Log.e(TAG, "Child Row Count : " + childRowCount);
                Log.e(TAG, "TextView : " + row);
                Log.e(TAG, "First Visible Position : " + firstpos);
                Log.e(TAG, "Last Visible Position : " + lastpos);

                for (int i = 0; i < childRowCount; i++) {
                    constraintLayout = (ConstraintLayout) listView.getAdapter().getView(i - firstpos, null, listView);
                    linearLayout1 = (LinearLayout)constraintLayout.findViewById(R.id.linear1);
                    //linearLayout2 = linearLayout1.findViewWithTag("linear2");
                    if(childRowCount == 1)
                        break;
                    else if(childRowCount >= 2 && i - firstpos == 0) {
                        //row = (TextView) constraintLayout.getChildAt(i - firstpos);
                        //TextView rowbefore = (TextView)constraintLayout.getChildAt(lastpos - firstpos);
                        //TextView rowafter = (TextView)constraintLayout.getChildAt(i + 1 - firstpos);

                        row = (TextView) linearLayout1.findViewWithTag(i - firstpos);
                        Log.e(TAG, "=========================================================================");
                        Log.e(TAG, "TextView tag : " + row.getTag());
                        Log.e(TAG, "Loop count : " + i);
                        Log.e(TAG, "=========================================================================");
                        constraintLayout = (ConstraintLayout) listView.getAdapter().getView(lastpos  - firstpos, null, listView);
                        linearLayout1 = (LinearLayout)constraintLayout.findViewById(R.id.linear1);
                        TextView rowbefore = (TextView)linearLayout1.findViewWithTag(lastpos - firstpos);

                        constraintLayout = (ConstraintLayout) listView.getAdapter().getView(i + 1 - firstpos, null, listView);
                        linearLayout1 = (LinearLayout)constraintLayout.findViewById(R.id.linear1);
                        TextView rowafter = (TextView)linearLayout1.findViewWithTag(i + 1 - firstpos);

                        //row.setAccessibilityTraversalBefore(rowbefore.getId());
                        //row.setAccessibilityTraversalAfter(rowafter.getId());
                        row.setAccessibilityTraversalBefore((int)adapter.getItemId(lastpos - firstpos));
                        row.setAccessibilityTraversalAfter((int)adapter.getItemId(i + 1 - firstpos));

                        Log.e(TAG, "=========================================================================");
                        Log.e(TAG, "count : " + i);
                        Log.e(TAG, "row nextfocus : " + row.getAccessibilityTraversalAfter());
                        Log.e(TAG, "row previousfocus : " + row.getAccessibilityTraversalBefore());
                        Log.e(TAG, "rowbefore : " + rowbefore);
                        Log.e(TAG, "rowafter : " + rowafter);
                        Log.e(TAG, "=========================================================================");
                    }
                    else if(childRowCount >= 2 && (i + 1 - firstpos) < childRowCount && i - firstpos >= 1) {
                        //row = (TextView) constraintLayout.getChildAt(i - firstpos);
                        //TextView rowbefore = (TextView)constraintLayout.getChildAt(i - 1 - firstpos);
                        //TextView rowafter = (TextView)constraintLayout.getChildAt(i + 1 - firstpos);

                        row = (TextView) linearLayout1.findViewWithTag(i - firstpos);
                        Log.e(TAG, "=========================================================================");
                        Log.e(TAG, "TextView tag : " + row.getTag());
                        Log.e(TAG, "Loop count : " + i);
                        Log.e(TAG, "=========================================================================");
                        constraintLayout = (ConstraintLayout) listView.getAdapter().getView(i - 1 - firstpos, null, listView);
                        linearLayout1 = (LinearLayout)constraintLayout.findViewById(R.id.linear1);
                        TextView rowbefore = (TextView)linearLayout1.findViewWithTag(i - 1 - firstpos);

                        constraintLayout = (ConstraintLayout) listView.getAdapter().getView(i + 1 - firstpos, null, listView);
                        linearLayout1 = (LinearLayout)constraintLayout.findViewById(R.id.linear1);
                        TextView rowafter = (TextView)linearLayout1.findViewWithTag(i + 1 - firstpos);

                        //row.setAccessibilityTraversalBefore(rowbefore.getId());
                        //row.setAccessibilityTraversalAfter(rowafter.getId());
                        row.setAccessibilityTraversalBefore((int)adapter.getItemId(i - 1 - firstpos));
                        row.setAccessibilityTraversalAfter((int)adapter.getItemId(i + 1 - firstpos));

                        Log.e(TAG, "=========================================================================");
                        Log.e(TAG, "count : " + i);
                        Log.e(TAG, "row nextfocus : " + row.getAccessibilityTraversalAfter());
                        Log.e(TAG, "row previousfocus : " + row.getAccessibilityTraversalBefore());
                        Log.e(TAG, "rowbefore : " + rowbefore);
                        Log.e(TAG, "rowafter : " + rowafter);
                        Log.e(TAG, "=========================================================================");
                    }
                    else if(childRowCount-1 == i - firstpos) {
                        //row = (TextView)constraintLayout.getChildAt(i - firstpos);
                        //TextView rowbefore = (TextView)constraintLayout.getChildAt(i -1 - firstpos);
                        //TextView rowafter = (TextView)constraintLayout.getChildAt(firstpos);

                        row = (TextView) linearLayout1.findViewWithTag(i - firstpos);
                        Log.e(TAG, "=========================================================================");
                        Log.e(TAG, "TextView tag : " + row.getTag());
                        Log.e(TAG, "Loop count : " + i);
                        Log.e(TAG, "=========================================================================");
                        constraintLayout = (ConstraintLayout) listView.getAdapter().getView(i - 1 - firstpos, null, listView);
                        linearLayout1 = (LinearLayout)constraintLayout.findViewById(R.id.linear1);
                        TextView rowbefore = (TextView)linearLayout1.findViewWithTag(i - 1 - firstpos);

                        constraintLayout = (ConstraintLayout) listView.getAdapter().getView(firstpos, null, listView);
                        linearLayout1 = (LinearLayout)constraintLayout.findViewById(R.id.linear1);
                        TextView rowafter = (TextView)linearLayout1.findViewWithTag(firstpos);

                        //row.setAccessibilityTraversalBefore(rowbefore.getId());
                        //row.setAccessibilityTraversalAfter(rowafter.getId());
                        row.setAccessibilityTraversalBefore((int)adapter.getItemId(i - 1 - firstpos));
                        row.setAccessibilityTraversalAfter((int)adapter.getItemId(firstpos));

                        Log.e(TAG, "=========================================================================");
                        Log.e(TAG, "count : " + i);
                        Log.e(TAG, "row nextfocus : " + row.getAccessibilityTraversalAfter());
                        Log.e(TAG, "row previousfocus : " + row.getAccessibilityTraversalBefore());
                        Log.e(TAG, "rowbefore : " + rowbefore);
                        Log.e(TAG, "rowafter : " + rowafter);
                        Log.e(TAG, "=========================================================================");
                    }
                }

                adapter.notifyDataSetChanged();

                /*
                for(int i = 0 ; i < adapter.getCount() ; i++) {
                    Log.e(TAG, "=========================================================================");
                    Log.e(TAG, "Count Value : " + count);
                    Log.e(TAG, "=========================================================================");
                    count++;
                    if(adapter.getCount() == 1)
                        break;
                    else if(adapter.getCount() >= 2 && i == 0) {
                        TextView name = (TextView)findViewById((int)adapter.getItemId(i));
                        TextView namebefore = (TextView)linearLayout.findViewById((int)adapter.getItemId(adapter.getCount()-1));
                        TextView nameafter = (TextView)linearLayout.findViewById((int)adapter.getItemId(i + 1));

                        name.setAccessibilityTraversalBefore((int)adapter.getItemId(adapter.getCount()-1));
                        name.setAccessibilityTraversalAfter((int)adapter.getItemId(i + 1));
                    }
                    else if(adapter.getCount() >= 2 && (i + 1) <= adapter.getCount() && i >= 1) {
                        TextView name = (TextView)linearLayout.findViewById((int)adapter.getItemId(i));
                        TextView namebefore = (TextView)linearLayout.findViewById((int)adapter.getItemId(i - 1));
                        TextView nameafter = (TextView)linearLayout.findViewById((int)adapter.getItemId(i + 1));

                        name.setAccessibilityTraversalBefore((int)adapter.getItemId(i - 1));
                        name.setAccessibilityTraversalAfter((int)adapter.getItemId(i + 1));
                    }
                    else if(adapter.getCount()-1 == i){
                        TextView name = (TextView)linearLayout.findViewById((int)adapter.getItemId(i));
                        TextView namebefore = (TextView)linearLayout.findViewById((int)adapter.getItemId(i - 1));
                        TextView nameafter = (TextView)linearLayout.findViewById((int)adapter.getItemId(0));

                        name.setAccessibilityTraversalBefore((int)adapter.getItemId(i - 1));
                        name.setAccessibilityTraversalAfter((int)adapter.getItemId(0));
                    }
                }
                adapter.notifyDataSetChanged();
                */
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        } );


    }
}
