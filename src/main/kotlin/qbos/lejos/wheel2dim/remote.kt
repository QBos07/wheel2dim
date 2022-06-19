package qbos.lejos.wheel2dim

import com.charleskorn.kaml.Yaml
import com.charleskorn.kaml.decodeFromStream
import lejos.remote.ev3.RMIEV3
import lejos.remote.ev3.RMIMenu
import java.io.File
import java.rmi.Naming
import java.rmi.RemoteException
import java.rmi.registry.LocateRegistry
import kotlin.system.exitProcess

var remote: Pair<RMIEV3, RMIMenu>? = null

fun main(vararg args: String) {
    if (args.size > 2 || args.isEmpty()) exitProcess(1)
    System.setProperty("java.rmi.server.hostname", "127.0.0.1")
    //System.getSecurityManager() ?: System.setSecurityManager(SecurityManager()) // deprecated for uselessness
    config = Yaml.default.decodeFromStream(File((if (args.size == 1) "wheel2dim.conf" else args[1])).inputStream())
    remote = Pair(Naming.lookup("//${args[0]}/RemoteEV3") as RMIEV3, Naming.lookup("//${args[0]}/RemoteMenu") as RMIMenu)
    try { LocateRegistry.createRegistry(1099) } catch (e: RemoteException) { println("Could not create RMI-Registry! Probably there's already one. Exception: ${e::class}") }
    Naming.rebind("//localhost/RemoteMenu", EmulatedRMIMenu().also { menu = it })
    Naming.rebind("//localhost/RemoteEV3", EmulatedRMIEV3().also { ev3 = it })
}