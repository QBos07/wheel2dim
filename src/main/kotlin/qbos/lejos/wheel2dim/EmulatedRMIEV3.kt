package qbos.lejos.wheel2dim

import lejos.hardware.ev3.LocalEV3
import lejos.hardware.lcd.Font
import lejos.hardware.motor.EV3LargeRegulatedMotor
import lejos.hardware.motor.EV3MediumRegulatedMotor
import lejos.hardware.motor.MindsensorsGlideWheelMRegulatedMotor
import lejos.hardware.motor.NXTRegulatedMotor
import lejos.remote.ev3.*
import java.rmi.server.UnicastRemoteObject

class EmulatedRMIEV3: RMIEV3, UnicastRemoteObject() {
    private val regulatedMotors = HashMap<String, EmulatedRMIRegulatedMotor>()
    val secondary by lazy { regulatedMotors[config.second] }
    val first by lazy { regulatedMotors[config.first] }

    internal companion object {
        private const val serialVersionUID = 8465235463238546334L
    }
    override fun openAnalogPort(p0: String?): RMIAnalogPort {
        TODO("Not yet implemented")
    }

    override fun openI2CPort(p0: String?): RMII2CPort {
        TODO("Not yet implemented")
    }

    override fun getBattery(): RMIBattery = object: RMIRemoteBattery() {}

    override fun openUARTPort(p0: String?): RMIUARTPort {
        TODO("Not yet implemented")
    }

    override fun openMotorPort(p0: String?): RMIMotorPort {
        TODO("Not yet implemented")
    }

    override fun createSampleProvider(p0: String?, p1: String?, p2: String?): RMISampleProvider {
        TODO("Not yet implemented")
    }

    override fun createRegulatedMotor(port: String, type: Char): RMIRegulatedMotor {
        return (regulatedMotors.getOrPut(port) {
            if (remote == null) {
                when (type) {
                    'N' -> NXTRegulatedMotor::class
                    'L' -> EV3LargeRegulatedMotor::class
                    'M' -> EV3MediumRegulatedMotor::class
                    'G' -> MindsensorsGlideWheelMRegulatedMotor::class
                    else -> throw IllegalArgumentException("unknown type: $type")
                }.run { constructors.first() }.let { mc ->
                    if (config.first != port && config.second != port && config.third != port) throw IllegalArgumentException(
                        "no such registered port: $port"
                    )
                    EmulatedRMIRegulatedMotor(BaseRegulatedMotor2RMIRegulatedMotor(mc.call(LocalEV3.get().getPort(port))))
                }
            } else {
                EmulatedRMIRegulatedMotor(remote!!.first.createRegulatedMotor(port, type))
            }
        })
    }

    override fun getAudio(): RMIAudio {
        TODO("Not yet implemented")
    }

    override fun getTextLCD(): RMITextLCD {
        TODO("Not yet implemented")
    }

    override fun getTextLCD(p0: Font?): RMITextLCD {
        TODO("Not yet implemented")
    }

    override fun getGraphicsLCD(): RMIGraphicsLCD {
        TODO("Not yet implemented")
    }

    override fun getWifi(): RMIWifi {
        TODO("Not yet implemented")
    }

    override fun getBluetooth(): RMIBluetooth {
        TODO("Not yet implemented")
    }

    override fun getName(): String = if (remote == null) LocalEV3.get().name else remote!!.first.name

    override fun getKey(p0: String?): RMIKey {
        TODO("Not yet implemented")
    }

    override fun getLED(): RMILED {
        TODO("Not yet implemented")
    }

    override fun getKeys(): RMIKeys {
        TODO("Not yet implemented")
    }

    init {
        Runtime.getRuntime().addShutdownHook(Thread { regulatedMotors.forEach { it.value.close(); regulatedMotors.remove(it.key) } })
    }
}
