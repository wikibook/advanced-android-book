package com.advanced_android.bmicalculator;

import android.support.annotation.VisibleForTesting;

/**
 * Created by shoma2da on 2015/12/23.
 */
public class BmiCalculator {

    /**
     * BMI값을 계산해서 반환한다
     * @param heightInMeter BMI 계산에 사용할 신장
     * @param weightInKg BMI 계산에 사용할 체중
     * @return BMI 값
     */
    public BmiValue calculate(float heightInMeter, float weightInKg) {
        if (heightInMeter < 0 || weightInKg < 0) {
            throw new RuntimeException("키와 몸무게는 양수로 지정해주세요");
        }
        float bmiValue = weightInKg / (heightInMeter * heightInMeter);
        return createValueObj(bmiValue);
    }

    @VisibleForTesting
    BmiValue createValueObj(float bmiValue) {
        return new BmiValue(bmiValue);
    }

}
