package qbos.lejos.wheel2dim

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName("config")
data class Config(
    val first: String = "B",
    val second: String = "C",
    val turn: Int,
    @SerialName("invert_turn") val invertTurn: Boolean = false,
    val third: String = "A"
    )