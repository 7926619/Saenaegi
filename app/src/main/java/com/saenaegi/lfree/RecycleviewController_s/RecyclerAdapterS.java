package com.saenaegi.lfree.RecycleviewController_s;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.SparseBooleanArray;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.saenaegi.lfree.R;
import java.util.ArrayList;

public class RecyclerAdapterS extends RecyclerView.Adapter<RecyclerAdapterS.ItemViewHolder> {

    // adapter에 들어갈 list 입니다.
    private ArrayList<DataS> listData = new ArrayList<>();
    private Context context;
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

    // RecyclerView의 핵심인 ViewHolder 입니다.
    // 여기서 subView를 setting 해줍니다.
    class ItemViewHolder extends RecyclerView.ViewHolder {

        private TextView nameView;
        private ImageButton subtitleButton;
        private ImageButton soundButton;
        private ImageButton moreButton;
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

            if(!dataS.getOnSubtitle())
                subtitleButton.setAlpha((float) 0.3);
            if(!dataS.getOnSound())
                soundButton.setAlpha((float) 0.3);

            moreButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    View view1 = view;
                    PopupMenu p = new PopupMenu(moreButton.getContext(), view);
                    p.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            switch (item.getItemId()) {
                                case R.id.opt1:
                                    Toast.makeText(moreButton.getContext(), "1", Toast.LENGTH_SHORT).show();
                                    break;
                                case R.id.opt2:
                                    Toast.makeText(moreButton.getContext(), "2", Toast.LENGTH_SHORT).show();
                                    break;
                                case R.id.opt3:
                                    Toast.makeText(moreButton.getContext(), "3", Toast.LENGTH_SHORT).show();
                                    break;
                                case R.id.opt4:
                                    Toast.makeText(moreButton.getContext(), "4", Toast.LENGTH_SHORT).show();
                                    break;
                            }

                            return false;
                        }
                    });
                    // here you can inflate your menu
                    p.inflate(R.menu.subtitle_menu);
                    p.setGravity(Gravity.RIGHT);
                    p.show();
                }
            });
        }
    }
}
