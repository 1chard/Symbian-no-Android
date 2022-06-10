package richard.symbian_android.model

data class Endereco(
    @JvmField var cep : String,
    @JvmField var numero: String?,
    @JvmField var complemento: String,
    @JvmField var idEndereco : Int? = null
)