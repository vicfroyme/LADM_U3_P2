package mx.tecnm.tepic.ladm_u3_p2

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    var baseRemota = FirebaseFirestore.getInstance()
    var datalista = ArrayList<String>()
    var listaID = ArrayList<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btnInsertar.setOnClickListener {
            insertar()
        }

        baseRemota.collection("pedidos")
            .addSnapshotListener { querySanapshot, error ->
                if (error != null) {
                    mensaje(error.message!!)
                    return@addSnapshotListener
                }
                datalista.clear()
                listaID.clear()
                for (document in querySanapshot!!) {
                    var cadena = "Nombre: ${document.getString("nombre")} Pedido: ${document.getString("pedido.item.descripcion")}"
                    datalista.add(cadena)

                    listaID.add(document.id.toString())
                }
                lista.adapter = ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, datalista)
            }
    }

    private fun insertar(){
        var datosInsertar = hashMapOf(
            "celular" to celular.text.toString(),
            "entregado" to entregado.text.toString().toBoolean(),
            "fecha" to fecha.text.toString(),
            "nombre" to nombre.text.toString(),
            "pedido" to hashMapOf(
                "item" to hashMapOf(
                "cantidad" to cantidad.text.toString().toInt(),
                "descripcion" to descripcion.text.toString(),
                "precio" to precio.text.toString().toInt()
                )
            ),
            "total" to precio.text.toString().toInt() * cantidad.text.toString().toInt()
        )

        baseRemota.collection("pedidos")
            .add(datosInsertar)
            .addOnSuccessListener {
                alerta("SE INSERTO CORRECTAMENTE")
                celular.setText("")
                entregado.setText("")
                fecha.setText("")
                nombre.setText("")
                cantidad.setText("")
                descripcion.setText("")
                precio.setText("")
            }
            .addOnFailureListener {
                mensaje("ERROR: ${it.message}!!}")
            }
    }

    private fun alerta(s: String) {
        Toast.makeText(this,s, Toast.LENGTH_LONG).show()
    }

    private fun mensaje(s: String) {
        AlertDialog.Builder(this).setTitle("ATENCIÓN")
            .setMessage(s)
            .setPositiveButton("OK"){d,i->}
            .show()
    }

}
