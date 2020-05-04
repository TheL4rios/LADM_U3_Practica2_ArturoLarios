package mx.edu.ittepic.ladm_u3_practica2_arturolarios

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.recyclerview.widget.GridLayoutManager
import com.google.firebase.firestore.FirebaseFirestore
import com.thelarios.firebaseservices.Adapters.AdapterShow
import kotlinx.android.synthetic.main.activity_main.*
import mx.edu.ittepic.ladm_u3_practica1_arturolarios.Utils.Utils
import mx.edu.ittepic.ladm_u3_practica2_arturolarios.Data.DataOrder

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        recyclerList.layoutManager = GridLayoutManager(this, 2)
        this.title = "Restaurante"

        val db = FirebaseFirestore.getInstance()
        val list = ArrayList<DataOrder>()

        db.collection("restaurant")
            .addSnapshotListener { querySnapshot, firebaseFirestoreException ->
                if (firebaseFirestoreException != null)
                {
                    Utils.showToastMessageLong("Algo salió mal, por favor verifique su conexión a internet", this)
                    return@addSnapshotListener
                }

                list.clear()

                var product = ""
                var type = "Descripción"

                querySnapshot?.let {
                    for (document in querySnapshot)
                    {
                        type = "Descripción"
                        product = document.getString("order.description").toString()

                        if (document.getString("order.product") != null)
                        {
                            product = document.getString("order.product").toString()
                            type = "Producto"
                        }

                        list.add(
                            DataOrder(document.id,
                                document.getString("name") ?: "",
                                document.getString("address") ?: "",
                                document.getString("phone") ?: "",
                                product,
                                document.get("order.price").toString().toFloat(),
                                document.get("order.quantity").toString().toInt(),
                                document.getBoolean("order.delivered") ?: false,
                                type)
                        )
                    }

                    if (list.size == 0)
                    {
                        Utils.showToastMessageLong("No hay ordenes", this)
                    }

                    recyclerList.adapter = AdapterShow(list, this)
                }
            }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId)
        {
            R.id.new_order -> startActivity(Intent(this, NewOrderActivity :: class.java))
        }
        return super.onOptionsItemSelected(item)
    }
}
