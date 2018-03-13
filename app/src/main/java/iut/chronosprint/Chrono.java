package iut.chronosprint;

import android.os.Bundle;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Chronometer;

import java.util.Timer;

public class Chrono extends AppCompatActivity{

    private Chronometer Clock;

    private boolean isStarted;

    private boolean isRunningMode;

    private Timer t;

    public boolean isStarted() {
        return isStarted;
    }

    public boolean isRunningMode() {
        return isRunningMode;
    }

    public Chrono(Chronometer c) {
        Clock=c;
    }

    public Chronometer getClock() {
        return Clock;
    }

    public void setStarted(boolean started) {
        isStarted = started;
    }

    public void setRunningMode(boolean runningMode) {
        isRunningMode = runningMode;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        t= new Timer();  //BUT WHY?
        Log.i("ChronoActivity","Created");
    }

    public void startChrono(){
        isStarted=true;
        final long delay=1000; // BUT WHY?
        Clock.setBase(SystemClock.elapsedRealtime());
        Clock.start();
        //Toast.makeText(getBaseContext(),"GO",
        //Toast.LENGTH_LONG).show();
        //if(t==null) t=new Timer();
        //t.schedule(new TimerTask() {
        //    @Override
        //    public void run() {
        //        Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        //        if (v != null && v.hasVibrator()) {
        //            v.vibrate(delay/2);
        //        }
        //    }
        //},delay);
    }

    public void stopChrono(){
        if(isStarted) {
            isStarted = false;
            Clock.stop();
            /*Toast.makeText(getBaseContext(), "GG",
                    Toast.LENGTH_LONG).show();*/
        }
    }

    public void resetChrono(){
        stopChrono();
        getClock().setBase(SystemClock.elapsedRealtime());
        getClock().setText(R.string.initTime);
        MainWindows.message="";
    }
}
