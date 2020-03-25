/*
 *	FILE			: WeightFilter.java
 *	PROJECT			: PROG3150 - Assignment-01
 *	PROGRAMMER		: Michael Gordon, Paul Smith, Duncan Snider, Gabriel Gurgel, Amy Dayasundara
 *	FIRST VERSION	: 2020 - 02 - 05
 *	DESCRIPTION		: Limits the associated edittext to a range of values. Done so to ensure weight
 *                    value does not exceed a desired amount.
 *                    Adapted from following example:
 *                    // https://stackoverflow.com/questions/14212518/is-there-a-way-to-define-a-min-and-max-value-for-edittext-in-android
 */
package com.github.mpagconestoga.mad_a01.objects;

import android.text.Spanned;
import android.text.InputFilter;

// class structure found at:
// https://stackoverflow.com/questions/14212518/is-there-a-way-to-define-a-min-and-max-value-for-edittext-in-android

public class WeightFilter implements InputFilter{

    private int min, max;

    public WeightFilter(int min, int max) {
        this.min = min;
        this.max = max;
    }

    public WeightFilter(String min, String max) {
        this.min = Integer.parseInt(min);
        this.max = Integer.parseInt(max);
    }


    /*
     *    METHOD      :   CharSequence
     *
     *    DESCRIPTION :   Filter for ensuring the input used is valid
     *
     *    PARAMETERS  :   CharSequence source       : Data to be checked
     *                    int start                 : Starting point of the range
     *                    int end                   : Ending point of the range
     *                    int dstart                : Starting point of destination
     *                    int dend                  : ending point of destination
     *
     *    RETURNS     :   null                      : if number is within range
     *                    ""                        : empty string if number is out of range
     * */
    @Override
    public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
        try {
            int input = Integer.parseInt(dest.toString() + source.toString());
            if (isInRange(min, max, input))
                return null;
        } catch (NumberFormatException nfe) { }
        return "";
    }

    /*
     *    METHOD      :   isInRange
     *
     *    DESCRIPTION :   boolean for checking range of value
     *
     *    PARAMETERS  :   int a                     : minimum value of range
     *                    int b                     : maximum value of range
     *                    int c                     : input of user
     *
     *    RETURNS     :   true                      : if number is within range
     *                    false                     : if number is out of range
     * */
    private boolean isInRange(int a, int b, int c) {
        return b > a ? c >= a && c <= b : c >= b && c <= a;
    }

}
