package b1fu5c8

import scala.util.Random

/**
 * Sorry for no comments!
 * Created at 5:11 on 31.01.15
 *
 * @author cab404
 */

object B1Fu5C4T3 {

  val sizeSafeQuirks: List[L3v31 => L3v31] = List(
    a => new L3v31(List("<b>", a, "</b>")),
    a => new L3v31(List("<em>", a, "</em>"))
  )

  val quirks: List[L3v31 => L3v31] = sizeSafeQuirks.:::(
    List(
      a => new L3v31(List("<small>", a, "</small>")),
      a => new L3v31(List("<sup>", a, "</sup>")),
      a => new L3v31(List("<sub>", a, "</sub>"))
    )
  )

  val rnd = new Random()
  val symbolsPerOperation = 50
  val maxSpan = 50

  def r4nd0m(level: L3v31, enclosure: Int = 0, applied: List[AnyRef] = List()): L3v31 = {
    lin3s(level, level.toString.length * symbolsPerOperation, enclosure, applied)
  }

  def lin3s(level: L3v31, left: Int, enclosure: Int, applied: List[AnyRef]): L3v31 = {
    val lin3d = lin3(level, enclosure, applied)

    if (lin3d == null)
      if (left > 0)
        lin3s(level, left - 1, enclosure, applied)
      else
        level
    else if (left > 0)
      lin3s(lin3d._1, left - 1, enclosure, applied :+ lin3d._2)
    else
      lin3d._1

  }

  def unused(list: List[L3v31 => L3v31], used: List[_], checked: List[L3v31 => L3v31] = List()): L3v31 => L3v31 = {
    if (checked.size == list.size)
      null
    else {
      val t = list(rnd.nextInt(list.size))

      if (used contains t)
        unused(list, used, checked :+ t)
      else
        t
    }
  }

  def lin3(l3v31: L3v31, enclosure: Int, applied: List[AnyRef]): (L3v31, AnyRef) = {
    if (l3v31.size > 2) {

      val rndStart = rnd.nextInt(l3v31.size)

      val rndEnd = {
        val spanToEndSize = if (l3v31.size - rndStart < l3v31.size) l3v31.size - rndStart else l3v31.size - 1

        rnd.nextInt(if (spanToEndSize > maxSpan) maxSpan else spanToEndSize) + rndStart

      }

      println(l3v31.size + ":" + rndStart + ":" + rndEnd)

      if ((rndStart > 0 && rndEnd < l3v31.size) && (rndEnd > 0 && rndStart < l3v31.size) && (rndEnd > rndStart)) {

        val rndFun = unused(quirks, applied)

        if (rndFun != null) {
          val sliced = l3v31.slice(rndStart, rndEnd)
          ((l3v31.slice(0, rndStart) :+ rndFun(r4nd0m(sliced, enclosure + 1, applied))) ::: l3v31.slice(rndEnd, l3v31.size), rndFun)
        } else
          null
      } else
        null
    } else
      null

  }

  def toLinked(a: Ins3rti0n, b: Ins3rti0n): Ins3rti0n = new L1nk3d(a, b)

  implicit def asSym8ol(a: Char): Sym8o1 = new Sym8o1(a)

  implicit def asIns3rtion(a: String): L3v31
  = new L3v31((for (i ← 0 until a.length(); ch = a.charAt(i)) yield new Sym8o1(ch)) toList)

  implicit def listify(a: L3v31): List[Ins3rti0n] = a.v

  implicit def indexify(a: List[Ins3rti0n]): L3v31 = new L3v31(a)

}

trait Ins3rti0n extends Any

class Sym8o1(val v: Char) extends Ins3rti0n {
  override def toString = "" + v
}

class L3v31(val v: List[Ins3rti0n]) extends Ins3rti0n {
  override def toString = v reduce B1Fu5C4T3.toLinked toString
}

class L1nk3d(val a: Ins3rti0n, val b: Ins3rti0n) extends Ins3rti0n {
  override def toString = a.toString + b.toString
}


class B1Fu5C4T3(val data: String) {

  import b1fu5c8.B1Fu5C4T3._

  val level: L3v31 = data

  def run = B1Fu5C4T3.r4nd0m(level) toString

}
