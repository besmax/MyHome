package bes.max.myhome.doors.ui

import android.widget.Toast
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import bes.max.myhome.R
import bes.max.myhome.cameras.ui.DragValue
import bes.max.myhome.cameras.ui.ShowError
import bes.max.myhome.core.ui.theme.BlackText
import bes.max.myhome.core.ui.theme.Blue
import bes.max.myhome.core.ui.theme.CardBgColor
import bes.max.myhome.core.ui.theme.Gray
import bes.max.myhome.core.ui.theme.GrayText
import bes.max.myhome.core.ui.theme.Yellow
import bes.max.myhome.doors.domain.models.Door
import bes.max.myhome.doors.presentation.DoorsScreenState
import bes.max.myhome.doors.presentation.DoorsViewModel
import coil.compose.AsyncImage
import kotlinx.coroutines.delay
import org.koin.androidx.compose.koinViewModel
import kotlin.math.roundToInt

@Composable
fun DoorsScreen(
    viewModel: DoorsViewModel = koinViewModel()
) {

    val uiState by viewModel.screenState.observeAsState(initial = DoorsScreenState.Loading)

    val context = LocalContext.current

    DoorsScreenContent(
        uiState = uiState,
        onItemClick = { },
        onFavIconClick = { door -> viewModel.updateDoor(door) },
        refreshDoors = { viewModel.getDoors() },
        onEditIconClick = { door ->  Toast.makeText(context, "Editing Door <${door.name}>", Toast.LENGTH_SHORT).show() }
    )
}

@Composable
fun DoorsScreenContent(
    uiState: DoorsScreenState,
    onItemClick: (Door) -> Unit,
    onFavIconClick: (Door) -> Unit,
    refreshDoors: () -> Unit,
    onEditIconClick: (Door) -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colorScheme.background)
    ) {
        when (uiState) {
            is DoorsScreenState.Content -> DoorsList(
                doors = uiState.doors,
                onItemClick = onItemClick,
                onFavIconClick = onFavIconClick,
                refreshDoors = refreshDoors,
                onEditIconClick = onEditIconClick
            )

            is DoorsScreenState.Loading -> DoorsScreenState.Loading
            is DoorsScreenState.Error -> ShowError()
        }
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DoorsList(
    doors: List<Door>,
    onItemClick: (Door) -> Unit,
    onFavIconClick: (Door) -> Unit,
    refreshDoors: () -> Unit,
    onEditIconClick: (Door) -> Unit,
) {

    val state = rememberPullToRefreshState()
    if (state.isRefreshing) {
        LaunchedEffect(true) {
            refreshDoors.invoke()
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
                .padding(vertical = 12.dp)
        ) {
            items(
                items = doors,
                key = { door -> door.id }
            ) { door ->
                if (!state.isRefreshing) {
                    DoorListItem(door, onItemClick, onFavIconClick, onEditIconClick)
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
fun DoorListItem(
    door: Door,
    onItemClick: (Door) -> Unit,
    onFavIconClick: (Door) -> Unit,
    onEditIconClick: (Door) -> Unit,
) {

    val density = LocalDensity.current
    val anchors = remember {
        DraggableAnchors {
            DragValue.Start at 0f
            DragValue.Center at with(density) { -100.dp.toPx() }
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
            .background(color = MaterialTheme.colorScheme.background),
        contentAlignment = Alignment.CenterEnd
    ) {
        // Icons after left swiping
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 20.dp),
            verticalAlignment = Alignment.Bottom,
            horizontalArrangement = Arrangement.End
        ) {
            IconButton(
                onClick = { onEditIconClick(door) },
                modifier = Modifier
                    .padding(end = 8.dp)
                    .size(40.dp)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_edit),
                    contentDescription = "Edit icon",
                    tint = Blue
                )
            }

            IconButton(
                onClick = { onFavIconClick(door.copy(favorites = !door.favorites)) },
                modifier = Modifier
                    .padding(end = 20.dp)
                    .size(40.dp)
            ) {
                Icon(
                    painter = if (door.favorites) painterResource(id = R.drawable.ic_star_active)
                    else painterResource(id = R.drawable.ic_star_empty),
                    contentDescription = "Fav icon",
                    tint = Yellow
                )
            }
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
            if (!door.snapshot.isNullOrBlank()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(208.dp)
                        .background(color = Gray),
                    contentAlignment = Alignment.TopStart
                ) {

                    AsyncImage(
                        model = door.snapshot,
                        contentDescription = "${door.name} snapshot",
                        error = painterResource(id = R.drawable.snapshot_placeholder),
                        placeholder = painterResource(id = R.drawable.snapshot_placeholder),
                        contentScale = ContentScale.FillBounds,
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(RoundedCornerShape(12.dp, 12.dp, 0.dp, 0.dp)),
                    )

                }
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = door.name,
                    fontWeight = FontWeight.Normal,
                    fontSize = 17.sp,
                    textAlign = TextAlign.Center,
                    color = BlackText,
                    modifier = Modifier.padding(top = 20.dp, end = 20.dp, start = 16.dp)
                )

                Icon(
                    painter = painterResource(id = R.drawable.ic_locker),
                    contentDescription = "Locker icon",
                    modifier = Modifier.padding(top = 20.dp, end = 20.dp)
                )
            }

            if (!door.snapshot.isNullOrBlank()) {
                Text(
                    text = stringResource(id = R.string.online),
                    fontWeight = FontWeight.Normal,
                    fontSize = 14.sp,
                    textAlign = TextAlign.Center,
                    color = GrayText,
                    modifier = Modifier.padding(end = 20.dp, start = 16.dp)
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

        }
    }

}