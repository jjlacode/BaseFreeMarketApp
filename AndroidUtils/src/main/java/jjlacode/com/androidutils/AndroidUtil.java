package jjlacode.com.androidutils;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class AndroidUtil extends AppCompatActivity {



    public static boolean validateEmail(CharSequence emailAddress) {
        return !TextUtils.isEmpty(emailAddress) && android.util.Patterns.EMAIL_ADDRESS.matcher(
                emailAddress).matches();
    }


    public static boolean validateCardNumber(String cardNumber) {

        if (TextUtils.isEmpty(cardNumber)) {
            return false;
        } else {
            cardNumber = cardNumber.replaceAll("\\D", "");
        }

        int len = cardNumber.length() - 1;
        if (len < 15) {
            return false;
        }
        int sum = 0;
        int value;
        StringBuilder sb = new StringBuilder(cardNumber.substring(0, len));
        for (int i = len - 1; i >= 0; i -= 2) {
            sb.replace(i, i + 1, String.valueOf((sb.charAt(i) - '0') * 2));
        }
        for (int i = 0; i < sb.length(); i++) {
            sum += sb.charAt(i) - '0';
        }
        if (sum % 10 == 0) {
            value = 0;
        } else {
            value = (sum / 10 + 1) * 10 - sum;
        }

        return value == cardNumber.charAt(len) - '0';
    }

    public static boolean validateCardExpireDate(String date) {
        if (date.length() == 5) {
            DateFormat df = new SimpleDateFormat("MM/yy", Locale.getDefault());
            try {
                Calendar checkDate = Calendar.getInstance();
                checkDate.setTime(df.parse(date));
                Calendar now = Calendar.getInstance();
                if (checkDate.get(Calendar.YEAR) < now.get(Calendar.YEAR)) {
                    return false;
                }
                if (checkDate.get(Calendar.YEAR) == now.get(Calendar.YEAR) && checkDate.get(Calendar.MONTH) >= now
                        .get(Calendar.MONTH)) {
                    return true;
                }
                if (checkDate.get(Calendar.YEAR) > now.get(Calendar.YEAR)) {
                    return true;
                }
                return false;
            } catch (ParseException e) {

            }
        }
        return false;
    }

    public class ScalableImageView extends AppCompatImageView {

        boolean adjustViewBounds;

        public ScalableImageView(Context context) {
            super(context);
        }

        public ScalableImageView(Context context, AttributeSet attrs) {
            super(context, attrs);
        }

        public ScalableImageView(Context context, AttributeSet attrs, int defStyleAttr) {
            super(context, attrs, defStyleAttr);
        }

        @Override
        public void setAdjustViewBounds(boolean adjustViewBounds) {
            this.adjustViewBounds = adjustViewBounds;
            super.setAdjustViewBounds(adjustViewBounds);
        }

        @Override
        protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
            Drawable drawable = getDrawable();
            if (drawable == null) {
                super.onMeasure(widthMeasureSpec, heightMeasureSpec);
                return;
            }

            if (adjustViewBounds) {
                int drawableWidth = drawable.getIntrinsicWidth();
                int drawableHeight = drawable.getIntrinsicHeight();
                int heightSize = MeasureSpec.getSize(heightMeasureSpec);
                int widthSize = MeasureSpec.getSize(widthMeasureSpec);
                int heightMode = MeasureSpec.getMode(heightMeasureSpec);
                int widthMode = MeasureSpec.getMode(widthMeasureSpec);

                if (heightMode == MeasureSpec.EXACTLY && widthMode != MeasureSpec.EXACTLY) {
                    int height = heightSize;
                    int width = height * drawableWidth / drawableHeight;
                    if (isInScrollingContainer())
                        setMeasuredDimension(width, height);
                    else
                        setMeasuredDimension(Math.min(width, widthSize), Math.min(height, heightSize));
                } else if (widthMode == MeasureSpec.EXACTLY && heightMode != MeasureSpec.EXACTLY) {
                    int width = widthSize;
                    int height = width * drawableHeight / drawableWidth;
                    if (isInScrollingContainer())
                        setMeasuredDimension(width, height);
                    else
                        setMeasuredDimension(Math.min(width, widthSize), Math.min(height, heightSize));
                } else {
                    super.onMeasure(widthMeasureSpec, heightMeasureSpec);
                }
            } else {
                super.onMeasure(widthMeasureSpec, heightMeasureSpec);
            }
        }

        private boolean isInScrollingContainer() {
            ViewParent parent = getParent();
            while (parent != null && parent instanceof ViewGroup) {
                if (((ViewGroup) parent).shouldDelayChildPressedState()) {
                    return true;
                }
                parent = parent.getParent();
            }
            return false;
        }
    }


}
