package DataBase;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.TypeConverters;
import androidx.room.Update;

import java.util.Date;
import java.util.List;

@Dao
@TypeConverters({DataConverter.class})
public interface TransactionDao {

    @Insert
    void insertTransaction(Transaction transaction);
    @Update
    void updateTransaction(Transaction transaction);

    @Delete
    void deleteTransaction(Transaction transaction);

    @Query("SELECT * FROM transactions ORDER BY date DESC")
    LiveData< List<Transaction> >  getAllTransactions();

    @Query("SELECT * FROM transactions WHERE id =:id")
    LiveData<Transaction>  getTransactionsById(int id);

    @Query("SELECT * FROM transactions WHERE type =:type ORDER BY date DESC")
    LiveData<List<Transaction>>  getAllTransactionsByType(int type);


    @Query("SELECT * FROM transactions WHERE date >=:startDate AND date <=:endDate  ORDER BY date DESC")
    LiveData<List<Transaction> >  getAllTransactionsByDate(Date startDate, Date endDate);

    @Query("SELECT * FROM transactions WHERE date >=:startDate AND date <=:endDate AND type=:type AND categoryId=:catId  ORDER BY date DESC")
    LiveData<List<Transaction> >  getAllTransactionsByDateCategoryType(Date startDate, Date endDate, String type, int catId);



    @Query("SELECT * FROM transactions WHERE date >=:startDate AND date <=:endDate AND type=:type ORDER BY date DESC")
    LiveData<List<Transaction> >  getAllTransactionsByDateType(Date startDate, Date endDate, String type);


    @Query("SELECT * FROM transactions WHERE categoryId =:categoryId ORDER BY date DESC")
    LiveData<List<Transaction> >  getAllTransactionsByCategory(int categoryId);


    @Query("SELECT categoryId, SUM(amount) as total FROM transactions WHERE type = :type GROUP BY categoryId")
    LiveData<List<CategorySum>> getCategorySummary(String type);


    @Query("SELECT strftime('%m', date/1000, 'unixepoch') as month, SUM(amount) as total FROM transactions WHERE type=:type GROUP BY month")
    LiveData<List<MonthSum>> getMonthlyExpenses(String type);



    @Query("SELECT SUM(amount) FROM transactions WHERE type = :incomeType")
    double getTotalIncome(String incomeType);

    @Query("SELECT SUM(amount) FROM transactions WHERE type = :expenseType")
    double getTotalExpense(String expenseType);


    @Query("SELECT * FROM transactions")
    LiveData<List<TransactionWithCategory>> getAllTransactionsWithCategory();


}
