package net.subroh0508.colormaster.db

interface IdolColorsDatabase {
    companion object {
        internal const val FAVORITE_DB = "FAVORITE_DB"
        internal const val FAVORITE_KEY = "FAVORITE"
    }

    suspend fun getFavorites(): Set<String>
    suspend fun addFavorite(id: String)
    suspend fun removeFavorite(id: String)
}
