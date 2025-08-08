package com.google.codelab.gamingzone.data.repository

import com.google.codelab.gamingzone.data.local.dao.GameResultDao
import com.google.codelab.gamingzone.data.local.entity.GameResultEntity
import com.google.codelab.gamingzone.domain.repository.GameResultRepository
import javax.inject.Inject

class GameResultRepositoryImpl @Inject constructor(
    private val dao: GameResultDao
)  {

}

