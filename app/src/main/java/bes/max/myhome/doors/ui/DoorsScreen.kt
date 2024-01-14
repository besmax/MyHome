package bes.max.myhome.doors.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import bes.max.myhome.R
import bes.max.myhome.cameras.ui.ShowError
import bes.max.myhome.core.ui.theme.BlackText
import bes.max.myhome.core.ui.theme.CardBgColor
import bes.max.myhome.core.ui.theme.Gray
import bes.max.myhome.core.ui.theme.GrayText
import bes.max.myhome.core.ui.theme.Yellow
import bes.max.myhome.doors.domain.models.Door
import bes.max.myhome.doors.presentation.DoorsScreenState
import bes.max.myhome.doors.presentation.DoorsViewModel
import coil.compose.AsyncImage
import org.koin.androidx.compose.koinViewModel

@Composable
fun DoorsScreen(
    viewModel: DoorsViewModel = koinViewModel()
) {

    val uiState by viewModel.screenState.observeAsState(initial = DoorsScreenState.Loading)

    DoorsScreenContent(
        uiState = uiState,
        onItemClick = { },
        onFavIconClick = { door -> viewModel.updateDoor(door) },
        refreshDoors = { viewModel.getDoors() }
    )
}

@Composable
fun DoorsScreenContent(
    uiState: DoorsScreenState,
    onItemClick: (Door) -> Unit,
    onFavIconClick: (Door) -> Unit,
    refreshDoors: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colorScheme.background)
    ) {
        when (uiState) {
            is DoorsScreenState.Content -> {}
            is DoorsScreenState.Loading -> DoorsScreenState.Loading
            is DoorsScreenState.Error -> ShowError()
        }
    }

}

@Composable
fun DoorListItem(
    door: Door,
    onItemClick: (Door) -> Unit,
    onFavIconClick: (Door) -> Unit,
) {

    Card(
        modifier = Modifier
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

            if (!door.snapshot.isNullOrBlank()) {
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
        Text(
            text = door.name,
            fontWeight = FontWeight.Normal,
            fontSize = 17.sp,
            textAlign = TextAlign.Center,
            color = BlackText,
            modifier = Modifier.padding(top = 20.dp, end = 20.dp, start = 16.dp)
        )

        if (!door.snapshot.isNullOrBlank()) {
            Text(
                text = stringResource(id = R.string.online),
                fontWeight = FontWeight.Normal,
                fontSize = 14.sp,
                textAlign = TextAlign.Center,
                color = GrayText,
                modifier = Modifier.padding(top = 20.dp, end = 20.dp, start = 16.dp)
            )
        }
        
    }

}