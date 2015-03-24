package com.cab404.tan

import com.cab404.libtabun.data.Type
import com.cab404.libtabun.requests.RefreshCommentsRequest
import com.cab404.moonlight.framework.AccessProfile
import Libtabun._

/**
 * Sorry for no comments!
 * Created at 5:23 on 05.01.15
 *
 * @author cab404
 */
class TopicCommentUpdate(val topicId: Int, val last: Int = 0, val profile: AccessProfile) {

  lazy val list = {
    val req = new RefreshCommentsRequest(Type.TOPIC, topicId, last);
    req.exec(profile)
    req.comments
  }

  private lazy val lastCommentId: Int = {
    var max = last
    this.list map (_.id) foreach ((num: Int) => max = if (max > num) max else num)
    max
  }

  lazy val next: TopicCommentUpdate = {
    new TopicCommentUpdate(topicId, lastCommentId, profile)
  }

}
