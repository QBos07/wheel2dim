package qbos.lejos.wheel2dim

import lejos.hardware.ev3.LocalEV3
import lejos.remote.ev3.Menu
import lejos.remote.ev3.RMIMenu
import java.rmi.server.UnicastRemoteObject
import kotlin.system.exitProcess

class EmulatedRMIMenu: RMIMenu, Menu, UnicastRemoteObject() {
    internal companion object {
        private const val serialVersionUID = 3412385415382143582L
    }
    override fun runProgram(p0: String?) {
        TODO("Not yet implemented")
    }

    override fun debugProgram(p0: String?) {
        TODO("Not yet implemented")
    }

    override fun runSample(p0: String?) {
        TODO("Not yet implemented")
    }

    override fun stopProgram() {
        if (remote == null) exitProcess(0)
    }

    override fun deleteFile(p0: String?): Boolean {
        TODO("Not yet implemented")
    }

    override fun getFileSize(p0: String?): Long {
        TODO("Not yet implemented")
    }

    override fun getProgramNames(): Array<String> {
        TODO("Not yet implemented")
    }

    override fun getSampleNames(): Array<String> {
        TODO("Not yet implemented")
    }

    override fun uploadFile(p0: String?, p1: ByteArray?): Boolean {
        TODO("Not yet implemented")
    }

    override fun fetchFile(p0: String?): ByteArray {
        TODO("Not yet implemented")
    }

    override fun getSetting(p0: String?): String {
        TODO("Not yet implemented")
    }

    override fun setSetting(p0: String?, p1: String?) {
        TODO("Not yet implemented")
    }

    override fun deleteAllPrograms() {} // nothing to do

    override fun getVersion(): String = if (remote == null) "unsupported" else remote!!.second.version

    override fun getMenuVersion(): String = if (remote == null) "unsupported" else remote!!.second.menuVersion

    override fun getName(): String = if (remote == null) LocalEV3.get().name else remote!!.second.name

    override fun setName(n: String?) { throw java.lang.UnsupportedOperationException() }

    override fun configureWifi(p0: String?, p1: String?) {
        TODO("Not yet implemented")
    }

    override fun getExecutingProgramName(): String? = if (remote == null) "unsupported" else remote!!.second.executingProgramName

    override fun shutdown() {
        if (remote == null) Runtime.getRuntime().exec("poweroff")
        exitProcess(0)
    }

    override fun suspend() = remote?.second?.suspend() ?: Unit

    override fun resume() = remote?.second?.resume() ?: Unit
}
