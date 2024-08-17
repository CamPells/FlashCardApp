package cpe81.flashcard.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import cpe81.flashcard.app.ui.theme.FlashCardAppTheme

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            FlashCardAppTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    HomePage(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

@Composable
fun HomePage(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text ="FlashCard App")
        Button(
            onClick = { /* Navigate to View Flash Cards screen */ },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "View Flash Cards")
        }
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = { /* Navigate to Create Flash Card screen */ },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "Create Flash Card")
        }
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = { /* Navigate to Play Flash Cards screen */ },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "Play Flash Cards")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HomePagePreview() {
    FlashCardAppTheme {
        HomePage()
    }
}