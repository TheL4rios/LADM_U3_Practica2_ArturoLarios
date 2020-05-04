package com.thelarios.firebaseservices.Adapters

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.item_order.view.*
import mx.edu.ittepic.ladm_u3_practica1_arturolarios.Utils.Utils
import mx.edu.ittepic.ladm_u3_practica2_arturolarios.Data.DataOrder
import mx.edu.ittepic.ladm_u3_practica2_arturolarios.NewOrderActivity
import mx.edu.ittepic.ladm_u3_practica2_arturolarios.R

class AdapterShow(private val data: List<DataOrder>, val activity : Context) : RecyclerView.Adapter<AdapterShow.Holder>()
{
    class Holder(itemView : View) : RecyclerView.ViewHolder(itemView) {
        @SuppressLint("SetTextI18n")
        fun bindView(item: DataOrder?, activity : Context) {
            item?.let {
                with(it)
                {
                    itemView.txtCostumer.text = "Cliente: $name"
                    itemView.txtAddressItem.text = "Dirección: $address"
                    itemView.txtPhoneNumberItem.text = "Celular: $phone"
                    itemView.txtOrder.text = "$type: $description_product"
                    itemView.txtDelivered.text =
                        if (delivered)
                            "Entregado"
                        else
                            "Pendiente"


                    itemView.setOnClickListener {
                        val costumer = "$name \n$address \n$phone"

                        AlertDialog.Builder(activity)
                            .setTitle("¿Qué desea hacer con este cliente?")
                            .setMessage(costumer)
                            .setNeutralButton("Cancelar"){_, _ ->}
                            .setNegativeButton("Eliminar Cliente"){_, _ ->
                                val db = FirebaseFirestore.getInstance()

                                db.collection("restaurant").document(id)
                                    .delete()
                                    .addOnSuccessListener {
                                        Utils.showToastMessageLong("Se eliminó correctamente", activity)
                                    }
                                    .addOnFailureListener {
                                        Utils.showAlertMessage("Atención", "Algo salió mal, por favor verifique su conexión a internet", activity)
                                    }
                            }
                            .setPositiveButton("Actualizar Datos"){_, _ ->
                                val newActivity = Intent(activity, NewOrderActivity :: class.java)

                                newActivity.putExtra("id", id)
                                newActivity.putExtra("name", name)
                                newActivity.putExtra("address", address)
                                newActivity.putExtra("phone", phone)
                                newActivity.putExtra("description_product", description_product)
                                newActivity.putExtra("price", price)
                                newActivity.putExtra("quantity", quantity)
                                newActivity.putExtra("delivered", delivered)
                                newActivity.putExtra("type", type)

                                activity.startActivity(newActivity)
                            }
                            .show()
                    }
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return Holder(
            layoutInflater.inflate(
                R.layout.item_order,
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int = data.size

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.bindView(data[position], activity)
    }
}
