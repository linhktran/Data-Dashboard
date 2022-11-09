import scala.math._

class Calculation {
  def min(list: Array[Double]) : Double = list.min
  def max(list: Array[Double]) : Double = list.max
  def sum(list: Array[Double]) : Double = list.sum
  def average(list: Array[Double]) : Double = this.sum(list) / list.length
  def sd(list: Array[Double]) : Double = sqrt(list.map(_ - average(list)).map(n => n*n).sum)
}
