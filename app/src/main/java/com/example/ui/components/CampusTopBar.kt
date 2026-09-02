package com.example.ui.components

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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.NotificationItem
import com.example.data.model.UserProfile
import com.example.data.model.UserRole
import com.example.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CampusTopBar(
  currentUser: UserProfile,
  notifications: List<NotificationItem>,
  onRoleSelected: (UserRole) -> Unit,
  onSearchClicked: () -> Unit,
  onNotificationsClicked: () -> Unit,
  onDigitalIdClicked: () -> Unit,
  onAiAssistantClicked: () -> Unit,
  modifier: Modifier = Modifier
) {
  var showRoleMenu by remember { mutableStateOf(false) }
  val unreadCount = notifications.count { !it.isRead }

  Surface(
    modifier = modifier.fillMaxWidth(),
    color = MaterialTheme.colorScheme.surface,
    tonalElevation = 0.dp,
    shadowElevation = 1.dp
  ) {
    Column(
      modifier = Modifier
        .fillMaxWidth()
        .statusBarsPadding()
        .padding(horizontal = 16.dp, vertical = 10.dp)
    ) {
      Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
      ) {
        // Left: Avatar + Good Morning Bento Header
        Row(
          verticalAlignment = Alignment.CenterVertically,
          modifier = Modifier.weight(1f)
        ) {
          Box(
            modifier = Modifier
              .size(44.dp)
              .clip(CircleShape)
              .background(BentoIndigo)
              .clickable { onDigitalIdClicked() },
            contentAlignment = Alignment.Center
          ) {
            Text(
              text = currentUser.avatarInitials,
              color = Color.White,
              fontWeight = FontWeight.Bold,
              fontSize = 15.sp
            )
          }

          Spacer(modifier = Modifier.width(12.dp))

          Column {
            Text(
              text = "CAMPUS CONNECT • ${currentUser.year}",
              style = MaterialTheme.typography.labelSmall.copy(
                fontWeight = FontWeight.Bold,
                fontSize = 10.sp,
                letterSpacing = 1.2.sp,
                color = BentoSlateMuted
              )
            )
            Text(
              text = currentUser.fullName,
              style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                color = MaterialTheme.colorScheme.onSurface
              ),
              maxLines = 1,
              overflow = TextOverflow.Ellipsis
            )
          }
        }

        // Right Actions: Role badge, AI, Search, Notifications
        Row(
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
          // Role selector pill
          Box {
            Surface(
              onClick = { showRoleMenu = true },
              shape = RoundedCornerShape(12.dp),
              color = Color(currentUser.role.badgeColor).copy(alpha = 0.12f),
              border = null,
              modifier = Modifier.testTag("role_switcher_pill")
            ) {
              Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp)
              ) {
                Box(
                  modifier = Modifier
                    .size(7.dp)
                    .clip(CircleShape)
                    .background(Color(currentUser.role.badgeColor))
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                  text = currentUser.role.displayName,
                  color = Color(currentUser.role.badgeColor),
                  fontSize = 11.sp,
                  fontWeight = FontWeight.Bold
                )
                Icon(
                  imageVector = Icons.Default.ArrowDropDown,
                  contentDescription = "Switch Role",
                  tint = Color(currentUser.role.badgeColor),
                  modifier = Modifier.size(14.dp)
                )
              }
            }

            DropdownMenu(
              expanded = showRoleMenu,
              onDismissRequest = { showRoleMenu = false }
            ) {
              Text(
                text = "Switch Persona View",
                style = MaterialTheme.typography.labelMedium.copy(color = BentoIndigo),
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
              )
              Divider()
              UserRole.values().forEach { role ->
                DropdownMenuItem(
                  text = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                      Box(
                        modifier = Modifier
                          .size(10.dp)
                          .clip(CircleShape)
                          .background(Color(role.badgeColor))
                      )
                      Spacer(modifier = Modifier.width(8.dp))
                      Text(
                        text = role.displayName,
                        fontWeight = if (currentUser.role == role) FontWeight.Bold else FontWeight.Normal
                      )
                    }
                  },
                  onClick = {
                    onRoleSelected(role)
                    showRoleMenu = false
                  }
                )
              }
            }
          }

          // Campus AI Assistant Bento Icon
          IconButton(
            onClick = onAiAssistantClicked,
            modifier = Modifier
              .size(38.dp)
              .clip(RoundedCornerShape(12.dp))
              .background(BentoIndigoLight)
              .testTag("campus_ai_top_button")
          ) {
            Icon(
              imageVector = Icons.Default.AutoAwesome,
              contentDescription = "Campus AI Assistant",
              tint = BentoIndigo,
              modifier = Modifier.size(18.dp)
            )
          }

          // Search button
          IconButton(
            onClick = onSearchClicked,
            modifier = Modifier
              .size(38.dp)
              .clip(RoundedCornerShape(12.dp))
              .background(Color(0xFFF1F5F9))
              .testTag("global_search_button")
          ) {
            Icon(
              imageVector = Icons.Outlined.Search,
              contentDescription = "Smart Search",
              tint = BentoSlateMedium,
              modifier = Modifier.size(18.dp)
            )
          }

          // Notification button with badge
          IconButton(
            onClick = onNotificationsClicked,
            modifier = Modifier
              .size(38.dp)
              .clip(RoundedCornerShape(12.dp))
              .background(Color(0xFFF1F5F9))
              .testTag("notifications_button")
          ) {
            BadgedBox(
              badge = {
                if (unreadCount > 0) {
                  Badge(
                    containerColor = CrimsonRed,
                    contentColor = Color.White
                  ) {
                    Text(text = "$unreadCount", fontSize = 9.sp, fontWeight = FontWeight.Bold)
                  }
                }
              }
            ) {
              Icon(
                imageVector = Icons.Outlined.Notifications,
                contentDescription = "Notifications",
                tint = BentoSlateMedium,
                modifier = Modifier.size(18.dp)
              )
            }
          }
        }
      }
    }
  }
}
