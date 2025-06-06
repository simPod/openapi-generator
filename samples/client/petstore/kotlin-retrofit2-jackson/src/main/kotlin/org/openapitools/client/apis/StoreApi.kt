package org.openapitools.client.apis

import org.openapitools.client.infrastructure.CollectionFormats.*
import retrofit2.http.*
import retrofit2.Call
import okhttp3.RequestBody
import com.fasterxml.jackson.annotation.JsonProperty

import org.openapitools.client.models.Order

interface StoreApi {
    /**
     * DELETE store/order/{orderId}
     * Delete purchase order by ID
     * For valid response try integer IDs with value &lt; 1000. Anything above 1000 or nonintegers will generate API errors
     * Responses:
     *  - 400: Invalid ID supplied
     *  - 404: Order not found
     *
     * @param orderId ID of the order that needs to be deleted
     * @return [Call]<[Unit]>
     */
    @DELETE("store/order/{orderId}")
    fun deleteOrder(@Path("orderId") orderId: kotlin.String): Call<Unit>

    /**
     * GET store/inventory
     * Returns pet inventories by status
     * Returns a map of status codes to quantities
     * Responses:
     *  - 200: successful operation
     *
     * @return [Call]<[kotlin.collections.Map<kotlin.String, kotlin.Int>]>
     */
    @GET("store/inventory")
    fun getInventory(): Call<kotlin.collections.Map<kotlin.String, kotlin.Int>>

    /**
     * GET store/order/{orderId}
     * Find purchase order by ID
     * For valid response try integer IDs with value &lt;&#x3D; 5 or &gt; 10. Other values will generate exceptions
     * Responses:
     *  - 200: successful operation
     *  - 400: Invalid ID supplied
     *  - 404: Order not found
     *
     * @param orderId ID of pet that needs to be fetched
     * @return [Call]<[Order]>
     */
    @GET("store/order/{orderId}")
    fun getOrderById(@Path("orderId") orderId: kotlin.Long): Call<Order>

    /**
     * POST store/order
     * Place an order for a pet
     * 
     * Responses:
     *  - 200: successful operation
     *  - 400: Invalid Order
     *
     * @param order order placed for purchasing the pet
     * @return [Call]<[Order]>
     */
    @POST("store/order")
    fun placeOrder(@Body order: Order): Call<Order>

}
