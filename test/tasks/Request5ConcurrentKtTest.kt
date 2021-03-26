package tasks

import contributors.MockGithubService
import contributors.expectedConcurrentResults
import contributors.testRequestData
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert
import org.junit.Test

class Request5ConcurrentKtTest {
    @Test
    fun testConcurrent() = runBlockingTest {
        val virtualStartTime = currentTime
        val result = loadContributorsConcurrent(MockGithubService, testRequestData)
        Assert.assertEquals("Wrong result for 'loadContributorsConcurrent'", expectedConcurrentResults.users, result)
        val totalVirtualTime = currentTime - virtualStartTime
        // TODO: uncomment this assertion
        Assert.assertEquals(
            "The calls run concurrently, so the total virtual time should be 2200 ms: " +
                    "1000 ms for repos request plus max(1000, 1200, 800) = 1200 ms for concurrent contributors requests)",
            expectedConcurrentResults.timeFromStart, totalVirtualTime
        )
        Assert.assertTrue(
            "The calls run concurrently, so the total virtual time should be 2200 ms: " +
                    "1000 ms for repos request plus max(1000, 1200, 800) = 1200 ms for concurrent contributors requests)",
            totalVirtualTime in expectedConcurrentResults.timeFromStart..(expectedConcurrentResults.timeFromStart + 500)
        )
    }
}
