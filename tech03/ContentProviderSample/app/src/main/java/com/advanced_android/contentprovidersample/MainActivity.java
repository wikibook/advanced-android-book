package com.advanced_android.contentprovidersample;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.format.DateFormat;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;

import static android.provider.MediaStore.Images.ImageColumns;
import static android.provider.MediaStore.Images.Media;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        try {
            Cursor cursor = getImage();
            if (cursor.moveToFirst()) {
                // 1. 각 칼럼의 열 인덱스 취득
                int idColNum = cursor.getColumnIndexOrThrow(ImageColumns._ID);
                int titleColNum = cursor.getColumnIndexOrThrow(ImageColumns.TITLE);
                int dateTakenColNum = cursor.getColumnIndexOrThrow(ImageColumns.
                        DATE_TAKEN);
                // 2. 인덱스를 바탕으로 데이터를 Cursor로부터 취득
                long id = cursor.getLong(idColNum);
                String title = cursor.getString(titleColNum);
                long dateTaken = cursor.getLong(dateTakenColNum);
                Uri imageUri = ContentUris.withAppendedId(Media.EXTERNAL_CONTENT_URI, id);
                // 3. 데이터를 View로 설정
                TextView textView = (TextView) findViewById(R.id.textView);
                ImageView imageView = (ImageView) findViewById(R.id.imageView);
                Calendar calendar = Calendar.getInstance();
                calendar.setTimeInMillis(dateTaken);
                String text = DateFormat.format("yyyy/MM/dd(E) kk:mm:ss", calendar).
                        toString();
                textView.setText("촬영일시: " + text);
                imageView.setImageURI(imageUri);
            }
            cursor.close();
        } catch (SecurityException e) {
            Toast.makeText(this, "스토리지에 접근 권한을 허가로 해주세요.（종료합니다)", Toast.LENGTH_LONG).show();
            finish();
        }
    }

    private Cursor getImage() {
        ContentResolver contentResolver = getContentResolver();
        Uri queryUri = Media.EXTERNAL_CONTENT_URI;
        // 가져올 칼럼명
        String[] projection = {
                ImageColumns._ID,
                ImageColumns.TITLE,
                ImageColumns.DATE_TAKEN,
        };
        // 정렬
        String sortOrder = ImageColumns.DATE_TAKEN + " DESC";
        // 1건만 가져옴
        queryUri = queryUri.buildUpon().appendQueryParameter("limit", "1").

                build();
        // selection、selectionArgs는 지정하지 않는다
        return contentResolver.query(queryUri, projection, null, null,
                sortOrder);
    }
}
