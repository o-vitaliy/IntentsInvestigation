package com.nullgr.investigation

import android.content.Intent

/**
 * @author ovitaliy
 */
object IntentFactory {
    private val TAG = IntentFactory.javaClass::class.java.canonicalName
    private val KEY_1 = TAG + ".key_1"
    private val KEY_2 = TAG + ".key_2"
    private val KEY_3 = TAG + ".key_3"
    private val KEY_4 = TAG + ".key_4"
    private val KEY_5 = TAG + ".key_5"
    private val KEY_6 = TAG + ".key_6"
    private val KEY_7 = TAG + ".key_7"
    private val KEY_8 = TAG + ".key_8"
    private val KEY_9 = TAG + ".key_9"
    private val KEY_10 = TAG + ".key_10"

    fun createIntent() = Intent().apply {
        putExtra(KEY_1, false);
        putExtra(KEY_2, 2);
        putExtra(KEY_3, "3");
        putExtra(KEY_4, 4.0);
        putExtra(KEY_5, 4.0f);
        putExtra(KEY_6, KEY_6);
        putExtra(KEY_7, KEY_7);
        putExtra(KEY_8, KEY_8);
        putExtra(KEY_9, KEY_9);
        putExtra(KEY_10, KEY_10);
    }

    fun createIntents(count: Int): List<Intent> {
        val intents = arrayListOf<Intent>()
        for (i in 0..count) {
            intents.add(createIntent())
        }
        return intents
    }

}