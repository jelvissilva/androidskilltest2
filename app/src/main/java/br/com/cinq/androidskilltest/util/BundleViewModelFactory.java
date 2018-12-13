package br.com.cinq.androidskilltest.util;

import android.app.Application;
import android.os.Bundle;

import java.lang.reflect.InvocationTargetException;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

public class BundleViewModelFactory implements ViewModelProvider.Factory {
    private Application mApplication;
    private Bundle bundle;


    public BundleViewModelFactory(Application application, Bundle bundle) {
        mApplication = application;

        if (bundle == null) {
            this.bundle = new Bundle();
        } else {
            this.bundle = (Bundle) bundle.clone();
        }

    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {

        try {
            return modelClass.getConstructor(Application.class, Bundle.class).newInstance(mApplication, bundle);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException("Cannot create an instance of " + modelClass, e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException("Cannot create an instance of " + modelClass, e);
        } catch (InstantiationException e) {
            throw new RuntimeException("Cannot create an instance of " + modelClass, e);
        } catch (InvocationTargetException e) {
            throw new RuntimeException("Cannot create an instance of " + modelClass, e);
        }

    }
}
