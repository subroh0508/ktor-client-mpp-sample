package net.subroh0508.colormaster.query

import net.subroh0508.colormaster.model.LiveName
import net.subroh0508.colormaster.query.internal.ESCAPED_ENDPOINT_RDFS_DETAIL

class SearchByLiveQuery(
    lang: String,
    name: LiveName?,
) : ImasparqlQuery() {
    override val rawQuery = """
        SELECT ?id ?name ?color WHERE {
          ?live rdf:type imas:Live;
            schema:name ?liveName;
            schema:actor ?actor.
          ${name?.value?.let { "FILTER (str(?liveName) = '$it')." } ?: ""}
          ?s imas:Color ?color;
            imas:Title ?title;
            imas:cv ?actor.
          OPTIONAL { ?s schema:name ?realName. FILTER(lang(?realName) = '$lang') }
          OPTIONAL { ?s schema:alternateName ?altName. FILTER(lang(?altName) = '$lang') }  
          BIND (COALESCE(?altName, ?realName) as ?name)
          FILTER (str(?title) != '1st Vision').
          BIND (REPLACE(str(?s), '${ESCAPED_ENDPOINT_RDFS_DETAIL}', '') as ?id).
        }
        ORDER BY ?name
    """.trimIndentAndBr()
}
