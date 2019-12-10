package apostaremczak.advent_of_code.intcode

case class OpCode(operation: Operation, parameterModes: List[ParameterMode])

object OpCode {
  /**
   * The rightmost two digits of the code indicate the type of
   * operation to be executed. Then, going right to left,
   * it stores parameter modes of up to three arguments.
   */
  def apply(code: Int): OpCode = {
    val params = f"$code%05d".map(_.asDigit).toList

    params match {
      case paramMode3 :: paramMode2 :: paramMode1 :: operationCode =>
        operationCode match {
          case _ :: 1 :: Nil =>
            OpCode(Sum, List(paramMode1, paramMode2, Immediate))
          case _ :: 2 :: Nil =>
            OpCode(Multiplication, List(paramMode1, paramMode2, Immediate))
          case _ :: 3 :: Nil =>
            OpCode(InputWriter, List(Immediate))
          case _ :: 4 :: Nil =>
            OpCode(OutputUpdater, List(Positional))
          case _ :: 5 :: Nil =>
            OpCode(IfTrueJumper, List(paramMode1, paramMode2))
          case _ :: 6 :: Nil =>
            OpCode(IfFalseJumper,  List(paramMode1, paramMode2))
          case _ :: 7 :: Nil =>
            OpCode(LessThanComparison, List(paramMode1, paramMode2, Immediate))
          case _ :: 8 :: Nil =>
            OpCode(EqualityComparison,  List(paramMode1, paramMode2, Immediate))
          case 9 :: 9 :: Nil =>
            OpCode(Halt, Nil)
          case operationCode =>
            throw new IllegalArgumentException(s"Invalid operation code: $operationCode")
        }
    }
  }
}
