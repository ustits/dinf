package dinf.exposed

import dinf.backend.DBAuthor
import dinf.domain.Article
import dinf.domain.Content
import dinf.types.*
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID

class ArticleEntity(id: EntityID<Int>) : IntEntity(id) {

    companion object : IntEntityClass<ArticleEntity>(ArticleTable)

    var name by ArticleTable.name
    var description by ArticleTable.description
    var author by UserEntity referencedOn ArticleTable.authorID
    var creation by ArticleTable.creationTime
    var lastUpdate by ArticleTable.lastUpdateTime
    var values by ArticleTable.values

    fun toArticle(): Article {
        return Article(
            id = ArticleID(id.value),
            content = toContent(),
            author = DBAuthor(author)
        )
    }

    fun toContent(): Content {
        return Content(
            title = NBString(name),
            description = description,
            values = Values(values.map { str -> NBString(str) }.toList())
        )
    }

}
