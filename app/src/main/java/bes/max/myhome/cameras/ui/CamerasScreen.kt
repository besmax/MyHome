package bes.max.myhome.cameras.ui

import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.AnchoredDraggableState
import androidx.compose.foundation.gestures.DraggableAnchors
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.anchoredDraggable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshContainer
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import bes.max.myhome.R
import bes.max.myhome.cameras.domain.models.Camera
import bes.max.myhome.cameras.presentation.CamerasScreenState
import bes.max.myhome.cameras.presentation.CamerasViewModel
import bes.max.myhome.core.ui.Loading
import bes.max.myhome.core.ui.theme.BlackText
import bes.max.myhome.core.ui.theme.Blue
import bes.max.myhome.core.ui.theme.CardBgColor
import bes.max.myhome.core.ui.theme.Gray
import bes.max.myhome.core.ui.theme.Yellow
import coil.compose.AsyncImage
import kotlinx.coroutines.delay
import org.koin.androidx.compose.koinViewModel
import kotlin.math.roundToInt

enum class DragValue { Start, Center, End }

@Composable
fun CamerasScreen(
    viewModel: CamerasViewModel = koinViewModel()
) {

    val uiState by viewModel.screenState.observeAsState(initial = CamerasScreenState.Loading)

    LaunchedEffect(key1 = Unit) {
        viewModel.getCameras()
    }

    CameraScreenContent(
        uiState = uiState,
        onItemClick = { },
        onFavIconClick = { camera -> viewModel.updateCamera(camera) },
        refreshCameras = { viewModel.getCameras() }
    )

}

@Composable
fun CameraScreenContent(
    uiState: CamerasScreenState,
    onItemClick: (Camera) -> Unit,
    onFavIconClick: (Camera) -> Unit,
    refreshCameras: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colorScheme.background)
    ) {
        when (uiState) {
            is CamerasScreenState.Content -> CamerasList(
                cameras = uiState.cameras,
                onItemClick = onItemClick,
                onFavIconClick = onFavIconClick,
                refreshCameras = refreshCameras
            )

            is CamerasScreenState.Loading -> Loading()
            is CamerasScreenState.Error -> ShowError()
        }
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CamerasList(
    cameras: List<Camera>,
    onItemClick: (Camera) -> Unit,
    onFavIconClick: (Camera) -> Unit,
    refreshCameras: () -> Unit
) {

    val state = rememberPullToRefreshState()
    if (state.isRefreshing) {
        LaunchedEffect(true) {
            refreshCameras.invoke()
            delay(1500)
            state.endRefresh()
        }
    }
    Box(
        modifier = Modifier
            .nestedScroll(state.nestedScrollConnection),
        contentAlignment = Alignment.Center
    ) {
        LazyColumn(
            modifier = Modifier
                .padding(top = 12.dp)
        ) {
            items(
                items = cameras,
                key = { camera -> camera.id }
            ) { camera ->
                if (!state.isRefreshing) {
                    CameraListItem(camera, onItemClick, onFavIconClick)
                }
            }
        }
        PullToRefreshContainer(
            modifier = Modifier
                .align(Alignment.TopCenter),
            state = state,
            contentColor = Blue,
            containerColor = if (!state.isRefreshing) Color.Transparent
            else MaterialTheme.colorScheme.background
        )

    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun CameraListItem(
    camera: Camera,
    onItemClick: (Camera) -> Unit,
    onFavIconClick: (Camera) -> Unit,
) {

    val density = LocalDensity.current
    val anchors = remember {
        DraggableAnchors {
            DragValue.Start at 0f
            DragValue.Center at with(density) { -60.dp.toPx() }
            DragValue.End at 0f
        }
    }
    val state = remember {
        AnchoredDraggableState(
            initialValue = DragValue.Start,
            positionalThreshold = { distance -> distance * 0.3f },
            velocityThreshold = { with(density) { 100.dp.toPx() } },
            animationSpec = tween(),
        )
    }
    SideEffect { state.updateAnchors(anchors) }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .anchoredDraggable(state, Orientation.Horizontal)
            .background(color = MaterialTheme.colorScheme.background)
    ) {
        // Icon after left swiping
        IconButton(
            onClick = { onFavIconClick(camera.copy(favorites = !camera.favorites)) },
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .padding(end = 20.dp)
                .size(40.dp)
        ) {
            Icon(
                painter = if (camera.favorites) painterResource(id = R.drawable.ic_star_active)
                else painterResource(id = R.drawable.ic_star_empty),
                contentDescription = "Fav icon",
                tint = Yellow
            )
        }

        Card(
            modifier = Modifier
                .offset {
                    IntOffset(
                        x = state
                            .requireOffset()
                            .roundToInt(),
                        y = 0
                    )
                }
                .fillMaxWidth()
                .padding(start = 20.dp, top = 12.dp, end = 20.dp),
            colors = CardDefaults.cardColors(
                containerColor = CardBgColor,
            ),
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(208.dp)
                    .background(color = Gray),
                contentAlignment = Alignment.TopStart
            ) {

                AsyncImage(
                    model = camera.snapshot,
                    contentDescription = "${camera.name} snapshot",
                    error = painterResource(id = R.drawable.snapshot_placeholder),
                    placeholder = painterResource(id = R.drawable.snapshot_placeholder),
                    contentScale = ContentScale.FillBounds,
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(RoundedCornerShape(12.dp, 12.dp, 0.dp, 0.dp)),
                )
                if (camera.rec) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_rec),
                        contentDescription = "Rec icon",
                        tint = Color.Red,
                        modifier = Modifier
                            .padding(all = 12.dp)
                    )
                }

                if (camera.favorites) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_star),
                        contentDescription = "Fav icon",
                        tint = Yellow,
                        modifier = Modifier
                            .padding(all = 8.dp)
                            .align(Alignment.TopEnd)
                    )
                }
            }
            Text(
                text = camera.name,
                fontWeight = FontWeight.Normal,
                fontSize = 17.sp,
                textAlign = TextAlign.Center,
                color = BlackText,
                modifier = Modifier.padding(top = 20.dp, end = 20.dp, start = 16.dp)
            )

            Spacer(modifier = Modifier.height(20.dp))
        }

    }


}

@Composable
fun ShowError() {
    // Здесь мы можем показывать разные виды ошибок в соответствии с нашими вариантами ErrorType:
    // NO_INTERNET, NO_CONTENT, SERVER_ERROR
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
        modifier = Modifier.fillMaxSize()
    ) {

        Text(
            text = stringResource(id = R.string.error_message),
            fontWeight = FontWeight.Normal,
            fontSize = 40.sp,
            textAlign = TextAlign.Center,
            color = BlackText,
            lineHeight = 50.sp
        )
    }
}

@Composable
@Preview
fun CameraListItemPreview() {
    CameraListItem(
        onItemClick = { },
        onFavIconClick = { },
        camera = Camera(
            name = "Cam1",
            snapshot = "",
            room = null,
            id = 1,
            favorites = true,
            rec = true
        )
    )
}