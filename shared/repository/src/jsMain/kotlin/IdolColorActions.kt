import io.ktor.client.HttpClient
import io.ktor.client.engine.js.Js
import io.ktor.client.features.defaultRequest
import io.ktor.client.features.json.JsonFeature
import io.ktor.client.features.json.serializer.KotlinxSerializer
import io.ktor.client.request.accept
import io.ktor.http.ContentType
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import net.subroh0508.ktor.client.mpp.sample.repository.IdolColorsRepository
import net.subroh0508.ktor.client.mpp.sample.valueobject.IdolColor
import kotlin.coroutines.CoroutineContext
import kotlin.js.Promise

@JsName("IdolColorActions")
class IdolColorActions : CoroutineScope {
    override val coroutineContext: CoroutineContext get() = SupervisorJob()

    private val repository = IdolColorsRepository(
        HttpClient(Js) {
            defaultRequest {
                accept(ContentType("application", "sparql-results+json"))
            }
            install(JsonFeature) {
                serializer = KotlinxSerializer(Json.nonstrict)
                acceptContentTypes += ContentType("application", "sparql-results+json")
            }
        }
    )

    @JsName("loadItems")
    fun loadItems(): Promise<Array<IdolColor>> = Promise { resolve, reject ->
        launch {
            runCatching { repository.search().toTypedArray() }
                .onSuccess(resolve)
                .onFailure(reject)
        }
    }
}
