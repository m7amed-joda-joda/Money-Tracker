package Activities;

import DataBase.Category;
import DataBase.Transaction;
import DataBase.TransactionWithCategory;

public interface OnItemClick_transaction {
    void onClick(TransactionWithCategory transactionWithCategory);
}