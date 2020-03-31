/*
 *	FILE			: HideKeyBoardUtility.java
 *	PROJECT			: PROG3150 - Assignment-02
 *	PROGRAMMER		: Michael Gordon, Paul Smith, Duncan Snider, Gabriel Gurgel, Amy Dayasundara
 *	FIRST VERSION	: 2020 - 03 - 08
 *	DESCRIPTION		: This file contains the utility that is used to close the keyboard.
 */
package com.github.mpagconestoga.mad_a01.objects;

import android.app.Activity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
/*
 *  CLASS: HideKeyBoardUtility
 *  DESCRIPTION: This class is used to hold a static method that hides the keyboard.
 */
public class HideKeyBoardUtility {

    //credit: https://stackoverflow.com/questions/1109022/close-hide-android-soft-keyboard
    public static void hideKeyboard(View view) {
        InputMethodManager imm = (InputMethodManager) view.getContext().getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}
