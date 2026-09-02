package com.example.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.*

enum class MainNavTab(
  val title: String,
  val selectedIcon: ImageVector,
  val unselectedIcon: ImageVector,
  val testTag: String
) {
  HOME("Home", Icons.Filled.Home, Icons.Outlined.Home, "nav_tab_home"),
  ACADEMICS("Academics", Icons.Filled.MenuBook, Icons.Outlined.MenuBook, "nav_tab_academics"),
  EVENTS("Events", Icons.Filled.Celebration, Icons.Outlined.Celebration, "nav_tab_events"),
  SERVICES("Campus Hub", Icons.Filled.Hub, Icons.Outlined.Hub, "nav_tab_services"),
  COMMUNITY("Community", Icons.Filled.Forum, Icons.Outlined.Forum, "nav_tab_community"),
  PROFILE("Profile & ID", Icons.Filled.Person, Icons.Outlined.Person, "nav_tab_profile")
}

@Composable
fun CampusBottomNavigationBar(
  currentTab: MainNavTab,
  onTabSelected: (MainNavTab) -> Unit,
  modifier: Modifier = Modifier
) {
  NavigationBar(
    modifier = modifier
      .fillMaxWidth()
      .navigationBarsPadding(),
    containerColor = MaterialTheme.colorScheme.surface,
    tonalElevation = 0.dp
  ) {
    MainNavTab.values().forEach { tab ->
      val isSelected = currentTab == tab
      NavigationBarItem(
        selected = isSelected,
        onClick = { onTabSelected(tab) },
        icon = {
          Icon(
            imageVector = if (isSelected) tab.selectedIcon else tab.unselectedIcon,
            contentDescription = tab.title,
            tint = if (isSelected) BentoIndigo else MaterialTheme.colorScheme.onSurfaceVariant
          )
        },
        label = {
          Text(
            text = tab.title,
            fontSize = 10.sp,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
            maxLines = 1
          )
        },
        colors = NavigationBarItemDefaults.colors(
          indicatorColor = BentoIndigoLight,
          selectedTextColor = BentoIndigo,
          unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant
        ),
        modifier = Modifier.testTag(tab.testTag)
      )
    }
  }
}

@Composable
fun CampusNavigationRail(
  currentTab: MainNavTab,
  onTabSelected: (MainNavTab) -> Unit,
  onDigitalIdClicked: () -> Unit,
  onAiAssistantClicked: () -> Unit,
  modifier: Modifier = Modifier
) {
  NavigationRail(
    modifier = modifier.fillMaxHeight(),
    containerColor = MaterialTheme.colorScheme.surface,
    header = {
      IconButton(
        onClick = onDigitalIdClicked,
        modifier = Modifier.padding(top = 16.dp).size(48.dp)
      ) {
        Icon(
          imageVector = Icons.Default.Badge,
          contentDescription = "Digital Student ID",
          tint = BentoIndigo
        )
      }
    }
  ) {
    Spacer(modifier = Modifier.height(16.dp))
    MainNavTab.values().forEach { tab ->
      val isSelected = currentTab == tab
      NavigationRailItem(
        selected = isSelected,
        onClick = { onTabSelected(tab) },
        icon = {
          Icon(
            imageVector = if (isSelected) tab.selectedIcon else tab.unselectedIcon,
            contentDescription = tab.title,
            tint = if (isSelected) BentoIndigo else MaterialTheme.colorScheme.onSurfaceVariant
          )
        },
        label = {
          Text(
            text = tab.title,
            fontSize = 11.sp,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
          )
        },
        colors = NavigationRailItemDefaults.colors(
          indicatorColor = BentoIndigoLight,
          selectedTextColor = BentoIndigo,
          unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant
        ),
        modifier = Modifier.testTag(tab.testTag)
      )
    }
  }
}
