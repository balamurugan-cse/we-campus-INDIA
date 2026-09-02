package com.example.ui.screens.community

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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.data.model.CommunityPost
import com.example.data.model.LostFoundItem
import com.example.data.model.MarketplaceItem
import com.example.data.repository.CampusRepository
import com.example.ui.theme.*

enum class CommunityTab(val title: String) {
  FEED("Campus Feed"),
  LOST_FOUND("Lost & Found"),
  MARKETPLACE("Student Market")
}

@Composable
fun CommunityScreen(
  repository: CampusRepository,
  modifier: Modifier = Modifier
) {
  var selectedTab by remember { mutableStateOf(CommunityTab.FEED) }
  val communityPosts by repository.communityPosts.collectAsState()
  val lostAndFound by repository.lostAndFound.collectAsState()
  val marketplace by repository.marketplace.collectAsState()

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
      CommunityTab.values().forEach { tab ->
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
          modifier = Modifier.testTag("community_tab_${tab.name.lowercase()}")
        )
      }
    }

    when (selectedTab) {
      CommunityTab.FEED -> FeedView(
        posts = communityPosts,
        onToggleLike = { repository.togglePostLike(it) },
        onToggleSave = { repository.togglePostSave(it) },
        onAddComment = { id, text -> repository.addComment(id, text) },
        onNewPost = { title, body, cat -> repository.addCommunityPost(title, body, cat) }
      )
      CommunityTab.LOST_FOUND -> LostAndFoundView(
        items = lostAndFound,
        onReport = { type, title, cat, loc, desc, ph -> repository.reportLostOrFound(type, title, cat, loc, desc, ph) },
        onMarkRecovered = { repository.markLostFoundRecovered(it) }
      )
      CommunityTab.MARKETPLACE -> MarketplaceView(
        items = marketplace,
        onPostItem = { title, cat, price, cond, desc, ph -> repository.addMarketplaceItem(title, cat, price, cond, desc, ph) }
      )
    }
  }
}

// -------------------------------------------------------------
// 1. CAMPUS FEED & FORUMS
// -------------------------------------------------------------
@Composable
private fun FeedView(
  posts: List<CommunityPost>,
  onToggleLike: (String) -> Unit,
  onToggleSave: (String) -> Unit,
  onAddComment: (String, String) -> Unit,
  onNewPost: (String, String, String) -> Unit
) {
  var showNewPostDialog by remember { mutableStateOf(false) }
  var commentDialogOpenForPost by remember { mutableStateOf<CommunityPost?>(null) }
  var selectedCategory by remember { mutableStateOf("All") }

  val categories = listOf("All", "Placements", "Hackathon", "GATE Prep", "Academics", "Hostel")

  val filteredPosts = remember(selectedCategory, posts) {
    if (selectedCategory == "All") posts
    else posts.filter { it.category.equals(selectedCategory, ignoreCase = true) }
  }

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
        LazyRow(
          horizontalArrangement = Arrangement.spacedBy(8.dp),
          modifier = Modifier.weight(1f)
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

        Spacer(modifier = Modifier.width(8.dp))

        Button(
          onClick = { showNewPostDialog = true },
          colors = ButtonDefaults.buttonColors(containerColor = PrimaryNavy),
          shape = RoundedCornerShape(8.dp),
          contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp),
          modifier = Modifier.testTag("create_community_post_button")
        ) {
          Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(16.dp))
          Spacer(modifier = Modifier.width(4.dp))
          Text("Post", fontSize = 12.sp)
        }
      }
    }

    items(filteredPosts) { post ->
      Card(
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(2.dp),
        modifier = Modifier.fillMaxWidth().testTag("community_post_${post.id}")
      ) {
        Column(modifier = Modifier.padding(16.dp)) {
          // Author Header
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
              Box(
                modifier = Modifier
                  .size(38.dp)
                  .clip(CircleShape)
                  .background(PrimaryNavy),
                contentAlignment = Alignment.Center
              ) {
                Text(post.authorName.take(2).uppercase(), color = SaffronGold, fontWeight = FontWeight.Bold, fontSize = 13.sp)
              }
              Spacer(modifier = Modifier.width(10.dp))
              Column {
                Text(post.authorName, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                Text("${post.authorDept} • ${post.timestamp}", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
              }
            }

            Surface(
              color = PrimaryNavy.copy(alpha = 0.08f),
              shape = RoundedCornerShape(4.dp)
            ) {
              Text(
                text = post.category,
                color = PrimaryNavy,
                fontWeight = FontWeight.Bold,
                fontSize = 10.sp,
                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
              )
            }
          }

          Spacer(modifier = Modifier.height(10.dp))

          Text(post.title, style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold, fontSize = 15.sp))
          Spacer(modifier = Modifier.height(6.dp))
          Text(post.body, style = MaterialTheme.typography.bodyMedium.copy(color = MaterialTheme.colorScheme.onSurfaceVariant, lineHeight = 20.sp))

          Spacer(modifier = Modifier.height(12.dp))

          // Action Row: Like, Comment, Save
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
              Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.clickable { onToggleLike(post.id) }
              ) {
                Icon(
                  imageVector = if (post.isLiked) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                  contentDescription = "Like",
                  tint = if (post.isLiked) CrimsonRed else MaterialTheme.colorScheme.onSurfaceVariant,
                  modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text("${post.likesCount}", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
              }

              Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.clickable { commentDialogOpenForPost = post }
              ) {
                Icon(
                  imageVector = Icons.Default.ChatBubbleOutline,
                  contentDescription = "Comments",
                  tint = MaterialTheme.colorScheme.onSurfaceVariant,
                  modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text("${post.commentsCount}", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
              }
            }

            IconButton(
              onClick = { onToggleSave(post.id) },
              modifier = Modifier.size(32.dp)
            ) {
              Icon(
                imageVector = if (post.isSaved) Icons.Default.Bookmark else Icons.Default.BookmarkBorder,
                contentDescription = "Save",
                tint = if (post.isSaved) SaffronGoldDark else MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.size(20.dp)
              )
            }
          }
        }
      }
    }
  }

  // Create Post Dialog
  if (showNewPostDialog) {
    var title by remember { mutableStateOf("") }
    var body by remember { mutableStateOf("") }
    var category by remember { mutableStateOf("Placements") }

    AlertDialog(
      onDismissRequest = { showNewPostDialog = false },
      title = { Text("Create Campus Post", fontWeight = FontWeight.Bold) },
      text = {
        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
          OutlinedTextField(
            value = title,
            onValueChange = { title = it },
            label = { Text("Post Title") },
            placeholder = { Text("e.g. Looking for frontend dev for Hack-Bharat") },
            modifier = Modifier.fillMaxWidth()
          )
          OutlinedTextField(
            value = category,
            onValueChange = { category = it },
            label = { Text("Category (Placements, Hackathon, GATE, General)") },
            modifier = Modifier.fillMaxWidth()
          )
          OutlinedTextField(
            value = body,
            onValueChange = { body = it },
            label = { Text("Post Body / Question") },
            modifier = Modifier.fillMaxWidth(),
            minLines = 3
          )
        }
      },
      confirmButton = {
        Button(
          onClick = {
            if (title.isNotBlank() && body.isNotBlank()) onNewPost(title, body, category)
            showNewPostDialog = false
          },
          colors = ButtonDefaults.buttonColors(containerColor = PrimaryNavy)
        ) {
          Text("Publish")
        }
      },
      dismissButton = { TextButton(onClick = { showNewPostDialog = false }) { Text("Cancel") } }
    )
  }

  // Comment Dialog
  commentDialogOpenForPost?.let { post ->
    var commentText by remember { mutableStateOf("") }

    Dialog(onDismissRequest = { commentDialogOpenForPost = null }) {
      Surface(
        shape = RoundedCornerShape(20.dp),
        color = MaterialTheme.colorScheme.surface,
        modifier = Modifier.fillMaxWidth(0.95f).fillMaxHeight(0.7f)
      ) {
        Column(modifier = Modifier.padding(16.dp)) {
          Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
            Text("Discussion (${post.comments.size})", fontWeight = FontWeight.Bold, fontSize = 16.sp)
            IconButton(onClick = { commentDialogOpenForPost = null }) { Icon(Icons.Default.Close, contentDescription = null) }
          }
          Text(post.title, fontWeight = FontWeight.SemiBold, fontSize = 13.sp, color = PrimaryNavy)
          Divider(modifier = Modifier.padding(vertical = 8.dp))

          LazyColumn(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            items(post.comments) { comm ->
              Card(
                shape = RoundedCornerShape(8.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)),
                modifier = Modifier.fillMaxWidth()
              ) {
                Column(modifier = Modifier.padding(8.dp)) {
                  Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text(comm.authorName, fontWeight = FontWeight.Bold, fontSize = 11.sp)
                    Text(comm.timestamp, fontSize = 9.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                  }
                  Spacer(modifier = Modifier.height(2.dp))
                  Text(comm.text, fontSize = 12.sp)
                }
              }
            }
          }

          Spacer(modifier = Modifier.height(8.dp))

          Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            OutlinedTextField(
              value = commentText,
              onValueChange = { commentText = it },
              placeholder = { Text("Write a reply...", fontSize = 11.sp) },
              modifier = Modifier.weight(1f),
              shape = RoundedCornerShape(12.dp),
              singleLine = true
            )
            Spacer(modifier = Modifier.width(8.dp))
            Button(
              onClick = {
                if (commentText.isNotBlank()) {
                  onAddComment(post.id, commentText)
                  commentText = ""
                }
              },
              colors = ButtonDefaults.buttonColors(containerColor = PrimaryNavy),
              shape = RoundedCornerShape(8.dp)
            ) {
              Text("Send")
            }
          }
        }
      }
    }
  }
}

// -------------------------------------------------------------
// 2. LOST & FOUND VIEW
// -------------------------------------------------------------
@Composable
private fun LostAndFoundView(
  items: List<LostFoundItem>,
  onReport: (String, String, String, String, String, String) -> Unit,
  onMarkRecovered: (String) -> Unit
) {
  var showReportDialog by remember { mutableStateOf(false) }
  var filterType by remember { mutableStateOf("All") }

  val filteredList = remember(filterType, items) {
    if (filterType == "All") items
    else items.filter { it.type.equals(filterType, ignoreCase = true) }
  }

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
        Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
          listOf("All", "Lost", "Found").forEach { type ->
            FilterChip(
              selected = filterType == type,
              onClick = { filterType = type },
              label = { Text(type) }
            )
          }
        }

        Button(
          onClick = { showReportDialog = true },
          colors = ButtonDefaults.buttonColors(containerColor = PrimaryNavy),
          shape = RoundedCornerShape(8.dp)
        ) {
          Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(16.dp))
          Spacer(modifier = Modifier.width(4.dp))
          Text("Report Item", fontSize = 12.sp)
        }
      }
    }

    items(filteredList) { item ->
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
            Surface(
              color = if (item.type == "LOST") CrimsonRedLight else EmeraldGreenLight,
              shape = RoundedCornerShape(4.dp)
            ) {
              Text(
                text = item.type,
                color = if (item.type == "LOST") CrimsonRed else EmeraldGreenDark,
                fontWeight = FontWeight.Bold,
                fontSize = 10.sp,
                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
              )
            }

            Surface(
              color = if (item.isRecovered) EmeraldGreenLight else SaffronGoldLight,
              shape = RoundedCornerShape(4.dp)
            ) {
              Text(
                text = if (item.isRecovered) "RECOVERED / HANDED OVER" else "ACTIVE",
                color = if (item.isRecovered) EmeraldGreenDark else SaffronGoldDark,
                fontWeight = FontWeight.Bold,
                fontSize = 9.sp,
                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
              )
            }
          }

          Spacer(modifier = Modifier.height(6.dp))
          Text(item.title, fontWeight = FontWeight.Bold, fontSize = 14.sp)
          Spacer(modifier = Modifier.height(2.dp))
          Text(item.description, fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
          Spacer(modifier = Modifier.height(6.dp))
          Text("Location: ${item.locationFoundOrLost} • Reported: ${item.reportedDate}", fontSize = 11.sp, color = PrimaryNavy, fontWeight = FontWeight.SemiBold)
          Spacer(modifier = Modifier.height(2.dp))
          Text("Reported by: ${item.reportedBy} (Tel: ${item.contactPhone})", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)

          if (!item.isRecovered) {
            Spacer(modifier = Modifier.height(8.dp))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
              OutlinedButton(
                onClick = { onMarkRecovered(item.id) },
                shape = RoundedCornerShape(6.dp),
                contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp)
              ) {
                Text("Mark as Recovered", fontSize = 11.sp)
              }
            }
          }
        }
      }
    }
  }

  if (showReportDialog) {
    var type by remember { mutableStateOf("LOST") }
    var title by remember { mutableStateOf("") }
    var cat by remember { mutableStateOf("Electronics") }
    var location by remember { mutableStateOf("") }
    var desc by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("+91 98401 23456") }

    AlertDialog(
      onDismissRequest = { showReportDialog = false },
      title = { Text("Report Lost or Found Item", fontWeight = FontWeight.Bold) },
      text = {
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
          Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            FilterChip(selected = type == "LOST", onClick = { type = "LOST" }, label = { Text("I Lost Something") })
            FilterChip(selected = type == "FOUND", onClick = { type = "FOUND" }, label = { Text("I Found Something") })
          }
          OutlinedTextField(value = title, onValueChange = { title = it }, label = { Text("Item Title (e.g. Casio fx-991EX)") }, modifier = Modifier.fillMaxWidth())
          OutlinedTextField(value = location, onValueChange = { location = it }, label = { Text("Campus Location (e.g. Mech Lab 2)") }, modifier = Modifier.fillMaxWidth())
          OutlinedTextField(value = desc, onValueChange = { desc = it }, label = { Text("Description & Distinguishing Marks") }, modifier = Modifier.fillMaxWidth())
          OutlinedTextField(value = phone, onValueChange = { phone = it }, label = { Text("Contact Mobile Number") }, modifier = Modifier.fillMaxWidth())
        }
      },
      confirmButton = {
        Button(
          onClick = {
            if (title.isNotBlank()) onReport(type, title, cat, location, desc, phone)
            showReportDialog = false
          },
          colors = ButtonDefaults.buttonColors(containerColor = PrimaryNavy)
        ) {
          Text("Submit Report")
        }
      },
      dismissButton = { TextButton(onClick = { showReportDialog = false }) { Text("Cancel") } }
    )
  }
}

// -------------------------------------------------------------
// 3. STUDENT MARKETPLACE VIEW
// -------------------------------------------------------------
@Composable
private fun MarketplaceView(
  items: List<MarketplaceItem>,
  onPostItem: (String, String, String, String, String, String) -> Unit
) {
  var showPostItemDialog by remember { mutableStateOf(false) }

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
          Text("Student Buy & Sell Exchange", style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold))
          Text("College books, drafters, blazers & stationery", style = MaterialTheme.typography.bodySmall.copy(color = MaterialTheme.colorScheme.onSurfaceVariant))
        }
        Button(
          onClick = { showPostItemDialog = true },
          colors = ButtonDefaults.buttonColors(containerColor = PrimaryNavy),
          shape = RoundedCornerShape(8.dp)
        ) {
          Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(16.dp))
          Spacer(modifier = Modifier.width(4.dp))
          Text("Sell Item", fontSize = 12.sp)
        }
      }
    }

    items(items) { item ->
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
            Column(modifier = Modifier.weight(1f)) {
              Text(item.title, fontWeight = FontWeight.Bold, fontSize = 14.sp)
              Text("Condition: ${item.condition} • Category: ${item.category}", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
            Surface(
              color = EmeraldGreenLight,
              shape = RoundedCornerShape(6.dp)
            ) {
              Text(
                text = item.price,
                color = EmeraldGreenDark,
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp,
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
              )
            }
          }

          Spacer(modifier = Modifier.height(6.dp))
          Text(item.description, fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
          Spacer(modifier = Modifier.height(8.dp))

          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Text("Seller: ${item.sellerName} (${item.sellerDept})", fontSize = 11.sp, color = PrimaryNavy, fontWeight = FontWeight.Medium)
            Surface(
              color = PrimaryNavy.copy(alpha = 0.1f),
              shape = RoundedCornerShape(6.dp)
            ) {
              Row(
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                verticalAlignment = Alignment.CenterVertically
              ) {
                Icon(Icons.Default.Phone, contentDescription = null, tint = PrimaryNavy, modifier = Modifier.size(14.dp))
                Spacer(modifier = Modifier.width(4.dp))
                Text(item.contactNumber, fontSize = 10.sp, color = PrimaryNavy, fontWeight = FontWeight.Bold)
              }
            }
          }
        }
      }
    }
  }

  if (showPostItemDialog) {
    var title by remember { mutableStateOf("") }
    var cat by remember { mutableStateOf("Books") }
    var price by remember { mutableStateOf("300") }
    var cond by remember { mutableStateOf("Good Condition") }
    var desc by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("+91 98401 23456") }

    AlertDialog(
      onDismissRequest = { showPostItemDialog = false },
      title = { Text("List Item for Sale", fontWeight = FontWeight.Bold) },
      text = {
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
          OutlinedTextField(value = title, onValueChange = { title = it }, label = { Text("Item Name (e.g. Engineering Graphics Kit)") }, modifier = Modifier.fillMaxWidth())
          OutlinedTextField(value = price, onValueChange = { price = it }, label = { Text("Price in ₹ INR") }, modifier = Modifier.fillMaxWidth())
          OutlinedTextField(value = cond, onValueChange = { cond = it }, label = { Text("Condition (e.g. Like New, Minor Wear)") }, modifier = Modifier.fillMaxWidth())
          OutlinedTextField(value = desc, onValueChange = { desc = it }, label = { Text("Description") }, modifier = Modifier.fillMaxWidth())
          OutlinedTextField(value = phone, onValueChange = { phone = it }, label = { Text("Contact Phone") }, modifier = Modifier.fillMaxWidth())
        }
      },
      confirmButton = {
        Button(
          onClick = {
            if (title.isNotBlank()) onPostItem(title, cat, price, cond, desc, phone)
            showPostItemDialog = false
          },
          colors = ButtonDefaults.buttonColors(containerColor = PrimaryNavy)
        ) {
          Text("Publish Listing")
        }
      },
      dismissButton = { TextButton(onClick = { showPostItemDialog = false }) { Text("Cancel") } }
    )
  }
}
