package krawczyk.krystian.fallingdetector.data;

import android.provider.BaseColumns;

public class DetectorContract {

    public static final class DetectorEntry implements BaseColumns {
        public static final String TABLE_NAME = "detector";
        public static final String COLUMN_NUMBER = "number";
        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_SURNAME = "surname";
        public static final String COLUMN_MESSAGE = "message";
    }
}
