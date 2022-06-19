package qbos.lejos.wheel2dim

import lejos.hardware.motor.BaseRegulatedMotor
import lejos.remote.ev3.RMIRegulatedMotor
import lejos.robotics.RegulatedMotorListener

class BaseRegulatedMotor2RMIRegulatedMotor(private val m: BaseRegulatedMotor): RMIRegulatedMotor {
    override fun addListener(listener: RegulatedMotorListener?) = m.addListener(listener)
    override fun removeListener(): RegulatedMotorListener = m.removeListener()
    override fun stop(immediateReturn: Boolean) = m.stop(immediateReturn)
    override fun flt(immediateReturn: Boolean) = m.flt(immediateReturn)
    override fun waitComplete() = m.waitComplete()
    override fun rotate(angle: Int, immediateReturn: Boolean) = m.rotate(angle, immediateReturn)
    override fun rotate(angle: Int) = m.rotate(angle)
    override fun rotateTo(limitAngle: Int) = m.rotateTo(limitAngle)
    override fun rotateTo(limitAngle: Int, immediateReturn: Boolean) = m.rotateTo(limitAngle, immediateReturn)
    override fun getLimitAngle(): Int = m.limitAngle
    override fun setSpeed(speed: Int) = speed.let { m.speed = it }
    override fun getSpeed(): Int = m.speed
    override fun getMaxSpeed(): Float = m.maxSpeed
    override fun isStalled(): Boolean = m.isStalled
    override fun setStallThreshold(error: Int, time: Int) = m.setStallThreshold(error, time)
    override fun setAcceleration(acceleration: Int) = acceleration.let { m.acceleration = it }
    override fun close() = m.close()
    override fun forward() = m.forward()
    override fun backward() = m.backward()
    override fun resetTachoCount() = m.resetTachoCount()
    override fun getTachoCount() = m.tachoCount
    override fun isMoving() = m.isMoving
}