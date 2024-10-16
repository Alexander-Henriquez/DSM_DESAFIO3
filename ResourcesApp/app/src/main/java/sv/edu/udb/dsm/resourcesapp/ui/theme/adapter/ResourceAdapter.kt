package sv.edu.udb.dsm.resourcesapp.ui.theme.adapter

import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import sv.edu.udb.dsm.resourcesapp.R
import sv.edu.udb.dsm.resourcesapp.ui.theme.model.Resource

class ResourceAdapter(
    private var resourceList: List<Resource>,
    private val onEditClick: (Int) -> Unit, // Callback para editar
    private val onDeleteClick: (Int) -> Unit // Callback para eliminar
) : RecyclerView.Adapter<ResourceAdapter.ResourceViewHolder>() {

    // ViewHolder para representar cada elemento en el RecyclerView
    class ResourceViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val resourceName: TextView = itemView.findViewById(R.id.resource_name)
        val resourceDescription: TextView = itemView.findViewById(R.id.resource_description)
        val resourceLink: TextView = itemView.findViewById(R.id.resource_link)
        val resourceType: TextView = itemView.findViewById(R.id.resource_type)
        val editButton: Button = itemView.findViewById(R.id.btnEdit)
        val deleteButton: Button = itemView.findViewById(R.id.btnDelete)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ResourceViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_resource, parent, false)
        return ResourceViewHolder(view)
    }

    override fun onBindViewHolder(holder: ResourceViewHolder, position: Int) {
        val resource = resourceList[position]
        holder.resourceName.text = resource.title
        holder.resourceDescription.text = resource.description
        holder.resourceLink.text = resource.link
        holder.resourceType.text = resource.type

        // Asignar acción de clic para el enlace
        holder.resourceLink.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(resource.link))
            holder.itemView.context.startActivity(intent)
        }

        // Asignar acciones de clic para editar y eliminar
        holder.editButton.setOnClickListener {
            onEditClick(resource.id) // Llama al callback de edición
        }

        holder.deleteButton.setOnClickListener {
            onDeleteClick(resource.id) // Llama al callback de eliminación
        }
    }

    override fun getItemCount(): Int {
        return resourceList.size
    }

    // Método para actualizar la lista de recursos en el adaptador
    fun updateResources(newResources: List<Resource>) {
        resourceList = newResources
        notifyDataSetChanged()
    }
}
