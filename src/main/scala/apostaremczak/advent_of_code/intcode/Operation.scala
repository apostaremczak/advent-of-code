package apostaremczak.advent_of_code.intcode

sealed trait Operation {
  def fetchInputs(state: State): List[Int] =
    state.opCode.parameterModes.zipWithIndex
      .map {
        case (paramMode: ParameterMode, i: Int) =>
          state.memory.get(state.instructionPointer + i + 1, paramMode)
      }

  def calculate(state: State): State = {
    val inputs = fetchInputs(state)
    execute(state)(inputs)
  }

  protected def execute(state: State)(inputs: List[Int]): State
}

/**
  * Opcode 1 adds together numbers read from two positions
  * and stores the result in a third position.
  */
object Sum extends Operation {
  def execute(state: State)(inputs: List[Int]): State = inputs match {
    case x1 :: x2 :: x3 :: Nil =>
      state.copy(
        state.memory.updated(x3, x1 + x2),
        state.instructionPointer + 4
      )
  }
}

/**
  * Opcode 2 multiplies the numbers read from two positions
  * and stores the result in a third position.
  */
object Multiplication extends Operation {
  def execute(state: State)(inputs: List[Int]): State = inputs match {
    case x1 :: x2 :: x3 :: Nil =>
      state.copy(
        state.memory.updated(x3, x1 * x2),
        state.instructionPointer + 4
      )
  }
}

/**
  * Opcode 3 takes a single integer as input and saves it
  * to the position given by its only parameter.
  */
object InputWriter extends Operation {
  def execute(state: State)(inputs: List[Int]): State = inputs match {
    case x1 :: Nil =>
      state.copy(
        state.memory.updated(x1, state.input),
        state.instructionPointer + 2
      )
  }
}

/**
  * Opcode 4 outputs the value of its only parameter.
  */
object OutputUpdater extends Operation {
  def execute(state: State)(inputs: List[Int]): State = inputs match {
    case x1 :: Nil =>
      state.copy(
        instructionPointer = state.instructionPointer + 2,
        outputs = state.outputs.appended(x1)
      )
  }
}

/**
  * Opcode 5 is jump-if-true: if the first parameter is non-zero,
  * it sets the instruction pointer to the value from
  * the second parameter. Otherwise, it does nothing.
  */
object IfTrueJumper extends Operation {
  def execute(state: State)(inputs: List[Int]): State = inputs match {
    case x1 :: x2 :: Nil =>
      if (x1 != 0)
        state.copy(instructionPointer = x2)
      else
        state.copy(instructionPointer = state.instructionPointer + 3)
  }
}

/**
  * Opcode 6 is jump-if-false: if the first parameter is zero,
  * it sets the instruction pointer to the value from
  * the second parameter. Otherwise, it does nothing.
  */
object IfFalseJumper extends Operation {
  def execute(state: State)(inputs: List[Int]): State = inputs match {
    case x1 :: x2 :: Nil =>
      if (x1 == 0)
        state.copy(instructionPointer = x2)
      else
        state.copy(instructionPointer = state.instructionPointer + 3)
  }
}

/**
  * Opcode 7 is less than: if the first parameter is less than
  * the second parameter, it stores 1 in the position given
  * by the third parameter. Otherwise, it stores 0.
  */
object LessThanComparison extends Operation {
  def execute(state: State)(inputs: List[Int]): State = inputs match {
    case x1 :: x2 :: x3 :: Nil =>
      state.copy(
        state.memory.updated(x3, if (x1 < x2) 1 else 0),
        state.instructionPointer + 4
      )
  }
}

/**
  * Opcode 8 is equals: if the first parameter is equal to
  * the second parameter, it stores 1 in the position given by
  * the third parameter. Otherwise, it stores 0.
  */
object EqualityComparison extends Operation {
  def execute(state: State)(inputs: List[Int]): State = inputs match {
    case x1 :: x2 :: x3 :: Nil =>
      state.copy(
        state.memory.updated(x3, if (x1 == x2) 1 else 0),
        state.instructionPointer + 4
      )
  }
}

/**
  * Opcode 99 means that the program is finished and should immediately halt.
  */
object Halt extends Operation {
  def execute(state: State)(inputs: List[Int]): State = state
}
