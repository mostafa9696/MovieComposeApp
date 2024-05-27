package com.example.moviecomposeapp.common.helpers

/**
 * Sealed interface representing different UI events related to dialogs.
 */
sealed interface DialogUiEvent {
    object Loading: DialogUiEvent
    object Active: DialogUiEvent
    object InActive: DialogUiEvent
}