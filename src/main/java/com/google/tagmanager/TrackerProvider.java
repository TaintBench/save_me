package com.google.tagmanager;

import android.content.Context;
import com.google.analytics.tracking.android.GoogleAnalytics;
import com.google.analytics.tracking.android.Logger;
import com.google.analytics.tracking.android.Logger.LogLevel;
import com.google.analytics.tracking.android.Tracker;
import com.google.android.gms.common.util.VisibleForTesting;

class TrackerProvider {
    private Context mContext;
    private GoogleAnalytics mGoogleAnalytics;

    static class LoggerImpl implements Logger {
        LoggerImpl() {
        }

        public void error(String message) {
            Log.e(message);
        }

        public void error(Exception exception) {
            Log.e("", exception);
        }

        public void info(String message) {
            Log.i(message);
        }

        public void verbose(String message) {
            Log.v(message);
        }

        public void warn(String message) {
            Log.w(message);
        }

        public LogLevel getLogLevel() {
            Logger.LogLevel logLevel = Log.getLogLevel();
            return logLevel == null ? LogLevel.ERROR : toAnalyticsLogLevel(logLevel);
        }

        public void setLogLevel(LogLevel logLevel) {
            Log.w("GA uses GTM logger. Please use TagManager.getLogger().setLogLevel(LogLevel) instead.");
        }

        private static LogLevel toAnalyticsLogLevel(Logger.LogLevel logLevel) {
            switch (logLevel) {
                case NONE:
                case ERROR:
                    return LogLevel.ERROR;
                case WARNING:
                    return LogLevel.WARNING;
                case INFO:
                case DEBUG:
                    return LogLevel.INFO;
                case VERBOSE:
                    return LogLevel.VERBOSE;
                default:
                    return LogLevel.ERROR;
            }
        }
    }

    TrackerProvider(Context context) {
        this.mContext = context;
    }

    @VisibleForTesting
    TrackerProvider(GoogleAnalytics ga) {
        this.mGoogleAnalytics = ga;
        this.mGoogleAnalytics.setLogger(new LoggerImpl());
    }

    public Tracker getTracker(String trackingId) {
        initTrackProviderIfNecessary();
        return this.mGoogleAnalytics.getTracker(trackingId);
    }

    public void close(Tracker tracker) {
        this.mGoogleAnalytics.closeTracker(tracker.getName());
    }

    private synchronized void initTrackProviderIfNecessary() {
        if (this.mGoogleAnalytics == null) {
            this.mGoogleAnalytics = GoogleAnalytics.getInstance(this.mContext);
            this.mGoogleAnalytics.setLogger(new LoggerImpl());
        }
    }
}
