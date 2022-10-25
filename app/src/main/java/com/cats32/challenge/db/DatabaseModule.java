package com.cats32.challenge.db;

import android.app.Application;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.components.SingletonComponent;

@Module
@InstallIn(SingletonComponent.class)
public class DatabaseModule {

   @Provides
   @Singleton
   MyDatabase provideDatabase(Application application) {
      return DatabaseBuilder.build(application);
   }

   @Provides
   @Singleton
   PointsDao providePointsDao(MyDatabase database) {
      return database.pointsDao();
   }
}
