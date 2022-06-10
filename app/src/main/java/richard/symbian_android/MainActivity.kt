package richard.symbian_android

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import richard.symbian_android.jdbc.Banco
import richard.symbian_android.jdbc.Session
import richard.symbian_android.model.Usuario

class MainActivity : AppCompatActivity() {
    lateinit var login : EditText
    lateinit var senha : EditText
    lateinit var cadastrar : Button
    lateinit var entrar : Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        startView()
        startEvents()
    }

    private fun startView(){
        login = findViewById(R.id.login_login)
        senha = findViewById(R.id.login_senha)
        cadastrar = findViewById(R.id.login_cadastrar)
        entrar = findViewById(R.id.login_entrar)
    }

    private fun startEvents(){
        cadastrar.setOnClickListener { startActivity(Intent(this, CadastroActivity::class.java)) }
        entrar.setOnClickListener {
            val user =
                Banco.getBanco(this).loginUsuario(login.text.toString(), senha.text.toString())

            if(user != null){
                Session.usuario = user

                startActivity(Intent(this, EnderecoActivity::class.java))
            } else {
                Toast.makeText(this, "n foi", Toast.LENGTH_SHORT).show()
            }
        }
    }

}