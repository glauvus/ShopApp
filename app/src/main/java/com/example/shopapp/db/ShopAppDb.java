package com.example.shopapp.db;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {Client.class, Product.class, Transaction.class}, version=1)
public abstract class ShopAppDb extends RoomDatabase {
    public abstract ShopAppDao shopAppDao();
}
