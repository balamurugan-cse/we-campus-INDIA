package com.example.ui.components

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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.data.model.*
import com.example.ui.theme.*

// -------------------------------------------------------------
// 1. DIGITAL STUDENT ID CARD DIALOG (With Verification QR)
// -------------------------------------------------------------
@Composable
fun DigitalIdCardDialog(
  user: UserProfile,
  onDismiss: () -> Unit
) {
  var isQrFlipped by remember { mutableStateOf(false) }

  Dialog(
    onDismissRequest = onDismiss,
    properties = DialogProperties(usePlatformDefaultWidth = false)
  ) {
    Surface(
      modifier = Modifier
        .fillMaxWidth(0.92f)
        .wrapContentHeight()
        .clip(RoundedCornerShape(24.dp)),
      color = MaterialTheme.colorScheme.surface,
      tonalElevation = 6.dp
    ) {
      Column(
        modifier = Modifier
          .fillMaxWidth()
          .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
      ) {
        // Close icon header
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
              imageVector = Icons.Default.Badge,
              contentDescription = null,
              tint = PrimaryNavy,
              modifier = Modifier.size(22.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
              text = "Official Smart Campus ID",
              style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.Bold,
                color = PrimaryNavy
              )
            )
          }
          IconButton(onClick = onDismiss) {
            Icon(imageVector = Icons.Default.Close, contentDescription = "Close")
          }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // ID Card Surface Container
        Box(
          modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(
              Brush.verticalGradient(
                colors = listOf(PrimaryNavy, PrimaryNavyDark)
              )
            )
            .border(2.dp, SaffronGold.copy(alpha = 0.6f), RoundedCornerShape(20.dp))
            .padding(18.dp)
        ) {
          Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
          ) {
            // College Header Crest
            Row(
              modifier = Modifier.fillMaxWidth(),
              verticalAlignment = Alignment.CenterVertically,
              horizontalArrangement = Arrangement.Center
            ) {
              Icon(
                imageVector = Icons.Default.AccountBalance,
                contentDescription = null,
                tint = SaffronGold,
                modifier = Modifier.size(24.dp)
              )
              Spacer(modifier = Modifier.width(8.dp))
              Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                  text = "NATIONAL INSTITUTE OF ENGG & TECH",
                  color = Color.White,
                  fontSize = 11.sp,
                  fontWeight = FontWeight.Bold,
                  letterSpacing = 0.5.sp
                )
                Text(
                  text = "AUTONOMOUS • COIMBATORE, TAMIL NADU",
                  color = SaffronGold,
                  fontSize = 9.sp,
                  fontWeight = FontWeight.SemiBold
                )
              }
            }

            Spacer(modifier = Modifier.height(16.dp))

            if (!isQrFlipped) {
              // Front: Photo Avatar + Student Details
              Box(
                modifier = Modifier
                  .size(90.dp)
                  .clip(CircleShape)
                  .background(SurfaceLight)
                  .border(3.dp, SaffronGold, CircleShape),
                contentAlignment = Alignment.Center
              ) {
                Text(
                  text = user.avatarInitials,
                  style = MaterialTheme.typography.displayMedium.copy(
                    color = PrimaryNavy,
                    fontWeight = FontWeight.Bold
                  )
                )
              }

              Spacer(modifier = Modifier.height(12.dp))

              Text(
                text = user.fullName,
                color = Color.White,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
              )

              Surface(
                color = SaffronGold.copy(alpha = 0.2f),
                shape = RoundedCornerShape(6.dp),
                modifier = Modifier.padding(vertical = 4.dp)
              ) {
                Text(
                  text = "REG NO: ${user.rollNumber}",
                  color = SaffronGold,
                  fontSize = 12.sp,
                  fontWeight = FontWeight.Bold,
                  modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                )
              }

              Spacer(modifier = Modifier.height(8.dp))

              // Detail Grid
              Column(
                modifier = Modifier
                  .fillMaxWidth()
                  .clip(RoundedCornerShape(12.dp))
                  .background(Color.White.copy(alpha = 0.08f))
                  .padding(12.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp)
              ) {
                IdCardRow(label = "Department", value = user.department)
                IdCardRow(label = "Degree & Year", value = "${user.degree} • ${user.year} (${user.section})")
                IdCardRow(label = "Blood Group", value = user.bloodGroup)
                IdCardRow(label = "Valid Upto", value = user.validityDate)
                IdCardRow(label = "Emergency No", value = user.emergencyContact)
              }
            } else {
              // Back: Campus Gate / Exam Hall Verification QR Code
              Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(vertical = 8.dp)
              ) {
                Text(
                  text = "DIGITAL GATE PASS & EXAM VERIFICATION",
                  color = SaffronGold,
                  fontSize = 10.sp,
                  fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(14.dp))

                // QR Code Matrix Simulation
                Box(
                  modifier = Modifier
                    .size(160.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color.White)
                    .padding(12.dp),
                  contentAlignment = Alignment.Center
                ) {
                  Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                  ) {
                    Icon(
                      imageVector = Icons.Default.QrCode2,
                      contentDescription = "QR Code",
                      tint = Color.Black,
                      modifier = Modifier.size(110.dp)
                    )
                    Text(
                      text = user.rollNumber,
                      color = Color.Black,
                      fontSize = 10.sp,
                      fontWeight = FontWeight.Bold,
                      fontFamily = FontFamily.Monospace
                    )
                  }
                }

                Spacer(modifier = Modifier.height(10.dp))

                Text(
                  text = "Scan at Security Gate, Library, Exam Hall & Bus Entry",
                  color = Color.White.copy(alpha = 0.8f),
                  fontSize = 10.sp,
                  textAlign = TextAlign.Center
                )
              }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Flip Button
            Button(
              onClick = { isQrFlipped = !isQrFlipped },
              colors = ButtonDefaults.buttonColors(
                containerColor = SaffronGold,
                contentColor = Color.Black
              ),
              shape = RoundedCornerShape(12.dp),
              modifier = Modifier.fillMaxWidth().testTag("flip_id_card_button")
            ) {
              Icon(
                imageVector = if (isQrFlipped) Icons.Default.Badge else Icons.Default.QrCode,
                contentDescription = null,
                modifier = Modifier.size(18.dp)
              )
              Spacer(modifier = Modifier.width(8.dp))
              Text(
                text = if (isQrFlipped) "Show Student Details" else "Show Verification QR Code",
                fontWeight = FontWeight.Bold,
                fontSize = 13.sp
              )
            }
          }
        }
      }
    }
  }
}

@Composable
private fun IdCardRow(label: String, value: String) {
  Row(
    modifier = Modifier.fillMaxWidth(),
    horizontalArrangement = Arrangement.SpaceBetween
  ) {
    Text(
      text = label,
      color = Color.White.copy(alpha = 0.65f),
      fontSize = 11.sp,
      fontWeight = FontWeight.Normal
    )
    Text(
      text = value,
      color = Color.White,
      fontSize = 11.sp,
      fontWeight = FontWeight.SemiBold,
      textAlign = TextAlign.End,
      maxLines = 1,
      overflow = TextOverflow.Ellipsis
    )
  }
}

// -------------------------------------------------------------
// 2. NOTIFICATIONS SHEET DIALOG
// -------------------------------------------------------------
@Composable
fun NotificationSheetDialog(
  notifications: List<NotificationItem>,
  onNotificationClick: (String) -> Unit,
  onMarkAllRead: () -> Unit,
  onDismiss: () -> Unit
) {
  var selectedCategory by remember { mutableStateOf("All") }
  val categories = listOf("All", "Academics", "Attendance", "Exams", "Assignments", "Events", "Placements", "Fees", "Library")

  val filteredList = remember(selectedCategory, notifications) {
    if (selectedCategory == "All") notifications
    else notifications.filter { it.category.equals(selectedCategory, ignoreCase = true) }
  }

  Dialog(
    onDismissRequest = onDismiss,
    properties = DialogProperties(usePlatformDefaultWidth = false)
  ) {
    Surface(
      modifier = Modifier
        .fillMaxWidth(0.95f)
        .fillMaxHeight(0.85f)
        .clip(RoundedCornerShape(24.dp)),
      color = MaterialTheme.colorScheme.surface
    ) {
      Column(
        modifier = Modifier
          .fillMaxSize()
          .padding(16.dp)
      ) {
        // Header
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
              imageVector = Icons.Default.NotificationsActive,
              contentDescription = null,
              tint = PrimaryNavy,
              modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
              text = "Notification Center",
              style = MaterialTheme.typography.titleLarge.copy(
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
              )
            )
          }
          IconButton(onClick = onDismiss) {
            Icon(imageVector = Icons.Default.Close, contentDescription = "Close")
          }
        }

        // Action row: Mark all read + unread badge
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Text(
            text = "${notifications.count { !it.isRead }} unread alerts",
            style = MaterialTheme.typography.bodySmall.copy(color = MaterialTheme.colorScheme.onSurfaceVariant)
          )
          TextButton(onClick = onMarkAllRead) {
            Text("Mark all as read", fontSize = 12.sp, color = PrimaryNavy)
          }
        }

        // Category Filter Chips
        LazyRow(
          horizontalArrangement = Arrangement.spacedBy(8.dp),
          modifier = Modifier.padding(vertical = 6.dp)
        ) {
          items(categories) { cat ->
            val isSelected = selectedCategory == cat
            FilterChip(
              selected = isSelected,
              onClick = { selectedCategory = cat },
              label = { Text(cat, fontSize = 11.sp) },
              colors = FilterChipDefaults.filterChipColors(
                selectedContainerColor = PrimaryNavy,
                selectedLabelColor = Color.White
              )
            )
          }
        }

        Divider(modifier = Modifier.padding(vertical = 8.dp))

        // Notification list
        if (filteredList.isEmpty()) {
          Box(
            modifier = Modifier
              .fillMaxWidth()
              .weight(1f),
            contentAlignment = Alignment.Center
          ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
              Icon(
                imageVector = Icons.Outlined.CheckCircle,
                contentDescription = null,
                tint = EmeraldGreen,
                modifier = Modifier.size(48.dp)
              )
              Spacer(modifier = Modifier.height(8.dp))
              Text("All caught up! No notifications in this category.", color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
          }
        } else {
          LazyColumn(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(10.dp)
          ) {
            items(filteredList) { item ->
              Card(
                onClick = { onNotificationClick(item.id) },
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(
                  containerColor = if (item.isRead) MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
                  else PrimaryNavy.copy(alpha = 0.08f)
                ),
                border = if (!item.isRead) androidx.compose.foundation.BorderStroke(1.dp, PrimaryNavy.copy(alpha = 0.3f)) else null,
                modifier = Modifier.fillMaxWidth().testTag("notification_item_${item.id}")
              ) {
                Row(
                  modifier = Modifier
                    .fillMaxWidth()
                    .padding(14.dp),
                  verticalAlignment = Alignment.Top
                ) {
                  Box(
                    modifier = Modifier
                      .size(36.dp)
                      .clip(CircleShape)
                      .background(
                        when (item.category) {
                          "Attendance" -> CrimsonRed.copy(alpha = 0.15f)
                          "Placements" -> EmeraldGreen.copy(alpha = 0.15f)
                          "Assignments" -> SaffronGold.copy(alpha = 0.15f)
                          else -> PrimaryNavy.copy(alpha = 0.15f)
                        }
                      ),
                    contentAlignment = Alignment.Center
                  ) {
                    Icon(
                      imageVector = when (item.category) {
                        "Attendance" -> Icons.Default.EventBusy
                        "Placements" -> Icons.Default.Work
                        "Assignments" -> Icons.Default.Assignment
                        "Library" -> Icons.Default.MenuBook
                        "Fees" -> Icons.Default.AccountBalanceWallet
                        else -> Icons.Default.Notifications
                      },
                      contentDescription = null,
                      tint = when (item.category) {
                        "Attendance" -> CrimsonRed
                        "Placements" -> EmeraldGreenDark
                        "Assignments" -> SaffronGoldDark
                        else -> PrimaryNavy
                      },
                      modifier = Modifier.size(20.dp)
                    )
                  }

                  Spacer(modifier = Modifier.width(12.dp))

                  Column(modifier = Modifier.weight(1f)) {
                    Row(
                      modifier = Modifier.fillMaxWidth(),
                      horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                      Text(
                        text = item.title,
                        style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                        color = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.weight(1f)
                      )
                      Text(
                        text = item.timestamp,
                        style = MaterialTheme.typography.bodySmall.copy(
                          color = MaterialTheme.colorScheme.onSurfaceVariant,
                          fontSize = 10.sp
                        )
                      )
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                      text = item.message,
                      style = MaterialTheme.typography.bodyMedium.copy(
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontSize = 12.sp
                      )
                    )
                  }
                }
              }
            }
          }
        }
      }
    }
  }
}

// -------------------------------------------------------------
// 3. GLOBAL SMART SEARCH DIALOG
// -------------------------------------------------------------
@Composable
fun GlobalSearchDialog(
  events: List<CollegeEvent>,
  subjects: List<AcademicSubject>,
  placements: List<PlacementDrive>,
  books: List<LibraryBook>,
  clubs: List<CampusClub>,
  onDismiss: () -> Unit,
  onNavigateToSection: (String) -> Unit
) {
  var searchQuery by remember { mutableStateOf("") }

  Dialog(
    onDismissRequest = onDismiss,
    properties = DialogProperties(usePlatformDefaultWidth = false)
  ) {
    Surface(
      modifier = Modifier
        .fillMaxWidth(0.95f)
        .fillMaxHeight(0.85f)
        .clip(RoundedCornerShape(24.dp)),
      color = MaterialTheme.colorScheme.surface
    ) {
      Column(
        modifier = Modifier
          .fillMaxSize()
          .padding(16.dp)
      ) {
        // Search Input Header
        OutlinedTextField(
          value = searchQuery,
          onValueChange = { searchQuery = it },
          placeholder = { Text("Search subjects, faculty, events, jobs, books...", fontSize = 13.sp) },
          leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = PrimaryNavy) },
          trailingIcon = {
            if (searchQuery.isNotEmpty()) {
              IconButton(onClick = { searchQuery = "" }) {
                Icon(Icons.Default.Clear, contentDescription = "Clear")
              }
            }
          },
          singleLine = true,
          shape = RoundedCornerShape(16.dp),
          colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = PrimaryNavy,
            unfocusedBorderColor = MaterialTheme.colorScheme.outline
          ),
          modifier = Modifier.fillMaxWidth().testTag("global_search_input_field")
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Search Results Area
        LazyColumn(
          modifier = Modifier.weight(1f),
          verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
          if (searchQuery.isBlank()) {
            item {
              Text(
                text = "Quick Suggestions",
                style = MaterialTheme.typography.labelMedium.copy(color = PrimaryNavy, fontWeight = FontWeight.Bold),
                modifier = Modifier.padding(vertical = 4.dp)
              )
            }
            items(
              listOf(
                "Data Structures & Algorithms (CS8501)",
                "Zoho Corporation Placement Drive",
                "KRYPTON 2026 National Tech Symposium",
                "Casio Scientific Calculator Lost & Found",
                "Kaveri Hostel Mess Menu",
                "Campus Coding Club (CCC)",
                "Library: Computer Networking (Kurose)"
              )
            ) { suggestion ->
              Row(
                modifier = Modifier
                  .fillMaxWidth()
                  .clip(RoundedCornerShape(10.dp))
                  .clickable { searchQuery = suggestion.split(" ").first() }
                  .padding(horizontal = 8.dp, vertical = 10.dp),
                verticalAlignment = Alignment.CenterVertically
              ) {
                Icon(
                  imageVector = Icons.Default.TrendingUp,
                  contentDescription = null,
                  tint = SaffronGoldDark,
                  modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(12.dp))
                Text(text = suggestion, style = MaterialTheme.typography.bodyMedium)
              }
            }
          } else {
            val q = searchQuery.lowercase()

            // Filtered Subjects
            val matchingSubjects = subjects.filter { it.name.lowercase().contains(q) || it.code.lowercase().contains(q) || it.facultyName.lowercase().contains(q) }
            if (matchingSubjects.isNotEmpty()) {
              item { Text("Academics & Faculty (${matchingSubjects.size})", fontWeight = FontWeight.Bold, color = PrimaryNavy, fontSize = 12.sp) }
              items(matchingSubjects) { subj ->
                SearchResultCard(title = "${subj.code} - ${subj.name}", subtitle = "Faculty: ${subj.facultyName} • Grade: ${subj.semesterGrade}", category = "Academics")
              }
            }

            // Filtered Events
            val matchingEvents = events.filter { it.title.lowercase().contains(q) || it.description.lowercase().contains(q) || it.organizerClub.lowercase().contains(q) }
            if (matchingEvents.isNotEmpty()) {
              item { Text("Campus Events (${matchingEvents.size})", fontWeight = FontWeight.Bold, color = PrimaryNavy, fontSize = 12.sp) }
              items(matchingEvents) { evt ->
                SearchResultCard(title = evt.title, subtitle = "${evt.eventDate} • ${evt.venue} • ${evt.organizerClub}", category = "Events")
              }
            }

            // Filtered Placements
            val matchingPlacements = placements.filter { it.companyName.lowercase().contains(q) || it.role.lowercase().contains(q) || it.requiredSkills.any { s -> s.lowercase().contains(q) } }
            if (matchingPlacements.isNotEmpty()) {
              item { Text("Placements & Jobs (${matchingPlacements.size})", fontWeight = FontWeight.Bold, color = PrimaryNavy, fontSize = 12.sp) }
              items(matchingPlacements) { plc ->
                SearchResultCard(title = "${plc.companyName} - ${plc.role}", subtitle = "CTC: ${plc.ctcLpa} • Min CGPA: ${plc.minCgpa} • Locations: ${plc.jobLocations.joinToString()}", category = "Placements")
              }
            }

            // Filtered Books
            val matchingBooks = books.filter { it.title.lowercase().contains(q) || it.author.lowercase().contains(q) }
            if (matchingBooks.isNotEmpty()) {
              item { Text("Library Catalog (${matchingBooks.size})", fontWeight = FontWeight.Bold, color = PrimaryNavy, fontSize = 12.sp) }
              items(matchingBooks) { bk ->
                SearchResultCard(title = bk.title, subtitle = "Author: ${bk.author} • ${bk.rackNumber} • Available: ${bk.availableCopies}/${bk.totalCopies}", category = "Library")
              }
            }
          }
        }

        Spacer(modifier = Modifier.height(8.dp))
        Button(
          onClick = onDismiss,
          modifier = Modifier.fillMaxWidth(),
          shape = RoundedCornerShape(12.dp),
          colors = ButtonDefaults.buttonColors(containerColor = PrimaryNavy)
        ) {
          Text("Close Search")
        }
      }
    }
  }
}

@Composable
private fun SearchResultCard(title: String, subtitle: String, category: String) {
  Card(
    modifier = Modifier.fillMaxWidth(),
    shape = RoundedCornerShape(12.dp),
    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f))
  ) {
    Column(modifier = Modifier.padding(12.dp)) {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Text(
          text = title,
          style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
          color = MaterialTheme.colorScheme.onSurface,
          modifier = Modifier.weight(1f)
        )
        Surface(
          color = PrimaryNavy.copy(alpha = 0.1f),
          shape = RoundedCornerShape(4.dp)
        ) {
          Text(
            text = category,
            color = PrimaryNavy,
            fontSize = 10.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
          )
        }
      }
      Spacer(modifier = Modifier.height(4.dp))
      Text(
        text = subtitle,
        style = MaterialTheme.typography.bodySmall.copy(color = MaterialTheme.colorScheme.onSurfaceVariant)
      )
    }
  }
}

// -------------------------------------------------------------
// 4. EMERGENCY SOS & SAFETY DIALOG
// -------------------------------------------------------------
@Composable
fun EmergencySosDialog(onDismiss: () -> Unit) {
  Dialog(
    onDismissRequest = onDismiss,
    properties = DialogProperties(usePlatformDefaultWidth = false)
  ) {
    Surface(
      modifier = Modifier
        .fillMaxWidth(0.92f)
        .clip(RoundedCornerShape(24.dp)),
      color = MaterialTheme.colorScheme.surface
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
          Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
              imageVector = Icons.Default.Emergency,
              contentDescription = null,
              tint = CrimsonRed,
              modifier = Modifier.size(26.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
              text = "Campus Safety & SOS",
              style = MaterialTheme.typography.titleLarge.copy(
                fontWeight = FontWeight.Bold,
                color = CrimsonRed
              )
            )
          }
          IconButton(onClick = onDismiss) {
            Icon(imageVector = Icons.Default.Close, contentDescription = "Close")
          }
        }

        Spacer(modifier = Modifier.height(12.dp))

        Surface(
          color = CrimsonRedLight,
          shape = RoundedCornerShape(12.dp),
          modifier = Modifier.fillMaxWidth()
        ) {
          Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
          ) {
            Icon(Icons.Default.Security, contentDescription = null, tint = CrimsonRed)
            Spacer(modifier = Modifier.width(10.dp))
            Text(
              text = "24/7 Rapid Response Unit & Medical First-Aid. Tap to call or alert campus security.",
              color = CrimsonRed,
              fontSize = 11.sp,
              fontWeight = FontWeight.Medium
            )
          }
        }

        Spacer(modifier = Modifier.height(14.dp))

        Column(
          verticalArrangement = Arrangement.spacedBy(10.dp),
          modifier = Modifier.fillMaxWidth()
        ) {
          EmergencyContactItem(name = "Campus Security Control Room", phone = "0422-2591099", location = "Main Gate Post 1")
          EmergencyContactItem(name = "24/7 First-Aid Center & Ambulance", phone = "+91 94430 00108", location = "Near Kaveri Hostel")
          EmergencyContactItem(name = "Anti-Ragging Squad (Toll-Free)", phone = "1800-425-9999", location = "Deanery Office")
          EmergencyContactItem(name = "Women's Safety & Counseling Cell", phone = "+91 98422 99100", location = "Admin Block Room 204")
          EmergencyContactItem(name = "Hostel Chief Warden Emergency", phone = "+91 94433 22110", location = "Warden Office Block A")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
          onClick = onDismiss,
          colors = ButtonDefaults.buttonColors(containerColor = PrimaryNavy),
          shape = RoundedCornerShape(12.dp),
          modifier = Modifier.fillMaxWidth()
        ) {
          Text("Done")
        }
      }
    }
  }
}

@Composable
private fun EmergencyContactItem(name: String, phone: String, location: String) {
  Card(
    modifier = Modifier.fillMaxWidth(),
    shape = RoundedCornerShape(12.dp),
    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
  ) {
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .padding(12.dp),
      verticalAlignment = Alignment.CenterVertically,
      horizontalArrangement = Arrangement.SpaceBetween
    ) {
      Column(modifier = Modifier.weight(1f)) {
        Text(text = name, style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold))
        Text(text = "$location • Tel: $phone", style = MaterialTheme.typography.bodySmall.copy(color = MaterialTheme.colorScheme.onSurfaceVariant))
      }
      Surface(
        color = EmeraldGreen,
        shape = CircleShape,
        modifier = Modifier.size(36.dp)
      ) {
        Box(contentAlignment = Alignment.Center) {
          Icon(imageVector = Icons.Default.Phone, contentDescription = "Call", tint = Color.White, modifier = Modifier.size(18.dp))
        }
      }
    }
  }
}
