package com.twi.awayday2014.view;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.twi.awayday2014.R;
import com.twi.awayday2014.models.Question;
import com.twi.awayday2014.utils.Fonts;

import java.util.List;

public class AllQuestionsViewHelper {
    private SessionDetailsActivity sessionDetailsActivity;
    private View mRootLayout;

    private QuestionsListAdapter questionsListAdapter;
    private ObjectAnimator mFadeOutAnimator;
    private ObjectAnimator mFadeInAnimator;
    private TextView countText;

    public AllQuestionsViewHelper(SessionDetailsActivity sessionDetailsActivity, View questionsLayout) {
        this.sessionDetailsActivity = sessionDetailsActivity;
        mRootLayout = questionsLayout;
    }

    public void onCreate() {
        setupDetailsLayout();
        setupAnimators();
    }

    public void show() {
        mRootLayout.setAlpha(0);
        mRootLayout.setVisibility(View.VISIBLE);
        mFadeInAnimator.start();
    }

    public void hide(){
        mFadeOutAnimator.start();
    }

    private void setupAnimators() {
        mFadeInAnimator = ObjectAnimator.ofFloat(mRootLayout, View.ALPHA, 1);
        mFadeOutAnimator = ObjectAnimator.ofFloat(mRootLayout, View.ALPHA, 0);

        mFadeOutAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                mRootLayout.setVisibility(View.GONE);
            }
        });
    }

    private void setupDetailsLayout() {
        TextView headerText = (TextView) mRootLayout.findViewById(R.id.allQuestionsHeaderText);
        headerText.setTypeface(Fonts.openSansRegular(sessionDetailsActivity));

        countText = (TextView) mRootLayout.findViewById(R.id.allQuestionsCountText);
        countText.setTypeface(Fonts.openSansLight(sessionDetailsActivity));

        ListView mItemsList = (ListView) mRootLayout.findViewById(R.id.questionsList);
        questionsListAdapter = new QuestionsListAdapter(sessionDetailsActivity, R.layout.item_all_questions_list);
        mItemsList.setAdapter(questionsListAdapter);

        Button backButton = (Button) mRootLayout.findViewById(R.id.backButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hide();
                sessionDetailsActivity.onQuestionsListHide();
            }
        });
    }

    public void onDataChange(List<Question> questions) {
        if(questions == null){
            return;
        }
        countText.setText("Total: " + questionsListAdapter.getCount());
        questionsListAdapter.onDataChange(questions);
    }

    public class QuestionsListAdapter extends ArrayAdapter<Question> {

        public QuestionsListAdapter(Context context, int resource) {
            super(context, resource);
        }

        public void onDataChange(List<Question> questionList){
            clear();
            addAll(questionList);
            notifyDataSetChanged();
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null) {
                LayoutInflater layoutInflater = LayoutInflater.from(getContext());
                convertView = layoutInflater.inflate(
                        R.layout.item_all_questions_list, parent, false);
                holder = new ViewHolder();
                holder.questionText = (TextView) convertView.findViewById(R.id.questionText);
                holder.name = (TextView) convertView.findViewById(R.id.name);
                holder.time = (TextView) convertView.findViewById(R.id.time);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            Question question = getItem(position);
            holder.questionText.setTypeface(Fonts.openSansRegular(getContext()));
            holder.questionText.setText(question.getQuestion());
            holder.name.setTypeface(Fonts.openSansLight(getContext()));
            holder.name.setText("- " + question.getName());
            holder.time.setTypeface(Fonts.openSansLight(getContext()));
            holder.time.setText(question.getDisplayTime());
            return convertView;
        }

        private class ViewHolder {
            TextView questionText;
            TextView name;
            TextView time;
        }
    }
}
