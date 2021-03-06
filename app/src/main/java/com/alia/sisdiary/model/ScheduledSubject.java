package com.alia.sisdiary.model;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Index;
import org.greenrobot.greendao.annotation.NotNull;
import org.greenrobot.greendao.annotation.ToOne;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.DaoException;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Created by Alyona on 22.08.2017.
 */
@Entity(active = true,
        nameInDb = "SCHEDULED_SUBJECTS",
        indexes = {
        @Index(value = "subjectId, weekday, lessonTime", unique = true)
        }
)
public class ScheduledSubject {
    @Id
    private Long id;
    @NotNull
    private Long subjectId;

    @ToOne(joinProperty = "subjectId")
    private Subject subject;
    @NotNull
    private Integer weekday;

    @NotNull
    private Date lessonTime;
@NotNull
    private String homework;



    /** Used to resolve relations */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;
    /** Used for active entity operations. */
    @Generated(hash = 1679835600)
    private transient ScheduledSubjectDao myDao;
    @Generated(hash = 711858396)
    private transient Long subject__resolvedKey;

    public ScheduledSubject(Subject subject){
        this.subject = subject;
        this.subjectId = subject.getId();
        this.homework = "";
    }

    @Generated(hash = 2108389844)
    public ScheduledSubject(Long id, @NotNull Long subjectId, @NotNull Integer weekday,
            @NotNull Date lessonTime, @NotNull String homework) {
        this.id = id;
        this.subjectId = subjectId;
        this.weekday = weekday;
        this.lessonTime = lessonTime;
        this.homework = homework;
    }

    @Generated(hash = 1688737304)
    public ScheduledSubject() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getWeekday() {
        return weekday;
    }

    public void setWeekday(Integer weekday) {
        this.weekday = weekday;
    }

    public Date getLessonTime() {
        return lessonTime;
    }

    public void setLessonTime(Date lessonTime) {
        this.lessonTime = lessonTime;
    }
    public void setLessonIntTime(int hours, int minutes){
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, hours);
        calendar.set(Calendar.MINUTE, minutes);
        calendar.set(Calendar.YEAR, 2017);
        calendar.set(Calendar.MONTH, 7);
        calendar.set(Calendar.DAY_OF_MONTH, 27);
        lessonTime = calendar.getTime();
    }

    public String getHomework() {
        return homework;
    }

    public void setHomework(String homework) {
        this.homework = homework;
    }

    public Long getSubjectId() {
        return this.subjectId;
    }

    public void setSubjectId(Long subjectId) {
        this.subjectId = subjectId;
    }

    /** To-one relationship, resolved on first access. */
    @Generated(hash = 86684749)
    public Subject getSubject() {
        Long __key = this.subjectId;
        if (subject__resolvedKey == null || !subject__resolvedKey.equals(__key)) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            SubjectDao targetDao = daoSession.getSubjectDao();
            Subject subjectNew = targetDao.load(__key);
            synchronized (this) {
                subject = subjectNew;
                subject__resolvedKey = __key;
            }
        }
        return subject;
    }

    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 476367223)
    public void setSubject(@NotNull Subject subject) {
        if (subject == null) {
            throw new DaoException(
                    "To-one property 'subjectId' has not-null constraint; cannot set to-one to null");
        }
        synchronized (this) {
            this.subject = subject;
            subjectId = subject.getId();
            subject__resolvedKey = subjectId;
        }
    }

    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#delete(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 128553479)
    public void delete() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.delete(this);
    }

    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#refresh(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 1942392019)
    public void refresh() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.refresh(this);
    }

    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#update(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 713229351)
    public void update() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.update(this);
    }

    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 71167549)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getScheduledSubjectDao() : null;
    }
}
