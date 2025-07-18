package com.example.store_product.local_data

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.store_product.data.models.ProductModel
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.flow.first


//defines a preferences-based DataStore instance scoped to the Context
private val Context.dataStore by preferencesDataStore(name = "product_cache")
//key used to store the timestamp of the last successful product update
private val lastUpdateKey = stringPreferencesKey("last_update")

object ProductCache {
    //gson instance used to convert product data to and from JSON
    private val gson = Gson()
    //key used to store the serialized list of products in DataStore
    private val productKey = stringPreferencesKey("product_list")

    //saves the list of products to DataStore as a JSON string
    suspend fun saveProducts(context: Context, products: List<ProductModel>) {
        val json = gson.toJson(products)
        context.dataStore.edit { prefs ->
            prefs[productKey] = json
        }
    }
    //loads the list of products from DataStore and converts it back from JSON
    suspend fun loadProducts(context: Context): List<ProductModel> {
        val prefs = context.dataStore.data.first()
        val json = prefs[productKey] ?: return emptyList()
        return try {
            val type = object : TypeToken<List<ProductModel>>() {}.type
            gson.fromJson(json, type)
        } catch (e: Exception) {
            emptyList()
        }
    }
    //saves the timestamp of the last update to DataStore
    suspend fun saveLastUpdate(context: Context) {
        val now = System.currentTimeMillis()
        context.dataStore.edit { prefs ->
            prefs[lastUpdateKey] = now.toString()
        }
    }
    //checks if the cache has expired based on the stored timestamp
    suspend fun isCacheExpired(context: Context): Boolean {
        val prefs = context.dataStore.data.first()
        val lastUpdate = prefs[lastUpdateKey]?.toLongOrNull() ?: return true
        val oneWeekMillis = 7 * 24 * 60 * 60 * 1000
        return System.currentTimeMillis() - lastUpdate > oneWeekMillis
    }
}