package com.example.data.repository

import com.example.data.mock.CampusMockData
import com.example.data.model.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.text.SimpleDateFormat
import java.util.*

class CampusRepository {

  private val _currentUser = MutableStateFlow(CampusMockData.sampleStudent)
  val currentUser: StateFlow<UserProfile> = _currentUser.asStateFlow()

  private val _attendanceSummary = MutableStateFlow(
    AttendanceSummary(
      overallPercentage = 86.4,
      totalDelivered = 340,
      totalAttended = 286,
      totalOD = 8,
      totalLeaves = 6,
      minimumRequired = 75.0,
      subjects = CampusMockData.sampleSubjectAttendance,
      recentLogs = CampusMockData.sampleAttendanceLogs
    )
  )
  val attendanceSummary: StateFlow<AttendanceSummary> = _attendanceSummary.asStateFlow()

  private val _assignments = MutableStateFlow(CampusMockData.sampleAssignments)
  val assignments: StateFlow<List<AssignmentItem>> = _assignments.asStateFlow()

  private val _events = MutableStateFlow(CampusMockData.sampleEvents)
  val events: StateFlow<List<CollegeEvent>> = _events.asStateFlow()

  private val _clubs = MutableStateFlow(CampusMockData.sampleClubs)
  val clubs: StateFlow<List<CampusClub>> = _clubs.asStateFlow()

  private val _announcements = MutableStateFlow(CampusMockData.sampleAnnouncements)
  val announcements: StateFlow<List<AnnouncementItem>> = _announcements.asStateFlow()

  private val _notifications = MutableStateFlow(CampusMockData.sampleNotifications)
  val notifications: StateFlow<List<NotificationItem>> = _notifications.asStateFlow()

  private val _placements = MutableStateFlow(CampusMockData.samplePlacements)
  val placements: StateFlow<List<PlacementDrive>> = _placements.asStateFlow()

  private val _internships = MutableStateFlow(CampusMockData.sampleInternships)
  val internships: StateFlow<List<InternshipItem>> = _internships.asStateFlow()

  private val _lostAndFound = MutableStateFlow(CampusMockData.sampleLostFound)
  val lostAndFound: StateFlow<List<LostFoundItem>> = _lostAndFound.asStateFlow()

  private val _marketplace = MutableStateFlow(CampusMockData.sampleMarketplace)
  val marketplace: StateFlow<List<MarketplaceItem>> = _marketplace.asStateFlow()

  private val _communityPosts = MutableStateFlow(CampusMockData.sampleCommunityPosts)
  val communityPosts: StateFlow<List<CommunityPost>> = _communityPosts.asStateFlow()

  private val _libraryBooks = MutableStateFlow(CampusMockData.sampleLibraryBooks)
  val libraryBooks: StateFlow<List<LibraryBook>> = _libraryBooks.asStateFlow()

  private val _fees = MutableStateFlow(CampusMockData.sampleFees)
  val fees: StateFlow<List<FeeItem>> = _fees.asStateFlow()

  private val _hostelInfo = MutableStateFlow(CampusMockData.sampleHostelInfo)
  val hostelInfo: StateFlow<HostelInfo> = _hostelInfo.asStateFlow()

  private val _canteenMenu = MutableStateFlow(CampusMockData.sampleCanteenMenu)
  val canteenMenu: StateFlow<List<CanteenMenuItem>> = _canteenMenu.asStateFlow()

  private val _canteenCart = MutableStateFlow<List<CanteenCartItem>>(emptyList())
  val canteenCart: StateFlow<List<CanteenCartItem>> = _canteenCart.asStateFlow()

  private val _recentOrders = MutableStateFlow<List<Pair<String, List<CanteenCartItem>>>>(emptyList())
  val recentOrders: StateFlow<List<Pair<String, List<CanteenCartItem>>>> = _recentOrders.asStateFlow()

  private val _grievances = MutableStateFlow(CampusMockData.sampleGrievances)
  val grievances: StateFlow<List<GrievanceTicket>> = _grievances.asStateFlow()

  private val _certificates = MutableStateFlow(CampusMockData.sampleCertificates)
  val certificates: StateFlow<List<CertificateItem>> = _certificates.asStateFlow()

  private val _skillProfile = MutableStateFlow(
    StudentSkillProfile(
      keyProjects = CampusMockData.sampleProjects
    )
  )
  val skillProfile: StateFlow<StudentSkillProfile> = _skillProfile.asStateFlow()

  private val _aiChatMessages = MutableStateFlow(
    listOf(
      CampusAIMessage(
        id = "MSG-1",
        sender = "AI",
        messageText = "Namaste Bala Murugan! I am Campus AI, your 24/7 college companion. How can I help you today with your timetable, attendance, exams, placements, or campus services?",
        timestamp = "Just now",
        suggestedPrompts = listOf("Today's Timetable", "Attendance Status", "Upcoming Exams", "Mess Lunch Menu", "Placement Drives"),
        isOfficialSource = true
      )
    )
  )
  val aiChatMessages: StateFlow<List<CampusAIMessage>> = _aiChatMessages.asStateFlow()

  // Switch role seamlessly
  fun switchRole(newRole: UserRole) {
    when (newRole) {
      UserRole.STUDENT -> _currentUser.value = CampusMockData.sampleStudent
      UserRole.FACULTY -> _currentUser.value = CampusMockData.sampleFaculty
      UserRole.ADMIN -> _currentUser.value = CampusMockData.sampleAdmin
      UserRole.PLACEMENT_TEAM -> _currentUser.value = CampusMockData.sampleAdmin.copy(
        fullName = "Dr. K. Jayaraman",
        designation = "Director - Placements & Corporate Relations",
        role = UserRole.PLACEMENT_TEAM
      )
      UserRole.CLUB_LEAD -> _currentUser.value = CampusMockData.sampleStudent.copy(
        fullName = "Rahul Raj (Club Secretary)",
        role = UserRole.CLUB_LEAD
      )
    }
  }

  // Attendance interactions
  fun submitLeaveOrOD(type: AttendanceStatus, reason: String, date: String) {
    val newLog = AttendanceDayLog(date, "Selected Day", type, "Application submitted: $reason")
    val currentLogs = _attendanceSummary.value.recentLogs.toMutableList()
    currentLogs.add(0, newLog)
    _attendanceSummary.value = _attendanceSummary.value.copy(
      recentLogs = currentLogs,
      totalOD = if (type == AttendanceStatus.OD) _attendanceSummary.value.totalOD + 1 else _attendanceSummary.value.totalOD,
      totalLeaves = if (type == AttendanceStatus.LEAVE) _attendanceSummary.value.totalLeaves + 1 else _attendanceSummary.value.totalLeaves
    )
  }

  // Faculty Attendance Update
  fun facultyUpdateAttendance(subjectCode: String, attended: Boolean) {
    val updatedSubjects = _attendanceSummary.value.subjects.map { subj ->
      if (subj.subjectCode == subjectCode) {
        subj.copy(
          totalClasses = subj.totalClasses + 1,
          attendedClasses = if (attended) subj.attendedClasses + 1 else subj.attendedClasses
        )
      } else subj
    }
    _attendanceSummary.value = _attendanceSummary.value.copy(subjects = updatedSubjects)
  }

  // Assignment submission
  fun submitAssignment(assignmentId: String, fileName: String) {
    _assignments.value = _assignments.value.map { asg ->
      if (asg.id == assignmentId) {
        asg.copy(
          isSubmitted = true,
          submissionDate = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Date()),
          attachedFileUrl = fileName
        )
      } else asg
    }
  }

  // Faculty grade assignment
  fun gradeAssignment(assignmentId: String, marks: Int, feedback: String) {
    _assignments.value = _assignments.value.map { asg ->
      if (asg.id == assignmentId) {
        asg.copy(
          obtainedMarks = marks,
          facultyFeedback = feedback
        )
      } else asg
    }
  }

  // Event Registration
  fun toggleEventRegistration(eventId: String) {
    _events.value = _events.value.map { evt ->
      if (evt.id == eventId) {
        val willRegister = !evt.isRegistered
        evt.copy(
          isRegistered = willRegister,
          totalParticipants = if (willRegister) evt.totalParticipants + 1 else evt.totalParticipants - 1,
          qrPassCode = if (willRegister) "CC-${evt.id}-${_currentUser.value.rollNumber}-ENTRY" else null
        )
      } else evt
    }
  }

  // Club Join
  fun toggleClubMembership(clubId: String) {
    _clubs.value = _clubs.value.map { club ->
      if (club.id == clubId) {
        val willJoin = !club.isJoined
        club.copy(
          isJoined = willJoin,
          memberCount = if (willJoin) club.memberCount + 1 else club.memberCount - 1
        )
      } else club
    }
  }

  // Placement Application
  fun applyPlacement(placementId: String) {
    _placements.value = _placements.value.map { plc ->
      if (plc.id == placementId) {
        plc.copy(
          isApplied = true,
          applicationStatus = "Application Submitted (Under Review)"
        )
      } else plc
    }
  }

  // Internship Application
  fun applyInternship(internshipId: String) {
    _internships.value = _internships.value.map { intItem ->
      if (intItem.id == internshipId) {
        intItem.copy(isApplied = true)
      } else intItem
    }
  }

  // Community interactions
  fun togglePostLike(postId: String) {
    _communityPosts.value = _communityPosts.value.map { post ->
      if (post.id == postId) {
        val newLiked = !post.isLiked
        post.copy(
          isLiked = newLiked,
          likesCount = if (newLiked) post.likesCount + 1 else post.likesCount - 1
        )
      } else post
    }
  }

  fun togglePostSave(postId: String) {
    _communityPosts.value = _communityPosts.value.map { post ->
      if (post.id == postId) post.copy(isSaved = !post.isSaved) else post
    }
  }

  fun addCommunityPost(title: String, body: String, category: String) {
    val newPost = CommunityPost(
      id = "POST-${System.currentTimeMillis()}",
      authorName = _currentUser.value.fullName,
      authorDept = "${_currentUser.value.department} (${_currentUser.value.year})",
      authorYear = "2023 - 2027",
      title = title,
      body = body,
      category = category,
      timestamp = "Just now",
      likesCount = 0,
      commentsCount = 0,
      isLiked = false
    )
    val list = _communityPosts.value.toMutableList()
    list.add(0, newPost)
    _communityPosts.value = list
  }

  fun addComment(postId: String, commentText: String) {
    _communityPosts.value = _communityPosts.value.map { post ->
      if (post.id == postId) {
        val newComments = post.comments.toMutableList()
        newComments.add(
          CommunityComment(
            id = "COMM-${System.currentTimeMillis()}",
            authorName = _currentUser.value.fullName,
            text = commentText,
            timestamp = "Just now"
          )
        )
        post.copy(
          comments = newComments,
          commentsCount = post.commentsCount + 1
        )
      } else post
    }
  }

  // Lost & Found
  fun reportLostOrFound(type: String, title: String, category: String, location: String, description: String, phone: String) {
    val newItem = LostFoundItem(
      id = "LF-${System.currentTimeMillis()}",
      type = type,
      title = title,
      category = category,
      locationFoundOrLost = location,
      reportedDate = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Date()),
      description = description,
      reportedBy = _currentUser.value.fullName,
      contactPhone = phone,
      isRecovered = false
    )
    val list = _lostAndFound.value.toMutableList()
    list.add(0, newItem)
    _lostAndFound.value = list
  }

  fun markLostFoundRecovered(id: String) {
    _lostAndFound.value = _lostAndFound.value.map {
      if (it.id == id) it.copy(isRecovered = true) else it
    }
  }

  // Marketplace
  fun addMarketplaceItem(title: String, category: String, price: String, condition: String, description: String, phone: String) {
    val newItem = MarketplaceItem(
      id = "MKT-${System.currentTimeMillis()}",
      title = title,
      category = category,
      price = if (price.startsWith("₹")) price else "₹ $price",
      condition = condition,
      sellerName = _currentUser.value.fullName,
      sellerDept = _currentUser.value.department,
      contactNumber = phone,
      description = description
    )
    val list = _marketplace.value.toMutableList()
    list.add(0, newItem)
    _marketplace.value = list
  }

  // Library Book Renewal
  fun renewLibraryBook(bookId: String) {
    _libraryBooks.value = _libraryBooks.value.map { book ->
      if (book.id == bookId) {
        book.copy(
          dueDate = "20/09/2026",
          fineAmount = "₹ 0"
        )
      } else book
    }
  }

  // Fee Payment Simulation
  fun payFee(feeId: String) {
    _fees.value = _fees.value.map { fee ->
      if (fee.id == feeId) {
        val now = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Date())
        fee.copy(
          paidAmount = fee.totalAmount,
          pendingAmount = 0,
          status = "PAID",
          receiptNumber = "REC-2026-UPI-${(1000..9999).random()}",
          paymentDate = now
        )
      } else fee
    }
  }

  // Hostel Complaint & Mess
  fun submitHostelComplaint(category: String, description: String) {
    val newComplaint = HostelComplaint(
      id = "HST-CMP-${(10..99).random()}",
      category = category,
      description = description,
      reportedDate = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Date()),
      status = "Pending Assignment"
    )
    val currentComplaints = _hostelInfo.value.activeComplaints.toMutableList()
    currentComplaints.add(0, newComplaint)
    _hostelInfo.value = _hostelInfo.value.copy(activeComplaints = currentComplaints)
  }

  // Canteen Cart & Order
  fun addToCart(item: CanteenMenuItem) {
    val current = _canteenCart.value.toMutableList()
    val existing = current.indexOfFirst { it.item.id == item.id }
    if (existing >= 0) {
      current[existing] = current[existing].copy(quantity = current[existing].quantity + 1)
    } else {
      current.add(CanteenCartItem(item, 1))
    }
    _canteenCart.value = current
  }

  fun removeFromCart(item: CanteenMenuItem) {
    val current = _canteenCart.value.toMutableList()
    val existing = current.indexOfFirst { it.item.id == item.id }
    if (existing >= 0) {
      if (current[existing].quantity > 1) {
        current[existing] = current[existing].copy(quantity = current[existing].quantity - 1)
      } else {
        current.removeAt(existing)
      }
    }
    _canteenCart.value = current
  }

  fun placeCanteenOrder(): String {
    val tokenNumber = "TOKEN #${(101..999).random()}"
    val orderList = _recentOrders.value.toMutableList()
    orderList.add(0, Pair(tokenNumber, _canteenCart.value))
    _recentOrders.value = orderList
    _canteenCart.value = emptyList()
    return tokenNumber
  }

  // Grievances
  fun submitGrievance(category: String, subject: String, description: String, anonymous: Boolean) {
    val newTicket = GrievanceTicket(
      ticketId = "GRV-2026-${(100..999).random()}",
      category = category,
      subject = subject,
      description = description,
      submittedDate = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Date()),
      status = "SUBMITTED",
      isAnonymous = anonymous,
      resolutionNotes = "Ticket registered with Student Affairs Office. Expected resolution within 48 hours."
    )
    val list = _grievances.value.toMutableList()
    list.add(0, newTicket)
    _grievances.value = list
  }

  // Notifications
  fun markNotificationRead(id: String) {
    _notifications.value = _notifications.value.map {
      if (it.id == id) it.copy(isRead = true) else it
    }
  }

  fun markAllNotificationsRead() {
    _notifications.value = _notifications.value.map { it.copy(isRead = true) }
  }

  // AI Assistant Query
  fun sendAIMessage(userQuery: String) {
    val currentMsgs = _aiChatMessages.value.toMutableList()
    val userMsg = CampusAIMessage(
      id = "USER-${System.currentTimeMillis()}",
      sender = "USER",
      messageText = userQuery,
      timestamp = "Just now"
    )
    currentMsgs.add(userMsg)

    // Generate intelligent contextual response
    val lower = userQuery.lowercase()
    val responseText = when {
      lower.contains("timetable") || lower.contains("class") || lower.contains("period") -> CampusMockData.sampleAIResponses["timetable"]!!
      lower.contains("attendance") || lower.contains("present") || lower.contains("shortage") -> CampusMockData.sampleAIResponses["attendance"]!!
      lower.contains("exam") || lower.contains("internal") || lower.contains("seat") || lower.contains("hall") -> CampusMockData.sampleAIResponses["exams"]!!
      lower.contains("mess") || lower.contains("food") || lower.contains("lunch") || lower.contains("canteen") -> CampusMockData.sampleAIResponses["mess"]!!
      lower.contains("placement") || lower.contains("job") || lower.contains("company") || lower.contains("package") -> CampusMockData.sampleAIResponses["placement"]!!
      lower.contains("fee") || lower.contains("tuition") || lower.contains("receipt") -> CampusMockData.sampleAIResponses["fees"]!!
      lower.contains("library") || lower.contains("book") -> "You have 2 issued books: 'Database System Concepts' and 'Computer Networking'. Due on 06/09/2026 and 02/09/2026. You can renew them in the Library tab."
      lower.contains("bus") || lower.contains("transport") -> "College buses depart at 07:30 AM from Gandhipuram, Ukkadam, and Pollachi. Your bus pass TN 38 BG 4510 is active."
      lower.contains("principal") || lower.contains("office") || lower.contains("contact") -> "Principal Office: Main Admin Block, Ground Floor | Phone: 0422-2591001. Timings: 09:00 AM - 05:00 PM."
      else -> "I can assist you with your academic schedule, live attendance calculations, internal marks, mess menus, bus routes, placement eligibility, or library book renewal. Feel free to tap one of the suggested topics!"
    }

    val aiMsg = CampusAIMessage(
      id = "AI-${System.currentTimeMillis()}",
      sender = "AI",
      messageText = responseText,
      timestamp = "Just now",
      suggestedPrompts = listOf("Check Attendance Shortage", "Tomorrow's Classes", "Placement Cutoff", "Mess Food Menu"),
      isOfficialSource = true
    )
    currentMsgs.add(aiMsg)
    _aiChatMessages.value = currentMsgs
  }

  // Update profile
  fun updateProfile(name: String, phone: String, bloodGroup: String) {
    _currentUser.value = _currentUser.value.copy(
      fullName = name,
      phoneNumber = phone,
      bloodGroup = bloodGroup
    )
  }
}
