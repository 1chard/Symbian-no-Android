package richard.symbian_android.model

data class Usuario(
    @JvmField var nome: String,
    @JvmField var sobrenome: String,
    @JvmField var login: String,
    @JvmField var senha: String,
    @JvmField var endereco: Endereco? = null,
    @JvmField var idUsuario: Int? = null
)