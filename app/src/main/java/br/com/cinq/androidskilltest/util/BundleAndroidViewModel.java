package br.com.cinq.androidskilltest.util;

import android.app.Application;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

public class BundleAndroidViewModel extends AndroidViewModel {

    private Bundle bundle;

    public BundleAndroidViewModel(@NonNull Application application, Bundle bundle) {
        super(application);
        this.bundle = bundle;
    }

    protected Bundle getBundle() {
        return bundle;
    }

    protected void setBundle(Bundle bundle) {
        this.bundle = bundle;
    }
}
