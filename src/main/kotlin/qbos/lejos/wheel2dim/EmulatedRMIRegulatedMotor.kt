package qbos.lejos.wheel2dim

import lejos.remote.ev3.RMIRegulatedMotor
import lejos.robotics.RegulatedMotorListener
import java.rmi.server.UnicastRemoteObject

class EmulatedRMIRegulatedMotor(private val motor: RMIRegulatedMotor): RMIRegulatedMotor, UnicastRemoteObject() {
    private val secondary by lazy { ev3.secondary!! }
    private val first by lazy { ev3.first!! }
    internal companion object {
        private const val serialVersionUID = -6584165138546151631L
    }

    private var tacho = 0

    override fun addListener(listener: RegulatedMotorListener?) {
        TODO("Not yet implemented")
    }

    override fun removeListener(): RegulatedMotorListener {
        TODO("Not yet implemented")
    }

    override fun stop(immediateReturn: Boolean) {
        TODO("Not yet implemented")
    }

    override fun flt(immediateReturn: Boolean) {
        motor.flt(immediateReturn)
        /*when (this) {
            first -> secondary.flt(immediateReturn)
            secondary -> first.flt(immediateReturn)
        }*/
    }

    override fun waitComplete() {
        motor.waitComplete()
        //secondary.waitComplete()
    }

    override fun rotateTo(limitAngle: Int, immediateReturn: Boolean) {
        rotate(limitAngle - tacho, immediateReturn)
    }

    override fun rotate(angle: Int) {
        rotate(angle, false)
    }

    override fun rotateTo(limitAngle: Int) {
        rotateTo(limitAngle, false)
    }

    override fun rotate(angle: Int, immediateReturn: Boolean) {
        tacho += angle
        when (this) {
            first -> {
                motor.rotate(config.turn, true)
                secondary.motorRotate(if (config.invertTurn) config.turn else -config.turn)
                motor.waitComplete()
                motor.rotate(angle, true)
                secondary.motorRotate(angle)
                motor.waitComplete()
                motor.rotate(-config.turn, true)
                secondary.motorRotate(if (config.invertTurn) -config.turn else config.turn)
                motor.waitComplete()
            }
            secondary -> {
                motor.rotate(angle, true)
                first.motorRotate(angle)
                motor.waitComplete()
            }
            else -> motor.rotate(angle, immediateReturn)
        }
    }

    private fun motorRotate(angle: Int, immediateReturn: Boolean = false) = motor.rotate(angle, immediateReturn)

    override fun getLimitAngle(): Int {
        TODO("Not yet implemented")
    }

    override fun setSpeed(speed: Int) {
        motor.speed = speed
    }

    override fun getSpeed() = motor.speed

    override fun getMaxSpeed() = motor.maxSpeed

    override fun isStalled() = motor.isStalled // || secondary?.isStalled ?: false

    override fun setStallThreshold(error: Int, time: Int) {
        TODO("Not yet implemented")
    }

    override fun setAcceleration(acceleration: Int) {
        TODO("Not yet implemented")
    }

    override fun close() = motor.close()

    override fun forward() {
        TODO("Not yet implemented")
    }

    override fun backward() {
        TODO("Not yet implemented")
    }

    override fun resetTachoCount() = motor.resetTachoCount()

    override fun getTachoCount() = tacho

    override fun isMoving() = motor.isMoving // || secondary?.isMoving ?: false
}