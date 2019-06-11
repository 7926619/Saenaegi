package com.saenaegi.lfree;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
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
import com.saenaegi.lfree.SubtitleController.SubtitleAndKey;
import com.saenaegi.lfree.SubtitleController.SubtitleData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

public class aSelectPartActivity extends AppCompatActivity {

    public ListView listView;
    public aListviewAdapter adapter;

    public ArrayList<TextView> sectionList = new ArrayList<>();
    public ArrayList<TextView> sectionbeforeList = new ArrayList<>();
    public ArrayList<TextView> sectionafterList = new ArrayList<>();
    public ConstraintLayout constraintLayout;

    private ArrayList<aListviewItem> sdata = new ArrayList<>();
    private String videoID;
    private String[] madesection;
    private int sectionCount;

    private int count = 0;

    public static Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        setContentView(R.layout.activity_a_select_part);

        final Intent data=getIntent();
        videoID = data.getExtras().getString("link");
        sectionCount=data.getExtras().getInt( "count" );
        madesection=data.getExtras().getStringArray( "madesection" );

        sectionCount=10;
        listView = (ListView) findViewById(R.id.listview);
        adapter = new aListviewAdapter(this, R.layout.a_list_item, sdata);

        listView.setItemsCanFocus(true);
        listView.setAdapter(adapter);

        //만들어 진 secton Num만 출력이 되는 지 확인 -> subtitle의 type의 값이 false인 데이터만 취급해야 한다.
        sdata.clear();
        for(int i=0;i<madesection.length;i++){
            if(madesection[i]!=null) {
                aListviewItem temp = new aListviewItem( madesection[i] );
                sdata.add( temp );
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

                sectionList.add(row);
                sectionbeforeList.add(rowbefore);
                sectionafterList.add(rowafter);
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

                sectionList.add(row);
                sectionbeforeList.add(rowbefore);
                sectionafterList.add(rowafter);
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

                sectionList.add(row);
                sectionbeforeList.add(rowbefore);
                sectionafterList.add(rowafter);
            }
        }

        adapter.notifyDataSetChanged();

        listView.setOnItemClickListener( new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(aSelectPartActivity.this, aWatchVideoActivity.class);
                intent.putExtra("link",videoID);
                intent.putExtra("count",sectionCount);
                intent.putExtra( "nowSection", madesection[position]);
                startActivity(intent);
            }
        } );

        //adapter.notifyDataSetChanged();

        context = this;
    }
}
