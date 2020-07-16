package parfum.koreatech.parfummobilecontrol;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.RemoteMessage;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    TextView tv1,tv2;
    Button btn1,btn2,btn3;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference stateRef = database.getReference("state");
    DatabaseReference runningRef = database.getReference("running");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Intent intent = new Intent(this, LoadingActivity.class);
        startActivity(intent);
        tv1 = (TextView) findViewById(R.id.water);
        tv2 = (TextView) findViewById(R.id.perfume);
        btn1 = (Button) findViewById(R.id.btn1);
        btn1.setOnClickListener(new Button.OnClickListener(){
            public void onClick(View view){
//                Intent intent = new Intent(MainActivity.this,GoodsActivity.class);
//                startActivity(intent);
                Intent intent = new Intent(MainActivity.this,WebviewActivity.class);
                startActivity(intent);
            }
        });
        btn2 = (Button) findViewById(R.id.btn2);
        btn2.setOnClickListener(new Button.OnClickListener(){
            public void onClick(View view){
                Intent intent = new Intent(MainActivity.this,SubActivity1.class);
                startActivity(intent);
            }
        });
        btn3 = (Button) findViewById(R.id.btn3);
        btn3.setOnClickListener(new Button.OnClickListener(){
            public void onClick(View view){
                Intent intent = new Intent(MainActivity.this,SubActivity2.class);
                startActivity(intent);
            }
        });
        Button logTokenButton = (Button)findViewById(R.id.logTokenButton);
        logTokenButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get token
                // [START retrieve_current_token]
                FirebaseInstanceId.getInstance().getInstanceId()
                        .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                            @Override
                            public void onComplete(@NonNull Task<InstanceIdResult> task) {
                                if (!task.isSuccessful()) {
                                    Log.w("MyFirebaseMsgService", "getInstanceId failed", task.getException());
                                    return;
                                }

                                // Get new Instance ID token
                                String token = task.getResult().getToken();

                                // Log and toast
                                String msg = getString(R.string.msg_token_fmt, token);
                                Log.d("MyFirebaseMsgService", msg);
                                Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();
                            }
                        });
                // [END retrieve_current_token]
            }
        });


    } //end onCreate()
    public void onStart() {
        super.onStart();
        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Get Post object and use the values to update the UI
                String content = dataSnapshot.child("water").getValue(String.class);        // 읽어오는 과정
                tv1.setText(content);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.w("error", "loadPost:onCancelled", databaseError.toException());
                // ...
            }
        };
        stateRef.addValueEventListener(postListener);    // 향수 정보를 가져오는 과정이다.

        ValueEventListener postListener1 = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Get Post object and use the values to update the UI
                String content = dataSnapshot.child("perfume").getValue(String.class);        // 읽어오는 과정
                tv2.setText(content);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.w("error", "loadPost:onCancelled", databaseError.toException());
                // ...
            }
        };
        runningRef.addValueEventListener(postListener1);    // 향수 정보를 가져오는 과정이다.
    } // end onStart();

}