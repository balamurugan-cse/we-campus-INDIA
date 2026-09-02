package com.example.ui.screens.ai

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
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
import androidx.compose.ui.window.DialogProperties
import com.example.data.model.CampusAIMessage
import com.example.data.repository.CampusRepository
import com.example.ui.theme.*
import kotlinx.coroutines.launch

@Composable
fun CampusAiDialog(
  repository: CampusRepository,
  onDismiss: () -> Unit
) {
  val messages by repository.aiChatMessages.collectAsState()
  var currentInput by remember { mutableStateOf("") }
  val listState = rememberLazyListState()
  val coroutineScope = rememberCoroutineScope()

  val suggestedPrompts = listOf(
    "What is my next class today?",
    "Is my attendance safe (>75%)?",
    "When is my IA-2 exam?",
    "What is today's Mess lunch menu?",
    "Zoho placement cutoff and package?",
    "How do I renew my library book?"
  )

  Dialog(
    onDismissRequest = onDismiss,
    properties = DialogProperties(usePlatformDefaultWidth = false)
  ) {
    Surface(
      modifier = Modifier
        .fillMaxWidth(0.96f)
        .fillMaxHeight(0.90f)
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
            Box(
              modifier = Modifier
                .size(38.dp)
                .clip(CircleShape)
                .background(PrimaryNavy),
              contentAlignment = Alignment.Center
            ) {
              Icon(
                imageVector = Icons.Default.AutoAwesome,
                contentDescription = null,
                tint = SaffronGold,
                modifier = Modifier.size(22.dp)
              )
            }

            Spacer(modifier = Modifier.width(10.dp))

            Column {
              Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                  text = "Campus AI Companion",
                  style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = PrimaryNavy
                  )
                )
                Spacer(modifier = Modifier.width(4.dp))
                Surface(
                  color = EmeraldGreenLight,
                  shape = RoundedCornerShape(4.dp)
                ) {
                  Text(
                    text = "LIVE ERP",
                    color = EmeraldGreenDark,
                    fontSize = 8.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(horizontal = 4.dp, vertical = 1.dp)
                  )
                }
              }
              Text(
                text = "Instant answers for schedule, marks, food & placements",
                style = MaterialTheme.typography.bodySmall.copy(
                  color = MaterialTheme.colorScheme.onSurfaceVariant,
                  fontSize = 10.sp
                )
              )
            }
          }

          IconButton(onClick = onDismiss) {
            Icon(Icons.Default.Close, contentDescription = "Close")
          }
        }

        Divider(modifier = Modifier.padding(vertical = 8.dp))

        // Chat Message Stream
        LazyColumn(
          state = listState,
          modifier = Modifier
            .weight(1f)
            .fillMaxWidth(),
          verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
          items(messages) { msg ->
            ChatMessageBubble(
              message = msg,
              onPromptSelected = { prompt ->
                repository.sendAIMessage(prompt)
                coroutineScope.launch { listState.animateScrollToItem(messages.size) }
              }
            )
          }
        }

        Spacer(modifier = Modifier.height(6.dp))

        // Suggested Prompts Quick Row
        LazyRow(
          horizontalArrangement = Arrangement.spacedBy(6.dp),
          modifier = Modifier.fillMaxWidth()
        ) {
          items(suggestedPrompts) { prompt ->
            Surface(
              shape = RoundedCornerShape(16.dp),
              color = PrimaryNavy.copy(alpha = 0.08f),
              modifier = Modifier
                .clip(RoundedCornerShape(16.dp))
                .clickable {
                  repository.sendAIMessage(prompt)
                  coroutineScope.launch { listState.animateScrollToItem(messages.size) }
                }
            ) {
              Text(
                text = prompt,
                color = PrimaryNavy,
                fontSize = 11.sp,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
              )
            }
          }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Input Field and Send Button
        Row(
          modifier = Modifier.fillMaxWidth(),
          verticalAlignment = Alignment.CenterVertically
        ) {
          OutlinedTextField(
            value = currentInput,
            onValueChange = { currentInput = it },
            placeholder = { Text("Ask anything about your campus...", fontSize = 12.sp) },
            singleLine = true,
            shape = RoundedCornerShape(20.dp),
            modifier = Modifier
              .weight(1f)
              .testTag("campus_ai_input_field"),
            colors = OutlinedTextFieldDefaults.colors(
              focusedBorderColor = PrimaryNavy,
              unfocusedBorderColor = MaterialTheme.colorScheme.outline
            )
          )

          Spacer(modifier = Modifier.width(8.dp))

          IconButton(
            onClick = {
              if (currentInput.isNotBlank()) {
                repository.sendAIMessage(currentInput.trim())
                currentInput = ""
                coroutineScope.launch { listState.animateScrollToItem(messages.size) }
              }
            },
            modifier = Modifier
              .size(48.dp)
              .clip(CircleShape)
              .background(PrimaryNavy)
              .testTag("campus_ai_send_button")
          ) {
            Icon(
              imageVector = Icons.Default.Send,
              contentDescription = "Send",
              tint = SaffronGold,
              modifier = Modifier.size(20.dp)
            )
          }
        }
      }
    }
  }
}

@Composable
private fun ChatMessageBubble(
  message: CampusAIMessage,
  onPromptSelected: (String) -> Unit
) {
  val isUser = message.sender == "USER"

  Column(
    modifier = Modifier.fillMaxWidth(),
    horizontalAlignment = if (isUser) Alignment.End else Alignment.Start
  ) {
    Row(
      modifier = Modifier.widthIn(max = 320.dp),
      verticalAlignment = Alignment.Top,
      horizontalArrangement = if (isUser) Arrangement.End else Arrangement.Start
    ) {
      if (!isUser) {
        Box(
          modifier = Modifier
            .size(28.dp)
            .clip(CircleShape)
            .background(PrimaryNavy),
          contentAlignment = Alignment.Center
        ) {
          Icon(
            imageVector = Icons.Default.AutoAwesome,
            contentDescription = null,
            tint = SaffronGold,
            modifier = Modifier.size(16.dp)
          )
        }
        Spacer(modifier = Modifier.width(8.dp))
      }

      Surface(
        shape = RoundedCornerShape(
          topStart = 16.dp,
          topEnd = 16.dp,
          bottomStart = if (isUser) 16.dp else 4.dp,
          bottomEnd = if (isUser) 4.dp else 16.dp
        ),
        color = if (isUser) PrimaryNavy else MaterialTheme.colorScheme.surfaceVariant,
        tonalElevation = if (isUser) 0.dp else 1.dp
      ) {
        Column(modifier = Modifier.padding(12.dp)) {
          Text(
            text = message.messageText,
            color = if (isUser) Color.White else MaterialTheme.colorScheme.onSurface,
            fontSize = 13.sp,
            lineHeight = 18.sp
          )

          if (message.isOfficialSource) {
            Spacer(modifier = Modifier.height(4.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
              Icon(
                imageVector = Icons.Default.CheckCircle,
                contentDescription = null,
                tint = EmeraldGreenDark,
                modifier = Modifier.size(12.dp)
              )
              Spacer(modifier = Modifier.width(4.dp))
              Text(
                text = "Verified from Autonomous ERP & CoE Records",
                fontSize = 9.sp,
                color = EmeraldGreenDark,
                fontWeight = FontWeight.SemiBold
              )
            }
          }
        }
      }
    }

    if (!isUser && message.suggestedPrompts.isNotEmpty()) {
      Spacer(modifier = Modifier.height(6.dp))
      LazyRow(
        horizontalArrangement = Arrangement.spacedBy(6.dp),
        modifier = Modifier.padding(start = 36.dp)
      ) {
        items(message.suggestedPrompts) { prompt ->
          Surface(
            shape = RoundedCornerShape(12.dp),
            color = SaffronGoldLight,
            modifier = Modifier
              .clip(RoundedCornerShape(12.dp))
              .clickable { onPromptSelected(prompt) }
          ) {
            Text(
              text = prompt,
              color = SaffronGoldDark,
              fontSize = 10.sp,
              fontWeight = FontWeight.Bold,
              modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
            )
          }
        }
      }
    }
  }
}
