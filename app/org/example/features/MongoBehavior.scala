package org.example.features

import com.mongodb.ConnectionString
import org.bson.codecs.configuration.CodecRegistries.{fromProviders, fromRegistries}
import org.bson.codecs.configuration.CodecRegistry
import org.mongodb.scala.bson.codecs.DEFAULT_CODEC_REGISTRY
import org.mongodb.scala.bson.codecs.Macros._
import org.mongodb.scala.bson.{BsonDocument, BsonObjectId}
import org.mongodb.scala.model.Sorts.ascending
import org.mongodb.scala.{MongoClient, MongoCollection, MongoDatabase}
import play.api.Configuration
import play.api.libs.json.{JsError, JsSuccess, JsValue, Json, OFormat}
import play.api.mvc.{Action, AnyContent, Controller}
import play.api.routing.Router
import play.api.routing.sird._

import scala.concurrent.{ExecutionContext, Future}

trait MongoBehavior {
  this: Controller =>

  def prefix: String
  def configuration: Configuration
  implicit def ec: ExecutionContext

  def mongoRoutes: Router.Routes = {
    case GET(p"/mongo/find") =>
      find()
    case POST(p"/mongo/create") =>
      create()
  }

  lazy val registry: CodecRegistry =
    fromRegistries(fromProviders(classOf[Client]), DEFAULT_CODEC_REGISTRY)

  lazy val connection: ConnectionString =
    new ConnectionString(
      configuration
        .getString("mongodb.uri")
        .getOrElse("mongodb://127.0.0.1:27017/instana"))

  lazy val client: MongoClient =
    MongoClient(connection.getConnectionString)

  lazy val database: MongoDatabase =
    client
      .getDatabase(connection.getDatabase)
      .withCodecRegistry(registry)

  lazy val collection: MongoCollection[Client] =
    database.getCollection[Client]("clients")

  def find(): Action[AnyContent] = Action.async { req =>
    collection
      .find(BsonDocument()) // all
      .sort(ascending(req.getQueryString("sort").getOrElse("_id")))
      .skip(req.getQueryString("skip").getOrElse("0").toInt)
      .limit(req.getQueryString("limit").getOrElse("10").toInt)
      .toFuture()
      .map { items => Ok(Json.toJson(items))
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
          }
      case err: JsError =>
        Future.successful(BadRequest(s"Failed to parse json ($err)"))
    }
  }
}

case class Client(name: String, age: Int)
object Client {
  implicit val json: OFormat[Client] = Json.format[Client]
}
