package br.univesp.pi7sem2;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.SearchView;
import android.widget.Toast;


public class Busca extends Fragment {
    SearchView searchView;
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view =inflater.inflate(R.layout.busca, null);

        searchView=(SearchView) view.findViewById(R.id.searchView);
        searchView.setQueryHint("Busca");
        searchView.setIconifiedByDefault(true);

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
