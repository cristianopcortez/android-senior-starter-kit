package br.com.ccortez.seniorstarterkitapplication.presentation.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import br.com.ccortez.seniorstarterkitapplication.R
import br.com.ccortez.seniorstarterkitapplication.domain.model.Country
import br.com.ccortez.seniorstarterkitapplication.domain.model.Resource
import br.com.ccortez.seniorstarterkitapplication.presentation.viewmodel.CountriesViewModel
import br.com.ccortez.seniorstarterkitapplication.ui.theme.SeniorStarterKitApplicationTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CountriesRoute(
    viewModel: CountriesViewModel,
    modifier: Modifier = Modifier,
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    CountriesScreen(
        state = state,
        onRetry = viewModel::reload,
        modifier = modifier,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CountriesScreen(
    state: Resource<List<Country>>,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val topBarTitle = stringResource(R.string.countries_title)
    val reloadContentDescription = stringResource(R.string.content_desc_reload_countries)
    val retryLabel = stringResource(R.string.action_retry)
    val refreshLabel = stringResource(R.string.action_refresh)
    val emptyListMessage = stringResource(R.string.countries_empty_list)

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

            is Resource.Success<List<Country>> -> {
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
                            key = { it.code },
                            contentType = { "countryCard" },
                        ) { country ->
                            CountryCard(
                                country = country,
                                metadataFormat = stringResource(R.string.country_metadata),
                                capitalUnknown = stringResource(R.string.country_capital_unknown),
                                modifier = Modifier.fillMaxWidth(),
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
private fun CountryCard(
    country: Country,
    metadataFormat: String,
    capitalUnknown: String,
    modifier: Modifier = Modifier,
) {
    val capital = country.capital?.takeIf { it.isNotBlank() } ?: capitalUnknown
    Card(modifier = modifier) {
        Row(
            Modifier.padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = country.emoji,
                style = MaterialTheme.typography.headlineMedium,
            )
            Column {
                Text(
                    text = country.name,
                    style = MaterialTheme.typography.titleMedium,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                )
                Text(
                    text = metadataFormat.format(country.code, capital, country.continent),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(top = 6.dp),
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun CountriesPreview_Success() {
    SeniorStarterKitApplicationTheme {
        val sample = Country(
            code = "BR",
            name = stringResource(R.string.preview_country_name),
            capital = stringResource(R.string.preview_country_capital),
            emoji = "🇧🇷",
            continent = stringResource(R.string.preview_country_continent),
        )
        CountriesScreen(
            state = Resource.Success(
                listOf(
                    sample,
                    sample.copy(code = "PT", name = "Portugal", emoji = "🇵🇹"),
                ),
            ),
            onRetry = {},
        )
    }
}
