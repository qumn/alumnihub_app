package xyz.qumn.alumnihub_app.screen.create

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults.SecondaryIndicator
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import xyz.qumn.alumnihub_app.AppState
import xyz.qumn.alumnihub_app.screen.fleamarket.CreateTradePage
import xyz.qumn.alumnihub_app.screen.forum.CreateFormPage
import xyz.qumn.alumnihub_app.screen.lostfound.PublishMissingItemPage
import xyz.qumn.alumnihub_app.ui.theme.Alumnihub_appTheme
import xyz.qumn.alumnihub_app.ui.theme.Blue80


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun CreateScreen(onClickClose: () -> Unit) {
    val titles = listOf("发闲置", "发帖子", "发失物")
    val pagerState = rememberPagerState(pageCount = { titles.size })
    val selectedTabIndex by remember { derivedStateOf { pagerState.currentPage } }
    Scaffold(
        topBar = { TopBar(pagerState, titles, selectedTabIndex, onClickClose) },
    ) {
        Column(
            Modifier
                .padding(it)
                .fillMaxSize(),
            verticalArrangement = Arrangement.Top
        ) {
            HorizontalPager(state = pagerState) { page ->
                if (page == 0) {
                    CreateTradePage { onClickClose() }
                } else if (page == 1) {
                    CreateFormPage { onClickClose() }
                } else if (page == 2) {
                    PublishMissingItemPage { onClickClose() }
                }
            }
        }
    }
}


@Composable
@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
private fun TopBar(
    pagerState: PagerState,
    titles: List<String>,
    selectedTabIndex: Int,
    onClickClose: () -> Unit
) {
    val scope = rememberCoroutineScope()
    TopAppBar(
        navigationIcon = {
            IconButton(onClick = onClickClose) {
                Icon(Icons.Filled.Close, null)
            }
        },
        title = {
            TabRow(selectedTabIndex,
                Modifier
                    .fillMaxWidth()
                    .padding(26.dp, 0.dp),
                divider = {},
                indicator = { tabPositions ->
                    SecondaryIndicator(
                        Modifier.tabIndicatorOffset(tabPositions[selectedTabIndex]),
                        height = 4.dp,
                        color = Blue80.copy(.3f)
                    )
                }) {
                titles.forEachIndexed { idx, title ->
                    Tab(selected = selectedTabIndex == idx,
                        selectedContentColor = Color.Black,
                        unselectedContentColor = Color.Gray,
                        onClick = {
                            scope.launch {
                                pagerState.animateScrollToPage(idx)
                            }
                        }) {
                        Text(
                            modifier = Modifier.padding(4.dp),
                            text = title,
                            style = MaterialTheme.typography.titleLarge.copy(
                                fontWeight = FontWeight.Bold
                            )
                        )
                    }
                }
            }
        }
    )
}


@Preview
@Composable
fun CreateScreenPreview() {
    AppState.ProvideAppState {
        Alumnihub_appTheme {
            CreateScreen {}
        }
    }
}