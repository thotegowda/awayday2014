package com.twi.awayday2014.components.activities;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.*;
import com.squareup.picasso.Picasso;
import com.twi.awayday2014.service.twitter.AsyncTweeter;
import com.twi.awayday2014.AwayDayApplication;
import com.twi.awayday2014.R;
import com.twi.awayday2014.adapters.TweetsAdapter;
import com.twi.awayday2014.animation.SmoothInterpolator;
import com.twi.awayday2014.animation.ZoomAnimator;
import com.twi.awayday2014.components.fragments.TwitterLoginFragment;
import com.twi.awayday2014.customviews.MultiSwipeRefreshLayout;
import com.twi.awayday2014.customviews.SwipeRefreshLayout;
import twitter4j.Status;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TweetsActivity
        extends FragmentActivity
        implements AsyncTweeter.TwitterCallbacks,
        TweetsAdapter.OnTweetClickListener,
        MultiSwipeRefreshLayout.CanChildScrollUpCallback {

    private static final String TAG = "AwayDaySocialize";
    private static final String ARG_SECTION_NUMBER = "section_number";

    private static final int REQUEST_IMAGE_PICK = 101;
    private static final int REQUEST_IMAGE_CAPTURE = 102;

    public static final long OPEN_ANIMATION_DURATION = 600;
    public static final long CLOSE_ANIMATION_DURATION = 400;

    private AsyncTweeter twitter;
    private TweetsAdapter tweetsAdapter;

    private View rootView;
    private View signInOrTweetButton;
    private View tweetMessageLayout;
    private View tweetButton;
    private EditText tweetMessageView;
    private View cancelButton;
    private MultiSwipeRefreshLayout swipeRefreshLayout;
    private ImageView photoFullscreenView;
    private View currentPhotoView;
    private View loadingView;
    private ImageView selectedImageToTweet;

    private Animation pushInAnimation;
    private Animation pushOutAnimation;
    Animator currentAnimator;

    private boolean isTweetWindowVisible;
    private boolean isRefreshInProgress;
    private boolean isImageSelectedToTweet;

    private Uri selectedGalleryImageUri;
    private String mCurrentCameraPhotoPath;
    private View overlayLayout;
    private ListView listView;
    private TwitterLoginFragment twitterLoginFragment;
    private View cancelLoginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_socialize);

        rootView = findViewById(R.id.tweets_root_layout);

        twitter = new AsyncTweeter(this, getAwayDayApplication().getTwitterService(), this);

        AwayDayApplication awayDayApplication = (AwayDayApplication) getApplication();
        Bitmap background = awayDayApplication.getHomeActivityScreenshot();
        if (background != null) {
            rootView.setBackground(new BitmapDrawable(getResources(), background));
        }

        bindViews();
        showLoadingWindow();
        setupAdapter();
    }

    @Override
    protected void onResume() {
        super.onResume();

        twitter.search();
        handleTwitterCallback();
    }

    private AwayDayApplication getAwayDayApplication() {
        return (AwayDayApplication) getApplication();
    }

    private ListView getListView() {
        if (listView == null) {
            listView = (ListView) findViewById(R.id.tweets_list);
        }
        return listView;
    }

    private void showLoadingWindow() {
        getListView().setVisibility(View.GONE);
        overlayLayout.setVisibility(View.GONE);
    }

    private void bindViews() {
        setupFragments();

        pushInAnimation = AnimationUtils.loadAnimation(this, R.anim.push_up_in);
        pushOutAnimation = AnimationUtils.loadAnimation(this, R.anim.push_up_out);

        trySetupSwipeRefresh();

        signInOrTweetButton = rootView.findViewById(R.id.signInOrTweet);
        signInOrTweetButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                onTweetButtonPressed();
            }
        });

        cancelLoginButton = rootView.findViewById(R.id.cancelButton);
        cancelLoginButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                twitterLoginFragment.slideOut();
                startTwitterButtonMoveDownAnimation();
            }
        });

        photoFullscreenView = (ImageView) rootView.findViewById(R.id.photo_full_screen);
        photoFullscreenView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                zoomOutToThumbnailView();
            }
        });

        bindTweetComposeLayout();

        loadingView = rootView.findViewById(R.id.loading_spinner);
        overlayLayout = rootView.findViewById(R.id.overlay_layout);
    }

    private void onTweetButtonPressed() {
        if (twitter.isLoggedIn()) {
            showTweetWindow();
        } else {
            twitterLoginFragment.slideIn();
            startTwitterButtonMoveUpAnimation();
            //twitter.logIn();
        }
    }

    private void bindTweetComposeLayout() {
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
                closeTweetComposeWindow();
            }
        });

        rootView.findViewById(R.id.pic_from_cam).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                pickImageFromCam();
            }
        });

        rootView.findViewById(R.id.pic_from_gallery).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                pickImageFromGallery();
            }
        });

        selectedImageToTweet = (ImageView) rootView.findViewById(R.id.selected_image_holder);
    }

    private void closeTweetComposeWindow() {
        hideTweetWindow();
        clearSelectedImage();
        tweetMessageView.setText("");
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
        if (isLastTweetVisible()) {
            return true;
        } else {
            return isTweetWindowVisible || !(isFirstTweet() && isFirstTweetVisible());
        }
    }

    private boolean isFirstTweetVisible() {
        return getListView().getChildAt(0) != null ? getListView().getChildAt(0).getTop() >= 0 : true;
    }

    private boolean isFirstTweet() {
        return getListView().getFirstVisiblePosition() == 0;
    }

    private boolean isLastTweetVisible() {
        return getListView().getLastVisiblePosition() >= tweetsAdapter.getCount() - 1;
    }

    private void refresh() {
        if (!isRefreshInProgress) {
            isRefreshInProgress = true;
            twitter.refresh();
        }
    }

    private boolean isLaunchedFromTwitterCallbackUrl(Uri uri) {
        return uri != null && twitter.isCallbackUrl(uri);
    }

    private void setupAdapter() {
        tweetsAdapter = new TweetsAdapter(this, new ArrayList<Status>(), this);
        setListAdapter(tweetsAdapter);

        getListView().setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView absListView, int i) {

            }

            @Override
            public void onScroll(AbsListView absListView, int firstVisible, int visibleCount, int totalCount) {
                if (!twitter.isSearchInProgress() && firstVisible + visibleCount >= (totalCount - 5) && twitter.hasMoreTweets()) {
                    twitter.searchNext();
                }
            }
        });
    }

    private void setListAdapter(ListAdapter adapter) {
        getListView().setAdapter(adapter);
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

    @Override
    public void onPhotoClicked(View photoView, String url) {
        Picasso.with(this).load(url).into(photoFullscreenView);
        currentPhotoView = photoView;
        zoomInToFullScreenView();
    }

    private boolean isTweetComposeWindowOpen() {
        return tweetMessageLayout.getVisibility() == View.VISIBLE;
    }

    public boolean isFullViewShowing() {
        return photoFullscreenView.getVisibility() == View.VISIBLE;
    }

    public void onBackPressed() {
        if (isFullViewShowing()) {
            zoomOutToThumbnailView();
        } else if (isTweetComposeWindowOpen()) {
            closeTweetComposeWindow();
        } else {
            finish();
        }
    }

    public void zoomInToFullScreenView() {
        new ZoomAnimator().from(currentPhotoView).to(photoFullscreenView).inside(rootView).animateIn();
    }

    public void zoomOutToThumbnailView() {
        new ZoomAnimator().from(currentPhotoView).to(photoFullscreenView).inside(rootView).animateOut();
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
                .setDuration(200)
                .setListener(null);

        hideView.animate()
                .alpha(0f)
                .setDuration(200)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        hideView.setVisibility(View.GONE);
                        overlayLayout.setVisibility(View.VISIBLE);
                    }
                });
    }

    private void pickImageFromCam() {
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        photoPickerIntent.setType("image/*");
        if (photoPickerIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(photoPickerIntent, REQUEST_IMAGE_PICK);
        }
    }

    private void pickImageFromGallery() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
            if (photoFile != null) {
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile));
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            handleImageCapture();

        } else if (requestCode == REQUEST_IMAGE_PICK && resultCode == RESULT_OK) {
            handleImageSelection(data);
        }
    }

    private void handleImageSelection(Intent data) {
        selectedGalleryImageUri = data.getData();
        try {
            setBitmap(getContentResolver().openInputStream(selectedGalleryImageUri));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            Log.d(TAG, "Failed to load the thumbnail image with ex: " + e.getMessage());
        }
    }

    private void handleImageCapture() {
        isImageSelectedToTweet = true;
        try {
            setBitmap(new FileInputStream(mCurrentCameraPhotoPath));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            Log.d(TAG, "Failed to load the thumbnail image with ex: " + e.getMessage());
        }
    }

    @Override
    public void onSearchResults(List<Status> tweets) {
        tweetsAdapter.append(tweets);
        showContentOrLoadingIndicator(true);
    }

    @Override
    public void onRefresh(List<Status> tweets) {
        tweetsAdapter.insertAtTheTop(tweets);
        swipeRefreshLayout.setRefreshing(false);
        isRefreshInProgress = false;
    }

    private void setBitmap(InputStream thumbnail) {
        setSelectedImage(BitmapFactory.decodeStream(thumbnail)); // TODO: load lighter image
    }

    private void setSelectedImage(Bitmap imageBitmap) {
        isImageSelectedToTweet = true;
        selectedImageToTweet.setImageBitmap(imageBitmap);
    }

    private void clearSelectedImage() {
        isImageSelectedToTweet = false;
        selectedGalleryImageUri = null;
        selectedImageToTweet.setImageBitmap(null);
    }

    private void handleTwitterCallback() {
        if (!twitter.isLoggedIn() && isLaunchedFromTwitterCallbackUrl(getIntent().getData())) {
            twitter.onCallbackUrlInvoked(getIntent().getData());
            showTweetWindow();
        }
    }

    private void tweet() {
        String tweetMessage = tweetMessageView.getText().toString();
        if (isImageSelectedToTweet) {
            tweetImage(tweetMessage);
        } else if (tweetMessage.length() > 0) {
            tweetText(tweetMessage);
        }

        tweetMessageLayout.setVisibility(View.GONE);
        tweetMessageView.setText("");
        clearSelectedImage();
    }

    private void tweetText(String text) {
        twitter.tweet(text);
    }

    private void tweetImage(String text) {
        if (selectedGalleryImageUri != null) {
            twitter.tweet(text, new File(getPath(selectedGalleryImageUri)));
        } else {
            twitter.tweet(text, new File(mCurrentCameraPhotoPath));
        }
    }

    public String getPath(Uri uri) {
        if (uri == null) {
            return null;
        }

        String path = uri.getPath();
        Cursor cursor = null;
        try {
            String[] projection = {MediaStore.Images.Media.DATA};
            cursor = getContentResolver().query(uri, projection, null, null, null);
            if (cursor != null) {
                int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                cursor.moveToFirst();
                path = cursor.getString(column_index);
                cursor.close();
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return path;
    }

    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File image = File.createTempFile(imageFileName, ".jpg", getDcimFolderPath());
        mCurrentCameraPhotoPath = image.getAbsolutePath();
        return image;
    }

    private File getDcimFolderPath() {
        return Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);
    }

    private void startTwitterButtonMoveUpAnimation() {
        signInOrTweetButton.animate()
                .alpha(0f)
                .rotation(360)
                .translationY(-twitterLoginFragment.getRootLayoutHeight())
                .setDuration(OPEN_ANIMATION_DURATION)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        signInOrTweetButton.setVisibility(View.GONE);
                    }
                })
                .setInterpolator(new SmoothInterpolator())
                .start();
        cancelLoginButton.setVisibility(View.VISIBLE);
        cancelLoginButton.setAlpha(0);
        cancelLoginButton.animate()
                .alpha(1f)
                .rotation(360)
                .translationY(-twitterLoginFragment.getRootLayoutHeight())
                .setDuration(OPEN_ANIMATION_DURATION)
                .setInterpolator(new SmoothInterpolator())
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        //just a placeholder to override the other listener
                    }
                })
                .start();
    }

    private void startTwitterButtonMoveDownAnimation() {
        signInOrTweetButton.setVisibility(View.VISIBLE);
        signInOrTweetButton.setAlpha(0);
        signInOrTweetButton.animate()
                .alpha(1f)
                .rotation(-360)
                .translationY(0)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        //just a placeholder to override the other listener
                    }
                })
                .setDuration(CLOSE_ANIMATION_DURATION)
                .start();

        cancelLoginButton.animate()
                .alpha(0f)
                .rotation(-360)
                .translationY(0)
                .setDuration(CLOSE_ANIMATION_DURATION)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        cancelLoginButton.setVisibility(View.GONE);
                    }
                })
                .start();
    }

    private void setupFragments() {
        twitterLoginFragment = (TwitterLoginFragment) getSupportFragmentManager().findFragmentById(R.id.twitterLoginFragment);
        twitterLoginFragment.setOnLoginClickListener(new TwitterLoginFragment.OnLoginClickListener() {
            @Override
            public void onLoginButtonClicked() {
                twitter.logIn();
            }
        });
    }
}
