package com.example.streamflix.app.data.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.streamflix.R
val NetflixBlack = Color(0xFF000000)

@Composable
fun TabRow(
    selectedTab: Int,
    onTabSelected: (Int) -> Unit
) {
    val tabs = listOf(
        stringResource(R.string.all),
        stringResource(R.string.tv_shows),
        stringResource(R.string.movies),
        stringResource(R.string.my_list)
    )

    androidx.compose.material3.TabRow(
        selectedTabIndex = selectedTab,
        containerColor = NetflixBlack,
        contentColor = Color.White,
        indicator = {},
        divider = {}
    ) {
        tabs.forEachIndexed { index, title ->
            Tab(
                selected = selectedTab == index,
                onClick = { onTabSelected(index) },
                modifier = Modifier.background(NetflixBlack)
            ) {
                Text(
                    text = title,
                    color = if (selectedTab == index) Color.White else Color.Gray,
                    fontSize = 14.sp,
                    fontWeight = if (selectedTab == index) FontWeight.Bold else FontWeight.Normal,
                    modifier = Modifier.padding(vertical = 12.dp)
                )
            }
        }
    }
}
