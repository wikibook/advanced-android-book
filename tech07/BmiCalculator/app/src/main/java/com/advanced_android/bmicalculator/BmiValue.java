package com.advanced_android.bmicalculator;

import android.support.annotation.VisibleForTesting;

import java.io.Serializable;

/**
 * Created by shoma2da on 2015/12/23.
 */
public class BmiValue implements Serializable {

    private static final long serialVersionUID = -4325336659053219895L;

    @VisibleForTesting
    static final String THIN = "마른";
    @VisibleForTesting
    static final String NORMAL = "보통";
    @VisibleForTesting
    static final String OBESITY = "비만（１도）";
    @VisibleForTesting
    static final String VERY_OBESITY = "비만（２도）";

    private final float mValue;

    public BmiValue(float value) {
        mValue = value;
    }

    /**
     * 소수점 아래 2자리까지의 부동소수점값
     * @return
     */
    public float toFloat() {
        int rounded = Math.round(mValue * 100);
        return  rounded / 100f;
    }

    /**
     * BMI에 따른 판정 메시지 반환
     */
    public String getMessage() {
        if (mValue < 18.5f) {
            return THIN;
        } else if (18.5 <= mValue && mValue < 25) {
            return NORMAL;
        } else if (25 <= mValue && mValue < 30) {
            return OBESITY;
        } else {
            return VERY_OBESITY;
        }
    }

}
