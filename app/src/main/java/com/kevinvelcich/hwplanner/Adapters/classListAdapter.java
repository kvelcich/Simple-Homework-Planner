package com.kevinvelcich.hwplanner.Adapters;

import android.animation.ValueAnimator;
import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.kevinvelcich.hwplanner.Classes.Class;
import com.kevinvelcich.hwplanner.Database.DatabaseAssignment;
import com.kevinvelcich.hwplanner.Database.DatabaseClass;
import com.kevinvelcich.hwplanner.R;

import java.util.Calendar;
import java.util.Random;

public class classListAdapter extends CursorAdapter {

    int icon[] = {R.drawable.num_blank, R.drawable.num_one, R.drawable.num_two, R.drawable.num_three,
            R.drawable.num_four, R.drawable.num_five, R.drawable.num_six, R.drawable.num_seven,
            R.drawable.num_eight, R.drawable.num_nine};

    int box[] = {R.drawable.box_red, R.drawable.box_pink, R.drawable.box_purple, R.drawable.box_blue,
            R.drawable.box_cyan, R.drawable.box_teal, R.drawable.box_green, R.drawable.box_yellow,
            R.drawable.box_orange};

    static TextView className;
    static ImageView numIcon;

    private AdapterCallback mAdapterCallback;

    public classListAdapter(Context context, Cursor cursor, int flags, AdapterCallback callback) {
        super(context, cursor, 0);
        this.mAdapterCallback = callback;
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        View view = LayoutInflater.from(context).inflate(R.layout.listview_classes, parent, false);
        className = (TextView) view.findViewById(R.id.className);
        numIcon = (ImageView) view.findViewById(R.id.numIcon);
        return view;
    }

    @Override
    public void bindView(View view, final Context context, final Cursor cursor) {


        className.setText(cursor.getString(cursor.getColumnIndex(DatabaseClass.COLUMN_NAME)));


        /* Setting the icon, displaying how many assignments are in the class. */
        if (cursor.getInt(cursor.getColumnIndex(DatabaseClass.COLUMN_QUANTITY)) <= 9)
            numIcon.setImageResource(icon[cursor.getInt(cursor.getColumnIndex(DatabaseClass.COLUMN_QUANTITY))]);
        else
            numIcon.setImageResource(R.drawable.num_plus);

        ImageView boximg = (ImageView) view.findViewById(R.id.box);
        boximg.setImageResource(box[cursor.getInt(cursor.getColumnIndex(DatabaseClass.COLUMN_COLOR))]);

        final int pos = cursor.getPosition();
        view.setOnTouchListener(new View.OnTouchListener() {
                                    private static final int DEFAULT_THRESHOLD = 512;
                                    int initialX = 0;
                                    final float slop = ViewConfiguration.get(context.getApplicationContext()).getScaledTouchSlop();
                                    private final int MAX_CLICK_DURATION = 400;
                                    private final int MAX_CLICK_DISTANCE = 5;
                                    private long startClickTime;
                                    private float x1;
                                    private float y1;
                                    private float x2;
                                    private float y2;
                                    private float dx;
                                    private float dy;
                                    boolean called;

                                    public boolean onTouch(final View view, MotionEvent event) {
                                        switch (event.getAction()) {
                                            case MotionEvent.ACTION_DOWN: {
                                                called = false;
                                                startClickTime = Calendar.getInstance().getTimeInMillis();
                                                x1 = event.getX();
                                                y1 = event.getY();
                                                initialX = (int) event.getX();
                                                view.setPadding(0, 0, 0, 0);
                                                break;
                                            }
                                            case MotionEvent.ACTION_UP: {
                                                called = false;
                                                long clickDuration = Calendar.getInstance().getTimeInMillis() - startClickTime;
                                                x2 = event.getX();
                                                y2 = event.getY();
                                                dx = x2 - x1;
                                                dy = y2 - y1;

                                                if (clickDuration < MAX_CLICK_DURATION && dx < MAX_CLICK_DISTANCE && dy < MAX_CLICK_DISTANCE) {
                                                    cursor.moveToPosition(pos);
                                                    mAdapterCallback.onMethodCallback(1, cursor.getString(cursor.getColumnIndex(DatabaseClass.COLUMN_NAME)));
                                                    //Log.v("", "On Item Clicked:: ");
                                                }
                                                ValueAnimator animator = ValueAnimator.ofInt(view.getPaddingLeft(), 0);

                                                animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                                                    @Override
                                                    public void onAnimationUpdate(ValueAnimator valueAnimator) {
                                                        view.setPadding((Integer) valueAnimator.getAnimatedValue(), 0, 0, 0);
                                                    }
                                                });
                                                animator.setDuration(150);
                                                animator.start();
                                                break;
                                            }
                                            case MotionEvent.ACTION_MOVE: {
                                                int currentX = (int) event.getX();
                                                int offset = currentX - initialX;
                                                if (Math.abs(offset) > slop) {
                                                    view.setPadding(offset, 0, 0, 0);

                                                    if (offset > DEFAULT_THRESHOLD) {
                                                        if (!called) {
                                                               /* When a class is swiped from left to right, delete the class, and its assignments */
                                                            cursor.moveToPosition(pos);
                                                            Class mClass = new Class(cursor.getString(cursor.getColumnIndex(DatabaseClass.COLUMN_NAME)),
                                                                    cursor.getInt(cursor.getColumnIndex(DatabaseClass.COLUMN_QUANTITY)),
                                                                    cursor.getInt(cursor.getColumnIndex(DatabaseClass.COLUMN_COLOR)));

                                                               /* Deleting the class. */
                                                            DatabaseClass db = new DatabaseClass(context, null, null, 1);
                                                            db.deleteClass(mClass.getClassName());
                                                            db.close();

                                                               /* Deleting the associating assignments */
                                                            DatabaseAssignment dbAssignment = new DatabaseAssignment(context, null, null, 1);
                                                            Cursor c = dbAssignment.getAssignments(mClass.getClassName());
                                                            if (c.moveToFirst()) {
                                                                do {
                                                                    dbAssignment.deleteAssignment(c.getString(c.getColumnIndex(DatabaseAssignment.COLUMN_NAME)));
                                                                } while (c.moveToNext());
                                                            }
                                                            c.close();
                                                            dbAssignment.close();

                                                            Toast.makeText(context, mClass.getClassName() + " deleted", Toast.LENGTH_SHORT).show();
                                                            called = true;

                                                            mAdapterCallback.onMethodCallback(0, mClass.getClassName());
                                                        }
                                                    } else if (offset < -DEFAULT_THRESHOLD) {
                                                        if (!called) {
                                                            called = true;
                                                            cursor.moveToPosition(pos);
                                                            Class mClass = new Class(cursor.getString(cursor.getColumnIndex(DatabaseClass.COLUMN_NAME)),
                                                                    cursor.getInt(cursor.getColumnIndex(DatabaseClass.COLUMN_QUANTITY)),
                                                                    cursor.getInt(cursor.getColumnIndex(DatabaseClass.COLUMN_COLOR)));
                                                        /* Open class edit dialog. */
                                                            mAdapterCallback.onMethodCallback(2, mClass.getClassName());
                                                        }
                                                    }
                                                }
                                                break;
                                            }
                                            case MotionEvent.ACTION_CANCEL: {
                                                /* Animating back. */
                                                ValueAnimator animator = ValueAnimator.ofInt(view.getPaddingLeft(), 0);

                                                animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                                                    @Override
                                                    public void onAnimationUpdate(ValueAnimator valueAnimator) {
                                                        view.setPadding((Integer) valueAnimator.getAnimatedValue(), 0, 0, 0);
                                                    }
                                                });
                                                animator.setDuration(150);
                                                animator.start();
                                            }
                                        }
                                        return true;
                                    }
                                }
        );
    }

    public interface AdapterCallback {
        void onMethodCallback(int call, String pClass);
    }
}
