import java.io.IOException
import java.net.ServerSocket
import java.net.SocketAddress
import java.util.*


class Server {

    private lateinit var server: ServerSocket
    private var clientList: ArrayList<ClientWorker> = ArrayList()


    fun start() {
        try {
            val input = Scanner(System.`in`)
            println("Введите порт сервера")
            server = ServerSocket(input.nextInt())
        } catch (e: IOException) {
            println("Could not listen on port 4444")
            System.exit(-1)
        }

        while (true) {
            val w: ClientWorker
            try {
                //server.accept returns a client connection
                w = ClientWorker(server.accept(), object : ClientWorker.ServerPoster {
                    override fun postToServer(address: SocketAddress, message: String) {
                        postToAllClients(address, message)
                    }
                }, object : ClientWorker.Exiter {
                    override fun disconnectClient(client: ClientWorker) {
                        clientList.remove(client)
                    }
                })
                postToAllClients(w.clientSocket.remoteSocketAddress, "[new user with ip: " + w.clientSocket.remoteSocketAddress.toString().substring(1) + "]")
                clientList.add(w)
                val t = Thread(w)
                t.start()
            } catch (e: IOException) {
                println("Accept failed: 4444")
                System.exit(-1)
            }

        }
    }

    private fun postToAllClients(address: SocketAddress, message: String) {
        clientList.forEach { client: ClientWorker ->

            if (client.clientSocket.remoteSocketAddress != address) client.post(message)

        }
    }
}