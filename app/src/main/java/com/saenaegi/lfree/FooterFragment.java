package com.saenaegi.lfree;

import android.support.v4.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class FooterFragment extends Fragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_footer, container, false);

        TextView submenu1 = (TextView) view.findViewById(R.id.submenu1);
        TextView submenu2 = (TextView) view.findViewById(R.id.submenu2);
        TextView submenu3 = (TextView) view.findViewById(R.id.submenu3);

        submenu1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), HowToUseActivity.class);
                startActivity(intent);
            }
        });

        submenu2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), PrivacyActivity.class);
                startActivity(intent);
            }
        });

        submenu3.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), CenterActivity.class);
                startActivity(intent);
            }
        });

        return view;
    }
}
