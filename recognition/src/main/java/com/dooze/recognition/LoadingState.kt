package com.dooze.recognition

sealed interface LoadingState

object Loading : LoadingState

object Done : LoadingState