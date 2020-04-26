package com.example.shopapp.adapters;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.shopapp.MainActivity;
import com.example.shopapp.R;
import com.example.shopapp.db.Client;
import com.example.shopapp.ui.UpDelClientFragment;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ClientRecyclerAdapter extends RecyclerView.Adapter<ClientRecyclerAdapter.ClientViewHolder> {
    private List<Client> clients;

    //Δημιουργία κλάσης για το view holder
    public class ClientViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView clientId, clientFirstName, clientLastName, clientAddress;

        //Constructor του view holder με φόρτωση των στοιχείων της διεπαφής και ορισμό ακροατή
        public ClientViewHolder(@NonNull View view) {
            super(view);
            clientId = view.findViewById(R.id.clientId);
            clientFirstName = view.findViewById(R.id.clientFirstName);
            clientLastName = view.findViewById(R.id.clientLastName);
            clientAddress = view.findViewById(R.id.clientAddress);

            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            //Λήψη της θέσης στην οποία πυροδοτήθηκε το γεγονός
            Client client = clients.get(getLayoutPosition());
            //Δημιουργία bundle και φόρτωμα παραμέτρων
            Bundle bundle = new Bundle();
            bundle.putInt("id", client.getId());
            bundle.putString("firstName", client.getFirstName());
            bundle.putString("lastName", client.getLastName());
            bundle.putString("address", client.getAddress());

            //Δημιουργία στιγμιοτύπου UpDelClientFragment και πέρασμα παραμέτρων
            UpDelClientFragment upDelClientFragment = new UpDelClientFragment();
            upDelClientFragment.setArguments(bundle);

            //Μετάβαση στο UpDelClientFragment
            MainActivity.fragmentManager.beginTransaction().replace(R.id.fragment_container,
                    upDelClientFragment).addToBackStack(null).commit();
        }
    }

    //Constructor του ClientRecyclerAdapter
    public ClientRecyclerAdapter(List<Client> clients) {
        this.clients = clients;
    }

    @NonNull
    @Override
    public ClientRecyclerAdapter.ClientViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //Φόρτωση της διεπαφής του recycler view
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_row_client,
                parent, false);
        return new ClientViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ClientRecyclerAdapter.ClientViewHolder holder, int position) {
        //Λήψη του πελάτη με βάση τη θέση του στη λίστα
        Client client = clients.get(position);
        //Φόρτωση στοιχείων του πελάτη στη διεπαφή
        holder.clientId.setText("#"+client.getId());
        holder.clientFirstName.setText(client.getFirstName());
        holder.clientLastName.setText(client.getLastName());
        holder.clientAddress.setText(client.getAddress());
    }

    @Override
    public int getItemCount() {
        return clients.size();
    }
}
