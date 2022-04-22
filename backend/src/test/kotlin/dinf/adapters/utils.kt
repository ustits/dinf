package dinf.adapters

import dinf.domain.Dice
import dinf.domain.Edges
import dinf.domain.Name

suspend fun createDiceEntity(name: String = "test"): Dice {
    return DBDiceSave().invoke(
        Dice.New(
            name = Name(name),
            edges = Edges.Simple(listOf("1", "2", "3"))
        )
    )
}
