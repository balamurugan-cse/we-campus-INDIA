package com.example.ui.screens.services

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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.data.mock.CampusMockData
import com.example.data.model.*
import com.example.data.repository.CampusRepository
import com.example.ui.theme.*

enum class ServiceHubSection(val title: String, val icon: androidx.compose.ui.graphics.vector.ImageVector) {
  PLACEMENTS("Placements & Internships", Icons.Default.WorkOutline),
  FEES("Fee Payments & Receipts", Icons.Default.ReceiptLong),
  LIBRARY("Central Library & Racks", Icons.Default.MenuBook),
  CANTEEN("Canteen Pre-Order", Icons.Default.Restaurant),
  HOSTEL("Hostel & Mess Menu", Icons.Default.Hotel),
  TRANSPORT("Bus Fleet & Passes", Icons.Default.DirectionsBus),
  GRIEVANCES("Grievance Redressal", Icons.Default.SupportAgent)
}

@Composable
fun CampusServicesScreen(
  repository: CampusRepository,
  initialSection: String? = null,
  modifier: Modifier = Modifier
) {
  var selectedSection by remember(initialSection) {
    mutableStateOf(
      when (initialSection?.lowercase()) {
        "placements" -> ServiceHubSection.PLACEMENTS
        "fees" -> ServiceHubSection.FEES
        "library" -> ServiceHubSection.LIBRARY
        "canteen" -> ServiceHubSection.CANTEEN
        "hostel" -> ServiceHubSection.HOSTEL
        "transport" -> ServiceHubSection.TRANSPORT
        "grievance", "grievances" -> ServiceHubSection.GRIEVANCES
        else -> ServiceHubSection.PLACEMENTS
      }
    )
  }

  val placements by repository.placements.collectAsState()
  val internships by repository.internships.collectAsState()
  val fees by repository.fees.collectAsState()
  val books by repository.libraryBooks.collectAsState()
  val canteenMenu by repository.canteenMenu.collectAsState()
  val canteenCart by repository.canteenCart.collectAsState()
  val hostelInfo by repository.hostelInfo.collectAsState()
  val grievances by repository.grievances.collectAsState()

  Column(
    modifier = modifier
      .fillMaxSize()
      .background(MaterialTheme.colorScheme.background)
  ) {
    // Horizontal pill selector for services
    Surface(
      color = MaterialTheme.colorScheme.surface,
      shadowElevation = 2.dp
    ) {
      LazyRow(
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 10.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier.fillMaxWidth()
      ) {
        items(ServiceHubSection.values()) { sec ->
          val isSelected = selectedSection == sec
          FilterChip(
            selected = isSelected,
            onClick = { selectedSection = sec },
            leadingIcon = {
              Icon(
                imageVector = sec.icon,
                contentDescription = null,
                modifier = Modifier.size(16.dp),
                tint = if (isSelected) Color.White else PrimaryNavy
              )
            },
            label = {
              Text(
                text = sec.title,
                fontSize = 12.sp,
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
              )
            },
            colors = FilterChipDefaults.filterChipColors(
              selectedContainerColor = PrimaryNavy,
              selectedLabelColor = Color.White
            ),
            modifier = Modifier.testTag("service_tab_${sec.name.lowercase()}")
          )
        }
      }
    }

    Box(modifier = Modifier.fillMaxSize()) {
      when (selectedSection) {
        ServiceHubSection.PLACEMENTS -> PlacementsSection(
          placements = placements,
          internships = internships,
          onApplyPlacement = { repository.applyPlacement(it) },
          onApplyInternship = { repository.applyInternship(it) }
        )
        ServiceHubSection.FEES -> FeesSection(
          fees = fees,
          onPayFee = { repository.payFee(it) }
        )
        ServiceHubSection.LIBRARY -> LibrarySection(
          books = books,
          onRenewBook = { repository.renewLibraryBook(it) }
        )
        ServiceHubSection.CANTEEN -> CanteenSection(
          menu = canteenMenu,
          cart = canteenCart,
          onAddToCart = { repository.addToCart(it) },
          onRemoveFromCart = { repository.removeFromCart(it) },
          onPlaceOrder = { repository.placeCanteenOrder() }
        )
        ServiceHubSection.HOSTEL -> HostelSection(
          hostel = hostelInfo,
          onSubmitComplaint = { cat, desc -> repository.submitHostelComplaint(cat, desc) }
        )
        ServiceHubSection.TRANSPORT -> TransportSection()
        ServiceHubSection.GRIEVANCES -> GrievanceSection(
          grievances = grievances,
          onSubmit = { cat, subj, desc, anon -> repository.submitGrievance(cat, subj, desc, anon) }
        )
      }
    }
  }
}

// -------------------------------------------------------------
// 1. PLACEMENTS & INTERNSHIPS
// -------------------------------------------------------------
@Composable
private fun PlacementsSection(
  placements: List<PlacementDrive>,
  internships: List<InternshipItem>,
  onApplyPlacement: (String) -> Unit,
  onApplyInternship: (String) -> Unit
) {
  var subTab by remember { mutableStateOf("Drives") }

  LazyColumn(
    modifier = Modifier.fillMaxSize(),
    contentPadding = PaddingValues(16.dp),
    verticalArrangement = Arrangement.spacedBy(14.dp)
  ) {
    // Top Placement Hero Banner
    item {
      Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = PrimaryNavy),
        modifier = Modifier.fillMaxWidth()
      ) {
        Column(modifier = Modifier.padding(18.dp)) {
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Column {
              Text("Campus Placement Season 2026", color = SaffronGold, fontWeight = FontWeight.Bold, fontSize = 12.sp)
              Text("Tier-1 Autonomous CTC Benchmark", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 16.sp)
            }
            Surface(
              color = SaffronGold,
              shape = RoundedCornerShape(8.dp)
            ) {
              Text("₹ 44 LPA Highest", color = Color.Black, fontWeight = FontWeight.Bold, fontSize = 11.sp, modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp))
            }
          }

          Spacer(modifier = Modifier.height(10.dp))
          Text(
            text = "Eligible for 18 upcoming Tier-1 product & core engineering drives with your current CGPA (8.74). Maintain no standing arrears.",
            color = Color.White.copy(alpha = 0.85f),
            fontSize = 12.sp,
            lineHeight = 16.sp
          )
        }
      }
    }

    item {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
      ) {
        FilterChip(
          selected = subTab == "Drives",
          onClick = { subTab = "Drives" },
          label = { Text("Placement Drives (${placements.size})") }
        )
        FilterChip(
          selected = subTab == "Internships",
          onClick = { subTab = "Internships" },
          label = { Text("Pre-Final Internships (${internships.size})") }
        )
      }
    }

    if (subTab == "Drives") {
      items(placements) { plc ->
        Card(
          shape = RoundedCornerShape(16.dp),
          colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
          elevation = CardDefaults.cardElevation(2.dp),
          modifier = Modifier.fillMaxWidth().testTag("placement_card_${plc.id}")
        ) {
          Column(modifier = Modifier.padding(16.dp)) {
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.CenterVertically
            ) {
              Column(modifier = Modifier.weight(1f)) {
                Text(plc.companyName, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                Text(plc.role, fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
              }
              Surface(
                color = EmeraldGreenLight,
                shape = RoundedCornerShape(6.dp)
              ) {
                Text(
                  text = plc.ctcLpa,
                  color = EmeraldGreenDark,
                  fontWeight = FontWeight.Bold,
                  fontSize = 13.sp,
                  modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                )
              }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Criteria & Dates
            Row(
              modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(8.dp))
                .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
                .padding(8.dp),
              horizontalArrangement = Arrangement.SpaceBetween
            ) {
              Text("Min CGPA: ${plc.minCgpa}", fontSize = 11.sp, fontWeight = FontWeight.Medium)
              Text("Deadline: ${plc.applicationDeadline}", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = CrimsonRed)
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Skills Chips
            LazyRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
              items(plc.requiredSkills) { skill ->
                Surface(
                  color = PrimaryNavy.copy(alpha = 0.08f),
                  shape = RoundedCornerShape(4.dp)
                ) {
                  Text(skill, fontSize = 10.sp, color = PrimaryNavy, modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp))
                }
              }
            }

            Spacer(modifier = Modifier.height(10.dp))
            Text(plc.description, fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant, lineHeight = 16.sp)

            Spacer(modifier = Modifier.height(12.dp))

            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.CenterVertically
            ) {
              Text("Locations: ${plc.jobLocations.joinToString(", ")}", fontSize = 11.sp, color = PrimaryNavy, fontWeight = FontWeight.SemiBold)

              Button(
                onClick = { onApplyPlacement(plc.id) },
                enabled = !plc.isApplied,
                colors = ButtonDefaults.buttonColors(
                  containerColor = if (plc.isApplied) EmeraldGreenDark else PrimaryNavy
                ),
                shape = RoundedCornerShape(8.dp),
                contentPadding = PaddingValues(horizontal = 14.dp, vertical = 6.dp),
                modifier = Modifier.testTag("apply_placement_btn_${plc.id}")
              ) {
                Text(if (plc.isApplied) "Applied (${plc.applicationStatus})" else "1-Click Apply", fontSize = 12.sp)
              }
            }
          }
        }
      }
    } else {
      items(internships) { intern ->
        Card(
          shape = RoundedCornerShape(16.dp),
          colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
          elevation = CardDefaults.cardElevation(2.dp),
          modifier = Modifier.fillMaxWidth().testTag("internship_card_${intern.id}")
        ) {
          Column(modifier = Modifier.padding(16.dp)) {
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.CenterVertically
            ) {
              Column(modifier = Modifier.weight(1f)) {
                Text(intern.companyName, fontWeight = FontWeight.Bold, fontSize = 15.sp)
                Text(intern.role, fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
              }
              Surface(
                color = SaffronGoldLight,
                shape = RoundedCornerShape(6.dp)
              ) {
                Text(
                  text = intern.stipend,
                  color = SaffronGoldDark,
                  fontWeight = FontWeight.Bold,
                  fontSize = 12.sp,
                  modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                )
              }
            }

            Spacer(modifier = Modifier.height(8.dp))
            Text("Duration: ${intern.duration} • Location: ${intern.location} • Deadline: ${intern.deadline}", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)

            Spacer(modifier = Modifier.height(12.dp))
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.End
            ) {
              Button(
                onClick = { onApplyInternship(intern.id) },
                enabled = !intern.isApplied,
                colors = ButtonDefaults.buttonColors(containerColor = if (intern.isApplied) EmeraldGreenDark else PrimaryNavy),
                shape = RoundedCornerShape(8.dp),
                contentPadding = PaddingValues(horizontal = 14.dp, vertical = 6.dp)
              ) {
                Text(if (intern.isApplied) "Application Sent" else "Apply for Internship", fontSize = 12.sp)
              }
            }
          }
        }
      }
    }
  }
}

// -------------------------------------------------------------
// 2. FEE PAYMENTS & RECEIPTS
// -------------------------------------------------------------
@Composable
private fun FeesSection(
  fees: List<FeeItem>,
  onPayFee: (String) -> Unit
) {
  var selectedReceipt by remember { mutableStateOf<FeeItem?>(null) }
  var paymentSuccessDialog by remember { mutableStateOf<String?>(null) }

  val totalDue = fees.filter { it.status != "PAID" }.sumOf { it.pendingAmount }

  LazyColumn(
    modifier = Modifier.fillMaxSize(),
    contentPadding = PaddingValues(16.dp),
    verticalArrangement = Arrangement.spacedBy(14.dp)
  ) {
    item {
      Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = if (totalDue > 0) PrimaryNavy else EmeraldGreenDark),
        modifier = Modifier.fillMaxWidth()
      ) {
        Column(modifier = Modifier.padding(18.dp)) {
          Text("College Accounts & Dues", color = SaffronGold, fontWeight = FontWeight.Bold, fontSize = 12.sp)
          Text(
            text = if (totalDue > 0) "Pending Dues: ₹ $totalDue" else "All Dues Cleared (No Dues Certificate Active)",
            color = Color.White,
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp
          )
          Spacer(modifier = Modifier.height(4.dp))
          Text(
            text = "Direct payment via UPI (GPay, PhonePe, Paytm), NetBanking & SBI Collect.",
            color = Color.White.copy(alpha = 0.85f),
            fontSize = 11.sp
          )
        }
      }
    }

    items(fees) { fee ->
      Card(
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(1.dp),
        modifier = Modifier.fillMaxWidth().testTag("fee_card_${fee.id}")
      ) {
        Column(modifier = Modifier.padding(16.dp)) {
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Column(modifier = Modifier.weight(1f)) {
              Text(fee.feeCategory, style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold))
              Text("Semester: ${fee.semester} • Due: ${fee.dueDate}", style = MaterialTheme.typography.bodySmall.copy(color = MaterialTheme.colorScheme.onSurfaceVariant))
            }
            Surface(
              color = if (fee.status == "PAID") EmeraldGreenLight else CrimsonRedLight,
              shape = RoundedCornerShape(6.dp)
            ) {
              Text(
                text = fee.status,
                color = if (fee.status == "PAID") EmeraldGreenDark else CrimsonRed,
                fontWeight = FontWeight.Bold,
                fontSize = 11.sp,
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
              )
            }
          }

          Spacer(modifier = Modifier.height(10.dp))

          Row(
            modifier = Modifier
              .fillMaxWidth()
              .clip(RoundedCornerShape(8.dp))
              .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f))
              .padding(10.dp),
            horizontalArrangement = Arrangement.SpaceBetween
          ) {
            Column {
              Text("Total Amount", fontSize = 10.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
              Text("₹ ${fee.totalAmount}", fontWeight = FontWeight.Bold, fontSize = 14.sp)
            }
            Column {
              Text("Paid", fontSize = 10.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
              Text("₹ ${fee.paidAmount}", fontWeight = FontWeight.Bold, fontSize = 14.sp, color = EmeraldGreenDark)
            }
            Column {
              Text("Pending", fontSize = 10.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
              Text("₹ ${fee.pendingAmount}", fontWeight = FontWeight.Bold, fontSize = 14.sp, color = if (fee.pendingAmount > 0) CrimsonRed else EmeraldGreenDark)
            }
          }

          Spacer(modifier = Modifier.height(12.dp))

          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End,
            verticalAlignment = Alignment.CenterVertically
          ) {
            if (fee.receiptNumber != null) {
              OutlinedButton(
                onClick = { selectedReceipt = fee },
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.padding(end = 8.dp)
              ) {
                Icon(Icons.Default.Receipt, contentDescription = null, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(4.dp))
                Text("View Receipt", fontSize = 12.sp)
              }
            }

            if (fee.status != "PAID") {
              Button(
                onClick = {
                  onPayFee(fee.id)
                  paymentSuccessDialog = fee.feeCategory
                },
                colors = ButtonDefaults.buttonColors(containerColor = PrimaryNavy),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.testTag("pay_fee_btn_${fee.id}")
              ) {
                Text("Pay ₹ ${fee.pendingAmount} via UPI", fontSize = 12.sp)
              }
            }
          }
        }
      }
    }
  }

  // Receipt Modal
  selectedReceipt?.let { rec ->
    Dialog(onDismissRequest = { selectedReceipt = null }) {
      Surface(
        shape = RoundedCornerShape(16.dp),
        color = MaterialTheme.colorScheme.surface,
        modifier = Modifier.fillMaxWidth(0.95f)
      ) {
        Column(modifier = Modifier.padding(20.dp)) {
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Text("Official Fee Receipt", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = PrimaryNavy)
            IconButton(onClick = { selectedReceipt = null }) { Icon(Icons.Default.Close, contentDescription = null) }
          }
          Divider(modifier = Modifier.padding(vertical = 8.dp))
          Text("Receipt No: ${rec.receiptNumber}", fontWeight = FontWeight.Bold, fontSize = 13.sp)
          Text("Fee Category: ${rec.feeCategory}", fontSize = 12.sp)
          Text("Amount Paid: ₹ ${rec.paidAmount}", fontWeight = FontWeight.Bold, color = EmeraldGreenDark, fontSize = 14.sp)
          Text("Payment Date: ${rec.paymentDate ?: "05/08/2026"}", fontSize = 12.sp)
          Text("Payment Mode: Indian Unified Payments Interface (UPI)", fontSize = 12.sp)
          Spacer(modifier = Modifier.height(14.dp))
          Button(
            onClick = { selectedReceipt = null },
            colors = ButtonDefaults.buttonColors(containerColor = PrimaryNavy),
            modifier = Modifier.fillMaxWidth()
          ) {
            Text("Download PDF Receipt")
          }
        }
      }
    }
  }

  paymentSuccessDialog?.let { title ->
    AlertDialog(
      onDismissRequest = { paymentSuccessDialog = null },
      title = { Text("Payment Successful!", fontWeight = FontWeight.Bold) },
      text = { Text("Payment for '$title' completed successfully via UPI. Official receipt generated in records.", fontSize = 13.sp) },
      confirmButton = {
        Button(onClick = { paymentSuccessDialog = null }, colors = ButtonDefaults.buttonColors(containerColor = PrimaryNavy)) {
          Text("OK")
        }
      }
    )
  }
}

// -------------------------------------------------------------
// 3. CENTRAL LIBRARY & RACKS
// -------------------------------------------------------------
@Composable
private fun LibrarySection(
  books: List<LibraryBook>,
  onRenewBook: (String) -> Unit
) {
  var searchQuery by remember { mutableStateOf("") }
  var showRenewSuccess by remember { mutableStateOf(false) }

  val filteredCatalog = remember(searchQuery, books) {
    if (searchQuery.isBlank()) books
    else books.filter {
      it.title.contains(searchQuery, ignoreCase = true) ||
        it.author.contains(searchQuery, ignoreCase = true) ||
        it.category.contains(searchQuery, ignoreCase = true)
    }
  }

  LazyColumn(
    modifier = Modifier.fillMaxSize(),
    contentPadding = PaddingValues(16.dp),
    verticalArrangement = Arrangement.spacedBy(14.dp)
  ) {
    item {
      OutlinedTextField(
        value = searchQuery,
        onValueChange = { searchQuery = it },
        placeholder = { Text("Search 65,000+ books, authors, IEEE journals...", fontSize = 12.sp) },
        leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = PrimaryNavy) },
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        singleLine = true
      )
    }

    item {
      Text("Currently Issued to You", style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold))
    }

    items(books.filter { it.isIssuedToMe }) { book ->
      Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        modifier = Modifier.fillMaxWidth().testTag("issued_book_${book.id}")
      ) {
        Column(modifier = Modifier.padding(14.dp)) {
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Column(modifier = Modifier.weight(1f)) {
              Text(book.title, fontWeight = FontWeight.Bold, fontSize = 13.sp)
              Text("Author: ${book.author} • ISBN: ${book.isbn}", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
            Surface(
              color = CrimsonRedLight,
              shape = RoundedCornerShape(4.dp)
            ) {
              Text(
                text = "Due: ${book.dueDate}",
                color = CrimsonRed,
                fontWeight = FontWeight.Bold,
                fontSize = 10.sp,
                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
              )
            }
          }

          Spacer(modifier = Modifier.height(8.dp))

          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Text("Rack: ${book.rackNumber} • Fine: ${book.fineAmount}", fontSize = 11.sp, color = PrimaryNavy, fontWeight = FontWeight.SemiBold)
            Button(
              onClick = {
                onRenewBook(book.id)
                showRenewSuccess = true
              },
              shape = RoundedCornerShape(6.dp),
              colors = ButtonDefaults.buttonColors(containerColor = PrimaryNavy),
              contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp)
            ) {
              Text("Renew (14 Days)", fontSize = 11.sp)
            }
          }
        }
      }
    }

    item {
      Text("Library Catalog & Rack Location", style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold), modifier = Modifier.padding(top = 6.dp))
    }

    items(filteredCatalog.filter { !it.isIssuedToMe }) { book ->
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
          Column(modifier = Modifier.weight(1f)) {
            Text(book.title, fontWeight = FontWeight.Bold, fontSize = 13.sp)
            Text("By ${book.author} • Category: ${book.category}", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
            Spacer(modifier = Modifier.height(4.dp))
            Text("Location: ${book.rackNumber}", fontSize = 11.sp, color = PrimaryNavy, fontWeight = FontWeight.SemiBold)
          }

          Surface(
            color = if (book.availableCopies > 0) EmeraldGreenLight else CrimsonRedLight,
            shape = RoundedCornerShape(6.dp)
          ) {
            Text(
              text = if (book.availableCopies > 0) "${book.availableCopies} Available" else "Issued Out",
              color = if (book.availableCopies > 0) EmeraldGreenDark else CrimsonRed,
              fontWeight = FontWeight.Bold,
              fontSize = 11.sp,
              modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
            )
          }
        }
      }
    }
  }

  if (showRenewSuccess) {
    AlertDialog(
      onDismissRequest = { showRenewSuccess = false },
      title = { Text("Book Loan Extended", fontWeight = FontWeight.Bold) },
      text = { Text("Loan period extended by 14 days. New due date updated in Central Library OPAC database.", fontSize = 13.sp) },
      confirmButton = {
        Button(onClick = { showRenewSuccess = false }, colors = ButtonDefaults.buttonColors(containerColor = PrimaryNavy)) {
          Text("Done")
        }
      }
    )
  }
}

// -------------------------------------------------------------
// 4. CANTEEN PRE-ORDER
// -------------------------------------------------------------
@Composable
private fun CanteenSection(
  menu: List<CanteenMenuItem>,
  cart: List<CanteenCartItem>,
  onAddToCart: (CanteenMenuItem) -> Unit,
  onRemoveFromCart: (CanteenMenuItem) -> Unit,
  onPlaceOrder: () -> String
) {
  var selectedCategory by remember { mutableStateOf("All") }
  var generatedToken by remember { mutableStateOf<String?>(null) }

  val categories = listOf("All", "Breakfast", "Lunch", "Snacks", "Beverages")
  val filteredMenu = remember(selectedCategory, menu) {
    if (selectedCategory == "All") menu
    else menu.filter { it.category.equals(selectedCategory, ignoreCase = true) }
  }

  LazyColumn(
    modifier = Modifier.fillMaxSize(),
    contentPadding = PaddingValues(16.dp),
    verticalArrangement = Arrangement.spacedBy(12.dp)
  ) {
    item {
      LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        items(categories) { cat ->
          FilterChip(
            selected = selectedCategory == cat,
            onClick = { selectedCategory = cat },
            label = { Text(cat) }
          )
        }
      }
    }

    items(filteredMenu) { item ->
      val inCart = cart.find { it.item.id == item.id }?.quantity ?: 0
      Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        modifier = Modifier.fillMaxWidth().testTag("canteen_item_${item.id}")
      ) {
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .padding(14.dp),
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.SpaceBetween
        ) {
          Column(modifier = Modifier.weight(1f)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
              Box(
                modifier = Modifier
                  .size(10.dp)
                  .clip(CircleShape)
                  .background(if (item.isVeg) EmeraldGreen else CrimsonRed)
              )
              Spacer(modifier = Modifier.width(6.dp))
              Text(item.name, fontWeight = FontWeight.Bold, fontSize = 14.sp)
            }
            Text("₹ ${item.price} • ${item.category}", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
          }

          if (inCart > 0) {
            Row(
              verticalAlignment = Alignment.CenterVertically,
              modifier = Modifier
                .clip(RoundedCornerShape(8.dp))
                .background(PrimaryNavy.copy(alpha = 0.1f))
                .padding(horizontal = 4.dp)
            ) {
              IconButton(onClick = { onRemoveFromCart(item) }, modifier = Modifier.size(28.dp)) {
                Icon(Icons.Default.Remove, contentDescription = "Decrease", modifier = Modifier.size(16.dp))
              }
              Text("$inCart", fontWeight = FontWeight.Bold, modifier = Modifier.padding(horizontal = 6.dp))
              IconButton(onClick = { onAddToCart(item) }, modifier = Modifier.size(28.dp)) {
                Icon(Icons.Default.Add, contentDescription = "Increase", modifier = Modifier.size(16.dp))
              }
            }
          } else {
            Button(
              onClick = { onAddToCart(item) },
              colors = ButtonDefaults.buttonColors(containerColor = PrimaryNavy),
              shape = RoundedCornerShape(8.dp),
              contentPadding = PaddingValues(horizontal = 14.dp, vertical = 6.dp),
              modifier = Modifier.testTag("add_to_cart_${item.id}")
            ) {
              Text("ADD", fontSize = 11.sp, fontWeight = FontWeight.Bold)
            }
          }
        }
      }
    }

    // Cart Bar at bottom if items present
    if (cart.isNotEmpty()) {
      val totalCartAmount = cart.sumOf { it.item.price * it.quantity }
      item {
        Card(
          shape = RoundedCornerShape(14.dp),
          colors = CardDefaults.cardColors(containerColor = SaffronGold),
          modifier = Modifier.fillMaxWidth().testTag("canteen_cart_summary")
        ) {
          Row(
            modifier = Modifier
              .fillMaxWidth()
              .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
          ) {
            Column {
              Text("${cart.sumOf { it.quantity }} items in Cart", fontWeight = FontWeight.Bold, color = Color.Black, fontSize = 12.sp)
              Text("Total: ₹ $totalCartAmount", fontWeight = FontWeight.Bold, fontSize = 18.sp, color = Color.Black)
            }

            Button(
              onClick = { generatedToken = onPlaceOrder() },
              colors = ButtonDefaults.buttonColors(containerColor = Color.Black),
              shape = RoundedCornerShape(8.dp),
              modifier = Modifier.testTag("place_canteen_order_button")
            ) {
              Text("Pay & Order", color = Color.White, fontWeight = FontWeight.Bold)
            }
          }
        }
      }
    }
  }

  generatedToken?.let { token ->
    AlertDialog(
      onDismissRequest = { generatedToken = null },
      title = { Text("Order Placed Successfully!", fontWeight = FontWeight.Bold) },
      text = {
        Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {
          Text("Show this token at Canteen Counter #2", fontSize = 12.sp)
          Spacer(modifier = Modifier.height(10.dp))
          Surface(
            color = PrimaryNavy,
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier.padding(8.dp)
          ) {
            Text(token, color = SaffronGold, fontWeight = FontWeight.Bold, fontSize = 24.sp, modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp))
          }
          Text("Preparation time: 8-12 mins", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
      },
      confirmButton = {
        Button(onClick = { generatedToken = null }, colors = ButtonDefaults.buttonColors(containerColor = PrimaryNavy)) {
          Text("Got It")
        }
      }
    )
  }
}

// -------------------------------------------------------------
// 5. HOSTEL & MESS
// -------------------------------------------------------------
@Composable
private fun HostelSection(
  hostel: HostelInfo,
  onSubmitComplaint: (String, String) -> Unit
) {
  var showComplaintDialog by remember { mutableStateOf(false) }

  LazyColumn(
    modifier = Modifier.fillMaxSize(),
    contentPadding = PaddingValues(16.dp),
    verticalArrangement = Arrangement.spacedBy(14.dp)
  ) {
    item {
      Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        modifier = Modifier.fillMaxWidth()
      ) {
        Column(modifier = Modifier.padding(16.dp)) {
          Text("Room Allocation & Mess", fontWeight = FontWeight.Bold, fontSize = 16.sp)
          Spacer(modifier = Modifier.height(8.dp))
          Text("• Hostel Block: ${hostel.blockName}", fontSize = 13.sp)
          Text("• Room Number: ${hostel.roomNumber}", fontSize = 13.sp)
          Text("• Room Type: ${hostel.roomType}", fontSize = 13.sp)
          Text("• Roommates: ${hostel.roommates.joinToString()}", fontSize = 13.sp)
          Text("• Chief Warden: ${hostel.wardenName} (Tel: ${hostel.wardenContact})", fontSize = 12.sp)
        }
      }
    }

    item {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Text("Weekly Mess Menu (Catered)", style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold))
        Button(
          onClick = { showComplaintDialog = true },
          colors = ButtonDefaults.buttonColors(containerColor = CrimsonRed),
          shape = RoundedCornerShape(8.dp),
          contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp)
        ) {
          Text("Hostel Complaint", fontSize = 11.sp)
        }
      }
    }

    items(hostel.weeklyMessMenu) { menuDay ->
      Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        modifier = Modifier.fillMaxWidth()
      ) {
        Column(modifier = Modifier.padding(14.dp)) {
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
          ) {
            Text(menuDay.day, fontWeight = FontWeight.Bold, fontSize = 14.sp, color = PrimaryNavy)
            if (menuDay.isSpecial) {
              Surface(color = SaffronGoldLight, shape = RoundedCornerShape(4.dp)) {
                Text("SPECIAL FEAST", color = SaffronGoldDark, fontSize = 9.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp))
              }
            }
          }
          Spacer(modifier = Modifier.height(6.dp))
          Text("Breakfast: ${menuDay.breakfast}", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
          Text("Lunch: ${menuDay.lunch}", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
          Text("Snacks: ${menuDay.snacks}", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
          Text("Dinner: ${menuDay.dinner}", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
      }
    }

    item {
      Text("Active Maintenance Complaints", style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold), modifier = Modifier.padding(top = 6.dp))
    }

    items(hostel.activeComplaints) { cmp ->
      Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        modifier = Modifier.fillMaxWidth()
      ) {
        Row(
          modifier = Modifier.fillMaxWidth().padding(14.dp),
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.SpaceBetween
        ) {
          Column(modifier = Modifier.weight(1f)) {
            Text(cmp.category, fontWeight = FontWeight.Bold, fontSize = 13.sp)
            Text(cmp.description, fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
            Text("Reported: ${cmp.reportedDate}", fontSize = 10.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
          }
          Surface(
            color = if (cmp.status == "Resolved") EmeraldGreenLight else SaffronGoldLight,
            shape = RoundedCornerShape(4.dp)
          ) {
            Text(
              text = cmp.status,
              color = if (cmp.status == "Resolved") EmeraldGreenDark else SaffronGoldDark,
              fontWeight = FontWeight.Bold,
              fontSize = 10.sp,
              modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
            )
          }
        }
      }
    }
  }

  if (showComplaintDialog) {
    var cat by remember { mutableStateOf("Electrical") }
    var desc by remember { mutableStateOf("") }

    AlertDialog(
      onDismissRequest = { showComplaintDialog = false },
      title = { Text("Log Hostel Complaint", fontWeight = FontWeight.Bold) },
      text = {
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
          OutlinedTextField(value = cat, onValueChange = { cat = it }, label = { Text("Category (Electrical, Plumbing, Wi-Fi)") }, modifier = Modifier.fillMaxWidth())
          OutlinedTextField(value = desc, onValueChange = { desc = it }, label = { Text("Issue Description") }, modifier = Modifier.fillMaxWidth())
        }
      },
      confirmButton = {
        Button(
          onClick = {
            if (desc.isNotBlank()) onSubmitComplaint(cat, desc)
            showComplaintDialog = false
          },
          colors = ButtonDefaults.buttonColors(containerColor = PrimaryNavy)
        ) {
          Text("Submit")
        }
      },
      dismissButton = { TextButton(onClick = { showComplaintDialog = false }) { Text("Cancel") } }
    )
  }
}

// -------------------------------------------------------------
// 6. TRANSPORT & BUS PASS
// -------------------------------------------------------------
@Composable
private fun TransportSection() {
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
          Text("College Transport Fleet (35 Buses)", color = SaffronGold, fontWeight = FontWeight.Bold, fontSize = 12.sp)
          Text("My Bus Pass: Active (Route 12)", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 16.sp)
          Spacer(modifier = Modifier.height(4.dp))
          Text("Pickup: Gandhipuram Bus Stand @ 07:30 AM • Return: 04:45 PM", color = Color.White.copy(alpha = 0.85f), fontSize = 11.sp)
        }
      }
    }

    item {
      Text("All College Bus Routes & Drivers", style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold))
    }

    items(CampusMockData.sampleBusRoutes) { route ->
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
            Text("${route.routeNumber} (${route.startLocation})", fontWeight = FontWeight.Bold, fontSize = 14.sp)
            Surface(color = PrimaryNavy.copy(alpha = 0.1f), shape = RoundedCornerShape(4.dp)) {
              Text(route.busNumber, fontSize = 10.sp, color = PrimaryNavy, fontWeight = FontWeight.Bold, modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp))
            }
          }

          Spacer(modifier = Modifier.height(6.dp))
          Text("Via: ${route.keyStops.joinToString(" ➔ ")}", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
          Spacer(modifier = Modifier.height(4.dp))
          Text("Departure: ${route.departureTime} • Driver: ${route.driverName} (Tel: ${route.driverPhone})", fontSize = 11.sp, color = PrimaryNavy, fontWeight = FontWeight.SemiBold)
        }
      }
    }
  }
}

// -------------------------------------------------------------
// 7. GRIEVANCE CELL
// -------------------------------------------------------------
@Composable
private fun GrievanceSection(
  grievances: List<GrievanceTicket>,
  onSubmit: (String, String, String, Boolean) -> Unit
) {
  var showNewTicketDialog by remember { mutableStateOf(false) }

  LazyColumn(
    modifier = Modifier.fillMaxSize(),
    contentPadding = PaddingValues(16.dp),
    verticalArrangement = Arrangement.spacedBy(14.dp)
  ) {
    item {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Column {
          Text("UGC & AICTE Grievance Cell", style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold))
          Text("Confidential & transparent resolution", style = MaterialTheme.typography.bodySmall.copy(color = MaterialTheme.colorScheme.onSurfaceVariant))
        }
        Button(
          onClick = { showNewTicketDialog = true },
          colors = ButtonDefaults.buttonColors(containerColor = PrimaryNavy),
          shape = RoundedCornerShape(8.dp)
        ) {
          Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(16.dp))
          Spacer(modifier = Modifier.width(4.dp))
          Text("File Grievance", fontSize = 12.sp)
        }
      }
    }

    items(grievances) { gv ->
      Card(
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        modifier = Modifier.fillMaxWidth().testTag("grievance_card_${gv.ticketId}")
      ) {
        Column(modifier = Modifier.padding(14.dp)) {
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Text("Ticket #${gv.ticketId} • ${gv.category}", fontWeight = FontWeight.Bold, fontSize = 12.sp, color = PrimaryNavy)
            Surface(
              color = when (gv.status) {
                "RESOLVED" -> EmeraldGreenLight
                "IN PROGRESS" -> SaffronGoldLight
                else -> Color(0xFFE2E8F0)
              },
              shape = RoundedCornerShape(4.dp)
            ) {
              Text(
                text = gv.status,
                color = when (gv.status) {
                  "RESOLVED" -> EmeraldGreenDark
                  "IN PROGRESS" -> SaffronGoldDark
                  else -> Color(0xFF475569)
                },
                fontWeight = FontWeight.Bold,
                fontSize = 10.sp,
                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
              )
            }
          }

          Spacer(modifier = Modifier.height(6.dp))
          Text(gv.subject, fontWeight = FontWeight.Bold, fontSize = 14.sp)
          Spacer(modifier = Modifier.height(2.dp))
          Text(gv.description, fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)

          if (gv.resolutionNotes != null) {
            Spacer(modifier = Modifier.height(8.dp))
            Surface(
              color = EmeraldGreenLight,
              shape = RoundedCornerShape(6.dp),
              modifier = Modifier.fillMaxWidth()
            ) {
              Text(
                text = "Resolution: ${gv.resolutionNotes}",
                fontSize = 11.sp,
                color = EmeraldGreenDark,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.padding(8.dp)
              )
            }
          }
        }
      }
    }
  }

  if (showNewTicketDialog) {
    var category by remember { mutableStateOf("Academic") }
    var subject by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var isAnonymous by remember { mutableStateOf(false) }

    AlertDialog(
      onDismissRequest = { showNewTicketDialog = false },
      title = { Text("Submit Grievance Redressal Ticket", fontWeight = FontWeight.Bold) },
      text = {
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
          OutlinedTextField(value = category, onValueChange = { category = it }, label = { Text("Category (Academic, Hostel, Transport, Lab)") }, modifier = Modifier.fillMaxWidth())
          OutlinedTextField(value = subject, onValueChange = { subject = it }, label = { Text("Grievance Subject") }, modifier = Modifier.fillMaxWidth())
          OutlinedTextField(value = description, onValueChange = { description = it }, label = { Text("Detailed Description") }, modifier = Modifier.fillMaxWidth(), minLines = 3)
          Row(verticalAlignment = Alignment.CenterVertically) {
            Checkbox(checked = isAnonymous, onCheckedChange = { isAnonymous = it })
            Text("Submit as Anonymous Student", fontSize = 12.sp)
          }
        }
      },
      confirmButton = {
        Button(
          onClick = {
            if (subject.isNotBlank()) onSubmit(category, subject, description, isAnonymous)
            showNewTicketDialog = false
          },
          colors = ButtonDefaults.buttonColors(containerColor = PrimaryNavy)
        ) {
          Text("Submit Ticket")
        }
      },
      dismissButton = { TextButton(onClick = { showNewTicketDialog = false }) { Text("Cancel") } }
    )
  }
}
