package com.example.data.model

enum class UserRole(val displayName: String, val badgeColor: Long) {
  STUDENT("Student", 0xFF2563EB),
  FACULTY("Faculty", 0xFF059669),
  ADMIN("College Admin", 0xFF7C3AED),
  PLACEMENT_TEAM("Placement Cell", 0xFFD97706),
  CLUB_LEAD("Club Coordinator", 0xFFEA580C)
}

data class UserProfile(
  val id: String,
  val fullName: String,
  val rollNumber: String, // Register number e.g. 21052421012
  val email: String,
  val phoneNumber: String,
  val department: String, // Computer Science and Engineering
  val degree: String = "B.E.",
  val year: String = "III Year",
  val semester: String = "V Semester",
  val section: String = "A",
  val collegeName: String = "National Institute of Technology & Science (Autonomous), Coimbatore",
  val affiliation: String = "Approved by AICTE | Affiliated to Anna University | NAAC A++ Grade",
  val role: UserRole = UserRole.STUDENT,
  val cgpa: Double = 8.74,
  val currentSgpa: Double = 8.92,
  val bloodGroup: String = "O +ve",
  val dob: String = "14/08/2004",
  val validityDate: String = "31/05/2027",
  val emergencyContact: String = "+91 94431 87650",
  val designation: String = "Undergraduate Scholar", // For faculty: "Professor & Head"
  val cabinNumber: String = "CS-304",
  val avatarInitials: String = "BM"
)

data class SubjectAttendance(
  val subjectCode: String,
  val subjectName: String,
  val facultyName: String,
  val totalClasses: Int,
  val attendedClasses: Int,
  val odCount: Int = 2,
  val leaveCount: Int = 1
) {
  val percentage: Double
    get() = if (totalClasses > 0) ((attendedClasses + odCount).toDouble() / totalClasses) * 100.0 else 0.0

  val isShortage: Boolean
    get() = percentage < 75.0
}

data class AttendanceSummary(
  val overallPercentage: Double = 86.4,
  val totalDelivered: Int = 340,
  val totalAttended: Int = 286,
  val totalOD: Int = 8,
  val totalLeaves: Int = 6,
  val minimumRequired: Double = 75.0,
  val subjects: List<SubjectAttendance> = emptyList(),
  val recentLogs: List<AttendanceDayLog> = emptyList()
)

data class AttendanceDayLog(
  val date: String,
  val day: String,
  val status: AttendanceStatus, // PRESENT, ABSENT, OD, LEAVE, HOLIDAY
  val periodDetails: String
)

enum class AttendanceStatus(val label: String, val color: Long) {
  PRESENT("Present", 0xFF10B981),
  ABSENT("Absent", 0xFFEF4444),
  OD("On-Duty (OD)", 0xFF3B82F6),
  LEAVE("Approved Leave", 0xFFF59E0B),
  HOLIDAY("Holiday", 0xFF8B5CF6)
}

data class TimetableSlot(
  val periodNumber: Int,
  val timeSlot: String, // e.g. "09:00 AM - 09:50 AM"
  val subjectCode: String,
  val subjectName: String,
  val facultyName: String,
  val roomNumber: String,
  val isLab: Boolean = false,
  val isFreePeriod: Boolean = false,
  val dayOfWeek: String = "Monday"
)

data class AcademicSubject(
  val code: String,
  val name: String,
  val credits: Int,
  val facultyName: String,
  val internal1Score: Double, // out of 50
  val internal2Score: Double, // out of 50
  val assignmentScore: Double, // out of 10
  val modelExamScore: Double, // out of 100
  val semesterGrade: String = "A+",
  val gradePoints: Int = 9
)

data class SemesterResult(
  val semesterNumber: Int,
  val semesterLabel: String,
  val sgpa: Double,
  val creditsEarned: Int,
  val totalCredits: Int,
  val status: String = "PASSED (First Class with Distinction)"
)

data class AssignmentItem(
  val id: String,
  val subjectCode: String,
  val subjectName: String,
  val title: String,
  val description: String,
  val facultyName: String,
  val dueDate: String,
  val isSubmitted: Boolean,
  val submissionDate: String? = null,
  val maxMarks: Int = 20,
  val obtainedMarks: Int? = null,
  val facultyFeedback: String? = null,
  val attachedFileUrl: String? = null,
  val isLate: Boolean = false
)

data class ExamScheduleItem(
  val id: String,
  val examType: String, // "Internal Assessment - II", "Model Examination", "End Semester Theory"
  val subjectCode: String,
  val subjectName: String,
  val examDate: String, // DD/MM/YYYY
  val session: String, // "FN (09:30 AM - 12:30 PM)"
  val hallNumber: String,
  val seatNumber: String,
  val invigilator: String,
  val instructions: String = "Bring College ID Card and Hall Ticket. Scientific calculator allowed (Non-programmable)."
)

enum class EventCategory(val label: String) {
  ALL("All"),
  TECHNICAL("Technical"),
  CULTURAL("Cultural"),
  HACKATHON("Hackathons"),
  WORKSHOP("Workshops"),
  SPORTS("Sports & Games"),
  SYMPOSIUM("Symposiums")
}

data class CollegeEvent(
  val id: String,
  val title: String,
  val category: EventCategory,
  val organizerClub: String,
  val department: String,
  val eventDate: String,
  val eventTime: String,
  val venue: String,
  val registrationFee: String, // "Free" or "₹150 / Team"
  val description: String,
  val totalParticipants: Int,
  val maxCapacity: Int,
  val isRegistered: Boolean = false,
  val qrPassCode: String? = null,
  val bannerColor: Long = 0xFF1E3A8A,
  val coordinators: String = "Karthik R (Sec) & Harini M"
)

data class CampusClub(
  val id: String,
  val name: String,
  val acronym: String,
  val category: String, // Technical, Cultural, Social Service
  val staffAdvisor: String,
  val studentPresident: String,
  val memberCount: Int,
  val description: String,
  val achievements: List<String>,
  val isJoined: Boolean = false,
  val recruitmentOpen: Boolean = true,
  val themeColor: Long = 0xFF2563EB
)

data class AnnouncementItem(
  val id: String,
  val title: String,
  val category: String, // "Examinations", "Placements", "Circular", "Emergency", "Events"
  val issuedBy: String,
  val publishDate: String,
  val content: String,
  val isPinned: Boolean = false,
  val priorityTag: String = "Normal"
)

data class NotificationItem(
  val id: String,
  val category: String,
  val title: String,
  val message: String,
  val timestamp: String,
  val isRead: Boolean = false
)

data class PlacementDrive(
  val id: String,
  val companyName: String,
  val role: String,
  val ctcLpa: String, // "₹ 14.5 LPA" or "₹ 8.5 LPA"
  val jobLocations: List<String>, // Chennai, Bengaluru, Hyderabad, Pune
  val minCgpa: Double,
  val eligibleDepts: List<String>,
  val applicationDeadline: String,
  val description: String,
  val requiredSkills: List<String>,
  val isApplied: Boolean = false,
  val applicationStatus: String = "Not Applied", // "Applied", "Shortlisted for Round 2", "Interview Scheduled"
  val interviewDate: String? = null
)

data class InternshipItem(
  val id: String,
  val companyName: String,
  val role: String,
  val duration: String, // "3 Months" / "6 Months"
  val stipend: String, // "₹ 25,000 / month"
  val location: String, // "Bengaluru (Hybrid)" / "Chennai (On-site)"
  val skillsRequired: List<String>,
  val deadline: String,
  val isApplied: Boolean = false
)

data class LostFoundItem(
  val id: String,
  val type: String, // "LOST" or "FOUND"
  val title: String,
  val category: String, // Electronics, Calculator, ID Card, Keys, Book
  val locationFoundOrLost: String,
  val reportedDate: String,
  val description: String,
  val reportedBy: String,
  val contactPhone: String,
  val isRecovered: Boolean = false
)

data class MarketplaceItem(
  val id: String,
  val title: String,
  val category: String, // Books, Calculators, Lab Equipment, Electronics, Bicycle
  val price: String, // "₹ 450"
  val condition: String, // "Like New", "Good", "Fair"
  val sellerName: String,
  val sellerDept: String,
  val contactNumber: String,
  val description: String
)

data class CommunityPost(
  val id: String,
  val authorName: String,
  val authorDept: String,
  val authorYear: String,
  val title: String,
  val body: String,
  val category: String, // "Academic", "Hackathons", "Campus Life", "Placements", "Hostel"
  val timestamp: String,
  val likesCount: Int,
  val commentsCount: Int,
  val isLiked: Boolean = false,
  val isSaved: Boolean = false,
  val comments: List<CommunityComment> = emptyList()
)

data class CommunityComment(
  val id: String,
  val authorName: String,
  val text: String,
  val timestamp: String
)

data class LibraryBook(
  val id: String,
  val title: String,
  val author: String,
  val isbn: String,
  val category: String,
  val rackNumber: String,
  val totalCopies: Int,
  val availableCopies: Int,
  val isIssuedToMe: Boolean = false,
  val issueDate: String? = null,
  val dueDate: String? = null,
  val fineAmount: String = "₹ 0"
)

data class FeeItem(
  val id: String,
  val feeCategory: String, // "Tuition Fee", "Hostel & Mess", "Special Lab & Training", "Anna Univ Exam Fee"
  val semester: String,
  val totalAmount: Long,
  val paidAmount: Long,
  val pendingAmount: Long,
  val dueDate: String,
  val status: String, // "PAID", "PARTIALLY PAID", "DUE"
  val receiptNumber: String? = null,
  val paymentDate: String? = null
)

data class HostelInfo(
  val blockName: String = "Kaveri Boys Hostel - Block C",
  val roomNumber: String = "C-312",
  val wardenName: String = "Dr. M. Soundararajan",
  val wardenContact: String = "+91 94433 22110",
  val roomType: String = "3-Sharing Attached Washroom",
  val roommates: List<String> = listOf("Arun Kumar (Mech III)", "Rahul Raj (ECE III)"),
  val weeklyMessMenu: List<DailyMessMenu> = emptyList(),
  val activeComplaints: List<HostelComplaint> = emptyList()
)

data class DailyMessMenu(
  val day: String,
  val breakfast: String,
  val lunch: String,
  val snacks: String,
  val dinner: String,
  val isSpecial: Boolean = false
)

data class HostelComplaint(
  val id: String,
  val category: String, // "Electrical", "Plumbing", "Wi-Fi", "Carpentry"
  val description: String,
  val reportedDate: String,
  val status: String // "Pending", "Assigned", "Resolved"
)

data class BusRouteItem(
  val routeNumber: String,
  val startLocation: String,
  val destination: String = "Campus Main Gate",
  val busNumber: String,
  val driverName: String,
  val driverPhone: String,
  val departureTime: String,
  val arrivalTime: String,
  val keyStops: List<String>
)

data class CanteenMenuItem(
  val id: String,
  val name: String,
  val category: String, // Breakfast, Lunch, Snacks, Beverages, Fresh Juices
  val price: Int, // ₹
  val isVeg: Boolean,
  val isTodaySpecial: Boolean = false,
  val availability: Boolean = true,
  val description: String
)

data class CanteenCartItem(
  val item: CanteenMenuItem,
  val quantity: Int
)

data class GrievanceTicket(
  val ticketId: String,
  val category: String, // "Academic", "Infrastructure", "Hostel", "Transport", "Accounts"
  val subject: String,
  val description: String,
  val submittedDate: String,
  val status: String, // "SUBMITTED", "UNDER REVIEW", "IN PROGRESS", "RESOLVED"
  val isAnonymous: Boolean = false,
  val resolutionNotes: String? = null
)

data class CertificateItem(
  val id: String,
  val title: String,
  val eventOrCourse: String,
  val issuedBy: String,
  val issueDate: String,
  val credentialId: String,
  val certificateType: String // "Participation", "NPTEL Elite", "First Place", "Merit"
)

data class StudentSkillProfile(
  val programmingLanguages: List<String> = listOf("Kotlin", "Java", "Python", "SQL", "C++"),
  val frameworks: List<String> = listOf("Jetpack Compose", "Android SDK", "Spring Boot", "React", "Node.js"),
  val developerTools: List<String> = listOf("Android Studio", "Git & GitHub", "Firebase", "Postman", "Docker"),
  val softSkills: List<String> = listOf("Technical Leadership", "Public Speaking", "Problem Solving", "Team Collaboration"),
  val certifications: List<String> = listOf(
    "NPTEL IIT Madras - Problem Solving through Programming (Elite+Silver)",
    "Oracle Certified Associate Java SE 8 Programmer",
    "Google Cloud Computing Foundations - Certified Scholar"
  ),
  val keyProjects: List<ProjectItem> = emptyList()
)

data class ProjectItem(
  val title: String,
  val techStack: String,
  val duration: String,
  val description: String,
  val githubLink: String = "https://github.com/balamurugan/smart-campus"
)

data class CampusAIMessage(
  val id: String,
  val sender: String, // "USER" or "AI"
  val messageText: String,
  val timestamp: String,
  val suggestedPrompts: List<String> = emptyList(),
  val isOfficialSource: Boolean = false
)

data class AdminAnalytics(
  val totalStudents: Int = 4850,
  val totalFaculty: Int = 310,
  val totalDepartments: Int = 11,
  val overallAttendanceAverage: Double = 87.2,
  val placementPercentage: Double = 91.4,
  val openGrievances: Int = 8,
  val resolvedGrievances: Int = 142,
  val activeClubMembers: Int = 2140,
  val totalBooksIssued: Int = 1820
)
