import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.io.PrintWriter
import java.net.Socket

internal class ClientWorker//Constructor
(private val client: Socket, id: Long, serverPoster: ServerPoster) : Runnable {

    interface ServerPoster {
        public fun postToServer(id: Long, message: String)
    }
    public val id = id
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
        }

        try {
            line = input.readLine()
            //Send data back to client
            print(line)
            poster.postToServer(id, line)
            //Append data to text area
        } catch (e: IOException) {
            println("Read failed")
            System.exit(-1)
        }
    }

    public fun post(message: String) {
        output.println(message)
    }
}