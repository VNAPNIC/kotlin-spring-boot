package com.vnapnic.p2p.model

data class WebSocketMessage(
        var from: String? =null,
        var type: String? =null,
        var data: String? =null,
        var candidate: Any? =null,
        var sdp: Any? =null
)