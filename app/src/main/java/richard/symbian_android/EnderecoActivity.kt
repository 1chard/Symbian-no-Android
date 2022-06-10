package richard.symbian_android

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import richard.symbian_android.jdbc.Banco
import richard.symbian_android.jdbc.Session
import richard.symbian_android.model.Endereco
import richard.symbian_android.model.Usuario
import java.util.zip.Inflater

class EnderecoActivity : AppCompatActivity() {
    lateinit var cep : EditText
    lateinit var numero : EditText
    lateinit var complemento : EditText
    lateinit var cadastrar : Button
    lateinit var quem_logado : TextView



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_endereco)

        startView()
        startEvents()

        quem_logado.text = Session.usuario.toString()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_principal, menu);

        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.menu_principal_endereco -> startActivity(Intent(this, EnderecoActivity::class.java))
            R.id.menu_principal_sair -> {
                Session.usuario = null;
                startActivity(Intent(this, MainActivity::class.java))
            }

        }

        return super.onOptionsItemSelected(item);
    }

    private fun startView(){
        cep = findViewById(R.id.endereco_cep)
        numero = findViewById(R.id.endereco_numero)
        complemento = findViewById(R.id.endereco_complemento)
        cadastrar = findViewById(R.id.endereco_cadastrar)
        quem_logado = findViewById(R.id.endereco_quem_logado)
    }

    private fun startEvents(){
        cadastrar.setOnClickListener {
            val invalidET = arrayListOf(cep, complemento).find {
                it.text.toString().isBlank()
            }

            if(invalidET == null){
                val endereco = Endereco(cep.text.toString(), if(numero.text.isNullOrBlank()) null else numero.text.toString(), complemento.text.toString());

                Toast.makeText(this,
                if(Banco.getBanco(this).insertEndereco(endereco, Session.usuario!!.idUsuario!! )){
                    Session.usuario!!.endereco = endereco
                    quem_logado.text = Session.usuario.toString()
                    true
                } else {
                    false
                }.toString()
                    , Toast.LENGTH_SHORT).show()




            } else {
                invalidET.requestFocus();
                Toast.makeText(this, "O campo ${invalidET.hint.toString().lowercase()} está inválido", Toast.LENGTH_LONG).show()
            }
        }
    }
}