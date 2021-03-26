package tasks

import contributors.GitHubService
import contributors.RequestData
import contributors.User
import contributors.logRepos
import contributors.logUsers
import kotlinx.coroutines.async
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.coroutineScope

suspend fun loadContributorsChannels(
    service: GitHubService,
    req: RequestData,
    updateResults: suspend (List<User>, completed: Boolean) -> Unit
) {
    coroutineScope {
        val channel = Channel<List<User>>()
        val repos = service
            .getOrgRepos(req.org)
            .also { logRepos(req, it) }
            .body() ?: listOf()

        repos.map { repo ->
            async {
                service
                    .getRepoContributors(req.org, repo.name)
                    .also { logUsers(repo, it) }
                    .bodyList()
                    .also { channel.send(it) }
            }
        }

        repos.foldIndexed(emptyList<User>(), { id, acc, _ ->
            channel.receive()
                .toMutableList()
                .also { it.addAll(acc) }
                .also { updateResults(it.toList().aggregate(), id == repos.lastIndex) }
        })
    }
}
