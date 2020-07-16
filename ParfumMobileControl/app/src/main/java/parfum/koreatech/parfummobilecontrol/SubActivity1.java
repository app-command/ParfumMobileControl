package parfum.koreatech.parfummobilecontrol;

import android.app.Fragment;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.sql.Ref;

public class SubActivity1 extends AppCompatActivity {

    ImageButton nw, ne, sw, se;
    Button bts1, bts2;
    TextView et, et2, tv1, tv2, tv3, tv4;
    static String now, chk_h, chk_l, chk_d = null;
    static String perfume_name[] = new String[4];
    static Long timeStr1, timeStr2, str1, str2;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference runningRef = database.getReference("running");
    DatabaseReference locRef = database.getReference("diffuser");
    DatabaseReference userRef = database.getReference("user_setting");
    DatabaseReference stateRef = database.getReference("state");
    DatabaseReference logRef = database.getReference("log");
    DatabaseReference waterRef = database.getReference("water_control");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub1);
        Intent intent = new Intent(this.getIntent());
        checkElapsedTime();
        initSetting();
        realTime rt = new realTime();       // 폴링 스레드 구현
        rt.start();
        nw = (ImageButton) findViewById(R.id.nw);
        nw.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View view) {
                if (now.equals("northwest") && chk_d.equals("on")) {
                    nw.setBackgroundResource(R.drawable.off);
                    runningRef.child("mode").child("D").setValue("off");            // 작동 중지
                    stateRef.child("perfume").child("northwest").setValue(-1);
                    et2.setText("Turn off");
                } else {
                    ne.setBackgroundResource(R.drawable.off);
                    se.setBackgroundResource(R.drawable.off);
                    sw.setBackgroundResource(R.drawable.off);
                    nw.setBackgroundResource(R.drawable.on);
                    runningRef.child("perfume").setValue("northwest");
                    runningRef.child("mode").child("D").setValue("on");
                    stateRef.child("perfume").child("northwest").setValue(System.currentTimeMillis());          // 켠 시간 구하기
                    logRef.child("diffuser").child(System.currentTimeMillis() + "").setValue(perfume_name[0]);
                }
            }
        });
        ne = (ImageButton) findViewById(R.id.ne);
        ne.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View view) {
                if (now.equals("northeast") && chk_d.equals("on")) {
                    ne.setBackgroundResource(R.drawable.off);
                    runningRef.child("mode").child("D").setValue("off");                          // 쓰는 과정
                    stateRef.child("perfume").child("northwest").setValue(-1);          // 꺼버리기 시간 구하기
                    et2.setText("Turn off");
                } else {
                    nw.setBackgroundResource(R.drawable.off);
                    se.setBackgroundResource(R.drawable.off);
                    sw.setBackgroundResource(R.drawable.off);
                    ne.setBackgroundResource(R.drawable.on);
                    runningRef.child("perfume").setValue("northeast");                          // 쓰는 과정
                    runningRef.child("mode").child("D").setValue("on");                          // 쓰는 과정
                    stateRef.child("perfume").child("northwest").setValue(System.currentTimeMillis());          // 켠 시간 구하기
                    logRef.child("diffuser").child(System.currentTimeMillis() + "").setValue(perfume_name[1]);
                }
            }
        });
        sw = (ImageButton) findViewById(R.id.sw);
        sw.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View view) {
                if (now.equals("southwest") && chk_d.equals("on")) {
                    sw.setBackgroundResource(R.drawable.off);
                    runningRef.child("mode").child("D").setValue("off");                          // 쓰는 과정
                    stateRef.child("perfume").child("northwest").setValue(-1);          // 꺼버리기 시간 구하기
                    et2.setText("Turn off");
                } else {
                    ne.setBackgroundResource(R.drawable.off);
                    se.setBackgroundResource(R.drawable.off);
                    nw.setBackgroundResource(R.drawable.off);
                    sw.setBackgroundResource(R.drawable.on);
                    runningRef.child("perfume").setValue("southwest");                          // 쓰는 과정
                    runningRef.child("mode").child("D").setValue("on");                          // 쓰는 과정
                    stateRef.child("perfume").child("northwest").setValue(System.currentTimeMillis());          // 켠 시간 구하기
                    logRef.child("diffuser").child(System.currentTimeMillis() + "").setValue(perfume_name[2]);
                }
            }
        });
        se = (ImageButton) findViewById(R.id.se);
        se.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View view) {
                if (now.equals("southeast") && chk_d.equals("on")) {
                    se.setBackgroundResource(R.drawable.off);
                    runningRef.child("mode").child("D").setValue("off");                          // 쓰는 과정
                    stateRef.child("perfume").child("northwest").setValue(-1);          // 꺼버리기 시간 구하기
                    et2.setText("Turn off");
                } else {
                    ne.setBackgroundResource(R.drawable.off);
                    nw.setBackgroundResource(R.drawable.off);
                    sw.setBackgroundResource(R.drawable.off);
                    se.setBackgroundResource(R.drawable.on);
                    runningRef.child("perfume").setValue("southeast");                          // 쓰는 과정
                    runningRef.child("mode").child("D").setValue("on");                          // 쓰는 과정
                    stateRef.child("perfume").child("northwest").setValue(System.currentTimeMillis());          // 켠 시간 구하기
                    logRef.child("diffuser").child(System.currentTimeMillis() + "").setValue(perfume_name[3]);
                }
            }
        });
        bts1 = (Button) findViewById(R.id.btn_l);           //가습기 관련 H
        bts1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (chk_h.equals("off")) {
                    bts1.setBackgroundResource(R.drawable.heaton);
                    waterRef.setValue("on");
                    stateRef.child("elapsed").setValue(System.currentTimeMillis());
                    logRef.child("heater").child(System.currentTimeMillis() + "").setValue("time");
                }
                if (chk_h.equals("on")) {
                    bts1.setBackgroundResource(R.drawable.heatoff);
                    waterRef.setValue("off");
                    stateRef.child("elapsed").setValue(-1);
                    et.setText("Turn off");
                }
            }
        });
        bts2 = (Button) findViewById(R.id.btn_w);       // 무드등 관련 L
        bts2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (chk_l.equals("off")) {
                    bts2.setBackgroundResource(R.drawable.ledon);
                    runningRef.child("mode").child("L").setValue("on");                          // 쓰는 과정
                }
                if (chk_l.equals("on")) {
                    bts2.setBackgroundResource(R.drawable.ledoff);
                    runningRef.child("mode").child("L").setValue("off");                          // 쓰는 과정
                }
            }
        });
    }

    private class realTime extends Thread {
        @Override
        public void run() {
            while (true) {
                stateRef.child("perfume").child("southeast").setValue("ddos");
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                stateRef.child("perfume").child("southeast").setValue("dsos");
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void checkElapsedTime() {
        et = (TextView) findViewById(R.id.et);
        et2 = (TextView) findViewById(R.id.et2);
        ValueEventListener postListener_4 = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Get Post object and use the values to update the UI
                timeStr1 = dataSnapshot.child("elapsed").getValue(Long.class);        // 읽어오는 과정
                timeStr2 = dataSnapshot.child("perfume").child("northwest").getValue(Long.class);        // 읽어오는 과정
                if (timeStr1 != -1) {
                    str1 = (System.currentTimeMillis() - timeStr1);
                    et.setText((str1 / 60000) / (60) + " : " + (str1 / 60000) % (60) + " : " + (str1 / 1000) % (60));
                }
                if (timeStr2 != -1) {
                    str2 = (System.currentTimeMillis() - timeStr2);
                    et2.setText((str2 / 60000) / (60) + " : " + (str2 / 60000) % (60) + " : " + (str2 / 1000) % (60));
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.w("error", "loadPost:onCancelled", databaseError.toException());
                // ...
            }
        };
        stateRef.addValueEventListener(postListener_4);    // 실행 시간 가져오기
    }

    private void initSetting() {
        tv1 = (TextView) findViewById(R.id.tv1);
        tv2 = (TextView) findViewById(R.id.tv2);
        tv3 = (TextView) findViewById(R.id.tv3);
        tv4 = (TextView) findViewById(R.id.tv4);

        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Get Post object and use the values to update the UI
                now = dataSnapshot.child("perfume").getValue(String.class);        // 읽어오는 과정
                chk_d = dataSnapshot.child("mode").child("D").getValue(String.class);        // 읽어오는 과정
                chk_l = dataSnapshot.child("mode").child("L").getValue(String.class);        // 읽어오는 과정
                if (chk_l.equals("on")) {
                    bts2.setBackgroundResource(R.drawable.ledon);
                }
                if (chk_d.equals("on")) {
                    switch (now) {
                        case "northwest":
                            nw.setBackgroundResource(R.drawable.on);
                            break;
                        case "northeast":
                            ne.setBackgroundResource(R.drawable.on);
                            break;
                        case "southwest":
                            sw.setBackgroundResource(R.drawable.on);
                            break;
                        case "southeast":
                            se.setBackgroundResource(R.drawable.on);
                            break;
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.w("error", "loadPost:onCancelled", databaseError.toException());
                // ...
            }
        };
        runningRef.addValueEventListener(postListener);    // 향수 정보를 가져오는 과정이다.

        ValueEventListener postListener_2 = new ValueEventListener() {
            String lo_1, lo_2, lo_3, lo_4;

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Get Post object and use the values to update the UI
                lo_1 = dataSnapshot.child("location").child("northeast").getValue(String.class);        // 읽어오는 과정
                tv1.setText(lo_1);
                lo_2 = dataSnapshot.child("location").child("northwest").getValue(String.class);        // 읽어오는 과정
                tv2.setText(lo_2);
                lo_3 = dataSnapshot.child("location").child("southeast").getValue(String.class);        // 읽어오는 과정
                tv3.setText(lo_3);
                lo_4 = dataSnapshot.child("location").child("southwest").getValue(String.class);        // 읽어오는 과정
                tv4.setText(lo_4);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.w("error", "loadPost:onCancelled", databaseError.toException());
                // ...
            }
        };
        locRef.addValueEventListener(postListener_2);    // 향수 정보를 가져오는 과정이다.

        ValueEventListener postListener_3 = new ValueEventListener() {
            String lo_1, lo_2, lo_3, lo_4;

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Get Post object and use the values to update the UI
                String colors;
                int RGBs[] = new int[3];
                colors = dataSnapshot.child("northeast").getValue(String.class);
                for (int j = 0; j < 3; j++) {
                    RGBs[j] = Integer.parseInt(colors.substring(j * 2 + 1, j * 2 + 3), 16);
                    Log.d("brain2", RGBs[j] + "/");
                }
                tv1.setTextColor(Color.rgb(RGBs[0], RGBs[1], RGBs[2]));

                colors = dataSnapshot.child("northwest").getValue(String.class);
                for (int j = 0; j < 3; j++) {
                    RGBs[j] = Integer.parseInt(colors.substring(j * 2 + 1, j * 2 + 3), 16);
                }
                tv2.setTextColor(Color.rgb(RGBs[0], RGBs[1], RGBs[2]));

                colors = dataSnapshot.child("southeast").getValue(String.class);
                for (int j = 0; j < 3; j++) {
                    RGBs[j] = Integer.parseInt(colors.substring(j * 2 + 1, j * 2 + 3), 16);
                }
                tv3.setTextColor(Color.rgb(RGBs[0], RGBs[1], RGBs[2]));

                colors = dataSnapshot.child("southwest").getValue(String.class);
                for (int j = 0; j < 3; j++) {
                    RGBs[j] = Integer.parseInt(colors.substring(j * 2 + 1, j * 2 + 3), 16);
                }
                tv4.setTextColor(Color.rgb(RGBs[0], RGBs[1], RGBs[2]));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.w("error", "loadPost:onCancelled", databaseError.toException());
                // ...
            }
        };
        userRef.addValueEventListener(postListener_3);    // 향수 정보를 가져오는 과정이다.

        ValueEventListener postListener_4 = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Get Post object and use the values to update the UI
                chk_h = dataSnapshot.getValue(String.class);        // 읽어오는 과정
                if (chk_h.equals("on")) {
                    bts1.setBackgroundResource(R.drawable.heaton);
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.w("error", "loadPost:onCancelled", databaseError.toException());
                // ...
            }
        };
        waterRef.addValueEventListener(postListener_4);    // 가습기 정보만 가져오는 과정.

        ValueEventListener postListener_5 = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Get Post object and use the values to update the UI
                perfume_name[0] = dataSnapshot.child("location").child("northeast").getValue(String.class);
                perfume_name[1] = dataSnapshot.child("location").child("northwest").getValue(String.class);
                perfume_name[2] = dataSnapshot.child("location").child("southeast").getValue(String.class);
                perfume_name[3] = dataSnapshot.child("location").child("southwest").getValue(String.class);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.w("error", "loadPost:onCancelled", databaseError.toException());
                // ...
            }
        };
        locRef.addValueEventListener(postListener_5);    // 방 위치에 맞는 향 이름을 가져오는 과정이다.
    }
}
