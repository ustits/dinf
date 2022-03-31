package dinf.domain

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.collections.shouldContainInOrder

class DiceSearchTest : StringSpec({

    "dices with higher click count must be first" {
        val first = Dice.Stub("first")
        val second = Dice.Stub("second")
        val third = Dice.Stub("third")

        val metrics = DiceMetrics.InMemory(
            first to Metric.Stub(10),
            second to Metric.Stub(3),
            third to Metric.Stub(0)
        )

        val search = DiceSearch.PopularFirst(
            search = DiceSearch.Stub(third, second, first),
            metrics = metrics
        )

        search.invoke("any") shouldContainInOrder listOf(first, second, third)
    }

})
