package richard.symbian_android

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import richard.symbian_android.jdbc.Banco
import richard.symbian_android.model.Usuario

class CadastroActivity : AppCompatActivity() {
    lateinit var nome : EditText
    lateinit var sobrenome : EditText
    lateinit var login : EditText
    lateinit var senha : EditText
    lateinit var cadastrar : Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cadastro)

        startView()
        startEvents()
    }

    private fun startView(){
        login = findViewById(R.id.cadastro_login)
        senha = findViewById(R.id.cadastro_senha)
        nome = findViewById(R.id.cadastro_nome)
        sobrenome = findViewById(R.id.cadastro_sobrenome)
        cadastrar = findViewById(R.id.cadastro_cadastrar)
    }

    private fun startEvents(){
        cadastrar.setOnClickListener {
            val invalidET = arrayListOf(nome, sobrenome, login, senha).find {
                it.text.toString().isBlank()
            }

            if(invalidET == null){
                Toast.makeText(this, Banco.getBanco(this).insertUsuario(
                    Usuario(nome.text.toString(), sobrenome.text.toString(), login.text.toString(), senha.text.toString())
                ).toString(), Toast.LENGTH_SHORT).show()
            } else {
                invalidET.requestFocus();
                Toast.makeText(this, "O campo ${invalidET.hint.toString().lowercase()} está inválido", Toast.LENGTH_LONG).show()
            }
        }
    }
}