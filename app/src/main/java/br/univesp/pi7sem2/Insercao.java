package br.univesp.pi7sem2;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;


public class Insercao extends Fragment {
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.insercao, null);

        Button login = (Button) view.findViewById(R.id.account);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://accounts.google.com/AccountChooser")));
            }
        });

        Button form = (Button) view.findViewById(R.id.form);
        form.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://docs.google.com/forms/d/e/1FAIpQLSdSJJF8EQi4pfESo8y230CeqS4I5sYiVeDv8yK0uOFakpo2_A/viewform")));
            }
        });

        return  view;
    }
}
