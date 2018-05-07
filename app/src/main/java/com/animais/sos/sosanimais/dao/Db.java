package com.animais.sos.sosanimais.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;
import android.util.Log;

// classe responsável por criar a tabela de dados
public class Db extends SQLiteOpenHelper {
    private static final int VERSION = 1;
    private static final String DB_NAME = "dbAnimal";

    public static class TAnimal implements BaseColumns {
        public static final String NAME = "animal";

        public static final String DESCRICAO = "descricao";
        public static final String CATEGORIA = "categoria";
        public static final String DATA = "data";
        public static final String IMAGEM = "imagem";
        public static final String RUA = "rua";
        public static final String BAIRRO = "bairro";
        public static final String ESTADO = "estado";
        public static final String CIDADE = "cidade";
        public static final String PAIS = "pais";
        public static final String[] ALL_COLUMNS = {
                _ID, CATEGORIA, RUA, BAIRRO, ESTADO, CIDADE, PAIS
        };

        public static final String CREATE =
                "CREATE TABLE " + NAME + " (" +
                        "    " + _ID + " INTEGER PRIMARY KEY AUTOINCREMENT,\n" +
                        "    " + DESCRICAO + " TEXT,\n" +
                        "    " + CATEGORIA + " TEXT,\n" +
                        "    " + DATA + " INTEGER,\n" +
                        "    " + RUA + " TEXT,\n" +
                        "    " + BAIRRO + " TEXT,\n" +
                        "    " + ESTADO + " TEXT,\n" +
                        "    " + CIDADE + " TEDT,\n" +
                        "    " + PAIS + " TEXT)\n";

        public static final String DROP =
                "DROP TABLE IF EXISTS " + NAME;

        public static final String SELECT_ALL_NOTES =
                "SELECT * FROM " + NAME;
    }

    public Db(Context context) {
        super(context, DB_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TAnimal.CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(TAnimal.DROP);
        onCreate(db);
    }
}
