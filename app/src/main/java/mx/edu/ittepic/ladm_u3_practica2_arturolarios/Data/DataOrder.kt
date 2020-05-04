package mx.edu.ittepic.ladm_u3_practica2_arturolarios.Data

data class DataOrder(val id : String,
                     val name : String,
                     val address : String,
                     val phone : String,
                     val description_product : String,
                     val price : Float,
                     val quantity : Int,
                     val delivered : Boolean,
                     val type : String)