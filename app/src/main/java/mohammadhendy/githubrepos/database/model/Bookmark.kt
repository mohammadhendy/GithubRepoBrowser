package mohammadhendy.githubrepos.database.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Bookmark(
    @ColumnInfo(name = "repo_id")
    @PrimaryKey
    val repoId: Int,
    @ColumnInfo(name = "is_bookmarked")
    val isBookmarked: Boolean
)