package com.dooze.recognition.model

import com.google.mlkit.vision.text.Text

fun Text.toStringText() = textBlocks.joinToString("\n") { textBlock ->
    textBlock.lines.joinToString("\n") { it.text }
}