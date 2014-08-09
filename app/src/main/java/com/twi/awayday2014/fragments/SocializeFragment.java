package com.twi.awayday2014.fragments;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.ListFragment;
import android.graphics.Point;
import android.graphics.Rect;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;
import android.widget.AbsListView;
import android.widget.EditText;
import android.widget.ImageView;
import com.squareup.picasso.Picasso;
import com.twi.awayday2014.AwayDayApplication;
import com.twi.awayday2014.R;
import com.twi.awayday2014.adapters.TweetsAdapter;
import com.twi.awayday2014.Tweeter;
import com.twi.awayday2014.ui.MultiSwipeRefreshLayout;
import com.twi.awayday2014.ui.SwipeRefreshLayout;
import twitter4j.Status;
import twitter4j.TwitterException;

import java.util.ArrayList;
import java.util.List;

import static com.twi.awayday2014.ui.MultiSwipeRefreshLayout.CanChildScrollUpCallback;

public class SocializeFragment
        extends ListFragment
        implements CanChildScrollUpCallback, TweetsAdapter.OnTweetClickListener {

    private static final String TAG = "AwayDaySocialize";
    private static final String ARG_SECTION_NUMBER = "section_number";
    private static final String TWITTER_SEARCH_TERM = "bangalore";
    private static final String AWAYDAY_TWITTER_TAG = "#awayday2014";

    private Tweeter tweeter;
    private TweetsAdapter tweetsAdapter;

    private View rootView;
    private View signInOrTweetButton;
    private View tweetMessageLayout;
    private View tweetButton;
    private EditText tweetMessageView;
    private View cancelButton;
    private MultiSwipeRefreshLayout swipeRefreshLayout;

    private Animation pushInAnimation;
    private Animation pushOutAnimation;
    Animator currentAnimator;

    private boolean isTweetWindowVisible;
    private boolean isRefreshInProgress;
    private ImageView photoFullscreenView;
    private View currentPhotoView;
    private View loadingView;

    public static SocializeFragment newInstance(int sectionNumber) {
        SocializeFragment fragment = new SocializeFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    public SocializeFragment() {

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();

        //getListView().setVisibility(View.INVISIBLE);

        tweeter = getApplication().getTwitterService();

        setupAdapter();

        handleTwitterCallback();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_socialize, container, false);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        bindViews();
    }

    private void bindViews() {
        pushInAnimation = AnimationUtils.loadAnimation(this.getActivity(), R.anim.push_up_in);
        pushOutAnimation = AnimationUtils.loadAnimation(this.getActivity(), R.anim.push_up_out);

        trySetupSwipeRefresh();

        signInOrTweetButton = rootView.findViewById(R.id.signInOrTweet);
        signInOrTweetButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                if (tweeter.isTwitterLoggedInAlready()) {
                    showTweetWindow();
                } else {
                    loginToTwitter();
                }
            }
        });

        tweetMessageLayout = rootView.findViewById(R.id.tweet_message_layout);
        tweetMessageView = (EditText) rootView.findViewById(R.id.tweet_message);
        tweetButton = rootView.findViewById(R.id.tweet);
        tweetButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                hideTweetWindow();
                tweet();
            }
        });

        cancelButton = rootView.findViewById(R.id.cancel);
        cancelButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                hideTweetWindow();
            }
        });

        photoFullscreenView = (ImageView) rootView.findViewById(R.id.photo_full_screen);
        photoFullscreenView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                zoomOutPhotoFullScreenView(currentPhotoView, photoFullscreenView, 500);
            }
        });

        loadingView = rootView.findViewById(R.id.loading_spinner);
    }

    private void handleTwitterCallback() {
        if (!tweeter.isTwitterLoggedInAlready()) {
            if (isLaunchedFromTwitterCallbackUrl(getActivity().getIntent().getData())) {
                retrieveAccessToken(getActivity().getIntent().getData());
                showTweetWindow();
            }
        }
    }

    private void tweet() {
        String text = tweetMessageView.getText().toString();
        tweetMessageView.setText("");
        if (text.length() > 0) {
            tweet(AddTagsIfNeeded(text));
            tweetMessageLayout.setVisibility(View.GONE);
        }
    }

    private void trySetupSwipeRefresh() {
        swipeRefreshLayout = (MultiSwipeRefreshLayout) rootView;
        if (swipeRefreshLayout != null) {
            swipeRefreshLayout.setColorScheme(
                    R.color.refresh_progress_1,
                    R.color.refresh_progress_2,
                    R.color.refresh_progress_3,
                    R.color.refresh_progress_4);
            swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    refresh();
                }
            });

            swipeRefreshLayout.setCanChildScrollUpCallback(this);
        }
    }

    @Override
    public boolean canSwipeRefreshChildScrollUp() {
        return isTweetWindowVisible || !(isFirstTweet() && isFirstTweetVisible());
    }

    private boolean isFirstTweetVisible() {
        return getListView().getChildAt(0) != null ? getListView().getChildAt(0).getTop() >= 0 : true;
    }

    private boolean isFirstTweet() {
        return getListView().getFirstVisiblePosition() == 0;
    }

    private void refresh() {
        if (!isRefreshInProgress) {
            isRefreshInProgress = true;
            loadRecentTweets();
        }
    }

    private void onRefreshFinished(List<Status> tweets) {
        tweetsAdapter.insertAtTheTop(tweets);
        swipeRefreshLayout.setRefreshing(false);
        isRefreshInProgress = false;
    }

    private boolean isLaunchedFromTwitterCallbackUrl(Uri uri) {
        return uri != null && uri.toString().startsWith(Tweeter.TWITTER_CALLBACK_URL);
    }

    private void setupAdapter() {
        tweetsAdapter = new TweetsAdapter(this.getActivity(), new ArrayList<Status>(), this);
        setListAdapter(tweetsAdapter);

        getListView().setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView absListView, int i) {

            }

            @Override
            public void onScroll(AbsListView absListView, int firstVisible, int visibleCount, int totalCount) {
                int padding = 5;
                if (totalCount > 10
                        && shouldLoadMore(firstVisible, visibleCount, totalCount, padding)) {
                    twitterSearchNext();
                }
            }
        });

        twitterSearch(TWITTER_SEARCH_TERM);

    }

    private AwayDayApplication getApplication() {
        return (AwayDayApplication) getActivity().getApplication();
    }

    private String AddTagsIfNeeded(String text) {
        if (!text.contains(AWAYDAY_TWITTER_TAG)) {
            text += " " + AWAYDAY_TWITTER_TAG;
        }
        return text;
    }

    private void showTweetWindow() {
        if (currentAnimator != null) {
            currentAnimator.cancel();
        }

        isTweetWindowVisible = true;
        tweetMessageLayout.setVisibility(View.VISIBLE);
        pushInAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                tweetMessageLayout.requestFocus();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        tweetMessageLayout.startAnimation(pushInAnimation);
    }

    private void hideTweetWindow() {
        if (currentAnimator != null) {
            currentAnimator.cancel();
        }

        isTweetWindowVisible = false;
        pushOutAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                tweetMessageLayout.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        tweetMessageLayout.startAnimation(pushOutAnimation);
    }

    private boolean shouldLoadMore(int firstVisible, int visibleCount, int totalCount, int padding) {
        return firstVisible + visibleCount + padding >= totalCount;
    }

    private void loginToTwitter() {
        new AsyncTask<Void, Void, Void>() {

            @Override
            protected Void doInBackground(Void... voids) {
                try {
                    tweeter.login(SocializeFragment.this.getActivity());
                } catch (TwitterException e) {
                    e.printStackTrace();
                }
                return null;
            }
        }.execute();

    }

    public void retrieveAccessToken(final Uri uri) {
        new AsyncTask<Void, Void, Void>() {

            @Override
            protected Void doInBackground(Void... voids) {
                try {
                    tweeter.retrieveAccessToken(uri);
                } catch (TwitterException e) {
                    e.printStackTrace();
                }
                return null;
            }
        }.execute();

    }

    private void twitterSearch(final String searchTerm) {
        new AsyncTask<Void, Void, List<Status>>() {
            @Override
            protected List<twitter4j.Status> doInBackground(Void... voids) {
                return tweeter.search(searchTerm);
            }

            @Override
            protected void onPostExecute(List<twitter4j.Status> tweets) {
                tweetsAdapter.append(tweets);
                showContentOrLoadingIndicator(true);
            }
        }.execute();
    }

    private void twitterSearchNext() {
        if (!tweeter.hasMoreResults()) {
            return;
        }

        new AsyncTask<Void, Void, List<Status>>() {
            @Override
            protected List<twitter4j.Status> doInBackground(Void... voids) {
                return tweeter.searchNext();
            }

            @Override
            protected void onPostExecute(List<twitter4j.Status> tweets) {
                tweetsAdapter.append(tweets);
            }
        }.execute();
    }

    private void loadRecentTweets() {
        new AsyncTask<Void, Void, List<Status>>() {

            @Override
            protected List<twitter4j.Status> doInBackground(Void... voids) {
                return tweeter.getRecentTweets(TWITTER_SEARCH_TERM);
            }

            @Override
            protected void onPostExecute(List<twitter4j.Status> tweets) {
                onRefreshFinished(tweets);
            }
        }.execute();
    }

    private void tweet(final String tweetMessage) {
        new AsyncTask<Void, Void, Void>() {

            @Override
            protected Void doInBackground(Void... voids) {
                tweeter.tweet(tweetMessage);
                return null;
            }
        }.execute();

    }

    @Override
    public void onPhotoClicked(View photoView, String url) {
        Picasso.with(this.getActivity()).load(url).into(photoFullscreenView);
        currentPhotoView = photoView;
        zoomImageFromThumb(photoView, photoFullscreenView, 500);
    }

    public boolean isFullViewShowing() {
        return photoFullscreenView.getVisibility() == View.VISIBLE;
    }

    public void hideFullView() {
        zoomOutPhotoFullScreenView(currentPhotoView, photoFullscreenView, 500);
    }

    private void zoomImageFromThumb(final View thumbView, final View expandedImageView, int duration) {
        // If there's an animation in progress, cancel it immediately and proceed with this one.
        if (currentAnimator != null) {
            currentAnimator.cancel();
        }

        // Calculate the starting and ending bounds for the zoomed-in image. This step
        // involves lots of math. Yay, math.
        final Rect startBounds = new Rect();
        final Rect finalBounds = new Rect();
        final Point globalOffset = new Point();

        // The start bounds are the global visible rectangle of the thumbnail, and the
        // final bounds are the global visible rectangle of the container view. Also
        // set the container view's offset as the origin for the bounds, since that's
        // the origin for the positioning animation properties (X, Y).
        thumbView.getGlobalVisibleRect(startBounds);
        rootView.getGlobalVisibleRect(finalBounds, globalOffset);
        startBounds.offset(-globalOffset.x, -globalOffset.y);
        finalBounds.offset(-globalOffset.x, -globalOffset.y);

        // Adjust the start bounds to be the same aspect ratio as the final bounds using the
        // "center crop" technique. This prevents undesirable stretching during the animation.
        // Also calculate the start scaling factor (the end scaling factor is always 1.0).
        float startScale;
        if ((float) finalBounds.width() / finalBounds.height()
                > (float) startBounds.width() / startBounds.height()) {
            // Extend start bounds horizontally
            startScale = (float) startBounds.height() / finalBounds.height();
            float startWidth = startScale * finalBounds.width();
            float deltaWidth = (startWidth - startBounds.width()) / 2;
            startBounds.left -= deltaWidth;
            startBounds.right += deltaWidth;
        } else {
            // Extend start bounds vertically
            startScale = (float) startBounds.width() / finalBounds.width();
            float startHeight = startScale * finalBounds.height();
            float deltaHeight = (startHeight - startBounds.height()) / 2;
            startBounds.top -= deltaHeight;
            startBounds.bottom += deltaHeight;
        }

        // Hide the thumbnail and show the zoomed-in view. When the animation begins,
        // it will position the zoomed-in view in the place of the thumbnail.
        thumbView.setAlpha(0f);
        expandedImageView.setVisibility(View.VISIBLE);

        // Set the pivot point for SCALE_X and SCALE_Y transformations to the top-left corner of
        // the zoomed-in view (the default is the center of the view).
        expandedImageView.setPivotX(0f);
        expandedImageView.setPivotY(0f);

        // Construct and run the parallel animation of the four translation and scale properties
        // (X, Y, SCALE_X, and SCALE_Y).
        AnimatorSet set = new AnimatorSet();
        set.play(ObjectAnimator.ofFloat(expandedImageView, View.X, startBounds.left, finalBounds.left))
                .with(ObjectAnimator.ofFloat(expandedImageView, View.Y, startBounds.top, finalBounds.top))
                .with(ObjectAnimator.ofFloat(expandedImageView, View.SCALE_X, startScale, 1f))
                .with(ObjectAnimator.ofFloat(expandedImageView, View.SCALE_Y, startScale, 1f));
        set.setDuration(duration);
        set.setInterpolator(new DecelerateInterpolator());
        set.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                currentAnimator = null;
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                currentAnimator = null;
            }
        });
        set.start();
        currentAnimator = set;
    }

    private void zoomOutPhotoFullScreenView(final View thumbView, final View expandedImageView, int duration) {
        if (currentAnimator != null) {
            currentAnimator.cancel();
        }
        // Calculate the starting and ending bounds for the zoomed-in image. This step
        // involves lots of math. Yay, math.
        final Rect startBounds = new Rect();
        final Rect finalBounds = new Rect();
        final Point globalOffset = new Point();

        // The start bounds are the global visible rectangle of the thumbnail, and the
        // final bounds are the global visible rectangle of the container view. Also
        // set the container view's offset as the origin for the bounds, since that's
        // the origin for the positioning animation properties (X, Y).
        thumbView.getGlobalVisibleRect(startBounds);
        rootView.getGlobalVisibleRect(finalBounds, globalOffset);
        startBounds.offset(-globalOffset.x, -globalOffset.y);
        finalBounds.offset(-globalOffset.x, -globalOffset.y);

        // Adjust the start bounds to be the same aspect ratio as the final bounds using the
        // "center crop" technique. This prevents undesirable stretching during the animation.
        // Also calculate the start scaling factor (the end scaling factor is always 1.0).
        float startScale;
        if ((float) finalBounds.width() / finalBounds.height()
                > (float) startBounds.width() / startBounds.height()) {
            // Extend start bounds horizontally
            startScale = (float) startBounds.height() / finalBounds.height();
            float startWidth = startScale * finalBounds.width();
            float deltaWidth = (startWidth - startBounds.width()) / 2;
            startBounds.left -= deltaWidth;
            startBounds.right += deltaWidth;
        } else {
            // Extend start bounds vertically
            startScale = (float) startBounds.width() / finalBounds.width();
            float startHeight = startScale * finalBounds.height();
            float deltaHeight = (startHeight - startBounds.height()) / 2;
            startBounds.top -= deltaHeight;
            startBounds.bottom += deltaHeight;
        }


        final float startScaleFinal = startScale;
        if (currentAnimator != null) {
            currentAnimator.cancel();
        }

        // Animate the four positioning/sizing properties in parallel, back to their
        // original values.
        AnimatorSet set = new AnimatorSet();
        set
                .play(ObjectAnimator.ofFloat(expandedImageView, View.X, startBounds.left))
                .with(ObjectAnimator.ofFloat(expandedImageView, View.Y, startBounds.top))
                .with(ObjectAnimator
                        .ofFloat(expandedImageView, View.SCALE_X, startScaleFinal))
                .with(ObjectAnimator
                        .ofFloat(expandedImageView, View.SCALE_Y, startScaleFinal));
        set.setDuration(duration);
        set.setInterpolator(new DecelerateInterpolator());
        set.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                thumbView.setAlpha(1f);
                expandedImageView.setVisibility(View.GONE);
                currentAnimator = null;
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                thumbView.setAlpha(1f);
                expandedImageView.setVisibility(View.GONE);
                 currentAnimator = null;
            }
        });
        set.start();
        currentAnimator = set;
    }

    private void showContentOrLoadingIndicator(boolean contentLoaded) {
        if (currentAnimator != null) {
            currentAnimator.cancel();
        }

        final View showView = contentLoaded ? getListView() : loadingView;
        final View hideView = contentLoaded ? loadingView : getListView();

        showView.setAlpha(0f);
        showView.setVisibility(View.VISIBLE);

        showView.animate()
                .alpha(1f)
                .setDuration(500)
                .setListener(null);

        hideView.animate()
                .alpha(0f)
                .setDuration(500)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        hideView.setVisibility(View.GONE);
                    }
                });
    }
}
