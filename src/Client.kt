import java.io.*
import java.net.Socket
import java.util.*
import kotlin.concurrent.thread

class Client {
    @Throws(IOException::class)

    fun start() {
        val input = Scanner(System.`in`)
        var ourMessage: String
        var inMessage: String
        println("Укажите порт сервера")
        val portStr = Integer.parseInt(input.nextLine())
        println("Укажите адрес сервера")
        val adress = input.nextLine()
        val clientSocket = Socket(adress, portStr)
        println("Chat")
        inMessage = ""
        val inputThread = thread(start = true) {
            while (true) {
                val input = BufferedReader(InputStreamReader(clientSocket.getInputStream(), "Cp866"))
                if (clientSocket.isConnected) {
                    inMessage = input.readLine()
                    if (inMessage == "exit") {
                        break
                    }
                    println(inMessage)
                } else {
                    input.close()
                }
            }
        }
        while (inMessage != "exit") {
            val out = PrintWriter(OutputStreamWriter(clientSocket.getOutputStream(), "Cp866"), true)
            ourMessage = input.nextLine()
            out.println("[" + clientSocket.remoteSocketAddress.toString().substring(1) + "]: " + ourMessage)
            if (ourMessage == "exit") {
                println("You are disconnected")
                out.close()
                inputThread.interrupt()
                clientSocket.close()
            }
        }
        println("Клиентский сокет закрыт.")
    }
}