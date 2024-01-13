package bes.max.myhome.core.data.network

import android.content.Context
import bes.max.myhome.cameras.data.dto.AllCamerasRequest
import bes.max.myhome.cameras.data.dto.AllCamerasResponse
import bes.max.myhome.core.data.dto.responses.Response
import bes.max.myhome.doors.data.dto.AllDoorsRequest
import bes.max.myhome.doors.data.dto.AllDoorsResponse
import io.ktor.client.HttpClient
import io.ktor.client.features.ClientRequestException
import io.ktor.client.features.ServerResponseException
import io.ktor.client.request.get
import io.ktor.client.request.url

class KtorNetworkClient(
    private val client: HttpClient,
    private val context: Context
) : NetworkClient {

    override suspend fun doRequest(request: Any): Response {
        if (!ConnectionChecker.isConnected(context)) {
            return Response().apply { resultCode = CODE_NO_INTERNET }
        }

        return when (request) {
            is AllCamerasRequest -> getCameras()
            is AllDoorsRequest -> getDoors()
            else -> Response().apply { resultCode = CODE_WRONG_REQUEST }
        }
    }

    private suspend fun getCameras(): Response {
        return try {
            client.get<AllCamerasResponse> { url(camerasUrl) }.apply { resultCode = CODE_SUCCESS }
        } catch (e: ClientRequestException) {
            Response().apply { resultCode = CODE_WRONG_REQUEST }
        } catch (e: ServerResponseException) {
            Response().apply { resultCode = CODE_SERVER_ERROR }
        } catch (e: Exception) {
            Response().apply { resultCode = CODE_SERVER_ERROR }
        }
    }

    private suspend fun getDoors(): Response {
        return try {
            client.get<AllDoorsResponse> { url(doorsUrl) }.apply { resultCode = CODE_SUCCESS }
        } catch (e: ClientRequestException) {
            Response().apply { resultCode = CODE_WRONG_REQUEST }
        } catch (e: ServerResponseException) {
            Response().apply { resultCode = CODE_SERVER_ERROR }
        } catch (e: Exception) {
            Response().apply { resultCode = CODE_SERVER_ERROR }
        }
    }

    companion object {
        private const val baseUrl = "https://cars.cprogroup.ru/api/rubetek"
        private const val camerasUrl = "$baseUrl/cameras/"
        private const val doorsUrl = "$baseUrl/doors/"
        const val CODE_NO_INTERNET = -1
        const val CODE_SUCCESS = 200
        const val CODE_WRONG_REQUEST = 400
        const val CODE_SERVER_ERROR = 500
    }
}