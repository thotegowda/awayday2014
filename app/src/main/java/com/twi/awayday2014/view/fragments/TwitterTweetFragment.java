package com.twi.awayday2014.view.fragments;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.InputFilter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.twi.awayday2014.AwayDayApplication;
import com.twi.awayday2014.R;
import com.twi.awayday2014.services.twitter.TwitterService;
import com.twi.awayday2014.tasks.AsyncTweeterTasks;
import com.twi.awayday2014.view.TweetsActivity;
import com.wrapp.floatlabelededittext.FloatLabeledEditText;

import java.io.File;
import java.io.FileNotFoundException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static android.app.Activity.RESULT_OK;

public class TwitterTweetFragment extends SlidingFragment {
    public static final String TAG = "TwitterTweetFragment";
    public static final int INTENT_LOAD_IMAGE_GALLERY = 1;
    public static final int INTENT_LOAD_IMAGE_CAMERA = 2;

    public static final int TWEET_MAX_LENGTH = 140;

    private ImageView imageView;
    private View imageHolder;
    private View removeImageButton;
    private FloatLabeledEditText tweetText;
    private String hashTag;
    private Uri imageUri;

    @Override
    protected View getLayout(LayoutInflater inflater, ViewGroup container) {
        return inflater.inflate(R.layout.fragment_twitter_tweet, container, false);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case INTENT_LOAD_IMAGE_GALLERY:
                if (resultCode == RESULT_OK) {
                    Uri imageUri = data.getData();
                    if (imageUri == null) {
                        Log.e(TAG, "ImageURI returned is NULL");
                        showToast("Unable to load image");
                        break;
                    } else {
                        try {
                            Bitmap bitmap = BitmapFactory.decodeStream(getActivity().getContentResolver().openInputStream(imageUri));
                            setImage(bitmap, imageUri);
                        } catch (FileNotFoundException e) {
                            Log.e(TAG, "Unable to load image");
                            showToast("Unable to load image");
                        }
                    }
                }
                break;
            case INTENT_LOAD_IMAGE_CAMERA:
                if (resultCode == RESULT_OK) {
                    if (imageUri == null) {
                        Log.e(TAG, "ImageURI returned is NULL");
                        showToast("Unable to load image");
                        break;
                    } else {
                        try {
                            Bitmap bitmap = BitmapFactory.decodeStream(getActivity().getContentResolver().openInputStream(imageUri));
                            setImage(bitmap, imageUri);
                        } catch (FileNotFoundException e) {
                            Log.e(TAG, "Unable to load image");
                            showToast("Unable to load image");
                        }
                    }
                }
                break;

        }

    }

    private void showToast(String msg) {
        Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        TwitterService twitterService = ((AwayDayApplication) getActivity().getApplication()).getTwitterService();
        hashTag = "#" + twitterService.getPreference().getHashTags();
        setupTweetEditText();

        View galleryButton = mRootLayout.findViewById(R.id.gallery);
        galleryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), INTENT_LOAD_IMAGE_GALLERY);
            }
        });

        View cameraButton = mRootLayout.findViewById(R.id.camera);
        cameraButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lauchCamera();
            }
        });

        View tweetButton = mRootLayout.findViewById(R.id.tweetButton);
        tweetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tweet();
            }
        });

        setupImageView();

        mRootLayout.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                ((TweetsActivity) getActivity()).onTweetFragmentHeightChange(v.getHeight());
            }
        });

        mSlidingPaneCloseAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                tweetText.getEditText().setText(hashTag);
                removeImage();
            }
        });

        return mRootLayout;
    }

    private void tweet() {
        String tweetMessage = tweetText.getEditText().getText().toString();
        if (tweetMessage.length() <= hashTag.length()) {
            showToast("Please enter the tweet content");
            return;
        }

        if(!tweetMessage.contains(hashTag)){
            showToast("Please add the awayday hashtag.");
            return;
        }

        if (imageUri != null) {
            tweetImageAndText(tweetMessage);
        } else {
            tweetText(tweetMessage);
        }
        ((TweetsActivity)getActivity()).onTweetButtonClick();
    }

    private void tweetText(String text) {
        AsyncTweeterTasks asyncTweeterTasks = ((TweetsActivity) getActivity()).getAsyncTweeterTasks();
        asyncTweeterTasks.tweet(text);
    }

    private void tweetImageAndText(String text) {
        AsyncTweeterTasks asyncTweeterTasks = ((TweetsActivity) getActivity()).getAsyncTweeterTasks();
        asyncTweeterTasks.tweet(text, imageUri);
    }

    private void lauchCamera() {
        // Check if there is a camera.
        PackageManager packageManager = getActivity().getPackageManager();
        if (!packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA)) {
            showToast("This device does not have a camera.");
            return;
        }

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        imageUri = getOutputMediaFileUri(); // create a file to save the image
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri); // set the image file name

        // start the image capture Intent
        startActivityForResult(intent, INTENT_LOAD_IMAGE_CAMERA);
    }

    /**
     * Create a file Uri for saving an image or video
     */
    private static Uri getOutputMediaFileUri() {
        return Uri.fromFile(getOutputMediaFile());
    }

    /**
     * Create a File for saving an image or video
     */
    private static File getOutputMediaFile() {
        // To be safe, you should check that the SDCard is mounted
        // using Environment.getExternalStorageState() before doing this.

        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), "MyCameraApp");
        // This location works best if you want the created images to be shared
        // between applications and persist after your app has been uninstalled.

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.d(TAG, "failed to create directory");
                return null;
            }
        }

        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File mediaFile;
        mediaFile = new File(mediaStorageDir.getPath() + File.separator +
                "IMG_" + timeStamp + ".jpg");


        return mediaFile;
    }

    private void setupTweetEditText() {
        tweetText = (FloatLabeledEditText) mRootLayout.findViewById(R.id.tweet_text);
        InputFilter[] filter = new InputFilter[1];
        filter[0] = new InputFilter.LengthFilter(TWEET_MAX_LENGTH);
        EditText tweetEditText = tweetText.getEditText();
        tweetEditText.setFilters(filter);
        tweetEditText.setText(hashTag);
    }

    private void setupImageView() {
        imageHolder = mRootLayout.findViewById(R.id.imageHolder);
        imageView = (ImageView) mRootLayout.findViewById(R.id.image);
        removeImageButton = mRootLayout.findViewById(R.id.removeImageButton);
        removeImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeImage();
            }
        });
    }

    public void setImage(Bitmap bitmap, Uri imageUri) {
        this.imageUri = imageUri;
        imageView.setImageBitmap(bitmap);
        imageHolder.setVisibility(View.VISIBLE);
    }

    public void removeImage() {
        imageUri = null;
        imageHolder.setVisibility(View.GONE);
    }
}
