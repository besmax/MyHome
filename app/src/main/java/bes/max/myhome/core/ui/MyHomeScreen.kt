package bes.max.myhome.core.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import bes.max.myhome.R
import bes.max.myhome.cameras.ui.CamerasScreen
import bes.max.myhome.core.ui.theme.BlackText
import bes.max.myhome.core.ui.theme.TabIndicatorColor
import bes.max.myhome.doors.ui.DoorsScreen

@Composable
fun MyHomeScreen() {
    MyHomeScreenContent()
}

@Composable
fun MyHomeScreenContent() {

    var tabIndex by remember {
        mutableIntStateOf(0)
    }

    val tabs = listOf(
        stringResource(id = R.string.cameras),
        stringResource(id = R.string.doors)
    )

    Column(modifier = Modifier
        .fillMaxWidth()
        .background(color = MaterialTheme.colorScheme.background)
    ) {

        Text(
            text = stringResource(id = R.string.my_home),
            fontSize = 21.sp,
            textAlign = TextAlign.Center,
            color = BlackText,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp)
        )

        TabRow(
            selectedTabIndex = tabIndex,
            contentColor = TabIndicatorColor,
            containerColor = MaterialTheme.colorScheme.background ,
            indicator = {
                Box(
                    Modifier
                        .tabIndicatorOffset(it[tabIndex])
                        .height(2.dp)
                        .border(2.dp, TabIndicatorColor)
                )
            }
        ) {
            tabs.forEachIndexed { index, title ->
                Tab(
                    selected = tabIndex == index,
                    onClick = { tabIndex = index }
                ) {
                    Text(
                        text = title,
                        fontWeight = FontWeight.Normal,
                        fontSize = 17.sp,
                        textAlign = TextAlign.Center,
                        color = BlackText,
                        modifier = Modifier.padding(vertical = 16.dp)
                    )
                }
            }
        }

        when(tabIndex) {
            0 -> CamerasScreen()
            1 -> DoorsScreen()
        }
    }
}