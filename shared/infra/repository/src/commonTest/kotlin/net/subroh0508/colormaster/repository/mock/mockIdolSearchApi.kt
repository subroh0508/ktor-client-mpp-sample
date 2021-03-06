package net.subroh0508.colormaster.repository.mock

import io.ktor.client.engine.mock.*
import io.ktor.http.*
import io.ktor.util.*
import net.subroh0508.colormaster.api.imasparql.query.RandomQuery
import net.subroh0508.colormaster.api.imasparql.query.SearchByIdQuery
import net.subroh0508.colormaster.api.imasparql.query.SearchByLiveQuery
import net.subroh0508.colormaster.api.imasparql.query.SearchByNameQuery
import net.subroh0508.colormaster.model.*
import net.subroh0508.colormaster.test.jsonIdolColor
import net.subroh0508.colormaster.test.resultJson
import net.subroh0508.colormaster.test.mockApi

fun mockRandom(lang: Languages, limit: Int) = mockApi { req ->
    if (req.url.parameters["query"] == RandomQuery(lang.code, limit).plainQuery) {
        return@mockApi respond(toJson(lang.code, getRandomIdols(lang)), headers = headers)
    }

    return@mockApi respondBadRequest()
}

fun mockSearchByName(
    lang: Languages, name: IdolName? = null, brands: Brands? = null, types: Set<Types> = setOf(),
    vararg res: IdolColor,
) = mockApi { req ->
    if (req.url.parameters["query"] == SearchByNameQuery(lang.code, name?.value, brands?.queryStr, types.map(Types::queryStr)).plainQuery) {
        return@mockApi respond(toJson(lang.code, res.toList()), headers = headers)
    }

    return@mockApi respondBadRequest()
}

fun mockSearchByLive(
    lang: Languages, liveName: LiveName?,
    vararg res: IdolColor,
) = mockApi { req ->
    if (req.url.parameters["query"] == SearchByLiveQuery(lang.code, liveName?.value).plainQuery) {
        return@mockApi respond(toJson(lang.code, res.toList()), headers = headers)
    }

    return@mockApi respondBadRequest()
}

fun mockSearchById(
    lang: Languages, ids: List<String>,
    vararg res: IdolColor,
) = mockApi { req ->
    if (req.url.parameters["query"] == SearchByIdQuery(lang.code, ids).plainQuery) {
        return@mockApi respond(toJson(lang.code, res.toList()), headers = headers)
    }

    return@mockApi respondBadRequest()
}

fun getRandomIdols(lang: Languages) = listOf(
    IdolColor("Shimabara_Elena", if (lang == Languages.JAPANESE) "島原エレナ" else "Elena Shimabara", HexColor("9BCE92")),
    IdolColor("Abe_nana", if (lang == Languages.JAPANESE) "安部菜々" else "Nana Abe", HexColor("E64A79")),
    IdolColor("Kohinata_Miho", if (lang == Languages.JAPANESE) "小日向美穂" else "Miho Kohinata", HexColor("C64796")),
    IdolColor("Sakuma_Mayu", if (lang == Languages.JAPANESE) "佐久間まゆ" else "Mayu Sakuma", HexColor("D1197B")),
    IdolColor("Hidaka_Ai", if (lang == Languages.JAPANESE) "日高愛" else "Ai Hidaka", HexColor("E85786")),
    IdolColor("Nakatani_Iku", if (lang == Languages.JAPANESE) "中谷育" else "Iku Nakatani", HexColor("F7E78E")),
    IdolColor("Taiga_Takeru", if (lang == Languages.JAPANESE) "大河タケル" else "Takeru Taiga", HexColor("0E0C9F")),
    IdolColor("Kuzunoha_Amehiko", if (lang == Languages.JAPANESE) "葛之葉雨彦" else "Amehiko Kuzunoha", HexColor("111721")),
    IdolColor("Suou_Momoko", if (lang == Languages.JAPANESE) "周防桃子" else "Momoko Suou", HexColor("EFB864")),
    IdolColor("Ichikawa_Hinana", if (lang == Languages.JAPANESE) "市川雛菜" else "Hinana Ichikawa", HexColor("FFC639")),
)

private fun toJson(lang: String, res: List<IdolColor>) = resultJson {
    res.joinToString(",") { (id, name, hexColor) ->
        jsonIdolColor(lang, id, name, hexColor.value)
    }
}
