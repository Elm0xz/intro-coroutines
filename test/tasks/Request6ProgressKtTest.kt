package tasks

import contributors.MockGithubService
import contributors.progressResults
import contributors.testRequestData
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert
import org.junit.Test

class Request6ProgressKtTest {
    @Test
    fun testProgress() = runBlockingTest {
        val virtualStartTime = currentTime
        var index = 0
        loadContributorsProgress(MockGithubService, testRequestData) {
            users, _ ->
            val expected = progressResults[index++]
            val totalVirtualTime = currentTime - virtualStartTime
            // TODO: uncomment this assertion
            Assert.assertEquals("Expected intermediate result after virtual ${expected.timeFromStart} ms:",
                expected.timeFromStart, totalVirtualTime)
            Assert.assertEquals("Wrong intermediate result after $totalVirtualTime:", expected.users, users)
        }
    }
}
