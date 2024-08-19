package cpe81.flashcard.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import cpe81.flashcard.app.screens.CreateFlashCard
import cpe81.flashcard.app.screens.EditFlashCard
import cpe81.flashcard.app.screens.FlashCardList
import cpe81.flashcard.app.screens.PlayFlashCardScreen
import cpe81.flashcard.app.viewmodels.FlashCardViewModel
import cpe81.flashcard.app.viewmodels.CreateFlashCardViewModel
import cpe81.flashcard.app.ui.theme.FlashCardAppTheme
import cpe81.flashcard.app.viewmodels.EditFlashCardViewModel
import cpe81.flashcard.app.viewmodels.PlayFlashCardViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel as koinViewModel

class MainActivity : ComponentActivity() {

    private val flashCardViewModel: FlashCardViewModel by koinViewModel()


    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        flashCardViewModel.loadDefaultNotesIfNoneExist()


        setContent {
            FlashCardAppTheme {
                val navController = rememberNavController()
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentRoute = navBackStackEntry?.destination?.route

                Scaffold(
                    topBar = {
                        TopAppBar(
                            title = { Text("FlashCard App") },
                            navigationIcon = {
                                if (currentRoute != "Home") {
                                    IconButton(onClick = { navController.popBackStack() }) {
                                        Icon(
                                            imageVector = Icons.Filled.ArrowBack,
                                            contentDescription = "Back"
                                        )
                                    }
                                }
                            }
                        )
                    }
                ) {
                    innerPadding ->
                    Box(modifier = Modifier.padding(innerPadding)) {
                        val createFlashCardViewModel: CreateFlashCardViewModel = viewModel()
                        val editFlashCardViewModel: EditFlashCardViewModel = viewModel()
                        NavHost(navController = navController, startDestination = "Home") {
                            composable("Home") {
                                Home(navController = navController)
                            }
                            composable("FlashCardList") {
                                FlashCardList(navController = navController, flashCardViewModel = flashCardViewModel)
                            }
                            composable("PlayFlashCards") {
                                val playViewModel: PlayFlashCardViewModel = viewModel()
                                PlayFlashCardScreen(navController = navController, flashCardViewModel = flashCardViewModel, playViewModel = playViewModel
                                )
                            }
                            composable("CreateFlashCard") {
                                CreateFlashCard(
                                    navController = navController,
                                    question = createFlashCardViewModel.question,
                                    onQuestionChange = { createFlashCardViewModel.updateQuestion(it) },
                                    answers = createFlashCardViewModel.answers,
                                    onAnswersChange = { createFlashCardViewModel.updateAnswers(it) },
                                    correctAnswerIndex = createFlashCardViewModel.correctAnswerIndex,
                                    onCorrectAnswerIndexChange = { createFlashCardViewModel.updateCorrectAnswerIndex(it) },
                                    createFlashCardFn = { question, answers, correctAnswerIndex ->
                                        flashCardViewModel.createFlashCard(question, answers, correctAnswerIndex)
                                    }
                                )
                            }
                            composable("EditFlashCard/{flashCardId}", arguments = listOf(navArgument("flashCardId") {
                                type = NavType.StringType
                            })
                            ) { backStackEntry ->
                                val flashCardId = backStackEntry.arguments?.getString("flashCardId")
                                flashCardId?.let { flashCardIdParam: String -> EditFlashCard(flashCardIdParam, editFlashCardViewModel, flashCardViewModel, navController = navController) }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun Home(navController: NavController) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            "Welcome to FlashCard App",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 32.dp)
        )

        Button(
            onClick = { navController.navigate("CreateFlashCard") },
            modifier = Modifier
                .fillMaxWidth(0.8f)
                .padding(vertical = 8.dp)
        ) {
            Text("Create Flash Card")
        }

        Button(
            onClick = { navController.navigate("FlashCardList") },
            modifier = Modifier
                .fillMaxWidth(0.8f)
                .padding(vertical = 8.dp)
        ) {
            Text("View Flash Cards")
        }

        Button(
            onClick = { navController.navigate("PlayFlashCards") },
            modifier = Modifier
                .fillMaxWidth(0.8f)
                .padding(vertical = 8.dp)
        ) {
            Text("Play Flash Cards")
        }
    }
}