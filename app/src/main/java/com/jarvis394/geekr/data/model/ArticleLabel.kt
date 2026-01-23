import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName
import kotlinx.serialization.json.JsonContentPolymorphicSerializer
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive

object ArticleLabelSerializer :
    JsonContentPolymorphicSerializer<ArticleLabel>(ArticleLabel::class) {
    override fun selectDeserializer(element: JsonElement): DeserializationStrategy<ArticleLabel> {
        val jsonObject = element.jsonObject
        val type = jsonObject["type"]?.jsonPrimitive?.content

        return when (type) {
            "translation" -> ArticleLabel.Translation.serializer()
            else -> ArticleLabel.AnyLabel.serializer()
        }
    }
}

@Serializable(with = ArticleLabelSerializer::class)
sealed class ArticleLabel {
    abstract val title: String
    abstract val type: String

    @Serializable
    @SerialName("translation")
    data class Translation(
        override val title: String,
        override val type: String = "translation",
        val data: TranslationData? = null
    ) : ArticleLabel()

    @Serializable
    data class AnyLabel(
        override val type: String,
        override val title: String,
        val data: JsonElement? = null
    ) : ArticleLabel()
}

@Serializable
data class TranslationData(
    val originalAuthorName: String,
    val originalUrl: String
)