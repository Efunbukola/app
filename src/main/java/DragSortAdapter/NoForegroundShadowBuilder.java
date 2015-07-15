package DragSortAdapter;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;

import com.example.saboorsalaam.veed10.R;

import java.lang.ref.WeakReference;

public class NoForegroundShadowBuilder extends DragSortShadowBuilder {

  private final WeakReference<View> viewRef;
  private Context context;

  public NoForegroundShadowBuilder(View view, Point touchPoint, Context context) {
    super(view, touchPoint);
    this.viewRef = new WeakReference<>(view);
      this.context = context;
  }

  @Override public void onDrawShadow(@NonNull Canvas canvas) {
    final View view = viewRef.get();
    if (view != null) {
      Drawable foreground = null; //ContextCompat.getDrawable(context, R.drawable.empty_channel_place_holder_green);

      // remove foreground before canvas draw
      if (view instanceof FrameLayout && ((FrameLayout) view).getForeground() != null) {
        foreground = ((FrameLayout) view).getForeground();
        ((FrameLayout) view).setForeground(null);
      }

      view.draw(canvas);

      // reset foreground if it was removed
      if (foreground != null) {
        ((FrameLayout) view).setForeground(foreground);
      }

    } else {
      Log.e(TAG, "Asked to draw drag shadow but no view");
    }
  }
}
