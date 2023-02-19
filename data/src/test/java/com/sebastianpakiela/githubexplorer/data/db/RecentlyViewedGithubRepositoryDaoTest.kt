package com.sebastianpakiela.githubexplorer.data.db

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.*
import androidx.test.platform.app.InstrumentationRegistry
import com.sebastianpakiela.githubexplorer.data.entity.db.RecentlyViewedRepositoryEntity
import com.sebastianpakiela.basetest.rule.TestCoroutineRule
import com.sebastianpakiela.basetest.rule.testCollect
import com.sebastianpakiela.githubexplorer.data.testdata.TestData
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.verifyNoMoreInteractions
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import java.io.IOException

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(RobolectricTestRunner::class)
@Config(sdk = [32])
class RecentlyViewedGithubRepositoryDaoTest {

    @get:Rule
    val testSchedulerRule = TestCoroutineRule()

    @get:Rule
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
    fun `Should  return inserted data`() = runTest {
        recentlyViewedRepositoriesDao.putRepository((TestData.repoEntity))

        val rvrEntityCollector: FlowCollector<List<RecentlyViewedRepositoryEntity>> = mock()
        recentlyViewedRepositoriesDao.getAll().testCollect(testScheduler, rvrEntityCollector)

        verify(rvrEntityCollector).emit(listOf(TestData.repoEntity))
        verifyNoMoreInteractions(rvrEntityCollector)
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }
}