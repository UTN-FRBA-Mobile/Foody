package ar.edu.utn.frba.foody.ui.Classes

data class Group (
    val groupId: Int = 0,
    val name: String = "Los Movileros",
    var members: List<User> = listOf(
        User(
            userId = 0,
            userName = "Zeke",
            userPassword = "SeQueL"
        ),
        User(
            userId = 1,
            userName = "Walter",
            userPassword = "1234"
        ),
    ),
    val limit: Int = 10,
)