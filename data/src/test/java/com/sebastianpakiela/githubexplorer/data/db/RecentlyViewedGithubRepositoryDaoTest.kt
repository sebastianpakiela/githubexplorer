package com.sebastianpakiela.githubexplorer.data.db

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.*
import androidx.test.platform.app.InstrumentationRegistry
import com.sebastianpakiela.githubexplorer.data.entity.db.RecentlyViewedRepositoryEntity
import com.sebastianpakiela.githubexplorer.data.rule.RxImmediateSchedulerRule
import com.sebastianpakiela.githubexplorer.data.testdata.TestData
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.schedulers.Schedulers
import org.hamcrest.CoreMatchers
import org.hamcrest.MatcherAssert
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import java.io.IOException


@RunWith(RobolectricTestRunner::class)
class RecentlyViewedGithubRepositoryDaoTest {

    @Rule
    @JvmField
    var testSchedulerRule = RxImmediateSchedulerRule()

    @Rule
    @JvmField
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    lateinit var db: AppDatabase
    lateinit var recentlyViewedRepositoriesDao: RecentlyViewedRepositoryDao

    @Before
    fun setup() {
        db = Room.inMemoryDatabaseBuilder(
            InstrumentationRegistry.getInstrumentation().context,
            AppDatabase::class.java
        ).allowMainThreadQueries()
            .build()

        recentlyViewedRepositoriesDao = db.recentlyViewedRepositoriesDao()
    }

    @Test
    fun `Should  return inserted data`() {
        recentlyViewedRepositoriesDao.putRepository((TestData.repoEntity)).test()

        val testObserver = recentlyViewedRepositoriesDao.getAll().test()

        testObserver.assertValueCount(1)
        MatcherAssert.assertThat(testObserver.values().first().size, CoreMatchers.equalTo(1))
        MatcherAssert.assertThat(
            testObserver.values().first(),
            CoreMatchers.equalTo(listOf(TestData.repoEntity))
        )
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }
}