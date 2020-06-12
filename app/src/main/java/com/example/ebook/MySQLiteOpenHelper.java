package com.example.ebook;

import android.content.ContentValues;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MySQLiteOpenHelper extends SQLiteOpenHelper {
    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + Books.BookEntry.TABLE_NAME + " (" +
                    Books.BookEntry._ID + " INTEGER PRIMARY KEY, " +
                    Books.BookEntry.COLUMN_NAME_NAME + " VARCHAR(200), " +
                    Books.BookEntry.COLUMN_NAME_PATH + " VARCHAR(100) " +
                     ")" ;
    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + Books.BookEntry.TABLE_NAME;

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "books.db";
    Context mContext;
    public MySQLiteOpenHelper(Context context) {
        super(context , DATABASE_NAME, null , DATABASE_VERSION);
        mContext = context;
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
//        db.execSQL(sql);
    }

//    private void initDb(SQLiteDatabase  sqLiteDatabase) {
//        Resources resources = mContext.getResources ();
//        String [] titles = resources .getStringArray(R.array.titles );
//        String [] authors = resources .getStringArray(R.array.authors);
//        String [] contents = resources .getStringArray(R.array.contents );
//        TypedArray images = resources .obtainTypedArray(R.array.images);
//        int length = 0;
//        length = Math.min( titles .length , authors.length );
//        length = Math.min(length , contents.length );
//        length = Math.min(length , images.length ());
//        for (int i = 0; i < length; i++) {
//            ContentValues values = new ContentValues();
//            values.put(NewsContract.NewsEntry.COLUMN_NAME_TITLE,
//                    titles [ i ]);
//            values.put(NewsContract.NewsEntry.COLUMN_NAME_AUTHOR,
//                    authors[ i ]);
//            values.put(NewsContract.NewsEntry.COLUMN_NAME_CONTENT,
//                    contents[ i ]);
//            values.put(NewsContract.NewsEntry.COLUMN_NAME_IMAGE,
//                    images. getString(i ));
//            long r = sqLiteDatabase. insert(
//                    NewsContract.NewsEntry.TABLE_NAME,
//                    null ,
//                    values );
//        }
//    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

//    public Cursor select() {
//        SQLiteDatabase db = this.getReadableDatabase();
//        Cursor cursor = db.query("books", null, null, null, null, null, null);
//        return cursor;
//    }

}
