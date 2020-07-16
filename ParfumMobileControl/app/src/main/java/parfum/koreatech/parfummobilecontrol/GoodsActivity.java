package parfum.koreatech.parfummobilecontrol;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class GoodsActivity extends AppCompatActivity {

    TextView tv;
    Button btn1;
    LinearLayout linearLayout;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference listRef = database.getReference("perfume_list");
    String colorBasket[] = new String[100];
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);    // 레이아웃 생성
        setContentView(R.layout.activity_goods);
        linearLayout = (LinearLayout)findViewById(R.id.llayout);
//        FrameLayout.LayoutParams pm = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT); //레이아웃파라미터 생성
//        linearLayout.setLayoutParams(pm);
        Intent intent = new Intent(this.getIntent());
        tv = (TextView)findViewById(R.id.tv);
        btn1 = (Button)findViewById(R.id.go_webview);
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(GoodsActivity.this,WebviewActivity.class);
                startActivity(intent);
            }
        });

        // firebase에서 향수 품목 정보 받아오기
        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Get Post object and use the values to update the UI
                String perfume = "perfume";
               long childcount = dataSnapshot.getChildrenCount();
                for(int i=1;i<=childcount;i++){
                    final String temp1 = dataSnapshot.child(perfume+""+i).child("name").getValue(String.class);        // 읽어오는 과정
                    String temp2 = dataSnapshot.child(perfume+""+i).child("color").getValue(String.class);        // 읽어오는 과정
                    String temp3 = dataSnapshot.child(perfume+""+i).child("purpose").getValue(String.class);        // 읽어오는 과정
                    Button btn = new Button(getApplicationContext());
                    btn.setText("향수명: "+temp1+"\n용도: "+temp3);
                    btn.setTextSize(20);
                    btn.setTextColor(Color.parseColor("#FFFFFF"));
                    btn.setBackgroundColor(Color.parseColor(temp2));
                    btn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            // 구매 창 (사진+가격) 띄우기 (Fragment)
                            Toast.makeText(getApplicationContext(),temp1+"을(를) 구매했습니다.",Toast.LENGTH_SHORT).show();
                        }
                    });
                    linearLayout.addView(btn);
                }                                                    // 화면에 출력
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.w("error", "loadPost:onCancelled", databaseError.toException());
                // ...
            }
        };
        listRef.addValueEventListener(postListener);    // 향수 정보를 가져오는 과정이다.
    }
}
