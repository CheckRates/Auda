/*
 *	FILE			: CategoryRepository.java
 *	PROJECT			: PROG3150 - Assignment-02
 *	PROGRAMMER		: Michael Gordon, Paul Smith, Duncan Snider, Gabriel Gurgel, Amy Dayasundara
 *	FIRST VERSION	: 2020 - 03 - 08
 *	DESCRIPTION		: This class is the data repository for Category. It interacts with the
 *                    Category Data Access Object and stores data retrieved from it.
 */

package com.github.mpagconestoga.mad_a01.repositories;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;
import androidx.room.Delete;

import com.github.mpagconestoga.mad_a01.dao.CategoryDao;
import com.github.mpagconestoga.mad_a01.objects.Category;
import com.github.mpagconestoga.mad_a01.objects.Database;

import java.util.List;

/*
 *  CLASS: CategoryRepository
 *  DESCRIPTION: This class is a model in the MVVM framework. It used to hold data about the categories.
 *              Using the category DAO, it retrieves data from the database, sending it to the view model.
 */
public class CategoryRepository {

    private CategoryDao categoryDao;

    //Constructor used to get database instance, and initialize the DAO
    public CategoryRepository(Application application) {
        Database database = Database.getInstance(application);
        categoryDao = database.categoryDao();

    }

    /*
     *    METHOD      :     insert
     *    DESCRIPTION :     Inserts a new Category into the database
     *    PARAMETERS  :     Category category
     *    RETURNS     :     VOID
     * */
    public void insert(Category category) {
        new InsertCategoryAsyncTask(categoryDao).execute(category);
    }

    /*
     *    METHOD      :     update
     *    DESCRIPTION :     Update a Category in the database
     *    PARAMETERS  :     Category category
     *    RETURNS     :     VOID
     * */
    public void update(Category category) {
        new UpdateCategoryAsyncTask(categoryDao).execute(category);
    }

    /*
     *    METHOD      :     delete
     *    DESCRIPTION :     Delete a Category from the database
     *    PARAMETERS  :     Category category
     *    RETURNS     :     VOID
     * */
    public void delete(Category category) {
        new DeleteCategoryAsyncTask(categoryDao).execute(category);
    }

    /*
     *    METHOD      :     getBackgroundURL
     *    DESCRIPTION :     Returns the background image url of a category, based on the category name
     *    PARAMETERS  :     String catName
     *    RETURNS     :     VOID
     * */
    public String getBackgroundURL(String catName){
        return categoryDao.getBackground(catName);
    }

    /*
     *    METHOD      :     getWebURL
     *    DESCRIPTION :     Returns the web help url of a category, based on the category name
     *    PARAMETERS  :     String catName
     *    RETURNS     :     VOID
     * */
    public String getWebURL(String catName){
        return categoryDao.getWebUrl(catName);
    }

    /*
     *    METHOD      :     deleteAllCategories
     *    DESCRIPTION :     Delete all Categories from the database
     *    PARAMETERS  :     NONE
     *    RETURNS     :     VOID
     * */
    public void deleteAllCategories() {
        new DeleteAllCategoriesAsyncTask(categoryDao).execute();
    }

    /*
     *    METHOD      :     getAllCategories
     *    DESCRIPTION :     Get a list of all the categories in the database
     *    PARAMETERS  :     NONE
     *    RETURNS     :     List<Category> ->list of all categories
     * */
    public List<Category> getAllCategories() {
        return categoryDao.getAllCategories();
    }


    //This class is an async task used to access the database in order to insert a new category
    private static class InsertCategoryAsyncTask extends AsyncTask<Category, Void, Void> {

        private CategoryDao categoryDao;

        private InsertCategoryAsyncTask(CategoryDao categoryDao) {
            this.categoryDao = categoryDao;
        }

        @Override
        protected Void doInBackground(Category... categories) {
            categoryDao.insert(categories[0]);
            return null;
        }
    }

    //This class is an async task used to access the database in order to update a category
    private static class UpdateCategoryAsyncTask extends AsyncTask<Category, Void, Void> {

        private CategoryDao categoryDao;

        private UpdateCategoryAsyncTask(CategoryDao categoryDao) {
            this.categoryDao = categoryDao;
        }

        @Override
        protected Void doInBackground(Category... categories) {
            categoryDao.update(categories[0]);
            return null;
        }
    }

    //This class is an async task used to access the database in order to delete a category
    private static class DeleteCategoryAsyncTask extends AsyncTask<Category, Void, Void> {

        private CategoryDao categoryDao;

        private DeleteCategoryAsyncTask(CategoryDao categoryDao) {
            this.categoryDao = categoryDao;
        }

        @Override
        protected Void doInBackground(Category... categories) {
            categoryDao.delete(categories[0]);
            return null;
        }
    }

    //This class is an async task used to access the database in order to delete all categories from the database
    private static class DeleteAllCategoriesAsyncTask extends AsyncTask<Void, Void, Void> {

        private CategoryDao categoryDao;

        private DeleteAllCategoriesAsyncTask(CategoryDao categoryDao) {
            this.categoryDao = categoryDao;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            categoryDao.deleteAllCategories();
            return null;
        }
    }

}
