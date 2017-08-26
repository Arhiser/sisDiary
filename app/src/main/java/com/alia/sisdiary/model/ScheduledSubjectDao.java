package com.alia.sisdiary.model;

import java.util.List;
import java.util.ArrayList;
import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.Property;
import org.greenrobot.greendao.internal.SqlUtils;
import org.greenrobot.greendao.internal.DaoConfig;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.database.DatabaseStatement;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "SCHEDULED_SUBJECTS".
*/
public class ScheduledSubjectDao extends AbstractDao<ScheduledSubject, Long> {

    public static final String TABLENAME = "SCHEDULED_SUBJECTS";

    /**
     * Properties of entity ScheduledSubject.<br/>
     * Can be used for QueryBuilder and for referencing column names.
     */
    public static class Properties {
        public final static Property Id = new Property(0, Long.class, "id", true, "_id");
        public final static Property SubjectId = new Property(1, Long.class, "subjectId", false, "SUBJECT_ID");
        public final static Property Weekday = new Property(2, Integer.class, "weekday", false, "WEEKDAY");
        public final static Property LessonNumber = new Property(3, Integer.class, "lessonNumber", false, "LESSON_NUMBER");
    }

    private DaoSession daoSession;


    public ScheduledSubjectDao(DaoConfig config) {
        super(config);
    }
    
    public ScheduledSubjectDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
        this.daoSession = daoSession;
    }

    /** Creates the underlying database table. */
    public static void createTable(Database db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"SCHEDULED_SUBJECTS\" (" + //
                "\"_id\" INTEGER PRIMARY KEY ," + // 0: id
                "\"SUBJECT_ID\" INTEGER NOT NULL ," + // 1: subjectId
                "\"WEEKDAY\" INTEGER NOT NULL ," + // 2: weekday
                "\"LESSON_NUMBER\" INTEGER NOT NULL );"); // 3: lessonNumber
        // Add Indexes
        db.execSQL("CREATE UNIQUE INDEX " + constraint + "IDX_SCHEDULED_SUBJECTS_SUBJECT_ID_WEEKDAY_LESSON_NUMBER ON \"SCHEDULED_SUBJECTS\"" +
                " (\"SUBJECT_ID\" ASC,\"WEEKDAY\" ASC,\"LESSON_NUMBER\" ASC);");
    }

    /** Drops the underlying database table. */
    public static void dropTable(Database db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"SCHEDULED_SUBJECTS\"";
        db.execSQL(sql);
    }

    @Override
    protected final void bindValues(DatabaseStatement stmt, ScheduledSubject entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
        stmt.bindLong(2, entity.getSubjectId());
        stmt.bindLong(3, entity.getWeekday());
        stmt.bindLong(4, entity.getLessonNumber());
    }

    @Override
    protected final void bindValues(SQLiteStatement stmt, ScheduledSubject entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
        stmt.bindLong(2, entity.getSubjectId());
        stmt.bindLong(3, entity.getWeekday());
        stmt.bindLong(4, entity.getLessonNumber());
    }

    @Override
    protected final void attachEntity(ScheduledSubject entity) {
        super.attachEntity(entity);
        entity.__setDaoSession(daoSession);
    }

    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    @Override
    public ScheduledSubject readEntity(Cursor cursor, int offset) {
        ScheduledSubject entity = new ScheduledSubject( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // id
            cursor.getLong(offset + 1), // subjectId
            cursor.getInt(offset + 2), // weekday
            cursor.getInt(offset + 3) // lessonNumber
        );
        return entity;
    }
     
    @Override
    public void readEntity(Cursor cursor, ScheduledSubject entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setSubjectId(cursor.getLong(offset + 1));
        entity.setWeekday(cursor.getInt(offset + 2));
        entity.setLessonNumber(cursor.getInt(offset + 3));
     }
    
    @Override
    protected final Long updateKeyAfterInsert(ScheduledSubject entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    @Override
    public Long getKey(ScheduledSubject entity) {
        if(entity != null) {
            return entity.getId();
        } else {
            return null;
        }
    }

    @Override
    public boolean hasKey(ScheduledSubject entity) {
        return entity.getId() != null;
    }

    @Override
    protected final boolean isEntityUpdateable() {
        return true;
    }
    
    private String selectDeep;

    protected String getSelectDeep() {
        if (selectDeep == null) {
            StringBuilder builder = new StringBuilder("SELECT ");
            SqlUtils.appendColumns(builder, "T", getAllColumns());
            builder.append(',');
            SqlUtils.appendColumns(builder, "T0", daoSession.getSubjectDao().getAllColumns());
            builder.append(" FROM SCHEDULED_SUBJECTS T");
            builder.append(" LEFT JOIN SUBJECTS T0 ON T.\"SUBJECT_ID\"=T0.\"_id\"");
            builder.append(' ');
            selectDeep = builder.toString();
        }
        return selectDeep;
    }
    
    protected ScheduledSubject loadCurrentDeep(Cursor cursor, boolean lock) {
        ScheduledSubject entity = loadCurrent(cursor, 0, lock);
        int offset = getAllColumns().length;

        Subject subject = loadCurrentOther(daoSession.getSubjectDao(), cursor, offset);
         if(subject != null) {
            entity.setSubject(subject);
        }

        return entity;    
    }

    public ScheduledSubject loadDeep(Long key) {
        assertSinglePk();
        if (key == null) {
            return null;
        }

        StringBuilder builder = new StringBuilder(getSelectDeep());
        builder.append("WHERE ");
        SqlUtils.appendColumnsEqValue(builder, "T", getPkColumns());
        String sql = builder.toString();
        
        String[] keyArray = new String[] { key.toString() };
        Cursor cursor = db.rawQuery(sql, keyArray);
        
        try {
            boolean available = cursor.moveToFirst();
            if (!available) {
                return null;
            } else if (!cursor.isLast()) {
                throw new IllegalStateException("Expected unique result, but count was " + cursor.getCount());
            }
            return loadCurrentDeep(cursor, true);
        } finally {
            cursor.close();
        }
    }
    
    /** Reads all available rows from the given cursor and returns a list of new ImageTO objects. */
    public List<ScheduledSubject> loadAllDeepFromCursor(Cursor cursor) {
        int count = cursor.getCount();
        List<ScheduledSubject> list = new ArrayList<ScheduledSubject>(count);
        
        if (cursor.moveToFirst()) {
            if (identityScope != null) {
                identityScope.lock();
                identityScope.reserveRoom(count);
            }
            try {
                do {
                    list.add(loadCurrentDeep(cursor, false));
                } while (cursor.moveToNext());
            } finally {
                if (identityScope != null) {
                    identityScope.unlock();
                }
            }
        }
        return list;
    }
    
    protected List<ScheduledSubject> loadDeepAllAndCloseCursor(Cursor cursor) {
        try {
            return loadAllDeepFromCursor(cursor);
        } finally {
            cursor.close();
        }
    }
    

    /** A raw-style query where you can pass any WHERE clause and arguments. */
    public List<ScheduledSubject> queryDeep(String where, String... selectionArg) {
        Cursor cursor = db.rawQuery(getSelectDeep() + where, selectionArg);
        return loadDeepAllAndCloseCursor(cursor);
    }
 
}
