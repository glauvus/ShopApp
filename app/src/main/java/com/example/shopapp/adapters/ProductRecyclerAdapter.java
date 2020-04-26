package com.example.shopapp.adapters;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.shopapp.MainActivity;
import com.example.shopapp.R;
import com.example.shopapp.db.Product;
import com.example.shopapp.db.Transaction;
import com.example.shopapp.ui.ShopFragment;
import com.example.shopapp.ui.UpdDelProductFragment;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

public class ProductRecyclerAdapter extends RecyclerView.Adapter<ProductRecyclerAdapter.ProductViewHolder> {

    private List<Product> products;
    private String tag;
    private Context context;

    //Δημιουργία κλάσης για το view holder
    public class ProductViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView id, name, price, quantity, pieces;
        ImageView addToCart, addPiece, removePiece;
        int pieceCounter;

        //Constructor του view holder με φόρτωση των στοιχείων της διεπαφής και ορισμό ακροατή
        public ProductViewHolder(@NonNull View view) {
            super(view);
            //Φόρτωση των στοιχείων της διεπαφής με βάση το tag
            if(tag.equals("products")) {
                id = view.findViewById(R.id.productId);
                name = view.findViewById(R.id.productName);
                price = view.findViewById(R.id.productPrice);
                quantity = view.findViewById(R.id.productQuantity);
            } else {
                name = view.findViewById(R.id.productName);
                price = view.findViewById(R.id.productPrice);
                pieces = view.findViewById(R.id.pieces);
                pieceCounter = Integer.parseInt(pieces.getText().toString());
                addToCart = view.findViewById(R.id.addToCart);
                addPiece = view.findViewById(R.id.addPiece);
                removePiece = view.findViewById(R.id.removePiece);
                //Ακροατής για το button της προσθήκης στο καλάθι
                addToCart.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //Ανανέωση του τρέχοντος fragment ώστε να ενημερωθούν οι τιμές από τη ΒΔ
                        Fragment frg = MainActivity.fragmentManager.findFragmentById(R.id.fragment_container);
                        MainActivity.fragmentManager.beginTransaction().detach(frg).attach(frg).commit();
                        //Λήψη του αποθέματος του προϊόντος
                        int productStock = products.get(getLayoutPosition()).getStock();
                        //Έλεγχος για υπέρβαση του αποθέματος και εμφάνιση κατάλληλου μηνύματος
                        if(products.get(getLayoutPosition()).getStock()-pieceCounter<0) {
                            Toast.makeText(view.getContext(), R.string.toast_stock, Toast.LENGTH_LONG).show();
                            return;
                        }
                        //Αφαίρεση της επιλεγμένης ποσότητας από το stock
                        MainActivity.shoppingAppDb.shopAppDao().
                                updateStock(-pieceCounter, products.get(getLayoutPosition()).getId());
                        /*Δημιουργία νέας συναλλαγής με το id προϊόντος, την επιλεγμένη ποσότητα,
                         την default τιμή 1 για το clientId και το cartNumber*/
                        Transaction transaction = new Transaction();
                        transaction.setProductId(products.get(getLayoutPosition()).getId());
                        transaction.setProductQuantity(pieceCounter);
                        transaction.setClientId(1);
                        transaction.setCartNumber(1);
                        //Προσθήκη της συναλλαγής στη ΒΔ
                        MainActivity.shoppingAppDb.shopAppDao().addTransaction(transaction);
                        Toast.makeText(view.getContext(), R.string.toast_add_cart, Toast.LENGTH_LONG).show();
                    }
                });
                //Ακροατής για το button της αύξησης ποσότητας
                addPiece.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(pieceCounter<9) {
                            pieceCounter++;
                            pieces.setText(String.valueOf(pieceCounter));
                        }
                    }
                });
                //Ακροατής για το button της μείωσης ποσότητας
                removePiece.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(pieceCounter>1) {
                            pieceCounter--;
                            pieces.setText(String.valueOf(pieceCounter));
                        }
                    }
                });
            }

            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if(tag.equals("products")) {
                //Λήψη της θέσης στην οποία πυροδοτήθηκε το γεγονός
                Product product = products.get(getLayoutPosition());
                //Δημιουργία bundle και φόρτωμα παραμέτρων
                Bundle bundle = new Bundle();
                bundle.putInt("id", product.getId());
                bundle.putString("name", product.getName());
                bundle.putDouble("price", product.getPrice());
                bundle.putInt("stock", product.getStock());

                //Δημιουργία στιγμιοτύπου UpDelProductFragment και πέρασμα παραμέτρων
                UpdDelProductFragment upDelProductFragment = new UpdDelProductFragment();
                upDelProductFragment.setArguments(bundle);

                //Μετάβαση στο UpDelProductFragment
                MainActivity.fragmentManager.beginTransaction().replace(R.id.fragment_container,
                        upDelProductFragment).addToBackStack(null).commit();
            }
        }
    }

    //Constructor του ProductRecyclerAdapter
    public ProductRecyclerAdapter(List<Product> products, String tag) {
        this.products = products;
        this.tag = tag;
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if(tag.equals("products")) {
            //Φόρτωση της διεπαφής του recycler view
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_row_product,
                    parent, false);
            return new ProductViewHolder(view);
        }
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_row_shop,
                parent, false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        //Λήψη του προϊόντος με βάση τη θέση του στη λίστα
        Product product = products.get(position);

        //Φόρτωση στοιχείων του προϊόντος στη διεπαφή με βάση το tag
        if(tag.equals("products")) {
            holder.id.setText("#"+product.getId());
            holder.name.setText(product.getName());
            holder.price.setText(product.getPrice()+" €");
            holder.quantity.setText(product.getStock()+" left");
        } else {
            holder.name.setText(product.getName());
            holder.price.setText(product.getPrice()+" €");
        }

    }

    @Override
    public int getItemCount() {
        return products.size();
    }

}
