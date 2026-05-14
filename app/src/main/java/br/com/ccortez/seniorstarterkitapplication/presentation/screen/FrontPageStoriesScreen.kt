package br.com.ccortez.seniorstarterkitapplication.presentation.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import br.com.ccortez.seniorstarterkitapplication.data.HackerNewsWeb
import br.com.ccortez.seniorstarterkitapplication.R
import br.com.ccortez.seniorstarterkitapplication.domain.model.Resource
import br.com.ccortez.seniorstarterkitapplication.domain.model.Story
import br.com.ccortez.seniorstarterkitapplication.presentation.viewmodel.FrontPageStoriesViewModel
import br.com.ccortez.seniorstarterkitapplication.ui.theme.SeniorStarterKitApplicationTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FrontPageStoriesRoute(
    viewModel: FrontPageStoriesViewModel,
    modifier: Modifier = Modifier,
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    FrontPageStoriesScreen(
        state = state,
        onRetry = viewModel::reload,
        modifier = modifier,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FrontPageStoriesScreen(
    state: Resource<List<Story>>,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val uriHandler = LocalUriHandler.current
    val topBarTitle = stringResource(R.string.hn_front_page_title)
    val reloadContentDescription = stringResource(R.string.content_desc_reload_stories)
    val retryLabel = stringResource(R.string.action_retry)
    val refreshLabel = stringResource(R.string.action_refresh)
    val emptyListMessage = stringResource(R.string.hn_empty_list)

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(text = topBarTitle) },
                actions = {
                    IconButton(onClick = onRetry) {
                        Icon(
                            imageVector = Icons.Default.Refresh,
                            contentDescription = reloadContentDescription,
                        )
                    }
                },
            )
        },
    ) { innerPadding ->
        when (state) {
            Resource.Loading -> LoadingContent(Modifier.padding(innerPadding))

            is Resource.Error ->
                MessageWithActionColumn(
                    message = state.message,
                    modifier = Modifier.padding(innerPadding),
                    primaryLabel = retryLabel,
                    onPrimary = onRetry,
                )

            is Resource.Success<List<Story>> -> {
                if (state.data.isEmpty()) {
                    MessageWithActionColumn(
                        message = emptyListMessage,
                        modifier = Modifier.padding(innerPadding),
                        primaryLabel = refreshLabel,
                        onPrimary = onRetry,
                    )
                } else {
                    LazyColumn(
                        modifier = Modifier
                            .padding(innerPadding)
                            .fillMaxSize(),
                        contentPadding = PaddingValues(
                            horizontal = 16.dp,
                            vertical = 8.dp,
                        ),
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                    ) {
                        items(
                            items = state.data,
                            key = { it.id },
                            contentType = { "storyCard" },
                        ) { story ->
                            StoryCard(
                                story = story,
                                metadataFormat = stringResource(R.string.hn_story_metadata),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable { uriHandler.openUri(story.url) },
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun LoadingContent(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        CircularProgressIndicator()
    }
}

@Composable
private fun MessageWithActionColumn(
    message: String,
    modifier: Modifier = Modifier,
    primaryLabel: String,
    onPrimary: () -> Unit,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(text = message, style = MaterialTheme.typography.bodyLarge)
        Button(onClick = onPrimary, modifier = Modifier.padding(top = 16.dp)) {
            Text(primaryLabel)
        }
    }
}

@Composable
private fun StoryCard(
    story: Story,
    metadataFormat: String,
    modifier: Modifier = Modifier,
) {
    Card(modifier = modifier) {
        Column(Modifier.padding(16.dp)) {
            Text(
                text = story.title,
                style = MaterialTheme.typography.titleMedium,
                maxLines = 3,
                overflow = TextOverflow.Ellipsis,
            )
            Text(
                text = metadataFormat.format(
                    story.points,
                    story.commentsCount,
                    story.author,
                ),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(top = 6.dp),
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun FrontPageStoriesPreview_Success() {
    SeniorStarterKitApplicationTheme {
        val story1 = Story(
            id = "1",
            title = stringResource(R.string.preview_hn_story_title),
            url = HackerNewsWeb.itemUrl("1"),
            points = 42,
            commentsCount = 7,
            author = stringResource(R.string.preview_hn_author),
        )
        val story2 = story1.copy(
            id = "2",
            title = stringResource(R.string.preview_hn_story_title_second),
        )
        FrontPageStoriesScreen(
            state = Resource.Success(listOf(story1, story2)),
            onRetry = {},
        )
    }
}
