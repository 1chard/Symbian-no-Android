package richard.symbian_android.jdbc

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import richard.symbian_android.model.Endereco
import richard.symbian_android.model.Usuario
import java.sql.SQLException

class Banco(context: Context) : SQLiteOpenHelper(context, "pedro", null, VERSION) {

    companion object {
        private lateinit var BANCO: Banco;

        fun getBanco(context: Context): Banco {
            if (!::BANCO.isInitialized) {
                BANCO = Banco(context);
            }

            return BANCO
        }

        val VERSION = 1;
    }

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(
            """CREATE table usuario(
                    idusuario integer not null primary key autoincrement,
                    nome text not null,
                    sobrenome text not null,
                    login text not null,
                    senha text not null
                );"""
        )

        db.execSQL(
            """CREATE table endereco(
                    idendereco integer not null primary key autoincrement,
                    cep text not null,
                    numero text not null,
                    complemento text not null,
                    idusuario int not null,
                    foreign key (idusuario) references usuario(idusuario)
                );"""
        )
    }


    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        TODO("Not yet implemented")
    }

    fun loginUsuario(login: String, senha: String): Usuario? {
        val readableDB = readableDatabase

        return try {
            readableDB.use {
                it.rawQuery(
                    "select usuario.*, endereco.idendereco, endereco.cep, endereco.complemento, endereco.numero from usuario left join endereco on usuario.idusuario = endereco.idusuario where usuario.login = ? and usuario.senha = ? order by endereco.idendereco desc;",
                    arrayOf(login, senha)
                ).use { cursor ->
                    return if (cursor.moveToNext()) {
                        Usuario(
                            cursor.getString(cursor.getColumnIndexOrThrow("nome")),
                            cursor.getString(cursor.getColumnIndexOrThrow("sobrenome")),
                            cursor.getString(cursor.getColumnIndexOrThrow("login")),
                            cursor.getString(cursor.getColumnIndexOrThrow("senha")),
                            if (!cursor.isNull(cursor.getColumnIndexOrThrow("idendereco"))) Endereco(
                                cursor.getString(cursor.getColumnIndexOrThrow("cep")),
                                cursor.getString(cursor.getColumnIndexOrThrow("numero")),
                                cursor.getString(cursor.getColumnIndexOrThrow("complemento")),
                                cursor.getInt(cursor.getColumnIndexOrThrow("idendereco"))
                            ) else null,
                            cursor.getInt(cursor.getColumnIndexOrThrow("idusuario")),
                        )
                    } else {
                        null
                    }
                }
            }
        } catch (e: Exception) {
            Log.e("insertUsuario", e.message ?: "")
            null
        }
    }

    fun insertUsuario(usuario: Usuario): Boolean {
        val writableDB = writableDatabase;

        return try {
            writableDB.beginTransaction()

            val contentValues = ContentValues()

            contentValues.put("nome", usuario.nome)
            contentValues.put("sobrenome", usuario.sobrenome)
            contentValues.put("login", usuario.login)
            contentValues.put("senha", usuario.senha)

            writableDB.insertOrThrow("usuario", null, contentValues)
            writableDB.setTransactionSuccessful()


            writableDB.endTransaction()
            true
        } catch (e: SQLException) {
            Log.e("insertUsuario", e.message ?: "")
            false
        }
    }

    fun insertEndereco(endereco: Endereco, idusuario: Int): Boolean {
        val writableDB = writableDatabase;

        return try {
            writableDB.beginTransaction()

            val contentValues = ContentValues()

            contentValues.put("cep", endereco.cep)
            contentValues.put("numero", endereco.numero)
            contentValues.put("complemento", endereco.complemento)
            contentValues.put("idusuario", idusuario)

            writableDB.insertOrThrow("endereco", null, contentValues)
            writableDB.setTransactionSuccessful()

            writableDB.endTransaction()
            true
        } catch (e: SQLException) {
            Log.e("insertEndereco", e.message ?: "")
            false
        }
    }
}