package cpe81.flashcard.app.screens

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import cpe81.flashcard.app.models.FlashCard
import cpe81.flashcard.app.viewmodels.FlashCardViewModel

@Composable
fun FlashCardList(navController: NavController, flashCardViewModel: FlashCardViewModel) {
    LaunchedEffect(Unit) {
        flashCardViewModel.getFlashCards()
    }

    val flashCards by flashCardViewModel.flashCards.collectAsState(initial = emptyList())

    if (flashCards.isEmpty()) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text("No flash cards created yet.")
        }
    } else {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(flashCards) { flashCard ->
                FlashCardItem(
                    navController = navController,
                    flashCard = flashCard,
                    deleteFn = { flashCardViewModel.deleteFlashCardById(it) }
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FlashCardItem(navController: NavController, flashCard: FlashCard, deleteFn: (id: Int) -> Unit) {
    val context = LocalContext.current

    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = flashCard.question,
                style = MaterialTheme.typography.titleMedium
            )
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                IconButton(
                    onClick = {
                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.google.com/search?q=${Uri.encode(flashCard.question)}"))
                        context.startActivity(intent)
                    }
                ) {
                    Icon(Icons.Default.Search, contentDescription = "Search")
                }
                IconButton(
                    onClick = { navController.navigate("EditFlashCard/${flashCard.id}") }
                ) {
                    Icon(Icons.Default.Edit, contentDescription = "Edit")
                }
            }
        }
    }
}