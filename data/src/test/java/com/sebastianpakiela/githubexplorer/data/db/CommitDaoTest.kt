package com.sebastianpakiela.githubexplorer.data.db

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.platform.app.InstrumentationRegistry
import com.sebastianpakiela.githubexplorer.data.rule.RxImmediateSchedulerRule
import com.sebastianpakiela.githubexplorer.data.testdata.TestData
import io.reactivex.rxjava3.schedulers.Schedulers
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import java.io.IOException

@RunWith(RobolectricTestRunner::class)
class CommitDaoTest {

    @Rule
    @JvmField
    var testSchedulerRule = RxImmediateSchedulerRule()

    @Rule
    @JvmField
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    lateinit var db: AppDatabase
    lateinit var commitDao: CommitDao

    @Before
    fun setup() {
        db = Room.inMemoryDatabaseBuilder(
            InstrumentationRegistry.getInstrumentation().context,
            AppDatabase::class.java
        ).allowMainThreadQueries()
            .build()
        commitDao = db.commitDao()
    }

    @Test
    fun `Should  return inserted data`() {
        commitDao.putCommits(listOf(TestData.commitEntity)).test()

        val testObserver = commitDao.getAll().test()

        testObserver.assertValueCount(1)
        assertThat(testObserver.values().first().size, equalTo(1))
        assertThat(testObserver.values().first(), equalTo(listOf(TestData.commitEntity)))
    }


    @Test
    fun `Should return inserted data by key`() {
        commitDao.putCommits(listOf(TestData.commitEntity)).subscribeOn(Schedulers.trampoline())
            .observeOn(Schedulers.trampoline()).test()

        val testObserver = commitDao.get(TestData.commitEntity.repoAndUserKey).test()

        testObserver.assertValueCount(1)
        assertThat(testObserver.values().first().size, equalTo(1))
        assertThat(testObserver.values().first(), equalTo(listOf(TestData.commitEntity)))
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }

}