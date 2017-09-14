package features

import io.instana.sdk.annotated.Instana
import org.bson.codecs.configuration.CodecRegistries.{fromProviders, fromRegistries}
import org.bson.codecs.configuration.CodecRegistry
import org.mongodb.scala.bson.codecs.DEFAULT_CODEC_REGISTRY
import org.mongodb.scala.bson.codecs.Macros._
import org.mongodb.scala.bson.{BsonDocument, BsonObjectId}
import org.mongodb.scala.model.Sorts.ascending
import org.mongodb.scala.{MongoClient, MongoCollection, MongoDatabase}
import play.api.libs.json.{JsError, JsSuccess, JsValue, Json, OFormat}
import play.api.mvc.{Action, AnyContent, Controller}
import play.api.routing.Router
import play.api.routing.sird._
import play.api.{Configuration, Logger}

import scala.concurrent.{ExecutionContext, Future}

/**
  * @author Christoph MEIER (TOP)
  */
trait MongoBehavior {
  this: Controller =>

  def prefix: String
  def configuration: Configuration
  def ec: ExecutionContext
  def mongoRoutes: Router.Routes = {
    case GET(p"/find" ? q_o"trace=${bool(trace)}") if trace.contains(true) =>
      OpenTracingAction.async { implicit request =>
        Logger.debug(s"span -> ${request.span}")
        find()(request)
      }
    case POST(p"/create" ? q_o"trace=${bool(trace)}") if trace.contains(true) =>
      OpenTracingAction.async(parse.json) { implicit request =>
        Logger.debug(s"span -> ${request.span}")
        create()(request)
      }

    case GET(p"/find") =>
      find()
    case POST(p"/create") =>
      create()
  }

  val registry: CodecRegistry =
    fromRegistries(fromProviders(classOf[Client]), DEFAULT_CODEC_REGISTRY)

  val client: MongoClient =
    MongoClient(
      configuration
        .getString("mongodb.uri")
        .getOrElse("mongodb://127.0.0.1:27017"))

  val database: MongoDatabase = client
    .getDatabase(
      configuration
        .getString("mongodb.name")
        .getOrElse("instana"))
    .withCodecRegistry(registry)

  val collection: MongoCollection[Client] = database.getCollection[Client]("clients")

  def find(): Action[AnyContent] = Action.async { req =>
    Instana {
      collection
        .find(BsonDocument()) // all
        .sort(ascending(req.getQueryString("sort").getOrElse("_id")))
        .skip(req.getQueryString("skip").getOrElse("0").toInt)
        .limit(req.getQueryString("limit").getOrElse("10").toInt)
        .toFuture()
        .map { items =>
          Ok(Json.toJson(items))
        }(ec)
    }
  }

  def create(): Action[JsValue] = Action.async(parse.json) { request =>
    request.body.validate[Client] match {
      case JsSuccess(cli, _) =>
        val id = BsonObjectId()
        collection
          .insertOne(cli)
          .head()
          .map { _ =>
            val uid = id.asObjectId().getValue.toHexString
            Created.withHeaders(LOCATION -> s"/$prefix/clients/$uid")
          }(ec)
      case err: JsError =>
        Future.successful(BadRequest(s"Failed to parse json ($err)"))
    }
  }
}

case class Client(name: String, age: Int)
object Client {

  implicit val json: OFormat[Client] = Json.format[Client]
}
