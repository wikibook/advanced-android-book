package com.advanced_android.wordoftoday1;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;
import android.util.SparseArray;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;

import static com.advanced_android.wordoftoday1.WordsOfTodayContract.AUTHORITY;
import static com.advanced_android.wordoftoday1.WordsOfTodayContract.MIME_TYPE_DIR;
import static com.advanced_android.wordoftoday1.WordsOfTodayContract.MIME_TYPE_ITEM;
import static com.advanced_android.wordoftoday1.WordsOfTodayContract.TABLE_NAME;
import static com.advanced_android.wordoftoday1.WordsOfTodayContract.WordsOfTodayColumns;

/**
 * 온메모리로 동작하는 ContentProvider
 */
public class WordsOfTodayProvider extends ContentProvider {
    private static final UriMatcher sUriMatcher;

    private static final SparseArray<WordsOfToday> sList = new SparseArray<>();
    private static final int ROW_DIR = 1;
    private static final int ROW_ITEM = 2;

    private static int sLastId = 0;
    static {
        sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        sUriMatcher.addURI(AUTHORITY, TABLE_NAME, ROW_DIR);
        sUriMatcher.addURI(AUTHORITY, TABLE_NAME + "/#", ROW_ITEM);

        sList.append(sLastId, new WordsOfToday(sLastId, "Taiki", "날씨 참 좋다", 20151001)); sLastId++;
        sList.append(sLastId, new WordsOfToday(sLastId, "Osamu", "앱 버그 수정함", 20151001)); sLastId++;
        sList.append(sLastId, new WordsOfToday(sLastId, "Osamu", "오늘도 앱 버그 잡기", 20151002)); sLastId++;
        sList.append(sLastId, new WordsOfToday(sLastId, "Taiki", "열심히 운동함", 20151002)); sLastId++;
        sList.append(sLastId, new WordsOfToday(sLastId, "Ken", "머리 짧게 깎음", 20151002)); sLastId++;
        sList.append(sLastId, new WordsOfToday(sLastId, "Taiki", "오늘 점심 맛있네", 20151003)); sLastId++;
        sList.append(sLastId, new WordsOfToday(sLastId, "Taiki", "아침 4시 30분에 일어났다", 20151004));
    }


    public WordsOfTodayProvider() {
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        int count = 0;
        switch (sUriMatcher.match(uri)) {
            case ROW_ITEM:
                int id =  (int) ContentUris.parseId(uri);
                synchronized (sList) {
                    if (sList.indexOfKey(id) >= 0) {
                        sList.remove(id);
                        // 변경통지 
                        getContext().getContentResolver().notifyChange(uri, null);
                        count = 1;
                    }
                }
                break;
            default:
                throw new IllegalArgumentException("인ㅅ의 URI가 틀렸습니다.");
        }
        return count;
    }

    @Override
    public String getType(Uri uri) {
        switch (sUriMatcher.match(uri)) {
            case ROW_DIR:
                return MIME_TYPE_DIR;
            case ROW_ITEM:
                return MIME_TYPE_ITEM;
            default:
                return null;
        }
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        Uri resultUri = null;
        switch (sUriMatcher.match(uri)) {
            case ROW_DIR:
                WordsOfToday obj = new WordsOfToday();
                if (values.containsKey(WordsOfTodayColumns.NAME)) {
                    obj.name = values.getAsString(WordsOfTodayColumns.NAME);
                }
                if (values.containsKey(WordsOfTodayColumns.WORDS)) {
                    obj.words = values.getAsString(WordsOfTodayColumns.WORDS);
                }
                if (values.containsKey(WordsOfTodayColumns.DATE)) {
                    obj.date = values.getAsInteger(WordsOfTodayColumns.DATE);
                }
                synchronized (sList) {
                    sLastId++;
                    obj._id = sLastId;
                    sList.append(sLastId, obj);
                    resultUri = ContentUris.withAppendedId(uri, sLastId);
                    Log.d("WordsOfToday", "WordsOfTodayProvider insert " + obj);
                    // 변경통지 
                    getContext().getContentResolver().notifyChange(resultUri, null);
                }
                break;
            default:
                throw new IllegalArgumentException("引数のURIが間違っています");
        }
        return resultUri;
    }

    @Override
    public boolean onCreate() {
        Log.d("WordsOfToday", "WordsOfTodayProvider onCreate");
        // SQLite 데이터베이스를 이용하는 경우는 SQLiteOpenHelper를 여기에 작성 
        // 이번에는 온메모리이므로 특별히 아무엇도 하지 않는다. 
        // 문제없이 끝았으므로 단순히 true를 반환한다.
        return true;
    }

    /**
     *
     * @param uri
     * @param projection
     * @param selection
     * @param selectionArgs
     * @param sortOrder
     * @return
     */
    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        Log.d("WordsOfToday", "query uri=" + uri.toString());
        // _ID는 항상 반환한다
        HashSet<String> set = new HashSet<String>(Arrays.asList(projection));
        //set.add(WordsOfTodayColumns._ID);
        Log.d("WordsOfToday", "query projection=" + set.toString());
        String[] columns = set.toArray(new String[set.size()]);
        MatrixCursor cursor = new MatrixCursor(columns);
        switch (sUriMatcher.match(uri)) {
            case ROW_DIR:
                synchronized (sList) {
                    String name = null;
                    if (selection != null) {
                        // 구현을 간단히 하고자 name 칼럼만 대응
                        // name=Taiki와 같은 형태 
                        Log.d(MainActivity.TAG, "query where " + selection );
                        name = selection.split("=")[1];
                    }
                    int size = sList.size();
                    for (int i = 0; i < size; i++) {
                        WordsOfToday obj = sList.valueAt(i);
                        // where구가 지정된 경우는 체크 
                        if (name == null || TextUtils.equals(obj.name, name)) {
                            Object[] row = getRow(obj._id, columns);
                            cursor.addRow(row);
                        }
                    }
                }
                return cursor;
            case ROW_ITEM:
                synchronized (sList) {
                    long id =  ContentUris.parseId(uri);
                    Object[] row = getRow(id, columns);
                    cursor.addRow(row);
                }
                break;
            default:
                ;
        }
        return cursor;
    }

    /**
     * projection으로 지정된 칼럼순으로 데이터를 배열에 넣는다 
     * @param id TodayOfWordのID
     * @param columns 가져온 칼럼  
     * @return
     */
    private Object[] getRow(long id, String[] columns) {
        ArrayList<Object> values = new ArrayList<Object>();
        WordsOfToday row = sList.get((int)id);
        for(String column : columns) {
            if (column.equals(WordsOfTodayColumns._ID)) {
                Log.d("WordsOfToday", "getRow _id=" + row._id);
                values.add(row._id);
                continue;
            }
            if(column.equals(WordsOfTodayColumns.NAME)) {
                Log.d("WordsOfToday", "getRow name=" + row.name);
                values.add(row.name);
                continue;
            }
            if(column.equals(WordsOfTodayColumns.WORDS)) {
                Log.d("WordsOfToday", "getRow words=" + row.words);
                values.add(row.words);
                continue;
            }
            if(column.equals(WordsOfTodayColumns.DATE)) {
                Log.d("WordsOfToday", "getRow date=" + row.date);
                values.add(row.date);
                continue;
            }
        }
        return values.toArray(new Object[values.size()]);
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        int count = 0;
        switch (sUriMatcher.match(uri)) {
            case ROW_ITEM:
                int id = (int) ContentUris.parseId(uri);
                synchronized (sList) {
                    WordsOfToday row = sList.get(id);
                    if (row != null) {
                        if (values.containsKey(WordsOfTodayColumns.NAME)) {
                            row.name = values.getAsString(WordsOfTodayColumns.NAME);
                        }
                        if (values.containsKey(WordsOfTodayColumns.WORDS)) {
                            row.words = values.getAsString(WordsOfTodayColumns.WORDS);
                        }
                        if (values.containsKey(WordsOfTodayColumns.NAME)) {
                            row.date = values.getAsInteger(WordsOfTodayColumns.DATE);
                        }
                        // 변경 통지 
                        getContext().getContentResolver().notifyChange(uri, null);
                        count = 1;
                    }
                }
                break;
            default:
                throw new IllegalArgumentException("인수의 URI가 틀렸습니다");
        }
        return count;
    }
}
