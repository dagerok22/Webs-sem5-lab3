import java.io.IOException
import java.net.ServerSocket


class Server {

    private lateinit var server: ServerSocket
    private var clientList: ArrayList<ClientWorker> = ArrayList()


    public fun listenSocket() {
        try {
            server = ServerSocket(4444)
        } catch (e: IOException) {
            println("Could not listen on port 4444")
            System.exit(-1)
        }

        while (true) {
            val w: ClientWorker
            try {
                //server.accept returns a client connection
                w = ClientWorker(server.accept(), Math.round(Math.random()*10000), object : ClientWorker.ServerPoster{
                    override fun postToServer(id: Long, message: String) {
                        postToAllClients(id, message)
                    }
                })
                clientList.add(w)
                val t = Thread(w)
                t.start()
            } catch (e: IOException) {
                println("Accept failed: 4444")
                System.exit(-1)
            }

        }
    }

    private fun postToAllClients(id:Long, message: String) {
        clientList.forEach { client: ClientWorker ->  if (client.id != id) client.post(message)}
    }
}