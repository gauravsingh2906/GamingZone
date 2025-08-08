package com.google.codelab.gamingzone.presentation.games.aqua_flow

enum class TileType {
    EMPTY,
    SOURCE,
    PIPE,
    TANK,
    MIXER,
    FILTER,
    LEAK
}


enum class WaterColor {
    NONE,
    BLUE,
    RED,
    GREEN,
    YELLOW,
    PURPLE,
    ORANGE
}

enum class PipeDirection {
    NONE, // not a pipe
    HORIZONTAL, // connects left ↔ right
    VERTICAL,   // connects up ↕ down
    CORNER_TL,  // top-left
    CORNER_TR,
    CORNER_BL,
    CORNER_BR,
    CROSS       // all directions (optional for later)
}





