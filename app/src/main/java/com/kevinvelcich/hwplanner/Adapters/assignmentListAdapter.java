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
import android.widget.TextView;
import android.widget.Toast;
import com.kevinvelcich.hwplanner.*;
import com.kevinvelcich.hwplanner.Classes.*;
import com.kevinvelcich.hwplanner.Database.DatabaseAssignment;
import com.kevinvelcich.hwplanner.Database.DatabaseClass;

import java.util.Calendar;

public class assignmentListAdapter extends CursorAdapter {

    private AdapterCallback mAdapterCallback;

    public assignmentListAdapter(Context context, Cursor cursor, int flags, AdapterCallback callback) {
        super(context, cursor, 0);
        this.mAdapterCallback = callback;
    }

    TextView title;
    TextView description;
    TextView date;

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        View view = LayoutInflater.from(context).inflate(R.layout.listview_assignments, parent, false);
        title = (TextView)view.findViewById(R.id.title);
        description = (TextView) view.findViewById(R.id.description);
        date = (TextView) view.findViewById(R.id.dueDate);
        return view;
    }

    @Override
    public void bindView(View view, final Context context, final Cursor cursor) {
        /* Setting the title, date and description from the assignment class passsed in. */
        title.setText(cursor.getString(cursor.getColumnIndex(DatabaseAssignment.COLUMN_NAME)));
        description.setText(cursor.getString(cursor.getColumnIndex(DatabaseAssignment.COLUMN_DESCRIPTION)));

        if (title.getText().equals("") || title.getText() == null) {
            TextView dots = (TextView) view.findViewById(R.id.dots);
            dots.setText("");
        }

        final int pos = cursor.getPosition();
        cursor.moveToPosition(pos);

        if (cursor.getInt(cursor.getColumnIndex(DatabaseAssignment.COLUMN_DAY)) != 0)
            date.setText(cursor.getString(cursor.getColumnIndex(DatabaseAssignment.COLUMN_MONTH)) + "/" +
                    cursor.getString(cursor.getColumnIndex(DatabaseAssignment.COLUMN_DAY)) + "/" +
                    cursor.getString(cursor.getColumnIndex(DatabaseAssignment.COLUMN_YEAR)));

        view.setOnTouchListener(new View.OnTouchListener() {
                                    private static final int DEFAULT_THRESHOLD = 528;
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
                                                    mAdapterCallback.onMethodCallback(1, new Assignment(cursor.getString(cursor.getColumnIndex(DatabaseAssignment.COLUMN_NAME)),
                                                            cursor.getString(cursor.getColumnIndex(DatabaseAssignment.COLUMN_DESCRIPTION)),
                                                            cursor.getInt(cursor.getColumnIndex(DatabaseAssignment.COLUMN_YEAR)),
                                                            cursor.getInt(cursor.getColumnIndex(DatabaseAssignment.COLUMN_MONTH)),
                                                            cursor.getInt(cursor.getColumnIndex(DatabaseAssignment.COLUMN_DAY))));
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
                                                        /* Deleting the class, when swiped right to left. */
                                                        DatabaseAssignment db = new DatabaseAssignment(context, null, null, 1);
                                                        cursor.moveToPosition(pos);
                                                        db.deleteAssignment(cursor.getString(cursor.getColumnIndex(DatabaseAssignment.COLUMN_NAME)));
                                                        db.close();

                                                        DatabaseClass dbClass = new DatabaseClass(context, null, null ,1);
                                                        dbClass.decrementQuantity(cursor.getString(cursor.getColumnIndex(DatabaseAssignment.COLUMN_CLASS)));
                                                        dbClass.close();

                                                        if (!called) {
                                                            Toast.makeText(context, cursor.getString(cursor.getColumnIndex(DatabaseAssignment.COLUMN_NAME)) + " deleted", Toast.LENGTH_SHORT).show();
                                                            called = true;
                                                        }
                                                        mAdapterCallback.onMethodCallback(0, new Assignment(cursor.getString(cursor.getColumnIndex(DatabaseAssignment.COLUMN_CLASS))));
                                                    } else if (offset < -DEFAULT_THRESHOLD) {
                                                        /* Open edit dialog. */
                                                        if (!called) {
                                                            cursor.moveToPosition(pos);
                                                            mAdapterCallback.onMethodCallback(2, new Assignment(cursor.getString(cursor.getColumnIndex(DatabaseAssignment.COLUMN_NAME)),
                                                                    cursor.getString(cursor.getColumnIndex(DatabaseAssignment.COLUMN_DESCRIPTION)),
                                                                    cursor.getInt(cursor.getColumnIndex(DatabaseAssignment.COLUMN_YEAR)),
                                                                    cursor.getInt(cursor.getColumnIndex(DatabaseAssignment.COLUMN_MONTH)),
                                                                    cursor.getInt(cursor.getColumnIndex(DatabaseAssignment.COLUMN_DAY))));
                                                            called = true;
                                                        }

                                                    }
                                                }
                                                break;
                                            }
                                            case MotionEvent.ACTION_CANCEL: {
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
        void onMethodCallback(int call, Assignment a);
    }
}
