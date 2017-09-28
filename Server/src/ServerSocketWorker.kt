import java.net.ServerSocket
import javax.sound.sampled.AudioSystem

class ServerSocketWorker(private val port:Int) : Thread("socket on port $port") {

    override fun run() {
        ServerSocket(port).takeIf { it.isBound }?.also {
            log("server bound to ${it.localSocketAddress}")
            while (true){
                val clientSocket = it.accept()
                log("client connected on port $port")
                val audioInputStream = AudioSystem.getAudioInputStream(clientSocket.getInputStream().buffered())
                AudioSystem.getClip().run {
                    open(audioInputStream)
                    start()
//                    drain()
                }

            }
        }
    }
}