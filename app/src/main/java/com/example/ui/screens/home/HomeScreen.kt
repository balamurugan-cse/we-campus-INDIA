package com.example.ui.screens.home

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.mock.CampusMockData
import com.example.data.model.*
import com.example.data.repository.CampusRepository
import com.example.ui.components.MainNavTab
import com.example.ui.theme.*

@Composable
fun HomeScreen(
  repository: CampusRepository,
  onNavigateTab: (MainNavTab) -> Unit,
  onOpenDigitalId: () -> Unit,
  onOpenCampusAi: () -> Unit,
  onOpenServiceSection: (String) -> Unit,
  modifier: Modifier = Modifier
) {
  val currentUser by repository.currentUser.collectAsState()
  val attendanceSummary by repository.attendanceSummary.collectAsState()
  val assignments by repository.assignments.collectAsState()
  val announcements by repository.announcements.collectAsState()
  val events by repository.events.collectAsState()
  val placements by repository.placements.collectAsState()

  LazyColumn(
    modifier = modifier
      .fillMaxSize()
      .background(MaterialTheme.colorScheme.background),
    contentPadding = PaddingValues(bottom = 96.dp, top = 14.dp, start = 16.dp, end = 16.dp),
    verticalArrangement = Arrangement.spacedBy(16.dp)
  ) {
    // -------------------------------------------------------------
    // 1. LIVE CURRENT CLASS BENTO CARD
    // -------------------------------------------------------------
    item {
      LiveClassBentoCard(
        onViewTimetable = { onNavigateTab(MainNavTab.ACADEMICS) }
      )
    }

    // -------------------------------------------------------------
    // 2. CORE BENTO GRID (2-Column Asymmetrical Grid)
    // -------------------------------------------------------------
    item {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
      ) {
        // LEFT COLUMN (Attendance Tall Bento + Library Bento)
        Column(
          modifier = Modifier.weight(1f),
          verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
          // Attendance Bento Tile (Indigo Vibrant Card)
          AttendanceBentoTile(
            percentage = attendanceSummary.overallPercentage,
            onTileClick = { onNavigateTab(MainNavTab.ACADEMICS) }
          )

          // Library Mint Bento Tile
          LibraryMintBentoTile(
            onTileClick = { onOpenServiceSection("Library") }
          )
        }

        // RIGHT COLUMN (Exams Bento + Placements Bento + Cultural Bento)
        Column(
          modifier = Modifier.weight(1f),
          verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
          // Upcoming IA-2 Exam Bento Tile
          ExamBentoTile(
            onTileClick = { onNavigateTab(MainNavTab.ACADEMICS) }
          )

          // Placements Bento Tile
          PlacementBentoTile(
            placement = placements.firstOrNull(),
            onTileClick = { onOpenServiceSection("Placements") }
          )

          // Cultural Fest Peach Bento Tile
          CulturalFestBentoTile(
            onTileClick = { onNavigateTab(MainNavTab.EVENTS) }
          )
        }
      }
    }

    // -------------------------------------------------------------
    // 3. CAMPUS AI ASSISTANT BENTO BANNER (Slate-900 Bento)
    // -------------------------------------------------------------
    item {
      CampusAiBentoBanner(
        onAskAi = onOpenCampusAi
      )
    }

    // -------------------------------------------------------------
    // 4. STUDENT / ROLE PROFILE BENTO CARD
    // -------------------------------------------------------------
    item {
      when (currentUser.role) {
        UserRole.STUDENT -> StudentProfileBentoCard(
          user = currentUser,
          attendance = attendanceSummary,
          onOpenDigitalId = onOpenDigitalId,
          onViewAcademics = { onNavigateTab(MainNavTab.ACADEMICS) }
        )
        UserRole.FACULTY -> FacultyProfileBentoCard(user = currentUser)
        UserRole.ADMIN, UserRole.PLACEMENT_TEAM -> AdminProfileBentoCard(user = currentUser)
        UserRole.CLUB_LEAD -> ClubLeadProfileBentoCard(user = currentUser)
      }
    }

    // -------------------------------------------------------------
    // 5. QUICK CAMPUS HUB BENTO ROW (Fees, Canteen, Bus, Grievance)
    // -------------------------------------------------------------
    item {
      Column {
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Text(
            text = "Campus Hub & Services",
            style = MaterialTheme.typography.titleMedium.copy(
              fontWeight = FontWeight.Bold,
              fontSize = 17.sp,
              color = BentoSlateDark
            )
          )
          TextButton(onClick = { onNavigateTab(MainNavTab.SERVICES) }) {
            Text("All Hubs", fontSize = 12.sp, color = BentoIndigo, fontWeight = FontWeight.Bold)
          }
        }

        Spacer(modifier = Modifier.height(6.dp))

        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
          QuickBentoHubTile(
            title = "Fees & Dues",
            subtitle = "₹6,000 Due",
            icon = Icons.Default.AccountBalanceWallet,
            iconColor = BentoOrange,
            bgColor = BentoOrangeBg,
            modifier = Modifier.weight(1f),
            onClick = { onOpenServiceSection("Fees") }
          )
          QuickBentoHubTile(
            title = "Canteen Food",
            subtitle = "Token #42",
            icon = Icons.Default.Fastfood,
            iconColor = BentoEmerald,
            bgColor = BentoMintBg,
            modifier = Modifier.weight(1f),
            onClick = { onOpenServiceSection("Canteen") }
          )
          QuickBentoHubTile(
            title = "Campus Bus",
            subtitle = "Route 12",
            icon = Icons.Default.DirectionsBus,
            iconColor = BentoIndigo,
            bgColor = BentoIndigoLight,
            modifier = Modifier.weight(1f),
            onClick = { onOpenServiceSection("Transport") }
          )
          QuickBentoHubTile(
            title = "Grievance",
            subtitle = "UGC Cell",
            icon = Icons.Default.Gavel,
            iconColor = CrimsonRed,
            bgColor = CrimsonRedLight,
            modifier = Modifier.weight(1f),
            onClick = { onOpenServiceSection("Grievance") }
          )
        }
      }
    }

    // -------------------------------------------------------------
    // 6. TIMETABLE TODAY BENTO CAROUSEL
    // -------------------------------------------------------------
    item {
      Column {
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
              modifier = Modifier
                .size(28.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(BentoIndigoLight),
              contentAlignment = Alignment.Center
            ) {
              Icon(Icons.Default.AccessTime, contentDescription = null, tint = BentoIndigo, modifier = Modifier.size(16.dp))
            }
            Spacer(modifier = Modifier.width(8.dp))
            Text(
              text = if (currentUser.role == UserRole.FACULTY) "My Teaching Schedule" else "Today's Schedule (Wednesday)",
              style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                color = BentoSlateDark
              )
            )
          }
          TextButton(onClick = { onNavigateTab(MainNavTab.ACADEMICS) }) {
            Text("Full Week", fontSize = 12.sp, color = BentoIndigo, fontWeight = FontWeight.Bold)
          }
        }

        Spacer(modifier = Modifier.height(8.dp))

        LazyRow(
          horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
          items(CampusMockData.sampleTimetableToday) { slot ->
            BentoTimetableCard(slot = slot, isCurrentClass = slot.periodNumber == 2)
          }
        }
      }
    }

    // -------------------------------------------------------------
    // 7. PENDING ASSIGNMENTS (Bento Card List)
    // -------------------------------------------------------------
    item {
      Column {
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
              modifier = Modifier
                .size(28.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(BentoMintBg),
              contentAlignment = Alignment.Center
            ) {
              Icon(Icons.Default.Assignment, contentDescription = null, tint = BentoEmeraldDark, modifier = Modifier.size(16.dp))
            }
            Spacer(modifier = Modifier.width(8.dp))
            Text(
              text = "Pending Submissions",
              style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                color = BentoSlateDark
              )
            )
          }
          TextButton(onClick = { onNavigateTab(MainNavTab.ACADEMICS) }) {
            Text("View All (${assignments.size})", fontSize = 12.sp, color = BentoIndigo, fontWeight = FontWeight.Bold)
          }
        }

        Spacer(modifier = Modifier.height(6.dp))

        assignments.filter { !it.isSubmitted }.take(2).forEach { asg ->
          var showSubmitDialog by remember { mutableStateOf(false) }

          Card(
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            border = BorderStroke(1.dp, CardBorderLight),
            elevation = CardDefaults.cardElevation(0.dp),
            modifier = Modifier
              .fillMaxWidth()
              .padding(vertical = 4.dp)
          ) {
            Column(modifier = Modifier.padding(16.dp)) {
              Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
              ) {
                Surface(
                  color = BentoIndigoLight,
                  shape = RoundedCornerShape(8.dp)
                ) {
                  Text(
                    text = asg.subjectName,
                    color = BentoIndigo,
                    fontWeight = FontWeight.Bold,
                    fontSize = 11.sp,
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                  )
                }

                Surface(
                  color = CrimsonRedLight,
                  shape = RoundedCornerShape(8.dp)
                ) {
                  Text(
                    text = "Due: ${asg.dueDate}",
                    color = CrimsonRed,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                  )
                }
              }

              Spacer(modifier = Modifier.height(8.dp))

              Text(
                text = asg.title,
                style = MaterialTheme.typography.titleSmall.copy(
                  fontWeight = FontWeight.Bold,
                  fontSize = 14.sp,
                  color = BentoSlateDark
                )
              )

              Spacer(modifier = Modifier.height(10.dp))

              Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
              ) {
                Text(
                  text = "Faculty: ${asg.facultyName}",
                  style = MaterialTheme.typography.bodySmall.copy(color = BentoSlateMuted, fontSize = 12.sp)
                )

                Button(
                  onClick = { showSubmitDialog = true },
                  colors = ButtonDefaults.buttonColors(containerColor = BentoIndigo),
                  shape = RoundedCornerShape(12.dp),
                  contentPadding = PaddingValues(horizontal = 14.dp, vertical = 6.dp),
                  modifier = Modifier.testTag("submit_assignment_button_${asg.id}")
                ) {
                  Icon(Icons.Default.CloudUpload, contentDescription = null, modifier = Modifier.size(15.dp))
                  Spacer(modifier = Modifier.width(6.dp))
                  Text("Submit PDF", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }
              }
            }
          }

          if (showSubmitDialog) {
            AlertDialog(
              onDismissRequest = { showSubmitDialog = false },
              shape = RoundedCornerShape(24.dp),
              title = { Text("Submit Assignment", fontWeight = FontWeight.Bold) },
              text = {
                Column {
                  Text(text = asg.title, fontSize = 13.sp, fontWeight = FontWeight.Medium)
                  Spacer(modifier = Modifier.height(12.dp))
                  Surface(
                    color = BentoMintBg,
                    shape = RoundedCornerShape(14.dp),
                    border = BorderStroke(1.dp, BentoMintBorder),
                    modifier = Modifier.fillMaxWidth()
                  ) {
                    Row(
                      modifier = Modifier.padding(12.dp),
                      verticalAlignment = Alignment.CenterVertically
                    ) {
                      Icon(Icons.Default.PictureAsPdf, contentDescription = null, tint = BentoEmeraldDark)
                      Spacer(modifier = Modifier.width(8.dp))
                      Text("${currentUser.rollNumber}_Assignment.pdf (1.4 MB)", fontSize = 12.sp, color = BentoEmeraldDark, fontWeight = FontWeight.Bold)
                    }
                  }
                }
              },
              confirmButton = {
                Button(
                  onClick = {
                    repository.submitAssignment(asg.id, "${currentUser.rollNumber}_Assignment.pdf")
                    showSubmitDialog = false
                  },
                  colors = ButtonDefaults.buttonColors(containerColor = BentoIndigo),
                  shape = RoundedCornerShape(12.dp)
                ) {
                  Text("Confirm Upload", fontWeight = FontWeight.Bold)
                }
              },
              dismissButton = {
                TextButton(onClick = { showSubmitDialog = false }) {
                  Text("Cancel", color = BentoSlateMuted)
                }
              }
            )
          }
        }
      }
    }

    // -------------------------------------------------------------
    // 8. CAMPUS CIRCULARS & ANNOUNCEMENTS (Bento Cards)
    // -------------------------------------------------------------
    item {
      Column {
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
              modifier = Modifier
                .size(28.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(BentoOrangeBg),
              contentAlignment = Alignment.Center
            ) {
              Icon(Icons.Default.Campaign, contentDescription = null, tint = BentoOrangeDark, modifier = Modifier.size(16.dp))
            }
            Spacer(modifier = Modifier.width(8.dp))
            Text(
              text = "Campus Circulars",
              style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                color = BentoSlateDark
              )
            )
          }
        }

        Spacer(modifier = Modifier.height(8.dp))

        announcements.forEach { ann ->
          Card(
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            border = BorderStroke(1.dp, if (ann.isPinned) BentoIndigoBorder else CardBorderLight),
            elevation = CardDefaults.cardElevation(0.dp),
            modifier = Modifier
              .fillMaxWidth()
              .padding(vertical = 4.dp)
          ) {
            Column(modifier = Modifier.padding(16.dp)) {
              Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
              ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                  if (ann.isPinned) {
                    Surface(
                      color = BentoIndigoLight,
                      shape = RoundedCornerShape(6.dp)
                    ) {
                      Row(
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                        verticalAlignment = Alignment.CenterVertically
                      ) {
                        Icon(Icons.Default.PushPin, contentDescription = "Pinned", tint = BentoIndigo, modifier = Modifier.size(12.dp))
                        Spacer(modifier = Modifier.width(2.dp))
                        Text("PINNED", color = BentoIndigo, fontSize = 9.sp, fontWeight = FontWeight.Bold)
                      }
                    }
                    Spacer(modifier = Modifier.width(6.dp))
                  }
                  Surface(
                    color = Color(0xFFF1F5F9),
                    shape = RoundedCornerShape(6.dp)
                  ) {
                    Text(
                      text = ann.category.uppercase(),
                      color = BentoSlateDark,
                      fontSize = 9.sp,
                      fontWeight = FontWeight.Bold,
                      modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                  }
                }

                Text(
                  text = ann.publishDate,
                  color = BentoSlateMuted,
                  fontSize = 11.sp,
                  fontWeight = FontWeight.Medium
                )
              }

              Spacer(modifier = Modifier.height(8.dp))

              Text(
                text = ann.title,
                style = MaterialTheme.typography.titleSmall.copy(
                  fontWeight = FontWeight.Bold,
                  fontSize = 14.sp,
                  color = BentoSlateDark
                )
              )

              Spacer(modifier = Modifier.height(6.dp))

              Text(
                text = ann.content,
                style = MaterialTheme.typography.bodySmall.copy(
                  color = BentoSlateMedium,
                  lineHeight = 18.sp,
                  fontSize = 12.sp
                )
              )

              Spacer(modifier = Modifier.height(8.dp))

              Text(
                text = "Issued by: ${ann.issuedBy}",
                style = MaterialTheme.typography.bodySmall.copy(
                  color = BentoIndigo,
                  fontSize = 11.sp,
                  fontWeight = FontWeight.SemiBold
                )
              )
            }
          }
        }
      }
    }
  }
}

// -------------------------------------------------------------
// BENTO TILES DEFINITIONS
// -------------------------------------------------------------

@Composable
private fun LiveClassBentoCard(onViewTimetable: () -> Unit) {
  Card(
    shape = RoundedCornerShape(24.dp),
    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
    border = BorderStroke(1.dp, CardBorderLight),
    elevation = CardDefaults.cardElevation(0.dp),
    modifier = Modifier
      .fillMaxWidth()
      .clickable(onClick = onViewTimetable)
      .testTag("home_live_class_bento_card")
  ) {
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .padding(18.dp),
      horizontalArrangement = Arrangement.SpaceBetween,
      verticalAlignment = Alignment.CenterVertically
    ) {
      Column(modifier = Modifier.weight(1f)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
          Box(
            modifier = Modifier
              .size(8.dp)
              .clip(CircleShape)
              .background(BentoEmerald)
          )
          Spacer(modifier = Modifier.width(6.dp))
          Text(
            text = "CURRENT CLASS • ROOM 402",
            style = MaterialTheme.typography.labelSmall.copy(
              fontWeight = FontWeight.Bold,
              fontSize = 10.sp,
              letterSpacing = 1.sp,
              color = BentoSlateMuted
            )
          )
        }

        Spacer(modifier = Modifier.height(4.dp))

        Text(
          text = "Data Structures & Algorithms",
          style = MaterialTheme.typography.titleMedium.copy(
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp,
            color = BentoSlateDark
          )
        )

        Spacer(modifier = Modifier.height(2.dp))

        Text(
          text = "Prof. S. Karthik • 09:00 - 10:30 AM",
          style = MaterialTheme.typography.bodySmall.copy(
            color = BentoSlateMedium,
            fontSize = 12.sp
          )
        )
      }

      // Circular Countdown Badge (24m left)
      Box(
        modifier = Modifier
          .size(52.dp)
          .clip(CircleShape)
          .background(BentoMintBg)
          .border(2.dp, BentoEmerald, CircleShape),
        contentAlignment = Alignment.Center
      ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
          Text(
            text = "24m",
            color = BentoEmeraldDark,
            fontSize = 13.sp,
            fontWeight = FontWeight.Bold,
            lineHeight = 14.sp
          )
          Text(
            text = "left",
            color = BentoEmeraldDark,
            fontSize = 9.sp,
            fontWeight = FontWeight.SemiBold,
            lineHeight = 10.sp
          )
        }
      }
    }
  }
}

@Composable
private fun AttendanceBentoTile(
  percentage: Double,
  onTileClick: () -> Unit
) {
  Card(
    shape = RoundedCornerShape(24.dp),
    colors = CardDefaults.cardColors(containerColor = BentoIndigo),
    elevation = CardDefaults.cardElevation(2.dp),
    modifier = Modifier
      .fillMaxWidth()
      .height(180.dp)
      .clickable(onClick = onTileClick)
      .testTag("bento_attendance_tile")
  ) {
    Column(
      modifier = Modifier
        .fillMaxSize()
        .padding(16.dp),
      verticalArrangement = Arrangement.SpaceBetween
    ) {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Box(
          modifier = Modifier
            .size(36.dp)
            .clip(CircleShape)
            .background(Color.White.copy(alpha = 0.2f)),
          contentAlignment = Alignment.Center
        ) {
          Icon(
            imageVector = Icons.Default.BarChart,
            contentDescription = null,
            tint = Color.White,
            modifier = Modifier.size(20.dp)
          )
        }

        Surface(
          color = Color.White.copy(alpha = 0.2f),
          shape = RoundedCornerShape(12.dp)
        ) {
          Text(
            text = "Target: 75%",
            color = Color.White,
            fontSize = 10.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
          )
        }
      }

      Column {
        Text(
          text = "${percentage.toInt()}%",
          style = MaterialTheme.typography.displayMedium.copy(
            fontWeight = FontWeight.Bold,
            fontSize = 32.sp,
            color = Color.White,
            lineHeight = 36.sp
          )
        )
        Text(
          text = "ATTENDANCE",
          style = MaterialTheme.typography.labelSmall.copy(
            color = Color.White.copy(alpha = 0.8f),
            fontWeight = FontWeight.Bold,
            fontSize = 10.sp,
            letterSpacing = 1.sp
          )
        )
        Text(
          text = if (percentage >= 75.0) "✓ Safe (AICTE Clear)" else "⚠ Shortage Alert",
          style = MaterialTheme.typography.bodySmall.copy(
            color = if (percentage >= 75.0) SaffronGoldLight else CrimsonRedLight,
            fontSize = 10.sp,
            fontWeight = FontWeight.SemiBold
          )
        )
      }
    }
  }
}

@Composable
private fun LibraryMintBentoTile(onTileClick: () -> Unit) {
  Card(
    shape = RoundedCornerShape(24.dp),
    colors = CardDefaults.cardColors(containerColor = BentoMintBg),
    border = BorderStroke(1.dp, BentoMintBorder),
    elevation = CardDefaults.cardElevation(0.dp),
    modifier = Modifier
      .fillMaxWidth()
      .clickable(onClick = onTileClick)
      .testTag("bento_library_tile")
  ) {
    Column(
      modifier = Modifier.padding(16.dp)
    ) {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Box(
          modifier = Modifier
            .size(32.dp)
            .clip(RoundedCornerShape(10.dp))
            .background(BentoEmerald),
          contentAlignment = Alignment.Center
        ) {
          Icon(Icons.Default.MenuBook, contentDescription = null, tint = Color.White, modifier = Modifier.size(18.dp))
        }

        Surface(
          color = BentoEmerald.copy(alpha = 0.15f),
          shape = RoundedCornerShape(8.dp)
        ) {
          Text(
            text = "LIBRARY",
            color = BentoEmeraldDark,
            fontSize = 9.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
          )
        }
      }

      Spacer(modifier = Modifier.height(10.dp))

      Text(
        text = "2 Books Due",
        fontWeight = FontWeight.Bold,
        fontSize = 14.sp,
        color = BentoEmeraldDark
      )
      Text(
        text = "Renew in 1-tap",
        fontSize = 11.sp,
        color = BentoEmeraldDark.copy(alpha = 0.8f)
      )
    }
  }
}

@Composable
private fun ExamBentoTile(onTileClick: () -> Unit) {
  Card(
    shape = RoundedCornerShape(24.dp),
    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
    border = BorderStroke(1.dp, CardBorderLight),
    elevation = CardDefaults.cardElevation(0.dp),
    modifier = Modifier
      .fillMaxWidth()
      .clickable(onClick = onTileClick)
      .testTag("bento_exam_tile")
  ) {
    Column(modifier = Modifier.padding(14.dp)) {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Box(
          modifier = Modifier
            .size(32.dp)
            .clip(RoundedCornerShape(10.dp))
            .background(BentoOrangeBg),
          contentAlignment = Alignment.Center
        ) {
          Icon(Icons.Default.EventNote, contentDescription = null, tint = BentoOrangeDark, modifier = Modifier.size(18.dp))
        }

        Surface(
          color = BentoOrangeBg,
          shape = RoundedCornerShape(8.dp)
        ) {
          Text(
            text = "UPCOMING IA",
            color = BentoOrangeDark,
            fontSize = 9.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
          )
        }
      }

      Spacer(modifier = Modifier.height(8.dp))

      Text(
        text = "Model Exam-II",
        fontWeight = FontWeight.Bold,
        fontSize = 13.sp,
        color = BentoSlateDark,
        maxLines = 1
      )
      Text(
        text = "14 Nov • Hall A-204",
        fontSize = 11.sp,
        color = BentoSlateMuted
      )
    }
  }
}

@Composable
private fun PlacementBentoTile(placement: PlacementDrive?, onTileClick: () -> Unit) {
  Card(
    shape = RoundedCornerShape(24.dp),
    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
    border = BorderStroke(1.dp, CardBorderLight),
    elevation = CardDefaults.cardElevation(0.dp),
    modifier = Modifier
      .fillMaxWidth()
      .clickable(onClick = onTileClick)
      .testTag("bento_placement_tile")
  ) {
    Column(modifier = Modifier.padding(14.dp)) {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Box(
          modifier = Modifier
            .size(32.dp)
            .clip(RoundedCornerShape(10.dp))
            .background(BentoMintBg),
          contentAlignment = Alignment.Center
        ) {
          Icon(Icons.Default.Work, contentDescription = null, tint = BentoEmeraldDark, modifier = Modifier.size(18.dp))
        }

        Surface(
          color = BentoMintBg,
          shape = RoundedCornerShape(8.dp)
        ) {
          Text(
            text = "CAREERS",
            color = BentoEmeraldDark,
            fontSize = 9.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
          )
        }
      }

      Spacer(modifier = Modifier.height(8.dp))

      Text(
        text = placement?.companyName ?: "Zoho Corp",
        fontWeight = FontWeight.Bold,
        fontSize = 13.sp,
        color = BentoSlateDark,
        maxLines = 1
      )
      Text(
        text = "₹ ${placement?.ctcLpa ?: "44 LPA"} • Open",
        fontSize = 11.sp,
        fontWeight = FontWeight.Bold,
        color = BentoEmeraldDark
      )
    }
  }
}

@Composable
private fun CulturalFestBentoTile(onTileClick: () -> Unit) {
  Card(
    shape = RoundedCornerShape(24.dp),
    colors = CardDefaults.cardColors(containerColor = BentoOrangeBg),
    border = BorderStroke(1.dp, BentoOrangeBorder),
    elevation = CardDefaults.cardElevation(0.dp),
    modifier = Modifier
      .fillMaxWidth()
      .clickable(onClick = onTileClick)
      .testTag("bento_cultural_tile")
  ) {
    Column(modifier = Modifier.padding(14.dp)) {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Box(
          modifier = Modifier
            .size(32.dp)
            .clip(RoundedCornerShape(10.dp))
            .background(BentoOrangeDark),
          contentAlignment = Alignment.Center
        ) {
          Icon(Icons.Default.Celebration, contentDescription = null, tint = Color.White, modifier = Modifier.size(18.dp))
        }

        Surface(
          color = BentoOrangeDark.copy(alpha = 0.15f),
          shape = RoundedCornerShape(8.dp)
        ) {
          Text(
            text = "FESTIVALS",
            color = BentoOrangeDark,
            fontSize = 9.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
          )
        }
      }

      Spacer(modifier = Modifier.height(8.dp))

      Text(
        text = "Sangam '26 Fest",
        fontWeight = FontWeight.Bold,
        fontSize = 13.sp,
        color = BentoOrangeDark,
        maxLines = 1
      )
      Text(
        text = "28 Oct • Register Now",
        fontSize = 11.sp,
        color = BentoOrangeDark.copy(alpha = 0.8f),
        fontWeight = FontWeight.SemiBold
      )
    }
  }
}

@Composable
private fun CampusAiBentoBanner(onAskAi: () -> Unit) {
  Card(
    shape = RoundedCornerShape(24.dp),
    colors = CardDefaults.cardColors(containerColor = BentoSlateDark),
    elevation = CardDefaults.cardElevation(2.dp),
    modifier = Modifier
      .fillMaxWidth()
      .clickable(onClick = onAskAi)
      .testTag("bento_ai_banner")
  ) {
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .padding(18.dp),
      horizontalArrangement = Arrangement.SpaceBetween,
      verticalAlignment = Alignment.CenterVertically
    ) {
      Column(modifier = Modifier.weight(1f)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
          Icon(Icons.Default.AutoAwesome, contentDescription = null, tint = SaffronGold, modifier = Modifier.size(18.dp))
          Spacer(modifier = Modifier.width(6.dp))
          Text(
            text = "Campus AI Assistant",
            style = MaterialTheme.typography.titleMedium.copy(
              fontWeight = FontWeight.Bold,
              color = Color.White,
              fontSize = 15.sp
            )
          )
        }

        Spacer(modifier = Modifier.height(4.dp))

        Text(
          text = "Ask questions regarding internal marks, hall tickets, or canteen tokens instantly.",
          style = MaterialTheme.typography.bodySmall.copy(
            color = Color.White.copy(alpha = 0.75f),
            fontSize = 12.sp,
            lineHeight = 16.sp
          )
        )
      }

      Spacer(modifier = Modifier.width(12.dp))

      Button(
        onClick = onAskAi,
        colors = ButtonDefaults.buttonColors(containerColor = BentoIndigo),
        shape = RoundedCornerShape(14.dp),
        contentPadding = PaddingValues(horizontal = 14.dp, vertical = 8.dp)
      ) {
        Text("Ask AI", fontSize = 12.sp, fontWeight = FontWeight.Bold)
      }
    }
  }
}

// -------------------------------------------------------------
// PROFILE BENTO CARDS
// -------------------------------------------------------------

@Composable
private fun StudentProfileBentoCard(
  user: UserProfile,
  attendance: AttendanceSummary,
  onOpenDigitalId: () -> Unit,
  onViewAcademics: () -> Unit
) {
  Card(
    shape = RoundedCornerShape(24.dp),
    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
    border = BorderStroke(1.dp, CardBorderLight),
    elevation = CardDefaults.cardElevation(0.dp),
    modifier = Modifier
      .fillMaxWidth()
      .testTag("student_hero_card")
  ) {
    Column(modifier = Modifier.padding(18.dp)) {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
          Box(
            modifier = Modifier
              .size(48.dp)
              .clip(CircleShape)
              .background(BentoIndigoLight)
              .border(2.dp, BentoIndigo, CircleShape)
              .clickable(onClick = onOpenDigitalId),
            contentAlignment = Alignment.Center
          ) {
            Text(
              text = user.avatarInitials,
              color = BentoIndigo,
              fontWeight = FontWeight.Bold,
              fontSize = 16.sp
            )
          }

          Spacer(modifier = Modifier.width(12.dp))

          Column {
            Text(
              text = user.fullName,
              style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                color = BentoSlateDark
              )
            )
            Text(
              text = "Roll: ${user.rollNumber} • ${user.section}",
              style = MaterialTheme.typography.bodySmall.copy(
                color = BentoSlateMuted,
                fontSize = 12.sp
              )
            )
          }
        }

        OutlinedButton(
          onClick = onOpenDigitalId,
          shape = RoundedCornerShape(12.dp),
          border = BorderStroke(1.dp, BentoIndigoBorder),
          contentPadding = PaddingValues(horizontal = 10.dp, vertical = 6.dp),
          modifier = Modifier.testTag("hero_digital_id_button")
        ) {
          Icon(Icons.Default.Badge, contentDescription = null, tint = BentoIndigo, modifier = Modifier.size(16.dp))
          Spacer(modifier = Modifier.width(4.dp))
          Text("ID Card", fontSize = 11.sp, color = BentoIndigo, fontWeight = FontWeight.Bold)
        }
      }

      Spacer(modifier = Modifier.height(14.dp))

      // Stats Bento Strip
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .clip(RoundedCornerShape(16.dp))
          .background(Color(0xFFF8FAFC))
          .border(1.dp, CardBorderLight, RoundedCornerShape(16.dp))
          .padding(vertical = 12.dp, horizontal = 14.dp),
        horizontalArrangement = Arrangement.SpaceAround
      ) {
        ProfileStatBlock(label = "Overall CGPA", value = "${user.cgpa}", subtext = "Rank #4 Dept")
        Box(modifier = Modifier.width(1.dp).height(32.dp).background(CardBorderLight))
        ProfileStatBlock(label = "Current SGPA", value = "${user.currentSgpa}", subtext = "Sem IV")
        Box(modifier = Modifier.width(1.dp).height(32.dp).background(CardBorderLight))
        ProfileStatBlock(label = "Attendance", value = "${attendance.overallPercentage}%", subtext = "AICTE Safe")
      }
    }
  }
}

@Composable
private fun FacultyProfileBentoCard(user: UserProfile) {
  Card(
    shape = RoundedCornerShape(24.dp),
    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
    border = BorderStroke(1.dp, CardBorderLight),
    modifier = Modifier.fillMaxWidth()
  ) {
    Column(modifier = Modifier.padding(18.dp)) {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Column {
          Text(text = "FACULTY PORTAL", color = BentoEmeraldDark, fontSize = 10.sp, fontWeight = FontWeight.Bold, letterSpacing = 1.sp)
          Text(text = user.fullName, color = BentoSlateDark, fontSize = 17.sp, fontWeight = FontWeight.Bold)
          Text(text = "${user.designation} • Cabin: ${user.cabinNumber}", color = BentoSlateMuted, fontSize = 12.sp)
        }
        Surface(color = BentoMintBg, shape = RoundedCornerShape(10.dp)) {
          Text(text = "FACULTY", color = BentoEmeraldDark, fontSize = 11.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp))
        }
      }
      Spacer(modifier = Modifier.height(14.dp))
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .clip(RoundedCornerShape(16.dp))
          .background(Color(0xFFF8FAFC))
          .padding(12.dp),
        horizontalArrangement = Arrangement.SpaceAround
      ) {
        ProfileStatBlock(label = "Assigned Classes", value = "4", subtext = "3 Lectures Today")
        ProfileStatBlock(label = "Pending Grading", value = "18", subtext = "Assignments")
        ProfileStatBlock(label = "Mentee Students", value = "24", subtext = "CSE III A")
      }
    }
  }
}

@Composable
private fun AdminProfileBentoCard(user: UserProfile) {
  Card(
    shape = RoundedCornerShape(24.dp),
    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
    border = BorderStroke(1.dp, CardBorderLight),
    modifier = Modifier.fillMaxWidth()
  ) {
    Column(modifier = Modifier.padding(18.dp)) {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Column {
          Text(text = "ADMINISTRATION CONSOLE", color = BentoIndigo, fontSize = 10.sp, fontWeight = FontWeight.Bold, letterSpacing = 1.sp)
          Text(text = user.fullName, color = BentoSlateDark, fontSize = 17.sp, fontWeight = FontWeight.Bold)
          Text(text = user.affiliation, color = BentoSlateMuted, fontSize = 12.sp)
        }
        Surface(color = BentoIndigoLight, shape = RoundedCornerShape(10.dp)) {
          Text(text = "ADMIN", color = BentoIndigo, fontSize = 11.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp))
        }
      }
      Spacer(modifier = Modifier.height(14.dp))
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .clip(RoundedCornerShape(16.dp))
          .background(Color(0xFFF8FAFC))
          .padding(12.dp),
        horizontalArrangement = Arrangement.SpaceAround
      ) {
        ProfileStatBlock(label = "Total Students", value = "4,850", subtext = "11 Depts")
        ProfileStatBlock(label = "Avg Attendance", value = "87.2%", subtext = "Campus")
        ProfileStatBlock(label = "Placements", value = "91.4%", subtext = "Batch '26")
      }
    }
  }
}

@Composable
private fun ClubLeadProfileBentoCard(user: UserProfile) {
  Card(
    shape = RoundedCornerShape(24.dp),
    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
    border = BorderStroke(1.dp, CardBorderLight),
    modifier = Modifier.fillMaxWidth()
  ) {
    Column(modifier = Modifier.padding(18.dp)) {
      Text(text = "STUDENT ACTIVITIES & CLUBS", color = BentoOrangeDark, fontSize = 10.sp, fontWeight = FontWeight.Bold, letterSpacing = 1.sp)
      Text(text = user.fullName, color = BentoSlateDark, fontSize = 17.sp, fontWeight = FontWeight.Bold)
      Text(text = "Campus Coding Club & Cultural Council Lead", color = BentoSlateMuted, fontSize = 12.sp)
    }
  }
}

@Composable
private fun ProfileStatBlock(label: String, value: String, subtext: String) {
  Column(horizontalAlignment = Alignment.CenterHorizontally) {
    Text(text = label, color = BentoSlateMuted, fontSize = 10.sp, fontWeight = FontWeight.Medium)
    Text(text = value, color = BentoSlateDark, fontSize = 17.sp, fontWeight = FontWeight.Bold)
    Text(text = subtext, color = BentoIndigo, fontSize = 10.sp, fontWeight = FontWeight.SemiBold)
  }
}

@Composable
private fun BentoTimetableCard(slot: TimetableSlot, isCurrentClass: Boolean) {
  Card(
    shape = RoundedCornerShape(20.dp),
    colors = CardDefaults.cardColors(
      containerColor = if (isCurrentClass) BentoIndigo else MaterialTheme.colorScheme.surface
    ),
    border = if (isCurrentClass) null else BorderStroke(1.dp, CardBorderLight),
    elevation = CardDefaults.cardElevation(if (isCurrentClass) 2.dp else 0.dp),
    modifier = Modifier
      .width(185.dp)
      .testTag("timetable_slot_${slot.periodNumber}")
  ) {
    Column(modifier = Modifier.padding(14.dp)) {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Surface(
          color = if (isCurrentClass) Color.White.copy(alpha = 0.2f) else BentoIndigoLight,
          shape = RoundedCornerShape(6.dp)
        ) {
          Text(
            text = "PERIOD ${slot.periodNumber}",
            color = if (isCurrentClass) Color.White else BentoIndigo,
            fontSize = 9.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
          )
        }

        if (isCurrentClass) {
          Surface(
            color = BentoEmerald,
            shape = RoundedCornerShape(6.dp)
          ) {
            Text(
              text = "LIVE",
              color = Color.White,
              fontSize = 8.sp,
              fontWeight = FontWeight.Bold,
              modifier = Modifier.padding(horizontal = 5.dp, vertical = 2.dp)
            )
          }
        }
      }

      Spacer(modifier = Modifier.height(8.dp))

      Text(
        text = slot.subjectName,
        style = MaterialTheme.typography.titleSmall.copy(
          fontWeight = FontWeight.Bold,
          fontSize = 13.sp,
          color = if (isCurrentClass) Color.White else BentoSlateDark
        ),
        maxLines = 2,
        overflow = TextOverflow.Ellipsis
      )

      Spacer(modifier = Modifier.height(4.dp))

      Text(
        text = slot.timeSlot,
        style = MaterialTheme.typography.bodySmall.copy(
          color = if (isCurrentClass) Color.White.copy(alpha = 0.8f) else BentoSlateMuted,
          fontSize = 11.sp
        )
      )

      Spacer(modifier = Modifier.height(6.dp))

      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
      ) {
        Text(
          text = slot.roomNumber,
          style = MaterialTheme.typography.bodySmall.copy(
            color = if (isCurrentClass) SaffronGoldLight else BentoIndigo,
            fontWeight = FontWeight.Bold,
            fontSize = 11.sp
          )
        )
        Text(
          text = if (slot.isLab) "LAB" else "THEORY",
          style = MaterialTheme.typography.bodySmall.copy(
            color = if (isCurrentClass) Color.White.copy(alpha = 0.7f) else BentoSlateMuted,
            fontSize = 10.sp,
            fontWeight = FontWeight.SemiBold
          )
        )
      }
    }
  }
}

@Composable
private fun QuickBentoHubTile(
  title: String,
  subtitle: String,
  icon: androidx.compose.ui.graphics.vector.ImageVector,
  iconColor: Color,
  bgColor: Color,
  modifier: Modifier = Modifier,
  onClick: () -> Unit
) {
  Card(
    onClick = onClick,
    shape = RoundedCornerShape(20.dp),
    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
    border = BorderStroke(1.dp, CardBorderLight),
    elevation = CardDefaults.cardElevation(0.dp),
    modifier = modifier.testTag("quick_service_${title.lowercase().replace(" ", "_")}")
  ) {
    Column(
      modifier = Modifier
        .fillMaxWidth()
        .padding(12.dp),
      horizontalAlignment = Alignment.CenterHorizontally
    ) {
      Box(
        modifier = Modifier
          .size(40.dp)
          .clip(CircleShape)
          .background(bgColor),
        contentAlignment = Alignment.Center
      ) {
        Icon(imageVector = icon, contentDescription = title, tint = iconColor, modifier = Modifier.size(20.dp))
      }
      Spacer(modifier = Modifier.height(8.dp))
      Text(
        text = title,
        style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold, fontSize = 11.sp, color = BentoSlateDark),
        textAlign = TextAlign.Center,
        maxLines = 1
      )
      Text(
        text = subtitle,
        style = MaterialTheme.typography.bodySmall.copy(color = BentoSlateMuted, fontSize = 10.sp),
        textAlign = TextAlign.Center,
        maxLines = 1
      )
    }
  }
}
