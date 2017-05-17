package com.advanced_android.bmicalculator;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;

import org.junit.Test;
import org.mockito.Matchers;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyBoolean;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by shoma2da on 2015/12/24.
 */
public class SaveBmiServiceTest {

    @Test
    public void static인start메소드를호출하면startService된다() {
        Context context = mock(Context.class);
        SaveBmiService.start(context, mock(BmiValue.class));
        verify(context, times(1)).startService((Intent) any());
    }

    @Test
    public void onHandleIntentにnull을전달하면아무것도하지않는다() {
        SaveBmiService service = spy(new SaveBmiService());
        service.onHandleIntent(null);
        verify(service, never()).sendLocalBroadcast(anyBoolean());
        verify(service, never()).saveToRemoteServer((BmiValue) any());
    }

    @Test
    public void onHandleIntent에파라미터없는Intent를전달하면아무것도하지않는다() {
        SaveBmiService service = spy(new SaveBmiService());
        service.onHandleIntent(mock(Intent.class));
        verify(service, never()).sendLocalBroadcast(anyBoolean());
        verify(service, never()).saveToRemoteServer((BmiValue)any());
    }

    @Test
    public void onHandleIntent에BmiValue형이외의데이터가들어간Intent를전달하면아무것도하지않는다() {
        Intent intent = mock(Intent.class);
        when(intent.getSerializableExtra(SaveBmiService.PARAM_KEY_BMI_VALUE)).thenReturn("hoge");

        SaveBmiService service = spy(new SaveBmiService());
        service.onHandleIntent(intent);
        verify(service, never()).sendLocalBroadcast(anyBoolean());
        verify(service, never()).saveToRemoteServer((BmiValue)any());
    }

    @Test
    public void onHandleIntent에바르게데이터가들어간Intent를전달하면데이터저장과Broadcast가이루어진다() {
        //준비：SaveBmiService에 전달할 Intent를 준비
        BmiValue bmiValue = mock(BmiValue.class);
        Intent intent = mock(Intent.class);
        when(intent.getSerializableExtra(SaveBmiService.PARAM_KEY_BMI_VALUE)).thenReturn(bmiValue);

        //준비：SaveBmiService의 각 메소드는 아무것도 하지 않도록 한다
        SaveBmiService service = spy(new SaveBmiService());
        doReturn(false).when(service).saveToRemoteServer((BmiValue)any());
        doNothing().when(service).sendLocalBroadcast(anyBoolean());

        //테스트와 메소드 호출 확인
        service.onHandleIntent(intent);
        verify(service, times(1)).sendLocalBroadcast(anyBoolean());
        verify(service, times(1)).saveToRemoteServer((BmiValue)any());
    }

    @Test
    public void Broadcast를날린다() {
        // 테스트를 위해 LocalBroadcastManager를 교체
        LocalBroadcastManager manager = mock(LocalBroadcastManager.class);
        SaveBmiService service = new SaveBmiService();
        service.setLocalBroadcastManager(manager);

        // Broadcast 송신
        service.sendLocalBroadcast(true);

        // LocalBroadcast로 Broadcast가 실행되고 있는지 확인
        verify(manager, times(1)).sendBroadcast((Intent)any());
    }
}
