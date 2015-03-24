package com.cab404.tan

import java.util.{Collection => JColl}

import com.cab404.libtabun.data.{Blog, Comment, Topic}
import com.cab404.libtabun.pages.{BlogPage, MainPage, TopicPage}
import com.cab404.libtabun.requests.LoginRequest
import com.cab404.libtabun.util.TabunAccessProfile
import com.cab404.moonlight.util.SU._

import scala.collection.JavaConversions

/**
 *
 * Created at 22:31 on 06.01.15
 *
 * @author cab404
 */
object Libtabun {

  implicit def asList[A](jColl: JColl[A]): List[A] = JavaConversions.collectionAsScalaIterable(jColl) toList

  implicit def strToBlog(from: String): Blog = new Blog(from)

  implicit def blogPageToList(from: BlogPage): List[Topic] = from.topics toList

  implicit def topicToList(from: TopicPage): List[Comment] = from.comments toList

  def login(login: => String, pwd: => String) = {
    val t_acc_profile = new TabunAccessProfile
    val page = new MainPage
    page fetch t_acc_profile

    println(removeAllTags(page.quote))

    val request = new LoginRequest(login, pwd)
    request exec t_acc_profile

    if (request.success())
      t_acc_profile
    else
      null
  }

  def page(blog: Blog, page_num: Int = 0)(implicit profile: TabunAccessProfile): BlogPage = {
    val blogPage = new BlogPage(blog, page_num)
    blogPage.fetch(profile)
    blogPage
  }

  def post(topic: Topic)(implicit profile: TabunAccessProfile): TopicPage = {
    val topicPage = new TopicPage(topic.id)
    topicPage.fetch(profile)
    topicPage
  }

}
