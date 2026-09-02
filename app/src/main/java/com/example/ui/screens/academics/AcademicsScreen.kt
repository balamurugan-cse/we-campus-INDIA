package com.example.ui.screens.academics

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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.mock.CampusMockData
import com.example.data.model.*
import com.example.data.repository.CampusRepository
import com.example.ui.theme.*

enum class AcademicSubTab(val title: String) {
  ATTENDANCE("Attendance"),
  TIMETABLE("Timetable"),
  RESULTS("Marks & CGPA"),
  ASSIGNMENTS("Assignments"),
  EXAMS("Exams"),
  CALENDAR("Calendar")
}

@Composable
fun AcademicsScreen(
  repository: CampusRepository,
  modifier: Modifier = Modifier
) {
  var selectedTab by remember { mutableStateOf(AcademicSubTab.ATTENDANCE) }
  val currentUser by repository.currentUser.collectAsState()
  val attendanceSummary by repository.attendanceSummary.collectAsState()
  val assignments by repository.assignments.collectAsState()

  Column(
    modifier = modifier
      .fillMaxSize()
      .background(MaterialTheme.colorScheme.background)
  ) {
    // Horizontal Tab Row
    ScrollableTabRow(
      selectedTabIndex = selectedTab.ordinal,
      containerColor = MaterialTheme.colorScheme.surface,
      contentColor = PrimaryNavy,
      edgePadding = 16.dp,
      modifier = Modifier.fillMaxWidth()
    ) {
      AcademicSubTab.values().forEach { tab ->
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
          modifier = Modifier.testTag("academic_tab_${tab.name.lowercase()}")
        )
      }
    }

    // Tab Content View
    when (selectedTab) {
      AcademicSubTab.ATTENDANCE -> AttendanceView(
        summary = attendanceSummary,
        user = currentUser,
        repository = repository
      )
      AcademicSubTab.TIMETABLE -> TimetableView()
      AcademicSubTab.RESULTS -> ResultsAndMarksView(user = currentUser)
      AcademicSubTab.ASSIGNMENTS -> AssignmentsView(
        assignments = assignments,
        user = currentUser,
        repository = repository
      )
      AcademicSubTab.EXAMS -> ExamsScheduleView()
      AcademicSubTab.CALENDAR -> AcademicCalendarView()
    }
  }
}

// -------------------------------------------------------------
// 1. ATTENDANCE VIEW
// -------------------------------------------------------------
@Composable
private fun AttendanceView(
  summary: AttendanceSummary,
  user: UserProfile,
  repository: CampusRepository
) {
  var showLeaveDialog by remember { mutableStateOf(false) }
  var showOdDialog by remember { mutableStateOf(false) }

  LazyColumn(
    modifier = Modifier.fillMaxSize(),
    contentPadding = PaddingValues(16.dp),
    verticalArrangement = Arrangement.spacedBy(14.dp)
  ) {
    // Top Metric Card
    item {
      Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(2.dp),
        modifier = Modifier.fillMaxWidth().testTag("attendance_overview_card")
      ) {
        Column(modifier = Modifier.padding(16.dp)) {
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Column {
              Text(
                text = "Overall Attendance",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
              )
              Text(
                text = "Academic Year 2026-27 (Odd Semester)",
                style = MaterialTheme.typography.bodySmall.copy(color = MaterialTheme.colorScheme.onSurfaceVariant)
              )
            }
            Surface(
              color = if (summary.overallPercentage >= 75.0) EmeraldGreenLight else CrimsonRedLight,
              shape = RoundedCornerShape(8.dp)
            ) {
              Text(
                text = "${summary.overallPercentage}%",
                color = if (summary.overallPercentage >= 75.0) EmeraldGreenDark else CrimsonRed,
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp,
                modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
              )
            }
          }

          Spacer(modifier = Modifier.height(12.dp))

          LinearProgressIndicator(
            progress = { (summary.overallPercentage / 100f).toFloat() },
            modifier = Modifier
              .fillMaxWidth()
              .height(10.dp)
              .clip(RoundedCornerShape(5.dp)),
            color = if (summary.overallPercentage >= 75.0) EmeraldGreen else CrimsonRed,
            trackColor = MaterialTheme.colorScheme.surfaceVariant
          )

          Spacer(modifier = Modifier.height(12.dp))

          // Attendance Counts Grid
          Row(
            modifier = Modifier
              .fillMaxWidth()
              .clip(RoundedCornerShape(10.dp))
              .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
              .padding(10.dp),
            horizontalArrangement = Arrangement.SpaceAround
          ) {
            AttendanceStatItem(label = "Total Hours", value = "${summary.totalDelivered}")
            AttendanceStatItem(label = "Attended", value = "${summary.totalAttended}", color = EmeraldGreenDark)
            AttendanceStatItem(label = "On-Duty (OD)", value = "${summary.totalOD}", color = TagBlue)
            AttendanceStatItem(label = "Leaves", value = "${summary.totalLeaves}", color = SaffronGoldDark)
          }

          Spacer(modifier = Modifier.height(14.dp))

          // Request Buttons
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
          ) {
            OutlinedButton(
              onClick = { showOdDialog = true },
              modifier = Modifier.weight(1f),
              shape = RoundedCornerShape(10.dp)
            ) {
              Icon(Icons.Default.Verified, contentDescription = null, modifier = Modifier.size(16.dp))
              Spacer(modifier = Modifier.width(6.dp))
              Text("Apply On-Duty", fontSize = 12.sp)
            }

            Button(
              onClick = { showLeaveDialog = true },
              modifier = Modifier.weight(1f),
              shape = RoundedCornerShape(10.dp),
              colors = ButtonDefaults.buttonColors(containerColor = PrimaryNavy)
            ) {
              Icon(Icons.Default.EventBusy, contentDescription = null, modifier = Modifier.size(16.dp))
              Spacer(modifier = Modifier.width(6.dp))
              Text("Apply Leave", fontSize = 12.sp)
            }
          }
        }
      }
    }

    // Faculty Attendance Update Section (When Faculty logged in)
    if (user.role == UserRole.FACULTY) {
      item {
        Card(
          shape = RoundedCornerShape(14.dp),
          colors = CardDefaults.cardColors(containerColor = EmeraldGreenLight),
          modifier = Modifier.fillMaxWidth()
        ) {
          Column(modifier = Modifier.padding(14.dp)) {
            Text("Faculty Attendance Update Console", fontWeight = FontWeight.Bold, color = EmeraldGreenDark)
            Text("Mark attendance for CSE III Year A (Period 1: CS8501 Data Structures)", fontSize = 12.sp, color = EmeraldGreenDark)
            Spacer(modifier = Modifier.height(8.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
              Button(
                onClick = { repository.facultyUpdateAttendance("CS8501", true) },
                colors = ButtonDefaults.buttonColors(containerColor = EmeraldGreenDark),
                shape = RoundedCornerShape(8.dp)
              ) {
                Text("Mark Batch Present (All 62)")
              }
            }
          }
        }
      }
    }

    // Subject-wise Breakdown List
    item {
      Text(
        text = "Subject-Wise Attendance Details",
        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
      )
    }

    items(summary.subjects) { subject ->
      Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(1.dp),
        modifier = Modifier.fillMaxWidth().testTag("subject_attendance_${subject.subjectCode}")
      ) {
        Column(modifier = Modifier.padding(14.dp)) {
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Column(modifier = Modifier.weight(1f)) {
              Text(
                text = "${subject.subjectCode} - ${subject.subjectName}",
                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
              )
              Text(
                text = "Faculty: ${subject.facultyName}",
                style = MaterialTheme.typography.bodySmall.copy(color = MaterialTheme.colorScheme.onSurfaceVariant)
              )
            }

            Surface(
              color = if (subject.isShortage) CrimsonRedLight else EmeraldGreenLight,
              shape = RoundedCornerShape(6.dp)
            ) {
              Text(
                text = "${String.format("%.1f", subject.percentage)}%",
                color = if (subject.isShortage) CrimsonRed else EmeraldGreenDark,
                fontWeight = FontWeight.Bold,
                fontSize = 13.sp,
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
              )
            }
          }

          Spacer(modifier = Modifier.height(8.dp))

          LinearProgressIndicator(
            progress = { (subject.percentage / 100f).toFloat() },
            modifier = Modifier
              .fillMaxWidth()
              .height(6.dp)
              .clip(RoundedCornerShape(3.dp)),
            color = if (subject.isShortage) CrimsonRed else PrimaryNavy,
            trackColor = MaterialTheme.colorScheme.surfaceVariant
          )

          Spacer(modifier = Modifier.height(6.dp))

          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
          ) {
            Text(
              text = "Attended: ${subject.attendedClasses + subject.odCount}/${subject.totalClasses} Classes",
              style = MaterialTheme.typography.bodySmall.copy(color = MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 11.sp)
            )
            if (subject.isShortage) {
              Text(
                text = "⚠️ Attendance Shortage (<75%)",
                color = CrimsonRed,
                fontWeight = FontWeight.Bold,
                fontSize = 10.sp
              )
            } else {
              Text(
                text = "OD: ${subject.odCount} • Leaves: ${subject.leaveCount}",
                style = MaterialTheme.typography.bodySmall.copy(color = MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 10.sp)
              )
            }
          }
        }
      }
    }

    // Attendance Day Logs
    item {
      Text(
        text = "Recent Attendance Logs",
        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
        modifier = Modifier.padding(top = 8.dp)
      )
    }

    items(summary.recentLogs) { log ->
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .clip(RoundedCornerShape(10.dp))
          .background(MaterialTheme.colorScheme.surface)
          .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
      ) {
        Column {
          Text(text = "${log.date} (${log.day})", fontWeight = FontWeight.Bold, fontSize = 13.sp)
          Text(text = log.periodDetails, fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
        Surface(
          color = Color(log.status.color).copy(alpha = 0.15f),
          shape = RoundedCornerShape(6.dp)
        ) {
          Text(
            text = log.status.label,
            color = Color(log.status.color),
            fontWeight = FontWeight.Bold,
            fontSize = 11.sp,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
          )
        }
      }
    }
  }

  // OD Request Dialog
  if (showOdDialog) {
    var odReason by remember { mutableStateOf("") }
    var odDate by remember { mutableStateOf("05/09/2026") }

    AlertDialog(
      onDismissRequest = { showOdDialog = false },
      title = { Text("Submit On-Duty (OD) Request", fontWeight = FontWeight.Bold) },
      text = {
        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
          Text("Official duty for Symposium / Hackathon / Sports representation.", fontSize = 12.sp)
          OutlinedTextField(
            value = odReason,
            onValueChange = { odReason = it },
            label = { Text("Event / Duty Reason") },
            placeholder = { Text("e.g. IEEE Conference Presentation at IIT Madras") },
            modifier = Modifier.fillMaxWidth()
          )
          OutlinedTextField(
            value = odDate,
            onValueChange = { odDate = it },
            label = { Text("Date (DD/MM/YYYY)") },
            modifier = Modifier.fillMaxWidth()
          )
        }
      },
      confirmButton = {
        Button(
          onClick = {
            if (odReason.isNotBlank()) {
              repository.submitLeaveOrOD(AttendanceStatus.OD, odReason, odDate)
            }
            showOdDialog = false
          },
          colors = ButtonDefaults.buttonColors(containerColor = PrimaryNavy)
        ) {
          Text("Submit OD")
        }
      },
      dismissButton = {
        TextButton(onClick = { showOdDialog = false }) { Text("Cancel") }
      }
    )
  }

  // Leave Request Dialog
  if (showLeaveDialog) {
    var leaveReason by remember { mutableStateOf("") }
    var leaveDate by remember { mutableStateOf("07/09/2026") }

    AlertDialog(
      onDismissRequest = { showLeaveDialog = false },
      title = { Text("Submit Leave Application", fontWeight = FontWeight.Bold) },
      text = {
        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
          Text("Medical / Personal leave submission to Class Advisor.", fontSize = 12.sp)
          OutlinedTextField(
            value = leaveReason,
            onValueChange = { leaveReason = it },
            label = { Text("Reason for Leave") },
            placeholder = { Text("e.g. Viral fever / Out of station family function") },
            modifier = Modifier.fillMaxWidth()
          )
          OutlinedTextField(
            value = leaveDate,
            onValueChange = { leaveDate = it },
            label = { Text("Date of Absence (DD/MM/YYYY)") },
            modifier = Modifier.fillMaxWidth()
          )
        }
      },
      confirmButton = {
        Button(
          onClick = {
            if (leaveReason.isNotBlank()) {
              repository.submitLeaveOrOD(AttendanceStatus.LEAVE, leaveReason, leaveDate)
            }
            showLeaveDialog = false
          },
          colors = ButtonDefaults.buttonColors(containerColor = PrimaryNavy)
        ) {
          Text("Submit Leave")
        }
      },
      dismissButton = {
        TextButton(onClick = { showLeaveDialog = false }) { Text("Cancel") }
      }
    )
  }
}

@Composable
private fun AttendanceStatItem(label: String, value: String, color: Color = MaterialTheme.colorScheme.onSurface) {
  Column(horizontalAlignment = Alignment.CenterHorizontally) {
    Text(text = label, fontSize = 10.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
    Text(text = value, fontSize = 14.sp, fontWeight = FontWeight.Bold, color = color)
  }
}

// -------------------------------------------------------------
// 2. TIMETABLE VIEW
// -------------------------------------------------------------
@Composable
private fun TimetableView() {
  val days = listOf("Monday", "Tuesday", "Wednesday", "Thursday", "Friday")
  var selectedDay by remember { mutableStateOf("Wednesday") }

  val schedule = CampusMockData.sampleWeeklyTimetable[selectedDay] ?: CampusMockData.sampleTimetableToday

  LazyColumn(
    modifier = Modifier.fillMaxSize(),
    contentPadding = PaddingValues(16.dp),
    verticalArrangement = Arrangement.spacedBy(12.dp)
  ) {
    item {
      Text(
        text = "Class Timetable & Room Matrix",
        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
      )
    }

    // Day Selector Chips
    item {
      LazyRow(
        horizontalArrangement = Arrangement.spacedBy(8.dp)
      ) {
        items(days) { day ->
          val isSelected = selectedDay == day
          FilterChip(
            selected = isSelected,
            onClick = { selectedDay = day },
            label = { Text(day, fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal) },
            colors = FilterChipDefaults.filterChipColors(
              selectedContainerColor = PrimaryNavy,
              selectedLabelColor = Color.White
            )
          )
        }
      }
    }

    items(schedule) { slot ->
      Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(1.dp),
        modifier = Modifier.fillMaxWidth()
      ) {
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .padding(14.dp),
          verticalAlignment = Alignment.CenterVertically
        ) {
          // Period number bubble
          Box(
            modifier = Modifier
              .size(44.dp)
              .clip(CircleShape)
              .background(PrimaryNavy.copy(alpha = 0.1f)),
            contentAlignment = Alignment.Center
          ) {
            Text(
              text = "P${slot.periodNumber}",
              fontWeight = FontWeight.Bold,
              color = PrimaryNavy,
              fontSize = 13.sp
            )
          }

          Spacer(modifier = Modifier.width(12.dp))

          Column(modifier = Modifier.weight(1f)) {
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween
            ) {
              Text(
                text = "${slot.subjectCode}: ${slot.subjectName}",
                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                modifier = Modifier.weight(1f),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
              )
              Surface(
                color = if (slot.isLab) SaffronGoldLight else Color(0xFFE0F2FE),
                shape = RoundedCornerShape(4.dp)
              ) {
                Text(
                  text = if (slot.isLab) "LAB" else "THEORY",
                  color = if (slot.isLab) SaffronGoldDark else TagBlue,
                  fontSize = 9.sp,
                  fontWeight = FontWeight.Bold,
                  modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                )
              }
            }

            Spacer(modifier = Modifier.height(4.dp))

            Text(
              text = "Faculty: ${slot.facultyName} • Room: ${slot.roomNumber}",
              style = MaterialTheme.typography.bodySmall.copy(color = MaterialTheme.colorScheme.onSurfaceVariant)
            )

            Spacer(modifier = Modifier.height(2.dp))

            Text(
              text = slot.timeSlot,
              style = MaterialTheme.typography.bodySmall.copy(color = PrimaryNavy, fontWeight = FontWeight.SemiBold, fontSize = 11.sp)
            )
          }
        }
      }
    }
  }
}

// -------------------------------------------------------------
// 3. RESULTS & MARKS VIEW (Indian CGPA / SGPA)
// -------------------------------------------------------------
@Composable
private fun ResultsAndMarksView(user: UserProfile) {
  var showDownloadReceipt by remember { mutableStateOf(false) }

  LazyColumn(
    modifier = Modifier.fillMaxSize(),
    contentPadding = PaddingValues(16.dp),
    verticalArrangement = Arrangement.spacedBy(14.dp)
  ) {
    item {
      Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = PrimaryNavy),
        modifier = Modifier.fillMaxWidth()
      ) {
        Column(modifier = Modifier.padding(16.dp)) {
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Column {
              Text("Cumulative GPA (CGPA)", color = Color.White.copy(alpha = 0.8f), fontSize = 12.sp)
              Text("${user.cgpa}", color = SaffronGold, fontSize = 28.sp, fontWeight = FontWeight.Bold)
              Text("Class: First Class with Distinction", color = Color.White, fontSize = 11.sp)
            }
            Button(
              onClick = { showDownloadReceipt = true },
              colors = ButtonDefaults.buttonColors(containerColor = SaffronGold, contentColor = Color.Black),
              shape = RoundedCornerShape(10.dp)
            ) {
              Icon(Icons.Default.Download, contentDescription = null, modifier = Modifier.size(16.dp))
              Spacer(modifier = Modifier.width(4.dp))
              Text("Mark Sheet", fontSize = 11.sp, fontWeight = FontWeight.Bold)
            }
          }
        }
      }
    }

    item {
      Text(
        text = "Semester Progress & SGPA History",
        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
      )
    }

    items(CampusMockData.sampleSemesterResults) { res ->
      Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        modifier = Modifier.fillMaxWidth()
      ) {
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .padding(14.dp),
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.SpaceBetween
        ) {
          Column {
            Text(text = res.semesterLabel, fontWeight = FontWeight.Bold, fontSize = 13.sp)
            Text(text = "Credits: ${res.creditsEarned}/${res.totalCredits} • ${res.status}", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
          }
          Surface(
            color = EmeraldGreenLight,
            shape = RoundedCornerShape(6.dp)
          ) {
            Text(
              text = "SGPA: ${res.sgpa}",
              color = EmeraldGreenDark,
              fontWeight = FontWeight.Bold,
              fontSize = 13.sp,
              modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
            )
          }
        }
      }
    }

    item {
      Text(
        text = "Current Semester V - Continuous Assessment Marks",
        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
        modifier = Modifier.padding(top = 6.dp)
      )
    }

    items(CampusMockData.sampleAcademicSubjects) { subj ->
      Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        modifier = Modifier.fillMaxWidth()
      ) {
        Column(modifier = Modifier.padding(14.dp)) {
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Column(modifier = Modifier.weight(1f)) {
              Text(
                text = "${subj.code}: ${subj.name}",
                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
              )
              Text(
                text = "Credits: ${subj.credits} • Faculty: ${subj.facultyName}",
                style = MaterialTheme.typography.bodySmall.copy(color = MaterialTheme.colorScheme.onSurfaceVariant)
              )
            }
            Surface(
              color = PrimaryNavy.copy(alpha = 0.1f),
              shape = RoundedCornerShape(6.dp)
            ) {
              Text(
                text = "Grade: ${subj.semesterGrade}",
                color = PrimaryNavy,
                fontWeight = FontWeight.Bold,
                fontSize = 12.sp,
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
              )
            }
          }

          Spacer(modifier = Modifier.height(8.dp))

          Row(
            modifier = Modifier
              .fillMaxWidth()
              .clip(RoundedCornerShape(8.dp))
              .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
              .padding(8.dp),
            horizontalArrangement = Arrangement.SpaceAround
          ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
              Text("IA-1", fontSize = 10.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
              Text("${subj.internal1Score}/50", fontSize = 12.sp, fontWeight = FontWeight.Bold)
            }
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
              Text("IA-2", fontSize = 10.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
              Text("${subj.internal2Score}/50", fontSize = 12.sp, fontWeight = FontWeight.Bold)
            }
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
              Text("Assignment", fontSize = 10.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
              Text("${subj.assignmentScore}/10", fontSize = 12.sp, fontWeight = FontWeight.Bold)
            }
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
              Text("Model Exam", fontSize = 10.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
              Text("${subj.modelExamScore}/100", fontSize = 12.sp, fontWeight = FontWeight.Bold)
            }
          }
        }
      }
    }
  }

  if (showDownloadReceipt) {
    AlertDialog(
      onDismissRequest = { showDownloadReceipt = false },
      title = { Text("Consolidated Grade Sheet", fontWeight = FontWeight.Bold) },
      text = {
        Text("Grade sheet for Semesters I to IV generated with Controller of Examinations digital signature hash: 0x88F2A901B.", fontSize = 13.sp)
      },
      confirmButton = {
        Button(onClick = { showDownloadReceipt = false }, colors = ButtonDefaults.buttonColors(containerColor = PrimaryNavy)) {
          Text("Download PDF")
        }
      }
    )
  }
}

// -------------------------------------------------------------
// 4. ASSIGNMENTS VIEW
// -------------------------------------------------------------
@Composable
private fun AssignmentsView(
  assignments: List<AssignmentItem>,
  user: UserProfile,
  repository: CampusRepository
) {
  var showUploadDialog by remember { mutableStateOf<AssignmentItem?>(null) }
  var showGradeDialog by remember { mutableStateOf<AssignmentItem?>(null) }

  LazyColumn(
    modifier = Modifier.fillMaxSize(),
    contentPadding = PaddingValues(16.dp),
    verticalArrangement = Arrangement.spacedBy(12.dp)
  ) {
    item {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Text(
          text = "Coursework & Laboratory Assignments",
          style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
        )
      }
    }

    items(assignments) { asg ->
      Card(
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(1.dp),
        modifier = Modifier.fillMaxWidth()
      ) {
        Column(modifier = Modifier.padding(14.dp)) {
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Text(
              text = "${asg.subjectCode}: ${asg.subjectName}",
              style = MaterialTheme.typography.labelMedium.copy(color = PrimaryNavy, fontWeight = FontWeight.Bold)
            )
            Surface(
              color = if (asg.isSubmitted) EmeraldGreenLight else CrimsonRedLight,
              shape = RoundedCornerShape(4.dp)
            ) {
              Text(
                text = if (asg.isSubmitted) "SUBMITTED" else "DUE: ${asg.dueDate}",
                color = if (asg.isSubmitted) EmeraldGreenDark else CrimsonRed,
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
              )
            }
          }

          Spacer(modifier = Modifier.height(6.dp))

          Text(
            text = asg.title,
            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
          )

          Spacer(modifier = Modifier.height(4.dp))

          Text(
            text = asg.description,
            style = MaterialTheme.typography.bodySmall.copy(color = MaterialTheme.colorScheme.onSurfaceVariant)
          )

          Spacer(modifier = Modifier.height(8.dp))

          if (asg.isSubmitted && asg.obtainedMarks != null) {
            Surface(
              color = EmeraldGreenLight,
              shape = RoundedCornerShape(8.dp),
              modifier = Modifier.fillMaxWidth()
            ) {
              Row(
                modifier = Modifier.padding(8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
              ) {
                Text(
                  text = "Score: ${asg.obtainedMarks}/${asg.maxMarks} • Feedback: ${asg.facultyFeedback ?: "Well done"}",
                  fontSize = 11.sp,
                  color = EmeraldGreenDark,
                  fontWeight = FontWeight.SemiBold
                )
              }
            }
          }

          Spacer(modifier = Modifier.height(8.dp))

          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Text(
              text = "Faculty: ${asg.facultyName}",
              style = MaterialTheme.typography.bodySmall.copy(color = MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 11.sp)
            )

            if (!asg.isSubmitted) {
              Button(
                onClick = { showUploadDialog = asg },
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(containerColor = PrimaryNavy),
                contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp)
              ) {
                Icon(Icons.Default.CloudUpload, contentDescription = null, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(4.dp))
                Text("Upload Solution", fontSize = 11.sp)
              }
            } else if (user.role == UserRole.FACULTY) {
              OutlinedButton(
                onClick = { showGradeDialog = asg },
                shape = RoundedCornerShape(8.dp),
                contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp)
              ) {
                Text("Grade Assignment", fontSize = 11.sp)
              }
            }
          }
        }
      }
    }
  }

  showUploadDialog?.let { asg ->
    AlertDialog(
      onDismissRequest = { showUploadDialog = null },
      title = { Text("Upload Assignment Solution", fontWeight = FontWeight.Bold) },
      text = {
        Column {
          Text(text = asg.title, fontSize = 13.sp)
          Spacer(modifier = Modifier.height(10.dp))
          Surface(
            color = Color(0xFFE0F2FE),
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier.fillMaxWidth()
          ) {
            Row(modifier = Modifier.padding(10.dp), verticalAlignment = Alignment.CenterVertically) {
              Icon(Icons.Default.AttachFile, contentDescription = null, tint = PrimaryNavy)
              Spacer(modifier = Modifier.width(8.dp))
              Text("21052421012_${asg.subjectCode}_Solution.pdf", fontSize = 11.sp, color = PrimaryNavy)
            }
          }
        }
      },
      confirmButton = {
        Button(
          onClick = {
            repository.submitAssignment(asg.id, "21052421012_${asg.subjectCode}_Solution.pdf")
            showUploadDialog = null
          },
          colors = ButtonDefaults.buttonColors(containerColor = PrimaryNavy)
        ) {
          Text("Submit")
        }
      },
      dismissButton = {
        TextButton(onClick = { showUploadDialog = null }) { Text("Cancel") }
      }
    )
  }

  showGradeDialog?.let { asg ->
    var marksInput by remember { mutableStateOf("19") }
    var feedbackInput by remember { mutableStateOf("Good implementation of data structures and algorithms.") }

    AlertDialog(
      onDismissRequest = { showGradeDialog = null },
      title = { Text("Grade Student Submission", fontWeight = FontWeight.Bold) },
      text = {
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
          Text("Grading submission for K. P. Bala Murugan (21052421012)", fontSize = 12.sp)
          OutlinedTextField(
            value = marksInput,
            onValueChange = { marksInput = it },
            label = { Text("Marks (out of 20)") },
            modifier = Modifier.fillMaxWidth()
          )
          OutlinedTextField(
            value = feedbackInput,
            onValueChange = { feedbackInput = it },
            label = { Text("Faculty Feedback") },
            modifier = Modifier.fillMaxWidth()
          )
        }
      },
      confirmButton = {
        Button(
          onClick = {
            repository.gradeAssignment(asg.id, marksInput.toIntOrNull() ?: 18, feedbackInput)
            showGradeDialog = null
          },
          colors = ButtonDefaults.buttonColors(containerColor = PrimaryNavy)
        ) {
          Text("Save Grade")
        }
      },
      dismissButton = {
        TextButton(onClick = { showGradeDialog = null }) { Text("Cancel") }
      }
    )
  }
}

// -------------------------------------------------------------
// 5. EXAMS SCHEDULE VIEW
// -------------------------------------------------------------
@Composable
private fun ExamsScheduleView() {
  LazyColumn(
    modifier = Modifier.fillMaxSize(),
    contentPadding = PaddingValues(16.dp),
    verticalArrangement = Arrangement.spacedBy(12.dp)
  ) {
    item {
      Card(
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFFEF3C7)),
        modifier = Modifier.fillMaxWidth()
      ) {
        Row(
          modifier = Modifier.padding(14.dp),
          verticalAlignment = Alignment.CenterVertically
        ) {
          Icon(Icons.Default.Info, contentDescription = null, tint = SaffronGoldDark)
          Spacer(modifier = Modifier.width(10.dp))
          Column {
            Text("Examination Hall Instructions", fontWeight = FontWeight.Bold, color = SaffronGoldDark, fontSize = 12.sp)
            Text("Candidates must carry Physical College ID Card & Autonomous Hall Ticket. Entry prohibited 15 mins after session commencement.", color = SaffronGoldDark, fontSize = 11.sp)
          }
        }
      }
    }

    item {
      Text(
        text = "Internal Assessment - II Timetable (September 2026)",
        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
      )
    }

    items(CampusMockData.sampleExams) { exam ->
      Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        modifier = Modifier.fillMaxWidth()
      ) {
        Column(modifier = Modifier.padding(14.dp)) {
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Text(
              text = "${exam.subjectCode}: ${exam.subjectName}",
              style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
              modifier = Modifier.weight(1f)
            )
            Surface(
              color = PrimaryNavy.copy(alpha = 0.1f),
              shape = RoundedCornerShape(4.dp)
            ) {
              Text(
                text = exam.examDate,
                color = PrimaryNavy,
                fontWeight = FontWeight.Bold,
                fontSize = 11.sp,
                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
              )
            }
          }

          Spacer(modifier = Modifier.height(6.dp))

          Text(
            text = "Session: ${exam.session} • Invigilator: ${exam.invigilator}",
            style = MaterialTheme.typography.bodySmall.copy(color = MaterialTheme.colorScheme.onSurfaceVariant)
          )

          Spacer(modifier = Modifier.height(4.dp))

          Surface(
            color = MaterialTheme.colorScheme.surfaceVariant,
            shape = RoundedCornerShape(6.dp),
            modifier = Modifier.fillMaxWidth()
          ) {
            Row(
              modifier = Modifier.padding(8.dp),
              horizontalArrangement = Arrangement.SpaceBetween
            ) {
              Text(text = "Hall: ${exam.hallNumber}", fontSize = 11.sp, fontWeight = FontWeight.SemiBold)
              Text(text = "Seating: ${exam.seatNumber}", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = PrimaryNavy)
            }
          }
        }
      }
    }
  }
}

// -------------------------------------------------------------
// 6. ACADEMIC CALENDAR VIEW
// -------------------------------------------------------------
@Composable
private fun AcademicCalendarView() {
  val calendarEvents = listOf(
    Pair("02/09/2026", "Commencement of IA-2 Preparation & Lab Assessment Series"),
    Pair("15/09/2026", "Autonomous Exam Registration Fee Payment Deadline (Nov/Dec 2026)"),
    Pair("18/09/2026 - 25/09/2026", "Internal Assessment - II (IA-2) Written Examinations"),
    Pair("24/09/2026", "KRYPTON 2026 National Level Technical Symposium"),
    Pair("03/10/2026", "HACK-BHARAT 36-Hour National Hackathon"),
    Pair("16/10/2026", "DHWAANI 2026 Inter-College Cultural Extravaganza"),
    Pair("02/11/2026 - 08/11/2026", "Model Practical & Theory Examinations"),
    Pair("15/11/2026", "Last Working Day for Odd Semester 2026-27"),
    Pair("20/11/2026", "Commencement of End Semester Autonomous Theory Examinations")
  )

  LazyColumn(
    modifier = Modifier.fillMaxSize(),
    contentPadding = PaddingValues(16.dp),
    verticalArrangement = Arrangement.spacedBy(10.dp)
  ) {
    item {
      Text(
        text = "College Academic Calendar (2026 - 2027)",
        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
      )
    }

    items(calendarEvents) { (date, description) ->
      Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        modifier = Modifier.fillMaxWidth()
      ) {
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .padding(14.dp),
          verticalAlignment = Alignment.CenterVertically
        ) {
          Surface(
            color = PrimaryNavy,
            shape = RoundedCornerShape(8.dp)
          ) {
            Text(
              text = date,
              color = Color.White,
              fontWeight = FontWeight.Bold,
              fontSize = 11.sp,
              modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
            )
          }
          Spacer(modifier = Modifier.width(12.dp))
          Text(
            text = description,
            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Medium),
            modifier = Modifier.weight(1f)
          )
        }
      }
    }
  }
}
