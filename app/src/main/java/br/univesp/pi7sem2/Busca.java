package br.univesp.pi7sem2;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.TranslateAnimation;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.SearchView;
import android.widget.Toast;


public class Busca extends Fragment {
    SearchView searchView;
    View myView;
    Button myButton;
    boolean isUp;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view =inflater.inflate(R.layout.busca, null);

        searchView=(SearchView) view.findViewById(R.id.searchView);
        searchView.setQueryHint("Busca");
        searchView.setIconifiedByDefault(true);

        myView = view.findViewById(R.id.my_view);
        myButton = (Button) view.findViewById(R.id.my_button);

        myView.setVisibility(View.GONE);
        isUp = false;

        myButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isUp) {

                    myView.setVisibility(View.GONE);

                } else {
                    myView.setVisibility(View.VISIBLE);

                }
                isUp = !isUp;

            }
        });



        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String query) {
                Toast.makeText(getActivity(), query, Toast.LENGTH_LONG).show();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        WebView wv = (WebView) view.findViewById(R.id.webView);
        wv.loadUrl("file:///android_asset/htmls/file.html");

    return view;
    }

}
