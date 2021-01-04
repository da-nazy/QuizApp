package app.datableed.myapplication;

import androidx.lifecycle.ViewModel;

public class timerViewModel extends ViewModel {
    public long tiemLeftInMilis=0;

    public long getTiemLeftInMilis() {
        return tiemLeftInMilis;
    }

    public void setTiemLeftInMilis(long tiemLeftInMilis) {
        this.tiemLeftInMilis = tiemLeftInMilis;
    }



}
