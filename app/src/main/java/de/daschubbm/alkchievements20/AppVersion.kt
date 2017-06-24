package de.daschubbm.alkchievements20

val APP_VERSION = version(2, 0, 0, 0)

fun version(major: Int, minor: Int, revision: Int, build: Int): Int {
    return build + 100 * (revision + 100 * (minor + 100 * major))
}
