package krawczyk.krystian.fallingdetector.data;

import android.net.Uri;
import android.provider.BaseColumns;

public class DetectorContract {

    public static final String CONTENT_AUTHORITY = "krawczyk.krystian.fallingdetector";
    public static final String PATH_DETECTOR = "detector";

    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final class DetectorEntry implements BaseColumns {

        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon()
                .appendPath(PATH_DETECTOR)
                .build();

        public static final String TABLE_NAME = "detector";
        public static final String COLUMN_NUMBER = "number";
        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_SURNAME = "surname";
        public static final String COLUMN_SELECTED = "selected";
    }
}
