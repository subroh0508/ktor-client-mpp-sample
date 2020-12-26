package net.subroh0508.colormaster.query

import net.subroh0508.colormaster.query.internal.ESCAPED_ENDPOINT_RDFS_DETAIL

class SearchByIdQuery(
    lang: String,
    ids: List<String>,
) : ImasparqlQuery() {
    override val rawQuery = """
        SELECT ?id ?name ?color WHERE {
          ?s imas:Color ?color;
            imas:Title ?title.
          OPTIONAL { ?s schema:name ?realName. FILTER(lang(?realName) = '$lang') }
          OPTIONAL { ?s schema:alternateName ?altName. FILTER(lang(?altName) = '$lang') }  
          BIND (COALESCE(?altName, ?realName) as ?name)
          FILTER (str(?title) != '1st Vision').
          BIND (REPLACE(str(?s), '${ESCAPED_ENDPOINT_RDFS_DETAIL}', '') as ?id).  
          ${ids.takeIf(List<String>::isNotEmpty)?.let { "FILTER (regex(?id, '(${it.joinToString("|")})', 'i'))." }}
        }
    """.trimIndentAndBr()
}