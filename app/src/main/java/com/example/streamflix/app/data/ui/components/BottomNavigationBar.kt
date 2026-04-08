package com.example.streamflix.app.data.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

val NetflixGray = Color(0xFF333333)

@Composable
fun BottomNavigationBar(
    selectedItem: Int = 0,
    onItemSelected: (Int) -> Unit = {},
    modifier: Modifier = Modifier
) {
    val items = listOf(
        BottomNavItem("Home", Icons.Default.Home),
        BottomNavItem("Search", Icons.Default.Search),
        BottomNavItem("My List", Icons.Default.Favorite),
        BottomNavItem("Account", Icons.Default.AccountCircle)
    )

    NavigationBar(
        modifier = modifier.fillMaxWidth(),
        containerColor = NetflixGray,
        contentColor = Color.White
    ) {
        items.forEachIndexed { index, item ->
            NavigationBarItem(
                icon = {
                    Icon(
                        imageVector = item.icon,
                        contentDescription = item.label,
                        modifier = Modifier.size(24.dp)
                    )
                },
                label = {
                    Text(
                        text = item.label,
                        fontSize = 10.sp
                    )
                },
                selected = selectedItem == index,
                onClick = { onItemSelected(index) },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = Color(0xFFE50914),
                    selectedTextColor = Color.White,
                    unselectedIconColor = Color.Gray,
                    unselectedTextColor = Color.Gray,
                    indicatorColor = Color.Transparent
                )
            )
        }
    }
}

data class BottomNavItem(
    val label: String,
    val icon: ImageVector
)