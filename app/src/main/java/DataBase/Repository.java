package DataBase;

import android.app.Application;

import androidx.lifecycle.LiveData;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import Activities.DoubleValueListener;
import Activities.SubmitValueCategoryId;

public class Repository {
    TransactionDao transactionDao;
    CategoryDao categoryDao;

    public Repository(Application application){
        MyDatabase myDatabase = MyDatabase.getDatabase(application);
        transactionDao = myDatabase.transactionDao();
        categoryDao = myDatabase.categoryDao();
    }



    public void insertTransaction(Transaction transaction)
    {
        MyDatabase.databaseWriterExecutor.execute(() -> {

            transactionDao.insertTransaction(transaction);
        });

    }
    public void updateTransaction(Transaction transaction){
        MyDatabase.databaseWriterExecutor.execute(() -> {

            transactionDao.updateTransaction(transaction);
        });
    }

    public void deleteTransaction(Transaction transaction){
        MyDatabase.databaseWriterExecutor.execute(() -> {

            transactionDao.deleteTransaction(transaction);
        });
    }

    public LiveData<List<Transaction>> getAllTransactions(){
        return transactionDao.getAllTransactions();
    }

    public LiveData<Transaction>  getTransactionsById(int id){
        return transactionDao.getTransactionsById(id);
    }

    public LiveData<List<Transaction>>  getAllTransactionsByType(int type){
        return transactionDao.getAllTransactionsByType(type);
    }


    public LiveData<List<Transaction> >  getAllTransactionsByDate(Date startDate, Date endDate){
        return transactionDao.getAllTransactionsByDate(startDate, endDate);
    }

    public LiveData<List<Transaction> >  getAllTransactionsByDateCategoryType(Date startDate, Date endDate, String type, int catId){
        return transactionDao.getAllTransactionsByDateCategoryType(startDate, endDate, type, catId);
    }

    public LiveData<List<Transaction> >  getAllTransactionsByDateType(Date startDate, Date endDate, String type){
        return transactionDao.getAllTransactionsByDateType(startDate, endDate, type);
    }




    public LiveData<List<Transaction> >  getAllTransactionsByCategory(int categoryId){
        return transactionDao.getAllTransactionsByCategory(categoryId);
    }


    public LiveData<List<CategorySum>> getCategorySummary(String type){

        return transactionDao.getCategorySummary(type);
    }

    public LiveData<List<MonthSum>> getMonthlyExpenses(String type){

        return transactionDao.getMonthlyExpenses(type);
    }

    public void getTotalIncome(String incomeType, DoubleValueListener listener){
        MyDatabase.databaseWriterExecutor.execute(() -> {
            listener.valueSubmit(transactionDao.getTotalIncome(incomeType));
        });
    }

    public void getTotalExpense(String expenseType, DoubleValueListener listener){
        MyDatabase.databaseWriterExecutor.execute(() -> {
            listener.valueSubmit(transactionDao.getTotalExpense(expenseType));
        });
    }





    public LiveData<List<TransactionWithCategory>> getAllTransactionsWithCategory(){
        return transactionDao.getAllTransactionsWithCategory();
    }






    public void insertCategory(Category category){

        MyDatabase.databaseWriterExecutor.execute(() -> {
            categoryDao.insertCategory(category);
        });
    }

    public void updateCategory(Category category){

        MyDatabase.databaseWriterExecutor.execute(() -> {
            categoryDao.updateCategory(category);
        });
    }

    public void deleteCategory(Category category){


        MyDatabase.databaseWriterExecutor.execute(() -> {
            categoryDao.deleteCategory(category);

        });
    }


    public LiveData< List<Category> >  getAllCategories(){
        return categoryDao.getAllCategories();
    }

    public void getCategoryById (int categoryId, SubmitValueCategoryId listener){
        MyDatabase.databaseWriterExecutor.execute(() -> {
            listener.submitValue(categoryDao.getCategoryById(categoryId));

        });

    }





    public int isCategoryExists(String name){
        return categoryDao.isCategoryExists(name);
    }




}
