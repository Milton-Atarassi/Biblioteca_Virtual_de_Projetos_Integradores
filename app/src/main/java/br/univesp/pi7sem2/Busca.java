package br.univesp.pi7sem2;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;

import br.univesp.pi7sem2.BancoDeDados.BancoDados;


public class Busca extends Fragment {
    static ArrayList<String> dado;
    static ArrayList<ArrayList<String>> dados;
    static ArrayList<String> lables;
    static ListView listView;
    static Context context;
    SearchView searchView;
    View myView;
    Button myButton;
    boolean isUp;
    WebView wv;
    TextView result;

    public static void dados(String query) {

        try {
/*            TestAdapter mDbHelper = new TestAdapter(getActivity());
            mDbHelper.createDatabase();
            mDbHelper.open();
            Cursor testdata = mDbHelper.getTestData();*/

            BancoDados mDbHelper = new BancoDados(context);
            mDbHelper.open();
            Cursor testdata = mDbHelper.searchSQL(query);

            lables = new ArrayList<String>();
            dados = new ArrayList<>();
            ArrayList<String> temp = null;
            int colcount = testdata.getColumnCount();
            int j = 1;
            while (!testdata.isAfterLast()) {
                temp = new ArrayList<>();
                for (int i = 0; i < colcount; i++) {
                    temp.add(testdata.getString(i));
                }
                dados.add(temp);
                lables.add(j + " - " + temp.get(6) + "\n" + temp.get(2));
                testdata.moveToNext();
                j++;
            }
            mDbHelper.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void atualizar() {
        dados("SELECT * FROM projetos");
        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(context,
                android.R.layout.simple_list_item_1, lables);
        listView.setAdapter(adapter2);
    }

    public static void init_query() {
        dados("SELECT * FROM projetos ORDER BY id DESC LIMIT 10;");
        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(context,
                android.R.layout.simple_list_item_1, lables);
        listView.setAdapter(adapter2);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.busca, null);
        context = getActivity();
        result = (TextView) view.findViewById(R.id.resultados);

        searchView = (SearchView) view.findViewById(R.id.searchView);
        searchView.setQueryHint("Dica: use * como coringa");
        //   searchView.setIconifiedByDefault(true);

        myView = view.findViewById(R.id.my_view);
        myButton = (Button) view.findViewById(R.id.my_button);

        myView.setVisibility(View.GONE);
        isUp = false;

        myButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vis();
            }
        });

        final CheckBox todos = (CheckBox) view.findViewById(R.id.todos);
        final CheckBox titulo = (CheckBox) view.findViewById(R.id.titulo);
        final CheckBox descricao = (CheckBox) view.findViewById(R.id.descricao);
        final CheckBox assuntos = (CheckBox) view.findViewById(R.id.assuntos);
        final CheckBox autores = (CheckBox) view.findViewById(R.id.autores);

        todos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!todos.isChecked()) {
                    titulo.setChecked(false);
                    descricao.setChecked(false);
                    assuntos.setChecked(false);
                    autores.setChecked(false);
                } else {
                    titulo.setChecked(true);
                    descricao.setChecked(true);
                    assuntos.setChecked(true);
                    autores.setChecked(true);
                }
            }
        });


        final Spinner spin_cursos = (Spinner) view.findViewById(R.id.cursos);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.cursos, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spin_cursos.setAdapter(adapter);

        final Spinner spin_polos = (Spinner) view.findViewById(R.id.polos);
        ArrayAdapter<CharSequence> adapter1 = ArrayAdapter.createFromResource(getActivity(),
                R.array.polos, android.R.layout.simple_spinner_item);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spin_polos.setAdapter(adapter1);

        listView = (ListView) view.findViewById(R.id.listView);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                try {
                    Intent intent = new Intent(getActivity(), Resultado.class);
                    intent.putExtra("id", dados.get(position).get(16));
                    startActivity(intent);
                } catch (Exception e) {
                    e.printStackTrace();

                }
            }
        });


        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String query) {
                if (isUp) myView.setVisibility(View.GONE);
                isUp = !isUp;

                wv.setVisibility(View.GONE);

                lables = new ArrayList<String>();
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
                        android.R.layout.simple_list_item_1, lables);
                listView.setAdapter(adapter);

/*                String[] qr = query.split(" ");
                String a="";
                for(int i=0;i<qr.length;i++){
                    qr[i]="^"+qr[i]+"* ";
                    a=a+qr[i];
                }
                query=a;*/

                String termo = "";
                String table = "";
                if (!spin_cursos.getSelectedItem().toString().equals("TODOS")) {
                    table = "SELECT * FROM projetos WHERE curso MATCH '" + spin_cursos.getSelectedItem().toString() + "'";
                    if (!spin_polos.getSelectedItem().toString().equals("TODOS"))
                        table = table + " INTERSECT " + "SELECT * FROM projetos WHERE polo MATCH '" + spin_polos.getSelectedItem().toString() + "'";
                } else if (!spin_polos.getSelectedItem().toString().equals("TODOS"))
                    table = "SELECT * FROM projetos WHERE polo MATCH '" + spin_polos.getSelectedItem().toString() + "'";


                if (!query.equals("*")) {
                    if (titulo.isChecked() || descricao.isChecked() || assuntos.isChecked() || autores.isChecked()) {

                        if (titulo.isChecked())
                            termo = termo + "SELECT * FROM projetos WHERE projetos MATCH 'titulo:" + query + "' UNION ";
                        if (descricao.isChecked())
                            termo = termo + "SELECT * FROM projetos WHERE projetos MATCH 'descricao:" + query + "' UNION ";
                        if (assuntos.isChecked())
                            termo = termo + "SELECT * FROM projetos WHERE projetos MATCH 'assuntos:" + query + "' UNION ";
                        if (autores.isChecked())
                            termo = termo + "SELECT * FROM projetos WHERE projetos MATCH 'autores:" + query + "' UNION ";
                        termo = termo.substring(0, termo.length() - 7);
                    }
                } else {
                    termo = "SELECT * FROM projetos";
                }

                if (!table.equals("")) termo = termo + " INTERSECT ";


                dados(termo + table);

                ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(getActivity(),
                        android.R.layout.simple_list_item_1, lables);
                listView.setAdapter(adapter1);

                result.setVisibility(View.VISIBLE);
                result.setText("Resultado da busca:");

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }


        });

        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                lables = new ArrayList<String>();
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
                        android.R.layout.simple_list_item_1, lables);
                listView.setAdapter(adapter);

                wv.setVisibility(View.VISIBLE);
                result.setVisibility(View.INVISIBLE);

                return false;
            }
        });

        wv = (WebView) view.findViewById(R.id.webView);
        wv.loadUrl("file:///android_asset/htmls/file.html");

        result.setVisibility(View.VISIBLE);
        result.setText("Projetos recentemente inseridos:");

        init_query();

        return view;
    }

    public void vis() {
        if (isUp) {

            myView.setVisibility(View.GONE);

        } else {
            myView.setVisibility(View.VISIBLE);

        }
        isUp = !isUp;

    }
}
