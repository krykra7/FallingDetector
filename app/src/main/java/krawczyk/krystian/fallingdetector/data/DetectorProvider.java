package krawczyk.krystian.fallingdetector.data;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

public class DetectorProvider extends ContentProvider {

    private static final int CODE_CONTACT_LIST = 100;
    private static final int CODE_SINGLE_CONTACT = 101;
    private static final UriMatcher sUriMatcher = builUriMatcher();
    private DetectorDbHelper mDbHelper;

    private static UriMatcher builUriMatcher() {
        UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        String authority = DetectorContract.CONTENT_AUTHORITY;

        uriMatcher.addURI(authority, DetectorContract.PATH_DETECTOR, CODE_CONTACT_LIST);
        uriMatcher.addURI(authority, DetectorContract.PATH_DETECTOR + "/#", CODE_SINGLE_CONTACT);

        return uriMatcher;
    }

    @Override
    public boolean onCreate() {
        mDbHelper = new DetectorDbHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable
            String[] selectionArgs, @Nullable String sortOrder) {
        return null;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        return null;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable
            String[] selectionArgs) {
        return 0;
    }

}
