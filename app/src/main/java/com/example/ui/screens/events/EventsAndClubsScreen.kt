package com.example.ui.screens.events

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.data.model.CampusClub
import com.example.data.model.CollegeEvent
import com.example.data.model.EventCategory
import com.example.data.repository.CampusRepository
import com.example.ui.theme.*

enum class EventsTab(val title: String) {
  ALL_EVENTS("Campus Events & Fests"),
  REGISTERED("My Registrations"),
  CLUBS("Student Clubs & Societies")
}

@Composable
fun EventsAndClubsScreen(
  repository: CampusRepository,
  modifier: Modifier = Modifier
) {
  var selectedTab by remember { mutableStateOf(EventsTab.ALL_EVENTS) }
  val events by repository.events.collectAsState()
  val clubs by repository.clubs.collectAsState()

  var selectedEventForPass by remember { mutableStateOf<CollegeEvent?>(null) }
  var selectedClubForDetails by remember { mutableStateOf<CampusClub?>(null) }

  Column(
    modifier = modifier
      .fillMaxSize()
      .background(MaterialTheme.colorScheme.background)
  ) {
    TabRow(
      selectedTabIndex = selectedTab.ordinal,
      containerColor = MaterialTheme.colorScheme.surface,
      contentColor = PrimaryNavy
    ) {
      EventsTab.values().forEach { tab ->
        Tab(
          selected = selectedTab == tab,
          onClick = { selectedTab = tab },
          text = {
            Text(
              text = tab.title,
              fontWeight = if (selectedTab == tab) FontWeight.Bold else FontWeight.Normal,
              fontSize = 13.sp
            )
          },
          modifier = Modifier.testTag("events_tab_${tab.name.lowercase()}")
        )
      }
    }

    when (selectedTab) {
      EventsTab.ALL_EVENTS -> AllEventsView(
        events = events,
        onToggleRegister = { repository.toggleEventRegistration(it) },
        onViewPass = { selectedEventForPass = it }
      )
      EventsTab.REGISTERED -> RegisteredEventsView(
        events = events.filter { it.isRegistered },
        onCancelRegistration = { repository.toggleEventRegistration(it) },
        onViewPass = { selectedEventForPass = it }
      )
      EventsTab.CLUBS -> ClubsView(
        clubs = clubs,
        onToggleJoin = { repository.toggleClubMembership(it) },
        onViewDetails = { selectedClubForDetails = it }
      )
    }
  }

  selectedEventForPass?.let { event ->
    EventPassDialog(
      event = event,
      onDismiss = { selectedEventForPass = null }
    )
  }

  selectedClubForDetails?.let { club ->
    ClubDetailDialog(
      club = club,
      onDismiss = { selectedClubForDetails = null },
      onToggleJoin = { repository.toggleClubMembership(club.id) }
    )
  }
}

// -------------------------------------------------------------
// 1. ALL EVENTS VIEW
// -------------------------------------------------------------
@Composable
private fun AllEventsView(
  events: List<CollegeEvent>,
  onToggleRegister: (String) -> Unit,
  onViewPass: (CollegeEvent) -> Unit
) {
  var selectedCategory by remember { mutableStateOf("All") }
  val categories = listOf("All", "Technical", "Hackathons", "Cultural", "Workshops", "Sports & Games", "Symposiums")

  val filteredEvents = remember(selectedCategory, events) {
    if (selectedCategory == "All") events
    else events.filter { it.category.label.equals(selectedCategory, ignoreCase = true) }
  }

  LazyColumn(
    modifier = Modifier.fillMaxSize(),
    contentPadding = PaddingValues(16.dp),
    verticalArrangement = Arrangement.spacedBy(14.dp)
  ) {
    item {
      LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        items(categories) { cat ->
          val isSelected = selectedCategory == cat
          FilterChip(
            selected = isSelected,
            onClick = { selectedCategory = cat },
            label = { Text(cat) },
            colors = FilterChipDefaults.filterChipColors(
              selectedContainerColor = PrimaryNavy,
              selectedLabelColor = Color.White
            )
          )
        }
      }
    }

    items(filteredEvents) { evt ->
      Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(2.dp),
        modifier = Modifier.fillMaxWidth().testTag("event_card_${evt.id}")
      ) {
        Column(modifier = Modifier.padding(16.dp)) {
          // Top row: Organizer & Fee
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Surface(
              color = PrimaryNavy.copy(alpha = 0.1f),
              shape = RoundedCornerShape(6.dp)
            ) {
              Text(
                text = evt.organizerClub.uppercase(),
                color = PrimaryNavy,
                fontWeight = FontWeight.Bold,
                fontSize = 10.sp,
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
              )
            }

            Surface(
              color = if (evt.registrationFee == "Free") EmeraldGreenLight else SaffronGoldLight,
              shape = RoundedCornerShape(6.dp)
            ) {
              Text(
                text = evt.registrationFee,
                color = if (evt.registrationFee == "Free") EmeraldGreenDark else SaffronGoldDark,
                fontWeight = FontWeight.Bold,
                fontSize = 11.sp,
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
              )
            }
          }

          Spacer(modifier = Modifier.height(8.dp))

          Text(
            text = evt.title,
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
          )

          Spacer(modifier = Modifier.height(4.dp))

          Text(
            text = evt.description,
            style = MaterialTheme.typography.bodySmall.copy(color = MaterialTheme.colorScheme.onSurfaceVariant, lineHeight = 18.sp)
          )

          Spacer(modifier = Modifier.height(10.dp))

          // Date, Time, Venue Pills
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
          ) {
            InfoChip(icon = Icons.Default.Event, text = evt.eventDate)
            InfoChip(icon = Icons.Default.Place, text = evt.venue)
          }

          Spacer(modifier = Modifier.height(8.dp))

          Text(
            text = "Faculty/Student Leads: ${evt.coordinators}",
            fontSize = 11.sp,
            color = PrimaryNavy,
            fontWeight = FontWeight.Medium
          )

          Spacer(modifier = Modifier.height(12.dp))

          // Bottom Action Row
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Text(
              text = "${evt.totalParticipants} / ${evt.maxCapacity} Registered",
              style = MaterialTheme.typography.bodySmall.copy(color = MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 11.sp)
            )

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
              if (evt.isRegistered) {
                OutlinedButton(
                  onClick = { onViewPass(evt) },
                  shape = RoundedCornerShape(8.dp),
                  contentPadding = PaddingValues(horizontal = 10.dp, vertical = 6.dp)
                ) {
                  Icon(Icons.Default.QrCode, contentDescription = null, modifier = Modifier.size(16.dp))
                  Spacer(modifier = Modifier.width(4.dp))
                  Text("Entry Pass", fontSize = 12.sp)
                }
              }

              Button(
                onClick = { onToggleRegister(evt.id) },
                colors = ButtonDefaults.buttonColors(
                  containerColor = if (evt.isRegistered) CrimsonRed else PrimaryNavy
                ),
                shape = RoundedCornerShape(8.dp),
                contentPadding = PaddingValues(horizontal = 14.dp, vertical = 6.dp),
                modifier = Modifier.testTag("event_action_btn_${evt.id}")
              ) {
                Text(
                  text = if (evt.isRegistered) "Cancel Pass" else "Register Now",
                  fontSize = 12.sp,
                  fontWeight = FontWeight.Bold
                )
              }
            }
          }
        }
      }
    }
  }
}

// -------------------------------------------------------------
// 2. REGISTERED EVENTS VIEW
// -------------------------------------------------------------
@Composable
private fun RegisteredEventsView(
  events: List<CollegeEvent>,
  onCancelRegistration: (String) -> Unit,
  onViewPass: (CollegeEvent) -> Unit
) {
  if (events.isEmpty()) {
    Box(
      modifier = Modifier.fillMaxSize().padding(32.dp),
      contentAlignment = Alignment.Center
    ) {
      Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Icon(Icons.Default.ConfirmationNumber, contentDescription = null, modifier = Modifier.size(56.dp), tint = MaterialTheme.colorScheme.onSurfaceVariant)
        Spacer(modifier = Modifier.height(12.dp))
        Text("No Active Event Registrations", fontWeight = FontWeight.Bold, fontSize = 16.sp)
        Text("Browse the Campus Fests tab to register for symposiums & hackathons.", color = MaterialTheme.colorScheme.onSurfaceVariant, textAlign = TextAlign.Center, fontSize = 12.sp)
      }
    }
    return
  }

  LazyColumn(
    modifier = Modifier.fillMaxSize(),
    contentPadding = PaddingValues(16.dp),
    verticalArrangement = Arrangement.spacedBy(14.dp)
  ) {
    items(events) { evt ->
      Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(2.dp),
        modifier = Modifier.fillMaxWidth()
      ) {
        Column(modifier = Modifier.padding(16.dp)) {
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Surface(
              color = EmeraldGreenLight,
              shape = RoundedCornerShape(6.dp)
            ) {
              Text(
                text = "CONFIRMED PASS",
                color = EmeraldGreenDark,
                fontWeight = FontWeight.Bold,
                fontSize = 10.sp,
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
              )
            }

            Text("Pass ID: ${evt.qrPassCode ?: "PASS-01"}", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = PrimaryNavy)
          }

          Spacer(modifier = Modifier.height(8.dp))
          Text(evt.title, style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold))
          Spacer(modifier = Modifier.height(4.dp))
          Text("Venue: ${evt.venue} • Date: ${evt.eventDate} (${evt.eventTime})", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)

          Spacer(modifier = Modifier.height(12.dp))

          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
          ) {
            Button(
              onClick = { onViewPass(evt) },
              colors = ButtonDefaults.buttonColors(containerColor = PrimaryNavy),
              shape = RoundedCornerShape(8.dp),
              modifier = Modifier.weight(1f)
            ) {
              Icon(Icons.Default.QrCodeScanner, contentDescription = null, modifier = Modifier.size(16.dp))
              Spacer(modifier = Modifier.width(6.dp))
              Text("Show QR Entry Ticket", fontSize = 12.sp)
            }

            OutlinedButton(
              onClick = { onCancelRegistration(evt.id) },
              shape = RoundedCornerShape(8.dp)
            ) {
              Text("Deregister", fontSize = 12.sp, color = CrimsonRed)
            }
          }
        }
      }
    }
  }
}

// -------------------------------------------------------------
// 3. STUDENT CLUBS & SOCIETIES
// -------------------------------------------------------------
@Composable
private fun ClubsView(
  clubs: List<CampusClub>,
  onToggleJoin: (String) -> Unit,
  onViewDetails: (CampusClub) -> Unit
) {
  LazyColumn(
    modifier = Modifier.fillMaxSize(),
    contentPadding = PaddingValues(16.dp),
    verticalArrangement = Arrangement.spacedBy(14.dp)
  ) {
    items(clubs) { club ->
      Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(2.dp),
        modifier = Modifier
          .fillMaxWidth()
          .clickable { onViewDetails(club) }
          .testTag("club_card_${club.id}")
      ) {
        Column(modifier = Modifier.padding(16.dp)) {
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Column {
              Text(
                text = club.name,
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
              )
              Text(
                text = "${club.category} Society • ${club.memberCount} Members",
                style = MaterialTheme.typography.bodySmall.copy(color = MaterialTheme.colorScheme.onSurfaceVariant)
              )
            }

            Surface(
              color = if (club.recruitmentOpen) EmeraldGreenLight else SaffronGoldLight,
              shape = RoundedCornerShape(6.dp)
            ) {
              Text(
                text = if (club.recruitmentOpen) "RECRUITING" else "CLOSED",
                color = if (club.recruitmentOpen) EmeraldGreenDark else SaffronGoldDark,
                fontWeight = FontWeight.Bold,
                fontSize = 10.sp,
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
              )
            }
          }

          Spacer(modifier = Modifier.height(10.dp))

          Text(
            text = club.description,
            style = MaterialTheme.typography.bodySmall.copy(color = MaterialTheme.colorScheme.onSurfaceVariant, lineHeight = 18.sp)
          )

          Spacer(modifier = Modifier.height(10.dp))

          Text(
            text = "Staff Advisor: ${club.staffAdvisor} • President: ${club.studentPresident}",
            style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp, color = PrimaryNavy, fontWeight = FontWeight.SemiBold)
          )

          Spacer(modifier = Modifier.height(12.dp))

          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Text(
              text = club.achievements.firstOrNull() ?: "Weekly campus workshops",
              style = MaterialTheme.typography.bodySmall.copy(color = MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 11.sp),
              modifier = Modifier.weight(1f)
            )

            Button(
              onClick = { onToggleJoin(club.id) },
              colors = ButtonDefaults.buttonColors(
                containerColor = if (club.isJoined) CrimsonRed else PrimaryNavy
              ),
              shape = RoundedCornerShape(8.dp),
              modifier = Modifier.testTag("join_club_button_${club.id}")
            ) {
              Text(text = if (club.isJoined) "Leave Club" else "Join Club", fontSize = 11.sp, fontWeight = FontWeight.Bold)
            }
          }
        }
      }
    }
  }
}

// -------------------------------------------------------------
// EVENT PASS MODAL
// -------------------------------------------------------------
@Composable
private fun EventPassDialog(
  event: CollegeEvent,
  onDismiss: () -> Unit
) {
  Dialog(onDismissRequest = onDismiss) {
    Surface(
      shape = RoundedCornerShape(20.dp),
      color = MaterialTheme.colorScheme.surface,
      modifier = Modifier.fillMaxWidth(0.95f)
    ) {
      Column(
        modifier = Modifier
          .fillMaxWidth()
          .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
      ) {
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Text("Official Entry Pass", fontWeight = FontWeight.Bold, color = PrimaryNavy, fontSize = 16.sp)
          IconButton(onClick = onDismiss) { Icon(Icons.Default.Close, contentDescription = "Close") }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Pass Badge
        Surface(
          color = PrimaryNavy,
          shape = RoundedCornerShape(12.dp),
          modifier = Modifier.fillMaxWidth()
        ) {
          Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
          ) {
            Text(event.title, color = SaffronGold, fontWeight = FontWeight.Bold, fontSize = 16.sp, textAlign = TextAlign.Center)
            Text(event.organizerClub, color = Color.White.copy(alpha = 0.8f), fontSize = 12.sp)
            Spacer(modifier = Modifier.height(10.dp))
            Text("Date: ${event.eventDate} | Time: ${event.eventTime}", color = Color.White, fontSize = 12.sp)
            Text("Venue: ${event.venue}", color = Color.White, fontSize = 12.sp)
          }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Visual QR Representation
        Box(
          modifier = Modifier
            .size(160.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(Color.White)
            .border(2.dp, PrimaryNavy, RoundedCornerShape(12.dp)),
          contentAlignment = Alignment.Center
        ) {
          Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(Icons.Default.QrCode2, contentDescription = null, modifier = Modifier.size(110.dp), tint = PrimaryNavy)
            Text(event.qrPassCode ?: "PASS-01", fontWeight = FontWeight.Bold, fontSize = 12.sp, color = PrimaryNavy)
          }
        }

        Spacer(modifier = Modifier.height(12.dp))
        Text("Scan at the venue entrance to record attendance & kit collection.", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant, textAlign = TextAlign.Center)

        Spacer(modifier = Modifier.height(16.dp))
        Button(
          onClick = onDismiss,
          colors = ButtonDefaults.buttonColors(containerColor = PrimaryNavy),
          modifier = Modifier.fillMaxWidth(),
          shape = RoundedCornerShape(10.dp)
        ) {
          Text("Done")
        }
      }
    }
  }
}

// -------------------------------------------------------------
// CLUB DETAIL MODAL
// -------------------------------------------------------------
@Composable
private fun ClubDetailDialog(
  club: CampusClub,
  onDismiss: () -> Unit,
  onToggleJoin: () -> Unit
) {
  Dialog(onDismissRequest = onDismiss) {
    Surface(
      shape = RoundedCornerShape(20.dp),
      color = MaterialTheme.colorScheme.surface,
      modifier = Modifier.fillMaxWidth(0.95f)
    ) {
      Column(
        modifier = Modifier
          .fillMaxWidth()
          .padding(20.dp)
      ) {
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Text(club.name, fontWeight = FontWeight.Bold, fontSize = 16.sp, color = PrimaryNavy)
          IconButton(onClick = onDismiss) { Icon(Icons.Default.Close, contentDescription = "Close") }
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text(club.description, fontSize = 13.sp, lineHeight = 18.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)

        Spacer(modifier = Modifier.height(12.dp))

        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
          Text("• Staff Advisor: ${club.staffAdvisor}", fontSize = 12.sp, fontWeight = FontWeight.Medium)
          Text("• Student Secretary: ${club.studentPresident}", fontSize = 12.sp, fontWeight = FontWeight.Medium)
          Text("• Total Active Members: ${club.memberCount}", fontSize = 12.sp, fontWeight = FontWeight.Medium)
          if (club.achievements.isNotEmpty()) {
            Text("• Key Achievements: ${club.achievements.joinToString("; ")}", fontSize = 12.sp, fontWeight = FontWeight.Medium)
          }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
          onClick = {
            onToggleJoin()
            onDismiss()
          },
          colors = ButtonDefaults.buttonColors(
            containerColor = if (club.isJoined) CrimsonRed else PrimaryNavy
          ),
          modifier = Modifier.fillMaxWidth(),
          shape = RoundedCornerShape(10.dp)
        ) {
          Text(if (club.isJoined) "Leave Club" else "Join Club")
        }
      }
    }
  }
}

@Composable
private fun InfoChip(icon: androidx.compose.ui.graphics.vector.ImageVector, text: String) {
  Row(
    verticalAlignment = Alignment.CenterVertically,
    modifier = Modifier
      .clip(RoundedCornerShape(6.dp))
      .background(MaterialTheme.colorScheme.surfaceVariant)
      .padding(horizontal = 8.dp, vertical = 4.dp)
  ) {
    Icon(imageVector = icon, contentDescription = null, modifier = Modifier.size(14.dp), tint = PrimaryNavy)
    Spacer(modifier = Modifier.width(4.dp))
    Text(text = text, fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
  }
}
