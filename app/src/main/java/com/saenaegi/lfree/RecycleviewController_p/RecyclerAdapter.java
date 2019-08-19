package com.saenaegi.lfree.RecycleviewController_p;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.saenaegi.lfree.R;

import java.util.ArrayList;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ItemViewHolder> {

    public void delItem(int position) {
        Data data=new Data();
        data.setNum( position );
        data.setState( false );
       listData.set( position-1,data);
       notifyDataSetChanged();
    }

    public interface OnListItemSelectedInterface {
        void onItemSelected(View v, int position);
    }

    //private OnListItemSelectedInterface mListener;
    //private Context context;
    public OnListItemSelectedInterface mListener;
    public Context context;

    public RecyclerAdapter(Context context
            , OnListItemSelectedInterface listener) {
        this.context = context;
        this.mListener = listener;
    }

    private ArrayList<Data> listData = new ArrayList<>();
    // Item의 클릭 상태를 저장할 array 객체
    private SparseBooleanArray selectedItems = new SparseBooleanArray();
    // 직전에 클릭됐던 Item의 position
    private int prePosition = -1;

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // LayoutInflater를 이용하여 전 단계에서 만들었던 item.xml을 inflate 시킵니다.
        // return 인자는 ViewHolder 입니다.
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycleview_part_item, parent, false);
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

    public void addItem(Data data) {
        // 외부에서 item을 추가시킬 함수입니다.
        listData.add(data);
    }

    // RecyclerView의 핵심인 ViewHolder 입니다.
    // 여기서 subView를 setting 해줍니다.
    public class ItemViewHolder extends RecyclerView.ViewHolder {

        public Data data;
        public ImageView imageView;
        private TextView textView;
        private int position;

        ItemViewHolder(View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.part_button);
            textView = itemView.findViewById(R.id.part_num);
        }

        void onBind(Data data, int position) {
            this.data = data;
            this.position = position;

            textView.setText(Integer.toString(data.getNum()));

            if(data.getState()) {
                imageView.setImageResource(R.drawable.on_button);
                imageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mListener.onItemSelected(v, getAdapterPosition());
                    }
                });
            }
            else
                imageView.setImageResource(R.drawable.off_button);
        }
    }
}
