package DataBase;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import java.io.Serializable;
import java.util.Date;

@Entity(
        tableName = "transactions",
        foreignKeys = {
                @ForeignKey(
                        entity = Category.class,
                        parentColumns = {"cat_ID"},
                        childColumns = {"categoryId"},
                        onDelete = ForeignKey.CASCADE ,
                        onUpdate = ForeignKey.CASCADE
                )
        }
)
@TypeConverters({DataConverter.class})
public class Transaction implements Serializable {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private double amount;
    @NonNull
    private String type;
    @NonNull
    private int categoryId;
    @NonNull
    private Date date;
    @NonNull
    private String note;


    public Transaction(int id, double amount, String type, int categoryId, Date date, String note) {
        this.id = id;
        this.amount = amount;
        this.type = type;
        this.categoryId = categoryId;
        this.date = date;
        this.note = note;
    }

    public Transaction(double amount, String type, int categoryId, Date date, String note) {
        this.amount = amount;
        this.type = type;
        this.categoryId = categoryId;
        this.date = date;
        this.note = note;
    }

    public Transaction() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
}

