package sv.edu.udb.dsm.resourcesapp.ui.theme.api

import retrofit2.http.*  // Asegúrate de importar retrofit2.http.*
import sv.edu.udb.dsm.resourcesapp.ui.theme.model.Resource

interface ApiService {

    // Obtener todos los recursos
    @GET("recursos-ingenieria-sistemas")
    suspend fun getResources(): List<Resource>

    // Obtener un recurso por ID
    @GET("recursos-ingenieria-sistemas/{id}")
    suspend fun getResourceById(@Path("id") id: String): Resource // Cambia a Int

    // Crear un nuevo recurso
    @POST("recursos-ingenieria-sistemas")
    suspend fun createResource(@Body resource: Resource): Resource

    // Modificar un recurso existente
    @PUT("recursos-ingenieria-sistemas/{id}")
    suspend fun updateResource(@Path("id") id: String, @Body resource: Resource): Resource // Cambia a Int

    // Eliminar un recurso
    @DELETE("recursos-ingenieria-sistemas/{id}")
    suspend fun deleteResource(@Path("id") id: Int) // Cambia a Int

    @PUT("recursos-ingenieria-sistemas/{id}")
    suspend fun updateResource(@Path("id") id: Int, @Body resource: Resource): Resource

    interface ApiService {
        @GET("resources/{id}")
        suspend fun getResourceById(@Path("id") id: Int): Resource
    }


}
