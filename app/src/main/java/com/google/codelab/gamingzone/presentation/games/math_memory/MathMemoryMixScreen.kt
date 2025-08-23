package com.google.codelab.gamingzone.presentation.games.math_memory

import android.os.Build
import android.util.Log
import com.google.codelab.gamingzone.R
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.google.codelab.gamingzone.presentation.home_screen.SampleGames.Default
import com.google.codelab.gamingzone.presentation.profile_stats.StatsViewModel
import kotlinx.coroutines.delay


@RequiresApi(Build.VERSION_CODES.VANILLA_ICE_CREAM)
@Composable
fun MathMemoryMixScreen(
    viewModel: MathMemoryViewModel = hiltViewModel(),
    statsViewModel: StatsViewModel = hiltViewModel()
) {
    val uiState = viewModel.uiState.value

    // Show loading while uiState is null (initial data loading)
    if (uiState == null) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
        return
    }

    val answerOptions = viewModel.answerOptions.value
    val theme = uiState.theme.selectedTheme
    val phase = when {
        uiState.game.isShowCards -> "MEMORIZE"
        !uiState.game.showResult -> "SOLVE"
        else -> "RESULT"
    }

    var secondChanceUsed by remember(uiState.game.level.number) { mutableStateOf(false) }
    var showMemorizePhaseAgain by remember { mutableStateOf(false) }

    val showUnlockAnimation by remember { derivedStateOf { viewModel.showUnlockAnimation } }
    val themeName by remember { derivedStateOf { viewModel.newlyUnlockedThemeName } }

    LaunchedEffect(uiState.game.level, uiState.game.isShowCards) {
        if (uiState.game.isShowCards) {
            val baseDelay = 2400L
            val perCardDelay = 600L
            viewModel.startMemorizationTimer(baseDelay + uiState.game.level.cards.size * perCardDelay)
        }
    }

    LaunchedEffect(Unit) {
        viewModel.loadThemes()
    }

    val gameLevel = uiState.game.level.number
    val showResult = uiState.game.showResult
    val isCorrect = uiState.game.isCorrect

    // Save stats after result appears, only once per level
    var lastSavedLevel by remember { mutableStateOf<Int?>(null) }
    if (showResult && lastSavedLevel != gameLevel) {
        LaunchedEffect(gameLevel, showResult) {
            viewModel.onLevelResultAndSaveStats(
                userId = statsViewModel.userId.value ?: return@LaunchedEffect,
                isCorrect = isCorrect,
                hintsUsed = 2,
                timeSpentSeconds = 54
            )
            lastSavedLevel = gameLevel
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(theme.backgroundImage),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )
        Box(
            Modifier.fillMaxSize().background(Color.Black.copy(alpha = 0.44f))
        )

        Column(
            Modifier.fillMaxSize().padding(horizontal = 20.dp, vertical = 14.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            ThemeSelector(
                builtInThemes = Default,
                selectedTheme = theme,
                unlockedNames = uiState.theme.unlockedThemes,
                onSelect = { viewModel.onAction(MathMemoryAction.SelectTheme(it)) }
            )
            Spacer(Modifier.height(14.dp))

            Text(
                text = "Level $gameLevel",
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier.padding(bottom = 10.dp),
                color = if (phase == "RESULT" && isCorrect)
                    Color(0xFF388E3C)
                else theme.textColor
            )

            when (phase) {
                "MEMORIZE" -> Text(
                    "👀 Memorize the moves! Start from:",
                    style = MaterialTheme.typography.titleMedium,
                    color = theme.textColor,
                    modifier = Modifier.padding(bottom = 10.dp)
                )
                "SOLVE" -> Text(
                    "Now solve: Start at the number below, do each step left-to-right (no BODMAS)!",
                    style = MaterialTheme.typography.titleMedium,
                    color = theme.textColor,
                    modifier = Modifier.padding(bottom = 10.dp)
                )
                else -> { /* Result handled below */ }
            }

            StartNumberBox(
                value = uiState.game.level.start,
                textColor = theme.buttonTextColor,
                bgColor = theme.buttonColor
            )

            Spacer(Modifier.height(10.dp))

            if (phase == "MEMORIZE") {
                CardsRow(cards = uiState.game.level.cards, textColor = theme.textColor)
                Spacer(Modifier.height(10.dp))
                Text(
                    "Try to remember all the moves and the start number!",
                    color = Color.White,
                    style = MaterialTheme.typography.bodySmall
                )
            }

            if (phase == "SOLVE") {
                Spacer(Modifier.height(16.dp))
                AnswerOptionsColumn(
                    options = answerOptions,
                    onSelect = {
                        viewModel.onAction(MathMemoryAction.InputChanged(it.value.toString()))
                        viewModel.onAction(MathMemoryAction.SubmitAnswer)
                    },
                    selectedTheme = theme
                )
                Spacer(Modifier.height(8.dp))
                Text(
                    "Tip: Start at ${uiState.game.level.start}, do every step one after another.",
                    color = Color.White,
                    style = MaterialTheme.typography.bodySmall
                )

                if (!secondChanceUsed) {
                    Button(
                        onClick = {
                            // TODO: Show rewarded ad here
                            secondChanceUsed = true
                            showMemorizePhaseAgain = true
                        },
                        modifier = Modifier.fillMaxWidth().padding(vertical = 12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFFD600))
                    ) {
                        Text("👁 Watch Ad to See Moves Again")
                    }
                }
            }

            if (phase == "RESULT") {
                Spacer(Modifier.height(10.dp))
                ResultSection(
                    isCorrect = isCorrect,
                    onNext = { viewModel.onAction(MathMemoryAction.NextLevel) },
                    onRetry = { viewModel.onAction(MathMemoryAction.ResetGame) },
                    textColor = theme.textColor,
                    buttonColor = theme.buttonColor,
                    buttonTextColor = theme.buttonTextColor
                )
            }
        }
    }

    if (showUnlockAnimation && !themeName.isNullOrEmpty()) {
        AnimatedUnlockBanner(
            themeName = themeName!!,
            onContinue = { viewModel.onUnlockAnimationDismiss() }
        )
    }

    if (showMemorizePhaseAgain) {
        LaunchedEffect(Unit) {
            viewModel.onAction(MathMemoryAction.RevealCards)
            kotlinx.coroutines.delay(3000)
            viewModel.onAction(MathMemoryAction.HideCards)
            showMemorizePhaseAgain = false
        }
    }
}



// The following can be outside or in separate file ----------------------------------

@Composable
fun StartNumberBox(value: Int, textColor: Color, bgColor: Color) {
    Card(
        shape = RoundedCornerShape(13.dp),
        colors = CardDefaults.cardColors(containerColor = bgColor),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Text(
            "Start: $value",
            color = textColor,
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(horizontal = 23.dp, vertical = 6.dp)
        )
    }
}

@Composable
fun CardsRow(cards: List<MemoryCard>, textColor: Color) {

    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(18.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        item {
            cards.forEach { card ->
                ElevatedCard(
                    modifier = Modifier.size(64.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.elevatedCardColors(containerColor = Color.White.copy(alpha = 0.94f)),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                ) {
                    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text(
                            text = when (card.op) {
                                Op.ADD -> "+${card.value}"
                                Op.SUB -> "-${card.value}"
                                Op.MUL -> "×${card.value}"
                                Op.DIV -> "÷${card.value}"
                            },
                            color = textColor,
                            style = MaterialTheme.typography.headlineMedium
                        )
                    }
                }
            }
        }


    }
//    Row(horizontalArrangement = Arrangement.spacedBy(18.dp)) {
//        cards.forEach { card ->
//            ElevatedCard(
//                modifier = Modifier.size(64.dp),
//                shape = RoundedCornerShape(16.dp),
//                colors = CardDefaults.elevatedCardColors(containerColor = Color.White.copy(alpha = 0.94f)),
//                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
//            ) {
//                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
//                    Text(
//                        text = when (card.op) {
//                            Op.ADD -> "+${card.value}"
//                            Op.SUB -> "-${card.value}"
//                            Op.MUL -> "×${card.value}"
//                            Op.DIV -> "÷${card.value}"
//                        },
//                        color = textColor,
//                        style = MaterialTheme.typography.headlineMedium
//                    )
//                }
//            }
//        }
//    }
}

@Composable
fun AnswerOptionsColumn(
    options: List<AnswerOption>,
    onSelect: (AnswerOption) -> Unit,
    selectedTheme: GameTheme
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(18.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        options.forEach { option ->
            Button(
                onClick = { onSelect(option) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = selectedTheme.buttonColor,
                    contentColor = selectedTheme.buttonTextColor
                )
            ) {
                Text(
                    text = option.value.toString(),
                    color = selectedTheme.buttonTextColor,
                    style = MaterialTheme.typography.titleLarge
                )
            }
        }
    }
}

@Composable
fun ResultSection(
    isCorrect: Boolean,
    onNext: () -> Unit,
    onRetry: () -> Unit,
    textColor: Color,
    buttonColor: Color,
    buttonTextColor: Color
) {
    val color = if (isCorrect) Color(0xFF4CAF50) else Color(0xFFD32F2F)
    val message = if (isCorrect) "🎉 Correct!" else "Try Again!"
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = message,
            color = color,
            style = MaterialTheme.typography.headlineMedium
        )
        Spacer(Modifier.height(10.dp))
        if (isCorrect) {
            Button(
                onClick = onNext,
                colors = ButtonDefaults.buttonColors(
                    containerColor = buttonColor,
                    contentColor = buttonTextColor
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
            ) {
                Text("Next Level", color = textColor)
            }
        } else {
            Button(
                onClick = onRetry,
                colors = ButtonDefaults.buttonColors(
                    containerColor = buttonColor,
                    contentColor = buttonTextColor
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
            ) {
                Text("Restart", color = buttonTextColor)
            }
        }
    }
}

@Composable
fun AnimatedUnlockBanner(themeName: String, onContinue: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.7f)),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            SimpleLottieRawAnimation(
                rawRes = R.raw.cycle, // Place your animation under res/raw
                modifier = Modifier.size(140.dp)
            )
            Spacer(Modifier.height(18.dp))
            Text(
                text = "🎉 New Theme Unlocked!",
                style = MaterialTheme.typography.headlineMedium,
                color = Color.White
            )
            Text(
                text = themeName,
                style = MaterialTheme.typography.titleLarge,
                color = Color.Yellow,
                modifier = Modifier.padding(vertical = 12.dp)
            )
            Button(
                onClick = onContinue,
                modifier = Modifier.padding(top = 8.dp)
            ) {
                Text("Continue")
            }
        }
    }
}


@Composable
fun SimpleLottieRawAnimation(rawRes: Int, modifier: Modifier = Modifier) {
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(rawRes))
    val progress by animateLottieCompositionAsState(
        composition = composition,
        iterations = 4    // Play once for reward, or use LottieConstants.IterateForever
    )
    LottieAnimation(
        isPlaying = true,
        modifier = modifier,
        composition = composition,
    )
}


@Composable
fun AnswerOptionsRow(
    options: List<AnswerOption>,
    onSelect: (AnswerOption) -> Unit,
    selectedTheme: GameTheme
) {
    Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
        options.forEach { option ->
            Button(
                onClick = { onSelect(option) },
                modifier = Modifier
                    .weight(1f)
                    .height(54.dp),
                colors = ButtonDefaults.buttonColors(containerColor = selectedTheme.buttonColor)
            ) {
                Text(
                    option.value.toString(),
                    color = selectedTheme.buttonTextColor,
                    style = MaterialTheme.typography.titleLarge
                )
            }
        }
    }
}


@Composable
fun InputAnswer(
    start: Int,
    input: String,
    onInputChange: (String) -> Unit,
    onSubmit: () -> Unit,
    buttonColor: Color,
    buttonTextColor: Color,
    textColor: Color
) {
    ElevatedCard(
        Modifier
            .fillMaxWidth(0.75f)
            .padding(top = 10.dp),
        shape = RoundedCornerShape(18.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
    ) {
        Column(
            Modifier.padding(horizontal = 20.dp, vertical = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Start: $start", style = MaterialTheme.typography.titleMedium, color = textColor)
            Spacer(Modifier.height(10.dp))
            OutlinedTextField(
                value = input,
                onValueChange = onInputChange,
                label = { Text("Result?", color = textColor) },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = buttonColor,
                    unfocusedBorderColor = Color.Gray,
                    cursorColor = buttonColor,
                    focusedLabelColor = textColor,
                    unfocusedLabelColor = Color.LightGray,
                    focusedTextColor = textColor,
                    unfocusedTextColor = textColor
                ),
                textStyle = LocalTextStyle.current.copy(color = textColor),
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(Modifier.height(18.dp))
            Button(
                onClick = onSubmit,
                enabled = input.isNotBlank(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = buttonColor,
                    contentColor = buttonTextColor
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
            ) {
                Text(
                    "Submit",
                    color = buttonTextColor,
                    style = MaterialTheme.typography.titleMedium
                )
            }
        }
    }
}


@Composable
fun ThemeSelector(
    builtInThemes: List<GameTheme>,
    selectedTheme: GameTheme,
    unlockedNames: Set<String>,
    onSelect: (GameTheme) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 10.dp),
        horizontalArrangement = Arrangement.Center
    ) {
        builtInThemes.forEach { theme ->
            val unlocked = theme.name in unlockedNames
            val borderColor = if (selectedTheme.name == theme.name)
                theme.buttonColor
            else Color.Gray
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .padding(6.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .border(
                        3.dp, borderColor, RoundedCornerShape(12.dp)
                    )
                    .clickable(enabled = unlocked) { onSelect(theme) },
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(theme.backgroundImage),
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize()
                )
                if (!unlocked)
                    Box(
                        Modifier
                            .fillMaxSize()
                            .background(Color(0xBB000000)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("🔒", color = Color.White)
                    }
            }
        }
    }
}


//itklfd