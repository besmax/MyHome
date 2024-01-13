package bes.max.myhome.cameras.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import bes.max.myhome.R
import bes.max.myhome.cameras.domain.models.Camera
import bes.max.myhome.cameras.presentation.CamerasScreenState
import bes.max.myhome.cameras.presentation.CamerasViewModel
import bes.max.myhome.core.ui.Loading
import bes.max.myhome.core.ui.theme.BlackText
import bes.max.myhome.core.ui.theme.Gray
import bes.max.myhome.core.ui.theme.LightGray
import bes.max.myhome.core.ui.theme.Yellow
import coil.compose.AsyncImage
import org.koin.androidx.compose.koinViewModel


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
        onFavIconClick = { camera -> viewModel.updateCamera(camera) }

    )

}

@Composable
fun CameraScreenContent(
    uiState: CamerasScreenState,
    onItemClick: (Camera) -> Unit,
    onFavIconClick: (Camera) -> Unit,
) {
    Column(modifier = Modifier
        .fillMaxSize()
        .background(color = MaterialTheme.colorScheme.background)) {
        when (uiState) {
            is CamerasScreenState.Content -> CamerasList(
                cameras = uiState.cameras,
                onItemClick = onItemClick,
                onFavIconClick = onFavIconClick
            )

            is CamerasScreenState.Loading -> Loading()
            is CamerasScreenState.Error -> ShowError()
        }
    }

}

@Composable
fun CamerasList(
    cameras: List<Camera>,
    onItemClick: (Camera) -> Unit,
    onFavIconClick: (Camera) -> Unit,
) {
    LazyColumn(
        modifier = Modifier
            .padding(top = 12.dp)
    ) {
        items(
            items = cameras,
            key = { camera -> camera.id }
        ) { camera ->
            CameraListItem(camera, onItemClick, onFavIconClick)
        }
    }
}

@Composable
fun CameraListItem(
    camera: Camera,
    onItemClick: (Camera) -> Unit,
    onFavIconClick: (Camera) -> Unit,
) {

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(208.dp)
                .background(color = Gray),
            contentAlignment = Alignment.TopStart
        ) {
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
                        .padding(all = 4.dp)
                        .align(Alignment.TopEnd)
                )
            }

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
        }
        Text(
            text = camera.name,
            fontWeight = FontWeight.Normal,
            fontSize = 17.sp,
            textAlign = TextAlign.Center,
            color = BlackText,
            modifier = Modifier.padding(vertical = 20.dp)
        )
    }
}

@Composable
fun ShowError() {
    // Здесь мы можем показывать разные виды ошибок в соответствии с нашими вариантами ErrorType:
    // NO_INTERNET, NO_CONTENT, SERVER_ERROR
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
        modifier = Modifier.fillMaxWidth()
    ) {

        Text(
            text = stringResource(id = R.string.cameras_error),
            fontWeight = FontWeight.Normal,
            fontSize = 42.sp,
            textAlign = TextAlign.Center,
            color = BlackText,
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