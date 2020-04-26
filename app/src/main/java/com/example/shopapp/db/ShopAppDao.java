package com.example.shopapp.db;

import java.util.List;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

@Dao
public interface ShopAppDao {
    //Standard inserts
    @Insert
    void addClient(Client client);
    @Insert
    void addProduct(Product product);
    @Insert
    void addTransaction(Transaction transaction);

    //Standard deletes
    @Delete
    void deleteClient(Client client);
    @Delete
    void deleteProduct(Product product);
    @Delete
    void deleteTransaction(Transaction transaction);

    //Standard updates
    @Update
    void updateClient(Client client);
    @Update
    void updateProduct(Product product);
    @Update
    void updateTransaction(Transaction transaction);

    //Queries on Client
    @Query("select * from client where not id=1")
    List<Client> getClients();
    @Query("select id from client order by id desc limit 1")
    int getCurrentClientId();
    @Query("select * from client where firstName=:firstName and lastName=:lastName and address=:address")
    List<Client> getClients(String firstName, String lastName, String address);

    //Queries on Product
    @Query("select * from product")
    List<Product> getProducts();
    @Query("select * from product where id=:productId")
    Product getProduct(int productId);
    @Query("select name from product")
    List<String> getProductsNames();
    @Query("select id from product where name=:productName")
    int getProductId(String productName);
    @Query("update product set stock=stock+:quantity where id=:productId")
    void updateStock(int quantity, int productId);

    //Queries on Transaction
    @Query("select * from `transaction`")
    List<Transaction> getTransactions();
    @Query("select * from `transaction` where cartNumber=1")
    List<Transaction> getCart();
    @Query("select * from `transaction` where productId=:productId")
    List<Transaction> getProductTransactions(int productId);
    @Query("update `transaction` set clientId=:clientId, cartNumber=:cartNumber where clientId=1")
    void updateTransaction(int clientId, int cartNumber);
    @Query("select cartNumber from `transaction` order by cartNumber desc limit 1")
    int getLastCartNumber();

}
