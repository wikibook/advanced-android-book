package com.advanced_android.bmicalculator;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.junit.Test;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by shoma2da on 2015/12/24.
 */
public class MainActivityTest {

    @Test
    public void initViews를호출하면버튼의Listener가설정된다() {
        //목오브젝트 준비
        EditText weightText = mock(EditText.class);
        EditText heightText = mock(EditText.class);
        TextView resultText = mock(TextView.class);
        Button resultButton = mock(Button.class);
        View.OnClickListener buttonListener = mock(View.OnClickListener.class);

        //목오브젝트를 반환하도록 설정
        MainActivity activity = spy(new MainActivity());
        when(activity.findViewById(R.id.text_weight)).thenReturn(weightText);
        when(activity.findViewById(R.id.text_height)).thenReturn(heightText);
        when(activity.findViewById(R.id.text_result)).thenReturn(resultText);
        when(activity.findViewById(R.id.button_calculate)).thenReturn(resultButton);
        when(activity.createButtonListener(weightText, heightText, resultText)).thenReturn(buttonListener);

        //테스트와 검증
        activity.initViews();
        verify(activity, times(1)).createButtonListener(weightText, heightText, resultText);
        verify(resultButton, times(1)).setOnClickListener(buttonListener);
    }

    @Test
    public void 버튼에설정하는Listener로각종처리를실행한다() {
        //목 준비
        TextView heightView = mock(TextView.class);
        TextView weightView = mock(TextView.class);
        TextView resultView = mock(TextView.class);
        BmiValue bmiValue = mock(BmiValue.class);

        MainActivity activity = spy(new MainActivity());
        doReturn(bmiValue).when(activity).calculateBmiValue(weightView, heightView);
        doNothing().when(activity).showCalcResult(resultView, bmiValue);
        doNothing().when(activity).startResultSaveService(bmiValue);
        doNothing().when(activity).prepareReceiveResultSaveServiceAction();

        View.OnClickListener buttonListener = activity.createButtonListener(weightView, heightView, resultView);
        buttonListener.onClick(mock(View.class));
        verify(activity, times(1)).calculateBmiValue(weightView, heightView);
        verify(activity, times(1)).showCalcResult(resultView, bmiValue);
        verify(activity, times(1)).startResultSaveService(bmiValue);
        verify(activity, times(1)).prepareReceiveResultSaveServiceAction();
    }

    @Test
    public void 버튼의리스너로각종처리가실행된다() {
        //목오브젝트 준비
        EditText weightText = mock(EditText.class);
        EditText heightText = mock(EditText.class);
        TextView resultText = mock(TextView.class);
        BmiValue result = mock(BmiValue.class);

        //목 오브젝트를 반환하도록 설정한다
        MainActivity activity = spy(new MainActivity());
        doReturn(result).when(activity).calculateBmiValue(weightText, heightText);
        doNothing().when(activity).showCalcResult(resultText, result);
        doNothing().when(activity).startResultSaveService(result);
        doNothing().when(activity).prepareReceiveResultSaveServiceAction();

        //테스트와 검증
        View.OnClickListener buttonListener = activity.createButtonListener(weightText, heightText, resultText);
        buttonListener.onClick(mock(View.class));
        verify(activity, times(1)).calculateBmiValue(weightText, heightText);
        verify(activity, times(1)).showCalcResult(resultText, result);
        verify(activity, times(1)).startResultSaveService(result);
        verify(activity, times(1)).prepareReceiveResultSaveServiceAction();
    }

    @Test
    public void Receiver로바르게값이전달되면버튼에대한변경이이루어진다() {
        Button button = mock(Button.class);
        Intent intent = mock(Intent.class);
        when(intent.hasExtra(SaveBmiService.PARAM_RESULT)).thenReturn(true);
        when(intent.getBooleanExtra(SaveBmiService.PARAM_RESULT, false)).thenReturn(true);

        MainActivity.BmiSaveResultReceiver receiver = new MainActivity.BmiSaveResultReceiver(button);
        receiver.onReceive(mock(Context.class), intent);

        verify(button, times(1)).setText((CharSequence) any());
        verify(button, times(1)).setEnabled(true);
    }

}
