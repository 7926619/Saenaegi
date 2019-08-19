package com.saenaegi.lfree.RecycleviewController_s;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.saenaegi.lfree.R;

import java.util.ArrayList;

public class RecyclerAdapterS extends RecyclerView.Adapter<RecyclerAdapterS.ItemViewHolder> {

    public interface OnListItemSelectedInterface {
        void onItemSelectedS(View v, int position);
    }

    //private RecyclerAdapterS.OnListItemSelectedInterface mListener;
    //private Context context;
    public RecyclerAdapterS.OnListItemSelectedInterface mListener;
    public Context context;

    public RecyclerAdapterS(Context context
            , OnListItemSelectedInterface listener) {
        this.context = context;
        this.mListener = listener;
    }

    // adapter에 들어갈 list 입니다.
    private ArrayList<DataS> listData = new ArrayList<>();
    // Item의 클릭 상태를 저장할 array 객체
    private SparseBooleanArray selectedItems = new SparseBooleanArray();
    // 직전에 클릭됐던 Item의 position
    private int prePosition = -1;

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // LayoutInflater를 이용하여 전 단계에서 만들었던 item.xml을 inflate 시킵니다.
        // return 인자는 ViewHolder 입니다.
        context = parent.getContext();
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycleview_subtitle_item, parent, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        // Item을 하나, 하나 보여주는(bind 되는) 함수입니다.
        holder.onBind(listData.get(position), position);
    }

    @Override
    public int getItemCount() {
        // RecyclerView의 총 개수 입니다.
        return listData.size();
    }

    public void addItem(DataS dataS) {
        // 외부에서 item을 추가시킬 함수입니다.
        listData.add(dataS);
    }

    public void removeItem(int Position){
        listData.remove( Position );
    }
    public void delItem() {
        listData.clear();
    }
    // RecyclerView의 핵심인 ViewHolder 입니다.
    // 여기서 subView를 setting 해줍니다.
    public class ItemViewHolder extends RecyclerView.ViewHolder {

        private FirebaseDatabase firebaseDatabase=FirebaseDatabase.getInstance();
        private DatabaseReference databaseReference=firebaseDatabase.getReference().child( "LFREE" );
        private TextView nameView;
        public ImageButton subtitleButton;
        public ImageButton soundButton;
        public ImageButton moreButton;
        private DataS dataS;
        private int position;

        ItemViewHolder(View itemView) {
            super(itemView);

            nameView = itemView.findViewById(R.id.textView1);
            subtitleButton = itemView.findViewById(R.id.sub_subtitle);
            soundButton = itemView.findViewById(R.id.sub_sound);
            moreButton = itemView.findViewById(R.id.sub_more);
        }

        void onBind(DataS dataS, int position) {
            this.dataS = dataS;
            this.position = position;

            nameView.setText(dataS.getName());

            if(!dataS.getOnSubtitle()) {
                subtitleButton.setAlpha((float) 0.3);
            }
            else {
                subtitleButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mListener.onItemSelectedS(v, getAdapterPosition());
                    }
                });
            }

            if(!dataS.getOnSound())
                soundButton.setAlpha((float) 0.3);
            else {
                soundButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mListener.onItemSelectedS(v, getAdapterPosition());
                    }
                });
            }

            if(!dataS.getOnMore())
                moreButton.setAlpha( (float)0.3 );

            else {
                moreButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mListener.onItemSelectedS( v, getAdapterPosition() );
                    }
                });
            }
        }

    }
}
