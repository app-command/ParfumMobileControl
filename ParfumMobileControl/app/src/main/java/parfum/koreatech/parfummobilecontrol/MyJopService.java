package parfum.koreatech.parfummobilecontrol;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class MyJopService extends Service {
    public MyJopService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
