package br.univesp.pi7sem2;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;

public class Leia extends Activity{
    @Override
    public void onCreate(Bundle bundle){
        super.onCreate(bundle);
        setContentView(R.layout.leia);

        Button button = (Button) findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        SharedPreferences leia = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = leia.edit();
        editor.putBoolean("valor", false);
        editor.apply();
    }
}
