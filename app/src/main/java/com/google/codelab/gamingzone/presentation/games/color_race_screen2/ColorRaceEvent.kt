package com.google.codelab.gamingzone.presentation.games.color_race_screen2


sealed class ColorRaceEvent {

    object StartGame : ColorRaceEvent()
    data class ColorClicked(val color: ColorOption) : ColorRaceEvent()
    object DismissGameOverDialog : ColorRaceEvent()

}
