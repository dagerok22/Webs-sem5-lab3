import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.io.PrintWriter
import java.net.Socket
import java.net.SocketAddress

class ClientWorker(private val client: Socket, serverPoster: ServerPoster, exiter: Exiter) : Runnable {

    interface ServerPoster {
        fun postToServer(address: SocketAddress, message: String)
    }
    interface Exiter{
        fun disconnectClient(client: ClientWorker)
    }

    val clientSocket = client
    val exiter = exiter
    lateinit var input: BufferedReader
    lateinit var output: PrintWriter
    var poster: ServerPoster = serverPoster
    override fun run() {
        var line: String
        try {
            input = BufferedReader(InputStreamReader(client.getInputStream()))
            output = PrintWriter(client.getOutputStream(), true)
        } catch (e: IOException) {
            println("in or out failed")
            System.exit(-1)
            exiter.disconnectClient(this)
        }
        while (true) {
            try {
                line = input.readLine()
                if (line == "exit"){
                    exiter.disconnectClient(this)
                    break
                }
                print(line)
                poster.postToServer(clientSocket.remoteSocketAddress, line)
            } catch (e: IOException) {
                println("Read failed")
                exiter.disconnectClient(this)
                System.exit(-1)
            }
        }
    }

    fun post(message: String) {
        output.println(message)
    }
}