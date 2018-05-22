package ba.unsa.etf.rma.nedim_tarakcija.rma18tarakija16925;

import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;

public class MojResultReceiver extends android.os.ResultReceiver {

    public interface Receiver {
        public void onReceiveResults(int resultCode, Bundle resultData);
    }

    private Receiver mReceiver;

    public MojResultReceiver(Handler handler) {
        super(handler);
    }

    public void setmReceiver(Receiver receiver) {
        this.mReceiver = receiver;
    }

    @Override
    protected void onReceiveResult(int resultCode, Bundle resultData) {
        if(mReceiver != null) {
            mReceiver.onReceiveResults(resultCode, resultData);
        }
    }
}
