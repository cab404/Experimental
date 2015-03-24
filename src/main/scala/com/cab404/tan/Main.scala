package com.cab404.tan

import java.io._
import java.text.SimpleDateFormat
import java.util
import java.util.function.BiFunction
import java.util.zip.{GZIPInputStream, GZIPOutputStream}

import com.cab404.libtabun.data.{Comment, Topic}
import com.cab404.libtabun.pages.{BlogPage, TopicPage}
import com.cab404.libtabun.util.TabunJSON
import com.cab404.tan.Libtabun._
import com.cab404.tan.PutDatIntoDaObject.put
import org.json.simple.parser.JSONParser
import org.json.simple.{JSONArray, JSONObject}

import scala.io.StdIn

object Main {

  implicit val Profile = login(StdIn.readLine("Login> "), StdIn.readLine("Password> "))
  val TimeStamp = new SimpleDateFormat("[HH:mm:ss]")
  val DateStamp = new SimpleDateFormat("HH")
  val Blog = "Ponyhawks"

  class UserInfo() {
    var nick: String = null
    var howMuch: util.HashMap[String, Int] = new util.HashMap[String, Int]()
  }


  def main(args: Array[String]) {
    val page = new BlogPage(Blog)

    page.fetch(Profile)

    //    infiniteUpdate(new TopicCommentUpdate(topicId = page.topics.get(0).id, profile = profile), findCalls)

    println("Scanning")

    try
//      recursiveScan()
          fastScan
    catch {
      case a: InterruptedException => println("Finished!")
    }

    println("Analysis")

    val toFile = new BufferedWriter(new FileWriter("data-monthly.csv"))

    /* Writing header */
    data.keySet foreach ((login) => {
      toFile write ','
      toFile write login
    })
    toFile write '\n'

    /* Writing data */
    dates foreach ((date) => {
      toFile write date
      nicks foreach ((nick) => {
        toFile write ','
        val wat = data.get(nick).getOrDefault(date, 0)
        toFile write (if (wat == 0) "" else wat.toString)
      })
      toFile write '\n'
    })

    toFile close()

    //    notVisited foreach ((login) => {
    //      val profile = new ProfilePage(login)
    //      try {
    //        profile.fetch(Profile)
    //
    //        if (null ne profile.user_info) {
    //          val blogs = profile.user_info.get(UserInfoType.BELONGS)
    //          if (null ne blogs)
    //            if (blogs.contains("Ponyhawks"))
    //              println(login)
    //        }
    //      } catch {
    //        case e: MoonlightFail => println("Do %s for yourself!" format login)
    //      }
    //
    //    })

    println("Done!")

  }

  @throws[InterruptedException]
  def recursiveScan(pg_num: Int = 1): Unit = {
    if (scanBlogPage(pg_num) != -1)
      recursiveScan(pg_num + 1)
  }

  def fastScan: Unit = {
    val ids = new File("topics") list() map (a => Integer.parseInt(a.substring(0, a indexOf '.'))) toList

    ids sortWith ((a, b) => a > b) foreach (id => scanTopic({
      val t = new Topic
      t.id = id
      t
    }))
  }

  @throws[InterruptedException]
  def scanBlogPage(page_num: Int = 1): Int = {
    val loaded = page(Blog, page_num)
    printf("Страница #%d\n", page_num)

    loaded foreach scanTopic

    if (loaded.topics.size() > 0)
      loaded.page + 1
    else
      -1
  }

  @throws[InterruptedException]
  def scanTopic(topic: Topic): Unit = {
    println(topic.id)

    val file = new File("topics/%d.json.gz" format topic.id)
    file.getParentFile.mkdirs()

    (if (file.exists) {
      val readFrom = new BufferedReader(new InputStreamReader(new GZIPInputStream(new FileInputStream(file)), "UTF-8"))
      val data = new JSONParser().parse(readFrom).asInstanceOf[JSONObject]
      val page = new TopicPage(topic.id)

      page.header = TabunJSON.parseTopic(data.get("header").asInstanceOf[JSONObject])
      data.get("comments").asInstanceOf[java.util.List[JSONObject]].foreach(a => page.comments.add(TabunJSON.parseComment(a)))

      page
    } else {

      val loaded = post(topic)

      val whereToPut = new BufferedOutputStream(new GZIPOutputStream(new FileOutputStream(file)))
      val data = new JSONObject

      PutDatIntoDaObject.put(data, "header", loaded.header.toJSON)

      val comments = new JSONArray
      loaded.comments foreach (a => put(comments, a.toJSON))

      put(data, "comments", comments)

      whereToPut.write(data.toJSONString.getBytes("UTF-8"))

      whereToPut.close()

      loaded
    }).comments foreach scanComment

  }

  //  val visited: util.Set[String] = new util.HashSet[String]()
  //  val notVisited: util.Set[String] = new util.HashSet[String]()

  val data = new util.HashMap[String, util.HashMap[String, Int]]()
  val dates = new util.HashSet[String]()
  val nicks = new util.HashSet[String]()

  val sum = new BiFunction[Int, Int, Int] {
    override def apply(t: Int, u: Int): Int = t + u
  }

  def scanComment(comment: Comment): Unit = {
    if (comment.deleted) return
    val nick = comment.author.login
    val date = DateStamp format comment.date.getTime

    dates add date
    nicks add nick

    val userdata = {
      val ifNull = data get nick
      if (ifNull == null) {
        println(nick)
        val map = new util.HashMap[String, Int]()
        data.put(nick, map)
        map
      } else
        ifNull
    }

    userdata merge(date, 1, sum)

  }

  //  def scanTopic(topic: Topic): Unit = {
  //
  //  }
  //
  //  def infiniteUpdate(topicCommentUpdate: TopicCommentUpdate, process: TopicCommentUpdate => Unit): Unit = {
  //    process(topicCommentUpdate)
  //    Thread sleep 5000
  //    infiniteUpdate(topicCommentUpdate.next, process)
  //  }


  //  def findCalls(what: TopicCommentUpdate): Unit = {
  //    what.list foreach ((c: Comment) => printf("%1$s %2$s : %3$s\n", TimeStamp.format(c.date.getTime), c.author.login, c.text))
  //  }

}