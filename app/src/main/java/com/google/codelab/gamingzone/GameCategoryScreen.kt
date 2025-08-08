package com.google.codelab.gamingzone
//
//import androidx.compose.foundation.Image
//import androidx.compose.foundation.background
//import androidx.compose.foundation.clickable
//import androidx.compose.foundation.layout.Arrangement
//import androidx.compose.foundation.layout.Box
//import androidx.compose.foundation.layout.Column
//import androidx.compose.foundation.layout.PaddingValues
//import androidx.compose.foundation.layout.Spacer
//import androidx.compose.foundation.layout.aspectRatio
//import androidx.compose.foundation.layout.fillMaxSize
//import androidx.compose.foundation.layout.fillMaxWidth
//import androidx.compose.foundation.layout.height
//import androidx.compose.foundation.layout.padding
//import androidx.compose.foundation.lazy.LazyRow
//import androidx.compose.foundation.lazy.grid.GridCells
//import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
//import androidx.compose.foundation.lazy.grid.items
//import androidx.compose.foundation.lazy.items
//import androidx.compose.foundation.shape.RoundedCornerShape
//import androidx.compose.material.icons.Icons
//import androidx.compose.material.icons.filled.Favorite
//import androidx.compose.material3.Card
//import androidx.compose.material3.CardDefaults
//import androidx.compose.material3.Icon
//import androidx.compose.material3.Text
//import androidx.compose.runtime.Composable
//import androidx.compose.runtime.getValue
//import androidx.compose.runtime.mutableStateOf
//import androidx.compose.runtime.remember
//import androidx.compose.runtime.setValue
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.draw.clip
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.layout.ContentScale
//import androidx.compose.ui.res.painterResource
//import androidx.compose.ui.text.font.FontWeight
//import androidx.compose.ui.unit.dp
//import androidx.compose.ui.unit.sp
//
//@Composable
//fun GameCategoryScreen() {
//    val categories = listOf("FUN", "PUZZLE", "ENTERTAINMENT", "SKILL")
//    var selectedCategory by remember { mutableStateOf("FUN") }
//
//    Column(
//        modifier = Modifier
//            .fillMaxSize()
//            .background(Color(0xFF0B0F2F)) // Dark fantasy background
//            .padding(16.dp)
//    ) {
//        // Title
//        Text(
//            text = "⭐ ${selectedCategory.uppercase()} 😊",
//            fontSize = 28.sp,
//            fontWeight = FontWeight.Bold,
//            color = Color(0xFFFFC107)
//        )
//
//        Spacer(Modifier.height(12.dp))
//
//        // Tab Row
//        LazyRow {
//            items(categories) { category ->
//                val isSelected = category == selectedCategory
//                Text(
//                    text = category,
//                    color = if (isSelected) Color.Yellow else Color.White,
//                    fontWeight = FontWeight.Bold,
//                    modifier = Modifier
//                        .padding(horizontal = 12.dp)
//                        .clickable { selectedCategory = category }
//                )
//            }
//        }
//
//        Spacer(Modifier.height(16.dp))
//
//        // Game Grid
//        LazyVerticalGrid(
//            columns = GridCells.Fixed(2),
//            modifier = Modifier.fillMaxSize(),
//            contentPadding = PaddingValues(8.dp),
//            verticalArrangement = Arrangement.spacedBy(16.dp),
//            horizontalArrangement = Arrangement.spacedBy(16.dp)
//        ) {
//            items(getGamesForCategory(selectedCategory)) { game ->
//                GameCardItem(game)
//            }
//        }
//    }
//}
//
//data class GameItem(
//    val title: String,
//    val description: String,
//    val imageRes: Int
//)
//
//
//
//@Composable
//fun GameCardItem(game: GameItem) {
//    Card(
//        shape = RoundedCornerShape(16.dp),
//        colors = CardDefaults.cardColors(containerColor = Color(0xFF1D2240)),
//        modifier = Modifier
//            .fillMaxWidth()
//            .aspectRatio(1f)
//    ) {
//        Column(
//            modifier = Modifier.padding(12.dp),
//            verticalArrangement = Arrangement.SpaceBetween
//        ) {
//            Box(
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .height(80.dp)
//            ) {
//                Image(
//                    painter = painterResource(id = game.imageRes),
//                    contentDescription = game.title,
//                    modifier = Modifier
//                        .fillMaxSize()
//                        .clip(RoundedCornerShape(12.dp)),
//                    contentScale = ContentScale.Crop
//                )
//
//                Icon(
//                    imageVector = Icons.Default.Favorite,
//                    contentDescription = "Favorite",
//                    tint = Color.Red,
//                    modifier = Modifier
//                        .align(Alignment.TopEnd)
//                        .padding(4.dp)
//                )
//            }
//
//            Text(
//                text = game.title,
//                fontWeight = FontWeight.Bold,
//                fontSize = 14.sp,
//                color = Color.White,
//                modifier = Modifier.padding(top = 6.dp)
//            )
//            Text(
//                text = game.description,
//                fontSize = 12.sp,
//                color = Color.LightGray
//            )
//        }
//    }
//}
//
//fun getGamesForCategory(category: String): List<GameItem> {
//    return listOf(
//        GameItem("Candy Quest", "Match three puzzle game", R.drawable.star),
//        GameItem("Happy Jump", "Endless jumping action", R.drawable.star),
//        GameItem("Emoji Search", "Emoji matching fun", R.drawable.star),
//        GameItem("Ball Bounce", "Physics based challenge", R.drawable.star),
//        GameItem("Color Splash", "Creative painting game", R.drawable.star),
//        GameItem("Pet Pals", "Care for cute pets", R.drawable.star),
//        GameItem("Bubble Pop", "Bubble shooting fun", R.drawable.star),
//        GameItem("Memory Match", "Logic & memory", R.drawable.star),
//        GameItem("Tile Tumble", "Logic puzzle game", R.drawable.star)
//    )
//}
//
//
//
//
