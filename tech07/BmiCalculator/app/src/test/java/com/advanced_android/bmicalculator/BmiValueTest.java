package com.advanced_android.bmicalculator;

import junit.framework.Assert;

import org.junit.Test;

/**
 * Created by shoma2da on 2015/12/23.
 */
public class BmiValueTest {

    @Test
    public void 생성시에전달한Float값을소수점2자리까지반올림해반환한다() {
        BmiValue bmiValue = new BmiValue(20.00511f);
        Assert.assertEquals(20.01f, bmiValue.toFloat());
    }

    @Test
    public void 생성시에전달한Float값을소수점2자리까지버림해반환한다() {
        BmiValue bmiValue = new BmiValue(20.00499f);
        Assert.assertEquals(20.00f, bmiValue.toFloat());
    }

    @Test
    public void 마른체형판정상한값() {
        BmiValue bmiValue = new BmiValue(18.499f);
        Assert.assertEquals(BmiValue.THIN, bmiValue.getMessage());
    }

    @Test
    public void 보통체형판정하한값() {
        BmiValue bmiValue = new BmiValue(18.500f);
        Assert.assertEquals(BmiValue.NORMAL, bmiValue.getMessage());
    }

    @Test
    public void 보통체형판정상한값() {
        BmiValue bmiValue = new BmiValue(24.999f);
        Assert.assertEquals(BmiValue.NORMAL, bmiValue.getMessage());
    }

    @Test
    public void 경도비만판정하한값() {
        BmiValue bmiValue = new BmiValue(25.000f);
        Assert.assertEquals(BmiValue.OBESITY, bmiValue.getMessage());
    }

    @Test
    public void 경도비만판정상한값() {
        BmiValue bmiValue = new BmiValue(29.999f);
        Assert.assertEquals(BmiValue.OBESITY, bmiValue.getMessage());
    }

    @Test
    public void 중도비만판정상한값() {
        BmiValue bmiValue = new BmiValue(30.000f);
        Assert.assertEquals(BmiValue.VERY_OBESITY, bmiValue.getMessage());
    }
}
