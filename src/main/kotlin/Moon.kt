
data class Moon(var name: String, var interval: MoonInterval) {

    private val diameters = listOf<String>("Big, very big", "Big", "Not so big but still big", "Too big")

    val diameter get() = diameters.shuffled().take(1)[0]
}
