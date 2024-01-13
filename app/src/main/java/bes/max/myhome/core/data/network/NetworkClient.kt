package bes.max.myhome.core.data.network

import bes.max.myhome.core.data.dto.responses.Response

interface NetworkClient {

    suspend fun doRequest(request: Any): Response

}