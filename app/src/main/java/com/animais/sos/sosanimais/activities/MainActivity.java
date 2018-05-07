package com.animais.sos.sosanimais.activities;


import android.app.Activity;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import com.animais.sos.sosanimais.R;
import com.animais.sos.sosanimais.dao.AnimalDAO;
import com.animais.sos.sosanimais.dao.Db;
import com.animais.sos.sosanimais.model.Animal;

import java.util.List;

public class MainActivity extends AppCompatActivity {


    private ArrayAdapter<Animal> dadosAdapter;
    private ListView listView;
    private Button btnNovo;
    private static final int REQUEST_CREATE = 1;

    private class getAllDataTask extends AsyncTask<Void, Void, List<Animal>> {

        @Override
        public List<Animal> doInBackground(Void... params) {
            SQLiteDatabase db = new Db(MainActivity.this).getReadableDatabase();
            AnimalDAO dao = new AnimalDAO(db);
            List<Animal> list = null;

            try {
                list = dao.getAllData();
            } finally {
                db.close();
            }
            return list;
        }

        @Override
        public void onPostExecute(List<Animal> notes) {
            dadosAdapter.addAll(notes);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.dadosAdapter = new ArrayAdapter<Animal>(this, android.R.layout.simple_list_item_1);
        this.listView = (ListView) findViewById(R.id.listView);
        this.listView.setAdapter(dadosAdapter);
        this.listView.setOnItemClickListener(new ListView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                animalItemClicked(position);
            }
        });
        this.btnNovo = (Button) findViewById(R.id.btnNovo);
        this.btnNovo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                inserirDado();
            }
        });
        // lista todos os dados
        new getAllDataTask().execute();
    }

    // manda para o detalhe do item
    private void animalItemClicked(int position) {
        Animal obj = dadosAdapter.getItem(position);
        Intent intent = new Intent(this, DetailAnimalActivity.class);

        intent.putExtra("animal", obj);
        startActivity(intent);
    }

    private void inserirDado() {
        Intent intent = new Intent(this, CreateAnimalActivity.class);

        startActivityForResult(intent, REQUEST_CREATE);
    }

    @Override
    public void onActivityResult (int requestCode, int resultCode, Intent data) {
        if ((requestCode == REQUEST_CREATE) && (resultCode == Activity.RESULT_OK)) {
            Animal obj = (Animal) data.getSerializableExtra("animal");

            this.dadosAdapter.add(obj);
        }
    }
}
