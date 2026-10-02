package com.monu.mobile.feature.realtime

import com.monu.mobile.domain.model.WebSocketState
import com.monu.mobile.domain.model.WebSocketStatus
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import okhttp3.*

class MONUWebSocketEngine {

    private val client =
        OkHttpClient()

    private var socket:
        WebSocket? = null

    private val _status =
        MutableStateFlow(
            WebSocketStatus(
                state =
                    WebSocketState.NOT_CONFIGURED,
                message =
                    "WebSocket URL not configured"
            )
        )

    val status:
        StateFlow<WebSocketStatus> =
            _status.asStateFlow()

    fun connect(
        websocketUrl: String
    ) {

        if (websocketUrl.isBlank()) {

            _status.value =
                WebSocketStatus(
                    state =
                        WebSocketState.NOT_CONFIGURED,
                    message =
                        "WebSocket URL not configured"
                )

            return
        }

        disconnect()

        _status.value =
            WebSocketStatus(
                state =
                    WebSocketState.CONNECTING,
                message =
                    "Connecting..."
            )

        val request =
            Request.Builder()
                .url(websocketUrl)
                .build()

        socket =
            client.newWebSocket(
                request,
                object : WebSocketListener() {

                    override fun onOpen(
                        webSocket: WebSocket,
                        response: Response
                    ) {

                        _status.value =
                            WebSocketStatus(
                                state =
                                    WebSocketState.CONNECTED,
                                message =
                                    "Real WebSocket connected",
                                lastEventAt =
                                    System.currentTimeMillis()
                            )
                    }

                    override fun onMessage(
                        webSocket: WebSocket,
                        text: String
                    ) {

                        _status.value =
                            WebSocketStatus(
                                state =
                                    WebSocketState.CONNECTED,
                                message =
                                    "Event received",
                                lastEventAt =
                                    System.currentTimeMillis()
                            )
                    }

                    override fun onClosing(
                        webSocket: WebSocket,
                        code: Int,
                        reason: String
                    ) {

                        _status.value =
                            WebSocketStatus(
                                state =
                                    WebSocketState.DISCONNECTED,
                                message =
                                    "Server closing: $reason"
                            )
                    }

                    override fun onClosed(
                        webSocket: WebSocket,
                        code: Int,
                        reason: String
                    ) {

                        _status.value =
                            WebSocketStatus(
                                state =
                                    WebSocketState.DISCONNECTED,
                                message =
                                    "WebSocket closed: $reason"
                            )
                    }

                    override fun onFailure(
                        webSocket: WebSocket,
                        throwable: Throwable,
                        response: Response?
                    ) {

                        _status.value =
                            WebSocketStatus(
                                state =
                                    WebSocketState.FAILED,
                                message =
                                    throwable.message
                                        ?: "WebSocket connection failed"
                            )
                    }
                }
            )
    }

    fun send(
        message: String
    ): Boolean {

        return socket?.send(
            message
        ) ?: false
    }

    fun disconnect() {

        socket?.close(
            1000,
            "MONU client disconnect"
        )

        socket = null

        _status.value =
            WebSocketStatus(
                state =
                    WebSocketState.DISCONNECTED,
                message =
                    "WebSocket disconnected"
            )
    }
}
