package xyz.qumn.alumnihub_app.composable;

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.collectIsDraggedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun SlidingCarousel(
    imgs: List<String>,
    modifier: Modifier = Modifier,
    autoSlideDuration: Long = 3000,
) {
    SlidingCarousel(
        modifier = modifier,
        autoSlideDuration = autoSlideDuration,
        itemsCount = imgs.size,
    ) { idx ->
        Box(modifier = modifier) {
            AsyncImage(model = imgs[idx], contentDescription = "image")
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun SlidingCarousel(
    modifier: Modifier = Modifier,
    autoSlideDuration: Long = 3000,
    itemsCount: Int,
    pagerState: PagerState = rememberPagerState { itemsCount },
    itemContent: @Composable (index: Int) -> Unit,
) {
    val isDragged by pagerState.interactionSource.collectIsDraggedAsState()
    val coroutineScope = rememberCoroutineScope()
    val currentPage by remember {
        derivedStateOf {
            pagerState.currentPage
        }
    }

    var currentPageKey by remember { mutableStateOf(0) }

    if (isDragged.not()) {
        LaunchedEffect(currentPageKey) {
            launch {
                delay(autoSlideDuration)
                val nextPage = (currentPage + 1).mod(pagerState.pageCount)
                pagerState.animateScrollToPage(page = nextPage)
                currentPageKey = nextPage
            }
        }
    }

    Box(
        modifier = modifier.fillMaxWidth(),
    ) {
        HorizontalPager(state = pagerState) { page ->
            itemContent(page)
        }

        // you can remove the surface in case you don't want
        // the transparant bacground
        Surface(
            modifier = Modifier
                .padding(bottom = 8.dp)
                .align(Alignment.BottomCenter),
            shape = CircleShape,
            color = Color.Black.copy(alpha = 0.5f)
        ) {
            DotsIndicator(
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp),
                totalDots = itemsCount,
                selectedIndex = if (isDragged) pagerState.currentPage else pagerState.targetPage,
                dotSize = 8.dp,
                onClick = { idx ->
                    coroutineScope.launch {
                        pagerState.animateScrollToPage(idx)
                    }
                }
            )
        }
    }
}

@Composable
fun DotsIndicator(
    modifier: Modifier = Modifier,
    totalDots: Int,
    selectedIndex: Int,
    selectedColor: Color = Color.Yellow,
    unSelectedColor: Color = Color.Gray,
    dotSize: Dp,
    onClick: (idx: Int) -> Unit
) {
    LazyRow(
        modifier = modifier
            .wrapContentWidth()
            .wrapContentHeight()
    ) {
        items(totalDots) { index ->
            IndicatorDot(
                modifier = Modifier.clickable { onClick(index) },
                color = if (index == selectedIndex) selectedColor else unSelectedColor,
                size = dotSize
            )

            if (index != totalDots - 1) {
                Spacer(modifier = Modifier.padding(horizontal = 2.dp))
            }
        }
    }
}

@Composable
fun IndicatorDot(
    modifier: Modifier = Modifier,
    size: Dp,
    color: Color
) {
    Box(
        modifier = modifier
            .size(size)
            .clip(CircleShape)
            .background(color)
    )
}


@OptIn(ExperimentalFoundationApi::class)
@Preview
@Composable
fun previewSlideCarousel() {
    val imgs = listOf(
        "https://placekitten.com/200/287",
        "https://placekitten.com/201/287",
        "https://placekitten.com/202/287"
    )

    Card(
        modifier = Modifier
            .height(220.dp)
            .width(200.dp)
            .padding(16.dp),
        shape = RoundedCornerShape(16.dp)
    ) {
        SlidingCarousel(itemsCount = 3) {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current).data(imgs[it]).build(),
                contentDescription = "image",
                modifier = Modifier.height(200.dp),
            )
        }
    }
}