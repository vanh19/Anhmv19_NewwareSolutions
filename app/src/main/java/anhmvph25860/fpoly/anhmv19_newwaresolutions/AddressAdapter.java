package anhmvph25860.fpoly.anhmv19_newwaresolutions;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class AddressAdapter extends RecyclerView.Adapter<AddressAdapter.AddressViewHolder> {
    private Context context;
    private List<String> addressList;
    private List<Double> latList;
    private List<Double> lngList;

    public AddressAdapter(Context context, List<String> addressList, List<Double> latList, List<Double> lngList) {
        this.context = context;
        this.addressList = addressList;
        this.latList = latList;
        this.lngList = lngList;
    }

    @NonNull
    @Override
    public AddressAdapter.AddressViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_address, parent, false);
        return new AddressViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AddressAdapter.AddressViewHolder holder, int position) {
        String address = addressList.get(position);
        double lat = latList.get(position);
        double lng = lngList.get(position);
        holder.tvAddress.setText(address);

        holder.ivSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveAddress(address);
            }
        });
        holder.ivMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGoogleMaps(lat, lng);
            }
        });
    }

    @Override
    public int getItemCount() {
        return addressList.size();
    }

    private void openGoogleMaps(double lat, double lng) {
        Uri gmmIntentUri = Uri.parse("geo:0,0?q=" + lat + "," + lng + "(" + "Location" + ")");
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
        mapIntent.setPackage("com.google.android.apps.maps");
        if (mapIntent.resolveActivity(context.getPackageManager()) != null) {
            context.startActivity(mapIntent);
        }
    }
    private void saveAddress(String address){
        SharedPreferences sharedPreferences = context.getSharedPreferences("SavedAddresses", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("lastAddress", address);
        editor.apply();
        Toast.makeText(context, "Đã lưu địa chỉ: " + address, Toast.LENGTH_SHORT).show();
    }

    public class AddressViewHolder extends RecyclerView.ViewHolder {
        TextView tvAddress;
        ImageView ivMap;
        ImageView ivSave;
        public AddressViewHolder(@NonNull View itemView) {
            super(itemView);
            tvAddress = itemView.findViewById(R.id.tvAddress);
            ivSave = itemView.findViewById(R.id.ivSave);
            ivMap = itemView.findViewById(R.id.ivMap);
        }
    }
}
