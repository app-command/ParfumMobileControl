package parfum.koreatech.parfummobilecontrol;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.sql.Ref;
import java.util.Vector;

public class SubActivity2 extends AppCompatActivity {

    Vector<String> vetor = new Vector<String>(3);
    private ImageView imageView;
    private TextView textView;
    private Button saveBtne;
    private Button saveBtnw;
    private Button saveBtse;
    private Button saveBtsw;
    private Bitmap bitmap;
    private int r,g,b;
    private String hex_r,hex_g,hex_b;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference usRef = database.getReference("user_setting");
    DatabaseReference runningRef = database.getReference("running");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub2);
        initColor();
        imageView = (ImageView) findViewById(R.id.colorView);
        textView = (TextView) findViewById(R.id.colorText);
        saveBtne = (Button) findViewById(R.id.save);
        saveBtnw = (Button) findViewById(R.id.save2);
        saveBtse = (Button) findViewById(R.id.save3);
        saveBtsw = (Button) findViewById(R.id.save4);

        imageView.setDrawingCacheEnabled(true);
        imageView.buildDrawingCache(true);

        imageView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN || motionEvent.getAction() == MotionEvent.ACTION_MOVE) {
                    bitmap = imageView.getDrawingCache();
                    int pixel = bitmap.getPixel((int) motionEvent.getX(), (int) motionEvent.getY());

                    r = Color.red(pixel);
                    g = Color.green(pixel);
                    b = Color.blue(pixel);

                    hex_r = Integer.toHexString(r);     // 16진수로 변환!
                    hex_g = Integer.toHexString(g);
                    hex_b = Integer.toHexString(b);

                    // 0이나 한 자리수로 나오는 현상 해결!
                    if(r==0)
                        hex_r="00";
                    if(r!=0 && r<=16)
                        hex_r = "0".concat(hex_r);
                    if(g==0)
                        hex_g="00";
                    if(g!=0 && g<=16)
                        hex_g = "0".concat(hex_g);
                    if(b==0)
                        hex_b="00";
                    if(b!=0 && b<=16)
                        hex_b = "0".concat(hex_b);

                    textView.setBackgroundColor(Color.rgb(r, g, b));
                    textView.setText("R(" + r + ")\n" + "G(" + g + ")\n" + "B(" + b + ")");

                    vetor.addElement("r");
                    vetor.addElement("g");
                    vetor.addElement("b");
                }
                return true;
            }
        });
        saveBtne.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(r==0 && g == 00 && b == 0)
                    return;
                usRef.child("northeast").setValue("#"+hex_r+hex_g+hex_b);
                saveBtne.setBackgroundColor(Color.rgb(r, g, b));

                // 쓰는 과정
            }
        });
        saveBtnw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(r==0 && g == 00 && b == 0)
                    return;
                usRef.child("northwest").setValue("#"+hex_r+hex_g+hex_b);
                saveBtnw.setBackgroundColor(Color.rgb(r, g, b));
            }
        });
        saveBtse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(r==0 && g == 00 && b == 0)
                    return;
                usRef.child("southeast").setValue("#"+hex_r+hex_g+hex_b);
                saveBtse.setBackgroundColor(Color.rgb(r, g, b));
            }
        });
        saveBtsw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(r==0 && g == 00 && b == 0)
                    return;
                usRef.child("southwest").setValue("#"+hex_r+hex_g+hex_b);
                saveBtsw.setBackgroundColor(Color.rgb(r, g, b));
            }
        });
        }

    private void initColor() {          // 서버에 저장했던 색 불러오기
        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Get Post object and use the values to update the UI
                String colors;
                int RGBs[] = new int[3];

                colors = dataSnapshot.child("northeast").getValue(String.class);
                Log.d("brain",colors);
                for(int j=0;j<3;j++){
                    RGBs[j] = Integer.parseInt(colors.substring(j*2+1,j*2+3),16);
                    Log.d("brain2",RGBs[j]+"/");
                }
                saveBtne.setBackgroundColor(Color.rgb(RGBs[0], RGBs[1], RGBs[2]));

                colors = dataSnapshot.child("northwest").getValue(String.class);
                for(int j=0;j<3;j++){
                    RGBs[j] = Integer.parseInt(colors.substring(j*2+1,j*2+3),16);
                }
                saveBtnw.setBackgroundColor(Color.rgb(RGBs[0], RGBs[1], RGBs[2]));

                colors = dataSnapshot.child("southeast").getValue(String.class);
                for(int j=0;j<3;j++){
                    RGBs[j] = Integer.parseInt(colors.substring(j*2+1,j*2+3),16);
                }
                saveBtse.setBackgroundColor(Color.rgb(RGBs[0], RGBs[1], RGBs[2]));

                colors = dataSnapshot.child("southwest").getValue(String.class);
                for(int j=0;j<3;j++){
                    RGBs[j] = Integer.parseInt(colors.substring(j*2+1,j*2+3),16);
                }
                saveBtsw.setBackgroundColor(Color.rgb(RGBs[0], RGBs[1], RGBs[2]));
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w("error", "loadPost:onCancelled", databaseError.toException());
            }
        };
        usRef.addValueEventListener(postListener);    // 향수 정보를 가져오는 과정이다.
    }
}

