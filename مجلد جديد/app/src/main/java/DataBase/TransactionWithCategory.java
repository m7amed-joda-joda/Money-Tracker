package DataBase;

import androidx.room.Embedded;
import androidx.room.Relation;

import java.io.Serializable;

public class TransactionWithCategory implements Serializable {
    @Embedded
    public Transaction transaction;

    @Relation(
            parentColumn = "categoryId",
            entityColumn = "cat_ID"
    )
    public Category category;
}
