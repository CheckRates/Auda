/*
 *	FILE			: Database.java
 *	PROJECT			: PROG3150 - Assignment-02
 *	PROGRAMMER		: Michael Gordon, Paul Smith, Duncan Snider, Gabriel Gurgel, Amy Dayasundara
 *	FIRST VERSION	: 2020 - 03 - 08
 *	DESCRIPTION		: This class contains our Room Database definition, and abstract
 *                    references to our Data Access Objects.
 */

package com.github.mpagconestoga.mad_a01.objects;

import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.github.mpagconestoga.mad_a01.converter.Converters;
import com.github.mpagconestoga.mad_a01.dao.CategoryDao;
import com.github.mpagconestoga.mad_a01.dao.PersonDao;
import com.github.mpagconestoga.mad_a01.dao.PersonTaskDao;
import com.github.mpagconestoga.mad_a01.dao.SubtaskDao;
import com.github.mpagconestoga.mad_a01.dao.TaskDao;
//Set the tables of the class, as well as the version of the database(version increases with each database structure modification
@androidx.room.Database(entities = {Person.class, Category.class, PersonTask.class, Subtask.class, Task.class}, version = 10, exportSchema = false)
@TypeConverters({Converters.class})
public abstract class Database extends RoomDatabase {

    //Class is a singleton design pattern
    private static Database instance;

    //The Data Access Objects associated with this database
    public abstract PersonDao personDao();
    public abstract PersonTaskDao personTaskDao();
    public abstract SubtaskDao subtaskDao();
    public abstract TaskDao taskDao();
    public abstract CategoryDao categoryDao();


    /*
     *    METHOD      :     getInstance
     *    DESCRIPTION :     This checks for the exist of database, if nonexists, then create
     *                      new database
     *    PARAMETERS  :     Context context
     *    RETURNS     :     VOID
     * */
    public static synchronized Database getInstance(Context context){
        if(instance == null){
            instance = Room.databaseBuilder(context.getApplicationContext(),
                    Database.class, "database1.9") //version of the database
                    .fallbackToDestructiveMigration()
                    .allowMainThreadQueries()
                    .addCallback(roomCallback)
                    .build();
        }
        return instance;
    }

   //Set up the callback for when database is created
    private static RoomDatabase.Callback roomCallback = new RoomDatabase.Callback() {
        //Set up callback for when the database is created
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            new PopulateDbAsyncTask(instance).execute();
        }
    };

    /*
     *  CLASS: PopulateDbAsyncTask
     *  DESCRIPTION: Async task for database creation callback. Used to populate the database with some sample data
     */
    private static class PopulateDbAsyncTask extends AsyncTask<Void, Void, Void> {
        private PersonDao personDao;
        private CategoryDao categoryDao;

        /*
         *    METHOD      :     PopulateDbAsyncTask
         *    DESCRIPTION :     This is the callback for when the database is created. Sample categories and
         *                      people are added to the database
         *    PARAMETERS  :     void
         *    RETURNS     :     VOID
         */
        private PopulateDbAsyncTask(Database db) {
            personDao = db.personDao();
            categoryDao = db.categoryDao();
        }

        //This is the async function that is used to populate the database
        @Override
        protected Void doInBackground(Void... voids) {
            personDao.insert(new Person("Igor"));
            personDao.insert(new Person("Amy"));
            personDao.insert(new Person("Gabriel"));
            personDao.insert(new Person("Michael"));
            personDao.insert(new Person("Duncan"));
            personDao.insert(new Person("Paul"));
            categoryDao.insert(new Category("Landscaping","https://www.houselogic.com/wp-content/uploads/2019/05/spring-landscaping-ideas-yard.jpg?crop&resize=1280%2C853", "https://www.diynetwork.com/how-to/outdoors/landscaping/the-essential-steps-to-landscape-design-pictures"));
            categoryDao.insert(new Category("House Renovations", "https://assets.entrepreneur.com/images/misc/1500455382_5.jpg","https://www.homestoriesatoz.com/decorating/home-exterior-makeover-before-and-after.html"));
            categoryDao.insert(new Category("Winter Cleaning","https://static.houselogic.com/content/images/winter-cleaning-outside-windows-retina_retina_164f4b36e83ca415c86d623f564773d1.jpg","https://www.houselogic.com/organize-maintain/cleaning-decluttering/winter-cleaning/"));

            return null;
        }
    }
}
