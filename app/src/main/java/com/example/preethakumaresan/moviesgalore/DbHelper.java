package com.example.preethakumaresan.moviesgalore;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.RuntimeExceptionDao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;


/**
 * Created by PREETHA KUMARESAN on 20-07-2016.
 */
public class DbHelper extends OrmLiteSqliteOpenHelper {

    private static final String DATABASE_NAME = "movies";

    private Dao<MovieRow, Integer> movieDao = null;
    private RuntimeExceptionDao<MovieRow, Integer> movieRunDao = null;

    public DbHelper(Context context) {
        super(context,"movies", null,1, R.raw.ormlite_config);
    }

    @Override
    public void onCreate(SQLiteDatabase database, ConnectionSource connectionSource) {
        try {
            TableUtils.createTable(connectionSource, MovieRow.class);
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (java.sql.SQLException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onUpgrade(SQLiteDatabase database, ConnectionSource connectionSource, int oldVersion, int newVersion) {
        try {
            TableUtils.dropTable(connectionSource, MovieRow.class, true);
            onCreate(database, connectionSource);
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (java.sql.SQLException e) {
            e.printStackTrace();
        }
    }


    public RuntimeExceptionDao<MovieRow, Integer> getMovieRunDao() throws SQLException {
        if (movieRunDao == null) {
            movieRunDao = getRuntimeExceptionDao(MovieRow.class);
        }
        return movieRunDao;
    }
}