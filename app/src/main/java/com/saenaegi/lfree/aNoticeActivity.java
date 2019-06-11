package com.saenaegi.lfree;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.saenaegi.lfree.Data.Notice;
import com.saenaegi.lfree.ListviewController.aListviewAdapter;
import com.saenaegi.lfree.ListviewController.aListviewItem;

import java.util.ArrayList;

public class aNoticeActivity extends AppCompatActivity {

    public ArrayList<TextView> noticeList = new ArrayList<>();
    public ArrayList<TextView> noticebeforeList = new ArrayList<>();
    public ArrayList<TextView> noticeafterList = new ArrayList<>();
    public ListView listView;
    public aListviewAdapter adapter;
    public ConstraintLayout constraintLayout;

    private ArrayList<aListviewItem> data = new ArrayList<>();

    private FirebaseDatabase firebaseDatabase=FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference=firebaseDatabase.getReference().child( "LFREE" ).child( "NOTICE" );
    private ArrayList<Notice> notices=new ArrayList<>();
    private ProgressBar progressBar;

    private int count = 0;

    public static Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        setContentView(R.layout.activity_a_notice);

        listView = (ListView) findViewById(R.id.listview);
        adapter = new aListviewAdapter(this, R.layout.a_list_item, data);
        listView.setAdapter(adapter);

        progressBar = findViewById(R.id.progressBar3);
        progressBar.setMax(100);

        getDataQuery();

        context = this;
    }

    public void  getDataQuery(){

        databaseReference.addValueEventListener( new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot:dataSnapshot.getChildren()){
                    Notice notice=snapshot.getValue(Notice.class);
                    notices.add( notice );
                    String read=notice.getSubtitle()+"  내용 : "+notice.getText();
                    aListviewItem aListviewItem=new aListviewItem( read);
                    data.add( aListviewItem );
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

                        noticeList.add(row);
                        noticebeforeList.add(rowbefore);
                        noticeafterList.add(rowafter);
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

                        noticeList.add(row);
                        noticebeforeList.add(rowbefore);
                        noticeafterList.add(rowafter);
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

                        noticeList.add(row);
                        noticebeforeList.add(rowbefore);
                        noticeafterList.add(rowafter);
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
