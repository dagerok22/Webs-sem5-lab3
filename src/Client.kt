import java.io.*
import java.net.Socket
import java.util.*
import kotlin.concurrent.thread

class Client {

    fun main(args: Array<String>) {
        run()
    }

    fun run() {
        startTCPClient()
        // Starting client, catching errors
    }

    @Throws(IOException::class)
    private fun startTCPClient() {
        // Scanning console
        val input = Scanner(System.`in`)
        var ourMessage: String
        var inMessage: String
        val file: File
        // Creating socket with port 2002, setting localhost as a server ip
        println("Укажите порт сервера")
        val portStr = Integer.parseInt(input.nextLine())
        println("Укажите адрес сервера")
        val adress = input.nextLine()
        val clientSocket = Socket(adress, portStr)
        println("Chat")
        inMessage = ""
        thread(start = true) {
            while (true) {
                val input = BufferedReader(InputStreamReader(clientSocket.getInputStream(), "Cp866"))
                // Reading input string, came from the server
                inMessage = input.readLine()
                println(inMessage)
            }
        }
        while (inMessage != "exit") {
            val out = PrintWriter(OutputStreamWriter(clientSocket.getOutputStream(), "Cp866"), true)
//            println("Введите сообщение:")
            // Scanning input message
            ourMessage = input.nextLine()
            // Sending message to the server
            out.println("[" + this.toString() + "]: " + ourMessage)
//            println("Сообщение отправлено")
            if (ourMessage.equals("exit")) {
                clientSocket.close()
            }
        }
        println("Клиентский сокет закрыт.")
    }
}