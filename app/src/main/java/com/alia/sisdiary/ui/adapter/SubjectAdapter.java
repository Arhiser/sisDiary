package com.alia.sisdiary.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.drm.DrmStore;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ActionMode;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alia.sisdiary.R;
import com.alia.sisdiary.model.ScheduledSubject;
import com.alia.sisdiary.model.Subject;
import com.alia.sisdiary.ui.activity.HomeWorkActivity;
import com.alia.sisdiary.ui.fragment.AddHomeWorkDialog;
import com.alia.sisdiary.ui.fragment.AddSubjectDialog;
import com.alia.sisdiary.ui.fragment.DayListTimetableFragment;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Alyona on 17.07.2017.
 */

public class SubjectAdapter extends RecyclerView.Adapter<SubjectAdapter.SubjectHolder> {
    private Context mContext;
    private ActionModeMenuClickListener mClickListener;
    private List<ScheduledSubject> mSubjects;

    private boolean multiSelect = false;
    private boolean isAddHwButton = false;
    private ArrayList<ScheduledSubject> selectedItems = new ArrayList<>();
    private ActionMode mActionMode;

    public interface ActionModeMenuClickListener {
        void onDeleteClick(ScheduledSubject subjectItem);

        void onAddHomeworkClick(ScheduledSubject subjectItem);

        void onEditLessonClick(ScheduledSubject subjectItem);

    }

    private ActionMode.Callback actionModeCallbacks = new ActionMode.Callback() {
        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            multiSelect = true;
            MenuInflater inflater = mode.getMenuInflater();
            inflater.inflate(R.menu.contextual_action_bar_menu, menu);
            return true;
        }

        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            menu.findItem(R.id.add_home_work).setVisible(isAddHwButton);
            menu.findItem(R.id.edit_lesson).setVisible(isAddHwButton);
            return true;
        }

        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            ScheduledSubject scheduledSubjectItem = selectedItems.get(0);
            switch (item.getItemId()) {
                case R.id.delete_subject:
                    for (ScheduledSubject lesson : selectedItems) {
                        mClickListener.onDeleteClick(lesson);
                    }
                    mode.finish();
                    return true;
                case R.id.add_home_work:
                    mClickListener.onAddHomeworkClick(scheduledSubjectItem);
                    mode.finish();
                    return true;
                case R.id.edit_lesson:
                    mClickListener.onEditLessonClick(scheduledSubjectItem);
                    mode.finish();
                    return true;
                default:
                    return false;
            }
        }

        @Override
        public void onDestroyActionMode(ActionMode mode) {
            multiSelect = false;
            selectedItems.clear();
            notifyDataSetChanged();
            mActionMode = null;
        }
    };


    public SubjectAdapter(Context context, List<ScheduledSubject> subjects, ActionModeMenuClickListener clickListener) {
        this.mContext = context;
        this.mClickListener = clickListener;
        this.mSubjects = subjects;
    }

    @Override
    public SubjectHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater
                .inflate(R.layout.list_item_subject, parent, false);
        return new SubjectHolder(view, mClickListener);
    }

    @Override
    public void onBindViewHolder(SubjectHolder holder, int position) {
        holder.bindSubject(position);
    }

    @Override
    public int getItemCount() {
        return mSubjects.size();
    }

    class SubjectHolder extends RecyclerView.ViewHolder {

        private LinearLayout mSubjectItem;
        private LinearLayout mTimeUnit;
        private LinearLayout mNameUnit;
        private TextView mNumberTextView;
        private TextView mTimeTextView;
        private TextView mNameTextView;
        private TextView mHomeworkTextView;

        private ScheduledSubject mSubject;

        public SubjectHolder(View itemView, final ActionModeMenuClickListener clickListener) {
            super(itemView);
            mSubjectItem = (LinearLayout)
                    itemView.findViewById(R.id.subject_item);
            mTimeUnit = (LinearLayout) itemView.findViewById(R.id.time_unit);
            mNameUnit = (LinearLayout) itemView.findViewById(R.id.name_unit);
            mNameTextView = (TextView)
                    itemView.findViewById(R.id.list_item_subject_name);
            mNumberTextView = (TextView)
                    itemView.findViewById(R.id.list_item_subject_number);
            mTimeTextView = (TextView)
                    itemView.findViewById(R.id.list_item_time);
            mHomeworkTextView = (TextView)
                    itemView.findViewById(R.id.list_item_homework);
        }


        public void selectItem(ScheduledSubject item) {
            if (multiSelect) {
                if (selectedItems.contains(item)) {
                    selectedItems.remove(item);
                   // mSubjectItem.setBackgroundColor(Color.WHITE);
                    mTimeUnit.setBackgroundColor(Color.parseColor("#8a6fae"));
                    mNameUnit.setBackgroundColor(Color.WHITE);

                } else {
                    selectedItems.add(item);
                  //  mSubjectItem.setBackgroundColor(Color.LTGRAY);
                    mTimeUnit.setBackgroundColor(Color.parseColor("#b3b3c3"));
                    mNameUnit.setBackgroundColor(Color.parseColor("#b3b3c3"));

                }
                if (selectedItems.size() == 1) {
                    isAddHwButton = true;
                    mActionMode.invalidate();
                } else {
                    isAddHwButton = false;
                    mActionMode.invalidate();
                }

            }
        }

        public void bindSubject(int position) {
            final ScheduledSubject subject = mSubjects.get(position);

            mSubject = subject;
            mNameTextView.setText(mSubject.getSubject().getName());
            mNumberTextView.setText(String.valueOf(position + 1));
            mHomeworkTextView.setText(mSubject.getHomework());

            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm");
            mTimeTextView.setText(simpleDateFormat.format(subject.getLessonTime()));

            if (selectedItems.contains(subject)) {
                mTimeUnit.setBackgroundColor(Color.parseColor("#b3b3c3"));
                mNameUnit.setBackgroundColor(Color.parseColor("#b3b3c3"));
            } else {
                mTimeUnit.setBackgroundColor(Color.parseColor("#8a6fae"));
                mNameUnit.setBackgroundColor(Color.WHITE);
            }
            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if (mActionMode != null) {
                        return false;
                    }
                    mActionMode = ((AppCompatActivity) mContext).startSupportActionMode(actionModeCallbacks);
                    selectItem(subject);
                    return true;
                }
            });
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mActionMode != null) {
                        selectItem(subject);
                        if (selectedItems.isEmpty())
                            mActionMode.finish();
                    } else {

                        Intent intent = new Intent(mContext, HomeWorkActivity.class);
                        intent.putExtra(HomeWorkActivity.EXTRA_NAME, mSubject.getSubject().getName());
                        intent.putExtra(HomeWorkActivity.EXTRA_HOMEWORK, mSubject.getHomework());
                        mContext.startActivity(intent);


                    }
                }
            });
        }

    }

    public ScheduledSubject getScheduleSubject(int position) {
        return mSubjects.get(position);
    }

    public void setSubjects(List<ScheduledSubject> subjects) {
        mSubjects = subjects;
        notifyDataSetChanged();
    }


}
