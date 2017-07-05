package com.d.wordsearch;

import android.app.Service;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Build;
import android.os.IBinder;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

public class FloatViewService extends Service {

    private WindowManager windowManager;
    private View mFloatingView;

    public FloatViewService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        mFloatingView= LayoutInflater.from(this).inflate(R.layout.floating_view_layout,null);

        final WindowManager.LayoutParams params=new WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.TYPE_PHONE,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT
        );

        params.gravity= Gravity.TOP|Gravity.LEFT;
        params.x=0;
        params.y=100;


        windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
        windowManager.addView(mFloatingView,params);

        final View collapseView=mFloatingView.findViewById(R.id.collapse_view);
        final View expandedView=mFloatingView.findViewById(R.id.expanded_container);

        ImageView closeButtonCollapsed=(ImageView) mFloatingView.findViewById(R.id.close_btn);
        closeButtonCollapsed.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                stopSelf();
            }
        });

        Button floatWordSearchButton=(Button) mFloatingView.findViewById(R.id.float_word_search_button);
        floatWordSearchButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Toast.makeText(FloatViewService.this, "Word Search Button", Toast.LENGTH_SHORT).show();
            }
        });

        Button floatOtherSearchButton=(Button) mFloatingView.findViewById(R.id.float_other_search_button);
        floatOtherSearchButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Toast.makeText(FloatViewService.this, "Other Search Button", Toast.LENGTH_SHORT).show();
            }
        });

        EditText floatEditText=(EditText) mFloatingView.findViewById(R.id.float_search_edit_text);
        floatEditText.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                collapseView.setVisibility(View.VISIBLE);
                expandedView.setVisibility(View.GONE);
            }
        });

        Button floatPermissionButton=(Button) mFloatingView.findViewById(R.id.float_get_permission);
        floatPermissionButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(FloatViewService.this,FloatPermissions.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);

                stopSelf();
            }
        });


        mFloatingView.findViewById(R.id.root_container).setOnTouchListener(new View.OnTouchListener() {
            private int initialX;
            private int initialY;
            private float initialTouchX;
            private float initialTouchY;


            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:


                        //remember the initial position.
                        initialX = params.x;
                        initialY = params.y;


                        //get the touch location
                        initialTouchX = event.getRawX();
                        initialTouchY = event.getRawY();
                        return true;

                    case MotionEvent.ACTION_UP:
                        int Xdiff = (int) (event.getRawX() - initialTouchX);
                        int Ydiff = (int) (event.getRawY() - initialTouchY);


                        //The check for Xdiff <10 && YDiff< 10 because sometime elements moves a little while clicking.
                        //So that is click event.
                        if (Xdiff < 10 && Ydiff < 10) {
                            if (isViewCollapsed()) {
                                //When user clicks on the image view of the collapsed layout,
                                //visibility of the collapsed layout will be changed to "View.GONE"
                                //and expanded view will become visible.
                                collapseView.setVisibility(View.GONE);
                                expandedView.setVisibility(View.VISIBLE);
                            }
                        }
                        return true;

                    case MotionEvent.ACTION_MOVE:
                        //Calculate the X and Y coordinates of the view.
                        params.x = initialX + (int) (event.getRawX() - initialTouchX);
                        params.y = initialY + (int) (event.getRawY() - initialTouchY);


                        //Update the layout with new X & Y coordinate
                        windowManager.updateViewLayout(mFloatingView, params);
                        return true;
                }
                return false;
            }
        });

    }

    private boolean isViewCollapsed() {
        return mFloatingView == null || mFloatingView.findViewById(R.id.collapse_view).getVisibility() == View.VISIBLE;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(mFloatingView!=null) windowManager.removeView(mFloatingView);
    }
}
