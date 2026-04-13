package DataBase;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity
public class Category implements Serializable {
    @ColumnInfo(name = "cat_ID")
    @PrimaryKey(autoGenerate = true)
    private int id;
    @NonNull
    private String name;
    @NonNull
    private int icon;
    @NonNull
    private String color;

    public Category() {
    }

    public Category(int id, @NonNull String name, @NonNull int icon, @NonNull String color) {
        this.id = id;
        this.name = name;
        this.icon = icon;
        this.color = color;
    }

    public Category(@NonNull String name, @NonNull int icon, @NonNull String color) {
        this.name = name;
        this.icon = icon;
        this.color = color;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @NonNull
    public String getName() {
        return name;
    }

    public void setName(@NonNull String name) {
        this.name = name;
    }

    @NonNull
    public int getIcon() {
        return icon;
    }

    public void setIcon(@NonNull int icon) {
        this.icon = icon;
    }

    @NonNull
    public String getColor() {
        return color;
    }

    public void setColor(@NonNull String color) {
        this.color = color;
    }
}
