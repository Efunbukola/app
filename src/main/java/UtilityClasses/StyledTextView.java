package UtilityClasses;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

import com.example.saboorsalaam.veed10.R;

/**
 * Created by Saboor Salaam on 6/12/2015.
 */
public class StyledTextView extends TextView {

    final static int NORMAL = 0, LIGHT = 5, BOLD = 1, ITALIC = 2, MEDIUM = 6, BLACK = 4;
    static int style;

    public StyledTextView(Context context) {
        super(context);
    }

    public StyledTextView(Context context, AttributeSet attrs) {
        super(context, attrs);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.StyledTextView);
        try {
                style = a.getInt(R.styleable.StyledTextView_text_style, NORMAL);
        } finally {
            a.recycle();
        }

        style(context);
    }

    public StyledTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    private void style(Context context) {
        Typeface tf;
        switch(style) {
            case BOLD:
                tf = Typeface.createFromAsset(context.getAssets(),
                        "fonts/Lato-Black.ttf");
                setTypeface(tf);
                break;
            case BLACK:
                tf = Typeface.createFromAsset(context.getAssets(),
                        "fonts//Lato-Heavy.ttf");
                setTypeface(tf);
                break;
            case ITALIC:
                tf = Typeface.createFromAsset(context.getAssets(),
                        "fonts/Lato-Regular.ttf");
                setTypeface(tf);
                break;
            case MEDIUM:
                tf = Typeface.createFromAsset(context.getAssets(),
                        "fonts/Lato-Semibold.ttf");
                setTypeface(tf);
                break;
            case NORMAL:
                tf = Typeface.createFromAsset(context.getAssets(),
                        "fonts/Lato-Regular.ttf");
                setTypeface(tf);
                break;
            case LIGHT:
                tf = Typeface.createFromAsset(context.getAssets(),
                        "fonts/Lato-Light.ttf");
                setTypeface(tf);
                break;
            default:
                tf = Typeface.createFromAsset(context.getAssets(),
                        "fonts//Lato-Regular.ttf");
                setTypeface(tf);
                break;
        }
    }
}
