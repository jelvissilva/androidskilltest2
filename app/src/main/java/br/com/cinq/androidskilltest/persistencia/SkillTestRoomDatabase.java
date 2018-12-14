package br.com.cinq.androidskilltest.persistencia;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {Usuario.class}, version = 2)
public abstract class SkillTestRoomDatabase extends RoomDatabase {

    private final static String DATABASE_NAME = "skilltest_database";

    public abstract UsuarioDao usuarioDao();

    private static SkillTestRoomDatabase INSTANCE;

    public static SkillTestRoomDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (SkillTestRoomDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE =
                            Room.databaseBuilder(context.getApplicationContext(),
                                    SkillTestRoomDatabase.class, DATABASE_NAME)
                                    .fallbackToDestructiveMigration()
                                    .build();

                }
            }
        }
        return INSTANCE;
    }
}