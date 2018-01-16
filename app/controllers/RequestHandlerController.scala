/*
 * Copyright 2018 HM Revenue & Customs
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package controllers

import javax.inject.{Inject, Singleton}

import models.HttpMethod._
import play.api.mvc.{Action, AnyContent}
import repositories.DataRepository
import uk.gov.hmrc.play.bootstrap.controller.BaseController

import scala.concurrent.ExecutionContext.Implicits.global

@Singleton
class RequestHandlerController @Inject()(dataRepository: DataRepository) extends BaseController {

  def getRequestHandler(url: String): Action[AnyContent] = Action.async {
    implicit request => {
      dataRepository().find("_id" -> s"""${request.uri}""", "method" -> GET).map {
        case head :: _ if head.response.nonEmpty => Status(head.status)(head.response.get) //return status and body
        case head :: _ => Status(head.status) //Only return status, no body.
        case _ => BadRequest(s"Could not find endpoint in Dynamic Stub matching the URI: ${request.uri}")
      }
    }
  }

  def postRequestHandler(url: String): Action[AnyContent] = Action.async {
    implicit request => {
      dataRepository().find("_id" -> s"""${request.uri}""", "method" -> POST).map {
        case head :: _ if head.response.nonEmpty => Status(head.status)(head.response.get) //return status and body
        case head :: _ => Status(head.status) //Only return status, no body.
        case _ => BadRequest(s"Could not find endpoint in Dynamic Stub matching the URI: ${request.uri}")
      }
    }
  }
}
