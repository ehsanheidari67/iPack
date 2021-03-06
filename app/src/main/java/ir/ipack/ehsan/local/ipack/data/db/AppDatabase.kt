package ir.ipack.ehsan.local.ipack.data.db

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import ir.ipack.ehsan.local.ipack.data.db.dao.BasePlanDao
import ir.ipack.ehsan.local.ipack.data.db.dao.CycleDao
import ir.ipack.ehsan.local.ipack.data.db.dao.OfferDao
import ir.ipack.ehsan.local.ipack.data.db.dao.UsageDao
import ir.ipack.ehsan.local.ipack.data.db.entity.BasePlanEntity
import ir.ipack.ehsan.local.ipack.data.db.entity.CycleEntity
import ir.ipack.ehsan.local.ipack.data.db.entity.OfferEntity
import ir.ipack.ehsan.local.ipack.data.db.entity.UsageEntity
import ir.ipack.ehsan.local.ipack.data.source.local.DataPersistence
import ir.ipack.ehsan.local.ipack.utils.CycleTypeEnum
import ir.ipack.ehsan.local.ipack.utils.PlanConstants
import ir.ipack.ehsan.local.ipack.utils.UnitEnum
import java.util.concurrent.Executors

@Database(
    entities = [BasePlanEntity::class, CycleEntity::class, OfferEntity::class, UsageEntity::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase(), DataPersistence {
    abstract fun basePlanDao(): BasePlanDao
    abstract fun cycleDao(): CycleDao
    abstract fun offerDao(): OfferDao
    abstract fun usageDao(): UsageDao

    companion object {

        private const val DATABASE_NAME = "AppDatabase.db"

        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: buildDatabase(context).also { INSTANCE = it }
            }

        private fun buildDatabase(context: Context) =
            Room.databaseBuilder(
                context.applicationContext,
                AppDatabase::class.java, DATABASE_NAME
            ).addCallback(object : RoomDatabase.Callback() {
                override fun onCreate(db: SupportSQLiteDatabase) {
                    super.onCreate(db)
                    Executors.newSingleThreadExecutor().execute {
                        getInstance(context).apply {
                            initialDb(this)
                        }
                    }
                }
            }).allowMainThreadQueries()
                .build()

        private fun initialDb(db: AppDatabase) {
            db.basePlanDao().insertBasePlan(
                BasePlanEntity(
                    baseCost = PlanConstants.INITIAL_BASE_COST,
                    addonCost = PlanConstants.INITIAL_ADDON_COST
                )
            )
            db.usageDao().insert(
                UsageEntity(
                    total = 200, isUnlimited = true, type = CycleTypeEnum.TALK, incoming = 29, outgoing = 96
                )
            )
            db.usageDao().insert(
                UsageEntity(
                    total = 350, isUnlimited = false, type = CycleTypeEnum.TEXT, incoming = 186, outgoing = 164
                )
            )
            db.usageDao().insert(
                UsageEntity(
                    appName = "Facebook", imageName = "social_dark_gray", type = CycleTypeEnum.INTERNET, used = 0.75,
                    limit = 2, isUnlimited = false
                )
            )
            db.usageDao().insert(
                UsageEntity(
                    appName = "YouTube", imageName = "video_dark_gray", type = CycleTypeEnum.INTERNET, used = 0.3,
                    limit = 2, isUnlimited = false
                )
            )
            db.usageDao().insert(
                UsageEntity(
                    appName = "WhatsApp", imageName = "social_dark_gray", type = CycleTypeEnum.INTERNET, used = 0.2,
                    limit = 2, isUnlimited = false
                )
            )

            db.cycleDao().insert(
                CycleEntity(
                    CycleTypeEnum.INTERNET, UnitEnum.GB, PlanConstants.INITIAL_USED_DATA.toDouble(),
                    PlanConstants.INITIAL_DATA_AMOUNT
                )
            )
            db.cycleDao().insert(
                CycleEntity(
                    CycleTypeEnum.TALK, UnitEnum.MIN, PlanConstants.INITIAL_USED_TALK.toDouble(),
                    PlanConstants.INITIAL_TALK_AMOUNT
                )
            )
            db.cycleDao().insert(
                CycleEntity(
                    CycleTypeEnum.TEXT, UnitEnum.SMS, PlanConstants.INITIAL_USED_TEXT.toDouble(),
                    PlanConstants.INITIAL_TEXT_AMOUNT
                )
            )
        }
    }

    override fun getUsageByTypeLive(cycleTypeEnum: CycleTypeEnum): LiveData<List<UsageEntity>> =
        usageDao().getByTypeLive(cycleTypeEnum)

    override fun getCycleByTypeLive(cycleTypeEnum: CycleTypeEnum): LiveData<List<CycleEntity>> =
        cycleDao().getCycleByType(cycleTypeEnum)

    override fun setCycle(cycle: CycleEntity) {
        cycleDao().insert(cycle)
    }

    override fun getBasePlan(): LiveData<List<BasePlanEntity>> = basePlanDao().getBasePlanLive()

    override fun setBasePlan(basePlanEntity: BasePlanEntity) {
        basePlanDao().insertBasePlan(basePlanEntity)
    }

    override fun updateBasePlan(changeAmount: Int) {
        basePlanDao().updateBasePlan(changeAmount)
    }
}