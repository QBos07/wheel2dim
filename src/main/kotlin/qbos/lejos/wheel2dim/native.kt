package qbos.lejos.wheel2dim

import com.charleskorn.kaml.Yaml
import com.charleskorn.kaml.decodeFromStream
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.runBlocking
import lejos.hardware.ev3.LocalEV3
import java.io.File
import java.rmi.Naming
import java.rmi.RemoteException
import java.rmi.registry.LocateRegistry
import kotlin.coroutines.CoroutineContext
import kotlin.system.exitProcess

lateinit var menu: EmulatedRMIMenu
lateinit var ev3: EmulatedRMIEV3
lateinit var config: Config

fun main(vararg args: String) {
    if (args.size > 1) exitProcess(1)
    System.setProperty("java.rmi.server.hostname", "127.0.0.1")
    //System.getSecurityManager() ?: System.setSecurityManager(SecurityManager()) // deprecated for uselessness
    config = Yaml.default.decodeFromStream(File((if (args.isEmpty()) "/home/root/wheel2dim.conf" else args[0])).inputStream())
    runBlocking { File("/proc").listFiles().map { f -> async(Dispatchers.IO) { try { f.name.toUInt().let { p -> if (File(f, "cmdline").readText().contains("EV3Menu.jar")) Runtime.getRuntime().exec("kill $p").waitFor() }  } catch (t: Throwable) { println("error ${t.message} on ${f.path}") } } }.awaitAll() }
    Runtime.getRuntime().addShutdownHook(Thread {Runtime.getRuntime().exec("/home/root/lejos/bin/startmenu")})
    try { LocateRegistry.createRegistry(1099) } catch (e: RemoteException) { println("Could not create RMI-Registry! Probably there's already one. Exception: ${e::class}") }
    Naming.rebind("//localhost/RemoteMenu", EmulatedRMIMenu().also { menu = it })
    Naming.rebind("//localhost/RemoteEV3", EmulatedRMIEV3().also { ev3 = it })
    LocalEV3.get().keys.waitForAnyPress()
    exitProcess(0)
}
