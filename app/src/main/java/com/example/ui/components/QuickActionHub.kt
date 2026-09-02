package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.*

@Composable
fun QuickActionFab(
  onOpenDigitalId: () -> Unit,
  onOpenCampusAi: () -> Unit,
  onOpenEmergencySos: () -> Unit,
  onOpenCanteenOrder: () -> Unit,
  onOpenLostFound: () -> Unit,
  modifier: Modifier = Modifier
) {
  var isExpanded by remember { mutableStateOf(false) }

  Box(
    modifier = modifier,
    contentAlignment = Alignment.BottomEnd
  ) {
    // Backdrop blur / overlay when expanded
    if (isExpanded) {
      Box(
        modifier = Modifier
          .fillMaxSize()
          .clickable { isExpanded = false }
      )
    }

    Column(
      horizontalAlignment = Alignment.End,
      verticalArrangement = Arrangement.spacedBy(12.dp),
      modifier = Modifier.padding(bottom = 16.dp, end = 16.dp)
    ) {
      AnimatedVisibility(
        visible = isExpanded,
        enter = fadeIn(),
        exit = fadeOut()
      ) {
        Column(
          horizontalAlignment = Alignment.End,
          verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
          QuickActionMiniItem(
            title = "Digital Student ID",
            icon = Icons.Default.Badge,
            backgroundColor = PrimaryNavy,
            onClick = {
              isExpanded = false
              onOpenDigitalId()
            },
            testTag = "fab_quick_digital_id"
          )
          QuickActionMiniItem(
            title = "Campus AI Assistant",
            icon = Icons.Default.AutoAwesome,
            backgroundColor = PrimaryNavyLight,
            onClick = {
              isExpanded = false
              onOpenCampusAi()
            },
            testTag = "fab_quick_campus_ai"
          )
          QuickActionMiniItem(
            title = "Canteen Food Order",
            icon = Icons.Default.Fastfood,
            backgroundColor = SaffronGoldDark,
            onClick = {
              isExpanded = false
              onOpenCanteenOrder()
            },
            testTag = "fab_quick_canteen"
          )
          QuickActionMiniItem(
            title = "Report Lost / Found",
            icon = Icons.Default.Search,
            backgroundColor = EmeraldGreenDark,
            onClick = {
              isExpanded = false
              onOpenLostFound()
            },
            testTag = "fab_quick_lost_found"
          )
          QuickActionMiniItem(
            title = "Campus SOS / Safety",
            icon = Icons.Default.Emergency,
            backgroundColor = CrimsonRed,
            onClick = {
              isExpanded = false
              onOpenEmergencySos()
            },
            testTag = "fab_quick_emergency"
          )
        }
      }

      // Main FAB Toggle
      FloatingActionButton(
        onClick = { isExpanded = !isExpanded },
        containerColor = if (isExpanded) PrimaryNavyDark else PrimaryNavy,
        contentColor = SaffronGold,
        shape = CircleShape,
        elevation = FloatingActionButtonDefaults.elevation(6.dp),
        modifier = Modifier.size(56.dp).testTag("main_quick_action_fab")
      ) {
        Icon(
          imageVector = if (isExpanded) Icons.Default.Close else Icons.Default.Bolt,
          contentDescription = "Quick Campus Actions",
          modifier = Modifier.size(28.dp)
        )
      }
    }
  }
}

@Composable
private fun QuickActionMiniItem(
  title: String,
  icon: ImageVector,
  backgroundColor: Color,
  onClick: () -> Unit,
  testTag: String
) {
  Row(
    verticalAlignment = Alignment.CenterVertically,
    horizontalArrangement = Arrangement.End,
    modifier = Modifier
      .clip(RoundedCornerShape(20.dp))
      .clickable(onClick = onClick)
      .padding(4.dp)
      .testTag(testTag)
  ) {
    Surface(
      shape = RoundedCornerShape(8.dp),
      color = MaterialTheme.colorScheme.surface,
      tonalElevation = 4.dp,
      shadowElevation = 2.dp,
      modifier = Modifier.padding(end = 8.dp)
    ) {
      Text(
        text = title,
        color = MaterialTheme.colorScheme.onSurface,
        fontSize = 12.sp,
        fontWeight = FontWeight.SemiBold,
        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
      )
    }

    SmallFloatingActionButton(
      onClick = onClick,
      containerColor = backgroundColor,
      contentColor = Color.White,
      shape = CircleShape,
      modifier = Modifier.size(42.dp)
    ) {
      Icon(
        imageVector = icon,
        contentDescription = title,
        modifier = Modifier.size(20.dp)
      )
    }
  }
}
