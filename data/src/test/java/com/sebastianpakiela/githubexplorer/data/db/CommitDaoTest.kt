package com.sebastianpakiela.githubexplorer.data.db

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.platform.app.InstrumentationRegistry
import com.sebastianpakiela.githubexplorer.data.entity.db.CommitEntity
import com.sebastianpakiela.githubexplorer.data.rule.TestCoroutineRule
import com.sebastianpakiela.githubexplorer.data.rule.testCollect
import com.sebastianpakiela.githubexplorer.data.testdata.TestData
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.test.advanceUntilIdle
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
class CommitDaoTest {

    @Rule
    @JvmField
    var testSchedulerRule = TestCoroutineRule()

    @Rule
    @JvmField
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    lateinit var db: AppDatabase
    lateinit var commitDao: CommitDao

    @Before
    fun setup() {
        db = Room.inMemoryDatabaseBuilder(
            InstrumentationRegistry.getInstrumentation().context, AppDatabase::class.java
        ).allowMainThreadQueries().build()
        commitDao = db.commitDao()
    }

    @Test
    fun `Should  return inserted data`() = runTest {
        commitDao.putCommits(listOf(TestData.commitEntity))

        val commitCollector: FlowCollector<List<CommitEntity>> = mock()
        commitDao.getAll().testCollect(testScheduler, commitCollector)

        advanceUntilIdle()

        verify(commitCollector).emit(listOf(TestData.commitEntity))
        verifyNoMoreInteractions(commitCollector)
    }

    @Test
    fun `Should return inserted data by key`() = runTest {
        commitDao.putCommits(listOf(TestData.commitEntity))

        val commitCollector: FlowCollector<List<CommitEntity>> = mock()
        val testObserver = commitDao.get(TestData.commitEntity.repoAndUserKey)
            .testCollect(testScheduler, commitCollector)

        advanceUntilIdle()

        verify(commitCollector).emit(listOf(TestData.commitEntity))
        verifyNoMoreInteractions(commitCollector)
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }

}