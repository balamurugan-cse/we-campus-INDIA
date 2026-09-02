package com.example.ui.screens.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.CertificateItem
import com.example.data.model.ProjectItem
import com.example.data.model.UserProfile
import com.example.data.repository.CampusRepository
import com.example.ui.theme.*

@Composable
fun ProfileScreen(
  repository: CampusRepository,
  onOpenDigitalId: () -> Unit,
  modifier: Modifier = Modifier
) {
  val currentUser by repository.currentUser.collectAsState()
  val certificates by repository.certificates.collectAsState()
  val skillProfile by repository.skillProfile.collectAsState()

  var showEditProfileDialog by remember { mutableStateOf(false) }
  var showResumeExportDialog by remember { mutableStateOf(false) }

  LazyColumn(
    modifier = modifier
      .fillMaxSize()
      .background(MaterialTheme.colorScheme.background),
    contentPadding = PaddingValues(16.dp),
    verticalArrangement = Arrangement.spacedBy(16.dp)
  ) {
    // -------------------------------------------------------------
    // 1. PROFILE HEADER CARD
    // -------------------------------------------------------------
    item {
      Card(
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(2.dp),
        modifier = Modifier.fillMaxWidth().testTag("profile_header_card")
      ) {
        Column(
          modifier = Modifier.padding(18.dp),
          horizontalAlignment = Alignment.CenterHorizontally
        ) {
          Box(
            modifier = Modifier
              .size(76.dp)
              .clip(CircleShape)
              .background(PrimaryNavy)
              .border(3.dp, SaffronGold, CircleShape),
            contentAlignment = Alignment.Center
          ) {
            Text(
              text = currentUser.avatarInitials,
              color = Color.White,
              fontWeight = FontWeight.Bold,
              fontSize = 24.sp
            )
          }

          Spacer(modifier = Modifier.height(10.dp))

          Text(
            text = currentUser.fullName,
            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
          )

          Text(
            text = "REG NO: ${currentUser.rollNumber}",
            color = PrimaryNavy,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold
          )

          Text(
            text = "${currentUser.degree} • ${currentUser.department} (${currentUser.year})",
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            fontSize = 12.sp
          )

          Spacer(modifier = Modifier.height(14.dp))

          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
          ) {
            Button(
              onClick = onOpenDigitalId,
              colors = ButtonDefaults.buttonColors(containerColor = PrimaryNavy),
              shape = RoundedCornerShape(10.dp),
              modifier = Modifier.weight(1f).testTag("profile_view_digital_id_btn")
            ) {
              Icon(Icons.Default.Badge, contentDescription = null, modifier = Modifier.size(16.dp))
              Spacer(modifier = Modifier.width(6.dp))
              Text("Digital Smart ID", fontSize = 12.sp)
            }

            OutlinedButton(
              onClick = { showEditProfileDialog = true },
              shape = RoundedCornerShape(10.dp),
              modifier = Modifier.weight(1f)
            ) {
              Icon(Icons.Default.Edit, contentDescription = null, modifier = Modifier.size(16.dp))
              Spacer(modifier = Modifier.width(6.dp))
              Text("Edit Details", fontSize = 12.sp)
            }
          }
        }
      }
    }

    // -------------------------------------------------------------
    // 2. ACADEMIC & INSTITUTIONAL BADGE
    // -------------------------------------------------------------
    item {
      Card(
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = PrimaryNavy.copy(alpha = 0.06f)),
        border = androidx.compose.foundation.BorderStroke(1.dp, PrimaryNavy.copy(alpha = 0.2f)),
        modifier = Modifier.fillMaxWidth()
      ) {
        Column(modifier = Modifier.padding(14.dp)) {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Default.Verified, contentDescription = null, tint = PrimaryNavy, modifier = Modifier.size(20.dp))
            Spacer(modifier = Modifier.width(8.dp))
            Text("Autonomous Accreditation & Affiliation", fontWeight = FontWeight.Bold, color = PrimaryNavy, fontSize = 13.sp)
          }
          Spacer(modifier = Modifier.height(6.dp))
          Text(currentUser.affiliation, fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
          Text("Section: ${currentUser.section} • Blood Group: ${currentUser.bloodGroup} • Validity: ${currentUser.validityDate}", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
      }
    }

    // -------------------------------------------------------------
    // 3. SKILL PORTFOLIO & VERIFIED CERTIFICATIONS
    // -------------------------------------------------------------
    item {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Text("Verified Skills & Certifications", style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold))
        TextButton(onClick = { showResumeExportDialog = true }) {
          Text("Export ATS Resume", color = PrimaryNavy, fontWeight = FontWeight.Bold, fontSize = 12.sp)
        }
      }
    }

    item {
      Card(
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        modifier = Modifier.fillMaxWidth()
      ) {
        Column(modifier = Modifier.padding(14.dp)) {
          Text("Technical Skills & Frameworks", fontWeight = FontWeight.Bold, fontSize = 12.sp, color = PrimaryNavy)
          Spacer(modifier = Modifier.height(6.dp))
          val allSkills = skillProfile.programmingLanguages + skillProfile.frameworks
          LazyRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
            items(allSkills) { skill ->
              Surface(color = PrimaryNavy.copy(alpha = 0.08f), shape = RoundedCornerShape(6.dp)) {
                Text(skill, fontSize = 11.sp, color = PrimaryNavy, fontWeight = FontWeight.SemiBold, modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp))
              }
            }
          }
        }
      }
    }

    items(certificates) { cert ->
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
          Box(
            modifier = Modifier
              .size(40.dp)
              .clip(CircleShape)
              .background(SaffronGoldLight),
            contentAlignment = Alignment.Center
          ) {
            Icon(Icons.Default.WorkspacePremium, contentDescription = null, tint = SaffronGoldDark, modifier = Modifier.size(22.dp))
          }

          Spacer(modifier = Modifier.width(12.dp))

          Column(modifier = Modifier.weight(1f)) {
            Text(cert.title, fontWeight = FontWeight.Bold, fontSize = 13.sp)
            Text("Issued By: ${cert.issuedBy} • Date: ${cert.issueDate}", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
            Text("Credential ID: ${cert.credentialId}", fontSize = 10.sp, color = PrimaryNavy, fontWeight = FontWeight.Medium)
          }
        }
      }
    }

    // -------------------------------------------------------------
    // 4. CAPSTONE & ACADEMIC PROJECTS
    // -------------------------------------------------------------
    item {
      Text("Academic & Capstone Projects", style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold), modifier = Modifier.padding(top = 4.dp))
    }

    items(skillProfile.keyProjects) { proj ->
      Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        modifier = Modifier.fillMaxWidth()
      ) {
        Column(modifier = Modifier.padding(14.dp)) {
          Text(proj.title, fontWeight = FontWeight.Bold, fontSize = 14.sp)
          Spacer(modifier = Modifier.height(2.dp))
          Text(proj.description, fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
          Spacer(modifier = Modifier.height(6.dp))
          Text("Tech Stack: ${proj.techStack}", fontSize = 11.sp, color = PrimaryNavy, fontWeight = FontWeight.SemiBold)
        }
      }
    }

    // -------------------------------------------------------------
    // 5. APP & NOTIFICATION PREFERENCES
    // -------------------------------------------------------------
    item {
      Text("App Preferences & Security", style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold), modifier = Modifier.padding(top = 8.dp))
    }

    item {
      Card(
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        modifier = Modifier.fillMaxWidth()
      ) {
        Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
          SettingRow(icon = Icons.Default.Notifications, title = "Push Alerts for Attendance & Exams", isEnabled = true)
          Divider()
          SettingRow(icon = Icons.Default.Fingerprint, title = "Biometric / Face ID Verification", isEnabled = true)
          Divider()
          SettingRow(icon = Icons.Default.Translate, title = "App Language", valueText = "English (IN)")
          Divider()
          SettingRow(icon = Icons.Default.PrivacyTip, title = "Campus Privacy Policy & Rules", valueText = "AICTE & UGC 2026")
        }
      }
    }
  }

  // Edit Profile Dialog
  if (showEditProfileDialog) {
    var editName by remember { mutableStateOf(currentUser.fullName) }
    var editPhone by remember { mutableStateOf(currentUser.phoneNumber) }
    var editBlood by remember { mutableStateOf(currentUser.bloodGroup) }

    AlertDialog(
      onDismissRequest = { showEditProfileDialog = false },
      title = { Text("Update Student Record", fontWeight = FontWeight.Bold) },
      text = {
        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
          OutlinedTextField(value = editName, onValueChange = { editName = it }, label = { Text("Full Name") }, modifier = Modifier.fillMaxWidth())
          OutlinedTextField(value = editPhone, onValueChange = { editPhone = it }, label = { Text("Mobile Number") }, modifier = Modifier.fillMaxWidth())
          OutlinedTextField(value = editBlood, onValueChange = { editBlood = it }, label = { Text("Blood Group") }, modifier = Modifier.fillMaxWidth())
        }
      },
      confirmButton = {
        Button(
          onClick = {
            repository.updateProfile(editName, editPhone, editBlood)
            showEditProfileDialog = false
          },
          colors = ButtonDefaults.buttonColors(containerColor = PrimaryNavy)
        ) {
          Text("Save Changes")
        }
      },
      dismissButton = { TextButton(onClick = { showEditProfileDialog = false }) { Text("Cancel") } }
    )
  }

  if (showResumeExportDialog) {
    AlertDialog(
      onDismissRequest = { showResumeExportDialog = false },
      title = { Text("ATS Campus Resume Generated", fontWeight = FontWeight.Bold) },
      text = {
        Text("Formatted standard Indian Engineering College Resume generated with CGPA 8.74, 3 certifications, and capstone project entries ready for placement drives.", fontSize = 13.sp)
      },
      confirmButton = {
        Button(onClick = { showResumeExportDialog = false }, colors = ButtonDefaults.buttonColors(containerColor = PrimaryNavy)) {
          Text("Download PDF Resume")
        }
      }
    )
  }
}

@Composable
private fun SettingRow(
  icon: androidx.compose.ui.graphics.vector.ImageVector,
  title: String,
  isEnabled: Boolean? = null,
  valueText: String? = null
) {
  Row(
    modifier = Modifier.fillMaxWidth(),
    verticalAlignment = Alignment.CenterVertically,
    horizontalArrangement = Arrangement.SpaceBetween
  ) {
    Row(verticalAlignment = Alignment.CenterVertically) {
      Icon(imageVector = icon, contentDescription = null, tint = PrimaryNavy, modifier = Modifier.size(20.dp))
      Spacer(modifier = Modifier.width(10.dp))
      Text(title, fontSize = 13.sp, fontWeight = FontWeight.Medium)
    }
    if (isEnabled != null) {
      var state by remember { mutableStateOf(isEnabled) }
      Switch(checked = state, onCheckedChange = { state = it })
    } else if (valueText != null) {
      Text(valueText, fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
    }
  }
}
