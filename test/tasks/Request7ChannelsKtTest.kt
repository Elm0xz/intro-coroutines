package tasks

import contributors.MockGithubService
import contributors.concurrentProgressResults
import contributors.testRequestData
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert
import org.junit.Test

class Request7ChannelsKtTest {
    @Test
    fun testChannels() = runBlockingTest {
        val virtualStartTime = currentTime
        var index = 0
        loadContributorsChannels(MockGithubService, testRequestData) {
                users, _ ->
            val expected = concurrentProgressResults[index++]
            val totalVirtualTime = currentTime - virtualStartTime
            // TODO: uncomment this assertion
            Assert.assertEquals("Expected intermediate result after virtual ${expected.timeFromStart} ms:",
                expected.timeFromStart, totalVirtualTime)
            Assert.assertEquals("Wrong intermediate result after $totalVirtualTime:", expected.users, users)
        }
    }
}
