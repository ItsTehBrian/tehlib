rootProject.name = "tehlib"

projects("core", "paper")

fun projects(vararg names: String) {
    include(*names)

    names.forEach {
        project(":$it").name = "tehlib-$it"
    }
}
