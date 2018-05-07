package com.animais.sos.sosanimais.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.animais.sos.sosanimais.model.Animal;
import com.animais.sos.sosanimais.model.Categoria;
import com.animais.sos.sosanimais.model.Endereco;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class AnimalDAO {
    private static final String TAG = AnimalDAO.class.getSimpleName();

    private SQLiteDatabase db;

    public AnimalDAO(SQLiteDatabase db) {
        this.db = db;
    }

    // lista todos os registros do banco
    public List<Animal> getAllData() {
        List<Animal> list = new ArrayList<>();
        Cursor cursor = this.db.query(Db.TAnimal.NAME,
                Db.TAnimal.ALL_COLUMNS, null, null, null, null, null);
        while (cursor.moveToNext()) {
            Animal obj = new Animal();
            Categoria cat = new Categoria();
            cat.setDescricao(cursor.getString(cursor.getColumnIndex(Db.TAnimal.CATEGORIA)));
            obj.setCategoria(cat);
            Endereco end = new Endereco();
            end.setRua(cursor.getString(cursor.getColumnIndex(Db.TAnimal.RUA)));
            end.setBairro(cursor.getString(cursor.getColumnIndex(Db.TAnimal.BAIRRO)));
            end.setCidade(cursor.getString(cursor.getColumnIndex(Db.TAnimal.CIDADE)));
            end.setEstado(cursor.getString(cursor.getColumnIndex(Db.TAnimal.ESTADO)));
            end.setPais(cursor.getString(cursor.getColumnIndex(Db.TAnimal.PAIS)));
            obj.setEndereco(end);
            obj.setDescricao(cursor.getString(cursor.getColumnIndex(Db.TAnimal.DESCRICAO)));
            long dateSecs = cursor.getLong(cursor.getColumnIndex(Db.TAnimal.DATA));
            Date date = new Date(dateSecs);
            obj.setDataCadastro(date);
            obj.setId(cursor.getLong(cursor.getColumnIndex(Db.TAnimal._ID)));
            list.add(obj);
        }
        return list;
    }

    //cria um novo registro no banco de dados
    public void inserir(Animal animal) {
        ContentValues values = new ContentValues();

        values.putNull(Db.TAnimal._ID);
        values.put(Db.TAnimal.DESCRICAO, animal.getDescricao());
        values.put(Db.TAnimal.DATA, animal.getDataCadastro().getTime());
        values.put(Db.TAnimal.CATEGORIA, animal.getCategoria().getDescricao());
        values.put(Db.TAnimal.RUA, animal.getEndereco().getRua());
        values.put(Db.TAnimal.BAIRRO, animal.getEndereco().getBairro());
        values.put(Db.TAnimal.ESTADO, animal.getEndereco().getEstado());
        values.put(Db.TAnimal.CIDADE, animal.getEndereco().getCidade());
        values.put(Db.TAnimal.PAIS, animal.getEndereco().getPais());

        long newRowId = this.db.insert(Db.TAnimal.NAME, null, values);

        if (newRowId == -1) {
            Log.e(TAG, "Error inserting animal.");
        } else {
            animal.setId(newRowId);
        }
    }
}
