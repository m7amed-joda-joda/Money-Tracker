package DataBase;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.Date;
import java.util.List;

import Activities.DoubleValueListener;
import Activities.SubmitValueCategoryId;

public class MyViewModel extends AndroidViewModel {

    Repository repository;
    public MyViewModel(@NonNull Application application) {
        super(application);
        repository = new Repository(application);
    }

    public void insertTransaction(Transaction transaction)
    {

        repository.insertTransaction(transaction);

    }
    public void updateTransaction(Transaction transaction){
            repository.updateTransaction(transaction);
    }

    public void deleteTransaction(Transaction transaction){
        repository.deleteTransaction(transaction);
    }

    public LiveData<List<Transaction>> getAllTransactions(){
        return repository.getAllTransactions();
    }


    public LiveData<Transaction>  getTransactionsById(int id){
        return repository.getTransactionsById(id);
    }


    public LiveData<List<Transaction> >  getAllTransactionsByType(int type){
        return repository.getAllTransactionsByType(type);
    }


    public LiveData<List<Transaction> >  getAllTransactionsByDate(Date startDate, Date endDate){
        return repository.getAllTransactionsByDate(startDate, endDate);
    }


    public LiveData<List<Transaction> >  getAllTransactionsByDateCategoryType(Date startDate, Date endDate, String type, int catId){
        return repository.getAllTransactionsByDateCategoryType(startDate, endDate, type, catId);
    }


    public LiveData<List<Transaction> >  getAllTransactionsByDateType(Date startDate, Date endDate, String type){
        return repository.getAllTransactionsByDateType(startDate, endDate, type);
    }


    public LiveData<List<Transaction> >  getAllTransactionsByCategory(int categoryId){
        return repository.getAllTransactionsByCategory(categoryId);
    }


    public LiveData<List<CategorySum>> getCategorySummary(String type){

        return repository.getCategorySummary(type);
    }

    public LiveData<List<MonthSum>> getMonthlyExpenses(String type){

        return repository.getMonthlyExpenses(type);
    }


    public void getTotalIncome(String incomeType, DoubleValueListener listener){
       repository.getTotalIncome(incomeType, listener);
    }

    public void getTotalExpense(String expenseType, DoubleValueListener listener){
        repository.getTotalExpense(expenseType, listener);
    }


    public LiveData<List<TransactionWithCategory>> getAllTransactionsWithCategory(){
        return repository.getAllTransactionsWithCategory();
    }







//--------------------------------------------------


    public void insertCategory(Category category){
        repository.insertCategory(category);
    }

    public void updateCategory(Category category){
        repository.updateCategory(category);
    }

    public void deleteCategory(Category category){
        repository.deleteCategory(category);
    }


    public LiveData<List<Category>>  getAllCategories(){
        return repository.getAllCategories();
    }

    public void  getCategoryById (int categoryId, SubmitValueCategoryId listener){
         repository.getCategoryById(categoryId, listener);
    }

    public int isCategoryExists(String name){
        return repository.isCategoryExists(name);
    }




}
