package apostaremczak.advent_of_code.day_05

object MemoryUtils {
  implicit class Memory(val instructions: IndexedSeq[Int]) {
    def updated(address: Int, value: Int): Memory =
      instructions.updated(address, value)

    def get(index: Int, parameterMode: ParameterMode = Positional): Int =
      parameterMode match {
        case Positional => instructions(instructions(index))
        case Immediate  => instructions(index)
      }
  }
}
