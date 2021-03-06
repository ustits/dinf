package dinf

import dinf.adapters.DBDiceFactory
import dinf.adapters.DBSearchIndexRepository
import dinf.adapters.DBDiceRepository
import dinf.adapters.FailoverSearchIndexRepository
import dinf.adapters.HashidsPublicIDFactory
import dinf.adapters.MeiliSearchIndexRepository
import dinf.config.Configuration
import dinf.config.URL
import dinf.domain.DiceMetricRepository
import dinf.domain.DiceFactory
import dinf.domain.DiceRepository
import dinf.domain.DiceService
import dinf.domain.PublicIDFactory
import dinf.domain.SearchIndexRepository
import org.hashids.Hashids

class AppDepsImpl(private val meiliDeps: MeiliDeps, private val cfg: Configuration) : AppDeps {

    private val diceMetricRepository = DiceMetricRepository.InMemory()

    override fun diceRepository(): DiceRepository {
        return DBDiceRepository()
    }

    override fun diceMetricRepository(): DiceMetricRepository {
        return diceMetricRepository
    }

    override fun diceFactory(): DiceFactory {
        return DBDiceFactory()
    }

    override fun publicIDFactory(): PublicIDFactory {
        return HashidsPublicIDFactory(
            shareHashids = hashids(cfg.urls.share),
            editHashids = hashids(cfg.urls.edit)
        )
    }

    override fun searchIndexRepository(): SearchIndexRepository {
        return FailoverSearchIndexRepository(
            main = MeiliSearchIndexRepository(meiliDeps.meiliDiceIndex()),
            fallback = DBSearchIndexRepository()
        )
    }

    override fun diceService(): DiceService {
        return DiceService.Logging(
            DiceService.Impl(
                diceFactory = diceFactory(),
                diceRepository = diceRepository(),
                searchIndexRepository = searchIndexRepository(),
                publicIDFactory = publicIDFactory(),
                diceMetricRepository = diceMetricRepository()
            )
        )
    }

    private fun hashids(url: URL): Hashids {
        return Hashids(url.salt, url.length)
    }
}
