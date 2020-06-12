package com.example.ebook;

import android.provider.BaseColumns;

 class Books {
    private Books() {}
    public static class BookEntry implements BaseColumns {
        public static final String TABLE_NAME = "books";
        public static final String COLUMN_NAME_NAME = "name";
        public static final String COLUMN_NAME_PATH = "path";
//        public static final String COLUMN_NAME_CONTENT = "content";
//        public static final String COLUMN_NAME_IMAGE = "image";
    }
}
