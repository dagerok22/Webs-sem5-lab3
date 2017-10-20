object ClientMain{
    @JvmStatic
    fun main(args: Array<String>) {
        val server = Client()
        server.start()
    }
}