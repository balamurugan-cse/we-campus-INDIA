package com.example.data.mock

import com.example.data.model.*

object CampusMockData {

  val sampleStudent = UserProfile(
    id = "STU-2021-CSE-012",
    fullName = "K. P. Bala Murugan",
    rollNumber = "21052421012",
    email = "balamurugan.kp@nec.edu.in",
    phoneNumber = "+91 98421 76543",
    department = "Computer Science and Engineering",
    degree = "B.E. (Hons)",
    year = "III Year",
    semester = "V Semester",
    section = "Section A",
    collegeName = "National Institute of Engineering & Technology",
    affiliation = "Autonomous Institution | Affiliated to Anna University | NAAC A++",
    role = UserRole.STUDENT,
    cgpa = 8.74,
    currentSgpa = 8.92,
    bloodGroup = "O +ve",
    dob = "14/08/2004",
    validityDate = "31/05/2027",
    emergencyContact = "+91 94431 87650",
    designation = "Undergraduate Scholar",
    cabinNumber = "Lab 4, Computing Block",
    avatarInitials = "BM"
  )

  val sampleFaculty = UserProfile(
    id = "FAC-CSE-104",
    fullName = "Dr. S. Ramanathan, M.E., Ph.D.",
    rollNumber = "EMP-9428",
    email = "ramanathan.s@nec.edu.in",
    phoneNumber = "+91 94432 10987",
    department = "Computer Science and Engineering",
    degree = "Ph.D. in Distributed Systems",
    year = "Faculty Staff",
    semester = "Dept of CSE",
    section = "Cabin CS-304",
    collegeName = "National Institute of Engineering & Technology",
    affiliation = "Professor & Head of Department",
    role = UserRole.FACULTY,
    cgpa = 0.0,
    currentSgpa = 0.0,
    bloodGroup = "B +ve",
    dob = "05/04/1979",
    validityDate = "Permanent Faculty",
    emergencyContact = "+91 94432 10988",
    designation = "Professor & Head - CSE",
    cabinNumber = "CS-304, Turing Block",
    avatarInitials = "SR"
  )

  val sampleAdmin = UserProfile(
    id = "ADM-MAIN-001",
    fullName = "Prof. V. Meenakshi Sundaram",
    rollNumber = "ADMIN-002",
    email = "dean.academics@nec.edu.in",
    phoneNumber = "+91 98940 55432",
    department = "Deanery of Academic Affairs",
    degree = "Dean (Academics)",
    year = "Administration",
    semester = "Central Administration",
    section = "Admin Block Room 102",
    collegeName = "National Institute of Engineering & Technology",
    affiliation = "Central Administration Office",
    role = UserRole.ADMIN,
    cgpa = 0.0,
    currentSgpa = 0.0,
    bloodGroup = "A +ve",
    dob = "18/02/1972",
    validityDate = "Administration",
    emergencyContact = "+91 98940 55430",
    designation = "Dean of Academic Affairs",
    cabinNumber = "Admin Block - Room 102",
    avatarInitials = "MS"
  )

  val sampleSubjectAttendance = listOf(
    SubjectAttendance("CS8501", "Data Structures & Algorithms", "Dr. S. Ramanathan", 42, 39, odCount = 2, leaveCount = 1),
    SubjectAttendance("CS8502", "Database Management Systems", "Prof. Priya S", 40, 36, odCount = 1, leaveCount = 1),
    SubjectAttendance("CS8503", "Computer Networks", "Dr. Karthik R", 38, 33, odCount = 2, leaveCount = 2),
    SubjectAttendance("CS8504", "Operating Systems", "Prof. Arun Kumar", 44, 31, odCount = 1, leaveCount = 4), // Shortage alert
    SubjectAttendance("CS8505", "Object Oriented Programming", "Prof. Harini M", 36, 33, odCount = 1, leaveCount = 1),
    SubjectAttendance("CS8506", "Software Engineering", "Dr. Divya S", 34, 30, odCount = 1, leaveCount = 1),
    SubjectAttendance("CS8507", "Web Technology & Cloud", "Prof. Vignesh P", 36, 32, odCount = 2, leaveCount = 1),
    SubjectAttendance("MA8551", "Discrete Mathematics", "Dr. G. Shanmugam", 42, 35, odCount = 0, leaveCount = 2),
    SubjectAttendance("HS8581", "Professional Communication", "Prof. Anita George", 28, 27, odCount = 0, leaveCount = 0)
  )

  val sampleAttendanceLogs = listOf(
    AttendanceDayLog("02/09/2026", "Wednesday", AttendanceStatus.PRESENT, "7/7 Periods Attended (DS, OS, DBMS Lab)"),
    AttendanceDayLog("01/09/2026", "Tuesday", AttendanceStatus.PRESENT, "7/7 Periods Attended"),
    AttendanceDayLog("31/08/2026", "Monday", AttendanceStatus.OD, "Approved On-Duty: Inter-College Hackathon at IIT Madras"),
    AttendanceDayLog("28/08/2026", "Friday", AttendanceStatus.PRESENT, "6/7 Periods Attended"),
    AttendanceDayLog("27/08/2026", "Thursday", AttendanceStatus.LEAVE, "Medical Leave (Doctor Certificate Approved)"),
    AttendanceDayLog("26/08/2026", "Wednesday", AttendanceStatus.PRESENT, "7/7 Periods Attended")
  )

  val sampleTimetableToday = listOf(
    TimetableSlot(1, "09:00 - 09:50 AM", "CS8501", "Data Structures & Algorithms", "Dr. S. Ramanathan", "LH-204", isLab = false),
    TimetableSlot(2, "09:50 - 10:40 AM", "CS8502", "Database Management Systems", "Prof. Priya S", "LH-204", isLab = false),
    TimetableSlot(3, "10:55 - 11:45 AM", "CS8503", "Computer Networks", "Dr. Karthik R", "LH-204", isLab = false),
    TimetableSlot(4, "11:45 - 12:35 PM", "CS8504", "Operating Systems", "Prof. Arun Kumar", "LH-204", isLab = false),
    TimetableSlot(5, "01:30 - 02:20 PM", "CS8511", "DBMS & SQL Laboratory", "Prof. Priya S", "Turing Lab-3", isLab = true),
    TimetableSlot(6, "02:20 - 03:10 PM", "CS8511", "DBMS & SQL Laboratory", "Prof. Priya S", "Turing Lab-3", isLab = true),
    TimetableSlot(7, "03:20 - 04:15 PM", "FREE", "Library / Mentorship Hour", "Dr. S. Ramanathan", "Central Library", isFreePeriod = true)
  )

  val sampleWeeklyTimetable = mapOf(
    "Monday" to sampleTimetableToday,
    "Tuesday" to listOf(
      TimetableSlot(1, "09:00 - 09:50 AM", "MA8551", "Discrete Mathematics", "Dr. G. Shanmugam", "LH-204"),
      TimetableSlot(2, "09:50 - 10:40 AM", "CS8505", "Object Oriented Programming", "Prof. Harini M", "LH-204"),
      TimetableSlot(3, "10:55 - 11:45 AM", "CS8506", "Software Engineering", "Dr. Divya S", "LH-204"),
      TimetableSlot(4, "11:45 - 12:35 PM", "CS8507", "Web Technology & Cloud", "Prof. Vignesh P", "LH-204"),
      TimetableSlot(5, "01:30 - 03:10 PM", "CS8512", "Networks Laboratory", "Dr. Karthik R", "Networks Lab", isLab = true),
      TimetableSlot(7, "03:20 - 04:15 PM", "CS8501", "Data Structures Tutorial", "Dr. S. Ramanathan", "LH-204")
    ),
    "Wednesday" to sampleTimetableToday,
    "Thursday" to listOf(
      TimetableSlot(1, "09:00 - 09:50 AM", "CS8504", "Operating Systems", "Prof. Arun Kumar", "LH-204"),
      TimetableSlot(2, "09:50 - 10:40 AM", "CS8503", "Computer Networks", "Dr. Karthik R", "LH-204"),
      TimetableSlot(3, "10:55 - 11:45 AM", "MA8551", "Discrete Mathematics", "Dr. G. Shanmugam", "LH-204"),
      TimetableSlot(4, "11:45 - 12:35 PM", "HS8581", "Professional Communication", "Prof. Anita George", "Comm Lab"),
      TimetableSlot(5, "01:30 - 03:10 PM", "CS8513", "Web Programming Lab", "Prof. Vignesh P", "Cloud Lab", isLab = true),
      TimetableSlot(7, "03:20 - 04:15 PM", "CLUB", "Club Activity / Sports Hour", "Coordinators", "Grounds", isFreePeriod = true)
    ),
    "Friday" to listOf(
      TimetableSlot(1, "09:00 - 09:50 AM", "CS8502", "Database Management Systems", "Prof. Priya S", "LH-204"),
      TimetableSlot(2, "09:50 - 10:40 AM", "CS8501", "Data Structures & Algorithms", "Dr. S. Ramanathan", "LH-204"),
      TimetableSlot(3, "10:55 - 11:45 AM", "CS8507", "Web Technology & Cloud", "Prof. Vignesh P", "LH-204"),
      TimetableSlot(4, "11:45 - 12:35 PM", "CS8506", "Software Engineering", "Dr. Divya S", "LH-204"),
      TimetableSlot(5, "01:30 - 02:20 PM", "MA8551", "Discrete Mathematics", "Dr. G. Shanmugam", "LH-204"),
      TimetableSlot(6, "02:20 - 03:10 PM", "PLACEMENT", "Campus Placement Training (Aptitude & Coding)", "Placement Cell", "Seminar Hall 1"),
      TimetableSlot(7, "03:20 - 04:15 PM", "PLACEMENT", "Mock Technical Interviews", "Alumni Mentors", "Seminar Hall 1")
    )
  )

  val sampleAcademicSubjects = listOf(
    AcademicSubject("CS8501", "Data Structures & Algorithms", 4, "Dr. S. Ramanathan", 47.0, 48.5, 10.0, 94.0, "O", 10),
    AcademicSubject("CS8502", "Database Management Systems", 3, "Prof. Priya S", 44.0, 46.0, 9.5, 89.0, "A+", 9),
    AcademicSubject("CS8503", "Computer Networks", 3, "Dr. Karthik R", 42.0, 43.5, 9.0, 84.0, "A+", 9),
    AcademicSubject("CS8504", "Operating Systems", 3, "Prof. Arun Kumar", 38.0, 41.0, 8.5, 78.0, "A", 8),
    AcademicSubject("CS8505", "Object Oriented Programming", 3, "Prof. Harini M", 46.0, 47.0, 10.0, 92.0, "O", 10),
    AcademicSubject("CS8506", "Software Engineering", 3, "Dr. Divya S", 41.0, 43.0, 9.0, 82.0, "A+", 9),
    AcademicSubject("CS8507", "Web Technology & Cloud", 3, "Prof. Vignesh P", 45.0, 46.5, 9.5, 90.0, "O", 10),
    AcademicSubject("MA8551", "Discrete Mathematics", 4, "Dr. G. Shanmugam", 39.0, 42.0, 8.5, 80.0, "A", 8)
  )

  val sampleSemesterResults = listOf(
    SemesterResult(1, "Semester I (Nov/Dec 2023)", 8.52, 23, 23),
    SemesterResult(2, "Semester II (Apr/May 2024)", 8.68, 24, 24),
    SemesterResult(3, "Semester III (Nov/Dec 2024)", 8.80, 24, 24),
    SemesterResult(4, "Semester IV (Apr/May 2025)", 8.92, 25, 25)
  )

  val sampleAssignments = listOf(
    AssignmentItem(
      id = "ASG-01",
      subjectCode = "CS8501",
      subjectName = "Data Structures & Algorithms",
      title = "Implementation of B-Trees & Red-Black Trees with Rebalancing",
      description = "Write C++/Java source code to implement insertion and self-balancing rotations in Red-Black Trees. Include memory leak analysis and benchmark results with 100,000 random keys.",
      facultyName = "Dr. S. Ramanathan",
      dueDate = "08/09/2026",
      isSubmitted = true,
      submissionDate = "02/09/2026",
      maxMarks = 20,
      obtainedMarks = 19,
      facultyFeedback = "Excellent memory profiling and neat code structure.",
      attachedFileUrl = "kp_balamurugan_rbtree.pdf"
    ),
    AssignmentItem(
      id = "ASG-02",
      subjectCode = "CS8502",
      subjectName = "Database Management Systems",
      title = "Hospital Management System - Normalized Schema Design (3NF/BCNF)",
      description = "Design complete ER Diagram, Relational schema in 3NF and BCNF, complex nested SQL queries and index tuning strategies for an Indian multi-specialty hospital management system.",
      facultyName = "Prof. Priya S",
      dueDate = "10/09/2026",
      isSubmitted = false,
      maxMarks = 20
    ),
    AssignmentItem(
      id = "ASG-03",
      subjectCode = "CS8503",
      subjectName = "Computer Networks",
      title = "Wireshark Packet Analysis & Subnetting Problem Set",
      description = "Capture TCP 3-way handshake, DNS query resolution and HTTP/2 frames. Solve 5 subnetting scenarios for a 10,000-host autonomous campus network.",
      facultyName = "Dr. Karthik R",
      dueDate = "12/09/2026",
      isSubmitted = false,
      maxMarks = 20
    ),
    AssignmentItem(
      id = "ASG-04",
      subjectCode = "CS8504",
      subjectName = "Operating Systems",
      title = "Dining Philosophers Synchronization using POSIX Semaphores",
      description = "Implement deadlock-free solution for the 5-Philosopher problem in C on Linux using pthreads and POSIX semaphores.",
      facultyName = "Prof. Arun Kumar",
      dueDate = "15/09/2026",
      isSubmitted = false,
      maxMarks = 20
    )
  )

  val sampleExams = listOf(
    ExamScheduleItem(
      id = "EX-01",
      examType = "Internal Assessment - II (IA-2)",
      subjectCode = "CS8501",
      subjectName = "Data Structures & Algorithms",
      examDate = "18/09/2026",
      session = "FN (09:30 AM - 11:30 AM)",
      hallNumber = "Mechanical Block MB-204",
      seatNumber = "MB-204-Row 3-Seat 8",
      invigilator = "Dr. G. Shanmugam"
    ),
    ExamScheduleItem(
      id = "EX-02",
      examType = "Internal Assessment - II (IA-2)",
      subjectCode = "CS8502",
      subjectName = "Database Management Systems",
      examDate = "19/09/2026",
      session = "FN (09:30 AM - 11:30 AM)",
      hallNumber = "Mechanical Block MB-204",
      seatNumber = "MB-204-Row 3-Seat 8",
      invigilator = "Prof. Harini M"
    ),
    ExamScheduleItem(
      id = "EX-03",
      examType = "Internal Assessment - II (IA-2)",
      subjectCode = "CS8503",
      subjectName = "Computer Networks",
      examDate = "21/09/2026",
      session = "FN (09:30 AM - 11:30 AM)",
      hallNumber = "Mechanical Block MB-204",
      seatNumber = "MB-204-Row 3-Seat 8",
      invigilator = "Prof. Vignesh P"
    ),
    ExamScheduleItem(
      id = "EX-04",
      examType = "Internal Assessment - II (IA-2)",
      subjectCode = "CS8504",
      subjectName = "Operating Systems",
      examDate = "22/09/2026",
      session = "FN (09:30 AM - 11:30 AM)",
      hallNumber = "Mechanical Block MB-204",
      seatNumber = "MB-204-Row 3-Seat 8",
      invigilator = "Dr. Divya S"
    )
  )

  val sampleEvents = listOf(
    CollegeEvent(
      id = "EVT-01",
      title = "KRYPTON 2026 - National Tech Symposium",
      category = EventCategory.SYMPOSIUM,
      organizerClub = "CSE Association & IEEE Student Chapter",
      department = "Dept of Computer Science & Engineering",
      eventDate = "24/09/2026",
      eventTime = "09:00 AM - 05:00 PM",
      venue = "Dr. APJ Abdul Kalam Auditorium",
      registrationFee = "Free for Internal | ₹150 for External",
      description = "Annual flagship technical symposium featuring Paper Presentation, Coding Relay, Reverse Engineering, Web Design Challenge, and Project Expo with ₹1,00,000 cash prizes.",
      totalParticipants = 480,
      maxCapacity = 600,
      isRegistered = true,
      qrPassCode = "CC-KRYPTON-STU21052421012-VERIFIED",
      bannerColor = 0xFF1E3A8A
    ),
    CollegeEvent(
      id = "EVT-02",
      title = "HACK-BHARAT 36-Hour National Hackathon",
      category = EventCategory.HACKATHON,
      organizerClub = "Coding Club & Google Developer Student Club",
      department = "Centres for Innovation & AI",
      eventDate = "03/10/2026",
      eventTime = "Starts 08:00 AM (36 Hours Continuous)",
      venue = "Central Computing Complex (CCC)",
      registrationFee = "Free",
      description = "Build innovative AI & FinTech solutions for rural India. Mentored by industry leaders from Bengaluru and Chennai top product firms.",
      totalParticipants = 320,
      maxCapacity = 400,
      isRegistered = false,
      bannerColor = 0xFF059669
    ),
    CollegeEvent(
      id = "EVT-03",
      title = "DHWAANI 2026 - Inter-College Cultural Fest",
      category = EventCategory.CULTURAL,
      organizerClub = "Fine Arts & Cultural Council",
      department = "Student Affairs Directorate",
      eventDate = "16/10/2026",
      eventTime = "04:30 PM - 10:00 PM",
      venue = "Open Air Amphitheatre (OAT)",
      registrationFee = "Free Entry with College ID",
      description = "Celebration of music, Indian classical dance, fusion bands, street theater, visual arts, and pro-night concert featuring prominent South Indian indie musicians.",
      totalParticipants = 1200,
      maxCapacity = 1500,
      isRegistered = true,
      qrPassCode = "CC-DHWAANI-STU21052421012-VERIFIED",
      bannerColor = 0xFFD97706
    ),
    CollegeEvent(
      id = "EVT-04",
      title = "Hands-on Workshop: Generative AI on Edge Devices",
      category = EventCategory.WORKSHOP,
      organizerClub = "AI & Robotics Club",
      department = "Dept of CSE & ECE",
      eventDate = "28/09/2026",
      eventTime = "01:30 PM - 04:30 PM",
      venue = "Seminar Hall 2 (ECE Block)",
      registrationFee = "₹50 (Kit Included)",
      description = "Practical session on quantizing On-Device SLMs, Jetson Nano deployment, and Android AI integrations with Gemini Nano.",
      totalParticipants = 95,
      maxCapacity = 100,
      isRegistered = false,
      bannerColor = 0xFF7C3AED
    )
  )

  val sampleClubs = listOf(
    CampusClub(
      id = "CLUB-01",
      name = "Campus Coding Club",
      acronym = "CCC",
      category = "Technical",
      staffAdvisor = "Dr. S. Ramanathan",
      studentPresident = "Rahul Raj (CSE IV)",
      memberCount = 420,
      description = "Competitive programming, data structures, algorithms, open-source development, and ICPC coaching.",
      achievements = listOf("Top 10 in ACM-ICPC Regional Amritapuri", "Winners of Smart India Hackathon 2025", "120+ students placed in Tier-1 product firms"),
      isJoined = true,
      themeColor = 0xFF2563EB
    ),
    CampusClub(
      id = "CLUB-02",
      name = "Robotics & Automation Society",
      acronym = "RAS",
      category = "Technical",
      staffAdvisor = "Dr. K. Vijayakumar (Mech)",
      studentPresident = "Harini M (Robotics IV)",
      memberCount = 280,
      description = "Hands-on robotics, drone design, autonomous rovers, ROS programming, and IoT automation.",
      achievements = listOf("First place in IIT Bombay Techfest Robowars", "Patented smart agriculture rover"),
      isJoined = false,
      themeColor = 0xFF059669
    ),
    CampusClub(
      id = "CLUB-03",
      name = "Entrepreneurship Cell (E-Cell)",
      acronym = "E-CELL",
      category = "Leadership & Innovation",
      staffAdvisor = "Prof. V. Meenakshi Sundaram",
      studentPresident = "Arun Kumar (Mech III)",
      memberCount = 310,
      description = "Nurturing student founders, startup incubators, pitch competitions, and angel investor networking.",
      achievements = listOf("8 campus startups funded via Startup India Seed Fund", "Annual E-Summit hosted 40+ founders"),
      isJoined = true,
      themeColor = 0xFFD97706
    ),
    CampusClub(
      id = "CLUB-04",
      name = "National Service Scheme (NSS Unit 4)",
      acronym = "NSS",
      category = "Social Service",
      staffAdvisor = "Dr. G. Shanmugam",
      studentPresident = "Priya S (ECE III)",
      memberCount = 500,
      description = "Rural village adoption, blood donation camps, digital literacy programs in government schools, and environmental drives.",
      achievements = listOf("Donated 600+ units of blood to Coimbatore Govt Hospital", "Planted 2,000 saplings in Western Ghats foothills"),
      isJoined = true,
      themeColor = 0xFFDC2626
    ),
    CampusClub(
      id = "CLUB-05",
      name = "Literary & Debating Society",
      acronym = "LIT-SOC",
      category = "Arts & Humanities",
      staffAdvisor = "Prof. Anita George",
      studentPresident = "Divya S (IT III)",
      memberCount = 190,
      description = "Parliamentary debating, Model United Nations (MUN), creative writing, elocution, and literary publications.",
      achievements = listOf("Best Delegation at National Law School MUN Bengaluru", "Published annual campus magazine 'PRISM'"),
      isJoined = false,
      themeColor = 0xFF7C3AED
    )
  )

  val sampleAnnouncements = listOf(
    AnnouncementItem(
      id = "ANN-01",
      title = "Official Circular: End Semester Examination Timetable - Nov/Dec 2026 Published",
      category = "Examinations",
      issuedBy = "Office of the Controller of Examinations (COE)",
      publishDate = "02/09/2026",
      content = "The revised timetable for the Autonomous End Semester Examinations (UG B.E./B.Tech Semesters III, V, VII) is now available on the portal. Hall tickets will be issued through student logins starting 15th October 2026. Hall clearance requires a minimum of 75% attendance.",
      isPinned = true,
      priorityTag = "Urgent"
    ),
    AnnouncementItem(
      id = "ANN-02",
      title = "TCS Digital & Titan Engineering Campus Recruitment Drive 2026-27",
      category = "Placements",
      issuedBy = "Directorate of Training & Placement",
      publishDate = "01/09/2026",
      content = "Placement registration is open for eligible VII Sem students and pre-final year interns. Eligible branches: CSE, IT, ECE, EEE, Mech. Cutoff: 7.0 CGPA with no history of standing arrears. Register on or before 10th September 2026, 05:00 PM.",
      isPinned = true,
      priorityTag = "Placement"
    ),
    AnnouncementItem(
      id = "ANN-03",
      title = "Holiday Announcement: Ganesh Chaturthi & Onam Celebrations",
      category = "Holiday",
      issuedBy = "Office of the Principal",
      publishDate = "30/08/2026",
      content = "The college will remain closed on Friday, 04/09/2026 on account of festival celebrations. Hostellers requiring weekend gate passes must submit outing slips on the hostel portal by Thursday evening.",
      isPinned = false,
      priorityTag = "General"
    ),
    AnnouncementItem(
      id = "ANN-04",
      title = "Annual Inter-Department Sports Trophy (Cricket, Football, Badminton)",
      category = "Sports",
      issuedBy = "Department of Physical Education",
      publishDate = "29/08/2026",
      content = "Inter-department fixtures will commence from 15th September. Department team selections will be held daily between 04:30 PM and 06:00 PM at the Main Sports Complex.",
      isPinned = false,
      priorityTag = "Events"
    )
  )

  val sampleNotifications = listOf(
    NotificationItem("NOTIF-01", "Attendance", "Attendance Alert", "Attendance Shortage Alert in Operating Systems (70.4%). Please meet Prof. Arun Kumar.", "10 mins ago", isRead = false),
    NotificationItem("NOTIF-02", "Assignments", "New Assignment", "New Assignment uploaded: 'Wireshark Packet Analysis' by Dr. Karthik R. Due 12/09/2026.", "1 hour ago", isRead = false),
    NotificationItem("NOTIF-03", "Placements", "Placement Shortlist", "Zoho Corporation shortlist announced for Round-2 Advanced Programming.", "3 hours ago", isRead = false),
    NotificationItem("NOTIF-04", "Events", "Registration Confirmed", "Your registration for KRYPTON 2026 Tech Fest is confirmed. View QR pass.", "Yesterday", isRead = true),
    NotificationItem("NOTIF-05", "Library", "Book Due Reminder", "Book 'Computer Networking' by Kurose is due for return on 05/09/2026.", "2 days ago", isRead = true),
    NotificationItem("NOTIF-06", "Fees", "Fee Payment Receipt", "Hostel Mess Fee for Odd Semester 2026 received with thanks. Receipt #REC-2026-8812.", "4 days ago", isRead = true)
  )

  val samplePlacements = listOf(
    PlacementDrive(
      id = "PLC-01",
      companyName = "Zoho Corporation",
      role = "Member Technical Staff (Software Developer)",
      ctcLpa = "₹ 8.5 - 12.0 LPA",
      jobLocations = listOf("Chennai", "Tenkasi", "Coimbatore"),
      minCgpa = 7.0,
      eligibleDepts = listOf("CSE", "IT", "ECE", "EEE", "Mech"),
      applicationDeadline = "10/09/2026",
      description = "Core product engineering role working on high-performance cloud applications, database engines, and native frameworks. Strong proficiency in C/C++/Java, Data Structures, and System Design required.",
      requiredSkills = listOf("Data Structures", "Java", "C++", "Algorithms", "System Design"),
      isApplied = true,
      applicationStatus = "Shortlisted for Round 2 (Coding & Problem Solving)",
      interviewDate = "15/09/2026 at Turing Hall"
    ),
    PlacementDrive(
      id = "PLC-02",
      companyName = "TCS Digital",
      role = "Digital Software Engineer / AI Specialist",
      ctcLpa = "₹ 7.5 - 9.0 LPA",
      jobLocations = listOf("Bengaluru", "Chennai", "Hyderabad", "Pune"),
      minCgpa = 7.0,
      eligibleDepts = listOf("CSE", "IT", "ECE"),
      applicationDeadline = "12/09/2026",
      description = "Digital enterprise engineering in Generative AI, Cloud transformation, Microservices architecture, and Next-Gen Data Analytics.",
      requiredSkills = listOf("Python", "SQL", "Cloud Basics", "Data Engineering"),
      isApplied = true,
      applicationStatus = "Application Submitted (Pending Online Assessment)"
    ),
    PlacementDrive(
      id = "PLC-03",
      companyName = "Titan Engineering & Automation (TATA)",
      role = "Graduate Engineer Trainee (Embedded & Robotics)",
      ctcLpa = "₹ 6.5 - 8.0 LPA",
      jobLocations = listOf("Hosur", "Coimbatore", "Bengaluru"),
      minCgpa = 7.5,
      eligibleDepts = listOf("CSE", "ECE", "EEE", "Mech"),
      applicationDeadline = "18/09/2026",
      description = "Smart manufacturing, PLC programming, Industrial IoT sensors, and autonomous assembly line automation.",
      requiredSkills = listOf("C", "Microcontrollers", "IoT", "Sensors"),
      isApplied = false
    ),
    PlacementDrive(
      id = "PLC-04",
      companyName = "Freshworks",
      role = "Associate Product Engineer",
      ctcLpa = "₹ 14.5 LPA",
      jobLocations = listOf("Chennai", "Bengaluru"),
      minCgpa = 8.0,
      eligibleDepts = listOf("CSE", "IT"),
      applicationDeadline = "20/09/2026",
      description = "Build scalable multi-tenant SaaS products serving over 60,000 businesses globally. Experience with Ruby/Java/Kotlin/Go is a plus.",
      requiredSkills = listOf("Full Stack", "APIs", "Distributed Systems", "JavaScript/TypeScript"),
      isApplied = false
    )
  )

  val sampleInternships = listOf(
    InternshipItem(
      id = "INT-01",
      companyName = "Larsen & Toubro Technology Services",
      role = "Edge AI & Embedded Software Intern",
      duration = "6 Months",
      stipend = "₹ 30,000 / month",
      location = "Bengaluru (Hybrid)",
      skillsRequired = listOf("C++", "OpenCV", "TensorFlow Lite", "Linux"),
      deadline = "15/09/2026",
      isApplied = true
    ),
    InternshipItem(
      id = "INT-02",
      companyName = "Cognizant AI Labs",
      role = "Generative AI Research Intern",
      duration = "3 Months",
      stipend = "₹ 25,000 / month",
      location = "Chennai (On-site)",
      skillsRequired = listOf("Python", "LLMs", "RAG Pipelines", "PyTorch"),
      deadline = "22/09/2026",
      isApplied = false
    ),
    InternshipItem(
      id = "INT-03",
      companyName = "Robert Bosch Engineering",
      role = "Automotive Telematics Intern",
      duration = "6 Months",
      stipend = "₹ 28,000 / month",
      location = "Coimbatore / Pune",
      skillsRequired = listOf("Embedded C", "CAN Protocol", "AUTOSAR"),
      deadline = "25/09/2026",
      isApplied = false
    )
  )

  val sampleLostFound = listOf(
    LostFoundItem(
      id = "LF-01",
      type = "LOST",
      title = "Casio fx-991EX Scientific Calculator (ClassWiz)",
      category = "Electronics",
      locationFoundOrLost = "LH-204 Desk Row 4 (Left Side)",
      reportedDate = "02/09/2026",
      description = "Black scientific calculator with 'KP' initials etched on the back casing. Left behind after Math lecture.",
      reportedBy = "K. P. Bala Murugan (CSE III)",
      contactPhone = "+91 98421 76543",
      isRecovered = false
    ),
    LostFoundItem(
      id = "LF-02",
      type = "FOUND",
      title = "Blue Boat Airdopes Case with Earbuds",
      category = "Electronics",
      locationFoundOrLost = "Central Library Reading Hall 2nd Floor",
      reportedDate = "01/09/2026",
      description = "Found near table 14 along with a brown notebook. Deposited with Library Reference Desk.",
      reportedBy = "Harini M (ECE III)",
      contactPhone = "+91 98420 11223",
      isRecovered = false
    ),
    LostFoundItem(
      id = "LF-03",
      type = "FOUND",
      title = "Keys with Royal Enfield Leather Keychain",
      category = "Keys",
      locationFoundOrLost = "Campus Canteen Parking Lot",
      reportedDate = "30/08/2026",
      description = "Two bike keys on a brown leather keychain. Handed over to Main Gate Security Cabin.",
      reportedBy = "Vignesh P (Mech IV)",
      contactPhone = "+91 97890 33445",
      isRecovered = true
    )
  )

  val sampleMarketplace = listOf(
    MarketplaceItem(
      id = "MKT-01",
      title = "Data Structures in C++ (Mark Allen Weiss) - 4th Ed",
      category = "Books",
      price = "₹ 350",
      condition = "Like New (No markings)",
      sellerName = "Arun Kumar",
      sellerDept = "CSE IV Year",
      contactNumber = "+91 94435 99881",
      description = "Standard Anna University textbook. Includes all practice code sheets and previous 5 years Anna Univ solved question papers."
    ),
    MarketplaceItem(
      id = "MKT-02",
      title = "Hercules Roadeo 21-Speed Gear Bicycle",
      category = "Bicycles",
      price = "₹ 3,800",
      condition = "Good Condition (New Tyres)",
      sellerName = "Rahul Raj",
      sellerDept = "ECE IV Year",
      contactNumber = "+91 98422 44556",
      description = "Ideal for campus commuting between Kaveri Hostel and Mechanical block. Serviced last month with dual disc brakes."
    ),
    MarketplaceItem(
      id = "MKT-03",
      title = "Arduino Mega 2560 Starter Kit + Sensor Shield + 16x2 LCD",
      category = "Lab Equipment",
      price = "₹ 1,200",
      condition = "Like New (Used for 1 project)",
      sellerName = "Priya S",
      sellerDept = "ECE III Year",
      contactNumber = "+91 94432 88771",
      description = "Complete kit with jumper wires, ultrasonic sensors, relay modules, and breadboard. Ready for Mini Project."
    ),
    MarketplaceItem(
      id = "MKT-04",
      title = "Official College White Lab Coat (Size 38 / Medium)",
      category = "Uniform / Lab",
      price = "₹ 180",
      condition = "Good",
      sellerName = "Karthik R",
      sellerDept = "Mech III Year",
      contactNumber = "+91 98421 99002",
      description = "Clean, washed cotton lab coat suitable for Chemistry & Physics lab."
    )
  )

  val sampleCommunityPosts = listOf(
    CommunityPost(
      id = "POST-01",
      authorName = "Priya S",
      authorDept = "CSE III Year",
      authorYear = "2023 - 2027",
      title = "Tips for TCS Digital & Zoho Round 1 Coding Strategy?",
      body = "Senior batch friends, how deep are tree algorithms and dynamic programming questions in Zoho's advanced coding round? Should we focus on sliding window and graph traversals as well?",
      category = "Placements",
      timestamp = "2 hours ago",
      likesCount = 38,
      commentsCount = 6,
      isLiked = true,
      comments = listOf(
        CommunityComment("C1", "Rahul Raj (CSE IV)", "Zoho round 2 focuses heavily on clean recursive backtracking, custom string parsing without split(), and matrix manipulation. Write clean code!", "1 hour ago"),
        CommunityComment("C2", "K. P. Bala Murugan", "Also practice Roman to Integer, Sudoku solver, and Snake game simulation.", "45 mins ago")
      )
    ),
    CommunityPost(
      id = "POST-02",
      authorName = "Arun Kumar",
      authorDept = "Mechanical III Year",
      authorYear = "2023 - 2027",
      title = "Looking for 2 teammates for HACK-BHARAT 36-Hr Hackathon!",
      body = "We are building an IoT-based cold chain monitoring device for rural agricultural produce. Need one frontend/Android developer and one backend developer. Ping if interested!",
      category = "Hackathons",
      timestamp = "5 hours ago",
      likesCount = 24,
      commentsCount = 4,
      isLiked = false
    ),
    CommunityPost(
      id = "POST-03",
      authorName = "Harini M",
      authorDept = "ECE III Year",
      authorYear = "2023 - 2027",
      title = "DBMS Model Exam Previous Year Question Paper Solutions PDF",
      body = "Uploaded handwritten worked-out solutions for BCNF decomposition and B+ Tree indexing problems on Google Drive. Link pinned in the CSE study group!",
      category = "Academic",
      timestamp = "Yesterday",
      likesCount = 92,
      commentsCount = 14,
      isLiked = true,
      isSaved = true
    )
  )

  val sampleLibraryBooks = listOf(
    LibraryBook(
      id = "BK-01",
      title = "Database System Concepts",
      author = "Abraham Silberschatz, Henry F. Korth, S. Sudarshan",
      isbn = "978-0078022159",
      category = "Computer Science",
      rackNumber = "Rack CS-12 / Shelf 3",
      totalCopies = 15,
      availableCopies = 4,
      isIssuedToMe = true,
      issueDate = "22/08/2026",
      dueDate = "06/09/2026",
      fineAmount = "₹ 0"
    ),
    LibraryBook(
      id = "BK-02",
      title = "Computer Networking: A Top-Down Approach",
      author = "James F. Kurose, Keith W. Ross",
      isbn = "978-0133594140",
      category = "Networking",
      rackNumber = "Rack CS-08 / Shelf 1",
      totalCopies = 12,
      availableCopies = 2,
      isIssuedToMe = true,
      issueDate = "18/08/2026",
      dueDate = "02/09/2026",
      fineAmount = "₹ 0"
    ),
    LibraryBook(
      id = "BK-03",
      title = "Operating System Concepts (Dinosaur Book)",
      author = "Abraham Silberschatz, Peter B. Galvin, Greg Gagne",
      isbn = "978-1118063330",
      category = "Systems",
      rackNumber = "Rack CS-14 / Shelf 2",
      totalCopies = 20,
      availableCopies = 7
    ),
    LibraryBook(
      id = "BK-04",
      title = "Discrete Mathematics and Its Applications",
      author = "Kenneth H. Rosen",
      isbn = "978-0073383095",
      category = "Mathematics",
      rackNumber = "Rack MA-04 / Shelf 4",
      totalCopies = 18,
      availableCopies = 9
    )
  )

  val sampleFees = listOf(
    FeeItem(
      id = "FEE-01",
      feeCategory = "Tuition Fee (Odd Semester - V Sem)",
      semester = "V Semester",
      totalAmount = 65000,
      paidAmount = 65000,
      pendingAmount = 0,
      dueDate = "10/08/2026",
      status = "PAID",
      receiptNumber = "REC-2026-AUG-5541",
      paymentDate = "05/08/2026"
    ),
    FeeItem(
      id = "FEE-02",
      feeCategory = "Hostel Accommodation & Mess Fee",
      semester = "Odd Semester 2026",
      totalAmount = 45000,
      paidAmount = 45000,
      pendingAmount = 0,
      dueDate = "15/08/2026",
      status = "PAID",
      receiptNumber = "REC-2026-HST-8812",
      paymentDate = "12/08/2026"
    ),
    FeeItem(
      id = "FEE-03",
      feeCategory = "Special Placement & Industry Training Fee",
      semester = "V Semester",
      totalAmount = 12000,
      paidAmount = 6000,
      pendingAmount = 6000,
      dueDate = "25/09/2026",
      status = "PARTIALLY PAID",
      receiptNumber = "REC-2026-TRN-1092",
      paymentDate = "20/08/2026"
    ),
    FeeItem(
      id = "FEE-04",
      feeCategory = "Autonomous End Semester Exam Fee (8 Subjects)",
      semester = "Nov/Dec 2026",
      totalAmount = 2800,
      paidAmount = 0,
      pendingAmount = 2800,
      dueDate = "30/09/2026",
      status = "DUE"
    )
  )

  val sampleMessMenu = listOf(
    DailyMessMenu("Monday", "Idli, Medu Vada, Sambar, Coconut & Tomato Chutney, Coffee/Tea", "South Indian Veg Meals (Rice, Sambar, Rasam, Kootu, Curd, Appalam)", "Onion Pakoda, Masala Chai", "Chapati, Paneer Butter Masala, Jeera Rice, Dal Fry"),
    DailyMessMenu("Tuesday", "Ghee Pongal, Vadai, Sambar, Kara Chutney, Tea", "Lemon Rice, Potato Fry, Curd Rice, Pickle", "Sweet Corn / Sundal, Tea", "Poori, Potato Masala, Veg Pulao, Raita"),
    DailyMessMenu("Wednesday", "Poori, Potato Sagu, Coconut Chutney, Tea", "Vegetable Biryani / Chicken Biryani (Special), Onion Raita, Brinjal Gravy, Boiled Egg / Gulab Jamun", "Veg Cutlet, Mint Chutney, Tea", "Dosa, Sambar, Idli Podi, Tomato Onion Gravy", isSpecial = true),
    DailyMessMenu("Thursday", "Rava Upma, Coconut Chutney, Sambar, Banana, Coffee", "Sambar Rice, Aloo Gobi Roast, Curd, Rasam", "Samosa, Tomato Sauce, Chai", "Phulka, Dal Tadka, Paneer Bhurji, Steamed Rice"),
    DailyMessMenu("Friday", "Uttapam, Sambar, Green Chutney, Tea", "Traditional South Indian Meals with Payasam, Mor Kuzhambu, Beetroot Poriyal", "Parippu Vada, Filter Coffee", "Parotta with Veg / Chicken Salna, Curd Rice", isSpecial = true),
    DailyMessMenu("Saturday", "Aloo Paratha, Curd, Pickle, Butter, Tea", "Tomato Rice, Egg Masala / Paneer Roast, Appalam, Curd", "Biscuits, Masala Tea", "Chapati, Mixed Veg Kurma, Ghee Rice, Dal"),
    DailyMessMenu("Sunday", "Masala Dosa, Sambar, Coconut Chutney, Filter Coffee", "Special Sunday Feast (Chicken Curry / Paneer Tikka Masala, Dum Biryani, Ice Cream)", "Pani Puri / Chaat, Tea", "Variety Rice (Curd Rice, Fried Rice, Manchurian Gravy)", isSpecial = true)
  )

  val sampleHostelInfo = HostelInfo(
    blockName = "Kaveri Boys Hostel - Block C",
    roomNumber = "C-312",
    wardenName = "Dr. M. Soundararajan (Assoc. Prof - Chemistry)",
    wardenContact = "+91 94433 22110",
    roomType = "3-Sharing Attached Washroom",
    roommates = listOf("Arun Kumar (Mech III)", "Rahul Raj (ECE III)"),
    weeklyMessMenu = sampleMessMenu,
    activeComplaints = listOf(
      HostelComplaint("HST-CMP-01", "Wi-Fi Access Point", "Floor 3 West Wing AP experiencing intermittent disconnection.", "01/09/2026", "Assigned to IT Admin"),
      HostelComplaint("HST-CMP-02", "Carpentry", "Study table drawer lock stuck in C-312.", "28/08/2026", "Resolved")
    )
  )

  val sampleBusRoutes = listOf(
    BusRouteItem(
      routeNumber = "Route 12",
      startLocation = "Gandhipuram Bus Stand, Coimbatore",
      busNumber = "TN 38 BG 4510",
      driverName = "Mr. K. Palanisamy",
      driverPhone = "+91 94421 66554",
      departureTime = "07:30 AM",
      arrivalTime = "08:35 AM",
      keyStops = listOf("Gandhipuram (07:30)", "Lakshmi Mills (07:40)", "Hopes College (07:55)", "Singanallur (08:10)", "Campus Main Gate (08:35)")
    ),
    BusRouteItem(
      routeNumber = "Route 18",
      startLocation = "Ukkadam Central, Coimbatore",
      busNumber = "TN 38 BG 4522",
      driverName = "Mr. S. Murugesan",
      driverPhone = "+91 94421 77665",
      departureTime = "07:35 AM",
      arrivalTime = "08:40 AM",
      keyStops = listOf("Ukkadam (07:35)", "Town Hall (07:45)", "RS Puram (08:00)", "Saibaba Colony (08:15)", "Campus Gate (08:40)")
    ),
    BusRouteItem(
      routeNumber = "Route 24",
      startLocation = "Pollachi Old Bus Stand",
      busNumber = "TN 38 BG 4539",
      driverName = "Mr. R. Natarajan",
      driverPhone = "+91 94421 88990",
      departureTime = "07:15 AM",
      arrivalTime = "08:30 AM",
      keyStops = listOf("Pollachi (07:15)", "Kinathukadavu (07:40)", "Malumichampatti (08:05)", "Campus (08:30)")
    )
  )

  val sampleCanteenMenu = listOf(
    CanteenMenuItem("FD-01", "Ghee Podi Dosa", "Breakfast", 45, isVeg = true, isTodaySpecial = true, description = "Crispy golden dosa roasted in pure ghee coated with spicy homemade idli podi."),
    CanteenMenuItem("FD-02", "Mini Tiffin (Idli, Medu Vada, Mini Dosa, Kesari)", "Breakfast", 60, isVeg = true, description = "Classic South Indian breakfast combo with filter coffee."),
    CanteenMenuItem("FD-03", "South Indian Special Veg Meals", "Lunch", 80, isVeg = true, description = "Ponni rice, sambar, rasam, kootu, poriyal, curd, appalam, and sweet payasam."),
    CanteenMenuItem("FD-04", "Chettinad Chicken Biryani", "Lunch", 140, isVeg = false, isTodaySpecial = true, description = "Fragrant seeraga samba rice cooked with tender farm chicken, served with raita & brinjal gravy."),
    CanteenMenuItem("FD-05", "Crispy Onion Samosa (2 Pcs) & Mint Chutney", "Snacks", 25, isVeg = true, description = "Freshly fried flaky pastry with spiced potato and onion filling."),
    CanteenMenuItem("FD-06", "Traditional Kumbakonam Degree Filter Coffee", "Beverages", 20, isVeg = true, isTodaySpecial = true, description = "Authentic chicory blend frothed with rich fresh cow milk."),
    CanteenMenuItem("FD-07", "Ginger Cardamom Masala Chai", "Beverages", 15, isVeg = true, description = "Brewed with fresh crushed ginger and aromatic green cardamom.")
  )

  val sampleCampusServices = listOf(
    Pair("Principal's Office", "Main Admin Block, Ground Floor | Phone: 0422-2591001 | Ext: 101 | Timings: 09:00 AM - 05:00 PM"),
    Pair("Dean (Academic Affairs)", "Admin Block, Room 102 | Phone: 0422-2591005 | Ext: 105 | Academic approvals & curriculum"),
    Pair("Controller of Examinations (COE)", "COE Tower, 1st Floor | Timetable, Hall Tickets, Results & Transcripts"),
    Pair("Directorate of Training & Placement", "Convention Center, 2nd Floor | Placement registration, drives & internship NOCs"),
    Pair("Central Library & Digital Knowledge Hub", "Aryabhatta Library Building | 08:00 AM - 08:00 PM | E-journals & Book circulation"),
    Pair("Campus Medical & First-Aid Center", "Near Kaveri Hostel | 24/7 Duty Doctor & Ambulance: +91 94430 00108"),
    Pair("Anti-Ragging & Student Grievance Cell", "Toll Free: 1800-425-9999 | Strict Zero-Tolerance Campus"),
    Pair("Campus Accounts & Fee Section", "Admin Block Counter 4 & 5 | UPI, NEFT, Challan & Scholarship verifications"),
    Pair("IT Helpdesk & Wi-Fi NOC", "Computing Complex Room 104 | Wi-Fi login, portal passwords & lab software"),
    Pair("Transport & Bus Pass Office", "Main Gate Administrative Cabin | Route schedules & Smart Card passes")
  )

  val sampleCertificates = listOf(
    CertificateItem("CERT-01", "NPTEL Elite + Silver Certificate", "Problem Solving through Programming in C", "IIT Madras & SWAYAM", "15/05/2024", "NPTEL24CS18S349102", "NPTEL Elite"),
    CertificateItem("CERT-02", "First Prize - State Level Hackathon", "HACK-FOR-TAMILNADU 2025", "Anna University & TNeGA", "22/02/2025", "AU-TNEGA-HACK-042", "First Place"),
    CertificateItem("CERT-03", "IEEE Student Member Certificate", "Active Technical Contributor", "IEEE Madras Section", "10/01/2025", "IEEE-STU-9402102", "Participation"),
    CertificateItem("CERT-04", "Oracle Certified Associate", "Java SE 8 Programmer I", "Oracle Corporation", "14/11/2024", "OCA-JAVA-881290", "Merit")
  )

  val sampleProjects = listOf(
    ProjectItem(
      title = "Smart Campus IoT Energy & Classroom Automation",
      techStack = "Kotlin, Jetpack Compose, ESP32, MQTT, Firebase, Python",
      duration = "4 Months (Jan - Apr 2026)",
      description = "Engineered smart classroom power controller reducing ambient energy wastage by 32% across 40 college lecture halls using PIR occupancy sensors and mobile dashboard."
    ),
    ProjectItem(
      title = "Tamil OCR & Automated Answer Script Digitizer",
      techStack = "Python, PyTorch, OpenCV, Flask REST API, SQLite",
      duration = "3 Months (Sep - Nov 2025)",
      description = "Developed deep learning pipeline recognizing handwritten Tamil scripts and evaluating marks with 94.2% accuracy for university examinations."
    )
  )

  val sampleGrievances = listOf(
    GrievanceTicket(
      ticketId = "GRV-2026-081",
      category = "Infrastructure",
      subject = "Projector HDMI cable faulty in Lecture Hall LH-204",
      description = "The main overhead projector in LH-204 has a damaged HDMI connector, causing display flicker during afternoon DBMS and Computer Networks lectures.",
      submittedDate = "01/09/2026",
      status = "IN PROGRESS",
      resolutionNotes = "Technician assigned (Ticket #ET-441). Cable replacement scheduled today 04:30 PM."
    ),
    GrievanceTicket(
      ticketId = "GRV-2026-049",
      category = "Library",
      subject = "Request to add more copies of Kurose Computer Networking 8th Edition",
      description = "Only 2 copies currently available on Rack CS-08 for 120 students in Semester V.",
      submittedDate = "20/08/2026",
      status = "RESOLVED",
      resolutionNotes = "Procured 10 additional copies and activated multi-user digital e-book access."
    )
  )

  val sampleAIResponses = mapOf(
    "timetable" to "Today is Wednesday! Your schedule is:\n1. 09:00 AM: Data Structures (Dr. S. Ramanathan, LH-204)\n2. 09:50 AM: DBMS (Prof. Priya S, LH-204)\n3. 10:55 AM: Computer Networks (Dr. Karthik R, LH-204)\n4. 11:45 AM: Operating Systems (Prof. Arun Kumar, LH-204)\n5. 01:30 PM: DBMS & SQL Lab (Turing Lab-3)\n6. 03:20 PM: Library / Mentorship Hour.",
    "attendance" to "Your overall attendance is 86.4% (Safe! Above the mandatory 75% AICTE cutoff).\n⚠️ Alert: Operating Systems is currently at 70.4% (31/44 classes). You need to attend the next 6 consecutive classes to cross 75%.",
    "exams" to "Upcoming Exams:\n• IA-2 Exams start from 18/09/2026 (FN: 09:30 AM to 11:30 AM)\n• Hall: Mechanical Block MB-204, Seat: Row 3, Seat 8\n• First Paper: CS8501 Data Structures & Algorithms.",
    "mess" to "Today's Special Lunch Menu in Kaveri Hostel:\nVegetable Biryani / Special Chicken Biryani with onion raita, brinjal gravy, and Gulab Jamun!\nEvening Snack: Veg Cutlet with hot tea.",
    "placement" to "Active Placement Drives:\n1. Zoho Corporation (₹8.5 - 12 LPA) - Round 2 Coding on 15/09/2026\n2. TCS Digital (₹7.5 - 9 LPA) - Deadline 12/09/2026\n3. Freshworks (₹14.5 LPA) - Applications open till 20/09/2026.",
    "fees" to "Fee Summary:\n• Tuition Fee: ₹65,000 (PAID)\n• Hostel Fee: ₹45,000 (PAID)\n• Special Training: ₹6,000 Pending (Due 25/09/2026)\n• Semester Exam Fee: ₹2,800 Due (30/09/2026)."
  )
}
