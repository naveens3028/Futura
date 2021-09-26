package com.upmyranksapp.model
import com.google.gson.annotations.SerializedName
data class GetQRCode(
    val `data`: QRData
)

data class QRData(
    val id: String,
    val qrCodeUrl: String,
    val videoUrl: String
)
