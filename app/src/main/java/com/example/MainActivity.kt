package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import com.example.data.repository.CampusRepository
import com.example.ui.components.*
import com.example.ui.screens.academics.AcademicsScreen
import com.example.ui.screens.ai.CampusAiDialog
import com.example.ui.screens.community.CommunityScreen
import com.example.ui.screens.events.EventsAndClubsScreen
import com.example.ui.screens.home.HomeScreen
import com.example.ui.screens.profile.ProfileScreen
import com.example.ui.screens.services.CampusServicesScreen
import com.example.ui.theme.CampusConnectTheme

class MainActivity : ComponentActivity() {

  private val campusRepository = CampusRepository()

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    enableEdgeToEdge()
    setContent {
      CampusConnectTheme {
        CampusConnectApp(repository = campusRepository)
      }
    }
  }
}

@Composable
fun CampusConnectApp(
  repository: CampusRepository,
  modifier: Modifier = Modifier
) {
  var currentTab by remember { mutableStateOf(MainNavTab.HOME) }
  var serviceInitialSection by remember { mutableStateOf<String?>(null) }

  // Dialog States
  var showDigitalIdDialog by remember { mutableStateOf(false) }
  var showNotificationsDialog by remember { mutableStateOf(false) }
  var showSearchDialog by remember { mutableStateOf(false) }
  var showEmergencySosDialog by remember { mutableStateOf(false) }
  var showCampusAiDialog by remember { mutableStateOf(false) }

  val currentUser by repository.currentUser.collectAsState()
  val notifications by repository.notifications.collectAsState()
  val events by repository.events.collectAsState()
  val academicSubjects = com.example.data.mock.CampusMockData.sampleAcademicSubjects
  val placements by repository.placements.collectAsState()
  val libraryBooks by repository.libraryBooks.collectAsState()
  val clubs by repository.clubs.collectAsState()

  val configuration = LocalConfiguration.current
  val isWideScreen = configuration.screenWidthDp >= 600

  Scaffold(
    modifier = modifier.fillMaxSize(),
    topBar = {
      CampusTopBar(
        currentUser = currentUser,
        notifications = notifications,
        onRoleSelected = { repository.switchRole(it) },
        onSearchClicked = { showSearchDialog = true },
        onNotificationsClicked = { showNotificationsDialog = true },
        onDigitalIdClicked = { showDigitalIdDialog = true },
        onAiAssistantClicked = { showCampusAiDialog = true }
      )
    },
    bottomBar = {
      if (!isWideScreen) {
        CampusBottomNavigationBar(
          currentTab = currentTab,
          onTabSelected = { currentTab = it }
        )
      }
    }
  ) { innerPadding ->
    Box(
      modifier = Modifier
        .fillMaxSize()
        .padding(innerPadding)
        .background(MaterialTheme.colorScheme.background)
    ) {
      Row(modifier = Modifier.fillMaxSize()) {
        if (isWideScreen) {
          CampusNavigationRail(
            currentTab = currentTab,
            onTabSelected = { currentTab = it },
            onDigitalIdClicked = { showDigitalIdDialog = true },
            onAiAssistantClicked = { showCampusAiDialog = true }
          )
        }

        Box(
          modifier = Modifier
            .weight(1f)
            .fillMaxHeight()
        ) {
          when (currentTab) {
            MainNavTab.HOME -> HomeScreen(
              repository = repository,
              onNavigateTab = { currentTab = it },
              onOpenDigitalId = { showDigitalIdDialog = true },
              onOpenCampusAi = { showCampusAiDialog = true },
              onOpenServiceSection = { section ->
                serviceInitialSection = section
                currentTab = MainNavTab.SERVICES
              }
            )
            MainNavTab.ACADEMICS -> AcademicsScreen(repository = repository)
            MainNavTab.EVENTS -> EventsAndClubsScreen(repository = repository)
            MainNavTab.SERVICES -> CampusServicesScreen(
              repository = repository,
              initialSection = serviceInitialSection
            )
            MainNavTab.COMMUNITY -> CommunityScreen(repository = repository)
            MainNavTab.PROFILE -> ProfileScreen(
              repository = repository,
              onOpenDigitalId = { showDigitalIdDialog = true }
            )
          }

          // Floating Action Hub for fast Indian College operations
          QuickActionFab(
            onOpenDigitalId = { showDigitalIdDialog = true },
            onOpenCampusAi = { showCampusAiDialog = true },
            onOpenEmergencySos = { showEmergencySosDialog = true },
            onOpenCanteenOrder = {
              serviceInitialSection = "canteen"
              currentTab = MainNavTab.SERVICES
            },
            onOpenLostFound = {
              currentTab = MainNavTab.COMMUNITY
            },
            modifier = Modifier.fillMaxSize()
          )
        }
      }
    }
  }

  // -------------------------------------------------------------
  // DIALOG OVERLAYS
  // -------------------------------------------------------------
  if (showDigitalIdDialog) {
    DigitalIdCardDialog(
      user = currentUser,
      onDismiss = { showDigitalIdDialog = false }
    )
  }

  if (showNotificationsDialog) {
    NotificationSheetDialog(
      notifications = notifications,
      onNotificationClick = { id -> repository.markNotificationRead(id) },
      onMarkAllRead = { repository.markAllNotificationsRead() },
      onDismiss = { showNotificationsDialog = false }
    )
  }

  if (showSearchDialog) {
    GlobalSearchDialog(
      events = events,
      subjects = academicSubjects,
      placements = placements,
      books = libraryBooks,
      clubs = clubs,
      onDismiss = { showSearchDialog = false },
      onNavigateToSection = { section ->
        showSearchDialog = false
        when (section) {
          "Academics" -> currentTab = MainNavTab.ACADEMICS
          "Events" -> currentTab = MainNavTab.EVENTS
          "Placements" -> {
            serviceInitialSection = "placements"
            currentTab = MainNavTab.SERVICES
          }
          "Library" -> {
            serviceInitialSection = "library"
            currentTab = MainNavTab.SERVICES
          }
        }
      }
    )
  }

  if (showEmergencySosDialog) {
    EmergencySosDialog(onDismiss = { showEmergencySosDialog = false })
  }

  if (showCampusAiDialog) {
    CampusAiDialog(
      repository = repository,
      onDismiss = { showCampusAiDialog = false }
    )
  }
}
