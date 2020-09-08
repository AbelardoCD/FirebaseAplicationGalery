package com.example.aplicationfirebase

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.*
import com.example.aplicationfirebase.Adapters.AdapterMovimientos
import com.example.aplicationfirebase.models.TemasPojo
import com.example.aplicationfirebase.models.User
import com.example.aplicationfirebase.models.userPreferenceManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.activity_home.txtEmailUser
import kotlinx.android.synthetic.main.activity_temas.*

class Temas : AppCompatActivity() {
    lateinit var database: FirebaseDatabase
    lateinit var referencia: DatabaseReference
    lateinit var lista: ListView
    lateinit var listaMovimeintos: MutableList<TemasPojo>
    private lateinit var Auth: FirebaseAuth
    private lateinit var user: FirebaseUser
    lateinit var userPreference: userPreferenceManager
    lateinit var btnCerrarSesion: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_temas)
        Auth = FirebaseAuth.getInstance()
        user = Auth.currentUser!!
        datosUsuarioLogeado(user)
        userPreference = userPreferenceManager(this)
        btnCerrarSesion =findViewById(R.id.btncerrarSesion)
        database = FirebaseDatabase.getInstance()
        referencia = database.getReference("movimiento")
        lista = findViewById(R.id.listMovimientos)
        listaMovimeintos = mutableListOf()
       // guardarTema()
        getMovimientos()
        itemOnclicLista()
        controlBotones()
    }
    private fun datosUsuarioLogeado(userMail: FirebaseUser) {
        println("Desde Usuario log" + userMail)

        database = FirebaseDatabase.getInstance()
        referencia = database.getReference("User")
        val query: Query = referencia.orderByChild("Email").equalTo(userMail.email)

        query.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                println("imprimimos usuario" + snapshot)
                for (dato in snapshot.children) {
                    val user = dato.getValue(User::class.java)
                  println("imprimimos usuario" + user?.Nombre)

                    txtUserName.text = user?.Nombre
                }

            }

        })
    }

    fun getMovimientos() {
        println("Desde obtener movimientos")
        referencia.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    listaMovimeintos.clear()
                    for (datos in snapshot.children) {
                        val movimiento = datos.getValue(TemasPojo::class.java)
                        println("Movimiento  " + movimiento?.Nombre + "    " + movimiento?.url)
                        listaMovimeintos.add(movimiento!!)
                    }
                }
                val adp = AdapterMovimientos(this@Temas, listaMovimeintos)
                lista.adapter = adp
            }


        })
    }

    fun itemOnclicLista() {
        lista.setOnItemClickListener { adapterView, view, i, l ->
            Toast.makeText(this, listaMovimeintos[i].Nombre, Toast.LENGTH_LONG).show()
            val intent = Intent(this, Home::class.java).apply {
                putExtra("idMovimiento", listaMovimeintos[i].idTema)
            }
            startActivity(intent)
        }
    }



    private fun controlBotones() {

        btnMovimiento.setOnClickListener {
            Toast.makeText(this, "Estamos en Movimiento!", Toast.LENGTH_LONG).show()
        }


        btnNuevo.setOnClickListener {
            val intent = Intent(this, form_cargar_nueve_obra::class.java)
            startActivity(intent)
        }

        btnCerrarSesion.setOnClickListener {
            val cerrasrSesion = userPreference.deleteUser()
            val intent = Intent(this, login::class.java)
            startActivity(intent)
        }
    }

    fun guardarTema() {
        println("entramos al metodo")
        val obtenerId = referencia.push().key
        println("ID.... " + obtenerId)

        val tema = TemasPojo(
            obtenerId!!,
            "Impresionismo",
            "El impresionismo es un movimiento artístico1\u200B inicialmente definido para la pintura impresionista, a partir del comentario despectivo de un crítico de arte (Louis Leroy) ante el cuadro Impresión, sol naciente de Claude Monet, generalizable a otros expuestos en el salón de artistas independientes de París entre el 15 de abril y el 15 de mayo de 1874 (un grupo en el que estaban Camille Pissarro, Edgar Degas, Pierre-Auguste Renoir, Paul Cézanne, Alfred Sisley, Berthe Morisot).\n" +
                    "\n" +
                    "Aunque el adjetivo «impresionista» se ha aplicado para etiquetar productos de otras artes, como la música (impresionismo musical —Claude Debussy—) y la literatura (literatura del Impresionismo —hermanos Goncourt—),2\u200B sus particulares rasgos definitorios (luz, color, pincelada, plenairismo) lo hacen de muy difícil extensión, incluso para otras artes plásticas como la escultura (Auguste Rodin)3\u200B y la arquitectura;4\u200B de tal modo que suele decirse que el impresionismo en sentido estricto solo puede darse en pintura y quizá en fotografía (pictorialismo) y cine (cine impresionista francés o première avant-garde: Abel Gance, Jean Renoir —hijo del pintor impresionista Auguste Renoir—).5\u200B\n" +
                    "\n" +
                    "El movimiento plástico impresionista se desarrolló a partir de la segunda mitad del siglo XIX en Europa —principalmente en Normandía (Giverny y la costa normanda principalmente)— caracterizado, a grandes rasgos, por el intento de plasmar la luz (la «impresión» visual) y el instante, sin reparar en la identidad de aquello que la proyectaba. Es decir, si sus antecesores pintaban formas con identidad, los impresionistas pintaban el momento de luz, más allá de las formas que subyacen bajo este. Fue clave para el desarrollo del arte posterior, a través del posimpresionismo y las vanguardias (Wikipedia 2020)",
            "https://upload.wikimedia.org/wikipedia/commons/thumb/5/59/Monet_-_Impression%2C_Sunrise.jpg/800px-Monet_-_Impression%2C_Sunrise.jpg"
        )
        println(tema.idTema + "  " + tema.Nombre + "  " + tema.Descripcion)
        referencia.child(tema.idTema).setValue(tema).addOnCompleteListener {
            Toast.makeText(this, "Se guardo el tema ", Toast.LENGTH_LONG).show()
        }
    }

    fun getCurrentUser() {


    }

}