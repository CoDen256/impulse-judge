package io.github.coden256.wpl.judge.bot.telegram.bot

import io.github.coden256.telegram.keyboard.Keyboard
import io.github.coden256.telegram.keyboard.KeyboardButton
import io.github.coden256.telegram.keyboard.keyboard

fun withButtons(): Keyboard {
    return keyboard {
        row { b(BUTTON); b(BUTTON) }
    }
}

val BUTTON = KeyboardButton("↩BUTTON", "BUTTON")