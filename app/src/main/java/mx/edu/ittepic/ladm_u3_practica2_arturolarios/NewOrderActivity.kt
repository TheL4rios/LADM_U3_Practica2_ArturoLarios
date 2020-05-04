package mx.edu.ittepic.ladm_u3_practica2_arturolarios

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_new_order.*
import mx.edu.ittepic.ladm_u3_practica1_arturolarios.Utils.Utils
import java.io.Serializable

class NewOrderActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_order)
        this.title = "Nueva Orden"

        rdProduct.isChecked = true

        val data = intent.extras

        sinceUpdate(data)

        rdProduct.setOnClickListener {
            hintProduct.hint = "Producto"
        }

        rdDescription.setOnClickListener {
            hintProduct.hint = "Descripción"
        }

        btnCancel.setOnClickListener {
            finish()
        }

        btnSave.setOnClickListener {
            if (data == null)
                saveOrder()
            else
                updateOrder(data)
        }
    }

    private fun updateOrder(data : Bundle)
    {
        if (isValid())
        {
            val db = FirebaseFirestore.getInstance()

            db.collection("restaurant").document(data.getString("id") ?: "")
                .set(getData())
                .addOnSuccessListener {
                    Utils.showToastMessageLong("Se actualizó correctamente", this)
                    finish()
                }
                .addOnFailureListener {
                    Utils.showAlertMessage("Atención", "Algo salió mal, por favor verifique su conexión a internet", this)
                }
        }
    }

    private fun sinceUpdate(data : Bundle?)
    {
        data?.let { d ->
            this.title = "Actualizar Cliente"
            txtName.setText(d.getString("name"))
            txtAddress.setText(d.getString("address"))
            txtPhoneNumber.setText(d.getString("phone"))

            if (d.getString("type").equals("Producto"))
            {
                rdProduct.isChecked = true
                hintProduct.hint = "Producto"
            }
            else
            {
                rdDescription.isChecked = true
                hintProduct.hint = "Descripción"
            }

            txtProduct.setText(d.getString("description_product"))
            txtPrice.setText(d.getFloat("price").toString())
            npQuantity.progress = d.getInt("quantity")
            chkDelivered.isChecked = d.getBoolean("delivered")
        }
    }

    private fun saveOrder()
    {
        if (isValid())
        {
            val db = FirebaseFirestore.getInstance()

            db.collection("restaurant").add(getData())
                .addOnSuccessListener {
                    clean()
                    Utils.showToastMessageLong("Se insertó correctamente", this)
                }
                .addOnFailureListener {
                    Utils.showAlertMessage("Atención", "Algo salió mal, por favor verifique su conexión a internet", this)
                }
        }
    }

    private fun getData() : HashMap<String, Serializable>
    {
        val type =
            if (rdProduct.isChecked)
                "product"
            else
                "description"

        val data = hashMapOf(
                "name" to txtName.text.toString(),
                "address" to txtAddress.text.toString(),
                "phone" to txtPhoneNumber.text.toString(),
                "order" to hashMapOf(
                    type to txtProduct.text.toString(),
                    "price" to txtPrice.text.toString().toFloat(),
                    "quantity" to npQuantity.progress,
                    "delivered" to chkDelivered.isChecked
                )
            )

        return data
    }

    private fun clean()
    {
        txtName.setText("")
        txtAddress.setText("")
        txtPhoneNumber.setText("")
        txtProduct.setText("")
        txtPrice.setText("")
        npQuantity.progress = 1
        chkDelivered.isChecked = false
    }

    private fun isValid() : Boolean
    {
        if (txtName.text.toString().isEmpty())
        {
            Utils.showAlertMessage("Atención", "Por favor llene el campo 'Nombre'", this)
            return false
        }

        if (txtAddress.text.toString().isEmpty())
        {
            Utils.showAlertMessage("Atención", "Por favor llene el campo 'Domicilio'", this)
            return false
        }

        if (txtPhoneNumber.text.toString().isEmpty())
        {
            Utils.showAlertMessage("Atención", "Por favor llene el campo 'Celular'", this)
            return false
        }

        if (txtProduct.text.toString().isEmpty())
        {
            Utils.showAlertMessage("Atención", "Por favor llene el campo 'Producto/Descripción'", this)
            return false
        }

        if (txtPrice.text.toString().isEmpty())
        {
            Utils.showAlertMessage("Atención", "Por favor llene el campo 'Precio'", this)
            return false
        }

        return true
    }
}
