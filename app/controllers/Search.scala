package translator.controllers

import org.elasticsearch.index.query._, FilterBuilders._, QueryBuilders._
import org.elasticsearch.search._, facet._, terms._, sort._, SortBuilders._, builder._
import com.mongodb.casbah.Imports._
import translator._
import translator.models._

object SearchController extends BaseController {

  //def entries(project: String) = SecuredWithProject(project) { implicit ctx =>
    //get("term") map { term =>
      //val ids = Search.indexer.search(query = queryString(term)).hits.hits.toList.map { searchResponse =>
        //new ObjectId(searchResponse.id)
      //}

      //JsonOk(EntryDAO.findAllByProjectAndIds(ctx.project.get, ids).map(_.toMap))
    //} getOrElse JsonOk(List())
  //}
}
