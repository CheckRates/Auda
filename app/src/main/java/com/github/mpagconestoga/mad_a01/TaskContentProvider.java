package com.github.mpagconestoga.mad_a01;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.github.mpagconestoga.mad_a01.dao.TaskDao;
import com.github.mpagconestoga.mad_a01.objects.Database;
import com.github.mpagconestoga.mad_a01.objects.Task;

//Class for Registered Content Provider. Implementation is
//in the settings page. Retrieves a list of tasks using this
//content provider
//Some of this source code was taken from the following repo:
//https://github.com/delaroy/PersistenceContentProviderSample
public class TaskContentProvider extends ContentProvider {

    /** The authority of this content provider. */
    public static final String AUTHORITY = "com.github.mpagconestoga.mad_a01";

    /** The match code for some items in the Menu table. */
    private static final int CODE_TASK_DIR = 1;

    /** The match code for an item in the Menu table. */
    private static final int CODE_TASK_ITEM = 2;

    /** The URI matcher. */
    private static final UriMatcher MATCHER = new UriMatcher(UriMatcher.NO_MATCH);

    /** The URI for the Menu table. */
    public static final Uri URI_TASK = Uri.parse(
            "content://" + AUTHORITY + "/Task");


    static {
        MATCHER.addURI(AUTHORITY, "Task", CODE_TASK_DIR);
        MATCHER.addURI(AUTHORITY, "Task" + "/*", CODE_TASK_ITEM);
    }

    @Override
    public boolean onCreate() {
        return true;
    }

    // FUNCTION   : query
    // DESCRIPTION: Returns a cursor containing tasks from the database
    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection,
                        @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        final int code = MATCHER.match(uri);
        if (code == CODE_TASK_DIR || code == CODE_TASK_ITEM) {
        final Context context = getContext();
        if (context == null) {
            return null;
        }
        //get the database's task Data Access Object
        TaskDao dao = Database.getInstance(context).taskDao();
        final Cursor cursor;

        //Check if getting all tasks or one task
        if (code == CODE_TASK_DIR) {
            cursor = dao.selectAllTasks();
        } else {
            cursor = dao.selectById(ContentUris.parseId(uri));
        }
        cursor.setNotificationUri(context.getContentResolver(), uri);
        return cursor;
        } else {
            throw new IllegalArgumentException("Unknown URI: " + uri);
        }
    }

    // FUNCTION   : query
    // DESCRIPTION: Returns a cursor containing tasks from the database
    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        switch (MATCHER.match(uri)) {
            case CODE_TASK_DIR:
                return "vnd.android.cursor.dir/" + AUTHORITY + ".task";
            case CODE_TASK_ITEM:
                return "vnd.android.cursor.item/" + AUTHORITY + ".task";
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
    }
    // FUNCTION   : insert
    // DESCRIPTION: Called by a client to determine the types of data streams that this
    // content provider supports for the given URI.
    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        switch (MATCHER.match(uri)) {
            case CODE_TASK_DIR:
                final Context context = getContext();
                if (context == null) {
                    return null;
                }
                final long id = Database.getInstance(context).taskDao()
                        .insert(Task.fromContentValues(values));
                context.getContentResolver().notifyChange(uri, null);
                return ContentUris.withAppendedId(uri, id);
            case CODE_TASK_ITEM:
                throw new IllegalArgumentException("Invalid URI, cannot insert with ID: " + uri);
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
    }
    // FUNCTION   : delete
    // DESCRIPTION: delete functionality not supported by this content provider.
    //              Content Provider used for getting list of task names
    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection,
                      @Nullable String[] selectionArgs) {
        return 0;
    }
    // FUNCTION   : update
    // DESCRIPTION: update functionality not supported by this content provider
    //              Content Provider used for getting list of task names
    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection,
                      @Nullable String[] selectionArgs) {
        return 0;
    }

}
