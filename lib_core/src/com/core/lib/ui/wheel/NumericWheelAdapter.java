/*
 *  Copyright 2010 Yuri Kanivets
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package com.core.lib.ui.wheel;


/**
 * Numeric Wheel adapter.
 */
public class NumericWheelAdapter implements WheelAdapter {

    /**
     * The default min value
     */
    public static final int DEFAULT_MAX_VALUE = 9;

    /**
     * The default max value
     */
    private static final int DEFAULT_MIN_VALUE = 0;

    // Values
    private int minValue;
    private int maxValue;

    // format
    private String format;
    //倍数 (不是时间)
    private int times = 0;

    //double倍数(例如：0.5倍显示)
    private double timesDouble = 0.0;


    /**
     * Default constructor
     */
    public NumericWheelAdapter() {
        this(DEFAULT_MIN_VALUE, DEFAULT_MAX_VALUE);
    }

    /**
     * Constructor
     *
     * @param minValue the wheel min value
     * @param maxValue the wheel max value
     */
    public NumericWheelAdapter(int minValue, int maxValue) {
        this(minValue, maxValue, null);
    }

    /**
     * Constructor
     *
     * @param minValue the wheel min value
     * @param maxValue the wheel max value
     * @param format   the format string
     */
    public NumericWheelAdapter(int minValue, int maxValue, String format) {
        this.minValue = minValue;
        this.maxValue = maxValue;
        this.format = format;
    }

    public NumericWheelAdapter(int minValue, int maxValue, int times) {
        this.minValue = minValue;
        this.maxValue = maxValue;
        this.times = times;
    }

    public NumericWheelAdapter(int minValue, int maxValue, double timesDouble) {
        this.minValue = minValue;
        this.maxValue = maxValue;
        this.timesDouble = timesDouble;
    }

    @Override
    public String getItem(int index) {
        if (index >= 0 && index < getItemsCount()) {
//            int value = minValue + index;
//            return format != null ? String.format(format, value) : Integer.toString(value);
            int value = 0;
            double valueDouble = 0.0;
            if(times == 0 && timesDouble == 0.0){
                value = minValue + index;
            }else if(timesDouble == 0.0){
                value = minValue + index * times;
            }else if(times == 0){
                valueDouble = minValue + index * timesDouble;
            }
            if(valueDouble == 0.0){
                return format != null ? String.format(format, value) : Integer.toString(value);
            }else if(value == 0){
                return format != null ? String.valueOf(valueDouble) : Double.toString(valueDouble);
            }
        }
        return null;
    }

    @Override
    public int getItemsCount() {
        return maxValue - minValue + 1;
    }

    @Override
    public int getMaximumLength() {
        int max = Math.max(Math.abs(maxValue), Math.abs(minValue));
        int maxLen = Integer.toString(max).length();
        if (minValue < 0) {
            maxLen++;
        }
        return maxLen;
    }
}
