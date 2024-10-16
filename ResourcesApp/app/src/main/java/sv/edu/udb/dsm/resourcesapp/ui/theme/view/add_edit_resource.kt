package sv.edu.udb.dsm.resourcesapp.ui.theme.view

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.ComponentActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import sv.edu.udb.dsm.resourcesapp.R
import sv.edu.udb.dsm.resourcesapp.ui.theme.api.RetrofitClient
import sv.edu.udb.dsm.resourcesapp.ui.theme.model.Resource
import java.util.UUID

class AddResourceActivity : ComponentActivity() {

    private var resourceId: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_edit_resource)

        val titleEditText = findViewById<EditText>(R.id.etTitle)
        val descriptionEditText = findViewById<EditText>(R.id.etDescription)
        val linkEditText = findViewById<EditText>(R.id.etLink)
        val typeEditText = findViewById<EditText>(R.id.etType)
        val saveButton = findViewById<Button>(R.id.btnSaveResource)

        // Verifica si hay un recurso que editar
        resourceId = intent.getIntExtra("RESOURCE_ID", -1).takeIf { it != -1 }

        resourceId?.let {
            // Si hay un ID, cargamos los datos del recurso para editar
            loadResourceData(it)
        }

        saveButton.setOnClickListener {
            val title = titleEditText.text.toString()
            val description = descriptionEditText.text.toString()
            val link = linkEditText.text.toString()
            val type = typeEditText.text.toString()

            if (resourceId == null) {
                // Crear nuevo recurso
                val newResource = Resource(0, title, description, link, type) // Usando 0 como ID para nuevo recurso
                createResource(newResource)
            } else {
                // Editar recurso existente
                val updatedResource = Resource(resourceId!!, title, description, link, type) // Usando resourceId
                updateResource(updatedResource)
            }
        }
    }

    private fun loadResourceData(id: Int) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val resource = RetrofitClient.api.getResourceById(id.toString())
                withContext(Dispatchers.Main) {
                    resource?.let {
                        // Rellenar los EditText con los datos del recurso
                        findViewById<EditText>(R.id.etTitle).setText(it.title)
                        findViewById<EditText>(R.id.etDescription).setText(it.description)
                        findViewById<EditText>(R.id.etLink).setText(it.link)
                        findViewById<EditText>(R.id.etType).setText(it.type)
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@AddResourceActivity, "Error al cargar recurso", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun createResource(resource: Resource) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                RetrofitClient.api.createResource(resource)
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@AddResourceActivity, "Recurso agregado", Toast.LENGTH_SHORT).show()
                    finish()  // Cierra la actividad después de agregar
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@AddResourceActivity, "Error al agregar recurso", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun updateResource(resource: Resource) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                RetrofitClient.api.updateResource(resource.id.toString(), resource) // Asegúrate de pasar el ID como String
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@AddResourceActivity, "Recurso actualizado", Toast.LENGTH_SHORT).show()
                    finish()  // Cierra la actividad después de actualizar
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@AddResourceActivity, "Error al actualizar recurso", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}
