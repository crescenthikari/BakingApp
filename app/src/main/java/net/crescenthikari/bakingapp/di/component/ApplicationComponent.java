package net.crescenthikari.bakingapp.di.component;

import android.app.Application;

import net.crescenthikari.bakingapp.BakingApp;
import net.crescenthikari.bakingapp.di.module.ActivityBuilder;
import net.crescenthikari.bakingapp.di.module.ApiModule;
import net.crescenthikari.bakingapp.di.module.ApplicationModule;
import net.crescenthikari.bakingapp.di.module.BroadcastReceiverBuilder;
import net.crescenthikari.bakingapp.di.module.FragmentBuilder;
import net.crescenthikari.bakingapp.di.module.PicassoModule;
import net.crescenthikari.bakingapp.di.module.RecipeRepositoryModule;

import javax.inject.Singleton;

import dagger.BindsInstance;
import dagger.Component;
import dagger.android.support.AndroidSupportInjectionModule;

/**
 * Created by Muhammad Fiqri Muthohar on 8/8/17.
 */
@Singleton
@Component(modules = {
        ApplicationModule.class,
        AndroidSupportInjectionModule.class,
        ActivityBuilder.class,
        FragmentBuilder.class,
        BroadcastReceiverBuilder.class,
        PicassoModule.class,
        RecipeRepositoryModule.class,
        ApiModule.class
})
public interface ApplicationComponent {
    void inject(BakingApp application);

    @Component.Builder
    interface Builder {
        @BindsInstance
        Builder application(Application application);

        ApplicationComponent build();
    }
}
