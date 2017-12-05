package br.univesp.pi7sem2;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;


public class Sobre extends AppCompatActivity {

    @Override
    public void onCreate(Bundle Saved){
        super.onCreate(Saved);
        setContentView(R.layout.sobre);
        Toolbar toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.toolbar);
        //toolbar.showOverflowMenu();
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

    }


}
