package sv.edu.udb.dsm.resourcesapp.ui.theme.view

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import sv.edu.udb.dsm.resourcesapp.R
import sv.edu.udb.dsm.resourcesapp.ui.theme.adapter.ResourceAdapter
import sv.edu.udb.dsm.resourcesapp.ui.theme.model.Resource
import sv.edu.udb.dsm.resourcesapp.ui.theme.network.RetrofitInstance

class MainActivity : ComponentActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: ResourceAdapter
    private var resources = mutableListOf<Resource>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        // Inicializa el adaptador con la lista de recursos
        adapter = ResourceAdapter(resources, { resourceId -> editResource(resourceId) }, { resourceId -> deleteResource(resourceId) })
        recyclerView.adapter = adapter

        // Botón para agregar un nuevo recurso
        findViewById<Button>(R.id.btnAddResource).setOnClickListener {
            val intent = Intent(this, AddResourceActivity::class.java)
            startActivity(intent)
        }

        // Configuración del BottomNavigationView
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_navigation)
        bottomNavigationView.setOnNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.navigation_reload -> {
                    loadResources() // Recarga los recursos
                    true
                }
                else -> false
            }
        }

        // Cargar recursos de la API
        loadResources()
    }

    private fun loadResources() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                resources = RetrofitInstance.api.getResources().toMutableList() // Carga los recursos
                withContext(Dispatchers.Main) {
                    adapter.updateResources(resources) // Actualiza la lista en el adaptador
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@MainActivity, "Error al cargar los recursos", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun editResource(resourceId: Int) {
        val intent = Intent(this, AddResourceActivity::class.java).apply {
            putExtra("RESOURCE_ID", resourceId) // Envía el ID del recurso a editar
        }
        startActivity(intent)
    }

    private fun deleteResource(resourceId: Int) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                RetrofitInstance.api.deleteResource(resourceId) // Llama al método de eliminación
                resources.removeAll { it.id == resourceId } // Elimina el recurso de la lista
                withContext(Dispatchers.Main) {
                    adapter.updateResources(resources) // Actualiza la lista en el adaptador
                    Toast.makeText(this@MainActivity, "Recurso eliminado", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@MainActivity, "Error al eliminar el recurso", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}
