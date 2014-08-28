package com.twi.awayday2014.stubs;

import android.content.Context;
import android.util.Log;
import com.twi.awayday2014.service.twitter.Tweeter;
import com.twi.awayday2014.service.twitter.TwitterPreference;
import org.joda.time.LocalDate;
import twitter4j.*;

import java.io.File;
import java.util.Arrays;
import java.util.Date;
import java.util.List;


public class TweeterStub extends Tweeter {

    public static final String TAG = "TweeterStub";

    private double iteration;
    private int number;

    public TweeterStub(Context context) {
        super(new TwitterPreference(context));
    }

    public boolean isLoggedIn() {
        return true;
    }

    public boolean hasMoreResults() {
        Log.d(TAG, "moreTweets :" + moreTweets());
        return moreTweets();
    }

    private boolean moreTweets() {
        if (iteration >= 2) {
            return false;
        } else {
            return true;
        }
    }

    public List<Status> search(String searchTerm) throws TwitterException {
        Log.d(TAG, "search :");
        return Arrays.asList(
                (Status)new StatusStub(number++), new StatusStub(number++), new StatusStub(number++),
                (Status)new StatusStub(number++), new StatusStub(number++), new StatusStub(number++),
                (Status)new StatusStub(number++), new StatusStub(number++), new StatusStub(number++),
                (Status)new StatusStub(number++));

    }


    public List<Status> searchNext() throws TwitterException {
        iteration++;
        Log.d(TAG, "searchNext :" + iteration);
        return Arrays.asList(
                (Status) new StatusStub(number++), new StatusStub(number++), new StatusStub(number++),
                (Status) new StatusStub(number++), new StatusStub(number++), new StatusStub(number++),
                (Status) new StatusStub(number++), new StatusStub(number++), new StatusStub(number++),
                (Status) new StatusStub(number++));

    }

    public void tweet(String tweetText) throws TwitterException {
    }

    public void tweet(String tweetText, File image) throws TwitterException {
    }

    public void tweet(StatusUpdate status) throws TwitterException {
    }

    public List<Status> getRecentTweets(String searchTerm) throws TwitterException {
        Log.d(TAG, "getRecentlyTweets :");
        return Arrays.asList(
                (Status)new StatusStub(number++), new StatusStub(number++), new StatusStub(number++),
                (Status)new StatusStub(number++), new StatusStub(number++));
    }

    public class StatusStub implements twitter4j.Status {

        private int i;

        public StatusStub(int i) {
            this.i = i;
        }

        @Override
        public Date getCreatedAt() {
            return LocalDate.now().toDate();
        }

        @Override
        public long getId() {
            return 0;
        }

        @Override
        public String getText() {
            return "tweet : " + i;
        }

        @Override
        public String getSource() {
            return null;
        }

        @Override
        public boolean isTruncated() {
            return false;
        }

        @Override
        public long getInReplyToStatusId() {
            return 0;
        }

        @Override
        public long getInReplyToUserId() {
            return 0;
        }

        @Override
        public String getInReplyToScreenName() {
            return null;
        }

        @Override
        public GeoLocation getGeoLocation() {
            return null;
        }

        @Override
        public Place getPlace() {
            return null;
        }

        @Override
        public boolean isFavorited() {
            return false;
        }

        @Override
        public boolean isRetweeted() {
            return false;
        }

        @Override
        public int getFavoriteCount() {
            return 0;
        }

        @Override
        public User getUser() {
            return new twitter4j.User() {
                @Override
                public long getId() {
                    return 0;
                }

                @Override
                public String getName() {
                    return "user name " + i;
                }

                @Override
                public String getScreenName() {
                    return "user + " + i;
                }

                @Override
                public String getLocation() {
                    return null;
                }

                @Override
                public String getDescription() {
                    return null;
                }

                @Override
                public boolean isContributorsEnabled() {
                    return false;
                }

                @Override
                public String getProfileImageURL() {
                    return null;
                }

                @Override
                public String getBiggerProfileImageURL() {
                    return null;
                }

                @Override
                public String getMiniProfileImageURL() {
                    return null;
                }

                @Override
                public String getOriginalProfileImageURL() {
                    return null;
                }

                @Override
                public String getProfileImageURLHttps() {
                    return null;
                }

                @Override
                public String getBiggerProfileImageURLHttps() {
                    return null;
                }

                @Override
                public String getMiniProfileImageURLHttps() {
                    return null;
                }

                @Override
                public String getOriginalProfileImageURLHttps() {
                    return null;
                }

                @Override
                public boolean isDefaultProfileImage() {
                    return false;
                }

                @Override
                public String getURL() {
                    return null;
                }

                @Override
                public boolean isProtected() {
                    return false;
                }

                @Override
                public int getFollowersCount() {
                    return 0;
                }

                @Override
                public Status getStatus() {
                    return null;
                }

                @Override
                public String getProfileBackgroundColor() {
                    return null;
                }

                @Override
                public String getProfileTextColor() {
                    return null;
                }

                @Override
                public String getProfileLinkColor() {
                    return null;
                }

                @Override
                public String getProfileSidebarFillColor() {
                    return null;
                }

                @Override
                public String getProfileSidebarBorderColor() {
                    return null;
                }

                @Override
                public boolean isProfileUseBackgroundImage() {
                    return false;
                }

                @Override
                public boolean isDefaultProfile() {
                    return false;
                }

                @Override
                public boolean isShowAllInlineMedia() {
                    return false;
                }

                @Override
                public int getFriendsCount() {
                    return 0;
                }

                @Override
                public Date getCreatedAt() {
                    return null;
                }

                @Override
                public int getFavouritesCount() {
                    return 0;
                }

                @Override
                public int getUtcOffset() {
                    return 0;
                }

                @Override
                public String getTimeZone() {
                    return null;
                }

                @Override
                public String getProfileBackgroundImageURL() {
                    return null;
                }

                @Override
                public String getProfileBackgroundImageUrlHttps() {
                    return null;
                }

                @Override
                public String getProfileBannerURL() {
                    return null;
                }

                @Override
                public String getProfileBannerRetinaURL() {
                    return null;
                }

                @Override
                public String getProfileBannerIPadURL() {
                    return null;
                }

                @Override
                public String getProfileBannerIPadRetinaURL() {
                    return null;
                }

                @Override
                public String getProfileBannerMobileURL() {
                    return null;
                }

                @Override
                public String getProfileBannerMobileRetinaURL() {
                    return null;
                }

                @Override
                public boolean isProfileBackgroundTiled() {
                    return false;
                }

                @Override
                public String getLang() {
                    return null;
                }

                @Override
                public int getStatusesCount() {
                    return 0;
                }

                @Override
                public boolean isGeoEnabled() {
                    return false;
                }

                @Override
                public boolean isVerified() {
                    return false;
                }

                @Override
                public boolean isTranslator() {
                    return false;
                }

                @Override
                public int getListedCount() {
                    return 0;
                }

                @Override
                public boolean isFollowRequestSent() {
                    return false;
                }

                @Override
                public URLEntity[] getDescriptionURLEntities() {
                    return new URLEntity[0];
                }

                @Override
                public URLEntity getURLEntity() {
                    return null;
                }

                @Override
                public int compareTo(User user) {
                    return 0;
                }

                @Override
                public RateLimitStatus getRateLimitStatus() {
                    return null;
                }

                @Override
                public int getAccessLevel() {
                    return 0;
                }
            };
        }

        @Override
        public boolean isRetweet() {
            return false;
        }

        @Override
        public Status getRetweetedStatus() {
            return null;
        }

        @Override
        public long[] getContributors() {
            return new long[0];
        }

        @Override
        public int getRetweetCount() {
            return 0;
        }

        @Override
        public boolean isRetweetedByMe() {
            return false;
        }

        @Override
        public long getCurrentUserRetweetId() {
            return 0;
        }

        @Override
        public boolean isPossiblySensitive() {
            return false;
        }

        @Override
        public String getLang() {
            return null;
        }

        @Override
        public Scopes getScopes() {
            return null;
        }

        @Override
        public int compareTo(Status status) {
            return 0;
        }

        @Override
        public UserMentionEntity[] getUserMentionEntities() {
            return new UserMentionEntity[0];
        }

        @Override
        public URLEntity[] getURLEntities() {
            return new URLEntity[0];
        }

        @Override
        public HashtagEntity[] getHashtagEntities() {
            return new HashtagEntity[0];
        }

        @Override
        public MediaEntity[] getMediaEntities() {
            return new MediaEntity[0];
        }

        @Override
        public MediaEntity[] getExtendedMediaEntities() {
            return new MediaEntity[0];
        }

        @Override
        public SymbolEntity[] getSymbolEntities() {
            return new SymbolEntity[0];
        }

        @Override
        public RateLimitStatus getRateLimitStatus() {
            return null;
        }

        @Override
        public int getAccessLevel() {
            return 0;
        }
    }
}
