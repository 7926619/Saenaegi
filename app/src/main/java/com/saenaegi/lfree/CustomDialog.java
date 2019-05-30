package com.saenaegi.lfree;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

public class CustomDialog extends Dialog implements View.OnClickListener{
    private static final int LAYOUT = R.layout.custom_dialog;

    private Context context;
    private Button okButton;
    private Button cancleButton;

    private int sectionCount;

    public CustomDialog(@NonNull Context context) {
        super(context);
        this.context = context;
    }

    public CustomDialog(@NonNull Context context, int count) {
        super(context);
        this.context = context;
        this.sectionCount = count;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(LAYOUT);

        okButton = findViewById(R.id.pbutton);
        cancleButton = findViewById(R.id.nbutton);
        okButton.setOnClickListener(this);
        cancleButton.setOnClickListener(this);

        Spinner dropdown = findViewById(R.id.spinner);
        String[] options = new String[sectionCount+1];
        options[0]="파트 선택" ;
        for(int i=0;i<sectionCount;i++)
            options[i+1]=String.valueOf( i+1 );
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this.context, android.R.layout.simple_spinner_dropdown_item, options);
        dropdown.setAdapter(adapter);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.nbutton:
                cancel();
                break;
            case R.id.pbutton:
                break;
        }
    }
}