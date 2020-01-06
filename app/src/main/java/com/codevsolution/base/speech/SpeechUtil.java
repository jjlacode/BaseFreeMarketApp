package com.codevsolution.base.speech;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.util.Log;

import com.codevsolution.base.interfaces.SpeechDelegate;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

public class SpeechUtil {

    private static final String LOG_TAG = SpeechUtil.class.getSimpleName();

    private static SpeechUtil instance = null;

    private SpeechRecognizer mSpeechRecognizer;
    private String mCallingPackage;
    private boolean mPreferOffline = false;
    private boolean mGetPartialResults = true;
    private SpeechDelegate mDelegate;
    private boolean mIsListening = false;

    private final List<String> mPartialData = new ArrayList<>();
    private String mUnstableData;

    private DelayedOperation mDelayedStopListening;
    private Context mContext;

    private Locale mLocale = Locale.getDefault();
    private float mTtsRate = 1.0f;
    private float mTtsPitch = 1.0f;
    private long mStopListeningDelayInMs = 10000;
    private long mTransitionMinimumDelay = 1200;
    private long mLastActionTimestamp;
    private List<String> mLastPartialResults = null;


    private final RecognitionListener mListener = new RecognitionListener() {

        @Override
        public void onReadyForSpeech(final Bundle bundle) {
            mPartialData.clear();
            mUnstableData = null;
        }

        @Override
        public void onBeginningOfSpeech() {

            mDelayedStopListening.start(new DelayedOperation.Operation() {
                @Override
                public void onDelayedOperation() {
                    returnPartialResultsAndRecreateSpeechRecognizer();
                    Log.d("ReachedStop", "Stoppong");
                    //  mListenerDelay.onClick("1");
                }

                @Override
                public boolean shouldExecuteDelayedOperation() {
                    return true;
                }
            });
        }

        @Override
        public void onRmsChanged(final float v) {
            try {
                if (mDelegate != null)
                    mDelegate.onSpeechRmsChanged(v);
            } catch (final Throwable exc) {
                Log.e(SpeechUtil.class.getSimpleName(),
                        "Unhandled exception in delegate onSpeechRmsChanged", exc);
            }

        }

        @Override
        public void onPartialResults(final Bundle bundle) {
            mDelayedStopListening.resetTimer();

            final List<String> partialResults = bundle.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
            final List<String> unstableData = bundle.getStringArrayList("android.speech.extra.UNSTABLE_TEXT");

            if (partialResults != null && !partialResults.isEmpty()) {
                mPartialData.clear();
                mPartialData.addAll(partialResults);
                mUnstableData = unstableData != null && !unstableData.isEmpty()
                        ? unstableData.get(0) : null;
                try {
                    if (mLastPartialResults == null || !mLastPartialResults.equals(partialResults)) {
                        if (mDelegate != null)
                            mDelegate.onSpeechPartialResults(partialResults);
                        mLastPartialResults = partialResults;
                    }
                } catch (final Throwable exc) {
                    Log.e(SpeechUtil.class.getSimpleName(),
                            "Unhandled exception in delegate onSpeechPartialResults", exc);
                }
            }
        }

        @Override
        public void onResults(final Bundle bundle) {
            mDelayedStopListening.cancel();

            final List<String> results = bundle.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);

            final String result;

            if (results != null && !results.isEmpty()
                    && results.get(0) != null && !results.get(0).isEmpty()) {
                result = results.get(0);
            } else {
                Log.i(SpeechUtil.class.getSimpleName(), "No speech results, getting partial");
                result = getPartialResultsAsString();
            }

            mIsListening = false;

            try {
                if (mDelegate != null)
                    mDelegate.onSpeechResult(result.trim());
            } catch (final Throwable exc) {
                Log.e(SpeechUtil.class.getSimpleName(),
                        "Unhandled exception in delegate onSpeechResult", exc);
            }

            initSpeechRecognizer(mContext);
        }

        @Override
        public void onError(final int code) {
            Log.e(LOG_TAG, "Speech recognition error", new SpeechRecognitionException(code));
            returnPartialResultsAndRecreateSpeechRecognizer();
        }

        @Override
        public void onBufferReceived(final byte[] bytes) {

        }

        @Override
        public void onEndOfSpeech() {

        }

        @Override
        public void onEvent(final int i, final Bundle bundle) {

        }
    };

    public SpeechUtil(final Context context) {
        initSpeechRecognizer(context);
    }

    public SpeechUtil(final Context context, final String callingPackage) {
        initSpeechRecognizer(context);
        mCallingPackage = callingPackage;
    }

    private void initSpeechRecognizer(final Context context) {
        if (context == null)
            throw new IllegalArgumentException("context must be defined!");

        mContext = context;

        if (SpeechRecognizer.isRecognitionAvailable(context)) {
            if (mSpeechRecognizer != null) {
                try {
                    mSpeechRecognizer.destroy();
                } catch (final Throwable exc) {
                    Log.d(SpeechUtil.class.getSimpleName(),
                            "Non-Fatal error while destroying speech. " + exc.getMessage());
                } finally {
                    mSpeechRecognizer = null;
                }
            }

            mSpeechRecognizer = SpeechRecognizer.createSpeechRecognizer(context);
            mSpeechRecognizer.setRecognitionListener(mListener);
            initDelayedStopListening(context);

        } else {
            mSpeechRecognizer = null;
        }

        mPartialData.clear();
        mUnstableData = null;
    }

    private void initDelayedStopListening(final Context context) {
        if (mDelayedStopListening != null) {
            mDelayedStopListening.cancel();
            mDelayedStopListening = null;
        }
//        Toast.makeText(context, "destroyed", Toast.LENGTH_SHORT).show();
        if (mListenerDelay != null) {
            mListenerDelay.onSpecifiedCommandPronounced("1");
        }
        mDelayedStopListening = new DelayedOperation(context, "delayStopListening", mStopListeningDelayInMs);
    }

    /**
     * Initializes speech recognition.
     *
     * @param context application context
     * @return speech instance
     */
    public static SpeechUtil init(final Context context) {
        if (instance == null) {
            instance = new SpeechUtil(context);
        }

        return instance;
    }

    /**
     * Initializes speech recognition.
     *
     * @param context        application context
     * @param callingPackage The extra key used in an intent to the speech recognizer for
     *                       voice search. Not generally to be used by developers.
     *                       The system search dialog uses this, for example, to set a calling
     *                       package for identification by a voice search API.
     *                       If this extra is set by anyone but the system process,
     *                       it should be overridden by the voice search implementation.
     *                       By passing null or empty string (which is the default) you are
     *                       not overriding the calling package
     * @return speech instance
     */
    public static SpeechUtil init(final Context context, final String callingPackage) {
        if (instance == null) {
            instance = new SpeechUtil(context, callingPackage);
        }

        return instance;
    }

    /**
     * Must be called inside Activity's onDestroy.
     */
    public synchronized void shutdown() {
        if (mSpeechRecognizer != null) {
            try {
                mSpeechRecognizer.stopListening();
            } catch (final Exception exc) {
                Log.e(getClass().getSimpleName(), "Warning while de-initing speech recognizer", exc);
            }
        }

        unregisterDelegate();
        instance = null;
    }

    /**
     * Gets speech recognition instance.
     *
     * @return SpeechRecognition instance
     */
    public static SpeechUtil getInstance() {
        if (instance == null) {
            throw new IllegalStateException("Speech recognition has not been initialized! call init method first!");
        }

        return instance;
    }

    /**
     * Starts voice recognition.
     *
     * @param delegate delegate which will receive speech recognition events and status
     * @throws SpeechRecognitionNotAvailable      when speech recognition is not available on the device
     * @throws GoogleVoiceTypingDisabledException when google voice typing is disabled on the device
     */
    public void startListening(final SpeechDelegate delegate)
            throws SpeechRecognitionNotAvailable, GoogleVoiceTypingDisabledException {
        if (mIsListening) return;

        if (mSpeechRecognizer == null)
            throw new SpeechRecognitionNotAvailable();

        if (delegate == null)
            throw new IllegalArgumentException("delegate must be defined!");

        if (throttleAction()) {
            Log.d(getClass().getSimpleName(), "Hey man calm down! Throttling start to prevent disaster!");
            return;
        }
        mDelegate = delegate;

        final Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
                .putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 1)
                .putExtra(RecognizerIntent.EXTRA_PARTIAL_RESULTS, mGetPartialResults)
                .putExtra(RecognizerIntent.EXTRA_LANGUAGE, mLocale.getLanguage())
                .putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);

        if (mCallingPackage != null && !mCallingPackage.isEmpty()) {
            intent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, mCallingPackage);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            intent.putExtra(RecognizerIntent.EXTRA_PREFER_OFFLINE, mPreferOffline);
        }

        try {
            mSpeechRecognizer.startListening(intent);
        } catch (final SecurityException exc) {
            throw new GoogleVoiceTypingDisabledException();
        }

        mIsListening = true;
        updateLastActionTimestamp();

        try {
            if (mDelegate != null)
                mDelegate.onStartOfSpeech();
        } catch (final Throwable exc) {
            Log.e(SpeechUtil.class.getSimpleName(),
                    "Unhandled exception in delegate onStartOfSpeech", exc);
        }

    }

    private void unregisterDelegate() {
        mDelegate = null;
    }

    private void updateLastActionTimestamp() {
        mLastActionTimestamp = new Date().getTime();
    }

    private boolean throttleAction() {
        return (new Date().getTime() <= (mLastActionTimestamp + mTransitionMinimumDelay));
    }

    /**
     * Stops voice recognition listening.
     * This method does nothing if voice listening is not active
     */
    public void stopListening() {
        if (!mIsListening) return;

        if (throttleAction()) {
            Log.d(getClass().getSimpleName(), "Hey man calm down! Throttling stop to prevent disaster!");
            return;
        }

        mIsListening = false;
        updateLastActionTimestamp();
        returnPartialResultsAndRecreateSpeechRecognizer();
    }

    private String getPartialResultsAsString() {
        final StringBuilder out = new StringBuilder();

        for (final String partial : mPartialData) {
            out.append(partial).append(" ");
        }

        if (mUnstableData != null && !mUnstableData.isEmpty())
            out.append(mUnstableData);

        return out.toString().trim();
    }

    private void returnPartialResultsAndRecreateSpeechRecognizer() {
        mIsListening = false;
        try {
            if (mDelegate != null)
                mDelegate.onSpeechResult(getPartialResultsAsString());
        } catch (final Throwable exc) {
            Log.e(SpeechUtil.class.getSimpleName(),
                    "Unhandled exception in delegate onSpeechResult", exc);
        }

//        if (mProgressView != null)
//            mProgressView.onResultOrOnError();

        // recreate the speech recognizer
        initSpeechRecognizer(mContext);
    }

    /**
     * Check if voice recognition is currently active.
     *
     * @return true if the voice recognition is on, false otherwise
     */
    public boolean isListening() {
        return mIsListening;
    }

    /**
     * Set whether to only use an offline speech recognition engine.
     * The default is false, meaning that either network or offline recognition engines may be used.
     *
     * @param preferOffline true to prefer offline engine, false to use either one of the two
     * @return speech instance
     */
    public SpeechUtil setPreferOffline(final boolean preferOffline) {
        mPreferOffline = preferOffline;
        return this;
    }

    /**
     * Set whether partial results should be returned by the recognizer as the user speaks
     * (default is true). The server may ignore a request for partial results in some or all cases.
     *
     * @param getPartialResults true to get also partial recognition results, false otherwise
     * @return speech instance
     */
    public SpeechUtil setGetPartialResults(final boolean getPartialResults) {
        mGetPartialResults = getPartialResults;
        return this;
    }


    /**
     * Sets the idle timeout after which the listening will be automatically stopped.
     *
     * @param milliseconds timeout in milliseconds
     * @return speech instance
     */
    public SpeechUtil setStopListeningAfterInactivity(final long milliseconds) {
        mStopListeningDelayInMs = milliseconds;
        initDelayedStopListening(mContext);
        return this;
    }

    /**
     * Sets the minimum interval between start/stop events. This is useful to prevent
     * monkey input from users.
     *
     * @param milliseconds minimum interval betweeb state change in milliseconds
     * @return speech instance
     */
    public SpeechUtil setTransitionMinimumDelay(final long milliseconds) {
        mTransitionMinimumDelay = milliseconds;
        return this;
    }

    private SpeechUtil.stopDueToDelay mListenerDelay;

    // define listener
    public interface stopDueToDelay {
        void onSpecifiedCommandPronounced(final String event);
    }

    // set the listener. Must be called from the fragment
    public void setListener(SpeechUtil.stopDueToDelay listener) {
        this.mListenerDelay = listener;
    }

}

class DelayedOperation {

    private static final String LOG_TAG = DelayedOperation.class.getSimpleName();

    public interface Operation {
        void onDelayedOperation();

        boolean shouldExecuteDelayedOperation();
    }

    private long mDelay;
    private Operation mOperation;
    private Timer mTimer;
    private boolean started;
    private Context mContext;
    private String mTag;

    public DelayedOperation(Context context, String tag, long delayInMilliseconds) {
        if (context == null) {
            throw new IllegalArgumentException("Context is null");
        }

        if (delayInMilliseconds <= 0) {
            throw new IllegalArgumentException("The delay in milliseconds must be > 0");
        }

        mContext = context;
        mTag = tag;
        mDelay = delayInMilliseconds;
        Log.d(LOG_TAG, "created delayed operation with tag: " + mTag);
    }

    public void start(final Operation operation) {
        if (operation == null) {
            throw new IllegalArgumentException("The operation must be defined!");
        }

        Log.d(LOG_TAG, "starting delayed operation with tag: " + mTag);
        mOperation = operation;
        cancel();
        started = true;
        resetTimer();
    }

    public void resetTimer() {
        if (!started) return;

        if (mTimer != null) mTimer.cancel();

        Log.d(LOG_TAG, "resetting delayed operation with tag: " + mTag);
        mTimer = new Timer();
        mTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (mOperation.shouldExecuteDelayedOperation()) {
                    Log.d(LOG_TAG, "executing delayed operation with tag: " + mTag);
                    new Handler(mContext.getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {
                            mOperation.onDelayedOperation();
                        }
                    });
                }
                cancel();
            }
        }, mDelay);
    }

    public void cancel() {
        if (mTimer != null) {
            Log.d(LOG_TAG, "cancelled delayed operation with tag: " + mTag);
            mTimer.cancel();
            mTimer = null;
        }

        started = false;
    }
}

class SpeechRecognitionException extends Exception {

    private int code;

    public SpeechRecognitionException(int code) {
        super(getMessage(code));
        this.code = code;
    }

    private static String getMessage(int code) {
        String message;

        // these have been mapped from here:
        // https://developer.android.com/reference/android/speech/SpeechRecognizer.html
        switch (code) {
            case SpeechRecognizer.ERROR_AUDIO:
                message = code + " - Audio recording error";
                break;

            case SpeechRecognizer.ERROR_INSUFFICIENT_PERMISSIONS:
                message = code + " - Insufficient permissions. Request android.permission.RECORD_AUDIO";
                break;

            case SpeechRecognizer.ERROR_CLIENT:
                // http://stackoverflow.com/questions/24995565/android-speechrecognizer-when-do-i-get-error-client-when-starting-the-voice-reco
                message = code + " - Client side error. Maybe your internet connection is poor!";
                break;

            case SpeechRecognizer.ERROR_NETWORK:
                message = code + " - Network error";
                break;

            case SpeechRecognizer.ERROR_NETWORK_TIMEOUT:
                message = code + " - Network operation timed out";
                break;

            case SpeechRecognizer.ERROR_NO_MATCH:
                message = code + " - No recognition result matched. Try turning on partial results as a workaround.";
                break;

            case SpeechRecognizer.ERROR_RECOGNIZER_BUSY:
                message = code + " - RecognitionService busy";
                break;

            case SpeechRecognizer.ERROR_SERVER:
                message = code + " - Server sends error status";
                break;

            case SpeechRecognizer.ERROR_SPEECH_TIMEOUT:
                message = code + " - No speech input";
                break;

            default:
                message = code + " - Unknown exception";
                break;
        }

        return message;
    }

    public int getCode() {
        return code;
    }
}

