object ServerMain{
    @JvmStatic
    fun main(args: Array<String>) {
        val server = Server()
        server.listenSocket()
    }

}